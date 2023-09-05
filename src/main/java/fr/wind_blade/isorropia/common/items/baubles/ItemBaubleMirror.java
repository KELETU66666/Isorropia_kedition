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
 
 
 
 public class ItemBaubleMirror
   extends Item
   implements IBauble
 {
   private BaubleType type;
   
   public ItemBaubleMirror(BaubleType type) {
/* 34 */     this.type = type;
   }
   
   public EnumRarity getRarity(ItemStack itemstack) {
/* 38 */     return EnumRarity.RARE;
   }
 
   
   public BaubleType getBaubleType(ItemStack itemstack) {
/* 43 */     return this.type;
   }
 
 
   
   public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
/* 49 */     if (world.getBlockState(pos).getBlock() == BlocksTC.mirror) {
/* 50 */       Common.INSTANCE.sendToServer((IMessage)new MirrorMessage(pos));
     }
/* 52 */     return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
   }
 
   
   @SideOnly(Side.CLIENT)
   public boolean hasEffect(ItemStack par1ItemStack) {
/* 58 */     return par1ItemStack.hasTagCompound();
   }
 
   
   public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
/* 63 */     NBTTagCompound compound = stack.getTagCompound();
/* 64 */     if (compound != null)
/* 65 */       tooltip.add(I18n.format("ir.mirrorLink", new Object[] { Integer.valueOf(compound.getInteger("linkX")), Integer.valueOf(compound.getInteger("linkY")), 
/* 66 */               Integer.valueOf(compound.getInteger("linkZ")), compound.getString("dimname") })); 
/* 67 */     super.addInformation(stack, worldIn, tooltip, flagIn);
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\items\baubles\ItemBaubleMirror.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */