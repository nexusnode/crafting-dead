package com.craftingdead.mod.masterserver.net;

import java.net.SocketAddress;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.network.TcpClient;
import com.craftingdead.network.pipeline.NetworkManager;
import com.craftingdead.network.protocol.IProtocol;
import com.craftingdead.network.protocol.ISession;
import com.craftingdead.network.util.TransportType;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.EventLoopGroup;
import lombok.Builder;

public class MasterServerConnector<S extends ISession, P extends IProtocol<S>> {

  private static final Logger logger = LogManager.getLogger();

  private final ScheduledExecutorService poller =
      Executors.newScheduledThreadPool(1, new ThreadFactoryBuilder()
          .setNameFormat("Master Server Connection Poller #%d").setDaemon(true).build());

  private final EventLoopGroup group;

  private final TcpClient tcpClient;

  private final long pollIntervalSeconds;

  private final SocketAddress address;

  private final Function<NetworkManager, S> sessionFactory;

  private final P protocol;

  private Optional<NetworkManager> networkManager = Optional.empty();

  @Builder
  public MasterServerConnector(boolean nativeTransport, long pollIntervalSeconds,
      SocketAddress address, Function<NetworkManager, S> sessionFactory, P protocol) {
    TransportType transport = nativeTransport ? TransportType.BEST : TransportType.NIO;
    this.group = transport.getEventLoopGroupFactory().apply(0, null);
    this.tcpClient = new TcpClient(this.group, transport.getSocketChannel());
    this.pollIntervalSeconds = pollIntervalSeconds;
    this.address = address;
    this.sessionFactory = sessionFactory;
    this.protocol = protocol;
  }

  public void start() {
    this.poller.scheduleAtFixedRate(this::pollConnection, 0L, this.pollIntervalSeconds,
        TimeUnit.SECONDS);
  }

  private void pollConnection() {
    if (!this.isConnected()) {
      logger.info("Attempting to connect to master server");
      try {
        this.networkManager =
            Optional.of(this.tcpClient.connect(this.address, this.sessionFactory, this.protocol));
      } catch (Throwable t) {
        logger.warn("Master server connection failed -> {}", t.getMessage());
      }
    }
  }

  public boolean isConnected() {
    return this.networkManager.map((networkManager) -> networkManager.isChannelOpen())
        .orElse(false);
  }
}
