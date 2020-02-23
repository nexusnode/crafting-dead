package com.craftingdead.mod.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroups {

  public static final ItemGroup CRAFTING_DEAD_FOOD = (new ItemGroup("craftingdead_foodstuffs") {

    @Override
    public ItemStack createIcon() {
      return new ItemStack(ModItems.POWER_BAR::get);
    }
  });

  public static final ItemGroup CRAFTING_DEAD_MISC = (new ItemGroup("craftingdead_misc") {

    @Override
    public ItemStack createIcon() {
      return new ItemStack(ModItems.CAN_OPENER::get);
    }
  });

  public static final ItemGroup CRAFTING_DEAD_MED = (new ItemGroup("craftingdead_med") {

    @Override
    public ItemStack createIcon() {
      return new ItemStack(ModItems.FIRST_AID_KIT::get);
    }
  });

  public static final ItemGroup CRAFTING_DEAD_COMBAT = (new ItemGroup("craftingdead_combat") {

    @Override
    public ItemStack createIcon() {
      return new ItemStack(ModItems.AK47::get);
    }
  });
}
