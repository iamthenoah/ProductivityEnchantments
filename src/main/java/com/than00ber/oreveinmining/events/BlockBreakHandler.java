package com.than00ber.oreveinmining.events;

import com.than00ber.oreveinmining.enchantments.TerrainEnchantmentBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;

public class BlockBreakHandler {

    @SubscribeEvent
    public void onBlockBreakEvent(BlockEvent.BreakEvent event) {
        PlayerEntity player = event.getPlayer();
        ItemStack heldItem = player.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(heldItem);

        for (Enchantment enchantment : enchantments.keySet()) {

            if (enchantment instanceof TerrainEnchantmentBase) {
                TerrainEnchantmentBase modEnchantment = (TerrainEnchantmentBase) enchantment;

                if (!player.isSneaking() || !player.isCrouching()) {
                    BlockPos pos = event.getPos();
                    World world = (World) event.getWorld();
                    BlockState state = world.getBlockState(pos);

                    if (modEnchantment.appliesToBlock(state)) {
                        Block block = state.getBlock();
                        int lvl = enchantments.get(modEnchantment);
                        Set<BlockPos> cluster = modEnchantment.getBlocks(lvl, world, pos, block);

                        for (BlockPos blockPos : cluster) {

                            if (!player.isCreative()) {
                                block.harvestBlock(world, player, blockPos, state, null, new ItemStack(block.asItem()));
                                heldItem.attemptDamageItem(1, world.rand, (ServerPlayerEntity) player);

                                Integer silkTouch = enchantments.get(Enchantments.SILK_TOUCH);
                                Integer fortune = enchantments.get(Enchantments.FORTUNE);

//                                block.getExpDrop(state, world, blockPos, silkTouch, fortune);

                                if (heldItem.getDamage() > heldItem.getMaxDamage()) break;
                            }

                            world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                        }

                        modEnchantment.notifyPlayer(player, "Affected: " + cluster.size());
                    }
                }
            }
        }
    }
}
