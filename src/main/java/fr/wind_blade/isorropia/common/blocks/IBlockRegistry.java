package fr.wind_blade.isorropia.common.blocks;

public interface IBlockRegistry {
    default boolean haveItemBlock() {
        return true;
    }

    default boolean isInCreativeTabs() {
        return true;
    }
}