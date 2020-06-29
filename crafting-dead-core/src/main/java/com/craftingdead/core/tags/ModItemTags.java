package com.craftingdead.core.tags;

import com.craftingdead.core.CraftingDead;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class ModItemTags {

  public static final Tag<Item> SYRINGES = tag("syringes");
  public static final Tag<Item> CLOTHING = tag("clothing");
  public static final Tag<Item> MELEES = tag("melees");
  public static final Tag<Item> HATS = tag("hats");

  private static Tag<Item> tag(String name) {
    return new ItemTags.Wrapper(new ResourceLocation(CraftingDead.ID, name));
  }
}
