package com.than00ber.oreveinmining.enchantments;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TerrainEnchantmentBase extends Enchantment {

    protected final ToolType[] TOOL_TYPES;

    protected TerrainEnchantmentBase(Rarity rarity, ToolType... types) {
        super(rarity, EnchantmentType.DIGGER, new EquipmentSlotType[] { EquipmentSlotType.MAINHAND });
        this.TOOL_TYPES = types;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean canApplyTogether(Enchantment enchantment) {
        return super.canApplyTogether(enchantment) && !(enchantment instanceof TerrainEnchantmentBase);
    }

    @Override
    public boolean canApply(ItemStack stack) {
        for (ToolType toolType : TOOL_TYPES)
            if (stack.getToolTypes().contains(toolType))
                return true;
        return false;
    }

    public int getMaxEffectiveRadius(int level) {
        return level + 1;
    }

    public Set<BlockPos> getBlocks(Integer level, World world, BlockPos origin, Block original) {
        return Stream.of(origin).collect(Collectors.toSet());
    }

    public boolean appliesToBlock(BlockState state) {
        for (ToolType toolType : TOOL_TYPES)
            if (state.isToolEffective(toolType))
                return true;
        return false;
    }
}
