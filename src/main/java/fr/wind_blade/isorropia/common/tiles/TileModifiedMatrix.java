 package fr.wind_blade.isorropia.common.tiles;
 
 import java.awt.Color;
 import java.text.DecimalFormat;
 import java.util.ArrayList;
 import java.util.List;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.item.ItemStack;
 import net.minecraft.tileentity.TileEntity;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.EnumHand;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.util.text.TextFormatting;
 import net.minecraft.util.text.translation.I18n;
 import net.minecraft.world.World;
 import thaumcraft.api.aspects.Aspect;
 import thaumcraft.api.aspects.AspectList;
 import thaumcraft.api.aspects.IAspectContainer;
 import thaumcraft.api.casters.IInteractWithCaster;
 import thaumcraft.api.items.IGogglesDisplayExtended;
 
 
 
 
 
 
 public class TileModifiedMatrix
   extends TileEntity
   implements IInteractWithCaster, IAspectContainer, IGogglesDisplayExtended
 {
   public AspectList getAspects() {
/*  32 */     TileVat vat = getMaster();
/*  33 */     return (vat == null) ? null : vat.getAspects();
   }
 
 
   
   public void setAspects(AspectList paramAspectList) {}
 
   
   public boolean doesContainerAccept(Aspect paramAspect) {
/*  42 */     return false;
   }
 
   
   public int addToContainer(Aspect paramAspect, int paramInt) {
/*  47 */     return 0;
   }
 
   
   public boolean takeFromContainer(Aspect paramAspect, int paramInt) {
/*  52 */     return false;
   }
 
   
   public boolean takeFromContainer(AspectList paramAspectList) {
/*  57 */     return false;
   }
 
   
   public boolean doesContainerContainAmount(Aspect paramAspect, int paramInt) {
/*  62 */     return false;
   }
 
   
   public boolean doesContainerContain(AspectList paramAspectList) {
/*  67 */     return false;
   }
 
   
   public int containerContains(Aspect paramAspect) {
/*  72 */     return 0;
   }
 
 
   
   public boolean onCasterRightClick(World paramWorld, ItemStack paramItemStack, EntityPlayer paramEntityPlayer, BlockPos paramBlockPos, EnumFacing paramEnumFacing, EnumHand paramEnumHand) {
/*  78 */     TileVat vat = getMaster();
/*  79 */     if (vat != null) {
/*  80 */       vat.onCasterRightClick(paramWorld, paramItemStack, paramEntityPlayer, paramBlockPos, paramEnumFacing, paramEnumHand);
       
/*  82 */       return true;
     } 
/*  84 */     return false;
   }
   
   public TileVat getMaster() {
/*  88 */     TileEntity te = this.world.getTileEntity(getPos().down());
/*  89 */     return (te instanceof TileVat) ? (TileVat)te : null;
   }
   
/*  92 */   static DecimalFormat myFormatter = new DecimalFormat("#######.##");
   
/*  94 */   private static final String[] EMPTY_STRING_ARRAY = new String[0];
 
 
   
   public String[] getIGogglesText() {
/*  99 */     TileVat tileVat = getMaster();
/* 100 */     if (tileVat instanceof TileVat) {
/* 101 */       TileVat master = tileVat;
/* 102 */       List<String> strings = new ArrayList<>();
/* 103 */       strings.add(TextFormatting.BOLD + I18n.translateToLocal("stability." + master.getStability().name()));
       
/* 105 */       if (master.getFluxStocked() > 0.0F) {
/* 106 */         strings.add(TextFormatting.DARK_PURPLE + "Flux : " + master.getFluxStocked());
       }
       
/* 109 */       if (master.getCelestialAura() > 0.0F || master.getCelestialAuraNeeded() > 0.0F) {
/* 110 */         strings.add(TextFormatting.fromColorIndex((new Color(0.49019608F, 0.5568628F, 0.63529414F))
/* 111 */               .getRGB()) + "Celestia Aura " + master
/* 112 */             .getCelestialAura() + "/" + master.getCelestialAuraNeeded());
       }
       
/* 115 */       float lpc = master.getLossPerCycle();
/* 116 */       if (lpc != 0.0F) {
/* 117 */         strings.add(TextFormatting.GOLD + "" + TextFormatting.ITALIC + myFormatter
/* 118 */             .format(master.getSymmetryStability()) + " " + 
/* 119 */             I18n.translateToLocal("stability.gain"));
/* 120 */         strings.add(TextFormatting.RED + "" + I18n.translateToLocal("stability.range") + TextFormatting.ITALIC + myFormatter
/* 121 */             .format(lpc) + " " + I18n.translateToLocal("stability.loss"));
       } else {
/* 123 */         strings.add(TextFormatting.GOLD + "" + TextFormatting.ITALIC + myFormatter
/* 124 */             .format(master.getSymmetryStability()) + " " + 
/* 125 */             I18n.translateToLocal("stability.gain"));
       } 
       
/* 128 */       return strings.<String>toArray(new String[0]);
     } 
/* 130 */     return EMPTY_STRING_ARRAY;
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\common\tiles\TileModifiedMatrix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */