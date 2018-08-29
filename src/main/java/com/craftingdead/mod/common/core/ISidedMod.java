package com.craftingdead.mod.common.core;

import com.craftingdead.mod.common.server.LogicalServer;

public interface ISidedMod<L extends LogicalServer> {

    void setup(CraftingDead mod);

    L getLogicalServer();

    void onShutdown();

}
