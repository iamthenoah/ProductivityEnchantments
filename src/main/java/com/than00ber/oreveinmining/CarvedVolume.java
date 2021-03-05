package com.than00ber.oreveinmining;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class CarvedVolume {

    private final BlockPos ORIGIN;
    private final World WORLD;
    private final Set<BlockPos> VOLUME;

    public CarvedVolume(CarvingShape shape, int radius, BlockPos origin, World world) {
        this.ORIGIN = origin;
        this.WORLD = world;
        this.VOLUME = new HashSet<>();

        int yOffsetUp = shape.equals(CarvingShape.DISC) ? origin.getY() : origin.getY() + radius;
        int yOffsetDown = shape.equals(CarvingShape.DISC) ? origin.getY() : origin.getY() - radius;
        BlockPos p1 = new BlockPos(origin.getX() - radius, yOffsetUp, origin.getZ() - radius);
        BlockPos p2 = new BlockPos(origin.getX() + radius, yOffsetDown, origin.getZ() + radius);

        for (BlockPos pos : BlockPos.getAllInBoxMutable(p1, p2)) {
            BlockPos current = new BlockPos(pos);

            if (shape.equals(CarvingShape.SQUARE))
                this.VOLUME.add(current);
            else if (origin.withinDistance(current, radius))
                this.VOLUME.add(current);
        }
    }

    public Iterable<BlockPos> getVolume() {
        return this.VOLUME;
    }

    public Iterable<BlockPos> sortNearestToOrigin() {
        List<BlockPos> volume = new ArrayList<>(this.VOLUME);
        volume.sort(Comparator.comparingDouble(pos -> distance3DVec(pos, this.ORIGIN)));
        return volume;
    }

    public Iterable<BlockPos> filterBy(BlockState... states) {
        List<BlockState> valid = Arrays.asList(states);
        List<BlockPos> volume = new ArrayList<>(this.VOLUME);
        volume.removeIf(pos -> !valid.contains(WORLD.getBlockState(pos)));
        return volume;
    }

    public Iterable<BlockPos> filterConnectedRecursively() {
        Block original = this.WORLD.getBlockState(this.ORIGIN).getBlock();
        return filterRecursively(this.WORLD, this.ORIGIN, original, this.VOLUME, new HashSet<>());
    }

    private static Set<BlockPos> filterRecursively(World world, BlockPos origin, Block original, Set<BlockPos> volume, Set<BlockPos> cluster) {
        cluster.add(origin);

        if (cluster.size() < volume.size() && cluster.size() < 1024) {
            Set<BlockPos> branch = new HashSet<>();

            for (Direction direction : Direction.values()) {
                BlockPos current = origin.offset(direction);
                Block block = world.getBlockState(current).getBlock();

                if (!cluster.contains(current) && volume.contains(current) && block.equals(original))
                    branch.add(current);
            }

            branch.forEach(pos -> cluster.addAll(filterRecursively(world, pos, original, volume, cluster)));
        }

        return cluster;
    }

    private static double distance3DVec(BlockPos p1, BlockPos p2) {
        double x = Math.pow(p2.getX() - p1.getX(), 2);
        double y = Math.pow(p2.getY() - p1.getY(), 2);
        double z = Math.pow(p2.getZ() - p1.getZ(), 2);
        return Math.abs(Math.sqrt(x + y + z));
    }

    public enum CarvingShape {
        SPHERICAL, DISC, SQUARE
    }
}
