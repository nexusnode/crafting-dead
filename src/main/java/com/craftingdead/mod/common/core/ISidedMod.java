package com.craftingdead.mod.common.core;

import com.craftingdead.mod.common.multiplayer.LogicalServer;
import com.recastproductions.network.impl.client.NetClientHandler;

/**
 * Implemented by each side of the game, one for the client and one for the
 * server
 * 
 * @author Sm0keySa1m0n
 *
 * @param <L> - the {@link LogicalServer} associated with this side
 */
public interface ISidedMod<T extends ISidedMod<T, H>, H extends NetClientHandler<?, ?>> {

	/**
	 * Called before minecraft is loaded, do any pre loading here
	 * 
	 * @param mod - the {@link CraftingDead} instance
	 */
	void setup(CraftingDead<T> mod);

	/**
	 * Get the {@link LogicalServer} associated with this side
	 * 
	 * @return the server instance
	 */
	Class<? extends LogicalServer<T>> getLogicalServer();

	/**
	 * Signals the shutdown of the game, do any shutdown tasks here, <b>runs in
	 * separate shutdown thread<b>
	 */
	void shutdown();

	/**
	 * Get the {@link NetClientHandler} associated with this side
	 * 
	 * @return
	 */
	H getNetHandler();

}
