package com.recastproductions.network.impl.server;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.recastproductions.network.impl.NetworkRegistry;
import com.recastproductions.network.impl.pipeline.HandshakeHandler;
import com.recastproductions.network.packet.IHandshakePacket;
import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map.Entry;

public class NetworkRegistryServer extends NetworkRegistry {

    private static final Logger LOGGER = LogManager.getLogger();

    private BiMap<String, Class<? extends IHandshakePacket>> handshakeMap = HashBiMap.create();
    private BiMap<String, NetServerHandler<?, ?>> handlerMap = HashBiMap.create();

    public void registerNetHandler(NetServerHandler<?, ?> handler) {
        if (!this.handlerMap.containsKey(handler.getName())) {
            this.handlerMap.put(handler.getName(), handler);
            this.handshakeMap.put(handler.getName(), handler.getHandshakePacketClass());
        } else {
            LOGGER.warn("A NetHandler with the name {} is already registered under the class {}", handler.getName(),
                    handlerMap.get(handler.getName()).getClass().getCanonicalName());
        }
    }

    public void removeNetHandler(String name) {
        handshakeMap.remove(name);
        handlerMap.remove(name);
    }

    public String getName(Class<? extends IHandshakePacket> handshakeMessage) {
        return handshakeMap.inverse().get(handshakeMessage);
    }

    public NetServerHandler<?, ?> getNetHandler(String name) {
        return handlerMap.get(name);
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        for (Entry<String, NetServerHandler<?, ?>> entry : handlerMap.entrySet()) {
            ch.pipeline().addLast(entry.getKey(), this.wrapHandshakeHandler((NetServerHandler<?, ?>) entry.getValue()));
        }
    }

    private <T extends IHandshakePacket, SH extends NetServerHandler<T, ?>> HandshakeHandler<T, SH> wrapHandshakeHandler(
            SH netHandler) {
        return new HandshakeHandler<T, SH>(netHandler.getHandshakePacketClass(), netHandler);
    }

}
