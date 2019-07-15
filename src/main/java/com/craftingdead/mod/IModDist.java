package com.craftingdead.mod;

import com.craftingdead.mod.masterserver.net.MasterServerConnector;

public interface IModDist {

  MasterServerConnector.MasterServerConnectorBuilder<?, ?> getConnectorBuilder();
}
