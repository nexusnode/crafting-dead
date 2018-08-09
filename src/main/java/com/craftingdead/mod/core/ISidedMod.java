package com.craftingdead.mod.core;

import com.craftingdead.mod.LogicalServer;

public interface ISidedMod<L extends LogicalServer> {
	
	void setup(CraftingDead mod);

	L getLogicalServer();
	
	void onShutdown();

}
