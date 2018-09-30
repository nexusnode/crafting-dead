package com.craftingdead.mod.server.multiplayer;

import com.craftingdead.mod.common.multiplayer.LogicalServer;
import com.craftingdead.mod.common.multiplayer.PlayerMP;
import com.craftingdead.mod.common.multiplayer.network.packet.PacketHandshake;
import com.craftingdead.mod.server.ServerMod;
import com.craftingdead.mod.server.network.packet.PacketKilledZombie;

public class DedicatedServer extends LogicalServer<ServerMod> {

	@Override
	protected boolean verifyHandshake(PacketHandshake handshakePacket) {
		return true;
	}

	@Override
	protected void playerAccepted(PlayerMP client) {
		;
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
		this.getMod().getNetHandler().getCurrentSession().sendPacket(new PacketKilledZombie(cause.getUUID()));
		return false;
	}

}
