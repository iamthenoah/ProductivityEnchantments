package com.than00ber.oreveinmining.enchantments;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.common.Tags;

import java.util.Objects;

public class ClusterEnchantment extends Enchantment {

    public ClusterEnchantment() {
        super(Rarity.RARE, EnchantmentType.DIGGER, new EquipmentSlotType[] { EquipmentSlotType.MAINHAND });
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    public static int getMaxBranchSize(int level) {
        return level == 1 ? 3 : 256;
    }

    public static boolean canApplyToBlock(BlockState state) {
        return state.getBlock().isIn(Tags.Blocks.ORES) && Objects.requireNonNull(state.getBlock().getRegistryName()).getPath().contains("ore");
    }
}
