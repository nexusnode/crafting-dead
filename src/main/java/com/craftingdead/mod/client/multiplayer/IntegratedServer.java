package com.craftingdead.mod.client.multiplayer;

import com.craftingdead.mod.client.ModClient;
import com.craftingdead.mod.common.multiplayer.LogicalServer;
import com.craftingdead.mod.common.multiplayer.PlayerMP;
import com.craftingdead.mod.common.multiplayer.network.packet.PacketHandshake;

public class IntegratedServer extends LogicalServer<ModClient> {

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

	@Override
	protected boolean onPlayerKillPlayer(PlayerMP player, PlayerMP cause) {
		return false;
	}

	@Override
	protected boolean onPlayerKillZombie(PlayerMP cause) {
		return false;
	}

}
