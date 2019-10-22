package com.craftingdead.mod.server;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.IModDist;
import com.craftingdead.mod.masterserver.net.protocol.handshake.packet.HandshakePacket;
import com.craftingdead.mod.masterserver.net.protocol.modserverlogin.ModServerLoginSession;
import com.craftingdead.mod.masterserver.net.protocol.modserverlogin.packet.LoginPacket;
import com.craftingdead.network.pipeline.NetworkManager;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ServerDist implements IModDist {

  private DedicatedServer dedicatedServer;

  public ServerDist() {
    FMLJavaModLoadingContext.get().getModEventBus().register(this);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public boolean isUsingNativeTransport() {
    return this.dedicatedServer.shouldUseNativeTransport();
  }

  @Override
  public void handleConnect(NetworkManager networkManager) {
    networkManager.sendMessage(new HandshakePacket(HandshakePacket.SERVER_LOGIN));
    networkManager.setSession(new ModServerLoginSession(networkManager, this.dedicatedServer));
    networkManager.sendMessage(
        new LoginPacket(this.dedicatedServer.getHostname(), this.dedicatedServer.getPort()));
  }

  // ================================================================================
  // Mod Events
  // ================================================================================

  @SubscribeEvent
  public void handleDedicatedServerSetup(FMLDedicatedServerSetupEvent event) {
    this.dedicatedServer = event.getServerSupplier().get();
  }

  // ================================================================================
  // Forge Events
  // ================================================================================

  @SubscribeEvent
  public void handleServerTick(TickEvent.ServerTickEvent event) {
    switch (event.phase) {
      case END:
        CraftingDead.getInstance().tickConnection();
        break;
      default:
        break;
    }
  }
}
