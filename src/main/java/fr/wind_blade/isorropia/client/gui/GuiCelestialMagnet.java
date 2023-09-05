 package fr.wind_blade.isorropia.client.gui;
 
 import fr.wind_blade.isorropia.common.Common;
 import fr.wind_blade.isorropia.common.network.MagnetMessage;
 import fr.wind_blade.isorropia.common.tiles.TileCelestialMagnet;
 import java.awt.Color;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.gui.GuiButton;
 import net.minecraft.client.gui.GuiScreen;
 import net.minecraft.client.renderer.BufferBuilder;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.RenderItem;
 import net.minecraft.client.renderer.Tessellator;
 import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
 import net.minecraft.init.Items;
 import net.minecraft.item.ItemStack;
 import net.minecraft.util.ResourceLocation;
 import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 import org.lwjgl.opengl.GL11;
 import thaumcraft.api.aspects.Aspect;
 import thaumcraft.client.gui.plugins.GuiImageButton;
 
 
 @SideOnly(Side.CLIENT)
 public class GuiCelestialMagnet
   extends GuiScreen
 {
/*  30 */   ResourceLocation tex = new ResourceLocation("isorropia", "textures/gui/gui_magnet.png");
   
   private final TileCelestialMagnet magnet;
   private GuiImageButton mob;
   private GuiImageButton areaLeft;
   private GuiImageButton areaRight;
   private GuiButton item;
   private GuiButton aspect;
   
   public GuiCelestialMagnet(TileCelestialMagnet magnetIn) {
/*  40 */     this.magnet = magnetIn;
   }
 
   
   public void initGui() {
/*  45 */     int k = this.width;
/*  46 */     int l = this.height;
/*  47 */     int i = 0;
/*  48 */     int j = 0;
/*  49 */     for (Aspect aspect : Aspect.aspects.values()) {
/*  50 */       int x = k / 2 + i * 17 - 68;
/*  51 */       int y = l / 2 + 0 - 80 + 16 * j;
/*  52 */       GuiCelestialAspectButton button = new GuiCelestialAspectButton(i, x, y, aspect, null);
/*  53 */       if (this.magnet.hasFilter(aspect))
/*  54 */         button.active = true; 
/*  55 */       this.buttonList.add(button);
/*  56 */       i++;
/*  57 */       if (i != 0 && i % 8 == 0) {
/*  58 */         j++;
/*  59 */         i = 0;
       } 
     } 
/*  62 */     this.mob = new GuiImageButton(this, 0, k / 2 + 84, l / 2 - 80, 16, 16, null, null, this.tex, 240, 16, 16, 16)
       {
         public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
/*  65 */           if (mouseX >= this.x - this.width / 2 && mouseY >= this.y - this.height / 2 && mouseX < this.x - this.width / 2 + this.width && mouseY < this.y - this.height / 2 + this.height) {
 
             
/*  68 */             this.active = !this.active;
/*  69 */             GuiCelestialMagnet.this.magnet.setFilter(this.active ? (
/*  70 */                 (GuiCelestialMagnet.this.magnet.getEntityFilter() == TileCelestialMagnet.EntityFilter.item) ? TileCelestialMagnet.EntityFilter.both : TileCelestialMagnet.EntityFilter.mob) : (
 
                 
/*  73 */                 (GuiCelestialMagnet.this.magnet.getEntityFilter() == TileCelestialMagnet.EntityFilter.both) ? TileCelestialMagnet.EntityFilter.item : TileCelestialMagnet.EntityFilter.none));
 
             
/*  76 */             Common.INSTANCE.sendToServer((IMessage)new MagnetMessage(GuiCelestialMagnet.this.magnet.getPos(), null, GuiCelestialMagnet.this.magnet.getEntityFilter(), 
/*  77 */                   GuiCelestialMagnet.this.magnet.aspectFilter, GuiCelestialMagnet.this.magnet.area));
/*  78 */             return true;
           } 
/*  80 */           return false;
         }
 
         
         public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
/*  85 */           GlStateManager.pushMatrix();
/*  86 */           GlStateManager.enableBlend();
/*  87 */           GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*  88 */           GlStateManager.blendFunc(770, 771);
/*  89 */           Color c = new Color(this.color);
/*  90 */           GlStateManager.color(c.getRed() / 255.0F, c.getGreen() / 255.0F, c.getBlue() / 255.0F, 1.0F);
/*  91 */           mc.getTextureManager().bindTexture(GuiCelestialMagnet.this.tex);
/*  92 */           drawTexturedModalRect(this.x - 8, this.y - 8, 240, 16, 16, 16);
/*  93 */           GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  94 */           if (!this.active) {
/*  95 */             GL11.glAlphaFunc(516, 0.003921569F);
/*  96 */             GL11.glEnable(3042);
             GL11.glBlendFunc(770, 771);
             drawTexturedModalRect(this.x - 8, this.y - 8, 240, 0, 16, 16);
           } 
/* 100 */           GlStateManager.popMatrix();
/* 101 */           mouseDragged(mc, mouseX, mouseY);
         }
       };
/* 104 */     this.item = new GuiButton(1, k / 2 + 76, l / 2 - 72, 14, 14, null)
       {
         public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
/* 107 */           GlStateManager.pushMatrix();
/* 108 */           GL11.glAlphaFunc(516, 0.003921569F);
/* 109 */           GL11.glEnable(3042);
/* 110 */           GL11.glBlendFunc(770, 771);
/* 111 */           RenderItem ri = Minecraft.getMinecraft().getRenderItem();
/* 112 */           ri.renderItemIntoGUI(new ItemStack(Items.CHEST_MINECART), this.x, this.y);
/* 113 */           if (!this.enabled) {
/* 114 */             mc.getTextureManager().bindTexture(GuiCelestialMagnet.this.tex);
/* 115 */             drawTexturedModalRect(this.x, this.y + 1, 240, 0, 16, 16);
           } 
/* 117 */           GlStateManager.popMatrix();
         }
 
         
         public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
/* 122 */           if (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height) {
             
/* 124 */             this.enabled = !this.enabled;
/* 125 */             GuiCelestialMagnet.this.magnet.setFilter(this.enabled ? (
/* 126 */                 (GuiCelestialMagnet.this.magnet.getEntityFilter() == TileCelestialMagnet.EntityFilter.mob) ? TileCelestialMagnet.EntityFilter.both : TileCelestialMagnet.EntityFilter.item) : (
 
                 
/* 129 */                 (GuiCelestialMagnet.this.magnet.getEntityFilter() == TileCelestialMagnet.EntityFilter.both) ? TileCelestialMagnet.EntityFilter.mob : TileCelestialMagnet.EntityFilter.none));
 
             
/* 132 */             Common.INSTANCE.sendToServer((IMessage)new MagnetMessage(GuiCelestialMagnet.this.magnet.getPos(), null, GuiCelestialMagnet.this.magnet.getEntityFilter(), 
                   GuiCelestialMagnet.this.magnet.aspectFilter, GuiCelestialMagnet.this.magnet.area));
/* 134 */             return true;
           } 
/* 136 */           return false;
         }
       };
     
/* 140 */     this.aspect = new GuiButton(0, k / 2 + 76, l / 2 - 56, 16, 16, null)
       {
         public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
/* 143 */           if (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height) {
             
/* 145 */             this.enabled = !this.enabled;
/* 146 */             GuiCelestialMagnet.this.magnet.aspectFilter = this.enabled;
/* 147 */             Common.INSTANCE.sendToServer((IMessage)new MagnetMessage(GuiCelestialMagnet.this.magnet.getPos(), null, this.enabled, GuiCelestialMagnet.this.magnet.area));
/* 148 */             return true;
           } 
/* 150 */           return false;
         }
 
         
         public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
/* 155 */           Color color = new Color(Aspect.EARTH.getColor());
/* 156 */           GL11.glPushMatrix();
/* 157 */           GL11.glAlphaFunc(516, 0.003921569F);
/* 158 */           GL11.glEnable(3042);
/* 159 */           GL11.glBlendFunc(770, 771);
/* 160 */           float alpha = this.enabled ? 0.9F : 0.3F;
/* 161 */           Minecraft.getMinecraft().getTextureManager().bindTexture(Aspect.EARTH.getImage());
/* 162 */           Tessellator tessellator = Tessellator.getInstance();
/* 163 */           BufferBuilder buffer = tessellator.getBuffer();
/* 164 */           buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 165 */           buffer.putColorRGB_F(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, (int)alpha);
           
/* 167 */           buffer.pos(this.x, this.y, 0.0D).tex(0.0D, 0.0D)
/* 168 */             .color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, alpha)
/* 169 */             .endVertex();
/* 170 */           buffer.pos(this.x, (this.y + 16), 0.0D).tex(0.0D, 1.0D)
/* 171 */             .color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, alpha)
/* 172 */             .endVertex();
/* 173 */           buffer.pos((this.x + 16), (this.y + 16), 0.0D).tex(1.0D, 1.0D)
/* 174 */             .color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, alpha)
/* 175 */             .endVertex();
/* 176 */           buffer.pos((this.x + 16), this.y, 0.0D).tex(1.0D, 0.0D)
/* 177 */             .color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, alpha)
/* 178 */             .endVertex();
/* 179 */           tessellator.draw();
/* 180 */           mouseDragged(mc, mouseX, mouseY);
/* 181 */           GL11.glPopMatrix();
         }
       };
/* 184 */     this.areaLeft = new GuiImageButton(this, 0, k / 2 + 84, l / 2 + 80, 7, 10, null, null, this.tex, 227, 0, 7, 10)
       {
         public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
/* 187 */           if (mouseX >= this.x - this.width / 2 && mouseY >= this.y - this.height / 2 && mouseX < this.x - this.width / 2 + this.width && mouseY < this.y - this.height / 2 + this.height && 
             
/* 189 */             GuiCelestialMagnet.this.magnet.area > 0) {
/* 190 */             GuiCelestialMagnet.this.magnet.area--;
/* 191 */             Common.INSTANCE.sendToServer((IMessage)new MagnetMessage(GuiCelestialMagnet.this.magnet.getPos(), null, this.enabled, GuiCelestialMagnet.this.magnet.area));
/* 192 */             return true;
           } 
/* 194 */           return true;
         }
       };
/* 197 */     this.areaRight = new GuiImageButton(this, 0, k / 2 + 99, l / 2 + 80, 7, 10, null, null, this.tex, 232, 0, 7, 10)
       {
         public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
/* 200 */           if (mouseX >= this.x - this.width / 2 && mouseY >= this.y - this.height / 2 && mouseX < this.x - this.width / 2 + this.width && mouseY < this.y - this.height / 2 + this.height && 
             
/* 202 */             GuiCelestialMagnet.this.magnet.area < 9) {
/* 203 */             GuiCelestialMagnet.this.magnet.area++;
/* 204 */             Common.INSTANCE.sendToServer((IMessage)new MagnetMessage(GuiCelestialMagnet.this.magnet.getPos(), null, this.enabled, GuiCelestialMagnet.this.magnet.area));
/* 205 */             return true;
           } 
/* 207 */           return true;
         }
       };
/* 210 */     this.mob
/* 211 */       .active = (this.magnet.getEntityFilter() == TileCelestialMagnet.EntityFilter.both || this.magnet.getEntityFilter() == TileCelestialMagnet.EntityFilter.mob);
/* 212 */     this.item
/* 213 */       .enabled = (this.magnet.getEntityFilter() == TileCelestialMagnet.EntityFilter.both || this.magnet.getEntityFilter() == TileCelestialMagnet.EntityFilter.item);
/* 214 */     this.aspect.enabled = this.magnet.aspectFilter;
/* 215 */     this.buttonList.add(this.mob);
/* 216 */     this.buttonList.add(this.item);
/* 217 */     this.buttonList.add(this.aspect);
/* 218 */     this.buttonList.add(this.areaLeft);
/* 219 */     this.buttonList.add(this.areaRight);
/* 220 */     super.initGui();
   }
 
   
   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 225 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 226 */     this.mc.getTextureManager().bindTexture(this.tex);
/* 227 */     int k = this.width;
/* 228 */     int l = this.height;
/* 229 */     GL11.glEnable(3042);
/* 230 */     GL11.glBlendFunc(770, 771);
/* 231 */     drawTexturedModalRect(k / 2 - 76, l / 2 - 88, 0, 0, 208, 200);
 
 
     
/* 235 */     drawString(this.fontRenderer, Integer.toString(this.magnet.area), k / 2 + 89, l / 2 + 76, Color.WHITE.getRGB());
/* 236 */     super.drawScreen(mouseX, mouseY, partialTicks);
   }
   
   public class GuiCelestialAspectButton
     extends GuiButton {
     private final Aspect aspect;
     private boolean active = false;
     
     public GuiCelestialAspectButton(int buttonId, int x, int y, Aspect aspectIn, String buttonText) {
/* 245 */       super(buttonId, x, y, 16, 16, buttonText);
/* 246 */       this.aspect = aspectIn;
     }
 
     
     public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
/* 251 */       if (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height) {
/* 252 */         this.active = !this.active;
/* 253 */         if (!GuiCelestialMagnet.this.magnet.addAspectFilter(this.aspect))
/* 254 */           GuiCelestialMagnet.this.magnet.removeAspectFilter(this.aspect); 
/* 255 */         Common.INSTANCE.sendToServer((IMessage)new MagnetMessage(GuiCelestialMagnet.this
/* 256 */               .magnet.getPos(), this.aspect, GuiCelestialMagnet.this.magnet.aspectFilter, GuiCelestialMagnet.this.magnet.area));
       } 
/* 258 */       return super.mousePressed(mc, mouseX, mouseY);
     }
 
     
     public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
/* 263 */       Color color = new Color(this.aspect.getColor());
/* 264 */       float alpha = this.active ? 0.9F : 0.3F;
/* 265 */       GL11.glPushMatrix();
/* 266 */       GL11.glAlphaFunc(516, 0.003921569F);
/* 267 */       GL11.glEnable(3042);
/* 268 */       GL11.glBlendFunc(770, 771);
/* 269 */       Minecraft.getMinecraft().getTextureManager().bindTexture(this.aspect.getImage());
/* 270 */       Tessellator tessellator = Tessellator.getInstance();
/* 271 */       BufferBuilder buffer = tessellator.getBuffer();
/* 272 */       buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 273 */       buffer.putColorRGB_F(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, (int)alpha);
       
/* 275 */       buffer.pos(this.x, this.y, 0.0D).tex(0.0D, 0.0D)
/* 276 */         .color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, alpha).endVertex();
/* 277 */       buffer.pos(this.x, (this.y + 16), 0.0D).tex(0.0D, 1.0D)
/* 278 */         .color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, alpha).endVertex();
/* 279 */       buffer.pos((this.x + 16), (this.y + 16), 0.0D).tex(1.0D, 1.0D)
/* 280 */         .color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, alpha).endVertex();
/* 281 */       buffer.pos((this.x + 16), this.y, 0.0D).tex(1.0D, 0.0D)
/* 282 */         .color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, alpha).endVertex();
/* 283 */       tessellator.draw();
/* 284 */       GL11.glScalef(0.5F, 0.5F, 0.5F);
/* 285 */       GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 286 */       GL11.glDisable(3042);
/* 287 */       GL11.glAlphaFunc(516, 0.1F);
/* 288 */       GL11.glPopMatrix();
     }
   }
 }


/* Location:              E:\recaf\233.jar!\fr\wind_blade\isorropia\client\gui\GuiCelestialMagnet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */