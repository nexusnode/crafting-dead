package com.craftingdead.mod.common.network;

import com.craftingdead.mod.client.ModClient;
import com.craftingdead.mod.common.core.ISidedMod;
import com.craftingdead.mod.server.ModServer;
import com.recastproductions.network.packet.IPacketContext;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.relauncher.Side;

public class CraftingDeadContext implements IPacketContext {

	/**
	 * The {@link INetHandler} for this message. It could be a client or server
	 * handler, depending on the {@link #side} received.
	 */
	private final INetHandler netHandler;

	/**
	 * The {@link Side} this message has been received on
	 */
	private final Side side;

	private final ISidedMod<?> mod;

	public CraftingDeadContext(INetHandler netHandler, Side side, ISidedMod<?> mod) {
		this.netHandler = netHandler;
		this.side = side;
		this.mod = mod;
	}

	public NetHandlerPlayServer getServerHandler() {
		return (NetHandlerPlayServer) netHandler;
	}

	public NetHandlerPlayClient getClientHandler() {
		return (NetHandlerPlayClient) netHandler;
	}

	public Side getSide() {
		return this.side;
	}

	public ModClient getModClient() {
		if (mod instanceof ModClient)
			return (ModClient) mod;
		else
			throw new RuntimeException("Accessing physical client on wrong side");
	}

	public ModServer getModServer() {
		if (mod instanceof ModServer)
			return (ModServer) mod;
		else
			throw new RuntimeException("Accessing physical server on wrong side");
	}

}
