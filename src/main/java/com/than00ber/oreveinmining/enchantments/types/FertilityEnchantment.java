package com.than00ber.oreveinmining.enchantments.types;

import com.than00ber.oreveinmining.enchantments.CarverEnchantmentBase;
import com.than00ber.oreveinmining.enchantments.ITerrainFormer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;

import java.util.Set;

public class FertilityEnchantment extends CarverEnchantmentBase implements ITerrainFormer {

    public FertilityEnchantment() {
        super(Rarity.COMMON, ToolType.HOE);
    }

    @Override
    public boolean canApplyTogether(Enchantment enchantment) {
        return super.canApplyTogether(enchantment) || enchantment instanceof PlowingEnchantment;
    }

    @Override
    public boolean isValidTargetBlockState(BlockState state, ItemStack stack) {
        return super.isValidTargetBlockState(state, stack) && state.isIn(Tags.Blocks.DIRT);
    }

    @Override
    public void transformBlock(ItemStack stack, int level, World world, BlockPos hit) {
        if (world.getBlockState(hit).getBlock().isIn(Tags.Blocks.DIRT))
            world.setBlockState(hit, Blocks.FARMLAND.getDefaultState());
    }

    @Override
    public Set<BlockPos> getAffectedVolume(ItemStack stack, int radius, World world, BlockPos origin) {
        Set<BlockPos> volume = super.getAffectedVolume(stack, radius, world, origin);
//        System.out.println("BEFORE -> " + volume.size());
        volume.removeIf(pos -> pos.getY() != origin.up().getY());
//        System.out.println("AFTER ->  " + volume.size());
        return volume;
    }
}
