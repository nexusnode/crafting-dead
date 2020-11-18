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
package com.craftingdead.immerse.game.survival;

import com.craftingdead.core.capability.living.IPlayer;
import com.craftingdead.core.capability.living.IPlayerHandler;
import com.craftingdead.core.capability.living.Player;
import com.craftingdead.immerse.game.GameTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.stats.Stats;
import net.minecraft.util.ResourceLocation;

public class SurvivalPlayer implements IPlayerHandler {

  public static final ResourceLocation ID = GameTypes.SURVIVAL.getId();

  private final Player<?> player;

  private final DataParameter<Integer> daysSurvived;
  private final DataParameter<Integer> zombiesKilled;
  private final DataParameter<Integer> playersKilled;

  public SurvivalPlayer(Player<?> player) {
    this.player = player;
    this.daysSurvived = this.player.getDataManager().register(DataSerializers.VARINT, 0);
    this.zombiesKilled = this.player.getDataManager().register(DataSerializers.VARINT, 0);
    this.playersKilled = this.player.getDataManager().register(DataSerializers.VARINT, 0);
  }

  @Override
  public void playerTick() {}

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
    return this.player.getDataManager().get(this.daysSurvived);
  }

  public void setDaysSurvived(int daysSurvived) {
    this.player.getDataManager().set(this.daysSurvived, daysSurvived);
  }

  public int getZombiesKilled() {
    return this.player.getDataManager().get(this.zombiesKilled);
  }

  public void setZombiesKilled(int zombiesKilled) {
    this.player.getDataManager().set(this.zombiesKilled, zombiesKilled);
  }

  public int getPlayersKilled() {
    return this.player.getDataManager().get(this.playersKilled);
  }

  public void setPlayersKilled(int playersKilled) {
    this.player.getDataManager().set(this.playersKilled, playersKilled);
  }

  @Override
  public void copyFrom(IPlayer<?> that, boolean wasDeath) {
    if (!wasDeath) {
      that.getExtension(GameTypes.SURVIVAL.getId())
          .map(t -> (SurvivalPlayer) t).ifPresent(t -> {
            this.setZombiesKilled(t.getZombiesKilled());
            this.setPlayersKilled(t.getPlayersKilled());
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

  public static SurvivalPlayer getExpected(IPlayer<?> player) {
    return (SurvivalPlayer) player.getExtension(ID)
        .orElseThrow(() -> new IllegalStateException("Missing survival player extension"));
  }
}
