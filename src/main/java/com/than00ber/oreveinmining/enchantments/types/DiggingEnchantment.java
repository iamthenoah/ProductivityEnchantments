package com.than00ber.oreveinmining.enchantments.types;

import com.than00ber.oreveinmining.enchantments.CarverEnchantmentBase;
import com.than00ber.oreveinmining.IValidatorCallback;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
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
    public boolean isBlockValid(BlockState state, ItemStack stack, ToolType type) {
        boolean isRocky = (stack.canHarvestBlock(state) || state.isToolEffective(type)) && stack.getItem() instanceof PickaxeItem;
        boolean isDirty = (stack.canHarvestBlock(state) || state.isToolEffective(type)) && stack.getItem() instanceof ShovelItem;
        return IValidatorCallback.defaultCheck(state, stack, type) && isRocky || isDirty;
    }
}
