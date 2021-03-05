package com.than00ber.oreveinmining.enchantments.types;

import com.than00ber.oreveinmining.enchantments.CarverEnchantmentBase;
import com.than00ber.oreveinmining.enchantments.IRightClickEffect;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class FertilityEnchantment extends CarverEnchantmentBase implements IRightClickEffect {

    public FertilityEnchantment() {
        super(Rarity.UNCOMMON, ToolType.HOE);
    }

    @Override
    public boolean isTargetValid(BlockState state, ItemStack stack) {
        return state.getBlock() == Blocks.FARMLAND;
    }

    @Override
    public Set<BlockPos> getVolume(ItemStack stack, int level, CarverEnchantmentBase enchantment, World world, BlockPos origin) {
        Set<BlockPos> block = new HashSet<>();
        block.add(origin);
        return block;
    }

    @Override
    public void onRightClick(ItemStack stack, int level, Direction facing, CarverEnchantmentBase enchantment, World world, BlockPos origin, PlayerEntity player) {
        int radius = enchantment.getMaxEffectiveRadius(level);
        Set<BlockPos> area = new HashSet<>();
        BlockPos surface = origin.up();

        BlockPos start = new BlockPos(surface.getX() - radius, surface.getY(), surface.getZ() - radius);
        BlockPos finish = new BlockPos(surface.getX() + radius, surface.getY(), surface.getZ() + radius);

        for (BlockPos current : BlockPos.getAllInBoxMutable(start, finish)) {
            BlockPos pos = new BlockPos(current);
            Block ground = world.getBlockState(pos.down()).getBlock();
            Block above = world.getBlockState(pos).getBlock();

            if (surface.withinDistance(pos, radius) && ground == Blocks.FARMLAND && above == Blocks.AIR)
                area.add(pos);
        }

        System.out.println(area);

        AtomicBoolean notBroken = new AtomicBoolean(true);

        for (BlockPos blockPos : area) {
            if (notBroken.get()) {
                world.setBlockState(blockPos, Blocks.WHEAT.getDefaultState().with(CropsBlock.AGE, 7));
                stack.damageItem(1, player, p -> notBroken.set(false));
            }
        }
    }
}
