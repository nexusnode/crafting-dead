package com.craftingdead.mod.common;

import com.craftingdead.mod.client.ModClient;
import com.craftingdead.mod.common.multiplayer.LogicalServer;
import com.craftingdead.mod.server.ModServer;
import com.recastproductions.network.impl.client.NetClientHandler;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Implemented by each side of the game, one for the client and one for the
 * server
 * 
 * @author Sm0keySa1m0n
 *
 * @param <L> - the {@link LogicalServer} associated with this side
 */
public interface IMod<H extends NetClientHandler<?, ?>> {

	/**
	 * Called before {@link FMLInitializationEvent} during mod startup. This is the
	 * first of three commonly called events during mod initialization.
	 */
	default void preInitialization(FMLPreInitializationEvent event) {
		;
	}

	/**
	 * Called after {@link FMLPreInitializationEvent} and before
	 * {@link FMLPostInitializationEvent} during mod startup. This is the second of
	 * three commonly called events during mod initialization.
	 */
	default void initialization(FMLInitializationEvent event) {
		;
	}

	/**
	 * Called after {@link FMLInitializationEvent} has been dispatched on every mod.
	 * This is the third and last commonly called event during mod initialization.
	 */
	default void postInitialization(FMLPostInitializationEvent event) {
		;
	}

	/**
	 * Indicates that loading is complete.
	 */
	default void loadComplete(FMLLoadCompleteEvent event) {
		;
	}

	/**
	 * Signals the shutdown of the game, do any shutdown tasks here, <b>runs in
	 * separate shutdown thread<b>
	 */
	default void shutdown() {
		;
	}

	/**
	 * Get the {@link LogicalServer} associated with this side
	 * 
	 * @return the server instance
	 */
	Class<? extends LogicalServer> getLogicalServer();

	/**
	 * Get the {@link NetClientHandler} associated with this side
	 * 
	 * @return
	 */
	H getNetHandler();

	default ModClient getModClient() {
		if (this instanceof ModClient)
			return (ModClient) this;
		else
			throw new RuntimeException("Accessing physical client on wrong side");
	}

	default ModServer getModServer() {
		if (this instanceof ModServer)
			return (ModServer) this;
		else
			throw new RuntimeException("Accessing physical server on wrong side");
	}

}
