package com.craftingdead.mod.item;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.block.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroups {

  public static final ItemGroup CRAFTING_DEAD = (new ItemGroup(CraftingDead.MOD_ID) {

    @Override
    public ItemStack createIcon() {
      return new ItemStack(ModBlocks.RESIDENTIAL_LOOT);
    }
  });
}
