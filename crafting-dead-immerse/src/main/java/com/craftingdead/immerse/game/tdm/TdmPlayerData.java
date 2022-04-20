/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
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
