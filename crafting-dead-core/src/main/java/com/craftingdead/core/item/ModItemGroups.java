package com.craftingdead.core.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroups {

  public static final ItemGroup CRAFTING_DEAD_MED = (new ItemGroup("craftingdead.med") {

    @Override
    public ItemStack createIcon() {
      return new ItemStack(ModItems.FIRST_AID_KIT::get);
    }
  });

  public static final ItemGroup CRAFTING_DEAD_CLOTHING = (new ItemGroup("craftingdead.clothing") {

    @Override
    public ItemStack createIcon() {
      return new ItemStack(ModItems.BUILDER_CLOTHING::get);
    }
  });

  public static final ItemGroup CRAFTING_DEAD_COMBAT = (new ItemGroup("craftingdead.combat") {

    @Override
    public ItemStack createIcon() {
      return new ItemStack(ModItems.AK47::get);
    }
  });

  public static final ItemGroup CRAFTING_DEAD_MISC = (new ItemGroup("craftingdead.misc") {

    @Override
    public ItemStack createIcon() {
      return new ItemStack(ModItems.MEDICAL_DROP_RADIO::get);
    }
  });
}
