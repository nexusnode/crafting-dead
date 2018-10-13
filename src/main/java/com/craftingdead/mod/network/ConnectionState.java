package com.craftingdead.mod.network;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.craftingdead.mod.network.message.handshake.MessageHandshake;
import com.craftingdead.mod.network.message.login.MessagePlayerLogin;
import com.craftingdead.mod.network.message.player.MessageUpdateNews;
import com.craftingdead.mod.network.message.server.MessageKilledPlayer;
import com.craftingdead.mod.network.message.server.MessageKilledZombie;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

import sm0keysa1m0n.network.message.Message;
import sm0keysa1m0n.network.message.MessageHandler;
import sm0keysa1m0n.network.message.MessageIndex;

public enum ConnectionState implements MessageIndex {

	HANDSHAKE(0) {
		{
			this.registerMessage(0x00, MessageHandshake.class);
		}
	},
	LOGIN(1) {
		{
			this.registerMessage(0x00, MessagePlayerLogin.class);
		}
	},
	PLAYER(2) {
		{
			this.registerMessage(0x00, MessageUpdateNews.class, new MessageUpdateNews.MessageHandlerUpdateNews());
		}
	},
	SERVER(3) {
		{
			this.registerMessage(0x00, MessageKilledZombie.class);
			this.registerMessage(0x01, MessageKilledPlayer.class);
		}
	};

	private static final Logger LOGGER = LogManager.getLogger();

	private final BiMap<Integer, Class<? extends Message>> messages = HashBiMap.create();

	private final Map<Class<? extends Message>, MessageHandler<?, ?>> handlers = Maps.newHashMap();

	private final int id;

	private ConnectionState(int id) {
		this.id = id;
	}

	public void registerMessage(Integer discriminator, Class<? extends Message> msg) {
		if (messages.containsKey(discriminator)) {
			LOGGER.fatal("A message with the discriminator {} already exists!", discriminator);
		} else {
			messages.put(discriminator, msg);
		}
	}

	public <MSG extends Message> void registerMessage(Integer discriminator, Class<MSG> msg,
			MessageHandler<MSG, ?> handler) {
		this.registerMessage(discriminator, msg);
		this.handlers.put(msg, handler);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <MSG extends Message> MessageHandler<MSG, ?> getMessageHandler(Class<MSG> msg) {
		return (MessageHandler<MSG, ?>) handlers.get(msg);
	}

	@Override
	public Integer getDiscriminator(Class<? extends Message> msg) {
		return messages.inverse().get(msg);
	}

	@Override
	public Class<? extends Message> getMessage(Integer discriminator) {
		return messages.get(discriminator);
	}

	public int getId() {
		return this.id;
	}

}
