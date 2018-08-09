package com.craftingdead.mod.player;

import com.craftingdead.mod.LogicalServer;
import com.craftingdead.mod.core.CraftingDead;
import com.craftingdead.mod.network.message.client.CMessageHandshake;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class Player {

	private final EntityPlayerMP entity;
	private String[] mods;
	private LogicalServer server;

	public Player(LogicalServer server, EntityPlayerMP entity, CMessageHandshake handshakeMessage) {
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

	public void sendMessage(IMessage message) {
		CraftingDead.NETWORK_WRAPPER.sendTo(message, this.getVanillaEntity());
	}

	public LogicalServer getLogicalServer() {
		return this.server;
	}

}
