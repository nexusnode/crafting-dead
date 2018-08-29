package com.craftingdead.mod.server;

import com.craftingdead.mod.common.network.packet.client.CPacketHandshake;
import com.craftingdead.mod.common.server.LogicalServer;
import com.craftingdead.mod.common.server.Player;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class DedicatedServer extends LogicalServer {

    @Override
    protected void onServerStart(FMLServerStartingEvent event) {

    }

    @Override
    protected boolean verifyHandshake(CPacketHandshake handshakeMessage) {
        return false;
    }

    @Override
    protected void playerAccepted(Player client) {

    }

}
