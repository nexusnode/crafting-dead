package com.craftingdead.mod.server;

import com.craftingdead.mod.common.IMod;
import com.craftingdead.mod.server.multiplayer.DedicatedServer;
import com.craftingdead.mod.server.network.NetClientHandlerModServer;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModServer implements IMod<NetClientHandlerModServer> {

	private NetClientHandlerModServer netHandler = new NetClientHandlerModServer();

	@Override
	public void preInitialization(FMLPreInitializationEvent event) {
		;
	}

	@Override
	public void initialization(FMLInitializationEvent event) {
		;
	}

	@Override
	public void postInitialization(FMLPostInitializationEvent event) {
		;
	}

	@Override
	public Class<DedicatedServer> getLogicalServer() {
		return DedicatedServer.class;
	}

	@Override
	public void shutdown() {
		;
	}

	@Override
	public NetClientHandlerModServer getNetHandler() {
		return netHandler;
	}

}
