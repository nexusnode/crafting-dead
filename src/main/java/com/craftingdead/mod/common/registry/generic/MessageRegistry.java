package com.craftingdead.mod.common.registry.generic;

import com.craftingdead.mod.common.multiplayer.message.MessageTriggerItem;
import com.craftingdead.mod.common.multiplayer.message.MessageUpdateStatistics;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Place to register all internal messages
 * 
 * @author Sm0keySa1m0n
 *
 */
public class MessageRegistry {

	private static int currentDiscriminator = -1;

	public static void registerMessages(SimpleNetworkWrapper networkWrapper) {
		networkWrapper.registerMessage(MessageUpdateStatistics.MessageHandlerUpdateStatistics.class,
				MessageUpdateStatistics.class, currentDiscriminator++, Side.CLIENT);
		networkWrapper.registerMessage(MessageTriggerItem.MessageHandlerItemTrigger.class, MessageTriggerItem.class,
				currentDiscriminator++, Side.SERVER);
	}

}
