package com.craftingdead.mod.server.dedicated;

import java.util.function.Supplier;

import com.craftingdead.mod.ModDist;

public class ServerDist implements ModDist {

	@Override
	public Supplier<DedicatedServer> getLogicalServerSupplier() {
		return DedicatedServer::new;
	}

}
