package com.craftingdead.mod.server;

import com.craftingdead.mod.core.CraftingDead;
import com.craftingdead.mod.core.ISidedMod;

public class ModServer implements ISidedMod<DedicatedServer> {

	@Override
	public void setup(CraftingDead mod) {
		
	}

	@Override
	public DedicatedServer getLogicalServer() {
		return null;
	}

	@Override
	public void onShutdown() {
		
	}

}
