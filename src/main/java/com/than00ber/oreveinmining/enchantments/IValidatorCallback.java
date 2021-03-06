package com.than00ber.oreveinmining.enchantments;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ToolType;

public interface IValidatorCallback {

    default boolean isBlockValid(BlockState state, ItemStack stack, ToolType type) {
        return state.isToolEffective(type) && stack.canHarvestBlock(state);
    }
}
