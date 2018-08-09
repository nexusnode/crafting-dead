package com.craftingdead.mod.core;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.craftingdead.mod.LogicalServer;
import com.craftingdead.mod.client.ModClient;
import com.craftingdead.mod.server.ModServer;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.IFMLCallHook;
import net.minecraftforge.fml.relauncher.Side;

public final class CraftingDead implements IFMLCallHook {

	public static final String MOD_ID = "craftingdead", MOD_VERSION = "0.0.1", MOD_NAME = "Crafting Dead";

	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static final Class<? extends ISidedMod<?>> CLIENT_MOD_CLASS = ModClient.class, SERVER_MOD_CLASS = ModServer.class;
	
	public static final SocketAddress MANAGEMENT_SERVER_ADDRESS = new InetSocketAddress("localhost", 32888);

	private static CraftingDead instance;
	
	public static final SimpleNetworkWrapper NETWORK_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);
	
	private ISidedMod<?> sidedMod;

	private File dataDir;

	private EventBus bus;

	public CraftingDead() {
		instance = this;
	}

	@Override
	public Void call() throws Exception {
		sidedMod = FMLLaunchHandler.side() == Side.CLIENT ? CLIENT_MOD_CLASS.newInstance()
				: SERVER_MOD_CLASS.newInstance();
		sidedMod.setup(this);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				sidedMod.onShutdown();
			}
		});
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		dataDir = new File(data.get("mcLocation") + File.separator + CraftingDead.MOD_ID);
		dataDir.mkdir();
	}

	protected boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		bus.register(sidedMod);
		bus.register(sidedMod.getLogicalServer());
		return true;
	}

	@Subscribe
	public void onFMLPreInitialization(FMLPreInitializationEvent event) {
		//MessageRegister.registerPackets(NETWORK_WRAPPER);
	}

	public File getOpposingForcesFolder() {
		return this.dataDir;
	}

	public EventBus getEventBus() {
		return this.bus;
	}

	public ModClient getClient() {
		if (sidedMod instanceof ModClient)
			return (ModClient) sidedMod;
		else
			throw new RuntimeException("Accessing physical client on wrong side");
	}

	public ModServer getServer() {
		if (sidedMod instanceof ModServer)
			return (ModServer) sidedMod;
		else
			throw new RuntimeException("Accessing physical server on wrong side");
	}
	
	public LogicalServer getLogicalServer() {
		return this.sidedMod.getLogicalServer();
	}

	public static CraftingDead instance() {
		return instance;
	}

}
