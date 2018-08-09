package com.craftingdead.mod.client;

import com.craftingdead.mod.LogicalServer;
import com.craftingdead.mod.network.message.client.CMessageHandshake;
import com.craftingdead.mod.player.Player;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class IntegratedServer extends LogicalServer {


	@Override
	protected void onServerStart(FMLServerStartingEvent event) {
		
	}

	@Override
	protected boolean verifyHandshake(CMessageHandshake handshakeMessage) {
		return true;
	}

	@Override
	protected void playerAccepted(Player player) {
		
	}

}
