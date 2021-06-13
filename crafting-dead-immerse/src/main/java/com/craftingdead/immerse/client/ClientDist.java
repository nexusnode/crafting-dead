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

package com.craftingdead.immerse.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.ModDist;
import com.craftingdead.immerse.client.fake.FakePlayerEntity;
import com.craftingdead.immerse.client.gui.IngameGui;
import com.craftingdead.immerse.client.gui.screen.menu.MainMenuView;
import com.craftingdead.immerse.client.renderer.SpectatorRenderer;
import com.craftingdead.immerse.client.renderer.entity.layer.TeamClothingLayer;
import com.craftingdead.immerse.client.shader.RoundedFrameShader;
import com.craftingdead.immerse.client.shader.RoundedRectShader;
import com.craftingdead.immerse.client.util.ServerPinger;
import com.craftingdead.immerse.game.ClientGameWrapper;
import com.craftingdead.immerse.game.GameClient;
import com.craftingdead.immerse.game.GameType;
import com.craftingdead.immerse.server.LogicalServer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.progress.StartupMessageManager;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;

public class ClientDist implements ModDist, ISelectiveResourceReloadListener {

  public static final KeyBinding SWITCH_TEAMS =
      new KeyBinding("key.switch_teams", KeyConflictContext.UNIVERSAL, KeyModifier.NONE,
          InputMappings.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_M), "key.categories.gameplay");

  public static final ResourceLocation BLUR_SHADER =
      new ResourceLocation(CraftingDeadImmerse.ID, "shaders/post/fade_in_blur.json");

  private static final Logger logger = LogManager.getLogger();

  private final Minecraft minecraft;

  @Nullable
  private LogicalServer logicalServer;

  private ClientGameWrapper gameWrapper;

  private final SpectatorRenderer spectatorRenderer;

  private final IngameGui ingameGui;

  public ClientDist() {
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::handleClientSetup);
    MinecraftForge.EVENT_BUS.register(this);
    this.minecraft = Minecraft.getInstance();
    ((IReloadableResourceManager) this.minecraft.getResourceManager()).registerReloadListener(this);
    this.spectatorRenderer = new SpectatorRenderer();
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

  @Override
  public LogicalServer createLogicalServer(MinecraftServer minecraftServer) {
    return new LogicalServer(minecraftServer);
  }

  @Override
  public void onResourceManagerReload(IResourceManager resourceManager,
      Predicate<IResourceType> resourcePredicate) {
    RoundedRectShader.INSTANCE.compile(resourceManager);
    RoundedFrameShader.INSTANCE.compile(resourceManager);
  }

  // ================================================================================
  // Mod Events
  // ================================================================================

  private void handleClientSetup(FMLClientSetupEvent event) {
    ClientRegistry.registerKeyBinding(SWITCH_TEAMS);

    CraftingDead.getInstance().getClientDist().registerPlayerLayer(TeamClothingLayer::new);

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
    final PlayerExtension<ClientPlayerEntity> player =
        CraftingDead.getInstance().getClientDist().getPlayer().orElse(null);
    final PlayerExtension<AbstractClientPlayerEntity> viewingPlayer =
        this.minecraft.getCameraEntity() instanceof AbstractClientPlayerEntity
            ? ((AbstractClientPlayerEntity) this.minecraft.getCameraEntity())
                .getCapability(Capabilities.LIVING)
                .<PlayerExtension<AbstractClientPlayerEntity>>cast()
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
  public void handleGuiOpen(GuiOpenEvent event) {
    if (event.getGui() instanceof MainMenuScreen
        || event.getGui() instanceof MultiplayerScreen) {
      event.setGui(MainMenuView.createScreen());
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
          boolean worldFocused = !this.minecraft.isPaused() && this.minecraft.overlay == null
              && (this.minecraft.screen == null);

          if (this.minecraft.player.isSpectator()) {
            if (this.minecraft.getCameraEntity() instanceof RemoteClientPlayerEntity) {
              this.spectatorRenderer
                  .tick((AbstractClientPlayerEntity) this.minecraft.getCameraEntity());
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
}
