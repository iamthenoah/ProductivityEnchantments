package com.than00ber.oreveinmining.events;

import com.than00ber.oreveinmining.enchantments.CarverEnchantmentBase;
import com.than00ber.oreveinmining.enchantments.IRightClickEffect;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
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

        if (player instanceof ServerPlayerEntity) {
            boolean hasPerformedCarvingAction = false;

            for (Enchantment enchantment : enchantments.keySet()) {
                if (hasPerformedCarvingAction) return;

                if (!player.isSneaking() || !player.isCrouching()) {

                    if (enchantment instanceof IRightClickEffect) {
                        int lvl = enchantments.get(enchantment);
                        BlockPos pos = event.getPos();
                        World world = event.getWorld();
                        Direction facing = event.getFace();
                        IRightClickEffect iRightClickEffect = (IRightClickEffect) enchantment;

                        if (iRightClickEffect.isCreativeOnly() && !player.isCreative())
                            return;

                        if (enchantment instanceof CarverEnchantmentBase) {
                            CarverEnchantmentBase carverEnchantmentBase = ((CarverEnchantmentBase) enchantment);

                            if (carverEnchantmentBase.isTargetValid(world.getBlockState(pos), heldItem)) {
                                iRightClickEffect.onRightClick(heldItem, lvl, facing, carverEnchantmentBase, world, pos, player);
                                hasPerformedCarvingAction = true;
                            }
                        }
                        else {
                            iRightClickEffect.onRightClick(heldItem, lvl, facing, world, pos, player);
                        }
                    }
                }
            }
        }
    }
}
