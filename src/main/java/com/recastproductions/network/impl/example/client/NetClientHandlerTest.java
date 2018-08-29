package com.recastproductions.network.impl.example.client;

import com.recastproductions.network.impl.client.NetClientHandler;
import com.recastproductions.network.impl.example.HandshakeTest;
import com.recastproductions.network.impl.example.PacketTest;
import io.netty.channel.Channel;

public class NetClientHandlerTest extends NetClientHandler<HandshakeTest, SessionTest> {

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public HandshakeTest getHandshakePacket() {
        return new HandshakeTest();
    }

    @Override
    public SessionTest newSession(Channel ch) {
        return new SessionTest(ch, this);
    }

    @Override
    protected void registerPackets() {
        this.registerPacket(0, PacketTest.class, new PacketTest.PacketTestHandler());
    }

}
