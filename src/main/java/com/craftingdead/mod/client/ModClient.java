package com.craftingdead.mod.client;

import java.util.ArrayList;
import java.util.List;

import com.craftingdead.discordrpc.DiscordEventHandlers;
import com.craftingdead.discordrpc.DiscordRPC;
import com.craftingdead.discordrpc.DiscordRichPresence;
import com.craftingdead.mod.client.gui.GuiCDScreen;
import com.craftingdead.mod.common.core.CDDummyContainer;
import com.craftingdead.mod.common.core.CraftingDead;
import com.craftingdead.mod.common.core.ISidedMod;
import com.craftingdead.mod.common.network.packet.client.CPacketHandshake;
import com.craftingdead.network.mod.client.NetClientHandlerModClient;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.Subscribe;
import com.recastproductions.network.NetworkClient;
import com.recastproductions.network.impl.client.NetworkRegistryClient;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fml.common.FMLContainer;
import net.minecraftforge.fml.common.InjectedModContainer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.MCPDummyContainer;
import net.minecraftforge.fml.common.MinecraftDummyContainer;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Physical client version of the mod
 * 
 * @author Sm0keySa1m0n
 *
 */
@EventBusSubscriber(value = Side.CLIENT, modid = CraftingDead.MOD_ID)
public final class ModClient implements ISidedMod<IntegratedServer> {

	private static final ImmutableList<Class<? extends ModContainer>> AUTHORIZED_MOD_CONTAINERS = new ImmutableList.Builder<Class<? extends ModContainer>>()
			.add(CDDummyContainer.class).add(MinecraftDummyContainer.class).add(FMLContainer.class)
			.add(ForgeModContainer.class).add(MCPDummyContainer.class).build();

	private Minecraft mc;

	private IntegratedServer integratedServer;

	private NetClientHandlerModClient netHandler = new NetClientHandlerModClient(this);
	private NetworkRegistryClient registryClient = new NetworkRegistryClient(netHandler);
	private NetworkClient networkClient = new NetworkClient(registryClient);

	// ================================================================================
	// Constructor
	// ================================================================================

	public ModClient() {
		netHandler = new NetClientHandlerModClient(this);
		registryClient = new NetworkRegistryClient(netHandler);
		networkClient = new NetworkClient(registryClient.getChannelInitializer());
	}

	// ================================================================================
	// Overridden Methods
	// ================================================================================

	@Override
	public void setup(CraftingDead mod) {
		mc = Minecraft.getMinecraft();
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

	// ================================================================================
	// Mod Events
	// ================================================================================

	@Subscribe
	public void preInitializationEvent(FMLPreInitializationEvent event) {
		networkClient.connect(CraftingDead.MANAGEMENT_SERVER_ADDRESS);
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

	// Fixes rendering crash - rendering entities when not inside a world
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

	// ================================================================================
	// Normal Methods
	// ================================================================================

	public CPacketHandshake buildHandshakePacket() {
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
		return new CPacketHandshake(unauthorizedMods.toArray(new String[0]));
	}

	// ================================================================================
	// Getters
	// ================================================================================

	public NetClientHandlerModClient getNetHandler() {
		return this.netHandler;
	}

}
