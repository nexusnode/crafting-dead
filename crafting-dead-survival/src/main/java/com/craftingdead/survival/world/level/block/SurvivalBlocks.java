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

package com.craftingdead.survival.world.level.block;

import java.util.function.IntSupplier;
import java.util.function.Supplier;
import com.craftingdead.survival.CraftingDeadSurvival;
import com.craftingdead.survival.particles.SurvivalParticleTypes;
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
              CraftingDeadSurvival.serverConfig.militaryLootRefreshDelayTicks::get));

  public static final RegistryObject<Block> MEDICAL_LOOT_GENERATOR =
      BLOCKS.register("medic_loot_gen",
          () -> lootGenerator(MEDICAL_LOOT, SurvivalParticleTypes.MEDIC_LOOT_GEN,
              CraftingDeadSurvival.serverConfig.medicalLootRefreshDelayTicks::get));

  public static final RegistryObject<Block> CIVILIAN_LOOT_GENERATOR =
      BLOCKS.register("civilian_loot_gen",
          () -> lootGenerator(CIVILIAN_LOOT, SurvivalParticleTypes.CIVILIAN_LOOT_GEN,
              CraftingDeadSurvival.serverConfig.civilianLootRefreshDelayTicks::get));

  public static final RegistryObject<Block> RARE_CIVILIAN_LOOT_GENERATOR =
      BLOCKS.register("civilian_rare_loot_gen",
          () -> lootGenerator(RARE_CIVILIAN_LOOT,
              SurvivalParticleTypes.CIVILIAN_RARE_LOOT_GEN,
              CraftingDeadSurvival.serverConfig.rareCivilianLootRefreshDelayTicks::get));

  public static final RegistryObject<Block> POLICE_LOOT_GENERATOR =
      BLOCKS.register("police_loot_gen",
          () -> lootGenerator(POLICE_LOOT, SurvivalParticleTypes.POLICE_LOOT_GEN,
              CraftingDeadSurvival.serverConfig.policeLootRefreshDelayTicks::get));

  private static LootGeneratorBlock lootGenerator(Supplier<Block> lootBlock,
      Supplier<? extends ParticleOptions> particleOptions, IntSupplier refreshDelayTicks) {
    return new LootGeneratorBlock(
        BlockBehaviour.Properties.of(Material.STRUCTURAL_AIR)
            .strength(5.0F, 5.0F)
            .randomTicks()
            .noOcclusion()
            .noCollission(),
        lootBlock, particleOptions, refreshDelayTicks);
  }
}
