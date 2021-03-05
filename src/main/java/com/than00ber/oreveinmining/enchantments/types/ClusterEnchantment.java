package com.than00ber.oreveinmining.enchantments.types;

import com.than00ber.oreveinmining.enchantments.CarverEnchantmentBase;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;

public class ClusterEnchantment extends CarverEnchantmentBase {

    public ClusterEnchantment() {
        super(Rarity.RARE, ToolType.PICKAXE);
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getMaxEffectiveRadius(int level) {
        return 5;
    }

    @Override
    public boolean canApplyTogether(Enchantment enchantment) {
        return super.canApplyTogether(enchantment) || enchantment instanceof DiggingEnchantment;
    }

    @Override
    public boolean isTargetValid(BlockState state, ItemStack stack) {
        return super.isTargetValid(state, stack) && state.isIn(Tags.Blocks.ORES) && state.isIn(Tags.Blocks.ORES_REDSTONE);
    }
}
