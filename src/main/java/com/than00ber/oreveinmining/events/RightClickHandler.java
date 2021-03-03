package com.than00ber.oreveinmining.events;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import static com.than00ber.oreveinmining.OreVeinMining.RegistryEvents.DEBUG;

public class RightClickHandler {

    @SubscribeEvent
    public void onRightClickEvent(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        PlayerEntity player = event.getPlayer();
        ItemStack heldItem = player.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
        Integer enchantmentLevel = EnchantmentHelper.getEnchantments(heldItem).get(DEBUG);

        Block block = Blocks.GOLD_ORE;
        int size = 30;

        if (enchantmentLevel != null) {
            BlockPos from = event.getPos();

            Pair<Integer, Pair<Integer, Integer>> grid = new ImmutablePair<>(size, new ImmutablePair<>(size, size)); // y x z
            world.setBlockState(from, block.getDefaultState(), 2);

            for (int x = 0; x < grid.getRight().getLeft(); x++)
                for (int y = 0; y < grid.getRight().getRight(); y++)
                    for (int z = 0; z < grid.getLeft(); z++) {
                        BlockPos current = from.add(x, y, z);
                        world.setBlockState(current, block.getDefaultState(), 2);
                    }
        }
    }
}
