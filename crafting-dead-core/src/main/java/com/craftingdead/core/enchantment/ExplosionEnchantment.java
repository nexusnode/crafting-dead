package com.craftingdead.core.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class ExplosionEnchantment extends Enchantment {

  protected ExplosionEnchantment(Rarity rarity, EquipmentSlotType... slotType) {
    super(rarity, ModEnchantmentTypes.MAGAZINE, slotType);
  }
}
