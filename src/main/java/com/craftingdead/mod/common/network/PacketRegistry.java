package com.craftingdead.mod.common.network;

import com.craftingdead.mod.common.network.packet.PacketHandshake;
import com.craftingdead.mod.common.network.packet.PacketHandshake.PacketHandlerHandshake;
import com.craftingdead.mod.common.network.packet.PacketRequestHandshake;
import com.craftingdead.mod.common.network.packet.PacketRequestHandshake.PacketHandlerRequestHandshake;
import com.craftingdead.mod.common.network.packet.PacketUpdateStatistics;
import com.craftingdead.mod.common.network.packet.PacketUpdateStatistics.PacketHandlerUpdateStatistics;

import net.minecraftforge.fml.relauncher.Side;

/**
 * Place to register all internal packets
 * 
 * @author Sm0keySa1m0n
 *
 */
public class PacketRegistry {

	private static int currentDiscriminator = -1;

	public static void registerPackets(NetworkWrapper wrapper) {
		wrapper.registerPacket(PacketHandlerRequestHandshake.class, PacketRequestHandshake.class,
				currentDiscriminator++, Side.CLIENT);
		wrapper.registerPacket(PacketHandlerUpdateStatistics.class, PacketUpdateStatistics.class,
				currentDiscriminator++, Side.CLIENT);

		wrapper.registerPacket(PacketHandlerHandshake.class, PacketHandshake.class, currentDiscriminator++,
				Side.SERVER);
	}

}
