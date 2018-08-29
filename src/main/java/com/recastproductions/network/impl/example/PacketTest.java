package com.recastproductions.network.impl.example;

import com.recastproductions.network.impl.Session;
import com.recastproductions.network.packet.IPacket;
import com.recastproductions.network.packet.IPacketHandler;
import com.recastproductions.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class PacketTest implements IPacket {

    private String test;

    public PacketTest() {
        ;
    }

    public PacketTest(String test) {
        this.test = test;
    }

    @Override
    public void toBytes(ByteBuf out) throws IOException {
        ByteBufUtils.writeUTF8(out, test);
    }

    @Override
    public void fromBytes(ByteBuf in) throws IOException {
        test = ByteBufUtils.readUTF8(in);
    }

    public static class PacketTestHandler implements IPacketHandler<PacketTest, IPacket, Session<?>> {

        @Override
        public IPacket processPacket(PacketTest packet, Session<?> session) {
            System.out.println(packet.test);
            return null;
        }

    }

}
