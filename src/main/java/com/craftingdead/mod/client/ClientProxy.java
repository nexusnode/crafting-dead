package com.craftingdead.mod.client;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.lwjgl.opengl.Display;

import com.craftingdead.mod.client.DiscordPresence.GameState;
import com.craftingdead.mod.client.animation.AnimationManager;
import com.craftingdead.mod.client.gui.GuiIngame;
import com.craftingdead.mod.client.gui.GuiScreen;
import com.craftingdead.mod.client.model.ModelManager;
import com.craftingdead.mod.client.multiplayer.IntegratedServer;
import com.craftingdead.mod.client.multiplayer.PlayerSP;
import com.craftingdead.mod.client.registry.generic.ColourHandlerRegistry;
import com.craftingdead.mod.client.registry.generic.EntityRendererRegistry;
import com.craftingdead.mod.client.renderer.transition.ScreenTransitionFade;
import com.craftingdead.mod.client.renderer.transition.TransitionManager;
import com.craftingdead.mod.common.CraftingDead;
import com.craftingdead.mod.common.Proxy;
import com.craftingdead.mod.common.item.ItemGun;
import com.craftingdead.mod.common.item.TriggerableItem;
import com.craftingdead.mod.common.multiplayer.message.MessageTriggerItem;
import com.craftingdead.mod.network.session.PlayerSession;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBiped.ArmPose;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderLivingEvent;
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
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

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
	public Supplier<IntegratedServer> getLogicalServerSupplier() {
		return IntegratedServer::new;
	}

	@Override
	public void preInitialization(FMLPreInitializationEvent event) {
		this.minecraft = FMLClientHandler.instance().getClient();

		Display.setTitle("");

		DiscordPresence.initialize("475405055302828034");
		DiscordPresence.updateState(GameState.PRE_INITIALIZATION, this);

		MinecraftForge.EVENT_BUS.register(this);

		this.guiIngame = new GuiIngame(this);

		this.transitionManager = new TransitionManager(new ScreenTransitionFade(), this.minecraft);
		MinecraftForge.EVENT_BUS.register(this.transitionManager);

		this.modelManager = new ModelManager(this);
		MinecraftForge.EVENT_BUS.register(this.modelManager);

		this.animationManager = new AnimationManager(this);

		EntityRendererRegistry.registerEntityRenderers();
	}

	@Override
	public void initialization(FMLInitializationEvent event) {
		DiscordPresence.updateState(GameState.INITIALIZATION, this);
		ColourHandlerRegistry.registerColourHandlers(this.minecraft.getBlockColors(), this.minecraft.getItemColors());
//		try {
//			patchRenderManager(minecraft.getRenderManager());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
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
			if (this.player == null) {
				this.player = new PlayerSP((EntityPlayerSP) event.getEntity());
				DiscordPresence.updateState(
						this.minecraft.isIntegratedServerRunning() ? GameState.SINGLEPLAYER : GameState.MULTIPLAYER,
						this);
			}
		}

	}

	@SubscribeEvent
	public void onClientDisconnectionFromServer(WorldEvent.Unload event) {
		// Destroy the player instance as it's no longer needed
		this.player = null;
		DiscordPresence.updateState(GameState.IDLE, this);
	}

	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
		if (event.getType() == ElementType.ALL)
			this.guiIngame.renderGameOverlay(event.getResolution(), event.getPartialTicks());
	}

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		if (event.phase == Phase.END)
			this.animationManager.update();
	}

	@SubscribeEvent
	public void onMouseEvent(MouseEvent event) {
		if (this.minecraft.inGameHasFocus) {
			if (event.getButton() - 100 == this.minecraft.gameSettings.keyBindAttack.getKeyCode()
					&& this.player.getVanillaEntity().getHeldItemMainhand().getItem() instanceof TriggerableItem) {
				// Cancel vanilla attack mechanics
				event.setCanceled(true);
				// Tell the server that we are attempting to hold down a trigger on an item
				CraftingDead.NETWORK_WRAPPER.sendToServer(new MessageTriggerItem(!event.isButtonstate()));
			}
		}
	}

	@SubscribeEvent
	public void onRenderLivingPre(RenderLivingEvent.Pre<?> event) {
		// We don't use RenderPlayerEvent.Pre as it gets called too early resulting in
		// any changes we make to the arm pose are overwritten
		if (event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntity();
			ItemStack stack = player.getHeldItemMainhand();
			if (!stack.isEmpty() && stack.getItem() instanceof ItemGun
					&& ((ItemGun) stack.getItem()).useBowAndArrowStance()) {
				ModelPlayer model = (ModelPlayer) event.getRenderer().getMainModel();
				if (player.getPrimaryHand() == EnumHandSide.RIGHT) {
					model.rightArmPose = ArmPose.BOW_AND_ARROW;
				} else {
					model.leftArmPose = ArmPose.BOW_AND_ARROW;
				}
			} else {
				ItemStack stack2 = player.getHeldItemOffhand();
				if (!stack2.isEmpty() && stack2.getItem() instanceof ItemGun
						&& ((ItemGun) stack2.getItem()).useBowAndArrowStance()) {
					ModelPlayer model = (ModelPlayer) event.getRenderer().getMainModel();
					if (player.getPrimaryHand() == EnumHandSide.RIGHT) {
						model.leftArmPose = ArmPose.BOW_AND_ARROW;
					} else {
						model.rightArmPose = ArmPose.BOW_AND_ARROW;
					}
				}
			}
		}
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
