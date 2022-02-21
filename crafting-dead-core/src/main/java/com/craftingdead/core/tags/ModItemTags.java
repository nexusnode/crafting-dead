/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.tags;

import com.craftingdead.core.CraftingDead;
import net.minecraft.world.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.Tags.IOptionalNamedTag;

public class ModItemTags {

  public static final IOptionalNamedTag<Item> SYRINGES = tag("syringes");
  public static final IOptionalNamedTag<Item> CLOTHING = tag("clothing");
  public static final IOptionalNamedTag<Item> MELEES = tag("melees");
  public static final IOptionalNamedTag<Item> HATS = tag("hats");
  public static final IOptionalNamedTag<Item> MAGAZINES = tag("magazines");

  private static IOptionalNamedTag<Item> tag(String name) {
    return ItemTags.createOptional(new ResourceLocation(CraftingDead.ID, name));
  }
}
