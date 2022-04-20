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

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import com.craftingdead.core.event.CombatPickupEvent;
import com.craftingdead.core.event.GunEvent;
import com.craftingdead.core.event.OpenEquipmentMenuEvent;
import com.craftingdead.core.network.SynchedData;
import com.craftingdead.core.world.item.ModItems;
import com.craftingdead.core.world.item.gun.ammoprovider.RefillableAmmoProvider;
import com.craftingdead.core.world.item.gun.attachment.Attachments;
import com.craftingdead.immerse.game.Game;
import com.craftingdead.immerse.game.GameType;
import com.craftingdead.immerse.game.GameTypes;
import com.craftingdead.immerse.game.module.team.TeamModule;
import com.craftingdead.immerse.game.tdm.state.TdmState;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public abstract class TdmGame implements Game {

  private static final EntityDataAccessor<Boolean> MOVEMENT_BLOCKED =
      new EntityDataAccessor<>(0x00, EntityDataSerializers.BOOLEAN);
  private static final EntityDataAccessor<Integer> TIMER_VALUE_SECONDS =
      new EntityDataAccessor<>(0x01, EntityDataSerializers.INT);
  private static final EntityDataAccessor<Integer> GAME_STATE_ORDINAL =
      new EntityDataAccessor<>(0x02, EntityDataSerializers.INT);
  private static final EntityDataAccessor<String> DISPLAY_NAME =
      new EntityDataAccessor<>(0x03, EntityDataSerializers.STRING);

  private final SynchedData data = new SynchedData();

  private final Map<UUID, TdmPlayerData> playerData = new Object2ObjectOpenHashMap<>();
  private final Map<UUID, TdmPlayerData> dirtyPlayerData = new Object2ObjectOpenHashMap<>();

  public TdmGame(String displayName) {
    this.data.register(MOVEMENT_BLOCKED, false);
    this.data.register(TIMER_VALUE_SECONDS, 0);
    this.data.register(GAME_STATE_ORDINAL, 0);
    this.data.register(DISPLAY_NAME, displayName);
  }

  public abstract TeamModule<TdmTeam> getTeamModule();

  @Override
  public void load() {
    this.getTeamModule().registerTeam(TdmTeam.RED);
    this.getTeamModule().registerTeam(TdmTeam.BLUE);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public void unload() {
    MinecraftForge.EVENT_BUS.unregister(this);
  }

  @Override
  public boolean disableBlockBurning() {
    return true;
  }

  @Override
  public void encode(FriendlyByteBuf out, boolean writeAll) {
    SynchedData.pack(writeAll
        ? this.data.getAll()
        : this.data.packDirty(), out);
    Set<Map.Entry<UUID, TdmPlayerData>> playerDataCollection = writeAll
        ? this.playerData.entrySet()
        : this.dirtyPlayerData.entrySet();
    out.writeVarInt(playerDataCollection.size());
    for (Map.Entry<UUID, TdmPlayerData> entry : playerDataCollection) {
      out.writeUUID(entry.getKey());
      TdmPlayerData playerData = entry.getValue();
      if (playerData == null) {
        out.writeBoolean(true);
      } else {
        out.writeBoolean(false);
        playerData.encode(out, writeAll);
      }
    }

    if (!writeAll) {
      this.dirtyPlayerData.clear();
    }
  }

  @Override
  public void decode(FriendlyByteBuf in) {
    this.data.assignValues(SynchedData.unpack(in));
    int playerDataSize = in.readVarInt();
    for (int i = 0; i < playerDataSize; i++) {
      UUID playerId = in.readUUID();
      if (in.readBoolean()) {
        this.playerData.remove(playerId);
      } else {
        this.getPlayerData(playerId).decode(in);
      }
    }
  }

  @Override
  public boolean requiresSync() {
    return this.data.isDirty() || !this.dirtyPlayerData.isEmpty();
  }

  @SubscribeEvent
  public void handleCombatPickup(CombatPickupEvent event) {
    // Only pickup combat items
    if (event.getCombatSlot() == null) {
      event.setCanceled(true);
    }
  }

  @SubscribeEvent
  public void handleGunInitialize(GunEvent.Initialize event) {
    RefillableAmmoProvider ammoProvider =
        new RefillableAmmoProvider(event.getGun().getDefaultMagazineStack(), 3, true);
    event.setAmmoProvider(ammoProvider);

    Item item = event.getItemStack().getItem();
    if (item == ModItems.AWP.get() || item == ModItems.M107.get() || item == ModItems.AS50.get()) {
      event.addAttachment(Attachments.LP_SCOPE.get());
    }
  }

  @SubscribeEvent
  public void handleOpenEquipmentMenu(OpenEquipmentMenuEvent event) {
    event.setCanceled(true);
  }

  public Optional<TdmTeam> getWinningTeam() {
    int redScore = TdmTeam.getScore(this.getTeamModule().getTeamInstance(TdmTeam.RED));
    int blueScore = TdmTeam.getScore(this.getTeamModule().getTeamInstance(TdmTeam.BLUE));
    if (redScore == blueScore) {
      return Optional.empty();
    }
    return Optional.of(redScore > blueScore ? TdmTeam.RED : TdmTeam.BLUE);
  }

  public boolean isMovementBlocked() {
    return this.data.get(MOVEMENT_BLOCKED);
  }

  public void setMovementBlocked(boolean movementBlocked) {
    this.data.set(MOVEMENT_BLOCKED, movementBlocked);
  }

  public int getTimerValueSeconds() {
    return this.data.get(TIMER_VALUE_SECONDS);
  }

  public void setTimerValueSeconds(int timerValueSeconds) {
    this.data.set(TIMER_VALUE_SECONDS, timerValueSeconds);
  }

  public TdmState getGameState() {
    return TdmState.values()[this.data.get(GAME_STATE_ORDINAL)];
  }

  public void setGameState(TdmState gameState) {
    this.data.set(GAME_STATE_ORDINAL, gameState.ordinal());
  }

  public String getDisplayName() {
    return this.data.get(DISPLAY_NAME);
  }

  protected void setDisplayName(String displayName) {
    this.data.set(DISPLAY_NAME, displayName);
  }

  public TdmPlayerData getPlayerData(UUID playerId) {
    return this.playerData.computeIfAbsent(playerId,
        __ -> new TdmPlayerData(
            playerData -> this.dirtyPlayerData.put(playerId, playerData)));
  }

  public void deletePlayerData(UUID playerId) {
    this.playerData.remove(playerId);
    this.dirtyPlayerData.put(playerId, null);
  }

  @Override
  public GameType getType() {
    return GameTypes.TEAM_DEATHMATCH.get();
  }
}
