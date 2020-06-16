package com.craftingdead.core.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class InfectionEnchantment extends Enchantment {

  protected InfectionEnchantment(Rarity rarity, EquipmentSlotType... slotType) {
    super(rarity, ModEnchantmentTypes.MAGAZINE, slotType);
  }
}
