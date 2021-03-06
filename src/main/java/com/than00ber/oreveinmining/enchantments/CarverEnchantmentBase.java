package com.than00ber.oreveinmining.enchantments;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import java.util.*;

public class CarverEnchantmentBase extends Enchantment implements IValidatorCallback {

    public final ToolType TOOL_TYPE;

    protected CarverEnchantmentBase(Rarity rarity, ToolType type) {
        super(rarity, EnchantmentType.DIGGER, new EquipmentSlotType[] { EquipmentSlotType.MAINHAND });
        this.TOOL_TYPE = type;
    }

    @Override
    public boolean canApplyTogether(Enchantment enchantment) {

        if (enchantment instanceof CarverEnchantmentBase) {
            CarverEnchantmentBase enchantmentBase = (CarverEnchantmentBase) enchantment;

            if (enchantmentBase.TOOL_TYPE.equals(ToolType.HOE))
                return true;
        }

        return super.canApplyTogether(enchantment) && !(enchantment instanceof CarverEnchantmentBase);
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return stack.getToolTypes().contains(TOOL_TYPE);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    public int getMaxEffectiveRadius(int level) {
        return level + 1;
    }

    public boolean isTargetValid(BlockState state, ItemStack stack) {
        return state.isToolEffective(TOOL_TYPE) && state.getBlock() != Blocks.BEDROCK;
    }

    public Set<BlockPos> getVolume(ItemStack stack, int level, CarverEnchantmentBase enchantment, World world, BlockPos origin) {
        int radius = enchantment.getMaxEffectiveRadius(level);
        Set<BlockPos> volume = new HashSet<>();

        BlockPos start = new BlockPos(origin.getX() - radius, origin.getY() - radius, origin.getZ() - radius);
        BlockPos finish = new BlockPos(origin.getX() + radius, origin.getY() + radius, origin.getZ() + radius);

        for (BlockPos current : BlockPos.getAllInBoxMutable(start, finish)) {
            BlockPos pos = new BlockPos(current);
            BlockState state = world.getBlockState(pos);

            if (origin.withinDistance(pos, radius) && enchantment.isTargetValid(state, stack))
                volume.add(pos);
        }

        Block original = world.getBlockState(origin).getBlock();
        List<BlockPos> cluster = new ArrayList<>(this.filterConnectedRecursively(world, origin, original, volume, new HashSet<>()));
        cluster.sort(Comparator.comparingDouble(p -> distance3DVec(p, origin)));

        return new HashSet<>(cluster);
    }

    public Set<BlockPos> filterConnectedRecursively(World world, BlockPos origin, Block original, Set<BlockPos> volume, Set<BlockPos> cluster) {
        cluster.add(origin);

        if (cluster.size() < volume.size() && cluster.size() < 1024) {
            Set<BlockPos> branch = new HashSet<>();

            for (Direction direction : Direction.values()) {
                BlockPos current = origin.offset(direction);
                Block block = world.getBlockState(current).getBlock();

                if (!cluster.contains(current) && volume.contains(current) && block.equals(original))
                    branch.add(current);
            }

            branch.forEach(pos -> cluster.addAll(filterConnectedRecursively(world, pos, original, volume, cluster)));
        }

        return cluster;
    }

    protected static double distance3DVec(BlockPos p1, BlockPos p2) {
        double x = Math.pow(p2.getX() - p1.getX(), 2);
        double y = Math.pow(p2.getY() - p1.getY(), 2);
        double z = Math.pow(p2.getZ() - p1.getZ(), 2);
        return Math.abs(Math.sqrt(x + y + z));
    }
}