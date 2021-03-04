package com.than00ber.oreveinmining.enchantments;

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

import java.util.HashSet;
import java.util.Set;

public class CarverEnchantmentBase extends Enchantment {

    protected final ToolType TOOL_TYPE;

    protected CarverEnchantmentBase(Rarity rarity, ToolType type) {
        super(rarity, EnchantmentType.DIGGER, new EquipmentSlotType[] { EquipmentSlotType.MAINHAND });
        this.TOOL_TYPE = type;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public boolean canApplyTogether(Enchantment enchantment) {
        return super.canApplyTogether(enchantment) && !(enchantment instanceof CarverEnchantmentBase);
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return stack.getToolTypes().contains(TOOL_TYPE);
    }

    public boolean dropItemAtOrigin() {
        return false;
    }

    public int getMaxEffectiveRadius(int level) {
        return (int) Math.pow(2, level);
    }

    public boolean isValidTargetBlockState(BlockState state, ItemStack stack) {
        return state.isToolEffective(TOOL_TYPE) && state.getBlock() != Blocks.BEDROCK;
    }

    public Set<BlockPos> getAffectedVolume(ItemStack stack, int radius, World world, BlockPos origin) {
        Set<BlockPos> volume = new HashSet<>();

        BlockPos start = new BlockPos(origin.getX() - radius, origin.getY() - radius, origin.getZ() - radius);
        BlockPos finish = new BlockPos(origin.getX() + radius, origin.getY() + radius, origin.getZ() + radius);

        for (BlockPos current : BlockPos.getAllInBoxMutable(start, finish)) {
            BlockPos pos = new BlockPos(current);

            if (origin.withinDistance(pos, radius))
                volume.add(pos);
        }

//        List<BlockPos> cluster = new ArrayList<>(
//                this.getBlockPosRecursively(world, origin, volume, new HashSet<>())
//        );
//
//        cluster.sort((p1, p2) -> {
//            double d1 = distance3DVec(p1, origin);
//            double d2 = distance3DVec(p2, origin);
//            return Double.compare(d1, d2);
//        });

        return new HashSet<>(volume);
    }

    public Set<BlockPos> getBlockPosRecursively(ItemStack stack, World world, BlockPos origin, Set<BlockPos> volume, Set<BlockPos> cluster) {
        cluster.add(origin);

        if (cluster.size() < volume.size()) {
            Set<BlockPos> branch = new HashSet<>();

            for (Direction direction : Direction.values()) {
                BlockPos current = origin.offset(direction);
                BlockState state = world.getBlockState(current);

                System.out.println(cluster.size() + " " + current);

                if (!cluster.contains(current) && volume.contains(current) && this.isValidTargetBlockState(state, stack))
                    branch.add(current);
            }

            branch.forEach(pos -> cluster.addAll(getBlockPosRecursively(stack, world, pos, volume, cluster)));
        }


        return cluster;
    }

    private static double distance3DVec(BlockPos p1, BlockPos p2) {
        double x = Math.pow(p2.getX() - p1.getX(), 2);
        double y = Math.pow(p2.getY() - p1.getY(), 2);
        double z = Math.pow(p2.getZ() - p1.getZ(), 2);
        return Math.abs(Math.sqrt(x + y + z));
    }
}
