package com.than00ber.oreveinmining.enchantments.types;

import com.than00ber.oreveinmining.enchantments.CarverEnchantmentBase;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ToolType;

public class WoodcuttingEnchantment extends CarverEnchantmentBase {

    public WoodcuttingEnchantment() {
        super(Rarity.COMMON, ToolType.AXE);
    }

    @Override
    public boolean isTargetValid(BlockState state, ItemStack stack) {
        return super.isTargetValid(state, stack) || state.getBlock() instanceof LeavesBlock;
    }
}
