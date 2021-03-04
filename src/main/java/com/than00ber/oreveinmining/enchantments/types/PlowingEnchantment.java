package com.than00ber.oreveinmining.enchantments.types;

import com.than00ber.oreveinmining.enchantments.CarverEnchantmentBase;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ToolType;

import java.util.Collections;

public class PlowingEnchantment extends CarverEnchantmentBase {

    public PlowingEnchantment() {
        super(Rarity.RARE, ToolType.HOE);
    }

    @Override
    public boolean canApplyTogether(Enchantment enchantment) {
        return super.canApplyTogether(enchantment) || enchantment instanceof FertilityEnchantment;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getMaxEffectiveRadius(int level) {
        return 3;
    }

    @Override
    public boolean isValidTargetBlockState(BlockState state, ItemStack stack) {
        return state.get(CropsBlock.AGE) >= Collections.max(CropsBlock.AGE.getAllowedValues());
    }
}
