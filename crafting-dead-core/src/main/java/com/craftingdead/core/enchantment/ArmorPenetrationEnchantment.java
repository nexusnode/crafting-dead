package com.craftingdead.core.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class ArmorPenetrationEnchantment extends Enchantment {

  protected ArmorPenetrationEnchantment(Rarity rarity, EquipmentSlotType... slotType) {
    super(rarity, ModEnchantmentTypes.MAGAZINE, slotType);
  }
}
