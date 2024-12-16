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
      super(0, false);
      setNoRepair();
      setHasSubtypes(true);
   }
 
   
   public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
      if (tab == Common.isorropiaCreativeTabs || tab == CreativeTabs.SEARCH) {
        items.add(new ItemStack((Item)this));
        for (Aspect tag : Aspect.aspects.values()) {
          ItemStack i = new ItemStack((Item)this);
          setAspects(i, (new AspectList()).add(tag, 5));
          items.add(i);
       } 
     } 
   }
 
   
   @SideOnly(Side.CLIENT)
   public String getItemStackDisplayName(ItemStack stack) {
      return (getAspects(stack) != null && !(getAspects(stack)).aspects.isEmpty()) ? (
        (getAspects(stack).getAspects()[0] == Aspect.FLUX) ? 
        I18n.format(getTranslationKey(stack) + ".vitium.name", new Object[0]) : 
        String.format(super.getItemStackDisplayName(stack), new Object[] {
            getAspects(stack).getAspects()[0].getName()
         })) : I18n.format(getTranslationKey(stack) + ".default.name", new Object[0]);
   }
 
   
   public AspectList getAspects(ItemStack itemstack) {
      if (itemstack.hasTagCompound()) {
        AspectList aspects = new AspectList();
        aspects.readFromNBT(itemstack.getTagCompound());
        return (aspects.size() > 0) ? aspects : null;
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
      return false;
   }
 
   
   public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
      ItemStack itemstack = playerIn.getHeldItem(handIn);
     
      if (getProvider(itemstack).canBeEaten(playerIn, itemstack)) {
        playerIn.setActiveHand(handIn);
        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
     } 
      return new ActionResult(EnumActionResult.FAIL, itemstack);
   }
 
 
   
   protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
      getProvider(stack).onFoodEeaten(player, stack);
   }
 
   
   public int getMaxItemUseDuration(ItemStack stack) {
      return getProvider(stack).getEatDuration(stack);
   }
 
   
   public int getHealAmount(ItemStack stack) {
      return getProvider(stack).getHungerReplinish(stack);
   }
 
   
   public float getSaturationModifier(ItemStack stack) {
     return getProvider(stack).getSaturationReplinish(stack);
   }
   
   private static IJellyAspectEffectProvider getProvider(ItemStack stack) {
   AspectList aspects = ItemsIS.itemJelly.getAspects(stack);
     
/* 115 */     IJellyAspectEffectProvider provider = IsorropiaAPI.getJellyAspectEffect((aspects == null) ? null : aspects.getAspects()[0]);
/* 116 */     return (provider == null) ? IsorropiaAPI.getJellyAspectEffect(null) : provider;
   }
 }