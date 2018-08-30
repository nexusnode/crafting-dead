package com.craftingdead.mod.common.network;

import com.craftingdead.mod.common.network.packet.PacketHandshake;
import com.craftingdead.mod.common.network.packet.PacketRequestHandshake;
import com.craftingdead.mod.common.network.packet.PacketHandshake.PacketHandlerHandshake;
import com.craftingdead.mod.common.network.packet.PacketRequestHandshake.PacketHandlerRequestHandshake;

import net.minecraftforge.fml.relauncher.Side;

public class PacketRegistry {

	private static int packetId = -1;

	public static void registerPackets(NetworkWrapper wrapper) {
		wrapper.registerPacket(PacketHandlerRequestHandshake.class, PacketRequestHandshake.class, packetId++,
				Side.CLIENT);

		wrapper.registerPacket(PacketHandlerHandshake.class, PacketHandshake.class, packetId++, Side.SERVER);
	}

}
