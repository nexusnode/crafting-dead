package com.craftingdead.mod.common.core;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.craftingdead.mod.client.ModClient;
import com.craftingdead.mod.common.network.NetworkWrapper;
import com.craftingdead.mod.server.ModServer;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.IFMLCallHook;
import net.minecraftforge.fml.relauncher.Side;

public final class CraftingDead implements IFMLCallHook {

	public static final String MOD_ID = "craftingdead", MOD_VERSION = "0.0.1", MOD_NAME = "Crafting Dead";

	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static final Class<? extends ISidedMod<?>> CLIENT_MOD_CLASS = ModClient.class,
			SERVER_MOD_CLASS = ModServer.class;

	public static final SocketAddress MANAGEMENT_SERVER_ADDRESS = new InetSocketAddress("localhost", 32888);

	private static CraftingDead instance;

	private ISidedMod<?> sidedMod;

	private File modLocation;

	private File folder;

	private EventBus bus;

	private NetworkWrapper networkWrapper;

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
		networkWrapper = new NetworkWrapper(MOD_ID, sidedMod);
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		modLocation = (File) data.get("coremodLocation");
		folder = new File(data.get("mcLocation") + File.separator + CraftingDead.MOD_ID);
		folder.mkdir();
	}

	boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		bus.register(sidedMod);
		bus.register(sidedMod.getLogicalServer());
		return true;
	}

	@Subscribe
	public void onFMLPreInitialization(FMLPreInitializationEvent event) {
		// MessageRegister.registerPackets(NETWORK_WRAPPER);
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
