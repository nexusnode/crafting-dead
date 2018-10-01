package com.craftingdead.mod.common.multiplayer.network.packet;

import com.craftingdead.mod.common.IMod;
import com.recastproductions.network.packet.IPacketContext;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.relauncher.Side;

/**
 * The context when receiving packets
 * 
 * @author Sm0keySa1m0n
 *
 */
public class PacketContextMod implements IPacketContext {

	/**
	 * The {@link INetHandler} for this message. It could be a client or server
	 * handler, depending on the {@link #side} received.
	 */
	private final INetHandler netHandler;

	/**
	 * The {@link Side} this message has been received on
	 */
	private final Side side;

	private final IMod<?> mod;

	public PacketContextMod(INetHandler netHandler, Side side, IMod<?> mod) {
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

	public IMod<?> getMod() {
		return this.mod;
	}

}
