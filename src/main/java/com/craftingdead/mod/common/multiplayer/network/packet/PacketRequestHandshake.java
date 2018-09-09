package com.craftingdead.mod.common.multiplayer.network.packet;

import com.recastproductions.network.packet.IPacket;
import com.recastproductions.network.packet.IPacketHandler;
import io.netty.buffer.ByteBuf;

public class PacketRequestHandshake implements IPacket {

	@Override
	public void fromBytes(ByteBuf buf) {
		;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		;
	}

	public static class PacketHandlerRequestHandshake
			implements IPacketHandler<PacketRequestHandshake, PacketHandshake, PacketContextMod> {

		@Override
		public PacketHandshake processPacket(PacketRequestHandshake packet, PacketContextMod ctx) {
			return ctx.getModClient().buildHandshakePacket();
		}

	}

}
