/*
 * Crafting Dead Copyright (C) 2021 NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
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
