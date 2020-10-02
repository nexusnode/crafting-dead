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
package com.craftingdead.immerse.client;

import java.io.IOException;
import java.io.InputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.IModDist;
import com.craftingdead.immerse.client.gui.screen.StartScreen;
import com.craftingdead.immerse.client.gui.transition.TransitionManager;
import com.craftingdead.immerse.client.gui.transition.Transitions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.progress.StartupMessageManager;

public class ClientDist implements IModDist {

  public static final ResourceLocation BLUR_SHADER =
      new ResourceLocation(CraftingDeadImmerse.ID, "shaders/post/fade_in_blur.json");

  private static final String DISCORD_CLIENT_ID = "475405055302828034";

  private static final Logger logger = LogManager.getLogger();

  private final Minecraft minecraft;

  private final TransitionManager transitionManager;

  private float lastTime = 0F;

  private float deltaTime;

  public ClientDist() {
    FMLJavaModLoadingContext.get().getModEventBus().register(this);
    MinecraftForge.EVENT_BUS.register(this);
    this.minecraft = Minecraft.getInstance();
    this.transitionManager = new TransitionManager(minecraft, Transitions.GROW);
  }

  public float getDeltaTime() {
    return this.deltaTime;
  }

  // ================================================================================
  // Mod Events
  // ================================================================================

  @SubscribeEvent
  public void handleClientSetup(FMLClientSetupEvent event) {
    // GLFW code needs to run on main thread
    minecraft.enqueue(() -> {
      StartupMessageManager.addModMessage("Applying branding");
      try {
        InputStream smallIcon = minecraft
            .getResourceManager()
            .getResource(
                new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/icons/icon_16x16.png"))
            .getInputStream();
        InputStream mediumIcon = minecraft
            .getResourceManager()
            .getResource(
                new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/icons/icon_32x32.png"))
            .getInputStream();
        minecraft.getMainWindow().setWindowIcon(smallIcon, mediumIcon);
      } catch (IOException e) {
        logger.error("Couldn't set icon", e);
      }
    });
  }

  @SubscribeEvent
  public void handleLoadComplete(FMLLoadCompleteEvent event) {
    StartupMessageManager.addModMessage("Loading Discord integration");
    DiscordPresence.initialize(DISCORD_CLIENT_ID);
    DiscordPresence.updateState(DiscordPresence.GameState.IDLE, this);
  }

  // ================================================================================
  // Forge Events
  // ================================================================================

  @SubscribeEvent
  public void handleClientPlayerLoggedIn(ClientPlayerNetworkEvent.LoggedInEvent event) {
    if (event.getPlayer() == minecraft.player) {
      ClientPlayNetHandler connection = minecraft.getConnection();
      if (connection != null && connection.getNetworkManager().isChannelOpen()) {
        if (minecraft.getIntegratedServer() != null
            && !minecraft.getIntegratedServer().getPublic()) {
          DiscordPresence.updateState(DiscordPresence.GameState.SINGLEPLAYER, this);
        } else if (minecraft.isConnectedToRealms()) {
          DiscordPresence.updateState(DiscordPresence.GameState.REALMS, this);
        } else if (minecraft.getIntegratedServer() == null
            && (minecraft.getCurrentServerData() == null
                || !minecraft.getCurrentServerData().isOnLAN())) {
          DiscordPresence.updateState(DiscordPresence.GameState.MULTIPLAYER, this);
        } else {
          DiscordPresence.updateState(DiscordPresence.GameState.LAN, this);
        }
      }
    }
  }

  @SubscribeEvent
  public void handleClientPlayerLoggedOut(ClientPlayerNetworkEvent.LoggedOutEvent event) {
    DiscordPresence.updateState(DiscordPresence.GameState.IDLE, this);
  }

  @SubscribeEvent
  public void handleGuiOpen(GuiOpenEvent event) {
    if (event.getGui() instanceof MainMenuScreen) {
      event.setGui(new StartScreen());
    }
  }

  @SubscribeEvent
  public void handleDrawScreenPre(DrawScreenEvent.Pre event) {
    event
        .setCanceled(this.transitionManager
            .checkDrawTransition(event.getMouseX(), event.getMouseY(),
                event.getRenderPartialTicks(), event.getGui()));
  }

  @SubscribeEvent
  public void handleClientTick(TickEvent.ClientTickEvent event) {
    switch (event.phase) {
      case START:
        this.lastTime = (float) Math.ceil(this.lastTime);
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
}
