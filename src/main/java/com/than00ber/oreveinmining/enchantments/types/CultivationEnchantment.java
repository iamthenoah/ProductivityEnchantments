package com.than00ber.oreveinmining.enchantments.types;

import com.than00ber.oreveinmining.enchantments.CarverEnchantmentBase;
import net.minecraft.block.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import java.util.*;

public class CultivationEnchantment extends CarverEnchantmentBase {

    public CultivationEnchantment() {
        super(Rarity.RARE, ToolType.HOE);
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getMaxEffectiveRadius(int level) {
        return 3;
    }

    @Override
    public boolean isTargetValid(BlockState state, ItemStack stack) {
        return state.getBlock() instanceof CropsBlock && state.get(CropsBlock.AGE) >= Collections.max(CropsBlock.AGE.getAllowedValues());
    }

    @Override
    public Set<BlockPos> getVolume(ItemStack stack, int level, CarverEnchantmentBase enchantment, World world, BlockPos origin) {
        int radius = this.getMaxEffectiveRadius(level);
        Set<BlockPos> area = new HashSet<>();

        BlockPos start = new BlockPos(origin.getX() - radius, origin.getY(), origin.getZ() - radius);
        BlockPos finish = new BlockPos(origin.getX() + radius, origin.getY(), origin.getZ() + radius);

        for (BlockPos current : BlockPos.getAllInBoxMutable(start, finish)) {
            BlockPos pos = new BlockPos(current);
            BlockState state = world.getBlockState(pos);

            if (origin.withinDistance(pos, radius) && this.isTargetValid(state, stack))
                area.add(pos);
        }

        return area;
    }
}
