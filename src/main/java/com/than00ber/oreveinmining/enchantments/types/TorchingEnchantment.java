package com.than00ber.oreveinmining.enchantments.types;

import com.than00ber.oreveinmining.enchantments.IRightClickEffect;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.minecraft.block.Blocks.WALL_TORCH;

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
            BlockPos current = origin.offset(facing);
            Block block = world.getBlockState(current).getBlock();

            if (block == Blocks.AIR) {
                BlockState state = world.getBlockState(origin);
                BlockState torch = null;

                if (Blocks.TORCH.isValidPosition(state, world, origin))
                    torch = Blocks.TORCH.getDefaultState();
                else if (WALL_TORCH.isValidPosition(state, world, origin))
                    torch = WALL_TORCH.getDefaultState().with(WallTorchBlock.HORIZONTAL_FACING, facing);

                if (torch != null) world.setBlockState(current, torch);
            }
        }
    }
}
