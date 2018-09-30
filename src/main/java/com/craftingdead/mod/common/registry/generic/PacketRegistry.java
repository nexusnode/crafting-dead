package com.craftingdead.mod.common.registry.generic;

import com.craftingdead.mod.common.multiplayer.network.NetworkWrapper;
import com.craftingdead.mod.common.multiplayer.network.packet.PacketHandshake;
import com.craftingdead.mod.common.multiplayer.network.packet.PacketRequestHandshake;
import com.craftingdead.mod.common.multiplayer.network.packet.PacketUpdateStatistics;
import com.craftingdead.mod.common.multiplayer.network.packet.PacketHandshake.PacketHandlerHandshake;
import com.craftingdead.mod.common.multiplayer.network.packet.PacketRequestHandshake.PacketHandlerRequestHandshake;
import com.craftingdead.mod.common.multiplayer.network.packet.PacketUpdateStatistics.PacketHandlerUpdateStatistics;

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
