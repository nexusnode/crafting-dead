package com.recastproductions.network.packet;

public interface IPacketHandler<REQ extends IPacket, REPLY extends IPacket, C extends IPacketContext> {

    REPLY processPacket(REQ packet, C ctx);

}
