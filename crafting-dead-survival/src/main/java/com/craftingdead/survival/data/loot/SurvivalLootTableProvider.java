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

package com.craftingdead.survival.data.loot;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import com.craftingdead.survival.world.level.storage.loot.BuiltInLootTables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.resources.ResourceLocation;

public class SurvivalLootTableProvider extends LootTableProvider {

  public SurvivalLootTableProvider(DataGenerator dataGenerator) {
    super(dataGenerator);
  }

  @Override
  protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
    ImmutableList.Builder<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> builder =
        ImmutableList.builder();
    builder.add(Pair.of(SupplyDropLootTables::new, LootContextParamSets.CHEST));
    builder.add(Pair.of(SurvivalBlockLootTables::new, LootContextParamSets.BLOCK));
    return builder.build();
  }

  @Override
  protected void validate(Map<ResourceLocation, LootTable> map,
      ValidationContext validationTracker) {
    for (ResourceLocation location : Sets.difference(BuiltInLootTables.getLootTables(),
        map.keySet())) {
      validationTracker.reportProblem("Missing built-in table: " + location);
    }
    map.forEach((location, lootTable) -> LootTables.validate(validationTracker,
        location, lootTable));
  }

  @Override
  public String getName() {
    return "Crafting Dead Loot Tables";
  }
}
