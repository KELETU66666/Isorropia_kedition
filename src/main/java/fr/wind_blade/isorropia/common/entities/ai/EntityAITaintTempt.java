package fr.wind_blade.isorropia.common.entities.ai;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.Set;


public class EntityAITaintTempt
        extends EntityAITempt {
    public EntityAITaintTempt(EntityCreature temptedEntityIn, double speedIn, boolean scaredByPlayerMovementIn) {
        this(temptedEntityIn, speedIn, scaredByPlayerMovementIn, Collections.emptySet());
    }


    public EntityAITaintTempt(EntityCreature temptedEntityIn, double speedIn, boolean scaredByPlayerMovementIn, Set<Item> aditionalTemptItems) {
        super(temptedEntityIn, speedIn, scaredByPlayerMovementIn, aditionalTemptItems);
    }


    protected boolean isTempting(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof net.minecraft.item.ItemBlock) {
            Block block = Block.getBlockFromItem(item);

            if (block instanceof thaumcraft.common.blocks.world.taint.ITaintBlock) {
                return true;
            }
        }

        return super.isTempting(stack);
    }
}