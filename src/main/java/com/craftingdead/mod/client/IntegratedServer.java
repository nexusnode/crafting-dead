package com.craftingdead.mod.client;

import com.craftingdead.mod.common.network.packet.PacketHandshake;
import com.craftingdead.mod.common.server.LogicalServer;
import com.craftingdead.mod.common.server.PlayerMP;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class IntegratedServer extends LogicalServer {

	@Override
	protected void onServerStart(FMLServerStartingEvent event) {

	}

	@Override
	protected boolean verifyHandshake(PacketHandshake handshakePacket) {
		return true;
	}

	@Override
	protected void playerAccepted(PlayerMP player) {

	}

	@Override
	protected int getHandshakeTimeout() {
		return 500;
	}

}
