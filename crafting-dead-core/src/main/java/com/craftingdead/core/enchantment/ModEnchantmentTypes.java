package com.craftingdead.core.enchantment;

import com.craftingdead.core.item.MagazineItem;
import net.minecraft.enchantment.EnchantmentType;

public class ModEnchantmentTypes {

  public static final EnchantmentType MAGAZINE =
      EnchantmentType.create("magazine", item -> item instanceof MagazineItem);
}
