package com.craftingdead.mod.client;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.craftingdead.mod.client.animation.AnimationManager;
import com.craftingdead.mod.client.gui.GuiIngame;
import com.craftingdead.mod.client.gui.GuiScreen;
import com.craftingdead.mod.client.model.ModelManager;
import com.craftingdead.mod.client.multiplayer.IntegratedServer;
import com.craftingdead.mod.client.multiplayer.PlayerSP;
import com.craftingdead.mod.client.network.NetClientHandlerModClient;
import com.craftingdead.mod.client.registry.generic.ColourRegistry;
import com.craftingdead.mod.client.renderer.entity.EntityRenderers;
import com.craftingdead.mod.client.renderer.transition.ScreenTransitionFade;
import com.craftingdead.mod.client.renderer.transition.TransitionManager;
import com.craftingdead.mod.common.CraftingDead;
import com.craftingdead.mod.common.IMod;
import com.craftingdead.mod.common.multiplayer.network.packet.PacketHandshake;
import com.google.common.collect.ImmutableList;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLContainer;
import net.minecraftforge.fml.common.InjectedModContainer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.MCPDummyContainer;
import net.minecraftforge.fml.common.MinecraftDummyContainer;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Physical client version of the mod, handles all client logic
 * 
 * @author Sm0keySa1m0n
 *
 */
public final class ClientMod implements IMod<ClientMod, NetClientHandlerModClient> {

	private static final ImmutableList<Class<? extends ModContainer>> AUTHORIZED_MOD_CONTAINERS = new ImmutableList.Builder<Class<? extends ModContainer>>()
			.add(MinecraftDummyContainer.class).add(FMLContainer.class).add(ForgeModContainer.class)
			.add(MCPDummyContainer.class).build();

	private Minecraft minecraft;

	private NetClientHandlerModClient netHandler = new NetClientHandlerModClient(this);

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
	public NetClientHandlerModClient getNetHandler() {
		return this.netHandler;
	}

	@Override
	public void preInitialization(FMLPreInitializationEvent event) {
		DiscordRPC.discordInitialize("475405055302828034", new DiscordEventHandlers(), true, "");
		this.setDiscordNotInGame();

		minecraft = FMLClientHandler.instance().getClient();

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
		ColourRegistry.registerColourHandlers(this.minecraft.getBlockColors(), this.minecraft.getItemColors());
	}

	@Override
	public void shutdown() {
		DiscordRPC.discordShutdown();
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
			((GuiScreen) event.getGui()).modClient = this;
		}
	}

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		// If the player doesn't exist set the player
		// instance
		if (event.getEntity() instanceof EntityPlayerSP) {
			if (player == null) {
				player = new PlayerSP((EntityPlayerSP) event.getEntity());

				DiscordRichPresence discordRichPresence = new DiscordRichPresence();
				if (event.getWorld().isRemote) {
					discordRichPresence.details = "Playing singleplayer";
				} else {
					discordRichPresence.state = minecraft.getCurrentServerData().serverMOTD;
					String[] playerListSplit = minecraft.getCurrentServerData().playerList.split("/");
					discordRichPresence.partyMax = Integer.valueOf(playerListSplit[0]);
					discordRichPresence.partySize = Integer.valueOf(playerListSplit[1]);
				}
				discordRichPresence.largeImageKey = "craftingdead";
				DiscordRPC.discordUpdatePresence(discordRichPresence);
			}
		}
	}

	@SubscribeEvent
	public void onClientDisconnectionFromServer(WorldEvent.Unload event) {
		// Destroy the player instance as it's no longer needed
		player = null;
		this.setDiscordNotInGame();
	}

	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
		if (event.getType() == ElementType.ALL) {
			guiIngame.renderGameOverlay(event.getResolution(), event.getPartialTicks());
		}
	}

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		animationManager.update();
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
			if (AUTHORIZED_MOD_CONTAINERS.contains(mod.getClass()) || mod.getMod() instanceof CraftingDead) {
				continue;
			}
			CraftingDead.LOGGER.warn("Found unauthorised mod container: " + mod.getName());
			unauthorizedMods.add(mod.getModId());
		}
		return new PacketHandshake(unauthorizedMods.toArray(new String[0]));
	}

	private void setDiscordNotInGame() {
		DiscordRPC.discordUpdatePresence(
				new DiscordRichPresence.Builder("Not in game").setBigImage("craftingdead", "Crafting Dead").build());
	}

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

}
