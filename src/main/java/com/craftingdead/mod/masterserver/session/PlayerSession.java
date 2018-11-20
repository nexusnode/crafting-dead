package com.craftingdead.mod.masterserver.session;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.client.ClientMod;
import com.craftingdead.mod.masterserver.ConnectionState;
import com.craftingdead.mod.masterserver.message.handshake.MessageHandshake;
import com.craftingdead.mod.masterserver.message.login.MessagePlayerLogin;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;

import net.minecraft.client.Minecraft;
import sm0keysa1m0n.network.wrapper.NetworkManager;
import sm0keysa1m0n.network.wrapper.Session;

public class PlayerSession implements Session {

	private static final Logger LOGGER = LogManager.getLogger();

	private final ClientMod client;

	public PlayerSession(ClientMod client) {
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
			LOGGER.warn("Bad UUID, could be in offline mode?");
		}
		String version = CraftingDead.instance().getMetadata().version;
		networkManager.sendMessage(new MessagePlayerLogin(accessToken, clientToken, username, uuid, version));
	}

	@Override
	public void sessionInactive(NetworkManager networkManager) {
		LOGGER.info("Disconnected from the Crafting Dead network");
	}

	@Override
	public void exceptionCaught(NetworkManager networkManager, Throwable t) throws Exception {
		LOGGER.error("Exception in session", t);
	}

	public ClientMod getClient() {
		return this.client;
	}

}