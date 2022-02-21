/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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

package com.craftingdead.immerse.game.survival;

import com.craftingdead.core.event.LivingExtensionEvent;
import com.craftingdead.core.network.SynchedData;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.game.Game;
import com.craftingdead.immerse.game.GameType;
import com.craftingdead.immerse.game.GameTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public abstract class SurvivalGame implements Game {

  private static final EntityDataAccessor<Boolean> THIRST_ENABLED =
      new EntityDataAccessor<>(0x00, EntityDataSerializers.BOOLEAN);

  private final SynchedData data = new SynchedData();

  public SurvivalGame() {
    this.data.register(THIRST_ENABLED, true);
  }

  public boolean isThirstEnabled() {
    return this.data.get(THIRST_ENABLED);
  }

  public void setThirstEnabled(boolean thirstEnabled) {
    this.data.set(THIRST_ENABLED, thirstEnabled);
  }

  @Override
  public boolean disableBlockBurning() {
    return true;
  }

  @Override
  public void started() {
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public void ended() {
    MinecraftForge.EVENT_BUS.unregister(this);
  }

  @Override
  public void tick() {}

  @SubscribeEvent
  public void handleLivingExtensionLoad(LivingExtensionEvent.Load event) {
    if (event.getLiving() instanceof PlayerExtension<?> player
        && !player.getHandler(SurvivalPlayerHandler.TYPE).isPresent()) {
      player.registerHandler(SurvivalPlayerHandler.TYPE, new SurvivalPlayerHandler(player));
    }
  }

  @Override
  public GameType getType() {
    return GameTypes.SURVIVAL.get();
  }

  @Override
  public void encode(FriendlyByteBuf out, boolean writeAll) {
    SynchedData.pack(writeAll ? this.data.getAll() : this.data.packDirty(), out);
  }

  @Override
  public void decode(FriendlyByteBuf in) {
    this.data.assignValues(SynchedData.unpack(in));
  }

  @Override
  public boolean requiresSync() {
    return this.data.isDirty();
  }
}
