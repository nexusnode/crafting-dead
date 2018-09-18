package com.craftingdead.mod.common.core;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;

import com.craftingdead.mod.item.ItemManager;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.craftingdead.mod.client.ModClient;
import com.craftingdead.mod.common.multiplayer.LogicalServer;
import com.craftingdead.mod.common.multiplayer.network.NetworkWrapper;
import com.craftingdead.mod.common.registry.EntityRegistry;
import com.craftingdead.mod.common.registry.PacketRegistry;
import com.craftingdead.mod.common.registry.WorldRegistry;
import com.craftingdead.mod.server.ModServer;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.recastproductions.network.NetworkClient;
import com.recastproductions.network.impl.client.NetworkRegistryClient;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.IFMLCallHook;
import net.minecraftforge.fml.relauncher.Side;

/**
 * The main class for Crafting Dead
 * 
 * @author Sm0keySa1m0n
 *
 */
public final class CraftingDead<T extends ISidedMod<T, ?>> implements IFMLCallHook {

	public static final String MOD_ID = "craftingdead", MOD_VERSION = "0.0.1", MOD_NAME = "Crafting Dead";

	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	private static final Class<? extends ISidedMod<?, ?>> CLIENT_SIDE = ModClient.class, SERVER_SIDE = ModServer.class;

	/**
	 * Singleton
	 */
	private static CraftingDead<?> instance;
	/**
	 * Current {@link ISidedMod} instance
	 */
	private T sidedMod;
	/**
	 * The network address of the management server
	 */
	private SocketAddress managementServerAddress = new InetSocketAddress("localhost", 32888);
	/**
	 * Handles all impl network logic
	 */
	private NetworkRegistryClient networkRegistryClient;
	/**
	 * Used to connect to the master server
	 */
	private NetworkClient networkClient;
	/**
	 * The location of the mod - can be a jar or directory
	 */
	private File modLocation;
	/**
	 * The data folder
	 */
	private File folder;
	/**
	 * Internal event bus
	 */
	private EventBus bus;
	/**
	 * Used for internal networking
	 */
	private NetworkWrapper networkWrapper;
	/**
	 * {@link LogicalServer} instance
	 */
	private LogicalServer<T> logicalServer;

	public CraftingDead() {
		instance = this;
	}

	/**
	 * Called by FML at early startup
	 */
	@Override
	public Void call() throws Exception {
		sidedMod = this.instantiateSide(CLIENT_SIDE, SERVER_SIDE);

		sidedMod.setup(this);

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				LOGGER.info("Shutting down " + MOD_NAME);
				networkClient.shutdown();
				sidedMod.shutdown();
			}
		});

		networkWrapper = new NetworkWrapper(MOD_ID, sidedMod);
		PacketRegistry.registerPackets(networkWrapper);

		return null;
	}

	@SuppressWarnings("unchecked")
	private T instantiateSide(Class<? extends ISidedMod<?, ?>> clientSide, Class<? extends ISidedMod<?, ?>> serverSide)
			throws InstantiationException, IllegalAccessException {
		return FMLLaunchHandler.side() == Side.CLIENT ? (T) clientSide.newInstance() : (T) serverSide.newInstance();
	}

	/**
	 * Injects data provided by FML
	 */
	@Override
	public void injectData(Map<String, Object> data) {
		modLocation = (File) data.get("coremodLocation");
		folder = new File(data.get("mcLocation") + File.separator + CraftingDead.MOD_ID);
		folder.mkdir();
	}

	@Subscribe
	public void preInitialization(FMLPreInitializationEvent event) {
        LOGGER.info("Pre initialization");
        LOGGER.info("################");
        LOGGER.info("################");


        MinecraftForge.EVENT_BUS.register(new RegistrationHandler());
        MinecraftForge.EVENT_BUS.register(EntityRegistry.class);
		MinecraftForge.EVENT_BUS.register(WorldRegistry.class);
	}

	@Mod.EventBusSubscriber
    public class RegistrationHandler {
        @SubscribeEvent
        public void registerItems(RegistryEvent.Register<Item> event) {
            LOGGER.info("################");
            LOGGER.info("################");
            LOGGER.info("################");
            LOGGER.info("register items event");


            ItemManager.register(event.getRegistry());
        }

        @SubscribeEvent
        public void registerItems(ModelRegistryEvent event) {
            LOGGER.info("register model event");
            LOGGER.info("################");
            LOGGER.info("################");
            LOGGER.info("################");
            ItemManager.registerModels();
        }
    }



	@Subscribe
	public void loadComplete(FMLLoadCompleteEvent event) {
		networkRegistryClient = new NetworkRegistryClient(sidedMod.getNetHandler());
		networkClient = new NetworkClient(networkRegistryClient.getChannelInitializer());
		networkClient.connect(managementServerAddress);
	}

	@Subscribe
	public void serverStarting(FMLServerStartingEvent event) {
		try {
			logicalServer = sidedMod.getLogicalServer().newInstance();
			MinecraftForge.EVENT_BUS.register(logicalServer);
			this.bus.register(logicalServer);
			logicalServer.start(sidedMod, event.getServer());
		} catch (InstantiationException | IllegalAccessException e) {
			LOGGER.error("Could not start logical server", e);
			throw new RuntimeException(e);
		}
	}

	@Subscribe
	public void serverStopping(FMLServerStoppingEvent event) {
		if (logicalServer != null) {
			MinecraftForge.EVENT_BUS.unregister(logicalServer);
			this.bus.unregister(logicalServer);
			logicalServer = null;
		}
	}

	/**
	 * Forwarded to this class by the mod container
	 * 
	 * @param bus        - the {@link EventBus} instance
	 * @param controller - the {@link LoadController} instance
	 * @return
	 */
	boolean registerBus(EventBus bus, LoadController controller) {
		this.bus = bus;
		bus.register(this);
		bus.register(sidedMod);
		bus.register(sidedMod.getLogicalServer());
		return true;
	}

	public File getModLocation() {
		return this.modLocation;
	}

	public File getFolder() {
		return this.folder;
	}

	public EventBus getEventBus() {
		return this.bus;
	}

	public NetworkWrapper getNetworkWrapper() {
		return this.networkWrapper;
	}

	public static CraftingDead<?> instance() {
		return instance;
	}

}
