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

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import com.craftingdead.core.ammoprovider.RefillableAmmoProvider;
import com.craftingdead.core.capability.living.IPlayer;
import com.craftingdead.core.event.CombatPickupEvent;
import com.craftingdead.core.event.GunEvent;
import com.craftingdead.core.event.LivingEvent;
import com.craftingdead.core.event.OpenEquipmentMenuEvent;
import com.craftingdead.core.item.ModItems;
import com.craftingdead.core.network.util.NetworkDataManager;
import com.craftingdead.immerse.game.GameType;
import com.craftingdead.immerse.game.GameTypes;
import com.craftingdead.immerse.game.deathmatch.message.BuyItemMessage;
import com.craftingdead.immerse.game.deathmatch.message.RequestJoinTeamMessage;
import com.craftingdead.immerse.game.deathmatch.state.DeathmatchState;
import com.craftingdead.immerse.game.network.INetworkProtocol;
import com.craftingdead.immerse.game.network.SimpleNetworkProtocol;
import com.craftingdead.immerse.game.team.AbstractTeamGame;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public abstract class DeathmatchGame extends AbstractTeamGame<DeathmatchTeam> {

  private static final DataParameter<Boolean> MOVEMENT_BLOCKED =
      new DataParameter<>(0x00, DataSerializers.BOOLEAN);
  private static final DataParameter<Integer> TIMER_VALUE_SECONDS =
      new DataParameter<>(0x01, DataSerializers.INT);
  private static final DataParameter<Integer> GAME_STATE_ORDINAL =
      new DataParameter<>(0x02, DataSerializers.INT);
  private static final DataParameter<String> DISPLAY_NAME =
      new DataParameter<>(0x03, DataSerializers.STRING);

  private static final SimpleNetworkProtocol PROTOCOL = new SimpleNetworkProtocol();

  static {
    PROTOCOL
        .messageBuilder(0x00, RequestJoinTeamMessage.class)
        .encoder(RequestJoinTeamMessage::encode)
        .decoder(RequestJoinTeamMessage::decode)
        .handler(RequestJoinTeamMessage::handle)
        .register()
        .messageBuilder(0x01, BuyItemMessage.class)
        .encoder(BuyItemMessage::encode)
        .decoder(BuyItemMessage::decode)
        .handler(BuyItemMessage::handle)
        .register();
  }

  private final NetworkDataManager dataManager = new NetworkDataManager();

  private final Map<UUID, DeathmatchPlayerData> playerData = new Object2ObjectOpenHashMap<>();
  private final Map<UUID, DeathmatchPlayerData> dirtyPlayerData = new Object2ObjectOpenHashMap<>();

  public DeathmatchGame(String displayName) {
    super(DeathmatchTeam.class);
    this.registerTeam(DeathmatchTeam.RED);
    this.registerTeam(DeathmatchTeam.BLUE);
    this.dataManager.register(MOVEMENT_BLOCKED, false);
    this.dataManager.register(TIMER_VALUE_SECONDS, 0);
    this.dataManager.register(GAME_STATE_ORDINAL, 0);
    this.dataManager.register(DISPLAY_NAME, displayName);
  }

  @Override
  public void load() {
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public void unload() {
    MinecraftForge.EVENT_BUS.unregister(this);
  }

  @Override
  public INetworkProtocol getNetworkProtocol() {
    return PROTOCOL;
  }

  @Override
  public Optional<DeathmatchTeam> getPlayerTeam(IPlayer<?> player) {
    return ((DeathmatchPlayerExtension) player
        .getExpectedExtension(DeathmatchPlayerExtension.EXTENSION_ID)).getTeam();
  }

  @Override
  public void encode(PacketBuffer packetBuffer, boolean writeAll) {
    super.encode(packetBuffer, writeAll);
    NetworkDataManager.writeEntries(
        writeAll ? this.dataManager.getAll() : this.dataManager.getDirty(), packetBuffer);
    Set<Map.Entry<UUID, DeathmatchPlayerData>> playerDataCollection =
        writeAll ? this.playerData.entrySet() : this.dirtyPlayerData.entrySet();
    packetBuffer.writeVarInt(playerDataCollection.size());
    for (Map.Entry<UUID, DeathmatchPlayerData> entry : playerDataCollection) {
      packetBuffer.writeUUID(entry.getKey());
      DeathmatchPlayerData playerData = entry.getValue();
      if (playerData == null) {
        packetBuffer.writeBoolean(true);
      } else {
        packetBuffer.writeBoolean(false);
        playerData.encode(packetBuffer, writeAll);
      }
    }
    this.dirtyPlayerData.clear();
  }

  @Override
  public void decode(PacketBuffer packetBuffer) {
    super.decode(packetBuffer);
    this.dataManager.setEntryValues(NetworkDataManager.readEntries(packetBuffer));
    int playerDataSize = packetBuffer.readVarInt();
    for (int i = 0; i < playerDataSize; i++) {
      UUID playerId = packetBuffer.readUUID();
      if (packetBuffer.readBoolean()) {
        this.playerData.remove(playerId);
      } else {
        this.getPlayerData(playerId).decode(packetBuffer);
      }
    }
  }

  @Override
  public boolean requiresSync() {
    return super.requiresSync() || this.dataManager.isDirty()
        || !this.dirtyPlayerData.isEmpty();
  }

  @SubscribeEvent
  public void handleLivingLoad(LivingEvent.Load event) {
    if (event.getLiving() instanceof IPlayer
        && !event.getLiving().getExtension(DeathmatchPlayerExtension.EXTENSION_ID).isPresent()) {
      IPlayer<?> player = (IPlayer<?>) event.getLiving();
      player.registerExtension(DeathmatchPlayerExtension.EXTENSION_ID,
          this.createPlayerExtension(player));
    }
  }

  protected DeathmatchPlayerExtension createPlayerExtension(IPlayer<?> player) {
    return new DeathmatchPlayerExtension(this, player);
  }

  @SubscribeEvent
  public void handleCombatPickup(CombatPickupEvent event) {
    // Only pickup combat items
    if (event.getCombatSlotType() == null) {
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
      event.addAttachment(ModItems.LP_SCOPE.get());
    }
  }

  @SubscribeEvent
  public void handleOpenEquipmentMenu(OpenEquipmentMenuEvent event) {
    event.setCanceled(true);
  }

  public Optional<DeathmatchTeam> getWinningTeam() {
    int redScore = DeathmatchTeam.getScore(this.getTeamInstance(DeathmatchTeam.RED));
    int blueScore = DeathmatchTeam.getScore(this.getTeamInstance(DeathmatchTeam.BLUE));
    if (redScore == blueScore) {
      return Optional.empty();
    }
    return Optional.of(redScore > blueScore ? DeathmatchTeam.RED : DeathmatchTeam.BLUE);
  }

  public boolean isMovementBlocked() {
    return this.dataManager.get(MOVEMENT_BLOCKED);
  }

  public void setMovementBlocked(boolean movementBlocked) {
    this.dataManager.set(MOVEMENT_BLOCKED, movementBlocked);
  }

  public int getTimerValueSeconds() {
    return this.dataManager.get(TIMER_VALUE_SECONDS);
  }

  public void setTimerValueSeconds(int timerValueSeconds) {
    this.dataManager.set(TIMER_VALUE_SECONDS, timerValueSeconds);
  }

  public DeathmatchState getGameState() {
    return DeathmatchState.values()[this.dataManager.get(GAME_STATE_ORDINAL)];
  }

  public void setGameState(DeathmatchState gameState) {
    this.dataManager.set(GAME_STATE_ORDINAL, gameState.ordinal());
  }

  public String getDisplayName() {
    return this.dataManager.get(DISPLAY_NAME);
  }

  protected void setDisplayName(String displayName) {
    this.dataManager.set(DISPLAY_NAME, displayName);
  }

  public DeathmatchPlayerData getPlayerData(UUID playerId) {
    return this.playerData.computeIfAbsent(playerId,
        __ -> new DeathmatchPlayerData(
            playerData -> this.dirtyPlayerData.put(playerId, playerData)));
  }

  public void deletePlayerData(UUID playerId) {
    this.playerData.remove(playerId);
    this.dirtyPlayerData.put(playerId, null);
  }

  @Override
  public GameType getGameType() {
    return GameTypes.DEATHMATCH.get();
  }
}
