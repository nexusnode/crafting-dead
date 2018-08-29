package com.recastproductions.network.impl.example.server;

import com.recastproductions.network.impl.Session;
import com.recastproductions.network.impl.example.PacketTest;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class SessionTest extends Session<NetServerHandlerTest> {

    public SessionTest(Channel ch, NetServerHandlerTest handler) {
        super(ch, handler);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        ctx.channel().writeAndFlush(new PacketTest("Hello client"));
    }

}
