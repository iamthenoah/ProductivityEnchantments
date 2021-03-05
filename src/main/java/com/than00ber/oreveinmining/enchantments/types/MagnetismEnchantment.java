package com.than00ber.oreveinmining.enchantments.types;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public class MagnetismEnchantment extends Enchantment {

    public MagnetismEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentType.DIGGER, new EquipmentSlotType[] { EquipmentSlotType.MAINHAND });
    }
}
