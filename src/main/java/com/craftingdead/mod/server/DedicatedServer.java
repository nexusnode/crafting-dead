package com.craftingdead.mod.server;

import com.craftingdead.mod.LogicalServer;
import com.craftingdead.mod.network.message.client.CMessageHandshake;
import com.craftingdead.mod.player.Player;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class DedicatedServer extends LogicalServer {

	@Override
	protected void onServerStart(FMLServerStartingEvent event) {
		
	}

	@Override
	protected boolean verifyHandshake(CMessageHandshake handshakeMessage) {
		return false;
	}

	@Override
	protected void playerAccepted(Player client) {
		
	}

}
