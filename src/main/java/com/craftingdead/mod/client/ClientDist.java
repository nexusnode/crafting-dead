package com.craftingdead.mod.client;

import java.io.IOException;
import java.io.InputStream;
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
import com.craftingdead.mod.client.animation.AnimationManager;
import com.craftingdead.mod.client.animation.GunAnimation;
import com.craftingdead.mod.client.crosshair.CrosshairManager;
import com.craftingdead.mod.client.crosshair.CrosshairProvider;
import com.craftingdead.mod.client.gui.GuiIngame;
import com.craftingdead.mod.client.model.ModelRegistry;
import com.craftingdead.mod.client.renderer.color.BasicColourHandler;
import com.craftingdead.mod.client.renderer.entity.RenderCDZombie;
import com.craftingdead.mod.entity.monster.EntityCDZombie;
import com.craftingdead.mod.event.GunEvent;
import com.craftingdead.mod.init.ModBlocks;
import com.craftingdead.mod.init.ModCapabilities;
import com.craftingdead.mod.item.ExtendedItem;
import com.craftingdead.mod.network.message.client.SetTriggerPressedCMessage;
import com.craftingdead.mod.server.integrated.IntegratedServer;
import com.craftingdead.mod.util.IOUtil;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBiped.ArmPose;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
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

	private CrosshairManager crosshairManager;

	private AnimationManager animationManager;

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

		this.animationManager = new AnimationManager();

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

	public AnimationManager getAnimationManager() {
		return this.animationManager;
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

		private static final Supplier<ClientDist> CLIENT_DIST = () -> CraftingDead.instance().getModDist()
				.getClientDist();

		@SubscribeEvent
		public static void onEvent(TickEvent.PlayerTickEvent event) {
			// Not using instanceof as it is slower
			if (event.player.getClass() == EntityPlayerSP.class) {
				CLIENT_DIST.get().animationManager.update();
			}
		}

		@SubscribeEvent
		public static void onEvent(MouseEvent event) {
			if (CLIENT_DIST.get().minecraft.inGameHasFocus) {
				if (event.getButton() == CLIENT_DIST.get().minecraft.gameSettings.keyBindAttack.getKeyCode() + 100) {
					CraftingDead.NETWORK_WRAPPER.sendToServer(new SetTriggerPressedCMessage(event.isButtonstate()));
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
		public static void onEvent(AttachCapabilitiesEvent<Entity> event) {
			if (event.getObject() instanceof AbstractClientPlayer) {
				event.addCapability(new ResourceLocation(CraftingDead.MOD_ID, "player"), new SerializableProvider<>(
						new ClientPlayer((AbstractClientPlayer) event.getObject()), ModCapabilities.PLAYER));
			}
		}

		@SubscribeEvent
		public static void onEvent(ClientConnectedToServerEvent event) {
			if (CLIENT_DIST.get().minecraft.isIntegratedServerRunning()) {
				DiscordPresence.updateState(GameState.SINGLEPLAYER, CLIENT_DIST.get());
			} else {
				ServerData serverData = CLIENT_DIST.get().getMinecraft().getCurrentServerData();
				DiscordPresence.updateState(serverData.isOnLAN() ? GameState.LAN : GameState.MULTIPLAYER,
						CLIENT_DIST.get());
			}

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
				ConfigManager.sync(CraftingDead.MOD_ID, Config.Type.INSTANCE);
			}
		}

		@SubscribeEvent
		public static void onEvent(ModelRegistryEvent event) {
			ModelRegistry.registerModels(CLIENT_DIST.get());
		}

		@SubscribeEvent
		public static void onEvent(GunEvent.ShootEvent.Pre event) {
			// This is event can be called on both the client thread and server thread so
			// check we are on the client first
			if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
				Supplier<GunAnimation> animation = event.getItem().getAnimations().get(GunAnimation.Type.SHOOT);
				if (animation != null && animation.get() != null) {
					AnimationManager animationManager = CLIENT_DIST.get().animationManager;
					animationManager.clear(event.getItemStack());
					animationManager.setNextGunAnimation(event.getItemStack(), animation.get());
				}
				CLIENT_DIST.get().crosshairManager.addRecoil(event.getItem().getRecoil());
			}
		}

		@SubscribeEvent
		public static void onEvent(GunEvent.ShootEvent.Post event) {
			// This is event can be called on both the client thread and server thread so
			// check we are on the client first
			if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
				switch (event.getRayTrace().typeOfHit) {
				case BLOCK:
					CLIENT_DIST.get().minecraft.effectRenderer.addBlockHitEffects(event.getRayTrace().getBlockPos(),
							event.getRayTrace());
					break;
				default:
					break;
				}
			}
		}
	}
}
