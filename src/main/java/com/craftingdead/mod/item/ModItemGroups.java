package com.craftingdead.mod.item;

import com.craftingdead.mod.CraftingDead;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroups {

  public static final ItemGroup CRAFTING_DEAD_CONSUMABLE = (new ItemGroup(CraftingDead.ID) {

    @Override
    public ItemStack createIcon() {
      return new ItemStack(ModItems.POWER_BAR);
    }
  });
}
