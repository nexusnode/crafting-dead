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

package com.craftingdead.immerse.game.deathmatch;

import java.util.Collection;
import java.util.Optional;
import javax.annotation.Nullable;
import com.craftingdead.core.network.util.NetworkDataManager;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.core.world.entity.extension.PlayerHandler;
import com.craftingdead.core.world.entity.extension.Visibility;
import com.craftingdead.immerse.game.GameTypes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class DeathmatchPlayerHandler implements PlayerHandler {

  public static final ResourceLocation ID = GameTypes.DEATHMATCH.getId();

  protected static final DataParameter<Integer> REMAINING_BUY_TIME_SECONDS =
      new DataParameter<>(0x00, DataSerializers.INT);

  protected static final DataParameter<Integer> TEAM_ORDINAL =
      new DataParameter<>(0x01, DataSerializers.INT);

  protected static final DataParameter<Integer> REMAINING_SPAWN_PROTECTION_SECONDS =
      new DataParameter<>(0x02, DataSerializers.INT);

  protected static final DataParameter<Integer> REMAINING_GHOST_TIME_SECONDS =
      new DataParameter<>(0x03, DataSerializers.INT);

  private final PlayerExtension<?> player;

  protected final NetworkDataManager dataManager = new NetworkDataManager();

  private final DeathmatchGame game;

  public DeathmatchPlayerHandler(DeathmatchGame game, PlayerExtension<?> player) {
    this(game, player, 0, 0, 0);
  }

  public DeathmatchPlayerHandler(DeathmatchGame game, PlayerExtension<?> player, int buyTimeSeconds,
      int spawnProtectionSeconds, int ghostTimeSeconds) {
    this.game = game;
    this.player = player;
    this.dataManager.register(REMAINING_BUY_TIME_SECONDS, buyTimeSeconds);
    this.dataManager.register(TEAM_ORDINAL, -1);
    this.dataManager.register(REMAINING_SPAWN_PROTECTION_SECONDS, spawnProtectionSeconds);
    this.dataManager.register(REMAINING_GHOST_TIME_SECONDS, ghostTimeSeconds);
  }

  public PlayerExtension<?> getPlayer() {
    return this.player;
  }

  public Optional<DeathmatchTeam> getTeam() {
    int teamOrdinal = this.dataManager.get(TEAM_ORDINAL);
    return teamOrdinal == -1 ? Optional.empty() : Optional.of(DeathmatchTeam.values()[teamOrdinal]);
  }

  void setTeam(@Nullable DeathmatchTeam team) {
    this.dataManager.set(TEAM_ORDINAL, team == null ? -1 : team.ordinal());
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

  public DeathmatchPlayerData getPlayerData() {
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
  public boolean onDeathDrops(DamageSource cause, Collection<ItemEntity> drops) {
    return true;
  }

  @Override
  public void encode(PacketBuffer out, boolean writeAll) {
    NetworkDataManager
        .writeEntries(writeAll ? this.dataManager.getAll() : this.dataManager.getDirty(), out);
  }

  @Override
  public void decode(PacketBuffer in) {
    this.dataManager.setEntryValues(NetworkDataManager.readEntries(in));
  }

  @Override
  public boolean requiresSync() {
    return this.dataManager.isDirty();
  }
}
