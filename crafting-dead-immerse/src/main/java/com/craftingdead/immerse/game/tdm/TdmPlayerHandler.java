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

import java.util.Collection;
import java.util.Optional;
import com.craftingdead.core.network.SynchedData;
import com.craftingdead.core.world.entity.extension.LivingHandlerType;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.core.world.entity.extension.PlayerHandler;
import com.craftingdead.core.world.entity.extension.Visibility;
import com.craftingdead.immerse.CraftingDeadImmerse;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;

public class TdmPlayerHandler<P extends Player> implements PlayerHandler {

  public static final LivingHandlerType<TdmPlayerHandler<?>> TYPE =
      new LivingHandlerType<>(new ResourceLocation(CraftingDeadImmerse.ID, "tdm_player"));

  protected static final EntityDataAccessor<Integer> REMAINING_BUY_TIME_SECONDS =
      new EntityDataAccessor<>(0x00, EntityDataSerializers.INT);

  protected static final EntityDataAccessor<Integer> REMAINING_SPAWN_PROTECTION_SECONDS =
      new EntityDataAccessor<>(0x02, EntityDataSerializers.INT);

  protected static final EntityDataAccessor<Integer> REMAINING_GHOST_TIME_SECONDS =
      new EntityDataAccessor<>(0x03, EntityDataSerializers.INT);

  private final PlayerExtension<P> player;

  protected final SynchedData dataManager = new SynchedData();

  private final TdmGame game;

  public TdmPlayerHandler(TdmGame game, PlayerExtension<P> player) {
    this(game, player, 0, 0, 0);
  }

  public TdmPlayerHandler(TdmGame game, PlayerExtension<P> player, int buyTimeSeconds,
      int spawnProtectionSeconds, int ghostTimeSeconds) {
    this.game = game;
    this.player = player;
    this.dataManager.register(REMAINING_BUY_TIME_SECONDS, buyTimeSeconds);
    this.dataManager.register(REMAINING_SPAWN_PROTECTION_SECONDS, spawnProtectionSeconds);
    this.dataManager.register(REMAINING_GHOST_TIME_SECONDS, ghostTimeSeconds);
  }

  public PlayerExtension<P> getPlayer() {
    return this.player;
  }

  public Optional<TdmTeam> getTeam() {
    return this.game.getTeamModule().getPlayerTeam(this.player.getEntity().getUUID());
  }

  public int getRemainingBuyTimeSeconds() {
    return this.dataManager.get(REMAINING_BUY_TIME_SECONDS);
  }

  public void setRemainingBuyTimeSeconds(int remainingBuyTimeSeconds) {
    this.dataManager.set(REMAINING_BUY_TIME_SECONDS, remainingBuyTimeSeconds);
  }

  public int getRemainingSpawnProtectionSeconds() {
    return this.dataManager.get(REMAINING_SPAWN_PROTECTION_SECONDS);
  }

  public void setRemainingSpawnProtectionSeconds(int spawnProtectionSeconds) {
    this.dataManager.set(REMAINING_SPAWN_PROTECTION_SECONDS, spawnProtectionSeconds);
  }

  @Override
  public Visibility getVisibility() {
    return this.getRemainingSpawnProtectionSeconds() > 0 ? Visibility.PARTIALLY_VISIBLE
        : Visibility.VISIBLE;
  }

  public TdmPlayerData getPlayerData() {
    return this.game.getPlayerData(this.player.getEntity().getUUID());
  }

  @Override
  public boolean isMovementBlocked() {
    return this.game.isMovementBlocked();
  }

  @Override
  public boolean isCombatModeEnabled() {
    return true;
  }

  @Override
  public boolean handleDeathLoot(DamageSource cause, Collection<ItemEntity> drops) {
    return true;
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
