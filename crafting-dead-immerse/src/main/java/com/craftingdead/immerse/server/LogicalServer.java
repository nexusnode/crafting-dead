/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.game.IGameServer;
import com.craftingdead.immerse.game.survival.SurvivalServer;
import com.craftingdead.immerse.network.login.SetupGameMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LogicalServer extends WorldSavedData {

  private static final Logger logger = LogManager.getLogger();

  private final Gson gson;

  private final MinecraftServer minecraftServer;

  private static final String GAME_FOLDER_NAME = "game";

  private final Path gamePath;

  private IGameServer<?> gameServer;

  public LogicalServer(MinecraftServer minecraftServer) {
    super(CraftingDead.ID);
    this.minecraftServer = minecraftServer;
    this.gamePath = CraftingDeadImmerse.getInstance().getModDir().resolve(GAME_FOLDER_NAME);
    this.gson = new GsonBuilder()
        .registerTypeAdapter(IGameServer.class, new IGameServer.Deserializer(this))
        .create();
  }

  public List<Pair<String, SetupGameMessage>> generateSetupGameMessage(boolean isLocal) {
    return Collections.singletonList(Pair.of(SetupGameMessage.class.getName(),
        new SetupGameMessage(this.gameServer.getGameType())));
  }

  public void startLoading() {}

  public void finishLoading() {
    this.minecraftServer.getWorld(World.OVERWORLD).getSavedData().getOrCreate(() -> this,
        CraftingDead.ID);

    // If there was no saved game in the world, load a new game
    if (this.gameServer == null) {
      this.loadNextGame();
    }
  }

  private void loadNextGame() {
    ForgeConfigSpec.ConfigValue<List<? extends String>> gameRotation =
        CraftingDeadImmerse.serverConfig.gameRotation;

    if (gameRotation.get().isEmpty()) {
      logger.info("Game rotation empty, defaulting to survival...");
      this.loadGame(new SurvivalServer());
      return;
    }

    final String nextGameName;
    if (this.gameServer == null) {
      nextGameName = gameRotation.get().get(0);
    } else {
      int nextGameIndex = gameRotation.get().indexOf(this.gameServer.getDisplayName()) + 1;
      nextGameName =
          gameRotation.get().get(nextGameIndex >= gameRotation.get().size() ? 0 : nextGameIndex);
    }

    if (!this.findAndLoadGame(nextGameName)) {
      logger.info("Removing game '{}' from game rotation", gameRotation);
      List<String> newGameRotation = new ArrayList<>(gameRotation.get());
      newGameRotation.remove(nextGameName);
      gameRotation.set(newGameRotation);
      gameRotation.save();
      this.loadNextGame();
    }
  }

  private boolean findAndLoadGame(String gameName) {
    File gameFile = new File(this.gamePath.toFile(), gameName + ".json");
    if (!gameFile.exists()) {
      logger.error("Game file with name '{}' does not exist");
      return false;
    }
    try (FileReader fileReader = new FileReader(gameFile)) {
      this.loadGame(this.gson.fromJson(fileReader, IGameServer.class));
      return true;
    } catch (IOException e) {
      logger.error("Failed to load game file '{}'", e);
      return false;
    }
  }

  private void loadGame(IGameServer<?> gameServer) {
    if (this.gameServer != null) {
      logger.info("Unloading game '{}'", this.gameServer.getDisplayName());
      this.gameServer.unload();
    }
    this.gameServer = gameServer;
    logger.info("Loading game '{}'", this.gameServer.getDisplayName());
    this.gameServer.load();
  }

  public MinecraftServer getMinecraftServer() {
    return this.minecraftServer;
  }

  public IGameServer<?> getGameServer() {
    return this.gameServer;
  }

  @Override
  public void read(CompoundNBT nbt) {
    String gameName = nbt.getString("gameName");
    if (!gameName.isEmpty() && this.findAndLoadGame(gameName)) {
      logger.info("Loading saved game ''", gameName);
      this.gameServer.deserializeNBT(nbt.getCompound("game"));
    }
  }

  @Override
  public CompoundNBT write(CompoundNBT nbt) {
    nbt.putString("gameType", this.gameServer.getGameType().getRegistryName().toString());
    nbt.put("game", this.gameServer.serializeNBT());
    return nbt;
  }

  // ================================================================================
  // Forge Events
  // ================================================================================

//  @SubscribeEvent
//  public void handleEntityJoinWorldEvent(EntityJoinWorldEvent event) {
//    if (event.getEntity() instanceof ServerPlayerEntity
//        && event.getWorld().getDimension().getType() == DimensionType.OVERWORLD) {
//      ServerPlayerEntity playerEntity = (ServerPlayerEntity) event.getEntity();
//      playerEntity.teleport(
//          this.minecraftServer.getWorld(DimensionType.byName(ModDimensions.MAP.getId())),
//          0, 100, 0, playerEntity.rotationYaw, playerEntity.rotationPitch);
//    }
//  }

  @SubscribeEvent
  public void handleServerTick(TickEvent.ServerTickEvent event) {
    switch (event.phase) {
      case START:
        this.gameServer.tick();
        break;
      default:
        break;
    }
  }
}
