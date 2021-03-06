package com.than00ber.productivityenchantments.enchantments.types;

import com.than00ber.productivityenchantments.CarvedVolume;
import com.than00ber.productivityenchantments.enchantments.CarverEnchantmentBase;
import com.than00ber.productivityenchantments.IValidatorCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import java.util.Set;

import static com.than00ber.productivityenchantments.ProductivityEnchantments.RegistryEvents.WOODCUTTING;

public class WoodcuttingEnchantment extends CarverEnchantmentBase {

    public WoodcuttingEnchantment() {
        super(Rarity.COMMON, ToolType.AXE);
    }

    @Override
    public boolean isBlockValid(BlockState state, ItemStack stack, ToolType type) {
        return state.isToolEffective(type) || state.getBlock() instanceof LeavesBlock;
    }

    @Override
    public Set<BlockPos> getRemoveVolume(ItemStack stack, int level, CarverEnchantmentBase enchantment, World world, BlockPos origin) {
        BlockState state = world.getBlockState(origin);
        boolean isTreeLog = state.getBlock() instanceof RotatedPillarBlock;
        int radius = isTreeLog ? 32 : enchantment.getMaxEffectiveRadius(level);

        CarvedVolume volume = new CarvedVolume(CarvedVolume.Shape.SPHERICAL, radius, origin, world)
                .setToolRestrictions(stack, WOODCUTTING.getToolType())
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

        return volume
                .filterConnectedRecursively()
                .sortNearestToOrigin()
                .getVolume();
    }
}
