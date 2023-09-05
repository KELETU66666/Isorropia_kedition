 package fr.wind_blade.isorropia.common.lenses;
 import baubles.api.BaublesApi;
 import fr.wind_blade.isorropia.common.IsorropiaAPI;
 import net.minecraft.client.Minecraft;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.item.Item;
 import net.minecraft.item.ItemStack;
 import net.minecraft.nbt.NBTTagCompound;
 import net.minecraft.util.ResourceLocation;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 import thaumcraft.api.items.IRevealer;
 
 public class LensManager {
   @SideOnly(Side.CLIENT)
   public static void putLens(EntityPlayer player, Lens lens, LENSSLOT type) {
/*  18 */     ItemStack revealer = getRevealer(player);
 
     
/*  21 */     Lens old = revealer.hasTagCompound() ? (Lens)IsorropiaAPI.lensRegistry.getValue(new ResourceLocation(revealer.getTagCompound().getString(type.getName()))) : null;
 
     
/*  24 */     if (old != null)
/*  25 */       old.handleRemoval(player.world, player); 
/*  26 */     setLens(getRevealer((EntityPlayer)(Minecraft.getMinecraft()).player), lens, type);
   }
   
   public static Lens getLens(ItemStack revealer, LENSSLOT type) {
/*  30 */     if (revealer.hasTagCompound() && revealer.getTagCompound().hasKey(type.getName()))
/*  31 */       return (Lens)IsorropiaAPI.lensRegistry
/*  32 */         .getValue(new ResourceLocation(revealer.getTagCompound().getString(type.getName()))); 
/*  33 */     return null;
   }
   
   public static void setLens(ItemStack revealer, Lens lens, LENSSLOT type) {
/*  37 */     if (lens == null)
       return; 
/*  39 */     if (!revealer.hasTagCompound())
/*  40 */       revealer.setTagCompound(new NBTTagCompound()); 
/*  41 */     NBTTagCompound compound = revealer.getTagCompound();
/*  42 */     compound.setString(type.getName(), lens.getRegistryName().toString());
/*  43 */     revealer.setTagCompound(compound);
   }
   
   public static void removeLens(EntityPlayer player, ItemStack revealer, LENSSLOT type) {
/*  47 */     if (revealer.hasTagCompound() && revealer.getTagCompound().hasKey(type.getName())) {
/*  48 */       String oldLens = revealer.getTagCompound().getString(type.getName());
/*  49 */       if (!oldLens.isEmpty()) {
/*  50 */         Lens lens2 = (Lens)IsorropiaAPI.lensRegistry.getValue(new ResourceLocation(oldLens));
/*  51 */         lens2.handleRemoval(player.world, player);
/*  52 */         if (!player.inventory.addItemStackToInventory(new ItemStack((Item)lens2.getItemLens())))
/*  53 */           player.dropItem(new ItemStack((Item)lens2.getItemLens()), false); 
/*  54 */         revealer.getTagCompound().removeTag(type.getName());
       } 
     } 
   }
   
   public static boolean changeLens(EntityPlayer player, Lens lens, LENSSLOT type) {
/*  60 */     ItemStack revealer = getRevealer(player);
/*  61 */     if (!revealer.isEmpty()) {
/*  62 */       removeLens(player, revealer, type);
/*  63 */       if (lens != null)
/*  64 */         setLens(revealer, lens, type); 
/*  65 */       return true;
     } 
/*  67 */     return false;
   }
   
   public static ItemStack getRevealer(EntityPlayer player) {
/*  71 */     boolean find = false;
/*  72 */     ItemStack revealer = ItemStack.EMPTY;
/*  73 */     for (int i = 0; i < 7; i++) {
/*  74 */       revealer = BaublesApi.getBaubles(player).getStackInSlot(i);
/*  75 */       if (revealer.getItem() instanceof IRevealer && ((IRevealer)revealer
/*  76 */         .getItem()).showNodes(revealer, (EntityLivingBase)player)) {
/*  77 */         find = true;
         break;
       } 
     } 
/*  81 */     if (!find) {
/*  82 */       for (ItemStack stack : player.getArmorInventoryList()) {
/*  83 */         if (stack.getItem() instanceof IRevealer && ((IRevealer)stack.getItem()).showNodes(stack, (EntityLivingBase)player)) {
/*  84 */           revealer = stack;
           break;
         } 
       } 
     }
/*  89 */     return revealer;
   }
   
   public enum LENSSLOT {
/*  93 */     LEFT("LeftLens", 42.0F), RIGHT("RightLens", -42.0F);
     
     private final float angle;
     private final String type;
     
     LENSSLOT(String type, float angle) {
/*  99 */       this.type = type;
/* 100 */       this.angle = angle;
     }
     
     public static LENSSLOT getByString(String name) {
/* 104 */       for (LENSSLOT slot : values()) {
/* 105 */         if (slot.getName().equals(name)) {
/* 106 */           return slot;
         }
       } 
       
/* 110 */       return LEFT;
     }
     
     public String getName() {
/* 114 */       return this.type;
     }
     
     public float getAngle() {
/* 118 */       return this.angle;
     }
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\lenses\LensManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */