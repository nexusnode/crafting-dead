package com.craftingdead.mod.common.multiplayer.network.packet;

import com.craftingdead.mod.client.ClientMod;
import com.craftingdead.mod.common.IMod;
import com.craftingdead.mod.server.ServerMod;
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

	private final IMod<?, ?> mod;

	public PacketContextMod(INetHandler netHandler, Side side, IMod<?, ?> mod) {
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

	public IMod<?, ?> getMod() {
		return this.mod;
	}

	public ClientMod getModClient() {
		if (mod instanceof ClientMod)
			return (ClientMod) mod;
		else
			throw new RuntimeException("Accessing physical client on wrong side");
	}

	public ServerMod getModServer() {
		if (mod instanceof ServerMod)
			return (ServerMod) mod;
		else
			throw new RuntimeException("Accessing physical server on wrong side");
	}

}
