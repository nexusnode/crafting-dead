package com.craftingdead.mod.common.server;

import com.craftingdead.mod.common.core.CraftingDead;
import com.craftingdead.mod.common.network.packet.client.CPacketHandshake;
import com.recastproductions.network.packet.IPacket;

import net.minecraft.entity.player.EntityPlayerMP;

public class Player {

    private final EntityPlayerMP entity;
    private String[] mods;
    private LogicalServer server;

    public Player(LogicalServer server, EntityPlayerMP entity, CPacketHandshake handshakeMessage) {
        this.server = server;
        this.entity = entity;
        this.mods = handshakeMessage.getMods();
    }

    public EntityPlayerMP getVanillaEntity() {
        return entity;
    }

    public String[] getMods() {
        return this.mods;
    }

    public void sendPacket(IPacket packet) {
        CraftingDead.instance().getNetworkWrapper().sendTo(packet, this.getVanillaEntity());
    }

    public LogicalServer getLogicalServer() {
        return this.server;
    }

}
