package com.craftingdead.mod.client;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.opengl.Display;

import com.craftingdead.mod.client.DiscordPresence.GameState;
import com.craftingdead.mod.client.animation.AnimationManager;
import com.craftingdead.mod.client.gui.GuiIngame;
import com.craftingdead.mod.client.gui.GuiScreen;
import com.craftingdead.mod.client.model.ModelManager;
import com.craftingdead.mod.client.multiplayer.IntegratedServer;
import com.craftingdead.mod.client.multiplayer.PlayerSP;
import com.craftingdead.mod.client.registry.generic.ColourRegistry;
import com.craftingdead.mod.client.renderer.entity.EntityRenderers;
import com.craftingdead.mod.client.renderer.transition.ScreenTransitionFade;
import com.craftingdead.mod.client.renderer.transition.TransitionManager;
import com.craftingdead.mod.common.Proxy;
import com.craftingdead.mod.network.session.PlayerSession;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Physical client version of the mod, handles all client logic
 * 
 * @author Sm0keySa1m0n
 *
 */
public final class ClientProxy implements Proxy {

	private Minecraft minecraft;

	private List<String> news = new ArrayList<String>();

	private GuiIngame guiIngame;

	private PlayerSP player;

	private TransitionManager transitionManager;

	private ModelManager modelManager;

	private AnimationManager animationManager;

	// ================================================================================
	// Overridden Methods
	// ================================================================================

	@Override
	public Class<IntegratedServer> getLogicalServer() {
		return IntegratedServer.class;
	}

	@Override
	public void preInitialization(FMLPreInitializationEvent event) {
		minecraft = FMLClientHandler.instance().getClient();

		Display.setTitle("");

		DiscordPresence.initialize("475405055302828034");
		DiscordPresence.updateState(GameState.PRE_INITIALIZATION, this);

		MinecraftForge.EVENT_BUS.register(this);

		guiIngame = new GuiIngame(this);

		transitionManager = new TransitionManager(new ScreenTransitionFade(), minecraft);
		MinecraftForge.EVENT_BUS.register(transitionManager);

		modelManager = new ModelManager(this);
		MinecraftForge.EVENT_BUS.register(modelManager);

		animationManager = new AnimationManager(this);

		EntityRenderers.registerRenderers();
	}

	@Override
	public void initialization(FMLInitializationEvent event) {
		DiscordPresence.updateState(GameState.INITIALIZATION, this);
		ColourRegistry.registerColourHandlers(this.minecraft.getBlockColors(), this.minecraft.getItemColors());
	}

	@Override
	public void postInitialization(FMLPostInitializationEvent event) {
		DiscordPresence.updateState(GameState.POST_INITIALIZATION, this);
	}

	@Override
	public void loadComplete(FMLLoadCompleteEvent event) {
		DiscordPresence.updateState(GameState.IDLE, this);
	}

	@Override
	public void shutdown() {
		DiscordPresence.shutdown();
	}

	@Override
	public InetSocketAddress getMasterServerAddress() {
		return new InetSocketAddress("localhost", 32888);
	}

	@Override
	public boolean useEpoll() {
		return this.minecraft.gameSettings.isUsingNativeTransport();
	}

	@Override
	public PlayerSession newSession() {
		return new PlayerSession(this);
	}

	// ================================================================================
	// Forge Events
	// ================================================================================

	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event) {
		if (event.getGui() instanceof net.minecraft.client.gui.GuiMainMenu) {
//			event.setGui(new GuiMainMenu());
		}
		if (event.getGui() instanceof GuiScreen) {
			((GuiScreen) event.getGui()).client = this;
		}
	}

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		// If the player doesn't exist set the player
		// instance
		if (event.getEntity() instanceof EntityPlayerSP) {
			if (player == null) {
				player = new PlayerSP((EntityPlayerSP) event.getEntity());
				DiscordPresence.updateState(
						minecraft.isIntegratedServerRunning() ? GameState.SINGLEPLAYER : GameState.MULTIPLAYER, this);
			}
		}

	}

	@SubscribeEvent
	public void onClientDisconnectionFromServer(WorldEvent.Unload event) {
		// Destroy the player instance as it's no longer needed
		player = null;
		DiscordPresence.updateState(GameState.IDLE, this);
	}

	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
		if (event.getType() == ElementType.ALL)
			guiIngame.renderGameOverlay(event.getResolution(), event.getPartialTicks());
	}

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		animationManager.update();
	}

	// ================================================================================
	// Getters
	// ================================================================================

	public Minecraft getMinecraft() {
		return this.minecraft;
	}

	@Nullable
	public PlayerSP getPlayer() {
		return this.player;
	}

	public AnimationManager getAnimationManager() {
		return this.animationManager;
	}

	public List<String> getNews() {
		return this.news;
	}

}
