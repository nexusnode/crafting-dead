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
import com.craftingdead.core.network.BufferSerializable;
import com.craftingdead.core.network.SynchedData;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;

public class TdmPlayerData implements BufferSerializable {

  private static final DataParameter<Integer> SCORE =
      new DataParameter<>(0x00, DataSerializers.INT);
  private static final DataParameter<Integer> KILLS =
      new DataParameter<>(0x01, DataSerializers.INT);
  private static final DataParameter<Integer> ASSISTS =
      new DataParameter<>(0x02, DataSerializers.INT);
  private static final DataParameter<Integer> DEATHS =
      new DataParameter<>(0x03, DataSerializers.INT);
  private static final DataParameter<Boolean> DEAD =
      new DataParameter<>(0x04, DataSerializers.BOOLEAN);

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
    this.dataManager.getUpdate(SCORE, score -> ++score);
  }

  public int getKills() {
    return this.dataManager.get(KILLS);
  }

  public void incrementKills() {
    this.dataManager.getUpdate(KILLS, kills -> ++kills);
  }

  public int getAssists() {
    return this.dataManager.get(ASSISTS);
  }

  public void incrementAssists() {
    this.dataManager.getUpdate(ASSISTS, assists -> ++assists);

  }

  public int getDeaths() {
    return this.dataManager.get(DEATHS);
  }

  public void incrementDeaths() {
    this.dataManager.getUpdate(DEATHS, deaths -> ++deaths);
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
