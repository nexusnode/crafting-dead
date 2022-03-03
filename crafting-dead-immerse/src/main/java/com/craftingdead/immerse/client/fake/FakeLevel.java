/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
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

package com.craftingdead.immerse.client.fake;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.ticks.LevelTickAccess;

public class FakeLevel extends ClientLevel {

  private static FakeLevel instance;

  private FakeLevel(Minecraft minecraft) {
    super(minecraft.getConnection(),
        new ClientLevel.ClientLevelData(Difficulty.NORMAL, false, false),
        OVERWORLD, Holder.direct(DimensionType.DEFAULT_OVERWORLD), 0, 0, minecraft::getProfiler,
        minecraft.levelRenderer, false, 0L);
  }

  @Override
  public void sendBlockUpdated(BlockPos blockPos, BlockState oldState,
      BlockState newState, int flags) {}

  @Override
  public void playSound(@Nullable Player playerEntity, double x,
      double y, double z, SoundEvent soundEvent,
      SoundSource soundCategory, float volume, float pitch) {}

  @Override
  public void playSound(@Nullable Player playerEntity, Entity entity,
      SoundEvent soundEvent, SoundSource soundCategory, float volume,
      float pitch) {}

  @Override
  public Entity getEntity(int entityId) {
    return null;
  }

  @Override
  public MapItemSavedData getMapData(String mapName) {
    return null;
  }

  @Override
  public int getFreeMapId() {
    return 0;
  }

  @Override
  public void destroyBlockProgress(int breakerId, BlockPos pos, int progress) {}

  @Override
  public Scoreboard getScoreboard() {
    return null;
  }

  @Override
  public RecipeManager getRecipeManager() {
    return null;
  }

  @Override
  public LevelTickAccess<Block> getBlockTicks() {
    return null;
  }

  @Override
  public LevelTickAccess<Fluid> getFluidTicks() {
    return null;
  }

  @Override
  public void levelEvent(@Nullable Player playerEntity, int type,
      BlockPos pos, int data) {}

  @Override
  public List<AbstractClientPlayer> players() {
    return null;
  }

  @Override
  public Holder<Biome> getUncachedNoiseBiome(int x, int y, int z) {
    return null;
  }

  @Override
  public RegistryAccess registryAccess() {
    return RegistryAccess.BUILTIN.get();
  }

  @Override
  public int getEntityCount() {
    return 1;
  }

  public static FakeLevel getInstance() {
    return instance == null ? instance = new FakeLevel(Minecraft.getInstance()) : instance;
  }
}
