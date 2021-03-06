package com.than00ber.oreveinmining.enchantments;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.Tag;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class CarvedVolume {

    private final BlockPos ORIGIN;
    private final World WORLD;
    private Set<BlockPos> VOLUME;
    private ItemStack TOOL_RESTRICTION;

    public CarvedVolume(Shape shape, int radius, BlockPos origin, World world) {
        this.ORIGIN = origin;
        this.WORLD = world;
        this.VOLUME = new HashSet<>();

        int yOffsetUp = shape.equals(Shape.DISC) ? origin.getY() : origin.getY() + radius;
        int yOffsetDown = shape.equals(Shape.DISC) ? origin.getY() : origin.getY() - radius;
        BlockPos p1 = new BlockPos(origin.getX() - radius, yOffsetUp, origin.getZ() - radius);
        BlockPos p2 = new BlockPos(origin.getX() + radius, yOffsetDown, origin.getZ() + radius);

        for (BlockPos pos : BlockPos.getAllInBoxMutable(p1, p2)) {
            BlockPos current = new BlockPos(pos);

            if (shape.equals(Shape.SQUARE))
                this.VOLUME.add(current);
            else if (origin.withinDistance(current, radius))
                this.VOLUME.add(current);
        }
    }

    public Set<BlockPos> getVolume() {
        return this.VOLUME;
    }

    public CarvedVolume setToolRestriction(ItemStack stack) {
        this.TOOL_RESTRICTION = stack;
        return this;
    }

    public CarvedVolume sortNearestToOrigin() {

        List<BlockPos> volume = new ArrayList<>(this.VOLUME);
        volume.sort(Comparator.comparingDouble(pos -> distance3DVec(pos, this.ORIGIN)));
        this.VOLUME = new HashSet<>(volume);

        return this;
    }

    public CarvedVolume filterBy(BlockState... states) {

        List<BlockState> valid = Arrays.asList(states);
        List<BlockPos> volume = new ArrayList<>(this.VOLUME);
        volume.removeIf(pos -> !valid.contains(WORLD.getBlockState(pos)));
        this.VOLUME = new HashSet<>(volume);

        return this;
    }

    public CarvedVolume filterBy(Tag<Block>... tags) {

        List<Tag<Block>> valid = Arrays.asList(tags);
        List<BlockPos> volume = new ArrayList<>(this.VOLUME);
        for (Tag<Block> tag : tags) volume.removeIf(pos -> !WORLD.getBlockState(pos).isIn(tag));
        this.VOLUME = new HashSet<>(volume);

        return this;
    }

    /**
     * Filters through implemented validation callback.
     * Make sure to call CarvedVolume#setToolRestriction before performing
     * this filtering.
     *
     * @param callback validation callback
     * @return instance
     */
    public CarvedVolume filterBy(IValidatorCallback callback) {

        if (this.TOOL_RESTRICTION == null) {
            throw new IllegalArgumentException("Cannot perform block filtering validation without any tool restrictions set.");
        }

        List<BlockPos> volume = new ArrayList<>(this.VOLUME);
        volume.removeIf(pos -> !callback.isBlockValid(this.WORLD.getBlockState(pos), this.TOOL_RESTRICTION, ((CarverEnchantmentBase) callback).TOOL_TYPE));
        this.VOLUME = new HashSet<>(volume);

        return this;
    }

    public CarvedVolume filterConnectedRecursively() {

        Block original = this.WORLD.getBlockState(this.ORIGIN).getBlock();
        this.VOLUME = filterRecursively(this.WORLD, this.ORIGIN, original, this.VOLUME, new HashSet<>());

        return this;
    }

    private static Set<BlockPos> filterRecursively(World world, BlockPos origin, Block original, Set<BlockPos> volume, Set<BlockPos> cluster) {
        cluster.add(origin);

        if (cluster.size() < volume.size() && cluster.size() < 2048) {
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

    public enum Shape {
        SPHERICAL, DISC, SQUARE
    }
}
