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
package com.craftingdead.core.game.survival;

import com.craftingdead.core.capability.living.Player;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.stats.Stats;

public class SurvivalPlayer<E extends PlayerEntity> extends Player<E> {

  private final DataParameter<Integer> daysSurvived;
  private final DataParameter<Integer> zombiesKilled;
  private final DataParameter<Integer> playersKilled;

  public SurvivalPlayer(E entity) {
    super(entity);
    this.daysSurvived = this.dataManager.register(DataSerializers.VARINT, 0);
    this.zombiesKilled = this.dataManager.register(DataSerializers.VARINT, 0);
    this.playersKilled = this.dataManager.register(DataSerializers.VARINT, 0);
  }

  @Override
  public void tick() {
    super.tick();
    if (this.getEntity() instanceof ServerPlayerEntity) {
      int aliveTicks = ((ServerPlayerEntity) this.getEntity()).getStats()
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

  @Override
  public void copyFrom(Player<?> that, boolean wasDeath) {
    if (that instanceof SurvivalPlayer && !wasDeath) {
      this.setZombiesKilled(((SurvivalPlayer<?>) that).getZombiesKilled());
      this.setPlayersKilled(((SurvivalPlayer<?>) that).getPlayersKilled());
    }
  }

  public int getDaysSurvived() {
    return this.dataManager.get(this.daysSurvived);
  }

  public void setDaysSurvived(int daysSurvived) {
    this.dataManager.set(this.daysSurvived, daysSurvived);
  }

  public int getZombiesKilled() {
    return this.dataManager.get(this.zombiesKilled);
  }

  public void setZombiesKilled(int zombiesKilled) {
    this.dataManager.set(this.zombiesKilled, zombiesKilled);
  }

  public int getPlayersKilled() {
    return this.dataManager.get(this.playersKilled);
  }

  public void setPlayersKilled(int playersKilled) {
    this.dataManager.set(this.playersKilled, playersKilled);
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = super.serializeNBT();
    nbt.putInt("zombiesKilled", this.getZombiesKilled());
    nbt.putInt("playersKilled", this.getPlayersKilled());
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    super.deserializeNBT(nbt);
    this.setZombiesKilled(nbt.getInt("zombiesKilled"));
    this.setPlayersKilled(nbt.getInt("playersKilled"));
  }
}
