package com.craftingdead.mod.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroups {

  public static final ItemGroup CRAFTING_DEAD_CONSUMABLES =
      (new ItemGroup("craftingdead_consumables") {

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

  public static final ItemGroup CRAFTING_DEAD_WEAPON = (new ItemGroup("craftingdead_weapon") {

    @Override
    public ItemStack createIcon() {
      return new ItemStack(ModItems.CROWBAR::get);
    }
  });

  public static final ItemGroup CRAFTING_DEAD_WEARABLE = (new ItemGroup("craftingdead_wearable") {

    @Override
    public ItemStack createIcon() {
      return new ItemStack(ModItems.SMALL_RED_BACKPACK::get);
    }
  });

  public static final ItemGroup CRAFTING_DEAD_GENERAL = (new ItemGroup("craftingdead_wearable") {

    @Override
    public ItemStack createIcon() {
      return new ItemStack(ModItems.LARGE_TAN_BACKPACK::get);
    }
  });
}
