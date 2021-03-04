package com.than00ber.oreveinmining.events;

import com.than00ber.oreveinmining.enchantments.CarverEnchantmentBase;
import com.than00ber.oreveinmining.enchantments.ITerrainFormer;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class RightClickHandler {

    @SubscribeEvent
    public void onRightClickEvent(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();
        ItemStack heldItem = player.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(heldItem);

        if (player instanceof ServerPlayerEntity) {

            for (Enchantment enchantment : enchantments.keySet()) {

                if (!player.isSneaking() || !player.isCrouching()) {
                    int lvl = enchantments.get(enchantment);
                    BlockPos pos = event.getPos();
                    World world = event.getWorld();
                    BlockState state = world.getBlockState(pos);

                    if (enchantment instanceof ITerrainFormer) {
                        ITerrainFormer iTerrainFormer = (ITerrainFormer) enchantment;

                        if (iTerrainFormer.isCreativeOnly() && !player.isCreative())
                            return;

                        if (enchantment instanceof CarverEnchantmentBase) {
                            CarverEnchantmentBase ench = (CarverEnchantmentBase) enchantment;

                            if (ench.isValidTargetBlockState(state, heldItem)) {
                                Set<BlockPos> area = ench.getAffectedVolume(heldItem, ench.getMaxEffectiveRadius(lvl), world, pos);
                                AtomicBoolean notBroken = new AtomicBoolean(true);

                                for (BlockPos blockPos : area) {

                                    if (notBroken.get()) {
                                        ((ITerrainFormer) ench).transformBlock(heldItem, lvl, world, blockPos);
                                        heldItem.damageItem(1, player, p -> notBroken.set(true));
                                    }
                                }
                            }
                        }
                        else {
                            iTerrainFormer.transformBlock(heldItem, lvl, world, pos);
                        }
                    }
                }
            }
        }
    }
}
