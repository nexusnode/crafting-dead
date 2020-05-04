package com.craftingdead.mod.enchantment;

import com.craftingdead.mod.item.MagazineItem;
import net.minecraft.enchantment.EnchantmentType;

public class ModEnchantmentTypes {

  public static final EnchantmentType MAGAZINE =
      EnchantmentType.create("magazine", item -> item instanceof MagazineItem);
}
