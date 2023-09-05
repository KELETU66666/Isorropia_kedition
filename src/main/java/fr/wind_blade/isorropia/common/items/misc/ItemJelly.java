 package fr.wind_blade.isorropia.common.items.misc;
 
 import fr.wind_blade.isorropia.common.Common;
 import fr.wind_blade.isorropia.common.IsorropiaAPI;
 import fr.wind_blade.isorropia.common.items.IJellyAspectEffectProvider;
 import fr.wind_blade.isorropia.common.items.ItemsIS;
 import net.minecraft.client.resources.I18n;
 import net.minecraft.creativetab.CreativeTabs;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.item.Item;
 import net.minecraft.item.ItemFood;
 import net.minecraft.item.ItemStack;
 import net.minecraft.nbt.NBTTagCompound;
 import net.minecraft.util.ActionResult;
 import net.minecraft.util.EnumActionResult;
 import net.minecraft.util.EnumHand;
 import net.minecraft.util.NonNullList;
 import net.minecraft.world.World;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 import thaumcraft.api.aspects.Aspect;
 import thaumcraft.api.aspects.AspectList;
 import thaumcraft.api.aspects.IEssentiaContainerItem;
 
 public class ItemJelly extends ItemFood implements IEssentiaContainerItem {
   private static final int ASPECT_SIZE = 5;
   
   public ItemJelly() {
/*  29 */     super(0, false);
/*  30 */     setNoRepair();
/*  31 */     setHasSubtypes(true);
   }
 
   
   public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
/*  36 */     if (tab == Common.isorropiaCreativeTabs || tab == CreativeTabs.SEARCH) {
/*  37 */       items.add(new ItemStack((Item)this));
/*  38 */       for (Aspect tag : Aspect.aspects.values()) {
/*  39 */         ItemStack i = new ItemStack((Item)this);
/*  40 */         setAspects(i, (new AspectList()).add(tag, 5));
/*  41 */         items.add(i);
       } 
     } 
   }
 
   
   @SideOnly(Side.CLIENT)
   public String getItemStackDisplayName(ItemStack stack) {
/*  49 */     return (getAspects(stack) != null && !(getAspects(stack)).aspects.isEmpty()) ? (
/*  50 */       (getAspects(stack).getAspects()[0] == Aspect.FLUX) ? 
/*  51 */       I18n.format(getTranslationKey(stack) + ".vitium.name", new Object[0]) : 
/*  52 */       String.format(super.getItemStackDisplayName(stack), new Object[] {
/*  53 */           getAspects(stack).getAspects()[0].getName()
/*  54 */         })) : I18n.format(getTranslationKey(stack) + ".default.name", new Object[0]);
   }
 
   
   public AspectList getAspects(ItemStack itemstack) {
/*  59 */     if (itemstack.hasTagCompound()) {
/*  60 */       AspectList aspects = new AspectList();
/*  61 */       aspects.readFromNBT(itemstack.getTagCompound());
/*  62 */       return (aspects.size() > 0) ? aspects : null;
     } 
/*  64 */     return null;
   }
 
   
   public void setAspects(ItemStack itemstack, AspectList aspects) {
/*  69 */     if (!itemstack.hasTagCompound()) {
/*  70 */       itemstack.setTagCompound(new NBTTagCompound());
     }
/*  72 */     aspects.writeToNBT(itemstack.getTagCompound());
   }
 
   
   public boolean ignoreContainedAspects() {
/*  77 */     return false;
   }
 
   
   public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
/*  82 */     ItemStack itemstack = playerIn.getHeldItem(handIn);
     
/*  84 */     if (getProvider(itemstack).canBeEaten(playerIn, itemstack)) {
/*  85 */       playerIn.setActiveHand(handIn);
/*  86 */       return new ActionResult(EnumActionResult.SUCCESS, itemstack);
     } 
/*  88 */     return new ActionResult(EnumActionResult.FAIL, itemstack);
   }
 
 
   
   protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
/*  94 */     getProvider(stack).onFoodEeaten(player, stack);
   }
 
   
   public int getMaxItemUseDuration(ItemStack stack) {
/*  99 */     return getProvider(stack).getEatDuration(stack);
   }
 
   
   public int getHealAmount(ItemStack stack) {
/* 104 */     return getProvider(stack).getHungerReplinish(stack);
   }
 
   
   public float getSaturationModifier(ItemStack stack) {
/* 109 */     return getProvider(stack).getSaturationReplinish(stack);
   }
   
   private static IJellyAspectEffectProvider getProvider(ItemStack stack) {
/* 113 */     AspectList aspects = ItemsIS.itemJelly.getAspects(stack);
     
/* 115 */     IJellyAspectEffectProvider provider = IsorropiaAPI.getJellyAspectEffect((aspects == null) ? null : aspects.getAspects()[0]);
/* 116 */     return (provider == null) ? IsorropiaAPI.getJellyAspectEffect(null) : provider;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\items\misc\ItemJelly.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */