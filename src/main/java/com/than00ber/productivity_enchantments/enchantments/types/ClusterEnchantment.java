package com.than00ber.productivity_enchantments.enchantments.types;

import com.than00ber.productivity_enchantments.enchantments.CarverEnchantmentBase;
import com.than00ber.productivity_enchantments.IValidatorCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
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
    public boolean isBlockValid(BlockState state, ItemStack stack, ToolType type) {
        return IValidatorCallback.defaultCheck(state, stack, type) && state.isIn(Tags.Blocks.ORES) || state.getBlock() instanceof OreBlock;
    }
}
