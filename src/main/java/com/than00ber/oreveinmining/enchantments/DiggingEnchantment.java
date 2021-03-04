package com.than00ber.oreveinmining.enchantments;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import java.util.HashSet;
import java.util.Set;

public class DiggingEnchantment extends TerrainEnchantmentBase {

    public DiggingEnchantment() {
        super(Rarity.UNCOMMON, ToolType.SHOVEL, ToolType.PICKAXE);
    }

    @Override
    public boolean canApplyTogether(Enchantment enchantment) {
        return super.canApplyTogether(enchantment) || enchantment instanceof ClusterEnchantment;
    }

    @Override
    public Set<BlockPos> getBlocks(Integer level, World world, BlockPos origin, Block original) {
        int r = this.getMaxEffectiveRadius(level);
        Set<BlockPos> cluster = new HashSet<>();

        BlockPos start = new BlockPos(origin.getX() - r, origin.getY() - r, origin.getZ() - r);
        BlockPos finish = new BlockPos(origin.getX() + r, origin.getY() + r, origin.getZ() + r);

        for (BlockPos current : BlockPos.getAllInBoxMutable(start, finish)) {
            BlockPos pos = new BlockPos(current);
            Block block = world.getBlockState(pos).getBlock();

            if (origin.withinDistance(pos, r) && block.equals(original))
                cluster.add(pos);
        }

        return cluster;
    }
}
