package com.than00ber.oreveinmining.enchantments.types;

import com.than00ber.oreveinmining.enchantments.CarverEnchantmentBase;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;

public class DiggingEnchantment extends CarverEnchantmentBase {

    public DiggingEnchantment() {
        super(Rarity.UNCOMMON, ToolType.SHOVEL);
    }

    @Override
    public boolean canApplyTogether(Enchantment enchantment) {
        return super.canApplyTogether(enchantment) || enchantment instanceof ClusterEnchantment;
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return stack.getItem() instanceof PickaxeItem || stack.getItem() instanceof ShovelItem;
    }

    @Override
    public boolean isValidTargetBlockState(BlockState state, ItemStack stack) {
        boolean isDirty = stack.getItem().canHarvestBlock(state) && stack.getItem() instanceof ShovelItem;
        boolean isRocky = stack.getItem().canHarvestBlock(state) && stack.getItem() instanceof PickaxeItem;
        return super.isValidTargetBlockState(state, stack) || (!state.isIn(Tags.Blocks.ORES) && (isDirty || isRocky));
    }
}
