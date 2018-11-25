package com.craftingdead.mod.client;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.SidedMod;
import com.craftingdead.mod.block.BlockLoot;
import com.craftingdead.mod.capability.SerializableProvider;
import com.craftingdead.mod.capability.player.ClientPlayerLocal;
import com.craftingdead.mod.capability.player.ClientPlayerOther;
import com.craftingdead.mod.capability.player.Player;
import com.craftingdead.mod.client.DiscordPresence.GameState;
import com.craftingdead.mod.client.gui.ExtendedGuiScreen;
import com.craftingdead.mod.client.gui.ingame.GuiIngame;
import com.craftingdead.mod.client.model.ModelManager;
import com.craftingdead.mod.client.renderer.color.BasicColourHandler;
import com.craftingdead.mod.client.renderer.entity.RenderCDZombie;
import com.craftingdead.mod.client.transition.TransitionManager;
import com.craftingdead.mod.client.transition.Transitions;
import com.craftingdead.mod.entity.monster.EntityCDZombie;
import com.craftingdead.mod.init.ModBlocks;
import com.craftingdead.mod.init.ModCapabilities;
import com.craftingdead.mod.item.ExtendedItem;
import com.craftingdead.mod.masterserver.session.PlayerSession;
import com.craftingdead.mod.server.integrated.IntegratedServer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBiped.ArmPose;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
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
public final class ClientMod implements SidedMod {

	private static final Logger LOGGER = LogManager.getLogger();

	private Minecraft minecraft;

	private List<String> news = new ArrayList<String>();

	private GuiIngame guiIngame;

	private TransitionManager transitionManager;

	private ModelManager modelManager;

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

		DiscordPresence.initialize("475405055302828034");
		DiscordPresence.updateState(GameState.PRE_INITIALIZATION, this);

		MinecraftForge.EVENT_BUS.register(this);

		this.guiIngame = new GuiIngame(this);
		MinecraftForge.EVENT_BUS.register(this.guiIngame);

		try {
			this.transitionManager = new TransitionManager(this.minecraft, Transitions.FADE);
			MinecraftForge.EVENT_BUS.register(this.transitionManager);
		} catch (RuntimeException e) {
			LOGGER.warn("An error occurred while enabling transitions; transitions will be disabled", e);
		}

		this.modelManager = new ModelManager(this);
		MinecraftForge.EVENT_BUS.register(this.modelManager);

		this.registerEntityRenderers();
	}

	@Override
	public void initialization(FMLInitializationEvent event) {
		DiscordPresence.updateState(GameState.INITIALIZATION, this);
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
	// Forge Events
	// ================================================================================

	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event) {
//		if (event.getGui() instanceof net.minecraft.client.gui.GuiMainMenu) {
//			event.setGui(new GuiMainMenu());
//		}
		if (event.getGui() instanceof ExtendedGuiScreen) {
			((ExtendedGuiScreen) event.getGui()).client = this;
		}
	}

	@SubscribeEvent
	public void onAttachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof AbstractClientPlayer) {
			Player<? extends AbstractClientPlayer> player = null;
			if (event.getObject() instanceof EntityPlayerSP) {
				player = new ClientPlayerLocal((EntityPlayerSP) event.getObject());
			} else if (event.getObject() instanceof EntityOtherPlayerMP) {
				player = new ClientPlayerOther((EntityOtherPlayerMP) event.getObject());
			}
			event.addCapability(new ResourceLocation(CraftingDead.MOD_ID, "player"),
					new SerializableProvider<>(player, ModCapabilities.PLAYER));
		}
	}

	@SubscribeEvent
	public void onClientConnectedToServer(ClientConnectedToServerEvent event) {
		DiscordPresence.updateState(
				this.minecraft.isIntegratedServerRunning() ? GameState.SINGLEPLAYER : GameState.MULTIPLAYER, this);
	}

	@SubscribeEvent
	public void onClientDisconnectionFromServer(ClientDisconnectionFromServerEvent event) {
		// Destroy the player instance as it's no longer needed
		DiscordPresence.updateState(GameState.IDLE, this);
	}

	@SubscribeEvent
	public void onMouseEvent(MouseEvent event) {
		int bindingCode = event.getButton() - 100;
		if (this.minecraft.inGameHasFocus) {
			if (bindingCode == this.minecraft.gameSettings.keyBindAttack.getKeyCode()) {
				this.getPlayer().setTriggerPressed(event.isButtonstate());
				if (this.getPlayer().getEntity().getHeldItemMainhand().getItem() instanceof ExtendedItem
						&& ((ExtendedItem) this.getPlayer().getEntity().getHeldItemMainhand().getItem())
								.getCancelVanillaAttack())
					event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onRenderLivingPre(RenderLivingEvent.Pre<?> event) {
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

	@Nullable
	public ClientPlayerLocal getPlayer() {
		return this.minecraft.player != null
				? (ClientPlayerLocal) this.minecraft.player.getCapability(ModCapabilities.PLAYER, null)
				: null;
	}

	public List<String> getNews() {
		return this.news;
	}

}
