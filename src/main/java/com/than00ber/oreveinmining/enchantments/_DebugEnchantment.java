package com.than00ber.oreveinmining.enchantments;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.GameData;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class _DebugEnchantment extends Enchantment implements ITerrainFormer {

    public _DebugEnchantment() {
        super(Rarity.RARE, EnchantmentType.DIGGER, new EquipmentSlotType[] { EquipmentSlotType.MAINHAND });
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return stack.getItem() == Items.STICK || stack.getItem() instanceof BlockItem;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public void onRightClick(ItemStack stack, int level, World world, BlockPos hit) {
        int r = (int) Math.pow(2, level);
        Block block = null;

        if (stack.getItem() instanceof BlockItem)
            block = ((BlockItem) stack.getItem()).getBlock();

        for (int x = 0; x < r; x++)
            for (int y = 0; y < r; y++)
                for (int z = 0; z < r; z++)
                    world.setBlockState(hit.add(x, y, z), getBlock(block).getDefaultState(), 2);
    }

    private static Block getBlock(Block block) {
        if (block != null) return block;

        Random r = new Random();
        Map<Block, Item> blockItemMap = GameData.getBlockItemMap();
        for (Block b : blockItemMap.keySet()) {
            if (r.nextInt(blockItemMap.size()) == 0)
                return b.getBlock();
        }

        return Blocks.DIRT;
    }
}
