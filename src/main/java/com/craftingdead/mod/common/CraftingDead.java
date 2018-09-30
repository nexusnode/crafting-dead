package com.craftingdead.mod.common;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.craftingdead.mod.client.ClientMod;
import com.craftingdead.mod.common.multiplayer.LogicalServer;
import com.craftingdead.mod.common.multiplayer.network.NetworkWrapper;
import com.craftingdead.mod.common.registry.generic.PacketRegistry;
import com.craftingdead.mod.common.registry.generic.TileEntityRegistry;
import com.craftingdead.mod.server.ServerMod;
import com.recastproductions.network.NetworkClient;
import com.recastproductions.network.impl.client.NetworkRegistryClient;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;

/**
 * The main class for Crafting Dead
 * 
 * @author Sm0keySa1m0n
 *
 */
@Mod(modid = CraftingDead.MOD_ID)
public class CraftingDead<T extends IMod<T, ?>> {

	public static final String MOD_ID = "craftingdead", MOD_VERSION = "0.0.1", MOD_NAME = "Crafting Dead";

	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	/**
	 * Singleton
	 */
	private static CraftingDead<?> instance;
	/**
	 * Current {@link IMod} instance
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
	 * The data folder
	 */
	private File modFolder;
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

		modFolder = new File(FMLClientHandler.instance().getClient().gameDir, CraftingDead.MOD_ID);
		modFolder.mkdir();

		LOGGER.info("Loading {}", FMLLaunchHandler.side());
		sidedMod = instantiateSide(ClientMod::new, ServerMod::new);

		LOGGER.info("Adding shutdown hook");
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				LOGGER.info("Shutting down {}", MOD_NAME);
				networkClient.shutdown();
				sidedMod.shutdown();
			}
		});
	}

	@SuppressWarnings("unchecked")
	private T instantiateSide(Supplier<? extends IMod<?, ?>> clientSide, Supplier<? extends IMod<?, ?>> serverSide) {
		return FMLLaunchHandler.side() == Side.CLIENT ? (T) clientSide.get() : (T) serverSide.get();
	}

	@Mod.EventHandler
	public void preInitialization(FMLPreInitializationEvent event) {
		LOGGER.info("Processing FMLPreInitializationEvent");

		LOGGER.info("Loading network wrapper");
		networkWrapper = new NetworkWrapper(MOD_ID, sidedMod);

		LOGGER.info("Registering packets");
		PacketRegistry.registerPackets(networkWrapper);
		LOGGER.info("Registering tile entities");
		TileEntityRegistry.registerTileEntities();

		sidedMod.preInitialization(event);
	}

	@Mod.EventHandler
	public void initialization(FMLInitializationEvent event) {
		LOGGER.info("Processing FMLInitializationEvent ");
		sidedMod.initialization(event);
	}

	@Mod.EventHandler
	public void postInitialization(FMLPostInitializationEvent event) {
		LOGGER.info("Processing FMLPostInitializationEvent");
		sidedMod.postInitialization(event);
	}

	@Mod.EventHandler
	public void loadComplete(FMLLoadCompleteEvent event) {
		LOGGER.info("Processing FMLLoadCompleteEvent");
		networkRegistryClient = new NetworkRegistryClient(sidedMod.getNetHandler());
		networkClient = new NetworkClient(networkRegistryClient.getChannelInitializer());
		networkClient.connect(managementServerAddress);
	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		LOGGER.info("Processing FMLServerStartingEvent");
		try {
			logicalServer = sidedMod.getLogicalServer().newInstance();
			MinecraftForge.EVENT_BUS.register(logicalServer);
			logicalServer.start(sidedMod, event.getServer());
		} catch (InstantiationException | IllegalAccessException e) {
			LOGGER.error("Could not start logical server", e);
			throw new RuntimeException(e);
		}
	}

	@Mod.EventHandler
	public void serverStopping(FMLServerStoppingEvent event) {
		LOGGER.info("Processing FMLServerStoppingEvent");
		if (logicalServer != null) {
			MinecraftForge.EVENT_BUS.unregister(logicalServer);
			logicalServer = null;
		}
	}

	public File getModFolder() {
		return this.modFolder;
	}

	public NetworkWrapper getNetworkWrapper() {
		return this.networkWrapper;
	}

	public static CraftingDead<?> instance() {
		return instance;
	}

}
