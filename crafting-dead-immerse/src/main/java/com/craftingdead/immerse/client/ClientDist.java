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
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.living.IPlayer;
import com.craftingdead.core.event.RenderArmClothingEvent;
import com.craftingdead.core.util.Text;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.IModDist;
import com.craftingdead.immerse.client.gui.IngameGui;
import com.craftingdead.immerse.client.gui.screen.game.ShopScreen;
import com.craftingdead.immerse.client.gui.transition.TransitionManager;
import com.craftingdead.immerse.client.gui.transition.Transitions;
import com.craftingdead.immerse.client.renderer.SpectatorRenderer;
import com.craftingdead.immerse.client.renderer.entity.layer.TeamClothingLayer;
import com.craftingdead.immerse.client.shader.RoundedFrameShader;
import com.craftingdead.immerse.client.shader.RoundedRectShader;
import com.craftingdead.immerse.game.GameType;
import com.craftingdead.immerse.game.GameUtil;
import com.craftingdead.immerse.game.IGameClient;
import com.craftingdead.immerse.game.deathmatch.client.SelectTeamScreen;
import com.craftingdead.immerse.game.team.AbstractTeamGame;
import com.craftingdead.immerse.game.team.ITeam;
import com.craftingdead.immerse.game.team.ITeamGame;
import com.craftingdead.immerse.server.LogicalServer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.progress.StartupMessageManager;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;

public class ClientDist implements IModDist, ISelectiveResourceReloadListener {

  public static final KeyBinding SWITCH_TEAMS =
      new KeyBinding("key.switch_teams", KeyConflictContext.UNIVERSAL, KeyModifier.NONE,
          InputMappings.Type.KEYSYM.getOrMakeInput(GLFW.GLFW_KEY_M), "key.categories.gameplay");

  public static final ResourceLocation BLUR_SHADER =
      new ResourceLocation(CraftingDeadImmerse.ID, "shaders/post/fade_in_blur.json");

  // private static final String DISCORD_CLIENT_ID = "475405055302828034";

  private static final Logger logger = LogManager.getLogger();

  private final Minecraft minecraft;

  private final TransitionManager transitionManager;

  private float lastTime = 0F;

  private float deltaTime;

  @Nullable
  private LogicalServer logicalServer;

  private IGameClient gameClient;

  private final SpectatorRenderer spectatorRenderer;

  private final IngameGui ingameGui;

  public ClientDist() {
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::handleClientSetup);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::handleLoadComplete);
    MinecraftForge.EVENT_BUS.register(this);
    this.minecraft = Minecraft.getInstance();
    this.transitionManager = new TransitionManager(this.minecraft, Transitions.GROW);
    ((IReloadableResourceManager) this.minecraft.getResourceManager()).addReloadListener(this);
    this.spectatorRenderer = new SpectatorRenderer();
    this.ingameGui = new IngameGui();
  }

  public SpectatorRenderer getSpectatorRenderer() {
    return this.spectatorRenderer;
  }

  @Override
  public LogicalServer createLogicalServer(MinecraftServer minecraftServer) {
    return new LogicalServer(minecraftServer);
  }

  public void loadGame(GameType gameType) {
    logger.info("Loading game: {}", gameType.getRegistryName().toString());
    try {
      if (this.gameClient != null) {
        this.gameClient.unload();
      }
      this.gameClient = gameType.createGameClient();
      this.gameClient.load();
    } catch (Exception e) {
      this.minecraft.displayGuiScreen(new DisconnectedScreen(new MainMenuScreen(),
          new TranslationTextComponent("connect.failed"),
          new TranslationTextComponent("disconnect.genericReason", e.toString())));
    }
  }

  @Override
  public void onResourceManagerReload(IResourceManager resourceManager,
      Predicate<IResourceType> resourcePredicate) {
    RoundedRectShader.INSTANCE.compile(resourceManager);
    RoundedFrameShader.INSTANCE.compile(resourceManager);
  }

  public float getDeltaTime() {
    return this.deltaTime;
  }

  public IGameClient getGameClient() {
    return this.gameClient;
  }

  public IngameGui getIngameGui() {
    return this.ingameGui;
  }

  // ================================================================================
  // Mod Events
  // ================================================================================

  private void handleClientSetup(FMLClientSetupEvent event) {
    ClientRegistry.registerKeyBinding(SWITCH_TEAMS);

    CraftingDead.getInstance().getClientDist().registerPlayerLayer(TeamClothingLayer::new);

    // GLFW code needs to run on main thread
    this.minecraft.enqueue(() -> {
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
        this.minecraft.getMainWindow().setWindowIcon(smallIcon, mediumIcon);
      } catch (IOException e) {
        logger.error("Couldn't set icon", e);
      }
    });
  }

  private void handleLoadComplete(FMLLoadCompleteEvent event) {
    // StartupMessageManager.addModMessage("Loading Discord integration");
    // DiscordPresence.initialize(DISCORD_CLIENT_ID);
    // DiscordPresence.updateState(DiscordPresence.GameState.IDLE, this);
  }

  // ================================================================================
  // Forge Events
  // ================================================================================

  @SubscribeEvent
  public void handleRenderGameOverlayPre(RenderGameOverlayEvent.Pre event) {
    final IPlayer<ClientPlayerEntity> player =
        CraftingDead.getInstance().getClientDist().getPlayer().orElse(null);
    final IPlayer<AbstractClientPlayerEntity> viewingPlayer =
        this.minecraft.getRenderViewEntity() instanceof AbstractClientPlayerEntity
            ? ((AbstractClientPlayerEntity) this.minecraft.getRenderViewEntity())
                .getCapability(ModCapabilities.LIVING).<IPlayer<AbstractClientPlayerEntity>>cast()
                .orElse(null)
            : null;

    switch (event.getType()) {
      case ALL:
        if (viewingPlayer != null && this.gameClient != null) {
          this.ingameGui.renderOverlay(viewingPlayer, event.getMatrixStack(),
              event.getWindow().getScaledWidth(), event.getWindow().getScaledHeight(),
              event.getPartialTicks());
          this.gameClient.renderOverlay(viewingPlayer, event.getMatrixStack(),
              event.getWindow().getScaledWidth(), event.getWindow().getScaledHeight(),
              event.getPartialTicks());
        }
        break;
      case PLAYER_LIST:
        if (player != null && this.gameClient != null) {
          event.setCanceled(true);
          this.gameClient.renderPlayerList(player, event.getMatrixStack(),
              event.getWindow().getScaledWidth(), event.getWindow().getScaledHeight(),
              event.getPartialTicks());
        }
        break;
      default:
        break;
    }
  }

  @SubscribeEvent
  public void handleClientPlayerLoggedIn(ClientPlayerNetworkEvent.LoggedInEvent event) {
    // if (event.getPlayer() == this.minecraft.player) {
    // ClientPlayNetHandler connection = this.minecraft.getConnection();
    // if (connection != null && connection.getNetworkManager().isChannelOpen()) {
    // if (this.minecraft.getIntegratedServer() != null
    // && !this.minecraft.getIntegratedServer().getPublic()) {
    // DiscordPresence.updateState(DiscordPresence.GameState.SINGLEPLAYER, this);
    // } else if (this.minecraft.isConnectedToRealms()) {
    // DiscordPresence.updateState(DiscordPresence.GameState.REALMS, this);
    // } else if (this.minecraft.getIntegratedServer() == null
    // && (this.minecraft.getCurrentServerData() == null
    // || !this.minecraft.getCurrentServerData().isOnLAN())) {
    // DiscordPresence.updateState(DiscordPresence.GameState.MULTIPLAYER, this);
    // } else {
    // DiscordPresence.updateState(DiscordPresence.GameState.LAN, this);
    // }
    // }
    // }
  }

  @SubscribeEvent
  public void handleClientPlayerLoggedOut(ClientPlayerNetworkEvent.LoggedOutEvent event) {
    // DiscordPresence.updateState(DiscordPresence.GameState.IDLE, this);
  }

  @SubscribeEvent
  public void handleGuiOpen(GuiOpenEvent event) {
    if (event.getGui() instanceof MainMenuScreen) {
      // event.setGui(new StartScreen());
    }
  }

  @SubscribeEvent
  public void handleDrawScreenPre(DrawScreenEvent.Pre event) {
    event.setCanceled(
        this.transitionManager.checkDrawTransition(event.getMatrixStack(), event.getMouseX(),
            event.getMouseY(), event.getRenderPartialTicks(), event.getGui()));
  }

  @SubscribeEvent
  public void handleClientTick(TickEvent.ClientTickEvent event) {
    switch (event.phase) {
      case START:
        this.lastTime = (float) Math.ceil(this.lastTime);
        if (this.gameClient != null) {
          this.gameClient.tick();
        }
        if (this.minecraft.player != null) {
          boolean worldFocused = !this.minecraft.isGamePaused() && this.minecraft.loadingGui == null
              && (this.minecraft.currentScreen == null);

          if (this.minecraft.player.isSpectator()) {
            if (this.minecraft.getRenderViewEntity() instanceof RemoteClientPlayerEntity) {
              this.spectatorRenderer
                  .tick((AbstractClientPlayerEntity) this.minecraft.getRenderViewEntity());
            }
          }

          if (worldFocused && this.gameClient != null) {
            this.gameClient.getShop().ifPresent(shop -> {
              // Consume key event
              while (this.minecraft.gameSettings.keyBindInventory.isPressed()) {
                if (!this.minecraft.player.isSpectator()) {
                  IPlayer<?> player = IPlayer.getExpected(this.minecraft.player);
                  if (shop.getBuyTimeSeconds(player) > 0) {
                    this.minecraft.displayGuiScreen(new ShopScreen(null, shop, player));
                  } else {
                    this.minecraft.ingameGUI.getChatGUI().printChatMessage(
                        GameUtil.formatMessage(Text.translate("message.buy_time_expired")));
                  }
                }
              }
            });

            if (this.gameClient instanceof ITeamGame) {
              while (SWITCH_TEAMS.isPressed()) {
                this.minecraft.displayGuiScreen(new SelectTeamScreen());
              }
            }

            if (this.gameClient.disableSwapHands()) {
              while (this.minecraft.gameSettings.keyBindSwapHands.isPressed());
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
        float currentTime = (float) Math.floor(this.lastTime) + event.renderTickTime;
        this.deltaTime = (currentTime - this.lastTime) * 50;
        this.lastTime = currentTime;
        break;
      default:
        break;
    }
  }

  @SubscribeEvent
  public void handleRenderArmClothing(RenderArmClothingEvent event) {
    if (this.gameClient instanceof AbstractTeamGame) {
      ITeam team = event.getPlayerEntity()
          .getCapability(ModCapabilities.LIVING)
          .<IPlayer<?>>cast()
          .resolve()
          .flatMap(((AbstractTeamGame<?>) this.gameClient)::getPlayerTeam)
          .orElse(null);
      if (team != null) {
        team.getSkin().ifPresent(event::setClothingTexture);
      }
    }
  }
}
