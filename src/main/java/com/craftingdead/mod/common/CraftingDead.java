package com.craftingdead.mod.common;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.craftingdead.mod.client.ModClient;
import com.craftingdead.mod.common.multiplayer.LogicalServer;
import com.craftingdead.mod.common.multiplayer.network.NetworkWrapper;
import com.craftingdead.mod.common.registry.generic.PacketRegistry;
import com.craftingdead.mod.common.registry.generic.TileEntityRegistry;
import com.craftingdead.mod.server.ModServer;
import com.recastproductions.network.NetworkClient;
import com.recastproductions.network.impl.client.NetworkRegistryClient;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.relauncher.FMLInjectionData;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;

/**
 * The main class for Crafting Dead
 * 
 * @author Sm0keySa1m0n
 *
 */
@Mod(modid = CraftingDead.MOD_ID)
public class CraftingDead {

	public static final String MOD_ID = "craftingdead";

	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	/**
	 * Singleton
	 */
	private static CraftingDead instance;
	/**
	 * Mod metadata
	 */
	private ModMetadata metadata;
	/**
	 * Current {@link IMod} instance
	 */
	private IMod<?> sidedMod;
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
	private LogicalServer logicalServer;

	public CraftingDead() {
		instance = this;

		modFolder = new File((File) FMLInjectionData.data()[6], CraftingDead.MOD_ID);
		modFolder.mkdir();

		LOGGER.info("Loading {}", FMLLaunchHandler.side());
		sidedMod = instantiateSide(ModClient::new, ModServer::new);

		LOGGER.info("Adding shutdown hook");
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				LOGGER.info("Shutting down");
				networkClient.shutdown();
				sidedMod.shutdown();
			}
		});
	}

	private IMod<?> instantiateSide(Supplier<? extends IMod<?>> clientSide, Supplier<? extends IMod<?>> serverSide) {
		return FMLLaunchHandler.side() == Side.CLIENT ? clientSide.get() : serverSide.get();
	}

	@Mod.EventHandler
	public void preInitialization(FMLPreInitializationEvent event) {
		LOGGER.info("Processing FMLPreInitializationEvent");

		metadata = event.getModMetadata();

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
		sidedMod.loadComplete(event);
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

	public ModMetadata getMetadata() {
		return this.metadata;
	}

	public static CraftingDead instance() {
		return instance;
	}

}
