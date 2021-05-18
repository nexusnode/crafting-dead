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

package com.craftingdead.core.world.level.storage.loot;

import java.util.Collections;
import java.util.Set;
import com.craftingdead.core.CraftingDead;
import com.google.common.collect.Sets;
import net.minecraft.util.ResourceLocation;

public class ModLootTables {

  private static final Set<ResourceLocation> LOOT_TABLES = Sets.newHashSet();
  private static final Set<ResourceLocation> READ_ONLY_LOOT_TABLES =
      Collections.unmodifiableSet(LOOT_TABLES);
  public static final ResourceLocation MEDICAL_SUPPLY_DROP =
      register("supply_drops/medical");
  public static final ResourceLocation MILITARY_SUPPLY_DROP =
      register("supply_drops/military");

  private static ResourceLocation register(String location) {
    return register(new ResourceLocation(CraftingDead.ID, location));
  }

  private static ResourceLocation register(ResourceLocation location) {
    if (LOOT_TABLES.add(location)) {
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
