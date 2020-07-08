package com.craftingdead.core.world.storage.loot;

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
