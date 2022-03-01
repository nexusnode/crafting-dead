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

package com.craftingdead.immerse;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.annotation.Nullable;
import com.craftingdead.core.capability.CapabilityUtil;
import com.craftingdead.immerse.client.ClientDist;
import com.craftingdead.immerse.command.Commands;
import com.craftingdead.immerse.game.Game;
import com.craftingdead.immerse.game.GameTypes;
import com.craftingdead.immerse.game.module.ModuleTypes;
import com.craftingdead.immerse.game.network.GameNetworkChannel;
import com.craftingdead.immerse.network.NetworkChannel;
import com.craftingdead.immerse.network.play.SyncLandChunkMessage;
import com.craftingdead.immerse.network.play.SyncLandManagerMessage;
import com.craftingdead.immerse.server.LogicalServer;
import com.craftingdead.immerse.server.ServerConfig;
import com.craftingdead.immerse.server.ServerDist;
import com.craftingdead.immerse.sounds.ImmerseSoundEvents;
import com.craftingdead.immerse.world.action.ImmerseActionTypes;
import com.craftingdead.immerse.world.item.ImmerseItems;
import com.craftingdead.immerse.world.level.block.ImmerseBlocks;
import com.craftingdead.immerse.world.level.block.entity.ImmerseBlockEntityTypes;
import com.craftingdead.immerse.world.level.extension.LandOwnerTypes;
import com.craftingdead.immerse.world.level.extension.LevelExtension;
import io.netty.buffer.Unpooled;
import io.sentry.Sentry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
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
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.permission.events.PermissionGatherEvent;

@Mod(CraftingDeadImmerse.ID)
public class CraftingDeadImmerse {

  public static final String ID = "craftingdeadimmerse";

  public static final String VERSION = JarVersionLookupHandler
      .getImplementationVersion(CraftingDeadImmerse.class)
      .orElse("[version]");

  public static final String DISPLAY_NAME = JarVersionLookupHandler
      .getImplementationTitle(CraftingDeadImmerse.class)
      .orElse("[display_name]");

  public static final ServerConfig serverConfig;
  public static final ForgeConfigSpec serverConfigSpec;
  public static final CommonConfig commonConfig;
  public static final ForgeConfigSpec commonConfigSpec;

  static {
    var serverConfigPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
    serverConfigSpec = serverConfigPair.getRight();
    serverConfig = serverConfigPair.getLeft();

    var commonConfigPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
    commonConfigSpec = commonConfigPair.getRight();
    commonConfig = commonConfigPair.getLeft();
  }

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

    this.modDir = FMLPaths.CONFIGDIR.get().resolve(ID);
    if (!Files.exists(this.modDir)) {
      try {
        Files.createDirectory(this.modDir);
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }

    ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, serverConfigSpec);
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, commonConfigSpec);

    this.modDist = DistExecutor.unsafeRunForDist(() -> ClientDist::new, () -> ServerDist::new);

    final var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    modEventBus.addListener(this::handleCommonSetup);

    ImmerseSoundEvents.soundEvents.register(modEventBus);
    GameTypes.gameTypes.register(modEventBus);
    ModuleTypes.moduleTypes.register(modEventBus);
    ImmerseActionTypes.actionTypes.register(modEventBus);
    ImmerseBlocks.blocks.register(modEventBus);
    ImmerseItems.items.register(modEventBus);
    ImmerseBlockEntityTypes.blockEntityTypes.register(modEventBus);
    LandOwnerTypes.landOwnerTypes.register(modEventBus);

    MinecraftForge.EVENT_BUS.register(this);
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
    if (commonConfig.sentryEnabled.get()) {
      Sentry.init(options -> {
        options.setDsn(commonConfig.sentryDsn.get());
        // Set tracesSampleRate to 1.0 to capture 100% of transactions for performance monitoring.
        // We recommend adjusting this value in production.
        options.setTracesSampleRate(0.5);
      });
    }

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

  @SubscribeEvent
  public void handleAttachCapabilities(AttachCapabilitiesEvent<Level> event) {
    event.addCapability(LevelExtension.CAPABILITY_KEY,
        CapabilityUtil.serializableProvider(() -> LevelExtension.create(event.getObject()),
            LevelExtension.CAPABILITY));
  }

  @SubscribeEvent
  public void handleChunkWatch(ChunkWatchEvent.Watch event) {
    var landManager = LevelExtension.getOrThrow(event.getWorld()).getLandManager();
    var buf = new FriendlyByteBuf(Unpooled.buffer());
    landManager.writeChunkToBuf(event.getPos(), buf);
    NetworkChannel.PLAY.getSimpleChannel().send(
        PacketDistributor.PLAYER.with(event::getPlayer),
        new SyncLandChunkMessage(event.getPos(), buf));
  }

  @SubscribeEvent
  public void handleLevelTick(TickEvent.WorldTickEvent event) {
    LevelExtension.getOrThrow(event.world).tick();
  }

  @SubscribeEvent
  public void handlePlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
    sendLandManager((ServerPlayer) event.getPlayer());
  }

  @SubscribeEvent
  public void handlePlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
    sendLandManager((ServerPlayer) event.getPlayer());
  }

  private static void sendLandManager(ServerPlayer player) {
    var landManager = LevelExtension.getOrThrow(player.getLevel()).getLandManager();
    var buf = new FriendlyByteBuf(Unpooled.buffer());
    landManager.writeToBuf(buf);
    NetworkChannel.PLAY.getSimpleChannel()
        .send(PacketDistributor.PLAYER.with(() -> player), new SyncLandManagerMessage(buf));
  }
}
