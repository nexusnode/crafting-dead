/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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

package com.craftingdead.survival.world.level.block;

import com.craftingdead.survival.CraftingDeadSurvival;
import com.craftingdead.survival.particles.SurvivalParticleTypes;
import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SurvivalBlocks {

  public static final DeferredRegister<Block> BLOCKS =
      DeferredRegister.create(ForgeRegistries.BLOCKS, CraftingDeadSurvival.ID);

  public static final RegistryObject<Block> MILITARY_LOOT =
      BLOCKS.register("military_loot",
          () -> new LootBlock(BlockBehaviour.Properties.of(Material.STONE)
              .strength(5.0F, 5.0F)
              .noOcclusion()));

  public static final RegistryObject<Block> MEDICAL_LOOT =
      BLOCKS.register("medic_loot",
          () -> new LootBlock(BlockBehaviour.Properties.of(Material.STONE)
              .strength(5.0F, 5.0F)
              .noOcclusion()));

  public static final RegistryObject<Block> CIVILIAN_LOOT =
      BLOCKS.register("civilian_loot",
          () -> new LootBlock(BlockBehaviour.Properties.of(Material.STONE)
              .strength(5.0F, 5.0F)
              .noOcclusion()));

  public static final RegistryObject<Block> RARE_CIVILIAN_LOOT =
      BLOCKS.register("civilian_rare_loot",
          () -> new LootBlock(BlockBehaviour.Properties.of(Material.STONE)
              .strength(5.0F, 5.0F)
              .noOcclusion()));

  public static final RegistryObject<Block> POLICE_LOOT =
      BLOCKS.register("police_loot",
          () -> new LootBlock(BlockBehaviour.Properties.of(Material.STONE)
              .strength(5.0F, 5.0F)
              .noOcclusion()));

  public static final RegistryObject<Block> MILITARY_LOOT_GENERATOR =
      BLOCKS.register("military_loot_gen",
          () -> lootGenerator(MILITARY_LOOT, SurvivalParticleTypes.MILITARY_LOOT_GEN,
              CraftingDeadSurvival.serverConfig.lootMilitaryRespawnTick::get,
              CraftingDeadSurvival.serverConfig.lootMilitaryLootEnabled::get));

  public static final RegistryObject<Block> MEDICAL_LOOT_GENERATOR =
      BLOCKS.register("medic_loot_gen",
          () -> lootGenerator(MEDICAL_LOOT, SurvivalParticleTypes.MEDIC_LOOT_GEN,
              CraftingDeadSurvival.serverConfig.lootMedicalRespawnTick::get,
              CraftingDeadSurvival.serverConfig.lootMedicalLootEnabled::get));

  public static final RegistryObject<Block> CIVILIAN_LOOT_GENERATOR =
      BLOCKS.register("civilian_loot_gen",
          () -> lootGenerator(CIVILIAN_LOOT, SurvivalParticleTypes.CIVILIAN_LOOT_GEN,
              CraftingDeadSurvival.serverConfig.lootCivilianRespawnTick::get,
              CraftingDeadSurvival.serverConfig.lootCivilianLootEnabled::get));

  public static final RegistryObject<Block> RARE_CIVILIAN_LOOT_GENERATOR =
      BLOCKS.register("civilian_rare_loot_gen",
          () -> lootGenerator(RARE_CIVILIAN_LOOT,
              SurvivalParticleTypes.CIVILIAN_RARE_LOOT_GEN,
              CraftingDeadSurvival.serverConfig.lootCivilianRareRespawnTick::get,
              CraftingDeadSurvival.serverConfig.lootCivilianRareLootEnabled::get));

  public static final RegistryObject<Block> POLICE_LOOT_GENERATOR =
      BLOCKS.register("police_loot_gen",
          () -> lootGenerator(POLICE_LOOT, SurvivalParticleTypes.POLICE_LOOT_GEN,
              CraftingDeadSurvival.serverConfig.lootPoliceRespawnTick::get,
              CraftingDeadSurvival.serverConfig.lootPoliceLootEnabled::get));

  private static LootGeneratorBlock lootGenerator(Supplier<Block> lootBlock,
      Supplier<? extends ParticleOptions> particleOptions, IntSupplier refreshDelayTicks,
      BooleanSupplier enabled) {
    return new LootGeneratorBlock(
        BlockBehaviour.Properties.of(Material.STRUCTURAL_AIR)
            .strength(5.0F, 5.0F)
            .randomTicks()
            .noOcclusion()
            .noCollission(),
        lootBlock, particleOptions, refreshDelayTicks, enabled);
  }
}
