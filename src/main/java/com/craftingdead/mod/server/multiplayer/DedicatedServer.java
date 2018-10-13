package com.craftingdead.mod.server.multiplayer;

import java.util.Map;

import com.craftingdead.mod.common.CraftingDead;
import com.craftingdead.mod.common.multiplayer.LogicalServer;
import com.craftingdead.mod.common.multiplayer.PlayerMP;
import com.craftingdead.mod.network.message.server.MessageKilledZombie;

public class DedicatedServer extends LogicalServer {

	@Override
	protected boolean checkModList(Map<String, String> mods) {
		return true;
	}

	@Override
	protected void onPlayerAccepted(PlayerMP client) {
		;
	}

	@Override
	protected boolean onPlayerKillPlayer(PlayerMP player, PlayerMP cause) {
		return false;
	}

	@Override
	protected boolean onPlayerKillZombie(PlayerMP cause) {
		CraftingDead.instance().getNetworkManager().sendMessage(new MessageKilledZombie(cause.getUUID()));
		return false;
	}

}
