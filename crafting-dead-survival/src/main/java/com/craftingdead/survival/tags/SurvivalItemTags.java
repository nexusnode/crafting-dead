package com.craftingdead.survival.tags;

import com.craftingdead.survival.CraftingDeadSurvival;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class SurvivalItemTags {

  public static final TagKey<Item> ZOMBIE_CLOTHING_LOOT = bind("zombie_clothing_loot");
  public static final TagKey<Item> ZOMBIE_HAT_LOOT = bind("zombie_hat_loot");
  public static final TagKey<Item> ZOMBIE_HAND_LOOT = bind("zombie_hand_loot");

  private static TagKey<Item> bind(String name) {
    return ItemTags.create(new ResourceLocation(CraftingDeadSurvival.ID, name));
  }
}
