package com.craftingdead.network.modclient;

import java.util.UUID;

import com.craftingdead.mod.client.ModClient;
import com.craftingdead.mod.core.CraftingDead;
import com.craftingdead.network.packet.handshake.client.CPacketHandshakeModClient;
import com.craftingdead.network.packet.modclient.server.SPacketNews;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import com.recastproductions.network.client.NetClientHandler;

import io.netty.channel.Channel;
import net.minecraft.client.Minecraft;

public class NetClientHandlerModClient extends NetClientHandler<CPacketHandshakeModClient, SessionModClient> {

	private final ModClient modClient;

	private SessionModClient session;

	public NetClientHandlerModClient(ModClient modClient) {
		this.modClient = modClient;
	}

	@Override
	public SessionModClient newSession(Channel ch) {
		return new SessionModClient(ch, this, modClient);
	}

	@Override
	public String getName() {
		return "mod_client";
	}

	@Override
	public CPacketHandshakeModClient getHandshakePacket() {
		Minecraft mc = Minecraft.getMinecraft();
		String accessToken = mc.getSession().getToken();
		String clientToken = ((YggdrasilAuthenticationService) ((YggdrasilMinecraftSessionService) mc
				.getSessionService()).getAuthenticationService()).getClientToken();
		String username = mc.getSession().getUsername();
		UUID uuid = null;
		try {
			UUID.fromString(mc.getSession().getPlayerID());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		String version = CraftingDead.MOD_VERSION;
		return new CPacketHandshakeModClient(accessToken, clientToken, username, uuid, version);
	}

	@Override
	protected void registerPackets() {
		this.registerPacket(0, SPacketNews.class, new SPacketNews.PacketHandlerSPacketNews());
	}

	public SessionModClient getSession() {
		return this.session;
	}

}
