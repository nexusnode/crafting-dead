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
