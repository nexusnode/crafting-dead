package com.craftingdead.mod.masterserver.net;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.network.pipeline.Session;
import com.craftingdead.network.protocol.IMessage;
import com.craftingdead.network.util.BootstrapUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;

public class MasterServerClient {

  private static final Logger logger = LogManager.getLogger();

  private final EventLoopGroup group = BootstrapUtil.createBestEventLoopGroup(0, null);

  private final InetSocketAddress address;

  private final Supplier<IMessage> loginMessageSupplier;

  private Optional<Channel> channel = Optional.empty();

  public MasterServerClient(InetSocketAddress address, Supplier<IMessage> loginMessageSupplier) {
    this.address = address;
    this.loginMessageSupplier = loginMessageSupplier;
    ScheduledExecutorService executor =
        Executors.newScheduledThreadPool(1, new ThreadFactoryBuilder()
            .setNameFormat("Master Server Connection Poller #%d").setDaemon(true).build());
    executor.scheduleAtFixedRate(() -> {
      if (!this.isConnected()) {
        logger.info("Attempting to connect to master server");
        try {
          this.connect();
        } catch (Throwable t) {
          logger.warn("Master server connection failed -> {}", t.getMessage());
        }
      }
    }, 0L, 30L, TimeUnit.SECONDS);
  }

  private void connect() {
    this.channel.filter((ch) -> ch.isActive()).ifPresent((ch) -> ch.close().syncUninterruptibly());
    this.channel = Optional.of(new Bootstrap() //
        .group(this.group) //
        .channel(BootstrapUtil.bestSocketChannel()) //
        .handler(BootstrapUtil.createChannelInitializer(
            () -> new CustomSession(ProtocolType.LOGIN.getProtocol(), this.loginMessageSupplier)))
        .connect(this.address).syncUninterruptibly().channel());
  }

  public boolean isConnected() {
    return this.channel.map((ch) -> ch.isActive()).orElse(false);
  }

  public Optional<Session> getSession() {
    return this.channel.filter((ch) -> ch.isActive())
        .map((ch) -> (Session) ch.pipeline().get("session"));
  }

  public void shutdown() {
    this.channel.filter((ch) -> ch.isActive()).ifPresent((ch) -> ch.close().syncUninterruptibly());
    this.group.shutdownGracefully().syncUninterruptibly();
  }
}
