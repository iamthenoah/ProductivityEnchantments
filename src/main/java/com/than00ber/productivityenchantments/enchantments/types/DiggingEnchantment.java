package com.than00ber.productivityenchantments.enchantments.types;

import com.than00ber.productivityenchantments.CarvedVolume;
import com.than00ber.productivityenchantments.enchantments.CarverEnchantmentBase;
import com.than00ber.productivityenchantments.IValidatorCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;

import java.util.Set;

import static com.than00ber.productivityenchantments.ProductivityEnchantments.RegistryEvents.CLUSTER;
import static com.than00ber.productivityenchantments.ProductivityEnchantments.RegistryEvents.DIGGING;

public class DiggingEnchantment extends CarverEnchantmentBase {

    public DiggingEnchantment() {
        super(Rarity.UNCOMMON, ToolType.SHOVEL);
    }

    @Override
    public boolean canApplyTogether(Enchantment enchantment) {
        return super.canApplyTogether(enchantment) || enchantment instanceof ClusterEnchantment;
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return stack.getItem() instanceof PickaxeItem || stack.getItem() instanceof ShovelItem;
    }

    @Override
    public boolean isBlockValid(BlockState state, World world, BlockPos pos, ItemStack stack, ToolType type) {
        boolean isRocky = (stack.canHarvestBlock(state) || state.isToolEffective(type)) && stack.getItem() instanceof PickaxeItem;
        boolean isDirty = (stack.canHarvestBlock(state) || state.isToolEffective(type)) && stack.getItem() instanceof ShovelItem;
        return isRocky || isDirty;
    }

    @Override
    public Set<BlockPos> getRemoveVolume(ItemStack stack, int level, CarverEnchantmentBase enchantment, World world, BlockPos origin) {
        int radius = enchantment.getMaxEffectiveRadius(level);

        IValidatorCallback callback = new IValidatorCallback() {
            @Override
            public boolean isBlockValid(BlockState state, World world, BlockPos pos, ItemStack stack, ToolType type) {
                boolean isOre = state.isIn(Tags.Blocks.ORES) || state.getBlock() instanceof OreBlock;
                return stack.canHarvestBlock(state.getBlockState()) && !isOre;
            }
        };


        CarvedVolume area = new CarvedVolume(CarvedVolume.Shape.SPHERICAL, radius, origin, world)
                .setToolRestrictions(stack, enchantment.getToolType())
                .filterViaCallback(callback)
                .filterConnectedRecursively()
                .sortNearestToOrigin();

        return area.getVolume();
    }
}
