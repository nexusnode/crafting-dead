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

package com.craftingdead.immerse.game.tdm;

import java.util.function.Consumer;
import com.craftingdead.core.network.Synched;
import com.craftingdead.core.network.SynchedData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;

public class TdmPlayerData implements Synched {

  private static final EntityDataAccessor<Integer> SCORE =
      new EntityDataAccessor<>(0x00, EntityDataSerializers.INT);
  private static final EntityDataAccessor<Integer> KILLS =
      new EntityDataAccessor<>(0x01, EntityDataSerializers.INT);
  private static final EntityDataAccessor<Integer> ASSISTS =
      new EntityDataAccessor<>(0x02, EntityDataSerializers.INT);
  private static final EntityDataAccessor<Integer> DEATHS =
      new EntityDataAccessor<>(0x03, EntityDataSerializers.INT);
  private static final EntityDataAccessor<Boolean> DEAD =
      new EntityDataAccessor<>(0x04, EntityDataSerializers.BOOLEAN);

  private final SynchedData dataManager;

  public TdmPlayerData(Consumer<TdmPlayerData> dirtyListener) {
    this.dataManager = new SynchedData(() -> dirtyListener.accept(this));
    this.dataManager.register(SCORE, 0);
    this.dataManager.register(KILLS, 0);
    this.dataManager.register(ASSISTS, 0);
    this.dataManager.register(DEATHS, 0);
    this.dataManager.register(DEAD, false);
  }

  public boolean isDead() {
    return this.dataManager.get(DEAD);
  }

  public void setDead(boolean dead) {
    this.dataManager.set(DEAD, dead);
  }

  public int getScore() {
    return this.dataManager.get(SCORE);
  }

  public void incrementScore() {
    this.dataManager.compute(SCORE, score -> ++score);
  }

  public int getKills() {
    return this.dataManager.get(KILLS);
  }

  public void incrementKills() {
    this.dataManager.compute(KILLS, kills -> ++kills);
  }

  public int getAssists() {
    return this.dataManager.get(ASSISTS);
  }

  public void incrementAssists() {
    this.dataManager.compute(ASSISTS, assists -> ++assists);

  }

  public int getDeaths() {
    return this.dataManager.get(DEATHS);
  }

  public void incrementDeaths() {
    this.dataManager.compute(DEATHS, deaths -> ++deaths);
  }

  @Override
  public void encode(FriendlyByteBuf out, boolean writeAll) {
    SynchedData.pack(writeAll
        ? this.dataManager.getAll()
        : this.dataManager.packDirty(), out);
  }

  @Override
  public void decode(FriendlyByteBuf in) {
    this.dataManager.assignValues(SynchedData.unpack(in));
  }

  @Override
  public boolean requiresSync() {
    return this.dataManager.isDirty();
  }
}
