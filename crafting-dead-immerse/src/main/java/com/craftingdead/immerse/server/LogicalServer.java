/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
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

package com.craftingdead.immerse.server;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import org.apache.commons.lang3.tuple.Pair;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.game.GameServer;
import com.craftingdead.immerse.game.ServerGameWrapper;
import com.craftingdead.immerse.game.survival.SurvivalServer;
import com.craftingdead.immerse.network.NetworkChannel;
import com.craftingdead.immerse.network.login.SetupGameMessage;
import com.craftingdead.immerse.network.play.ChangeGameMessage;
import com.google.common.base.Predicates;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkDirection;

public class LogicalServer extends SavedData {

  private static final Logger logger = LogUtils.getLogger();

  private static final Gson gson = new Gson();

  private final MinecraftServer minecraftServer;

  private static final LevelResource GAME_FOLDER_NAME = new LevelResource("games");

  private final Path gamePath;

  private ServerGameWrapper gameWrapper;
  private String currentGameName;

  public LogicalServer(MinecraftServer minecraftServer) {
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
    this.minecraftServer.getLevel(Level.OVERWORLD).getDataStorage()
        .computeIfAbsent(this::load, () -> this, CraftingDeadImmerse.ID);

    // If there was no saved game in the world, load a new game
    if (this.gameWrapper == null) {
      this.loadNextGame(true);
    }
  }

  private void loadNextGame(boolean loadFirst) {
    var gameRotationConfig = CraftingDeadImmerse.serverConfig.gameRotation;

    var gameRotation = gameRotationConfig.get();

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
    var players = this.minecraftServer.getPlayerList().getPlayers();

    var oldGameWrapper = this.gameWrapper;
    if (oldGameWrapper != null) {
      logger.info("Unloading current game");
      players.stream()
          .map(PlayerExtension::getOrThrow)
          .forEach(oldGameWrapper::removePlayer);
      oldGameWrapper.unload();
    }

    logger.info("Loading game type '{}'",
        gameWrapper.getGame().getType().getRegistryName().toString());

    this.gameWrapper = gameWrapper;
    gameWrapper.load();

    logger.info("Loading players");
    for (var player : players) {
      player.connection.send(NetworkChannel.PLAY.getSimpleChannel().toVanillaPacket(
          new ChangeGameMessage(gameWrapper.getGame().getType()),
          NetworkDirection.PLAY_TO_CLIENT));

      if (oldGameWrapper != null && gameWrapper.getGame().persistPlayerData()
          && !oldGameWrapper.getGame().persistPlayerData()) {
        this.minecraftServer.getPlayerList().load(player);
      }

      gameWrapper.addPlayer(PlayerExtension.getOrThrow(player));
    }

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
    this.respawnPlayers(Predicates.alwaysTrue(), keepData);
  }

  public void respawnPlayers(Predicate<ServerPlayer> predicate, boolean keepData) {
    // Copy this as respawn will modify the list
    var players = List.copyOf(this.minecraftServer.getPlayerList().getPlayers());
    for (var playerEntity : players) {
      if (predicate.test(playerEntity)) {
        this.respawnPlayer(playerEntity, keepData);
      }
    }
  }

  public void respawnPlayer(ServerPlayer playerEntity, boolean keepData) {
    playerEntity.connection.player =
        this.minecraftServer.getPlayerList().respawn(playerEntity, keepData);
  }

  public LogicalServer load(CompoundTag tag) {
    var gameTag = tag.getCompound("game");
    if (!gameTag.isEmpty()) {
      var gameServer =
          GameServer.CODEC.parse(NbtOps.INSTANCE, gameTag).getOrThrow(false, logger::error);
      this.loadGame(new ServerGameWrapper(gameServer, this));
    }
    return this;
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    if (this.getGame().persistGameData()) {
      tag.put("game", GameServer.CODEC.encodeStart(NbtOps.INSTANCE, this.getGame())
          .getOrThrow(false, logger::error));
    }
    return tag;
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
    final var player = (ServerPlayer) event.getPlayer();
    this.gameWrapper.addPlayer(PlayerExtension.getOrThrow(player));
  }

  @SubscribeEvent
  public void handlePlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
    final var player = (ServerPlayer) event.getPlayer();
    player.reviveCaps();
    this.gameWrapper.removePlayer(PlayerExtension.getOrThrow(player));
  }
}
