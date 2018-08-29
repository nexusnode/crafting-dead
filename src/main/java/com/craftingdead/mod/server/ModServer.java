package com.craftingdead.mod.server;

import com.craftingdead.mod.common.core.CraftingDead;
import com.craftingdead.mod.common.core.ISidedMod;
import com.craftingdead.network.mod.server.NetClientHandlerModServer;

public class ModServer implements ISidedMod<DedicatedServer, NetClientHandlerModServer> {

	private NetClientHandlerModServer netHandler = new NetClientHandlerModServer();

	private DedicatedServer dedicatedServer;

	@Override
	public void setup(CraftingDead mod) {
		dedicatedServer = new DedicatedServer();
	}

	@Override
	public DedicatedServer getLogicalServer() {
		return dedicatedServer;
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
