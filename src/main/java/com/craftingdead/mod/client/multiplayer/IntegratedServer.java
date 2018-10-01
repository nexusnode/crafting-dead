package com.craftingdead.mod.client.multiplayer;

import java.util.Map;

import com.craftingdead.mod.common.multiplayer.LogicalServer;
import com.craftingdead.mod.common.multiplayer.PlayerMP;

public class IntegratedServer extends LogicalServer {

	@Override
	protected boolean checkModList(Map<String, String> modList) {
		return true;
	}

	@Override
	protected void onPlayerAccepted(PlayerMP player) {

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
