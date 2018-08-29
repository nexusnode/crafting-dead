package com.recastproductions.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.net.SocketAddress;

public class NetworkServer {

    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * The {@link ServerBootstrap} used to initialise Netty.
     */
    private final ServerBootstrap bootstrap = new ServerBootstrap();
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    public NetworkServer(ChannelHandler bootstrapHandler) {
        LOGGER.info("Initialising network server");
        bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(bootstrapHandler)
                .childOption(ChannelOption.TCP_NODELAY, true).childOption(ChannelOption.SO_KEEPALIVE, true);
        LOGGER.info("Ready to bind");
    }

    /**
     * Bind to the specified {@link SocketAddress}
     *
     * @param address
     * @return the {@link ChannelFuture}
     */
    public ChannelFuture bind(final SocketAddress address) {
        LOGGER.info("Binding to {}", address.toString());
        return bootstrap.bind(address).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> f) throws Exception {
                if (f.isSuccess()) {
                    onBindSuccess(address);
                } else {
                    onBindFailure(address, f.cause());
                }
            }
        });
    }

    /**
     * Called when a bind is successfully made
     *
     * @param address - the address we are now bound too
     */
    public void onBindSuccess(SocketAddress address) {
        LOGGER.info("Bind success! Now listening for connections on {}", address.toString());
    }

    /**
     * Called when a bind fails
     *
     * @param address - the address we attempted to bind too
     * @param t       - the cause of why the binding failed
     */
    public void onBindFailure(SocketAddress address, @Nullable Throwable t) {
        LOGGER.warn(
                "Bind failed! Check no other services are running on {} and that the address specified exists on the local host",
                address.toString());
    }

    /**
     * Shutdown the boss and worker {@link EventLoopGroup}
     */
    public void shutdown() {
        LOGGER.info("Shutting down the network server");
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

}
