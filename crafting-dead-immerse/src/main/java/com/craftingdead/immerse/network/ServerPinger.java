package com.craftingdead.immerse.network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.core.util.Text;
import com.google.common.collect.Lists;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.network.status.IClientStatusNetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.ProtocolType;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.handshake.client.CHandshakePacket;
import net.minecraft.network.status.client.CPingPacket;
import net.minecraft.network.status.client.CServerQueryPacket;
import net.minecraft.network.status.server.SPongPacket;
import net.minecraft.network.status.server.SServerInfoPacket;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Edited version of {@link net.minecraft.client.network.ServerPinger}
 */
public class ServerPinger {

  private static final Logger LOGGER = LogManager.getLogger();
  /** A list of NetworkManagers that have pending pings */
  private final List<NetworkManager> pingDestinations =
      Collections.synchronizedList(Lists.newArrayList());

  public void ping(String ip, Consumer<PingData> pingCallback) throws UnknownHostException {
    ServerAddress serveraddress = ServerAddress.fromString(ip);
    final NetworkManager networkmanager = NetworkManager
        .createNetworkManagerAndConnect(InetAddress.getByName(serveraddress.getIP()),
            serveraddress.getPort(),
            false);
    this.pingDestinations.add(networkmanager);
    networkmanager.setNetHandler(new IClientStatusNetHandler() {
      private boolean successful;
      private boolean receivedStatus;
      private long pingSentAt;
      private PingData pingData = new PingData();

      public void handleServerInfo(SServerInfoPacket packetIn) {
        if (this.receivedStatus) {
          networkmanager
              .closeChannel(new TranslationTextComponent("multiplayer.status.unrequested"));
        } else {
          this.receivedStatus = true;
          ServerStatusResponse serverstatusresponse = packetIn.getResponse();
          ITextComponent serverDescription = serverstatusresponse.getServerDescription();
          pingData.setMotd(serverDescription);
          pingData.setServerVersion(
              new StringTextComponent(serverstatusresponse.getVersion().getName()));
          pingData.setVersion(serverstatusresponse.getVersion().getProtocol());
          pingData.setPlayersAmount(
              Text.of(serverstatusresponse.getPlayers().getOnlinePlayerCount() + "/" +
                  serverstatusresponse.getPlayers().getMaxPlayers()));
          this.pingSentAt = Util.milliTime();
          networkmanager.sendPacket(new CPingPacket(this.pingSentAt));
          this.successful = true;
        }
      }

      public void handlePong(SPongPacket packetIn) {
        long i = this.pingSentAt;
        long j = Util.milliTime();
        pingData.setPing(j - i);
        networkmanager.closeChannel(new TranslationTextComponent("multiplayer.status.finished"));
      }

      public void onDisconnect(ITextComponent reason) {
        if (!this.successful) {
          LOGGER.error("Can't ping {}: {}", serveraddress, reason.getString());
          pingData.setMotd(new TranslationTextComponent("multiplayer.status.cannot_connect"));
          pingData.setPlayersAmount(
              new TranslationTextComponent("menu.play.server_list.failed_to_load"));
        }
        pingCallback.accept(pingData);
      }

      public NetworkManager getNetworkManager() {
        return networkmanager;
      }
    });

    try {
      networkmanager.sendPacket(new CHandshakePacket(serveraddress.getIP(), serveraddress.getPort(),
          ProtocolType.STATUS));
      networkmanager.sendPacket(new CServerQueryPacket());
    } catch (Throwable throwable) {
      LOGGER.error(throwable);
    }
  }

  public void pingPendingNetworks() {
    synchronized (this.pingDestinations) {
      Iterator<NetworkManager> iterator = this.pingDestinations.iterator();

      while (iterator.hasNext()) {
        NetworkManager networkmanager = iterator.next();
        if (networkmanager.isChannelOpen()) {
          networkmanager.tick();
        } else {
          iterator.remove();
          networkmanager.handleDisconnection();
        }
      }

    }
  }

  public void clearPendingNetworks() {
    synchronized (this.pingDestinations) {
      Iterator<NetworkManager> iterator = this.pingDestinations.iterator();

      while (iterator.hasNext()) {
        NetworkManager networkmanager = iterator.next();
        if (networkmanager.isChannelOpen()) {
          iterator.remove();
          networkmanager.closeChannel(new TranslationTextComponent("multiplayer.status.cancelled"));
        }
      }

    }
  }

  public static class PingData {
    public static final PingData FAILED;
    static {
      FAILED = new PingData();
      FAILED.ping = -1L;
      FAILED.motd = StringTextComponent.EMPTY;
      FAILED.serverVersion = new TranslationTextComponent("multiplayer.status.old");
      FAILED.version = 0;
      FAILED.playersAmount = new TranslationTextComponent("menu.play.server_list.failed_to_load");

    }

    private long ping;
    private ITextComponent motd;
    private ITextComponent serverVersion;
    private ITextComponent playersAmount;
    private int version;


    public long getPing() {
      return ping;
    }

    public void setPing(long ping) {
      this.ping = ping;
    }

    public ITextComponent getMotd() {
      return motd;
    }

    public void setMotd(ITextComponent motd) {
      this.motd = motd;
    }

    public ITextComponent getPlayersAmount() {
      return playersAmount;
    }

    public void setPlayersAmount(ITextComponent playersAmount) {
      this.playersAmount = playersAmount;
    }

    public ITextComponent getServerVersion() {
      return serverVersion;
    }

    public void setServerVersion(ITextComponent serverVersion) {
      this.serverVersion = serverVersion;
    }

    public int getVersion() {
      return version;
    }

    public void setVersion(int version) {
      this.version = version;
    }
  }
}
