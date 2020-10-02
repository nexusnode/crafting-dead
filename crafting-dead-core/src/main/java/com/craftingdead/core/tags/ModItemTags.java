/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
