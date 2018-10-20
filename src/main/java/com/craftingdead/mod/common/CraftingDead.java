package com.craftingdead.mod.common;

import java.io.File;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.craftingdead.mod.client.ClientProxy;
import com.craftingdead.mod.common.multiplayer.LogicalServer;
import com.craftingdead.mod.common.multiplayer.network.NetworkWrapper;
import com.craftingdead.mod.common.registry.generic.MessageRegistry;
import com.craftingdead.mod.common.registry.generic.TileEntityRegistry;
import com.craftingdead.mod.network.ConnectionState;
import com.craftingdead.mod.server.ServerProxy;

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
	 * {@link NettyClient} used for connecting to the master server
	 */
	private NettyClient nettyClient;
	/**
	 * Current {@link Proxy} instance
	 */
	private Proxy proxy;
	/**
	 * The data folder
	 */
	private File modFolder;
	/**
	 * Used for internal networking
	 */
	private NetworkWrapper networkWrapper;
	/**
	 * Used for external networking (with the master server)
	 */
	private NetworkManager networkManager;
	/**
	 * {@link LogicalServer} instance
	 */
	private LogicalServer logicalServer;

	public CraftingDead() {
		instance = this;

		this.modFolder = new File((File) FMLInjectionData.data()[6], CraftingDead.MOD_ID);
		this.modFolder.mkdir();

		LOGGER.info("Loading proxy for {}", FMLLaunchHandler.side());
		this.proxy = this.instantiateSide(ClientProxy::new, ServerProxy::new);

		LOGGER.info("Adding shutdown hook");
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				LOGGER.info("Shutting down");
				CraftingDead.this.proxy.shutdown();
			}
		});
	}

	private Proxy instantiateSide(Supplier<? extends Proxy> clientSide, Supplier<? extends Proxy> serverSide) {
		return FMLLaunchHandler.side() == Side.CLIENT ? clientSide.get() : serverSide.get();
	}

	@Mod.EventHandler
	public void preInitialization(FMLPreInitializationEvent event) {
		LOGGER.info("Processing FMLPreInitializationEvent");

		this.metadata = event.getModMetadata();

		LOGGER.info("Loading network wrapper");
		this.networkWrapper = new NetworkWrapper(MOD_ID, this.proxy);

		LOGGER.info("Registering messages");
		MessageRegistry.registerMessages(this.networkWrapper);
		LOGGER.info("Registering tile entities");
		TileEntityRegistry.registerTileEntities();

		this.proxy.preInitialization(event);
	}

	@Mod.EventHandler
	public void initialization(FMLInitializationEvent event) {
		LOGGER.info("Processing FMLInitializationEvent ");
		this.proxy.initialization(event);
	}

	@Mod.EventHandler
	public void postInitialization(FMLPostInitializationEvent event) {
		LOGGER.info("Processing FMLPostInitializationEvent");
		this.proxy.postInitialization(event);
	}

	@Mod.EventHandler
	public void loadComplete(FMLLoadCompleteEvent event) {
		LOGGER.info("Processing FMLLoadCompleteEvent");
		this.nettyClient = new NettyClient(this.proxy.useEpoll());
		try {
			this.networkManager = this.nettyClient.connect(this.proxy.getMasterServerAddress(),
					ConnectionState.HANDSHAKE, this.proxy::newSession);
		} catch (Exception e) {
			LOGGER.warn("Could not connect to the master server", e);
		}

		this.proxy.loadComplete(event);
	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		LOGGER.info("Processing FMLServerStartingEvent");
		try {
			this.logicalServer = this.proxy.getLogicalServer().newInstance();
			MinecraftForge.EVENT_BUS.register(this.logicalServer);
			this.logicalServer.start(this.proxy, event.getServer());
		} catch (InstantiationException | IllegalAccessException e) {
			LOGGER.error("Could not start logical server", e);
			throw new RuntimeException(e);
		}
	}

	@Mod.EventHandler
	public void serverStopping(FMLServerStoppingEvent event) {
		LOGGER.info("Processing FMLServerStoppingEvent");
		if (this.logicalServer != null) {
			MinecraftForge.EVENT_BUS.unregister(this.logicalServer);
			this.logicalServer = null;
		}
	}

	public File getModFolder() {
		return this.modFolder;
	}

	public NetworkWrapper getNetworkWrapper() {
		return this.networkWrapper;
	}

	@Nullable
	public NetworkManager getNetworkManager() {
		return this.networkManager;
	}

	public ModMetadata getMetadata() {
		return this.metadata;
	}

	public static CraftingDead instance() {
		return instance;
	}

}
