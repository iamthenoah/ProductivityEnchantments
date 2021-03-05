package com.than00ber.oreveinmining.events;

import com.than00ber.oreveinmining.enchantments.types.MagnetismEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;

public class LivingDropsHandler {

    @SubscribeEvent
    public void onLivingDropsEvent(LivingDropsEvent event) {

        System.out.println(event.getEntity());

        if (event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            ItemStack heldItem = player.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(heldItem);


            for (Enchantment enchantment : enchantments.keySet()) {

                if (enchantment instanceof MagnetismEnchantment) {

                    for (ItemEntity drop : event.getDrops()) {
                        player.entityDropItem(drop.getItem(), 10.0F);
                    }
                }
            }
        }
    }
}
