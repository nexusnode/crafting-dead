package com.craftingdead.mod.block;

import com.craftingdead.mod.CraftingDead;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(CraftingDead.MOD_ID)
@Mod.EventBusSubscriber(modid = CraftingDead.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {

  public static final Block RESIDENTIAL_LOOT = null;

  @SubscribeEvent
  public static void handle(RegistryEvent.Register<Block> event) {
    event.getRegistry().registerAll(appendRegistryName("residential_loot",
        new LootBlock(0xFFFFFF, ImmutableMap.of(new ResourceLocation("stone"), 1))));
  }

  private static Block appendRegistryName(String registryName, Block block) {
    return block.setRegistryName(new ResourceLocation(CraftingDead.MOD_ID, registryName));
  }
}
