package com.recastproductions.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.net.SocketAddress;

public class NetworkClient {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Bootstrap bootstrap = new Bootstrap();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    public NetworkClient(ChannelHandler bootstrapHandler) {
        LOGGER.info("Initialising network client");
        bootstrap.group(workerGroup).channel(NioSocketChannel.class).handler(bootstrapHandler);
        LOGGER.info("Ready to connect");
    }

    /**
     * Connect to the specified {@link SocketAddress}
     *
     * @param address
     * @return the {@link ChannelFuture} or null something wrong happened
     */
    @Nullable
    public ChannelFuture connect(final SocketAddress address) {
        LOGGER.info("Connecting to {}", address);
        return bootstrap.connect(address).addListener(new GenericFutureListener<ChannelFuture>() {
            @Override
            public void operationComplete(ChannelFuture f) throws Exception {
                if (f.isSuccess()) {
                    onConnectSuccess(f);
                } else {
                    onConnectFailure(f);
                }
            }
        });
    }

    /**
     * Sets a {@link ChannelOption} to apply prior to connecting. After a connection
     * has been established, this method is useless; an Exception *may* be thrown on
     * it's use
     *
     * @param <T>
     * @param option
     * @param value
     */
    public <T> void preConnectOption(ChannelOption<T> option, T value) {
        bootstrap.option(option, value);
    }

    /**
     * Called when a connection has successfully been made
     *
     * @param address - the address we successfully connected to
     */
    public void onConnectSuccess(ChannelFuture future) {
        LOGGER.info("Successfully connected to {}", future.channel().remoteAddress().toString());
    }

    /**
     * Called when a connection cannot be made
     *
     * @param address - the address that we attempted to connect to
     */
    public void onConnectFailure(ChannelFuture future) {
//        LOGGER.warn("Could not connect to {}, make sure the remote host is listening for connection on that address",
//                future.channel().remoteAddress().toString());
    }

    /**
     * Shutdown the {@link EventLoopGroup}
     */
    public void shutdown() {
        LOGGER.info("Shutting down the network client");
        workerGroup.shutdownGracefully();
    }

}
