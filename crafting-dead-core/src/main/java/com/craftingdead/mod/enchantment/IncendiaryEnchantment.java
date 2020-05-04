package com.craftingdead.mod.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class IncendiaryEnchantment extends Enchantment {

  protected IncendiaryEnchantment(Rarity rarity, EquipmentSlotType... slotType) {
    super(rarity, ModEnchantmentTypes.MAGAZINE, slotType);
  }
}
