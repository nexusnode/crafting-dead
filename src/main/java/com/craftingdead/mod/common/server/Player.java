package com.craftingdead.mod.common.server;

import com.craftingdead.mod.common.core.CraftingDead;
import com.craftingdead.mod.common.network.packet.PacketHandshake;
import com.recastproductions.network.packet.IPacket;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Wraps around the vanilla {@link EntityPlayerMP} so we can associate our own
 * data
 * 
 * @author Sm0keySa1m0n
 *
 */
public class Player {

	/**
	 * The vanilla entity
	 */
	private final EntityPlayerMP entity;
	/**
	 * The mods the player has installed on their client
	 */
	private String[] mods;
	/**
	 * The server instance used by this session
	 */
	private LogicalServer server;

	public Player(LogicalServer server, EntityPlayerMP entity, PacketHandshake handshakeMessage) {
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
