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

package com.craftingdead.immerse.server;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.game.GameServer;
import com.craftingdead.immerse.game.ServerGameWrapper;
import com.craftingdead.immerse.game.survival.SurvivalServer;
import com.craftingdead.immerse.network.NetworkChannel;
import com.craftingdead.immerse.network.login.SetupGameMessage;
import com.craftingdead.immerse.network.play.ChangeGameMessage;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.FolderName;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkDirection;

public class LogicalServer extends WorldSavedData {

  private static final Logger logger = LogManager.getLogger();

  private static final Gson gson = new Gson();

  private final MinecraftServer minecraftServer;

  private static final FolderName GAME_FOLDER_NAME = new FolderName("games");

  private final Path gamePath;

  private ServerGameWrapper gameWrapper;
  private String currentGameName;

  public LogicalServer(MinecraftServer minecraftServer) {
    super(CraftingDeadImmerse.ID);
    this.minecraftServer = minecraftServer;
    this.gamePath = minecraftServer.getWorldPath(GAME_FOLDER_NAME);
  }

  public MinecraftServer getMinecraftServer() {
    return this.minecraftServer;
  }

  public ServerGameWrapper getGameWrapper() {
    return this.gameWrapper;
  }

  public GameServer getGame() {
    return this.gameWrapper.getGame();
  }

  public List<Pair<String, SetupGameMessage>> generateSetupGameMessage(boolean isLocal) {
    return Collections.singletonList(Pair.of(SetupGameMessage.class.getName(),
        new SetupGameMessage(this.getGame().getType())));
  }

  public void startLoading() {}

  public void finishLoading() {
    this.minecraftServer.getLevel(World.OVERWORLD).getDataStorage()
        .computeIfAbsent(() -> this, CraftingDeadImmerse.ID);

    // If there was no saved game in the world, load a new game
    if (this.gameWrapper == null) {
      this.loadNextGame(true);
    }
  }

  private void loadNextGame(boolean loadFirst) {
    ForgeConfigSpec.ConfigValue<List<? extends String>> gameRotationConfig =
        CraftingDeadImmerse.serverConfig.gameRotation;

    List<? extends String> gameRotation = gameRotationConfig.get();

    if (gameRotation.isEmpty()) {
      logger.info("Game rotation empty, defaulting to survival...");
      this.loadGame(new ServerGameWrapper(new SurvivalServer(), this));
      return;
    }

    final String nextGameName;
    if (loadFirst || this.gameWrapper == null || this.currentGameName == null) {
      nextGameName = gameRotation.get(0);
    } else {
      nextGameName =
          gameRotation.get((gameRotation.indexOf(this.currentGameName) + 1) % gameRotation.size());
    }

    if (!this.findAndLoadGame(nextGameName)) {
      logger.info("Removing game '{}' from game rotation", nextGameName);
      gameRotation.remove(nextGameName);
      gameRotationConfig.save();
      this.loadNextGame(false);
    }
  }

  private boolean findAndLoadGame(String gameName) {
    File gameFile = new File(this.gamePath.toFile(), gameName + ".json");
    if (!gameFile.exists()) {
      logger.error("Game file with name '{}' does not exist");
      return false;
    }

    GameServer gameServer;
    try (FileReader fileReader = new FileReader(gameFile)) {
      gameServer =
          GameServer.CODEC.parse(JsonOps.INSTANCE, gson.fromJson(fileReader, JsonElement.class))
              .getOrThrow(false, logger::error);
    } catch (Throwable t) {
      logger.error("Failed to load game file '{}'", gameFile.toString(), t);
      return false;
    }

    this.loadGame(new ServerGameWrapper(gameServer, this));
    this.currentGameName = gameName;
    return true;
  }

  private void loadGame(ServerGameWrapper gameWrapper) {
    List<ServerPlayerEntity> players = this.minecraftServer.getPlayerList().getPlayers();

    ServerGameWrapper oldGameWrapper = this.gameWrapper;
    if (oldGameWrapper != null) {
      logger.info("Unloading current game");
      players.stream()
          .map(PlayerExtension::getExpected)
          .forEach(oldGameWrapper::removePlayer);
      oldGameWrapper.unload();
    }

    logger.info("Loading game type '{}'",
        gameWrapper.getGame().getType().getRegistryName().toString());

    this.gameWrapper = gameWrapper;
    gameWrapper.load();

    logger.info("Loading players");
    for (ServerPlayerEntity playerEntity : players) {
      playerEntity.connection.send(NetworkChannel.PLAY.getSimpleChannel().toVanillaPacket(
          new ChangeGameMessage(gameWrapper.getGame().getType()),
          NetworkDirection.PLAY_TO_CLIENT));

      if (oldGameWrapper != null && gameWrapper.getGame().persistPlayerData()
          && !oldGameWrapper.getGame().persistPlayerData()) {
        this.minecraftServer.getPlayerList().load(playerEntity);
      }
    }

    players.stream()
        .map(PlayerExtension::getExpected)
        .forEach(gameWrapper::addPlayer);

    // Respawn all players - keep data if new game uses persisted player data, otherwise discard
    // player data.
    logger.info("Respawning players");
    this.respawnPlayers(gameWrapper.getGame().persistPlayerData());
  }

  public void reloadGameRotation() {
    this.loadNextGame(true);
  }

  public void restartGame() {
    this.loadGame(this.gameWrapper);
  }

  public void respawnPlayers(boolean keepData) {
    this.respawnPlayers(player -> true, keepData);
  }

  public void respawnPlayers(Predicate<ServerPlayerEntity> predicate, boolean keepData) {
    List<ServerPlayerEntity> players =
        new ArrayList<>(this.minecraftServer.getPlayerList().getPlayers());
    for (ServerPlayerEntity playerEntity : players) {
      if (predicate.test(playerEntity)) {
        this.respawnPlayer(playerEntity, keepData);
      }
    }
  }

  public void respawnPlayer(ServerPlayerEntity playerEntity, boolean keepData) {
    playerEntity.connection.player =
        this.minecraftServer.getPlayerList().respawn(playerEntity, keepData);
  }

  @Override
  public void load(CompoundNBT nbt) {
    CompoundNBT gameNbt = nbt.getCompound("game");
    if (!gameNbt.isEmpty()) {
      GameServer gameServer = GameServer.CODEC.parse(NBTDynamicOps.INSTANCE, gameNbt)
          .getOrThrow(false, logger::error);
      this.loadGame(new ServerGameWrapper(gameServer, this));
    }
  }

  @Override
  public CompoundNBT save(CompoundNBT nbt) {
    if (this.getGame().save()) {
      nbt.put("game", GameServer.CODEC.encodeStart(NBTDynamicOps.INSTANCE, this.getGame())
          .getOrThrow(false, logger::error));
    }
    return nbt;
  }

  // ================================================================================
  // Forge Events
  // ================================================================================

  @SubscribeEvent
  public void handleServerTick(TickEvent.ServerTickEvent event) {
    switch (event.phase) {
      case START:
        this.gameWrapper.tick();
        if (this.getGame().isFinished()) {
          this.loadNextGame(false);
        }
        break;
      default:
        break;
    }
  }

  @SubscribeEvent
  public void handlePlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
    ServerPlayerEntity playerEntity = (ServerPlayerEntity) event.getPlayer();
    this.gameWrapper.addPlayer(PlayerExtension.getExpected(playerEntity));
  }

  @SubscribeEvent
  public void handlePlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
    this.gameWrapper
        .removePlayer(PlayerExtension.getExpected((ServerPlayerEntity) event.getPlayer()));
  }
}
