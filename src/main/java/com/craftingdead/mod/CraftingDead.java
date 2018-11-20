package com.craftingdead.mod;

import java.io.File;
import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.craftingdead.mod.capability.triggerable.Triggerable;
import com.craftingdead.mod.client.ClientMod;
import com.craftingdead.mod.init.ModCapabilities;
import com.craftingdead.mod.masterserver.ConnectionState;
import com.craftingdead.mod.network.message.MessageSetTriggerPressed;
import com.craftingdead.mod.network.message.MessageUpdateStatistics;
import com.craftingdead.mod.server.LogicalServer;
import com.craftingdead.mod.server.dedicated.ServerMod;
import com.craftingdead.mod.tileentity.TileEntityLoot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.FMLInjectionData;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;
import sm0keysa1m0n.network.system.NettyClient;
import sm0keysa1m0n.network.wrapper.NetworkManager;

/**
 * The main class for Crafting Dead
 * 
 * @author Sm0keySa1m0n
 *
 */
@Mod(modid = CraftingDead.MOD_ID)
public class CraftingDead {
	/**
	 * Mod ID
	 */
	public static final String MOD_ID = "craftingdead";
	/**
	 * Used for internal networking
	 */
	public static final SimpleNetworkWrapper NETWORK_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);
	/**
	 * {@link Logger} instance
	 */
	private static final Logger LOGGER = LogManager.getLogger();
	/**
	 * Singleton
	 */
	private static CraftingDead instance;
	/**
	 * Mod metadata
	 */
	private ModMetadata metadata;
	/**
	 * {@link NettyClient} used for connecting to the master server
	 */
	private NettyClient nettyClient;
	/**
	 * The {@link SidedMod} instance
	 */
	private SidedMod mod;
	/**
	 * The data folder
	 */
	private File modFolder;
	/**
	 * Used for external networking (with the master server)
	 */
	private NetworkManager networkManager;
	/**
	 * {@link LogicalServer} instance
	 */
	private LogicalServer logicalServer;

	private CraftingDead() {
		instance = this;

		this.modFolder = new File((File) FMLInjectionData.data()[6], CraftingDead.MOD_ID);
		this.modFolder.mkdir();

		this.mod = instantiateSide(() -> ClientMod::new, () -> ServerMod::new);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			LOGGER.info("Shutting down");
			CraftingDead.this.mod.shutdown();
		}));
	}

	// ================================================================================
	// FML Events
	// ================================================================================

	@Mod.EventHandler
	public void preInitialization(FMLPreInitializationEvent event) {
		LOGGER.info("Processing {}", event.description());

		this.metadata = event.getModMetadata();

		MinecraftForge.EVENT_BUS.register(this);

		LOGGER.info("Registering messages");
		this.registerMessages();

		LOGGER.info("Registering tile entities");
		this.registerTileEntities();

		LOGGER.info("Registering capabilities");
		ModCapabilities.registerCapabilities();

		this.mod.preInitialization(event);
	}

	@Mod.EventHandler
	public void initialization(FMLInitializationEvent event) {
		LOGGER.info("Processing {}", event.description());
		this.mod.initialization(event);
	}

	@Mod.EventHandler
	public void postInitialization(FMLPostInitializationEvent event) {
		LOGGER.info("Processing {}", event.description());
		this.mod.postInitialization(event);
	}

	@Mod.EventHandler
	public void loadComplete(FMLLoadCompleteEvent event) {
		LOGGER.info("Processing {}", event.description());
		this.nettyClient = new NettyClient(this.mod.useEpoll());
		try {
			this.networkManager = this.nettyClient.connect(this.mod.getMasterServerAddress(), ConnectionState.HANDSHAKE,
					this.mod::newSession);
		} catch (Exception e) {
			LOGGER.warn("Could not connect to the master server", e);
		}
		this.mod.loadComplete(event);
	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		LOGGER.info("Processing {}", event.description());
		this.logicalServer = this.mod.getLogicalServerSupplier().get();
		MinecraftForge.EVENT_BUS.register(this.logicalServer);
		this.logicalServer.start(event.getServer());
	}

	@Mod.EventHandler
	public void serverStopping(FMLServerStoppingEvent event) {
		LOGGER.info("Processing {}", event.description());
		if (this.logicalServer != null) {
			MinecraftForge.EVENT_BUS.unregister(this.logicalServer);
			this.logicalServer = null;
		}
	}

	@NetworkCheckHandler
	public boolean networkCheck(Map<String, String> mods, Side side) {
		return this.mod.networkCheck(mods, side);
	}

	// ================================================================================
	// Forge Events
	// ================================================================================

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == Phase.END)
			event.player.getCapability(ModCapabilities.PLAYER, null).update();
	}

	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event) {
		if (event.getSource().getTrueSource() instanceof EntityPlayer)
			event.getSource().getTrueSource().getCapability(ModCapabilities.PLAYER, null).onKill(event.getEntity(),
					event.getSource());
	}

	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event) {
		ItemStack itemStack = event.getEntityLiving().getHeldItemMainhand();
		Triggerable triggerable = itemStack.getCapability(ModCapabilities.TRIGGERABLE, null);
		if (triggerable != null)
			triggerable.update(itemStack, event.getEntity());
	}

	// ================================================================================
	// Bootstrap Methods
	// ================================================================================

	private void registerMessages() {
		int discriminator = -1;
		NETWORK_WRAPPER.registerMessage(MessageUpdateStatistics.MessageHandlerUpdateStatistics.class,
				MessageUpdateStatistics.class, discriminator++, Side.CLIENT);
		NETWORK_WRAPPER.registerMessage(MessageSetTriggerPressed.MessageHandlerUpdateTriggerStatus.class,
				MessageSetTriggerPressed.class, discriminator++, Side.SERVER);
	}

	private void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityLoot.class, new ResourceLocation(CraftingDead.MOD_ID, "loot"));
	}

	// ================================================================================
	// Getters
	// ================================================================================

	@Nullable
	public NetworkManager getNetworkManager() {
		return this.networkManager;
	}

	public ModMetadata getMetadata() {
		return this.metadata;
	}

	public SidedMod getMod() {
		return this.mod;
	}

	@Nullable
	public LogicalServer getLogicalServer() {
		return this.logicalServer;
	}

	public File getModFolder() {
		return this.modFolder;
	}

	// ================================================================================
	// Static Methods
	// ================================================================================

	private static SidedMod instantiateSide(Supplier<Supplier<? extends SidedMod>> clientSide,
			Supplier<Supplier<? extends SidedMod>> serverSide) {
		SidedMod mod = FMLLaunchHandler.side() == Side.CLIENT ? clientSide.get().get() : serverSide.get().get();
		LOGGER.info("Loaded {} on {}", mod.getClass().getName(), FMLLaunchHandler.side());
		return mod;
	}

	@Mod.InstanceFactory
	public static CraftingDead instance() {
		return instance != null ? instance : new CraftingDead();
	}

}
