package com.craftingdead.mod.common.core;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.craftingdead.mod.client.ModClient;
import com.craftingdead.mod.common.network.NetworkWrapper;
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
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.IFMLCallHook;
import net.minecraftforge.fml.relauncher.Side;

/**
 * The main class for Crafting Dead
 * 
 * @author Sm0keySa1m0n
 *
 */
public final class CraftingDead implements IFMLCallHook {

	public static final String MOD_ID = "craftingdead", MOD_VERSION = "0.0.1", MOD_NAME = "Crafting Dead";

	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static final Class<? extends ISidedMod<?, ?>> CLIENT_MOD_CLASS = ModClient.class,
			SERVER_MOD_CLASS = ModServer.class;

	public static final SocketAddress MANAGEMENT_SERVER_ADDRESS = new InetSocketAddress("localhost", 32888);

	/**
	 * Singleton
	 */
	private static CraftingDead instance;

	/**
	 * Current {@link ISidedMod} instance
	 */
	private ISidedMod<?, ?> sidedMod;

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

	public CraftingDead() {
		instance = this;
	}

	/**
	 * Called by FML at early startup
	 */
	@Override
	public Void call() throws Exception {
		sidedMod = FMLLaunchHandler.side() == Side.CLIENT ? CLIENT_MOD_CLASS.newInstance()
				: SERVER_MOD_CLASS.newInstance();

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
		MinecraftForge.EVENT_BUS.register(EntityRegistry.class);
		MinecraftForge.EVENT_BUS.register(WorldRegistry.class);
		MinecraftForge.EVENT_BUS.register(this.sidedMod.getLogicalServer());
	}

	@Subscribe
	public void loadComplete(FMLLoadCompleteEvent event) {
		networkRegistryClient = new NetworkRegistryClient(sidedMod.getNetHandler());
		networkClient = new NetworkClient(networkRegistryClient.getChannelInitializer());
		networkClient.connect(MANAGEMENT_SERVER_ADDRESS);
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

	public static CraftingDead instance() {
		return instance;
	}

}
