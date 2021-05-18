/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.immerse.client.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.minecraft.client.network.status.IClientStatusNetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.ProtocolType;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.handshake.client.CHandshakePacket;
import net.minecraft.network.status.client.CPingPacket;
import net.minecraft.network.status.client.CServerQueryPacket;
import net.minecraft.network.status.server.SPongPacket;
import net.minecraft.network.status.server.SServerInfoPacket;
import net.minecraft.util.DefaultUncaughtExceptionHandler;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Edited version of {@link net.minecraft.client.network.ServerPinger}
 */
public class ServerPinger {

  public static final ServerPinger INSTANCE = new ServerPinger();

  private static final Logger logger = LogManager.getLogger();

  private static final Executor executor = Executors.newFixedThreadPool(5,
      new ThreadFactoryBuilder()
          .setNameFormat("Server Pinger #%d")
          .setDaemon(true)
          .setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(logger))
          .build());

  private final List<NetworkManager> pendingPings =
      Collections.synchronizedList(Lists.newArrayList());

  private ServerPinger() {}

  public void ping(String hostName, int port, Consumer<PingData> callback) {
    NetworkManager networkManagger;
    try {
      networkManagger =
          NetworkManager.connectToServer(InetAddress.getByName(hostName), port, false);
    } catch (UnknownHostException e) {
      logger.warn("Unknown host: " + hostName);
      callback.accept(PingData.FAILED);
      return;
    }

    this.pendingPings.add(networkManagger);

    networkManagger.setListener(new IClientStatusNetHandler() {

      private boolean successful;
      private boolean receivedStatus;
      private long pingSentAt;
      private PingData pingData = new PingData();

      @Override
      public void handleStatusResponse(SServerInfoPacket packet) {
        if (this.receivedStatus) {
          networkManagger
              .disconnect(new TranslationTextComponent("multiplayer.status.unrequested"));
        } else {
          this.receivedStatus = true;
          ServerStatusResponse status = packet.getStatus();
          ITextComponent description = status.getDescription();
          this.pingData.setMotd(description);
          this.pingData.setServerVersion(
              new StringTextComponent(status.getVersion().getName()));
          this.pingData.setVersion(status.getVersion().getProtocol());
          this.pingData.setPlayersAmount(
              new StringTextComponent(status.getPlayers().getNumPlayers() + "/" +
                  status.getPlayers().getMaxPlayers()));
          this.pingSentAt = Util.getMillis();
          networkManagger.send(new CPingPacket(this.pingSentAt));
          this.successful = true;
        }
      }

      @Override
      public void handlePongResponse(SPongPacket packetIn) {
        long i = this.pingSentAt;
        long j = Util.getMillis();
        pingData.setPing(j - i);
        networkManagger.disconnect(new TranslationTextComponent("multiplayer.status.finished"));
      }

      @Override
      public void onDisconnect(ITextComponent reason) {
        if (!this.successful) {
          logger.error("Can't ping {}:{} Reason: {}", hostName, port, reason.getString());
          pingData.setMotd(new TranslationTextComponent("multiplayer.status.cannot_connect"));
          pingData.setPlayersAmount(
              new TranslationTextComponent("menu.play.server_list.failed_to_load"));
        }
        callback.accept(pingData);
      }

      @Override
      public NetworkManager getConnection() {
        return networkManagger;
      }
    });

    executor.execute(() -> {
      try {
        networkManagger.send(new CHandshakePacket(hostName, port, ProtocolType.STATUS));
        networkManagger.send(new CServerQueryPacket());
      } catch (Throwable t) {
        logger.error(t);
      }
    });
  }

  public void pingPendingNetworks() {
    synchronized (this.pendingPings) {
      Iterator<NetworkManager> iterator = this.pendingPings.iterator();

      while (iterator.hasNext()) {
        NetworkManager networkmanager = iterator.next();
        if (networkmanager.isConnected()) {
          networkmanager.tick();
        } else {
          iterator.remove();
          networkmanager.handleDisconnection();
        }
      }

    }
  }

  public void clearPendingNetworks() {
    synchronized (this.pendingPings) {
      Iterator<NetworkManager> iterator = this.pendingPings.iterator();
      while (iterator.hasNext()) {
        NetworkManager networkmanager = iterator.next();
        if (networkmanager.isConnected()) {
          iterator.remove();
          networkmanager.disconnect(new TranslationTextComponent("multiplayer.status.cancelled"));
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
