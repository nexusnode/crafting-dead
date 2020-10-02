/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
package com.craftingdead.immerse.client.gui.component;

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
import net.minecraft.tags.NetworkTagManager;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.ITickList;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.MapData;

public class FakeWorld extends ClientWorld {

  private static FakeWorld instance;

  private FakeWorld(Minecraft mc) {
    super(mc.getConnection(),
        new WorldSettings(0L, GameType.ADVENTURE, false, false, WorldType.DEFAULT),
        DimensionType.OVERWORLD, 3, mc.getProfiler(), mc.worldRenderer);
  }

  @Override
  public void notifyBlockUpdate(BlockPos blockPos, BlockState oldState,
      BlockState newState, int flags) {}

  @Override
  public void playSound(@Nullable PlayerEntity playerEntity, double x,
      double y, double z, SoundEvent soundEvent,
      SoundCategory soundCategory, float volume, float pitch) {}

  @Override
  public void playMovingSound(@Nullable PlayerEntity playerEntity, Entity entity,
      SoundEvent soundEvent, SoundCategory soundCategory, float volume,
      float pitch) {}

  @Override
  public Entity getEntityByID(int entityId) {
    return null;
  }

  @Override
  public MapData getMapData(String mapName) {
    return null;
  }

  @Override
  public void registerMapData(MapData mapData) {}

  @Override
  public int getNextMapId() {
    return 0;
  }

  @Override
  public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {}

  @Override
  public Scoreboard getScoreboard() {
    return null;
  }

  @Override
  public RecipeManager getRecipeManager() {
    return null;
  }

  @Override
  public NetworkTagManager getTags() {
    return null;
  }

  @Override
  public ITickList<Block> getPendingBlockTicks() {
    return null;
  }

  @Override
  public ITickList<Fluid> getPendingFluidTicks() {
    return null;
  }

  @Override
  public void playEvent(@Nullable PlayerEntity playerEntity, int type,
      BlockPos pos, int data) {}

  @Override
  public List<AbstractClientPlayerEntity> getPlayers() {
    return null;
  }

  @Override
  public Biome getNoiseBiomeRaw(int x, int y, int z) {
    return null;
  }

  public static FakeWorld getInstance() {
    return instance == null ? instance = new FakeWorld(Minecraft.getInstance()) : instance;
  }
}
