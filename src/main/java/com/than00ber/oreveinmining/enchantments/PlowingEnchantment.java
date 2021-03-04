package com.than00ber.oreveinmining.enchantments;

import net.minecraft.block.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlowingEnchantment extends TerrainEnchantmentBase implements ITerrainFormer {

    public PlowingEnchantment() {
        super(Rarity.COMMON, ToolType.HOE);
    }

    @Override
    public Set<BlockPos> getBlocks(Integer level, World world, BlockPos origin, Block original) {
        int r = this.getMaxEffectiveRadius(level);
        Set<BlockPos> field = new HashSet<>();

        BlockPos start = new BlockPos(origin.getX() - r, -1.0D, origin.getZ() - r);
        BlockPos finish = new BlockPos(origin.getX() + r, -1.0D, origin.getZ() + r);

        for (BlockPos current : BlockPos.getAllInBoxMutable(start, finish)) {
            BlockPos pos = new BlockPos(current);
            Block block = world.getBlockState(pos).getBlock();

            if (origin.withinDistance(pos, r) && block.equals(original))
                field.add(pos);
        }

        return field;
    }

    @Override
    public void onRightClick(ItemStack stack, int level, World world, BlockPos hit) {
        int radius = this.getMaxEffectiveRadius(level);
        List<BlockPos> field = new ArrayList<>(getFieldCluster(radius, world, hit));
        field.forEach(p -> world.setBlockState(p, Blocks.FARMLAND.getDefaultState()));
    }

    private Set<BlockPos> getFieldCluster(int r, World world, BlockPos origin) {
        Set<BlockPos> field = new HashSet<>();
        BlockPos start = new BlockPos(origin.getX() - r, origin.getY(), origin.getZ() - r);
        BlockPos finish = new BlockPos(origin.getX() + r, origin.getY(), origin.getZ() + r);

        for (BlockPos pos : BlockPos.getAllInBoxMutable(start, finish)) {
            BlockPos current = new BlockPos(pos);
            Block block = world.getBlockState(current).getBlock();
            BlockState above = world.getBlockState(current.up());

            if (origin.withinDistance(current, r) && above.getBlock() == Blocks.AIR)
                if (block instanceof GrassBlock || block == Blocks.DIRT)
                   field.add(current);
        }

        return field;
    }
}
