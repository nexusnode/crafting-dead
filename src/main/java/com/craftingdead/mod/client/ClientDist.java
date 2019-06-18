package com.craftingdead.mod.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import com.craftingdead.mod.CommonConfig;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.IModDist;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.capability.SerializableProvider;
import com.craftingdead.mod.capability.player.ClientPlayer;
import com.craftingdead.mod.capability.player.DefaultPlayer;
import com.craftingdead.mod.client.DiscordPresence.GameState;
import com.craftingdead.mod.client.animation.AnimationManager;
import com.craftingdead.mod.client.animation.GunAnimation;
import com.craftingdead.mod.client.crosshair.CrosshairManager;
import com.craftingdead.mod.client.crosshair.CrosshairProvider;
import com.craftingdead.mod.client.gui.GuiIngame;
import com.craftingdead.mod.client.model.ModelRegistry;
import com.craftingdead.mod.event.GunEvent;
import com.craftingdead.mod.item.GunItem;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.BipedModel.ArmPose;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.MavenVersionStringHelper;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

public class ClientDist implements IModDist {

	private static final Logger LOGGER = LogManager.getLogger();

	public static final KeyBinding RELOAD = new KeyBinding("key.reload", GLFW.GLFW_KEY_R, "key.categories.gameplay"),
			KEY_BIND_TOGGLE_FIRE_MODE = new KeyBinding("key.toggle_fire_mode", GLFW.GLFW_KEY_F,
					"key.categories.gameplay");

	private final Minecraft minecraft;

	private final CraftingDead craftingDead;

	@Getter
	private CrosshairManager crosshairManager;

	@Getter
	private AnimationManager animationManager;

	private GuiIngame guiIngame;

	public ClientDist() {
		this.minecraft = Minecraft.getInstance();
		this.craftingDead = CraftingDead.getInstance();
		this.craftingDead.getEventBus().addListener(this::clientSetup);
		this.craftingDead.getEventBus().addListener(this::loadComplete);
	}

	private void clientSetup(FMLClientSetupEvent event) {
		this.crosshairManager = new CrosshairManager();
		((IReloadableResourceManager) this.minecraft.getResourceManager()).func_219534_a(this.crosshairManager);

		this.animationManager = new AnimationManager();

		this.guiIngame = new GuiIngame(this.minecraft, this, CrosshairManager.DEFAULT_CROSSHAIR);

		ClientRegistry.registerKeyBinding(KEY_BIND_TOGGLE_FIRE_MODE);
		ClientRegistry.registerKeyBinding(RELOAD);

		// GLFW code needs to run on main thread
		this.minecraft.enqueue(() -> {
			if (CommonConfig.CLIENT.applyBranding.get()) {
				GLFW.glfwSetWindowTitle(this.minecraft.mainWindow.getHandle(),
						this.craftingDead.getModInfo().getDisplayName() + " " + MavenVersionStringHelper
								.artifactVersionToString(this.craftingDead.getModInfo().getVersion()));
				try {
					InputStream inputstream = this.minecraft.getResourceManager()
							.getResource(new ResourceLocation(CraftingDead.MOD_ID, "textures/gui/icons/icon_16x16.png"))
							.getInputStream();
					InputStream inputstream1 = this.minecraft.getResourceManager()
							.getResource(new ResourceLocation(CraftingDead.MOD_ID, "textures/gui/icons/icon_32x32.png"))
							.getInputStream();
					this.minecraft.mainWindow.func_216529_a(inputstream, inputstream1);
				} catch (IOException e) {
					LOGGER.error("Couldn't set icon", e);
				}
			}
		});
	}

	private void loadComplete(FMLLoadCompleteEvent event) {
		if (CommonConfig.CLIENT.enableDiscordRpc.get())
			DiscordPresence.initialize("475405055302828034");
		DiscordPresence.updateState(GameState.IDLE, this);

	}

	public LazyOptional<ClientPlayer> getPlayer() {
		return this.minecraft.player != null ? this.minecraft.player.getCapability(ModCapabilities.PLAYER, null).cast()
				: LazyOptional.empty();
	}

	// ================================================================================
	// Forge Events
	// ================================================================================

	@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = CraftingDead.MOD_ID)
	public static class Events {

		private static final ClientDist client = (ClientDist) CraftingDead.getInstance().getModDist();
		private static final Minecraft minecraft = Minecraft.getInstance();

		@SubscribeEvent
		public static void handle(InputEvent.MouseInputEvent event) {
			if (minecraft.getConnection() != null && !minecraft.isGamePaused()) {
				if (event.getButton() == minecraft.gameSettings.keyBindAttack.getKey().getKeyCode()) {
					boolean press = event.getAction() == GLFW.GLFW_PRESS;
					client.getPlayer().ifPresent((player) -> {
						player.setTriggerPressed(press);
					});
				}
			}
		}

		@SubscribeEvent
		public static void handle(AttachCapabilitiesEvent<Entity> event) {
			if (event.getObject() instanceof ClientPlayerEntity) {
				event.addCapability(new ResourceLocation(CraftingDead.MOD_ID, "player"), new SerializableProvider<>(
						new ClientPlayer((ClientPlayerEntity) event.getObject()), ModCapabilities.PLAYER));
			} else if (event.getObject() instanceof AbstractClientPlayerEntity) {
				event.addCapability(new ResourceLocation(CraftingDead.MOD_ID, "player"), new SerializableProvider<>(
						new DefaultPlayer<>((AbstractClientPlayerEntity) event.getObject()), ModCapabilities.PLAYER));
			}
		}

		@SubscribeEvent
		public static void handle(EntityJoinWorldEvent event) {
			if (minecraft.isIntegratedServerRunning()) {
				DiscordPresence.updateState(GameState.SINGLEPLAYER, client);
			} else {
				ServerData serverData = minecraft.getCurrentServerData();
				DiscordPresence.updateState(serverData.isOnLAN() ? GameState.LAN : GameState.MULTIPLAYER, client);
			}

		}

		@SubscribeEvent
		public static void handle(GuiOpenEvent event) {
			DiscordPresence.updateState(GameState.IDLE, client);
		}

		@SubscribeEvent
		public static void handle(RenderLivingEvent.Pre<?, BipedModel<?>> event) {
			// We don't use RenderPlayerEvent.Pre as it gets called too early resulting in
			// changes we make to the arm pose being overwritten
			ItemStack stack = event.getEntity().getHeldItemMainhand();
			// isAssignableFrom is faster than instanceof
			if (GunItem.class.isAssignableFrom(stack.getItem().getClass())) {
				BipedModel<?> model = event.getRenderer().func_217764_d();
				switch (event.getEntity().getPrimaryHand()) {
				case LEFT:
					model.field_187075_l = ArmPose.BOW_AND_ARROW;
					break;
				case RIGHT:
					model.field_187076_m = ArmPose.BOW_AND_ARROW;
					break;
				}
			}
		}

		@SubscribeEvent
		public static void handle(RenderGameOverlayEvent.Pre event) {
			switch (event.getType()) {
			case ALL:
				client.guiIngame.renderGameOverlay(event.getPartialTicks());
				break;
			case CROSSHAIRS:
				client.getPlayer().ifPresent((player) -> {
					ItemStack heldStack = player.getEntity().getHeldItemMainhand();
					if (heldStack.getItem() instanceof CrosshairProvider) {
						event.setCanceled(true);
						player.setBaseSpread(((CrosshairProvider) heldStack.getItem()).getDefaultSpread());
						client.guiIngame.renderCrosshairs(player.getSpread(), event.getPartialTicks());
					}
				});
				break;
			default:
				break;
			}
		}

		@SubscribeEvent
		public static void onEvent(ModelRegistryEvent event) {
			ModelRegistry.registerModels(client);
		}

		@SubscribeEvent
		public static void onEvent(GunEvent.ShootEvent.Pre event) {
			// This is event can be called on both the client thread and server thread so
			// check we are on the client first
			if (event.getEntity().world.isRemote()) {
				Supplier<GunAnimation> animation = event.getItem().getAnimations().get(GunAnimation.Type.SHOOT);
				if (animation != null && animation.get() != null) {
					AnimationManager animationManager = client.animationManager;
					animationManager.clear(event.getItemStack());
					animationManager.setNextGunAnimation(event.getItemStack(), animation.get());
				}
				client.getPlayer().ifPresent((player) -> player.queueRecoil());
			}
		}
	}
}
