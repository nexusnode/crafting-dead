package com.craftingdead.core.stats;

import net.minecraft.stats.IStatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class ModStats {

  public static ResourceLocation interactWithGunTable;

  public static void registerCustomStats() {
    interactWithGunTable = registerCustom("interact_with_gun_table", IStatFormatter.DEFAULT);
  }

  private static ResourceLocation registerCustom(String name, IStatFormatter formatter) {
    ResourceLocation resourcelocation = new ResourceLocation(name);
    Registry.register(Registry.CUSTOM_STAT, name, resourcelocation);
    Stats.CUSTOM.get(resourcelocation, formatter);
    return resourcelocation;
  }
}
