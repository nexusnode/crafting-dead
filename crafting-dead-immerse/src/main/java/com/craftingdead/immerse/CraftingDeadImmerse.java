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

package com.craftingdead.immerse;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.annotation.Nullable;
import org.apache.commons.lang3.tuple.Pair;
import com.craftingdead.immerse.client.ClientDist;
import com.craftingdead.immerse.command.Commands;
import com.craftingdead.immerse.game.Game;
import com.craftingdead.immerse.game.GameTypes;
import com.craftingdead.immerse.game.module.ModuleTypes;
import com.craftingdead.immerse.game.network.GameNetworkChannel;
import com.craftingdead.immerse.network.NetworkChannel;
import com.craftingdead.immerse.server.LogicalServer;
import com.craftingdead.immerse.server.ServerConfig;
import com.craftingdead.immerse.server.ServerDist;
import com.craftingdead.immerse.sounds.ImmerseSoundEvents;
import com.craftingdead.immerse.util.DependencyLoader;
import io.sentry.Sentry;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.JarVersionLookupHandler;
import net.minecraftforge.server.permission.events.PermissionGatherEvent;

@Mod(CraftingDeadImmerse.ID)
public class CraftingDeadImmerse {

  private static final boolean ENABLE_SENTRY = false;

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

  private static final String SENTRY_DSN =
      "https://31d8ac34b0c24ddf98223098d42fd526@o1128514.ingest.sentry.io/6174174";

  /**
   * Singleton.
   */
  private static CraftingDeadImmerse instance;

  /**
   * Mod distribution.
   */
  private final ModDist modDist;

  private final Path modDir;

  @Nullable
  private LogicalServer logicalServer;

  public CraftingDeadImmerse() {
    instance = this;

    DependencyLoader.loadDependencies();

    this.modDir = FMLPaths.CONFIGDIR.get().resolve(ID);
    if (!Files.exists(this.modDir)) {
      try {
        Files.createDirectory(this.modDir);
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }

    ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, serverConfigSpec);

    this.modDist = DistExecutor.unsafeRunForDist(() -> ClientDist::new, () -> ServerDist::new);

    final var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    modEventBus.addListener(this::handleCommonSetup);

    ImmerseSoundEvents.SOUND_EVENTS.register(modEventBus);
    GameTypes.GAME_TYPES.register(modEventBus);
    ModuleTypes.MODULE_TYPES.register(modEventBus);

    MinecraftForge.EVENT_BUS.register(this);

    if (ENABLE_SENTRY) {
      Sentry.init(options -> {
        options.setDsn(SENTRY_DSN);
        // Set tracesSampleRate to 1.0 to capture 100% of transactions for performance monitoring.
        // We recommend adjusting this value in production.
        options.setTracesSampleRate(1.0);
        // When first trying Sentry it's good to see what the SDK is doing:
        options.setDebug(true);
      });
    }
  }

  @Nullable
  public LogicalServer getLogicalServer() {
    return this.logicalServer;
  }

  public ModDist getModDist() {
    return this.modDist;
  }

  public ClientDist getClientDist() {
    if (this.modDist instanceof ClientDist clientDist) {
      return clientDist;
    }
    throw new IllegalStateException("Accessing client dist on wrong side");
  }

  public Path getModDir() {
    return this.modDir;
  }

  public Game getGame(LogicalSide side) {
    return switch (side) {
      case CLIENT -> this.getClientDist().getGameClient();
      case SERVER -> this.getLogicalServer().getGame();
      default -> throw new IllegalArgumentException("Unknown side: " + side.toString());
    };
  }

  public static CraftingDeadImmerse getInstance() {
    return instance;
  }

  // ================================================================================
  // Mod Events
  // ================================================================================

  private void handleCommonSetup(FMLCommonSetupEvent event) {
    NetworkChannel.loadChannels();
    GameNetworkChannel.load();
  }

  // ================================================================================
  // Forge Events
  // ================================================================================

  @SubscribeEvent
  public void handlePermissionNodesGather(PermissionGatherEvent.Nodes event) {
    event.addNodes(Permissions.GAME_OP);
  }

  @SubscribeEvent
  public void handleRegisterCommands(RegisterCommandsEvent event) {
    Commands.register(event.getDispatcher());
  }

  @SubscribeEvent
  public void handleServerAboutToStart(ServerAboutToStartEvent event) {
    this.logicalServer = this.modDist.createLogicalServer(event.getServer());
    this.logicalServer.startLoading();
    MinecraftForge.EVENT_BUS.register(this.logicalServer);
  }

  @SubscribeEvent
  public void handleServerStarting(ServerStartingEvent event) {
    this.logicalServer.finishLoading();
  }

  @SubscribeEvent
  public void handleServerStopped(ServerStoppedEvent event) {
    // Server may not have fully started yet so could be null
    if (this.logicalServer != null) {
      MinecraftForge.EVENT_BUS.unregister(this.logicalServer);
      this.logicalServer = null;
    }
  }
}
