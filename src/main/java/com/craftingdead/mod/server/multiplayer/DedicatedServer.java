package com.craftingdead.mod.server.multiplayer;

import java.util.Map;

import com.craftingdead.mod.common.CraftingDead;
import com.craftingdead.mod.common.multiplayer.LogicalServer;
import com.craftingdead.mod.common.multiplayer.PlayerMP;
import com.craftingdead.mod.network.message.server.MessageKilledZombie;

import sm0keysa1m0n.network.wrapper.NetworkManager;

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
		NetworkManager networkManager = CraftingDead.instance().getNetworkManager();
		if (networkManager != null)
			networkManager.sendMessage(new MessageKilledZombie(cause.getUUID()));
		return false;
	}

}
