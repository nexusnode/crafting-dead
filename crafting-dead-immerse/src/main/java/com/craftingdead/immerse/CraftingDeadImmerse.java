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

package com.craftingdead.immerse;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import org.apache.commons.lang3.tuple.Pair;
import com.craftingdead.immerse.block.ModBlocks;
import com.craftingdead.immerse.client.ClientDist;
import com.craftingdead.immerse.game.GameType;
import com.craftingdead.immerse.game.GameTypes;
import com.craftingdead.immerse.game.IGame;
import com.craftingdead.immerse.game.network.GameNetworkChannel;
import com.craftingdead.immerse.item.ModItems;
import com.craftingdead.immerse.network.NetworkChannel;
import com.craftingdead.immerse.server.LogicalServer;
import com.craftingdead.immerse.server.ServerConfig;
import com.craftingdead.immerse.server.ServerDist;
import com.craftingdead.immerse.util.ModSoundEvents;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.JarVersionLookupHandler;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

@Mod(CraftingDeadImmerse.ID)
public class CraftingDeadImmerse {

  public static final String ID = "craftingdeadimmerse";

  public static final String VERSION;

  public static final String DISPLAY_NAME;

  public static final ServerConfig serverConfig;
  public static final ForgeConfigSpec serverConfigSpec;

  static {
    VERSION = JarVersionLookupHandler
        .getImplementationVersion(CraftingDeadImmerse.class)
        .orElse("[version]");
    DISPLAY_NAME = JarVersionLookupHandler
        .getImplementationTitle(CraftingDeadImmerse.class)
        .orElse("[display_name]");

    final Pair<ServerConfig, ForgeConfigSpec> serverConfigPair =
        new ForgeConfigSpec.Builder().configure(ServerConfig::new);
    serverConfigSpec = serverConfigPair.getRight();
    serverConfig = serverConfigPair.getLeft();
  }

  /**
   * Singleton.
   */
  private static CraftingDeadImmerse instance;

  /**
   * Mod distribution.
   */
  private final IModDist modDist;

  private final Path modDir;

  @Nullable
  private LogicalServer logicalServer;

  private final Supplier<IForgeRegistry<GameType>> gameTypeRegistry;

  public CraftingDeadImmerse() {
    instance = this;

    this.modDir = FMLPaths.CONFIGDIR.get().resolve(ID);
    if (!Files.exists(this.modDir)) {
      try {
        Files.createDirectory(this.modDir);
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }

    ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, serverConfigSpec);

    this.modDist = DistExecutor.safeRunForDist(() -> ClientDist::new, () -> ServerDist::new);

    final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    ModBlocks.BLOCKS.register(modEventBus);
    ModItems.ITEMS.register(modEventBus);
    ModSoundEvents.SOUND_EVENTS.register(modEventBus);

    this.gameTypeRegistry = GameTypes.GAME_TYPES.makeRegistry("game_type", RegistryBuilder::new);
    GameTypes.GAME_TYPES.register(modEventBus);

    modEventBus.addListener(this::handleCommonSetup);
    MinecraftForge.EVENT_BUS.register(this);

    // SchematicFormats.SCHEMATIC_FORMATS.makeRegistry("map_formats", RegistryBuilder::new);
    // SchematicFormats.SCHEMATIC_FORMATS.register(modEventBus);
    //
    // ModDimensions.MOD_DIMENSIONS.register(modEventBus);
  }

  public IForgeRegistry<GameType> getGameTypeRegistry() {
    return this.gameTypeRegistry.get();
  }

  @Nullable
  public LogicalServer getLogicalServer() {
    return this.logicalServer;
  }

  public IModDist getModDist() {
    return this.modDist;
  }

  public ClientDist getClientDist() {
    if (this.modDist instanceof ClientDist) {
      return (ClientDist) this.modDist;
    }
    throw new IllegalStateException("Accessing client dist on wrong side");
  }

  public Path getModDir() {
    return this.modDir;
  }

  public IGame getGame(LogicalSide side) {
    switch (side) {
      case CLIENT:
        return this.getClientDist().getGameClient();
      case SERVER:
        return this.getLogicalServer().getGameServer();
      default:
        throw new IllegalArgumentException("Unkown side: " + side.toString());
    }
  }

  public static CraftingDeadImmerse getInstance() {
    return instance;
  }

  // ================================================================================
  // Forge Events
  // ================================================================================

  public void handleCommonSetup(FMLCommonSetupEvent event) {
    NetworkChannel.loadChannels();
    GameNetworkChannel.load();
  }

  @SubscribeEvent
  public void handleServerAboutToStart(FMLServerAboutToStartEvent event) {
    this.logicalServer = this.modDist.createLogicalServer(event.getServer());
    this.logicalServer.startLoading();
    MinecraftForge.EVENT_BUS.register(this.logicalServer);
  }

  @SubscribeEvent
  public void handleServerStarting(FMLServerStartingEvent event) {
    this.logicalServer.finishLoading();
  }

  @SubscribeEvent
  public void handleServerStopped(FMLServerStoppedEvent event) {
    // Server may not have fully started yet so could be null
    if (this.logicalServer != null) {
      MinecraftForge.EVENT_BUS.unregister(this.logicalServer);
      this.logicalServer = null;
    }
  }
}
