package com.craftingdead.mod.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.ModConfig;
import com.craftingdead.mod.ModDist;
import com.craftingdead.mod.block.BlockLoot;
import com.craftingdead.mod.capability.SerializableProvider;
import com.craftingdead.mod.capability.player.ClientPlayer;
import com.craftingdead.mod.client.DiscordPresence.GameState;
import com.craftingdead.mod.client.crosshair.CrosshairManager;
import com.craftingdead.mod.client.crosshair.CrosshairProvider;
import com.craftingdead.mod.client.gui.ExtendedGuiScreen;
import com.craftingdead.mod.client.gui.GuiIngame;
import com.craftingdead.mod.client.model.ModelRegistry;
import com.craftingdead.mod.client.renderer.color.BasicColourHandler;
import com.craftingdead.mod.client.renderer.entity.RenderCDZombie;
import com.craftingdead.mod.client.transition.TransitionManager;
import com.craftingdead.mod.client.transition.Transitions;
import com.craftingdead.mod.entity.monster.EntityCDZombie;
import com.craftingdead.mod.event.BulletCollisionEvent;
import com.craftingdead.mod.init.ModBlocks;
import com.craftingdead.mod.init.ModCapabilities;
import com.craftingdead.mod.item.ExtendedItem;
import com.craftingdead.mod.masterserver.session.PlayerSession;
import com.craftingdead.mod.network.message.MessageSetTriggerPressed;
import com.craftingdead.mod.server.integrated.IntegratedServer;
import com.craftingdead.mod.util.IOUtil;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBiped.ArmPose;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Physical client version of the mod, handles all client logic
 * 
 * @author Sm0keySa1m0n
 *
 */
public final class ClientDist implements ModDist {

	private static final Logger LOGGER = LogManager.getLogger();

	private static final ResourceLocation[] ICONS = new ResourceLocation[] {
			new ResourceLocation(CraftingDead.MOD_ID, "textures/gui/icons/icon_16x16.png"),
			new ResourceLocation(CraftingDead.MOD_ID, "textures/gui/icons/icon_32x32.png") };

	public static final KeyBinding KEY_BIND_TOGGLE_FIRE_MODE = new KeyBinding("key.toggle_fire_mode", Keyboard.KEY_F,
			"key.categories.gameplay");

	private Minecraft minecraft;

	private ModMetadata modMetadata;

	private List<String> news = new ArrayList<String>();

	private GuiIngame guiIngame;

	private TransitionManager transitionManager;

	private CrosshairManager crosshairManager;

	// ================================================================================
	// Overridden Methods
	// ================================================================================

	@Override
	public Supplier<IntegratedServer> getLogicalServerSupplier() {
		return IntegratedServer::new;
	}

	@Override
	public void preInitialization(FMLPreInitializationEvent event) {
		this.minecraft = FMLClientHandler.instance().getClient();
		this.modMetadata = event.getModMetadata();

		if (ModConfig.client.enableDiscordRpc)
			DiscordPresence.initialize("475405055302828034");
		DiscordPresence.updateState(GameState.PRE_INITIALIZATION, this);

		if (ModConfig.client.applyBranding) {
			Display.setTitle(modMetadata.name + " " + modMetadata.version);
			List<ByteBuffer> iconBuffers = Lists.newArrayList();
			for (ResourceLocation icon : ICONS) {
				try (InputStream input = this.minecraft.getResourceManager().getResource(icon).getInputStream()) {
					iconBuffers.add(IOUtil.readImageToBuffer(input));
				} catch (IOException e) {
					LOGGER.catching(e);
				}
			}
			Display.setIcon(iconBuffers.toArray(new ByteBuffer[0]));
		}

		this.guiIngame = new GuiIngame(this);

		this.crosshairManager = new CrosshairManager(this);
		((IReloadableResourceManager) this.minecraft.getResourceManager())
				.registerReloadListener(this.crosshairManager);

		this.transitionManager = new TransitionManager(this.minecraft, Transitions.FADE_GROW);

		ClientRegistry.registerKeyBinding(KEY_BIND_TOGGLE_FIRE_MODE);

		this.registerEntityRenderers();
	}

	@Override
	public void initialization(FMLInitializationEvent event) {
		DiscordPresence.updateState(GameState.INITIALIZATION, this);
		ConfigManager.sync(CraftingDead.MOD_ID, Type.INSTANCE);
		this.registerColourHandlers();
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
	// Bootstrap Methods
	// ================================================================================

	private void registerEntityRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(EntityCDZombie.class, new IRenderFactory<EntityCDZombie>() {

			@Override
			public Render<? super EntityCDZombie> createRenderFor(RenderManager manager) {
				return new RenderCDZombie(manager, new ModelBiped(), 0.4F);
			}

		});
	}

	private void registerColourHandlers() {
		this.registerLootColourHandler((BlockLoot) ModBlocks.RESIDENTIAL_LOOT);
	}

	private void registerLootColourHandler(BlockLoot loot) {
		this.minecraft.getBlockColors().registerBlockColorHandler(new BasicColourHandler(loot.getColour()), loot);
		this.minecraft.getItemColors().registerItemColorHandler(new BasicColourHandler(loot.getColour()), loot);
	}

	// ================================================================================
	// Getters
	// ================================================================================

	public Minecraft getMinecraft() {
		return this.minecraft;
	}

	public CrosshairManager getCrosshairManager() {
		return this.crosshairManager;
	}

	@Nullable
	public ClientPlayer getPlayer() {
		return this.minecraft.player != null
				? (ClientPlayer) this.minecraft.player.getCapability(ModCapabilities.PLAYER, null)
				: null;
	}

	public List<String> getNews() {
		return this.news;
	}

	// ================================================================================
	// Forge Events
	// ================================================================================

	@Mod.EventBusSubscriber(value = Side.CLIENT, modid = CraftingDead.MOD_ID)
	public static class Events {

		private static final Supplier<ClientDist> CLIENT_DIST = () -> CraftingDead.instance().getMod().getClientDist();

		@SubscribeEvent
		public static void onEvent(GuiScreenEvent.DrawScreenEvent.Pre event) {
			event.setCanceled(CLIENT_DIST.get().transitionManager.checkDrawTransition(event.getMouseX(),
					event.getMouseY(), event.getRenderPartialTicks(), event.getGui()));
		}

		@SubscribeEvent
		public static void onEvent(MouseEvent event) {
			if (CLIENT_DIST.get().minecraft.inGameHasFocus) {
				if (event.getButton() == CLIENT_DIST.get().minecraft.gameSettings.keyBindAttack.getKeyCode() + 100) {
					CraftingDead.NETWORK_WRAPPER.sendToServer(new MessageSetTriggerPressed(event.isButtonstate()));
					CLIENT_DIST.get().getPlayer().setTriggerPressed(event.isButtonstate());
					if (CLIENT_DIST.get().getPlayer().getEntity().getHeldItemMainhand()
							.getItem() instanceof ExtendedItem
							&& ((ExtendedItem) CLIENT_DIST.get().getPlayer().getEntity().getHeldItemMainhand()
									.getItem()).getCancelVanillaAttack())
						event.setCanceled(true);
				}
			}
		}

		@SubscribeEvent
		public static void onEvent(GuiOpenEvent event) {
//			if (event.getGui() instanceof net.minecraft.client.gui.GuiMainMenu) {
//				event.setGui(new GuiMainMenu());
//			}
			if (event.getGui() instanceof ExtendedGuiScreen) {
				((ExtendedGuiScreen) event.getGui()).client = CLIENT_DIST.get();
			}
		}

		@SubscribeEvent
		public static void onEvent(AttachCapabilitiesEvent<Entity> event) {
			if (event.getObject() instanceof AbstractClientPlayer) {
				event.addCapability(new ResourceLocation(CraftingDead.MOD_ID, "player"), new SerializableProvider<>(
						new ClientPlayer((AbstractClientPlayer) event.getObject()), ModCapabilities.PLAYER));
			}
		}

		@SubscribeEvent
		public static void onEvent(ClientConnectedToServerEvent event) {
			DiscordPresence.updateState(CLIENT_DIST.get().minecraft.isIntegratedServerRunning() ? GameState.SINGLEPLAYER
					: GameState.MULTIPLAYER, CLIENT_DIST.get());
		}

		@SubscribeEvent
		public static void onEvent(ClientDisconnectionFromServerEvent event) {
			// Destroy the player instance as it's no longer needed
			DiscordPresence.updateState(GameState.IDLE, CLIENT_DIST.get());
		}

		@SubscribeEvent
		public static void onEvent(RenderLivingEvent.Pre<?> event) {
			// We don't use RenderPlayerEvent.Pre as it gets called too early resulting in
			// changes we make to the arm pose being overwritten
			ItemStack stack = event.getEntity().getHeldItemMainhand();
			if (stack.getItem() instanceof ExtendedItem) {
				ExtendedItem item = (ExtendedItem) stack.getItem();
				if (item.getBowAndArrowPose() && event.getRenderer().getMainModel() instanceof ModelBiped) {
					ModelBiped model = (ModelBiped) event.getRenderer().getMainModel();
					switch (event.getEntity().getPrimaryHand()) {
					case LEFT:
						model.leftArmPose = ArmPose.BOW_AND_ARROW;
						break;
					case RIGHT:
						model.rightArmPose = ArmPose.BOW_AND_ARROW;
						break;
					}
				}
			}
		}

		@SubscribeEvent
		public static void onEvent(RenderGameOverlayEvent.Pre event) {
			switch (event.getType()) {
			case ALL:
				CLIENT_DIST.get().guiIngame.renderGameOverlay(event.getResolution(), event.getPartialTicks());
				break;
			case CROSSHAIRS:
				ItemStack heldStack = CLIENT_DIST.get().getPlayer().getEntity().getHeldItemMainhand();
				if (heldStack.getItem() instanceof CrosshairProvider) {
					event.setCanceled(true);
					CLIENT_DIST.get().crosshairManager.updateAndDrawCrosshairs(event.getResolution(),
							event.getPartialTicks(), (CrosshairProvider) heldStack.getItem());
				}
				break;
			default:
				break;
			}
		}

		@SubscribeEvent
		public static void onEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(CraftingDead.MOD_ID)) {
				ConfigManager.sync(CraftingDead.MOD_ID, Type.INSTANCE);
			}
		}

		@SubscribeEvent
		public static void onEvent(ModelRegistryEvent event) {
			ModelRegistry.registerModels(CLIENT_DIST.get());
		}

		@SubscribeEvent
		public static void onEvent(BulletCollisionEvent.HitBlock.Post event) {
			CLIENT_DIST.get().minecraft.effectRenderer.addBlockHitEffects(event.getRayTrace().getBlockPos(),
					event.getRayTrace());
		}

	}

}
