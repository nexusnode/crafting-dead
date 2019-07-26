package com.craftingdead.mod.block;

import java.util.ArrayList;
import java.util.List;
import com.craftingdead.mod.CraftingDead;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

public class ModBlocks {

  private static final List<Block> toRegister = new ArrayList<>();

  public static Block residentialLoot = null;

  public static void initialize() {
    residentialLoot = add("residential_loot",
        new LootBlock(0xFFFFFF, ImmutableMap.of(new ResourceLocation("stone"), 1)));
  }

  public static void register(RegistryEvent.Register<Block> event) {
    toRegister.forEach(event.getRegistry()::register);
  }

  private static Block add(String registryName, Block block) {
    toRegister.add(block.setRegistryName(new ResourceLocation(CraftingDead.ID, registryName)));
    return block;
  }
}
