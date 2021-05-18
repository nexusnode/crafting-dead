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

package com.craftingdead.immerse.client.fake;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.tags.ITagCollectionSupplier;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DimensionType;
import net.minecraft.world.ITickList;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.MapData;

public class FakeLevel extends ClientWorld {

  private static FakeLevel instance;

  private FakeLevel(Minecraft mc) {
    super(mc.getConnection(), new ClientWorld.ClientWorldInfo(Difficulty.NORMAL, false, false),
        World.OVERWORLD, DimensionType.DEFAULT_OVERWORLD, 3, mc::getProfiler, mc.levelRenderer,
        false, 0L);
  }

  @Override
  public void sendBlockUpdated(BlockPos blockPos, BlockState oldState,
      BlockState newState, int flags) {}

  @Override
  public void playSound(@Nullable PlayerEntity playerEntity, double x,
      double y, double z, SoundEvent soundEvent,
      SoundCategory soundCategory, float volume, float pitch) {}

  @Override
  public void playSound(@Nullable PlayerEntity playerEntity, Entity entity,
      SoundEvent soundEvent, SoundCategory soundCategory, float volume,
      float pitch) {}

  @Override
  public Entity getEntity(int entityId) {
    return null;
  }

  @Override
  public MapData getMapData(String mapName) {
    return null;
  }

  @Override
  public void setMapData(MapData mapData) {}

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
  public ITagCollectionSupplier getTagManager() {
    return null;
  }

  @Override
  public ITickList<Block> getBlockTicks() {
    return null;
  }

  @Override
  public ITickList<Fluid> getLiquidTicks() {
    return null;
  }

  @Override
  public void levelEvent(@Nullable PlayerEntity playerEntity, int type,
      BlockPos pos, int data) {}

  @Override
  public List<AbstractClientPlayerEntity> players() {
    return null;
  }

  @Override
  public Biome getUncachedNoiseBiome(int x, int y, int z) {
    return null;
  }

  @Override
  public DynamicRegistries registryAccess() {
    return DynamicRegistries.builtin();
  }

  public static FakeLevel getInstance() {
    return instance == null ? instance = new FakeLevel(Minecraft.getInstance()) : instance;
  }
}
