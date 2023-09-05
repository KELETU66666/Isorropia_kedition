// Decompiled with: CFR 0.152
// Class Version: 8
package fr.wind_blade.isorropia.common.libs.helpers;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import fr.wind_blade.isorropia.common.Common;
import fr.wind_blade.isorropia.common.blocks.BlocksIS;
import fr.wind_blade.isorropia.common.capabilities.LivingCapability;
import fr.wind_blade.isorropia.common.celestial.CelestialBody;
import fr.wind_blade.isorropia.common.celestial.ICelestialBody;
import fr.wind_blade.isorropia.common.config.ConfigContainment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.lib.utils.InventoryUtils;

public class IsorropiaHelper {
    public static GameProfile AUTHOR_GAMEPROFILE = new GameProfile(UUID.fromString("e5528ce9-e498-40e1-833b-2dceee510efb"), "Wind_Blade");
    public static final String ENTITY_ID = "ENTITY_ID";
    public static final String ENTITY_UUID = "ENTITY_UUID";
    public static final String ENTITY_DATA = "ENTITY_DATA";
    public static final Map<UUID, Float> contain = new HashMap<UUID, Float>();
    public static final DataSerializer<Aspect> ASPECT = new DataSerializer<Aspect>(){

        public DataParameter<Aspect> createKey(int id) {
            return new DataParameter(id, (DataSerializer)this);
        }

        public void write(PacketBuffer buf, Aspect value) {
            buf.writeString(value != null ? value.getTag() : "");
        }

        public Aspect read(PacketBuffer buf) throws IOException {
            return Aspect.getAspect(buf.readString(Short.MAX_VALUE));
        }

        public Aspect copyValue(Aspect value) {
            return value;
        }
    };

    public static EntityPlayer getOwner(EntityLiving living) {
        EntityPlayer player = living instanceof EntityTameable ? (EntityPlayer)((EntityTameable)living).getOwner() : living.world.getPlayerEntityByUUID(((LivingCapability)Common.getCap((EntityLivingBase)living)).uuidOwner);
        return player;
    }

    public static boolean canEntityBeJarred(EntityLiving living) {
        switch (ConfigContainment.TYPE.getName()) {
            case "whitelist": {
                return ConfigContainment.ENTRIES.contains(living.getClass());
            }
            case "blacklist": {
                return !ConfigContainment.ENTRIES.contains(living.getClass());
            }
        }
        return false;
    }

    public static boolean doPlayerHaveJar(EntityPlayer player, boolean simulate) {
        boolean flag;
        boolean bl = flag = InventoryUtils.isPlayerCarryingAmount(player, new ItemStack(BlocksTC.jarNormal, 1), false) || player.isCreative();
        if (!simulate && !flag) {
            player.sendMessage(new TextComponentString(TextFormatting.ITALIC + "" + TextFormatting.GRAY + I18n.translateToLocal((String)"isorropia.containment.jar")));
        }
        return flag;
    }

    public static boolean containEntity(EntityLivingBase owner, EntityLivingBase target, float amount) {
        float progression = (contain.containsKey(target.getUniqueID()) ? contain.get(target.getUniqueID()).floatValue() : 0.0f) + amount;
        contain.put(target.getUniqueID(), Float.valueOf(progression));
        return progression > target.getHealth() * 20.0f;
    }

    public static void playerJarEntity(EntityPlayer player, EntityLiving target) {
        if (InventoryUtils.consumePlayerItem(player, Item.getItemFromBlock((Block)BlocksTC.jarNormal), 0) || player.isCreative()) {
            ItemStack jar = new ItemStack(BlocksIS.blockJarSoul);
            jar.setTagCompound(IsorropiaHelper.livingToNBT(target));
            if (!player.inventory.addItemStackToInventory(jar)) {
                player.entityDropItem(jar, 1.0f);
            }
            contain.remove(target.getUniqueID());
        }
    }

    public static NBTTagCompound livingToNBT(EntityLiving living) {
        NBTTagCompound nbt = IsorropiaHelper.livingToNBTNoWorld(living);
        living.world.removeEntity(living);
        return nbt;
    }

    public static NBTTagCompound livingToNBTNoWorld(EntityLiving living) {
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagCompound entityData = new NBTTagCompound();
        living.writeToNBTOptional(entityData);
        nbt.setTag(ENTITY_DATA, entityData);
        return nbt;
    }

    public static EntityLiving nbtToLiving(NBTTagCompound nbt, World world, BlockPos pos) {
        EntityLiving living = (EntityLiving)EntityList.createEntityFromNBT((NBTTagCompound)nbt.getCompoundTag(ENTITY_DATA), (World)world);
        if (living != null && !living.isDead) {
            living.moveToBlockPosAndAngles(pos, 0.0f, 0.0f);
            living.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
            world.spawnEntity(living);
        }
        return living;
    }

    public static NBTTagCompound livingBaseToStatue(EntityLivingBase base) {
        NBTTagCompound nbt = new NBTTagCompound();
        if (base instanceof EntityLiving) {
            nbt = IsorropiaHelper.livingToNBTNoWorld((EntityLiving)base);
            nbt.setString(ENTITY_ID, "LIVING");
        } else if (base instanceof EntityPlayer) {
            NBTTagCompound profile = new NBTTagCompound();
            NBTUtil.writeGameProfile((NBTTagCompound)profile, (GameProfile)((EntityPlayer)base).getGameProfile());
            nbt.setString(ENTITY_ID, "PLAYER");
            nbt.setTag(ENTITY_DATA, profile);
        }
        return nbt;
    }

    public static void profileToStatueData(NBTTagCompound nbt, GameProfile profile) {
        NBTTagCompound data = new NBTTagCompound();
        NBTUtil.writeGameProfile((NBTTagCompound)data, (GameProfile)profile);
        nbt.setTag(ENTITY_DATA, data);
        nbt.setString(ENTITY_ID, "PLAYER");
    }

    @SideOnly(value=Side.CLIENT)
    public static ResourceLocation loadSkin(final MinecraftProfileTexture profileTexture, final MinecraftProfileTexture.Type textureType, final @Nullable SkinManager.SkinAvailableCallback skinAvailableCallback) {
        final ResourceLocation resourcelocation = new ResourceLocation("skins/" + profileTexture.getHash());
        ITextureObject itextureobject = Minecraft.getMinecraft().renderEngine.getTexture(resourcelocation);
        if (itextureobject != null) {
            if (skinAvailableCallback != null) {
                skinAvailableCallback.skinAvailable(textureType, resourcelocation, profileTexture);
            }
        } else {
            File file1 = new File(new File(Minecraft.getMinecraft().gameDir, "skins"), profileTexture.getHash().length() > 2 ? profileTexture.getHash().substring(0, 2) : "xx");
            File file2 = new File(file1, profileTexture.getHash());
            final ImageBufferDownload iimagebuffer = textureType == MinecraftProfileTexture.Type.SKIN ? new ImageBufferDownload() : null;
            ThreadDownloadImageData threaddownloadimagedata = new ThreadDownloadImageData(file2, profileTexture.getUrl(), DefaultPlayerSkin.getDefaultSkinLegacy(), new IImageBuffer(){

                public BufferedImage parseUserSkin(BufferedImage image) {
                    if (iimagebuffer != null) {
                        image = iimagebuffer.parseUserSkin(image);
                    }
                    return image;
                }

                public void skinAvailable() {
                    if (iimagebuffer != null) {
                        iimagebuffer.skinAvailable();
                    }
                    if (skinAvailableCallback != null) {
                        skinAvailableCallback.skinAvailable(textureType, resourcelocation, profileTexture);
                    }
                }
            });
            Minecraft.getMinecraft().renderEngine.loadTexture(resourcelocation, threaddownloadimagedata);
        }
        return resourcelocation;
    }

    public static Entity getEntityByUUID(UUID uuid, World world) {
        for (Entity e : world.loadedEntityList) {
            if (!e.getPersistentID().equals(uuid)) continue;
            return e;
        }
        return null;
    }

    public static ICelestialBody getCurrentCelestialBody(World world) {
        return CelestialBody.isDay(world) ? CelestialBody.SUN : CelestialBody.MOON;
    }

    static {
        DataSerializers.registerSerializer(ASPECT);
    }
}
