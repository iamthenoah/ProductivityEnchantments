package com.than00ber.oreveinmining.enchantments.types;

import com.than00ber.oreveinmining.CarvedVolume;
import com.than00ber.oreveinmining.enchantments.CarverEnchantmentBase;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import java.util.Collections;
import java.util.Set;

import static com.than00ber.oreveinmining.OreVeinMining.RegistryEvents.CULTIVATION;

public class CultivationEnchantment extends CarverEnchantmentBase {

    public CultivationEnchantment() {
        super(Rarity.RARE, ToolType.HOE);
    }

    @Override
    public boolean canApplyTogether(Enchantment enchantment) {
        if (enchantment instanceof CarverEnchantmentBase)
            return ((CarverEnchantmentBase) enchantment).getToolType().equals(ToolType.HOE);
        return super.canApplyTogether(enchantment);
    }

    @Override
    public boolean isBlockValid(BlockState state, ItemStack stack, ToolType type) {
        return state.getBlock() instanceof CropsBlock && state.get(CropsBlock.AGE) >= Collections.max(CropsBlock.AGE.getAllowedValues());
    }

    @Override
    public Set<BlockPos> getRemoveVolume(ItemStack stack, int level, CarverEnchantmentBase enchantment, World world, BlockPos origin) {
        int radius = this.getMaxEffectiveRadius(level);

        CarvedVolume volume = new CarvedVolume(CarvedVolume.Shape.DISC, radius, origin, world)
                .setToolRestrictions(stack, CULTIVATION.getToolType())
                .filterViaCallback(CULTIVATION)
                .sortNearestToOrigin();

        return volume.getVolume();
    }
}
