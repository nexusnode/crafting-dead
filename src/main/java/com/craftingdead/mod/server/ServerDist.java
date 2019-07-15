package com.craftingdead.mod.server;

import java.net.InetSocketAddress;
import com.craftingdead.mod.CommonConfig;
import com.craftingdead.mod.IModDist;
import com.craftingdead.mod.masterserver.net.MasterServerConnector;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ServerDist implements IModDist {

  private DedicatedServer dedicatedServer;

  public ServerDist() {
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverSetup);
  }

  private void serverSetup(FMLDedicatedServerSetupEvent event) {
    this.dedicatedServer = event.getServerSupplier().get();
  }

  @Override
  public MasterServerConnector.MasterServerConnectorBuilder<?, ?> getConnectorBuilder() {
    return MasterServerConnector.builder() //
        .nativeTransport(this.dedicatedServer.shouldUseNativeTransport()) //
        .pollIntervalSeconds(30L) //
        .address(
            InetSocketAddress.createUnresolved(CommonConfig.commonConfig.masterServerHost.get(),
                CommonConfig.commonConfig.masterServerPort.get())) //
        .sessionFactory(null) //
        .protocol(null);
  }
}
