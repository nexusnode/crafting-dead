package com.craftingdead.mod.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroups {

  public static final ItemGroup CRAFTING_DEAD_CONSUMABLE =
      (new ItemGroup("craftingdead_consumable") {

        @Override
        public ItemStack createIcon() {
          return new ItemStack(ModItems.POWER_BAR);
        }
      });
}
