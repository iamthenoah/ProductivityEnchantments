package com.than00ber.oreveinmining.enchantments.types;

import com.than00ber.oreveinmining.enchantments.CarvedVolume;
import com.than00ber.oreveinmining.enchantments.CarverEnchantmentBase;
import com.than00ber.oreveinmining.enchantments.IValidatorCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import java.util.Set;

import static com.than00ber.oreveinmining.OreVeinMining.RegistryEvents.WOODCUTTING;

public class WoodcuttingEnchantment extends CarverEnchantmentBase {

    public WoodcuttingEnchantment() {
        super(Rarity.COMMON, ToolType.AXE);
    }

    @Override
    public boolean isBlockValid(BlockState state, ItemStack stack, ToolType type) {
        return state.isToolEffective(type) || state.getBlock() instanceof LeavesBlock;
    }

    @Override
    public Set<BlockPos> getVolume(ItemStack stack, int level, CarverEnchantmentBase enchantment, World world, BlockPos origin) {
        BlockState state = world.getBlockState(origin);
        boolean isTreeLog = state.getBlock() instanceof RotatedPillarBlock;
        int radius = isTreeLog ? 32 : enchantment.getMaxEffectiveRadius(level);

        CarvedVolume volume = new CarvedVolume(CarvedVolume.Shape.SPHERICAL, radius, origin, world)
                .setToolRestrictionItem(stack)
                .setToolRestrictionType(ToolType.AXE)
                .filterViaCallback(WOODCUTTING);

        if (isTreeLog) {
            volume.filterBy(state);
        }
        else {
            IValidatorCallback callback = new IValidatorCallback() {
                @Override
                public boolean isBlockValid(BlockState state, ItemStack stack, ToolType type) {
                    return !(state.getBlock() instanceof RotatedPillarBlock);
                }
            };

            volume.filterViaCallback(callback);
        }

        volume.filterConnectedRecursively();

        return volume.getVolume();
    }
}
