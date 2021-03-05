package com.than00ber.oreveinmining.enchantments.types;

import com.than00ber.oreveinmining.enchantments.IRightClickEffect;
import net.minecraft.block.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TorchingEnchantment extends Enchantment implements IRightClickEffect {

    public TorchingEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentType.DIGGER, new EquipmentSlotType[] { EquipmentSlotType.MAINHAND });
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return super.canApply(stack) && stack.getItem() instanceof PickaxeItem;
    }

    @Override
    public void onRightClick(ItemStack stack, int level, Direction facing, World world, BlockPos origin, PlayerEntity player) {

        if (stack.getItem() instanceof PickaxeItem) {
            PlayerInventory inventory = player.inventory;

            if (!inventory.hasItemStack(new ItemStack(Items.TORCH)) && !player.isCreative())
                return;

            BlockPos current = origin.offset(facing);
            Block block = world.getBlockState(current).getBlock();

            if (block == Blocks.AIR) {
                BlockState state = world.getBlockState(current);
                Direction direction = facing.equals(Direction.DOWN) || facing.equals(Direction.UP)
                        ? player.getHorizontalFacing().getOpposite() : facing;
                BlockState torch = Blocks.TORCH.isValidPosition(state, world, origin) && facing.equals(Direction.UP)
                        ? Blocks.TORCH.getDefaultState()
                        : Blocks.WALL_TORCH.getDefaultState().with(WallTorchBlock.HORIZONTAL_FACING, direction);

                world.setBlockState(current, torch);
            }
        }
    }
}
