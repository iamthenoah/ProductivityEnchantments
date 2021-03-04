package com.than00ber.oreveinmining.enchantments;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITerrainFormer {

    void transformBlock(ItemStack stack, int level, World world, BlockPos hit);

    default boolean isCreativeOnly() {
        return false;
    }
}
