package com.craftingdead.mod.server;

import com.craftingdead.mod.common.core.CraftingDead;
import com.craftingdead.mod.common.core.ISidedMod;
import com.craftingdead.mod.server.multiplayer.DedicatedServer;
import com.craftingdead.mod.server.network.NetClientHandlerModServer;

public class ModServer implements ISidedMod<ModServer, NetClientHandlerModServer> {

	private NetClientHandlerModServer netHandler = new NetClientHandlerModServer();

	@Override
	public void setup(CraftingDead<ModServer> mod) {
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
