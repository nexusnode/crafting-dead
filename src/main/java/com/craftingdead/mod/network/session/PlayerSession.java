package com.craftingdead.mod.network.session;

import java.util.UUID;

import com.craftingdead.mod.client.ClientProxy;
import com.craftingdead.mod.common.CraftingDead;
import com.craftingdead.mod.network.ConnectionState;
import com.craftingdead.mod.network.message.handshake.MessageHandshake;
import com.craftingdead.mod.network.message.login.MessagePlayerLogin;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;

import net.minecraft.client.Minecraft;
import sm0keysa1m0n.network.wrapper.NetworkManager;
import sm0keysa1m0n.network.wrapper.Session;

public class PlayerSession implements Session {

	private final ClientProxy client;

	public PlayerSession(ClientProxy client) {
		this.client = client;
	}

	@Override
	public void sessionActive(NetworkManager networkManager) {
		networkManager.sendMessage(new MessageHandshake(ConnectionState.LOGIN));
		Minecraft mc = client.getMinecraft();
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
		String version = CraftingDead.instance().getMetadata().version;
		networkManager.sendMessage(new MessagePlayerLogin(accessToken, clientToken, username, uuid, version));
	}

	@Override
	public void sessionInactive(NetworkManager networkManager) {
		CraftingDead.LOGGER.info("Disconnected from the Crafting Dead network");
	}

	@Override
	public void exceptionCaught(NetworkManager networkManager, Throwable t) throws Exception {
		CraftingDead.LOGGER.error("Exception in session", t);
	}

	public ClientProxy getClient() {
		return this.client;
	}

}