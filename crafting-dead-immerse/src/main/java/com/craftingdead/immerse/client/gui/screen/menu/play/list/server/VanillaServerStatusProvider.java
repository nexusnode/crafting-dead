/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.client.gui.screen.menu.play.list.server;

import java.net.ConnectException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import com.google.common.base.Predicates;
import com.mojang.logging.LogUtils;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.multiplayer.resolver.ResolvedServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerNameResolver;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.network.protocol.status.ClientStatusPacketListener;
import net.minecraft.network.protocol.status.ClientboundPongResponsePacket;
import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.network.protocol.status.ServerboundPingRequestPacket;
import net.minecraft.network.protocol.status.ServerboundStatusRequestPacket;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class VanillaServerStatusProvider implements ServerStatusProvider {

  private static final Logger logger = LogUtils.getLogger();

  private static final Component CANNOT_CONNECT_MESSAGE =
      new TranslatableComponent("multiplayer.status.cannot_connect");

  private static final Scheduler SCHEDULER =
      Schedulers.newBoundedElastic(5, Integer.MAX_VALUE, "server-pinger");

  private final Queue<Connection> connections = new ConcurrentLinkedQueue<>();

  @Override
  public Mono<Result> checkStatus(ServerEntry serverEntry) {
    return Mono.just(serverEntry.toServerAddress())
        .map(ServerNameResolver.DEFAULT::resolveAddress)
        .flatMap(Mono::justOrEmpty)
        .map(ResolvedServerAddress::asInetSocketAddress)
        .switchIfEmpty(Mono.error(() -> new PingError(ConnectScreen.UNKNOWN_HOST_MESSAGE)))
        .map(address -> Connection.connectToServer(address, false))
        .onErrorMap(ConnectException.class, __ -> new PingError(CANNOT_CONNECT_MESSAGE))
        .flatMap(connection -> Mono.<Result>create(sink -> {
          this.connections.add(connection);
          connection.setListener(new PacketListener(sink, connection));
          connection.send(new ClientIntentionPacket(
              serverEntry.host(), serverEntry.port(), ConnectionProtocol.STATUS));
          connection.send(new ServerboundStatusRequestPacket());
          sink.onDispose(() -> this.connections.remove(connection));
        }))
        .onErrorMap(Predicates.not(Predicates.instanceOf(PingError.class)), error -> {
          logger.warn("An unknown error occurred while pinging: {}:{}",
              serverEntry.host(), serverEntry.port(), error);
          return new PingError(CANNOT_CONNECT_MESSAGE);
        })
        .subscribeOn(SCHEDULER);
  }

  public void tick() {
    for (var connection : this.connections) {
      if (connection.isConnected()) {
        connection.tick();
      }
    }
  }

  private static class PacketListener implements ClientStatusPacketListener {

    private final MonoSink<Result> sink;
    private final Connection connection;

    private boolean success;
    private long pingStartMs;
    private long pingStopMs;

    @Nullable
    private ServerStatus status;

    public PacketListener(MonoSink<Result> sink, Connection connection) {
      this.sink = sink;
      this.connection = connection;
    }

    @Override
    public void handleStatusResponse(ClientboundStatusResponsePacket packet) {
      if (this.status != null) {
        this.connection.disconnect(new TranslatableComponent("multiplayer.status.unrequested"));
        this.connection.handleDisconnection();
        return;
      }

      this.status = packet.getStatus();
      this.pingStartMs = Util.getMillis();
      this.connection.send(new ServerboundPingRequestPacket(this.pingStartMs));
      this.success = true;
    }

    @Override
    public void handlePongResponse(ClientboundPongResponsePacket packetIn) {
      this.pingStopMs = Util.getMillis();
      this.connection.disconnect(new TranslatableComponent("multiplayer.status.finished"));
      this.connection.handleDisconnection();
    }

    @Override
    public void onDisconnect(Component reason) {
      if (this.success) {
        this.sink.success(new Result(this.pingStopMs - this.pingStartMs, this.status));
      } else {
        this.sink.error(new PingError(reason));
      }
    }

    @Override
    public Connection getConnection() {
      return this.connection;
    }
  }
}
