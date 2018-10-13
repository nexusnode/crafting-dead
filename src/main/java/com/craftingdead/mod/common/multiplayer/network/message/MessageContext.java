package com.craftingdead.mod.common.multiplayer.network.message;

import com.craftingdead.mod.common.Proxy;

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
public class MessageContext {

	/**
	 * The {@link INetHandler} for this message. It could be a client or server
	 * handler, depending on the {@link #side} received.
	 */
	private final INetHandler netHandler;

	/**
	 * The {@link Side} this message has been received on
	 */
	private final Side side;

	private final Proxy mod;

	public MessageContext(INetHandler netHandler, Side side, Proxy mod) {
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

	public Proxy getMod() {
		return this.mod;
	}

}
