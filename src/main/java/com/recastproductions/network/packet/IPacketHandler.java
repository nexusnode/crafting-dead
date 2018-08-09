package com.recastproductions.network.packet;

import com.recastproductions.network.Session;

public interface IPacketHandler<REQ extends IPacket, REPLY extends IPacket, S extends Session<?>> {
	
	REPLY processPacket(REQ packet, S session);

}
