package fr.wind_blade.isorropia.common.items.misc;

import fr.wind_blade.isorropia.common.lenses.Lens;
import net.minecraft.item.Item;
public class ItemLens 
        extends Item { 
    private Lens lens;
    public ItemLens() {
             setMaxStackSize(1);  
    }
    public void setLens(Lens lensIn) {
             this.lens = lensIn;  
    }
    public Lens getLens() {
             return this.lens;
    }
}