package com.craftingdead.mod.common.multiplayer.network.packet;

import java.io.IOException;

import com.craftingdead.mod.common.core.CraftingDead;
import com.craftingdead.mod.common.event.server.HandshakeEvent;
import com.recastproductions.network.packet.IPacket;
import com.recastproductions.network.packet.IPacketHandler;
import com.recastproductions.network.util.ByteBufUtils;

import io.netty.buffer.ByteBuf;

public class PacketHandshake implements IPacket {

	private String[] mods;

	public PacketHandshake() {
		;
	}

	public PacketHandshake(String[] mods) {
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

	public static class PacketHandlerHandshake implements IPacketHandler<PacketHandshake, IPacket, PacketContextMod> {

		@Override
		public IPacket processPacket(PacketHandshake packet, PacketContextMod ctx) {
			CraftingDead.instance().getEventBus().post(new HandshakeEvent(ctx.getServerHandler().player, packet));
			return null;
		}

	}

}
