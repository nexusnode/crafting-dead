package com.craftingdead.mod.common.registry.generic;

import com.craftingdead.mod.common.multiplayer.network.NetworkWrapper;
import com.craftingdead.mod.common.multiplayer.network.message.MessageUpdateStatistics;
import com.craftingdead.mod.common.multiplayer.network.message.MessageUpdateStatistics.MessageHandlerUpdateStatistics;

import net.minecraftforge.fml.relauncher.Side;

/**
 * Place to register all internal messages
 * 
 * @author Sm0keySa1m0n
 *
 */
public class MessageRegistry {

	private static int currentDiscriminator = -1;

	public static void registerMessages(NetworkWrapper wrapper) {
		wrapper.registerMessage(MessageHandlerUpdateStatistics.class, MessageUpdateStatistics.class,
				currentDiscriminator++, Side.CLIENT);
	}

}
