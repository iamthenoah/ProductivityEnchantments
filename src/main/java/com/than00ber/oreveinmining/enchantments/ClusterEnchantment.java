package com.than00ber.oreveinmining.enchantments;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClusterEnchantment extends TerrainEnchantmentBase {

    public ClusterEnchantment() {
        super(Rarity.RARE, ToolType.PICKAXE);
    }

    @Override
    public boolean canApplyTogether(Enchantment enchantment) {
        return super.canApplyTogether(enchantment) || enchantment instanceof DiggingEnchantment;
    }

    @Override
    public boolean appliesToBlock(BlockState state) {
        return super.appliesToBlock(state) && state.isIn(Tags.Blocks.ORES);
    }

    @Override
    public int getMaxRadius(int level) {
        return (int) Math.pow(3, level);
    }

    @Override
    public Set<BlockPos> getBlocks(Integer level, World world, BlockPos origin, Block original) {
        List<BlockPos> cluster = new ArrayList<>(getOreCluster(world, origin, original, new HashSet<>()));

        cluster.sort((p1, p2) -> {
            double d1 = distance3DVec(p1, origin);
            double d2 = distance3DVec(p2, origin);
            return Double.compare(d1, d2);
        });

        int startIndex = Math.min(cluster.size(), this.getMaxRadius(level));
        cluster.subList(startIndex, cluster.size()).clear();

        return new HashSet<>(cluster);
    }

    private static Set<BlockPos> getOreCluster(World world, BlockPos pos, Block original, Set<BlockPos> cluster) {
        cluster.add(pos);

        if (cluster.size() < 1024) {
            Set<BlockPos> branch = new HashSet<>();

            for (Direction direction : Direction.values()) {
                BlockPos current = pos.offset(direction);
                Block block = world.getBlockState(current).getBlock();

                if (block.equals(original) && !cluster.contains(current))
                    branch.add(current);
            }

            branch.forEach(p -> cluster.addAll(getOreCluster(world, p, original, cluster)));
        }

        return cluster;
    }

    private static double distance3DVec(BlockPos p1, BlockPos p2) {
        double x1 = Math.pow(p2.getX() - p1.getX(), 2);
        double y1 = Math.pow(p2.getY() - p1.getY(), 2);
        double z1 = Math.pow(p2.getZ() - p1.getZ(), 2);
        return Math.abs(Math.sqrt(x1 + y1 + z1));
    }
}
