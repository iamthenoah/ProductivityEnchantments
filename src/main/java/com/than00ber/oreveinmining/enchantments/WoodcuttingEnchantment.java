package com.than00ber.oreveinmining.enchantments;

import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import java.util.HashSet;
import java.util.Set;

public class WoodcuttingEnchantment extends TerrainEnchantmentBase {

    public WoodcuttingEnchantment() {
        super(Rarity.COMMON, ToolType.AXE);
    }

    @Override
    public Set<BlockPos> getBlocks(Integer level, World world, BlockPos origin, Block original) {
        return getTree(world, origin, original, new HashSet<>());
    }

    private static Set<BlockPos> getTree(World world, BlockPos pos, Block original, Set<BlockPos> logs) {
        logs.add(pos);

        if (logs.size() < 1024) {

            for (Direction direction : Direction.values()) {
                BlockPos current = pos.offset(direction);
                Block block = world.getBlockState(current).getBlock();

                if (block.equals(original) && !logs.contains(current))
                    return getTree(world, current, original, logs);
            }
        }

        return logs;
    }
}
