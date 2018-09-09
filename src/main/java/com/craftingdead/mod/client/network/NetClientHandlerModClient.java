package com.craftingdead.mod.client.network;

import com.craftingdead.mod.client.ModClient;
import com.craftingdead.mod.client.network.packet.PacketUpdateNews;
import com.craftingdead.mod.common.core.CraftingDead;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import com.recastproductions.network.impl.client.NetClientHandler;
import io.netty.channel.Channel;
import net.minecraft.client.Minecraft;

import java.util.UUID;

public class NetClientHandlerModClient extends NetClientHandler<PacketHandshakeModClient, SessionModClient> {

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
	public PacketHandshakeModClient getHandshakePacket() {
		Minecraft mc = Minecraft.getMinecraft();
		String accessToken = mc.getSession().getToken();
		String clientToken = ((YggdrasilAuthenticationService) ((YggdrasilMinecraftSessionService) mc
				.getSessionService()).getAuthenticationService()).getClientToken();
		String username = mc.getSession().getUsername();
		UUID uuid = null;
		try {
			UUID.fromString(mc.getSession().getPlayerID());
		} catch (IllegalArgumentException e) {
			CraftingDead.LOGGER.warn("Bad UUID, could be in offline mode?");
		}
		String version = CraftingDead.MOD_VERSION;
		return new PacketHandshakeModClient(accessToken, clientToken, username, uuid, version);
	}

	@Override
	protected void registerPackets() {
		this.registerPacket(0, PacketUpdateNews.class, new PacketUpdateNews.PacketHandlerUpdateNews());
	}

	public SessionModClient getSession() {
		return this.session;
	}

}
