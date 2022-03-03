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

package com.craftingdead.immerse.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import javax.annotation.Nullable;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.sources.ManualTimingSource;
import org.lwjgl.glfw.GLFW;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.ModDist;
import com.craftingdead.immerse.client.fake.FakePlayerEntity;
import com.craftingdead.immerse.client.gui.IngameGui;
import com.craftingdead.immerse.client.gui.screen.menu.MainMenuView;
import com.craftingdead.immerse.client.gui.view.ViewScreen;
import com.craftingdead.immerse.client.gui.view.style.StylesheetManager;
import com.craftingdead.immerse.client.renderer.BlueprintOutlineRenderer;
import com.craftingdead.immerse.client.renderer.SpectatorRenderer;
import com.craftingdead.immerse.client.renderer.entity.layer.TeamClothingLayer;
import com.craftingdead.immerse.client.shader.RectShader;
import com.craftingdead.immerse.client.shader.RoundedRectShader;
import com.craftingdead.immerse.client.shader.RoundedTexShader;
import com.craftingdead.immerse.client.util.ServerPinger;
import com.craftingdead.immerse.game.ClientGameWrapper;
import com.craftingdead.immerse.game.GameClient;
import com.craftingdead.immerse.game.GameType;
import com.craftingdead.immerse.server.LogicalServer;
import com.craftingdead.immerse.util.LwjglNativeUtil;
import com.craftingdead.immerse.world.item.BlueprintItem;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.DrawSelectionEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.progress.StartupMessageManager;
import net.rocketpowered.api.Rocket;
import net.rocketpowered.connector.client.gui.RocketToast;
import reactor.core.scheduler.Schedulers;

public class ClientDist implements ModDist {

  private static final ManualTimingSource TIMING_SOURCE = new ManualTimingSource();

  public static final KeyMapping SWITCH_TEAMS =
      new KeyMapping("key.switch_teams", KeyConflictContext.UNIVERSAL, KeyModifier.NONE,
          InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_M), "key.categories.gameplay");

  public static final ResourceLocation BLUR_SHADER =
      new ResourceLocation(CraftingDeadImmerse.ID, "shaders/post/fade_in_blur.json");

  private static final Logger logger = LogUtils.getLogger();

  static {
    Animator.setDefaultTimingSource(TIMING_SOURCE);
  }

  private final Minecraft minecraft;

  @Nullable
  private LogicalServer logicalServer;

  private ClientGameWrapper gameWrapper;

  private final SpectatorRenderer spectatorRenderer;

  private final BlueprintOutlineRenderer blueprintOutlineRenderer;

  private final IngameGui ingameGui;

  private boolean firstLoad = true;

  @Nullable
  private static RectShader rectShader;
  @Nullable
  private static RoundedRectShader roundedRectShader;
  @Nullable
  private static RoundedTexShader roundedTexShader;

  public ClientDist() {
    if (FMLLoader.isProduction()) {
      LwjglNativeUtil.load("lwjgl_yoga");
    }

    final var modBus = FMLJavaModLoadingContext.get().getModEventBus();
    modBus.addListener(this::handleClientSetup);
    modBus.addListener(this::handleEntityRenderersAddLayers);
    modBus.addListener(this::handleRegisterShaders);
    modBus.addListener(this::handleRegisterClientReloadListeners);
    MinecraftForge.EVENT_BUS.register(this);
    this.minecraft = Minecraft.getInstance();
    this.spectatorRenderer = new SpectatorRenderer();
    this.blueprintOutlineRenderer = new BlueprintOutlineRenderer();
    this.ingameGui = new IngameGui();
  }

  @Nullable
  public ClientGameWrapper getGameWrapper() {
    return this.gameWrapper;
  }

  @Nullable
  public GameClient getGameClient() {
    return this.gameWrapper == null ? null : this.gameWrapper.getGame();
  }

  public SpectatorRenderer getSpectatorRenderer() {
    return this.spectatorRenderer;
  }

  public IngameGui getIngameGui() {
    return this.ingameGui;
  }

  public void loadGame(GameType gameType) {
    logger.info("Loading game: {}", gameType.getRegistryName().toString());
    try {
      if (this.gameWrapper != null) {
        this.gameWrapper.unload();
      }
      this.gameWrapper = new ClientGameWrapper(gameType.createGameClient());
      this.gameWrapper.load();
    } catch (Exception e) {
      logger.error("Failed to load game", e);
    }
  }

  @Nullable
  public static RectShader getRectShader() {
    return rectShader;
  }

  @Nullable
  public static RoundedRectShader getRoundedRectShader() {
    return roundedRectShader;
  }

  @Nullable
  public static RoundedTexShader getRoundedTexShader() {
    return roundedTexShader;
  }

  @Override
  public LogicalServer createLogicalServer(MinecraftServer minecraftServer) {
    return new LogicalServer(minecraftServer);
  }

  // ================================================================================
  // Mod Events
  // ================================================================================

  private void handleRegisterClientReloadListeners(RegisterClientReloadListenersEvent event) {
    event.registerReloadListener(StylesheetManager.getInstance());
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private void handleEntityRenderersAddLayers(EntityRenderersEvent.AddLayers event) {
    event.getSkins().forEach(skin -> {
      var renderer = event.getSkin(skin);
      renderer.addLayer(new TeamClothingLayer(renderer));
    });
  }

  private void handleClientSetup(FMLClientSetupEvent event) {
    ClientRegistry.registerKeyBinding(SWITCH_TEAMS);

    this.blueprintOutlineRenderer.register();

    // GLFW code needs to run on main thread
    this.minecraft.submit(() -> {
      StartupMessageManager.addModMessage("Applying branding");
      try {
        InputStream smallIcon = this.minecraft
            .getResourceManager()
            .getResource(
                new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/icons/icon_16x16.png"))
            .getInputStream();
        InputStream mediumIcon = this.minecraft
            .getResourceManager()
            .getResource(
                new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/icons/icon_32x32.png"))
            .getInputStream();
        this.minecraft.getWindow().setIcon(smallIcon, mediumIcon);
      } catch (IOException e) {
        logger.error("Couldn't set icon", e);
      }
    });
  }

  private void handleRegisterShaders(RegisterShadersEvent event) {
    try {
      event.registerShader(new ShaderInstance(event.getResourceManager(),
          new ResourceLocation(CraftingDeadImmerse.ID, "rect"),
          DefaultVertexFormat.POSITION_COLOR),
          shader -> rectShader = new RectShader(shader));
      event.registerShader(new ShaderInstance(event.getResourceManager(),
          new ResourceLocation(CraftingDeadImmerse.ID, "rounded_rect"),
          DefaultVertexFormat.POSITION_COLOR),
          shader -> roundedRectShader = new RoundedRectShader(shader));
      event.registerShader(new ShaderInstance(event.getResourceManager(),
          new ResourceLocation(CraftingDeadImmerse.ID, "rounded_tex"),
          DefaultVertexFormat.POSITION_TEX),
          shader -> roundedTexShader = new RoundedTexShader(shader));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  // ================================================================================
  // Forge Events
  // ================================================================================

  @SubscribeEvent
  public void handleRenderNameplate(RenderNameplateEvent event) {
    if (event.getEntity() instanceof FakePlayerEntity) {
      event.setResult(Event.Result.DENY);
    }
  }

  @SubscribeEvent
  public void handlePlayerLoggedOut(ClientPlayerNetworkEvent.LoggedOutEvent event) {
    if (this.gameWrapper != null) {
      this.gameWrapper.unload();
      this.gameWrapper = null;
    }
  }

  @SubscribeEvent
  public void handleRenderGameOverlayPre(RenderGameOverlayEvent.Pre event) {
    final PlayerExtension<LocalPlayer> player =
        CraftingDead.getInstance().getClientDist().getPlayerExtension().orElse(null);
    final PlayerExtension<AbstractClientPlayer> viewingPlayer =
        this.minecraft.getCameraEntity() instanceof AbstractClientPlayer
            ? ((AbstractClientPlayer) this.minecraft.getCameraEntity())
                .getCapability(LivingExtension.CAPABILITY)
                .<PlayerExtension<AbstractClientPlayer>>cast()
                .orElse(null)
            : null;

    switch (event.getType()) {
      case ALL:
        if (viewingPlayer != null && this.getGameClient() != null) {
          this.ingameGui.renderOverlay(viewingPlayer, event.getMatrixStack(),
              event.getWindow().getGuiScaledWidth(), event.getWindow().getGuiScaledHeight(),
              event.getPartialTicks());
          event.setCanceled(this.getGameClient().renderOverlay(viewingPlayer,
              event.getMatrixStack(),
              event.getWindow().getGuiScaledWidth(), event.getWindow().getGuiScaledHeight(),
              event.getPartialTicks()));
        }
        break;
      case PLAYER_LIST:
        if (player != null && this.getGameClient() != null) {
          event.setCanceled(this.getGameClient().renderPlayerList(player,
              event.getMatrixStack(),
              event.getWindow().getGuiScaledWidth(), event.getWindow().getGuiScaledHeight(),
              event.getPartialTicks()));
        }
        break;
      default:
        break;
    }
  }

  @SubscribeEvent
  public void handleGuiOpen(ScreenOpenEvent event) {
    if (event.getScreen() instanceof TitleScreen && this.firstLoad) {
      // Do this here to make sure font is loaded for toasts
      Rocket.getGameClientGatewayFeed()
          .doOnNext(__ -> RocketToast.info(this.minecraft, "Connected to Rocket"))
          .flatMap(connection -> connection.onClose()
              .doOnSuccess(__ -> RocketToast.info(this.minecraft, "Disconnected from Rocket")))
          .publishOn(Schedulers.fromExecutor(this.minecraft))
          .subscribe();
      this.firstLoad = false;
    }

    if (event.getScreen() instanceof TitleScreen
        || event.getScreen() instanceof JoinMultiplayerScreen) {
      if (this.minecraft.screen instanceof ViewScreen screen
          && screen.getRoot() instanceof MainMenuView) {
        event.setCanceled(true);
        return;
      }
      event.setScreen(MainMenuView.createScreen());
    }
  }

  @SubscribeEvent
  public void handleClientTick(TickEvent.ClientTickEvent event) {
    switch (event.phase) {
      case START:
        if (this.gameWrapper != null) {
          this.gameWrapper.tick();
        }

        ServerPinger.INSTANCE.pingPendingNetworks();

        if (this.minecraft.player != null) {
          boolean worldFocused = !this.minecraft.isPaused() && this.minecraft.getOverlay() == null
              && (this.minecraft.screen == null);

          if (this.minecraft.player.isSpectator()) {
            if (this.minecraft.getCameraEntity() instanceof RemotePlayer) {
              this.spectatorRenderer
                  .tick((AbstractClientPlayer) this.minecraft.getCameraEntity());
            }
          }

          if (worldFocused && this.getGameClient() != null) {
            if (this.getGameClient().disableSwapHands()) {
              while (this.minecraft.options.keySwapOffhand.consumeClick());
            }
          }
        }
        break;
      default:
        break;
    }
  }

  @SubscribeEvent
  public void handleRenderTick(TickEvent.RenderTickEvent event) {
    switch (event.phase) {
      case START:
        TIMING_SOURCE.tick();
        break;
      default:
        break;
    }
  }

  @SubscribeEvent
  public void handleDrawHighlightBlock(DrawSelectionEvent.HighlightBlock event) {
    var cameraPlayer = CraftingDead.getInstance().getClientDist().getCameraPlayer();
    if (cameraPlayer != null
        && cameraPlayer.getMainHandItem().getItem() instanceof BlueprintItem blueprint) {
      event.setCanceled(true);
      this.blueprintOutlineRenderer.render(cameraPlayer, blueprint, event.getTarget(),
          event.getCamera(), event.getPoseStack(), event.getMultiBufferSource());
    }
  }
}
