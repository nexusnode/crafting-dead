/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.client.util;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import org.slf4j.Logger;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.DefaultUncaughtExceptionHandler;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.multiplayer.resolver.ResolvedServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerNameResolver;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.network.protocol.status.ClientStatusPacketListener;
import net.minecraft.network.protocol.status.ClientboundPongResponsePacket;
import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.network.protocol.status.ServerboundPingRequestPacket;
import net.minecraft.network.protocol.status.ServerboundStatusRequestPacket;

/**
 * Edited version of {@link net.minecraft.client.network.ServerPinger}
 */
public class ServerPinger {

  public static final ServerPinger INSTANCE = new ServerPinger();

  private static final Logger logger = LogUtils.getLogger();

  private static final Executor executor = Executors.newFixedThreadPool(5,
      new ThreadFactoryBuilder().setNameFormat("Server Pinger #%d").setDaemon(true)
          .setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(logger)).build());

  private final List<Connection> pendingPings = Collections.synchronizedList(Lists.newArrayList());

  private ServerPinger() {}

  public void ping(ServerAddress serveraddress, Consumer<PingData> callback) {
    executor.execute(() -> {
      Optional<InetSocketAddress> optional =
          ServerNameResolver.DEFAULT.resolveAddress(serveraddress)
              .map(ResolvedServerAddress::asInetSocketAddress);
      var pingData = new PingData();

      if (!optional.isPresent()) {
        pingFailed(serveraddress, ConnectScreen.UNKNOWN_HOST_MESSAGE, pingData);
        return;
      }
      final var inetsocketaddress = optional.get();

      Connection connection;
      try {
        connection = Connection.connectToServer(inetsocketaddress, false);
      } catch (Throwable e) {
        callback.accept(PingData.FAILED);
        return;
      }

      this.pendingPings.add(connection);

      connection.setListener(new ClientStatusPacketListener() {

        private boolean successful;
        private boolean receivedStatus;
        private long pingSentAt;

        @Override
        public void handleStatusResponse(ClientboundStatusResponsePacket packet) {
          if (this.receivedStatus) {
            connection.disconnect(new TranslatableComponent("multiplayer.status.unrequested"));
          } else {
            this.receivedStatus = true;
            ServerStatus status = packet.getStatus();
            if (status.getDescription() != null) {
              pingData.motd = status.getDescription();
            } else {
              pingData.motd = TextComponent.EMPTY;
            }
            pingData.setServerVersion(new TextComponent(status.getVersion().getName()));
            pingData.setVersion(status.getVersion().getProtocol());
            pingData.setPlayersAmount(new TextComponent(
                status.getPlayers().getNumPlayers() + "/" + status.getPlayers().getMaxPlayers()));
            this.pingSentAt = Util.getMillis();
            connection.send(new ServerboundPingRequestPacket(this.pingSentAt));
            this.successful = true;
          }
        }

        @Override
        public void handlePongResponse(ClientboundPongResponsePacket packetIn) {
          long i = this.pingSentAt;
          long j = Util.getMillis();
          pingData.setPing(j - i);
          connection.disconnect(new TranslatableComponent("multiplayer.status.finished"));
        }

        @Override
        public void onDisconnect(Component reason) {
          if (!this.successful) {
            pingFailed(serveraddress, reason, pingData);
          }
          callback.accept(pingData);
        }

        @Override
        public Connection getConnection() {
          return connection;
        }
      });

      try {
        connection.send(
            new ClientIntentionPacket(inetsocketaddress.getHostName(), inetsocketaddress.getPort(),
                ConnectionProtocol.STATUS));
        connection.send(new ServerboundStatusRequestPacket());
      } catch (Throwable t) {
        callback.accept(PingData.FAILED);
      }
    });
  }

  private static void pingFailed(ServerAddress address, Component reason, PingData pingData) {
    logger.error("Can't ping {}:{} Reason: {}", address.getHost(), address.getPort(),
        reason.getString());
    pingData.setMotd(new TranslatableComponent("multiplayer.status.cannot_connect"));
    pingData.setPlayersAmount(new TextComponent("?"));
  }

  public void pingPendingNetworks() {
    synchronized (this.pendingPings) {
      Iterator<Connection> iterator = this.pendingPings.iterator();

      while (iterator.hasNext()) {
        Connection networkmanager = iterator.next();
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
      Iterator<Connection> iterator = this.pendingPings.iterator();
      while (iterator.hasNext()) {
        Connection networkmanager = iterator.next();
        if (networkmanager.isConnected()) {
          iterator.remove();
          networkmanager.disconnect(new TranslatableComponent("multiplayer.status.cancelled"));
        }
      }

    }
  }

  public static class PingData {
    public static final PingData FAILED;
    static {
      FAILED = new PingData();
      FAILED.ping = -1L;
      FAILED.motd = new TranslatableComponent("multiplayer.status.cannot_connect")
          .withStyle(ChatFormatting.RED);
      FAILED.serverVersion = new TranslatableComponent("multiplayer.status.old");
      FAILED.version = 0;
      FAILED.playersAmount = new TextComponent("?");
    }

    private long ping;
    private Component motd;
    private Component serverVersion;
    private Component playersAmount;
    private int version;

    public long getPing() {
      return ping;
    }

    public void setPing(long ping) {
      this.ping = ping;
    }

    public Component getMotd() {
      return motd;
    }

    public void setMotd(Component motd) {
      this.motd = motd;
    }

    public Component getPlayersAmount() {
      return playersAmount;
    }

    public void setPlayersAmount(Component playersAmount) {
      this.playersAmount = playersAmount;
    }

    public Component getServerVersion() {
      return serverVersion;
    }

    public void setServerVersion(Component serverVersion) {
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
