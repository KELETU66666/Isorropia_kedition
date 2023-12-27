package fr.wind_blade.isorropia.common.items.baubles;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import fr.wind_blade.isorropia.common.Common;
import fr.wind_blade.isorropia.common.network.MirrorMessage;
import java.util.List;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.blocks.BlocksTC;



public class ItemBaubleMirror extends Item implements IBauble {
    private BaubleType type;

    public ItemBaubleMirror(BaubleType type) {
        this.type = type;
    }

    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.RARE;
    }


    public BaubleType getBaubleType(ItemStack itemstack) {
        return this.type;
    }



    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (world.getBlockState(pos).getBlock() == BlocksTC.mirror) {
            Common.INSTANCE.sendToServer(new MirrorMessage(pos));
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }


    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack par1ItemStack) {
        return par1ItemStack.hasTagCompound();
    }


    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound != null)
            tooltip.add(I18n.format("ir.mirrorLink", compound.getInteger("linkX"), compound.getInteger("linkY"), compound.getInteger("linkZ"), compound.getString("dimname")));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}