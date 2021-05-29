/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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

package com.craftingdead.survival.world.level.storage.loot;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import com.craftingdead.survival.CraftingDeadSurvival;
import net.minecraft.util.ResourceLocation;

public class BuiltInLootTables {

  private static final Set<ResourceLocation> lootTables = new HashSet<>();
  private static final Set<ResourceLocation> READ_ONLY_LOOT_TABLES =
      Collections.unmodifiableSet(lootTables);

  public static final ResourceLocation MEDICAL_SUPPLY_DROP =
      register("supply_drops/medical");
  public static final ResourceLocation MILITARY_SUPPLY_DROP =
      register("supply_drops/military");

  private static ResourceLocation register(String location) {
    return register(new ResourceLocation(CraftingDeadSurvival.ID, location));
  }

  private static ResourceLocation register(ResourceLocation location) {
    if (lootTables.add(location)) {
      return location;
    } else {
      throw new IllegalArgumentException(
          location + " is already a registered built-in loot table");
    }
  }

  public static Set<ResourceLocation> getLootTables() {
    return READ_ONLY_LOOT_TABLES;
  }
}
