package com.craftingdead.core.data;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import com.craftingdead.core.world.storage.loot.ModLootTables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootParameterSet;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraft.world.storage.loot.ValidationTracker;

public class ModLootTableProvider extends LootTableProvider {

  public ModLootTableProvider(DataGenerator dataGenerator) {
    super(dataGenerator);
  }

  @Override
  protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
    ImmutableList.Builder<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> builder =
        ImmutableList.builder();
    builder.add(Pair.of(ModChestLootTables::new, LootParameterSets.CHEST));
    return builder.build();
  }

  @Override
  protected void validate(Map<ResourceLocation, LootTable> map,
      ValidationTracker validationTracker) {
    for (ResourceLocation location : Sets.difference(ModLootTables.getLootTables(),
        map.keySet())) {
      validationTracker.addProblem("Missing built-in table: " + location);
    }
    map.forEach(
        (location, lootTable) -> LootTableManager.func_227508_a_(validationTracker, location,
            lootTable));
  }

  @Override
  public String getName() {
    return "Crafting Dead Loot Tables";
  }
}
