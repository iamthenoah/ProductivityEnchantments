package com.than00ber.oreveinmining.enchantments.types;

import com.than00ber.oreveinmining.CarvedVolume;
import com.than00ber.oreveinmining.enchantments.CarverEnchantmentBase;
import com.than00ber.oreveinmining.enchantments.IRightClickEffect;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import java.util.HashSet;
import java.util.Set;

import static com.than00ber.oreveinmining.OreVeinMining.RegistryEvents.FERTILITY;

public class FertilityEnchantment extends CarverEnchantmentBase implements IRightClickEffect {

    public FertilityEnchantment() {
        super(Rarity.UNCOMMON, ToolType.HOE);
    }

    @Override
    public boolean canApplyTogether(Enchantment enchantment) {
        if (enchantment instanceof CarverEnchantmentBase)
            return ((CarverEnchantmentBase) enchantment).getToolType().equals(ToolType.HOE);
        return super.canApplyTogether(enchantment);
    }

    @Override
    public boolean isBlockValid(BlockState state, ItemStack stack, ToolType type) {
        return state.getBlock() == Blocks.FARMLAND;
    }

    @Override
    public Set<BlockPos> getRemoveVolume(ItemStack stack, int level, CarverEnchantmentBase enchantment, World world, BlockPos origin) {
        Set<BlockPos> block = new HashSet<>();
        block.add(origin);
        return block;
    }

    @Override
    public void onRightClick(ItemStack stack, int level, Direction facing, CarverEnchantmentBase enchantment, World world, BlockPos origin, PlayerEntity player) {

        if (!player.isSneaking() || !player.isCrouching()) {
            int radius = enchantment.getMaxEffectiveRadius(level);

            CarvedVolume area = new CarvedVolume(CarvedVolume.Shape.DISC, radius, origin, world)
                    .setToolRestrictions(stack, FERTILITY.getToolType())
                    .filterViaCallback(FERTILITY)
                    .filterConnectedRecursively()
                    .sortNearestToOrigin()
                    .shiftBy(0, 1, 0);

            BlockState state = Blocks.WHEAT.getDefaultState().with(CropsBlock.AGE, 7);
            this.performPlacements(world, player, stack, area.getVolume(), state);
        }
    }
}
