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
    public int getMaxEffectiveRadius(int level) {
        return (int) Math.pow(3, level);
    }

    @Override
    public boolean canApplyTogether(Enchantment enchantment) {
        return super.canApplyTogether(enchantment) || enchantment instanceof DiggingEnchantment;
    }

    @Override
    public boolean isValidTargetBlockState(BlockState state, ItemStack stack) {
        return super.isValidTargetBlockState(state, stack) && state.isIn(Tags.Blocks.ORES);
    }
}
