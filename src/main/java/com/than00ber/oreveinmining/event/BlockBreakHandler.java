package com.than00ber.oreveinmining.event;

import com.than00ber.oreveinmining.enchantments.ClusterEnchantment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashSet;
import java.util.Set;

import static com.than00ber.oreveinmining.OreVeinMining.RegistryEvents.CLUSTER_ENCHANTMENT;

public class BlockBreakHandler {

    @SubscribeEvent
    public void onBlockBreakEvent(BlockEvent.BreakEvent event) {
        PlayerEntity player = event.getPlayer();
        ItemStack heldItem = player.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
        Integer clusterEnchantLevel = EnchantmentHelper.getEnchantments(heldItem).get(CLUSTER_ENCHANTMENT);

        if (heldItem.getItem() instanceof PickaxeItem && clusterEnchantLevel != null) {

            if (!player.isSneaking() || !player.isCrouching()) {
                BlockPos pos = event.getPos();
                World world = (World) event.getWorld();
                BlockState state = world.getBlockState(pos);

                if (heldItem.canHarvestBlock(state) && ClusterEnchantment.canApplyToBlock(state)) {
                    Block block = state.getBlock();
                    Set<BlockPos> cluster = getCluster(clusterEnchantLevel, world, pos, block, new HashSet<>());

                    // TODO - SORT <cluster> BY CLOSEST TO PLAYER

                    for (BlockPos blockPos : cluster) {

                        if (!player.isCreative()) {
                            block.harvestBlock(world, player, blockPos, state, null, new ItemStack(block.asItem()));
                            heldItem.attemptDamageItem(1, world.rand, (ServerPlayerEntity) player);

                            if (heldItem.getDamage() > heldItem.getMaxDamage()) break;
                        }

                        world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                    }
                }
            }
        }
    }

    private static Set<BlockPos> getCluster(int level, World world, BlockPos pos, Block original, Set<BlockPos> cluster) {
        cluster.add(pos);

        if (cluster.size() < ClusterEnchantment.getMaxBranchSize(level)) {

            Set<BlockPos> branch = new HashSet<>();
            for (Direction direction : Direction.values()) {
                BlockPos current = pos.offset(direction);
                Block block = world.getBlockState(current).getBlock();

                if (block.equals(original) && !cluster.contains(current))
                    branch.add(current);
            }

            for (BlockPos blockPos : branch)
                cluster.addAll(getCluster(level, world, blockPos, original, cluster));
        }

        return cluster;
    }
}
