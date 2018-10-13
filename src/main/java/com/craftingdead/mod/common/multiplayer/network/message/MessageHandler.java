package com.craftingdead.mod.common.multiplayer.network.message;

import sm0keysa1m0n.network.message.Message;

public interface MessageHandler<REQ extends Message, REPLY extends Message> {

	REPLY processMessage(REQ msg, MessageContext ctx);

}
