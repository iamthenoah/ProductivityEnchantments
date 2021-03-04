package com.than00ber.oreveinmining.events;

import com.than00ber.oreveinmining.enchantments.ITerrainFormer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;

public class RightClickHandler {

    @SubscribeEvent
    public void onRightClickEvent(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();
        ItemStack heldItem = player.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(heldItem);
        World world = event.getWorld();

        for (Enchantment enchantment : enchantments.keySet()) {

            if (enchantment instanceof ITerrainFormer) {
                ITerrainFormer ench = (ITerrainFormer) enchantment;

                if (!player.isSneaking() || !player.isCrouching()) {
                    BlockPos pos = event.getPos();

                    ench.onRightClick(heldItem, enchantments.get(ench), world, pos);

                    heldItem.setAnimationsToGo(3);
                }
            }
        }
    }
}
