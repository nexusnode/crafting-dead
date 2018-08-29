package com.craftingdead.mod.common.network.packet.client;

import com.craftingdead.mod.common.network.packet.PacketContextMod;
import com.recastproductions.network.packet.IPacket;
import com.recastproductions.network.packet.IPacketHandler;
import com.recastproductions.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class CPacketHandshake implements IPacket {

	private String[] mods;

	public CPacketHandshake() {
		;
	}

	public CPacketHandshake(String[] mods) {
		this.mods = mods;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			mods = ByteBufUtils.readStringArray(buf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		try {
			ByteBufUtils.writeStringArray(buf, mods);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String[] getMods() {
		return this.mods;
	}

	public static class SPacketHandlerHandshake implements IPacketHandler<CPacketHandshake, IPacket, PacketContextMod> {

		@Override
		public IPacket processPacket(CPacketHandshake packet, PacketContextMod ctx) {
			ctx.getModClient().getLogicalServer().onHandshake(ctx.getServerHandler().player, packet);
			return null;
		}

	}

}
