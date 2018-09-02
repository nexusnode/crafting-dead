package com.craftingdead.mod.client;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.craftingdead.discordrpc.DiscordEventHandlers;
import com.craftingdead.discordrpc.DiscordRPC;
import com.craftingdead.discordrpc.DiscordRichPresence;
import com.craftingdead.mod.client.gui.GuiCDScreen;
import com.craftingdead.mod.client.gui.GuiIngame;
import com.craftingdead.mod.client.renderer.RenderCDZombie;
import com.craftingdead.mod.common.core.CDDummyContainer;
import com.craftingdead.mod.common.core.CraftingDead;
import com.craftingdead.mod.common.core.ISidedMod;
import com.craftingdead.mod.common.entity.monster.EntityCDZombie;
import com.craftingdead.mod.common.network.packet.PacketHandshake;
import com.craftingdead.network.mod.client.NetClientHandlerModClient;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.Subscribe;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLContainer;
import net.minecraftforge.fml.common.InjectedModContainer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.MCPDummyContainer;
import net.minecraftforge.fml.common.MinecraftDummyContainer;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;

/**
 * Physical client version of the mod, handles all client logic
 * 
 * @author Sm0keySa1m0n
 *
 */
public final class ModClient implements ISidedMod<IntegratedServer, NetClientHandlerModClient> {

	private static final ImmutableList<Class<? extends ModContainer>> AUTHORIZED_MOD_CONTAINERS = new ImmutableList.Builder<Class<? extends ModContainer>>()
			.add(CDDummyContainer.class).add(MinecraftDummyContainer.class).add(FMLContainer.class)
			.add(ForgeModContainer.class).add(MCPDummyContainer.class).build();

	private Minecraft mc;

	private IntegratedServer integratedServer;

	private NetClientHandlerModClient netHandler = new NetClientHandlerModClient(this);

	private GuiIngame guiIngame;

	private PlayerSP player;

	// ================================================================================
	// Overridden Methods
	// ================================================================================

	@Override
	public void setup(CraftingDead mod) {
		integratedServer = new IntegratedServer();

		DiscordRPC discordRPC = DiscordRPC.INSTANCE;

		DiscordEventHandlers handlers = new DiscordEventHandlers();
		discordRPC.discordInitialize("484121003589500929", handlers, true, null);

		// DiscordRPC Test
		DiscordRichPresence discordRichPresence = new DiscordRichPresence();
		discordRichPresence.details = "Playing Mod Server";
		discordRichPresence.state = "Atlanta US";
		discordRichPresence.largeImageKey = "logo-512";
		discordRichPresence.partyMax = 200;
		discordRichPresence.partySize = 13;
		discordRPC.discordUpdatePresence(discordRichPresence);
	}

	@Override
	public IntegratedServer getLogicalServer() {
		return integratedServer;
	}

	@Override
	public void shutdown() {
		DiscordRPC.INSTANCE.discordShutdown();
	}

	@Override
	public NetClientHandlerModClient getNetHandler() {
		return this.netHandler;
	}

	@Subscribe
	public void preInitialization(FMLPreInitializationEvent event) {
		mc = FMLClientHandler.instance().getClient();
		MinecraftForge.EVENT_BUS.register(this);
		guiIngame = new GuiIngame(this);

		RenderingRegistry.registerEntityRenderingHandler(EntityCDZombie.class, new IRenderFactory<EntityCDZombie>() {

			@Override
			public Render<? super EntityCDZombie> createRenderFor(RenderManager manager) {
				return new RenderCDZombie(manager, new ModelBiped(), 0.4F);
			}

		});
	}

	@Subscribe
	public void posInitialization(FMLPostInitializationEvent event) {

	}

	// ================================================================================
	// Forge Events
	// ================================================================================

	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event) {
		if (event.getGui() instanceof net.minecraft.client.gui.GuiMainMenu) {
			// event.setGui(new GuiMainMenu());
		}
		if (event.getGui() instanceof GuiCDScreen) {
			((GuiCDScreen) event.getGui()).modClient = this;
		}
	}

	// Fixes rendering crash with rendering entities when not inside a world
	@SubscribeEvent
	public void onPreRenderEntitySpecials(RenderLivingEvent.Specials.Pre<?> event) {
		if (mc.player == null)
			event.setCanceled(true);
	}

	@SubscribeEvent
	public void onPreTooltipRender(RenderTooltipEvent.Color event) {
		event.setBackground(0xFF101010);
		event.setBorderEnd(0);
		event.setBorderStart(0);
	}

	@SubscribeEvent
	public void onClientConnectedToServer(ClientConnectedToServerEvent event) {
		// If the player doesn't exist (first time connecting to server) set the player
		// instance
		if (player == null) {
			player = new PlayerSP(mc.player);
		}
	}

	@SubscribeEvent
	public void onClientDisconnectionFromServer(ClientDisconnectionFromServerEvent event) {
		// Destroy the player instance as it's no longer needed
		player = null;
	}

	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
		if (event.getType() == ElementType.ALL) {
			guiIngame.renderGameOverlay(event.getResolution(), event.getPartialTicks());
		}
	}

	// ================================================================================
	// Normal Methods
	// ================================================================================

	public PacketHandshake buildHandshakePacket() {
		List<String> unauthorizedMods = new ArrayList<String>();
		for (ModContainer mod : Loader.instance().getModList()) {
			if (mod instanceof InjectedModContainer) {
				InjectedModContainer injectedMod = (InjectedModContainer) mod;
				mod = injectedMod.wrappedContainer;
			}
			if (AUTHORIZED_MOD_CONTAINERS.contains(mod.getClass())) {
				continue;
			}
			CraftingDead.LOGGER.warn("Found unauthorised mod container: " + mod.getName());
			unauthorizedMods.add(mod.getModId());
		}
		return new PacketHandshake(unauthorizedMods.toArray(new String[0]));
	}

	public Minecraft getMinecraft() {
		return this.mc;
	}

	@Nullable
	public PlayerSP getPlayer() {
		return this.player;
	}

}
