package com.than00ber.oreveinmining.enchantments.types;

import com.than00ber.oreveinmining.enchantments.CarverEnchantmentBase;
import com.than00ber.oreveinmining.enchantments.IRightClickEffect;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlowingEnchantment extends CarverEnchantmentBase implements IRightClickEffect {

    public PlowingEnchantment() {
        super(Rarity.COMMON, ToolType.HOE);
    }

    @Override
    public boolean isTargetValid(BlockState state, ItemStack stack) {
        return state.isIn(Tags.Blocks.DIRT);
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

        BlockPos start = new BlockPos(origin.getX() - radius, origin.getY(), origin.getZ() - radius);
        BlockPos finish = new BlockPos(origin.getX() + radius, origin.getY(), origin.getZ() + radius);

        for (BlockPos current : BlockPos.getAllInBoxMutable(start, finish)) {
            BlockPos pos = new BlockPos(current);
            BlockState state = world.getBlockState(pos);
            Block above = world.getBlockState(pos.up()).getBlock();

            if (origin.withinDistance(pos, radius) && this.isTargetValid(state, stack) && above == Blocks.AIR)
                area.add(pos);
        }

        Block original = world.getBlockState(origin).getBlock();
        List<BlockPos> field = new ArrayList<>(super.filterConnectedRecursively(world, origin, original, area, new HashSet<>()));
        field.sort(Comparator.comparingDouble(p -> distance3DVec(p, origin)));
        AtomicBoolean notBroken = new AtomicBoolean(true);

        for (BlockPos blockPos : field) {

            if (notBroken.get()) {
                world.setBlockState(blockPos, Blocks.FARMLAND.getDefaultState());
                stack.damageItem(1, player, p -> notBroken.set(false));
            }
        }
    }
}
