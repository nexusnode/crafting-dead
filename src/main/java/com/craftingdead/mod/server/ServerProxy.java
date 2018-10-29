package com.craftingdead.mod.server;

import java.net.InetSocketAddress;
import java.util.function.Supplier;

import com.craftingdead.mod.common.Proxy;
import com.craftingdead.mod.network.session.ServerSession;
import com.craftingdead.mod.server.multiplayer.DedicatedServer;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.server.FMLServerHandler;

public class ServerProxy implements Proxy {

	private MinecraftServer minecraftServer;

	@Override
	public void preInitialization(FMLPreInitializationEvent event) {
		this.minecraftServer = FMLServerHandler.instance().getServer();
	}

	@Override
	public Supplier<DedicatedServer> getLogicalServerSupplier() {
		return DedicatedServer::new;
	}

	@Override
	public InetSocketAddress getMasterServerAddress() {
		return new InetSocketAddress("localhost", 32888);
	}

	@Override
	public boolean useEpoll() {
		return minecraftServer.shouldUseNativeTransport();
	}

	@Override
	public ServerSession newSession() {
		return new ServerSession();
	}

}
