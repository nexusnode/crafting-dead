package com.craftingdead.mod.item;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroups {

  public static final ItemGroup CRAFTING_DEAD_FOOD = (new ItemGroup("craftingdead.consumables") {

    @Override
    public ItemStack createIcon() {
      return new ItemStack(ModItems.POWER_BAR::get);
    }
  });

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
      return new ItemStack(ModItems.CAN_OPENER::get);
    }
  });

  public static final ItemGroup CRAFTING_DEAD_BLOCKS = (new ItemGroup("craftingdead.blocks") {

    @Override
    public ItemStack createIcon() {
      return new ItemStack(Blocks.ANDESITE);
    }
  });
}
