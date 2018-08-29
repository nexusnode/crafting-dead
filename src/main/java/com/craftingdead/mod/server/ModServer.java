package com.craftingdead.mod.server;

import com.craftingdead.mod.common.core.CraftingDead;
import com.craftingdead.mod.common.core.ISidedMod;

public class ModServer implements ISidedMod<DedicatedServer> {

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

}
