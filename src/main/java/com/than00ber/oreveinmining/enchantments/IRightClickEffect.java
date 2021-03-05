package com.than00ber.oreveinmining.enchantments;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IRightClickEffect {

    default void onRightClick(ItemStack stack, int level, Direction facing, World world, BlockPos origin, PlayerEntity player) {};

    default void onRightClick(ItemStack stack, int level, Direction facing, CarverEnchantmentBase enchantment, World world, BlockPos origin, PlayerEntity player) {};

    default boolean isCreativeOnly() {
        return false;
    }
}
