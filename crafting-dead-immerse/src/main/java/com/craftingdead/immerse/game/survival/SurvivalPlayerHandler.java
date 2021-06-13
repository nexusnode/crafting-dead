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

package com.craftingdead.immerse.game.survival;

import com.craftingdead.core.network.SynchedData;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.core.world.entity.extension.PlayerHandler;
import com.craftingdead.immerse.game.GameTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.stats.Stats;
import net.minecraft.util.ResourceLocation;

public class SurvivalPlayerHandler implements PlayerHandler {

  public static final ResourceLocation EXTENSION_ID = GameTypes.SURVIVAL.getId();

  private static final DataParameter<Integer> DAYS_SURVIVED =
      new DataParameter<>(0x00, DataSerializers.INT);
  private static final DataParameter<Integer> ZOMBIES_KILLED =
      new DataParameter<>(0x01, DataSerializers.INT);
  private static final DataParameter<Integer> PLAYERS_KILLED =
      new DataParameter<>(0x02, DataSerializers.INT);

  private final PlayerExtension<?> player;

  private final SynchedData dataManager = new SynchedData();

  public SurvivalPlayerHandler(PlayerExtension<?> player) {
    this.player = player;
    this.dataManager.register(DAYS_SURVIVED, 0);
    this.dataManager.register(ZOMBIES_KILLED, 0);
    this.dataManager.register(PLAYERS_KILLED, 0);
  }

  @Override
  public void tick() {
    if (this.player.getEntity() instanceof ServerPlayerEntity) {
      int aliveTicks = ((ServerPlayerEntity) this.player.getEntity()).getStats()
          .getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_DEATH));
      this.setDaysSurvived(aliveTicks / 20 / 60 / 20);
    }
  }

  @Override
  public boolean onKill(Entity target) {
    if (target instanceof ZombieEntity) {
      this.setZombiesKilled(this.getZombiesKilled() + 1);
    } else if (target instanceof ServerPlayerEntity) {
      this.setPlayersKilled(this.getPlayersKilled() + 1);
    }
    return false;
  }

  public int getDaysSurvived() {
    return this.dataManager.get(DAYS_SURVIVED);
  }

  public void setDaysSurvived(int daysSurvived) {
    this.dataManager.set(DAYS_SURVIVED, daysSurvived);
  }

  public int getZombiesKilled() {
    return this.dataManager.get(ZOMBIES_KILLED);
  }

  public void setZombiesKilled(int zombiesKilled) {
    this.dataManager.set(ZOMBIES_KILLED, zombiesKilled);
  }

  public int getPlayersKilled() {
    return this.dataManager.get(PLAYERS_KILLED);
  }

  public void setPlayersKilled(int playersKilled) {
    this.dataManager.set(PLAYERS_KILLED, playersKilled);
  }

  @Override
  public boolean isCombatModeEnabled() {
    return false;
  }

  @Override
  public void copyFrom(PlayerExtension<?> that, boolean wasDeath) {
    if (!wasDeath) {
      that.getHandler(GameTypes.SURVIVAL.getId())
          .map(extension -> (SurvivalPlayerHandler) extension)
          .ifPresent(extension -> {
            this.setZombiesKilled(extension.getZombiesKilled());
            this.setPlayersKilled(extension.getPlayersKilled());
          });
    }
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
    nbt.putInt("zombiesKilled", this.getZombiesKilled());
    nbt.putInt("playersKilled", this.getPlayersKilled());
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    this.setZombiesKilled(nbt.getInt("zombiesKilled"));
    this.setPlayersKilled(nbt.getInt("playersKilled"));
  }

  @Override
  public void encode(PacketBuffer out, boolean writeAll) {
    SynchedData
        .writeEntries(writeAll ? this.dataManager.getAll() : this.dataManager.getDirty(), out);
  }

  @Override
  public void decode(PacketBuffer in) {
    this.dataManager.setEntryValues(SynchedData.readEntries(in));
  }

  @Override
  public boolean requiresSync() {
    return this.dataManager.isDirty();
  }
}
