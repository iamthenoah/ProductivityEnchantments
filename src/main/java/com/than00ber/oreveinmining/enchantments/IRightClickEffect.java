package com.than00ber.oreveinmining.enchantments;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public interface IRightClickEffect {

    default void onRightClick(ItemStack stack, int level, Direction facing, World world, BlockPos origin, PlayerEntity player) {};

    default void onRightClick(ItemStack stack, int level, Direction facing, CarverEnchantmentBase enchantment, World world, BlockPos origin, PlayerEntity player) {};

    default void performPlacements(World world, PlayerEntity player, ItemStack stack, Set<BlockPos> area, BlockState state) {
        AtomicBoolean notBroken = new AtomicBoolean(true);
        player.swingArm(Hand.MAIN_HAND);

        for (BlockPos blockPos : area) {

            if (notBroken.get()) {
                world.setBlockState(blockPos, state);
                stack.damageItem(1, player, p -> notBroken.set(false));
            }
        }
    }
}
