package com.craftingdead.mod;

import java.io.File;
import java.util.Map;
import java.util.function.Supplier;

import com.craftingdead.mod.client.ClientDist;
import com.craftingdead.mod.server.LogicalServer;
import com.craftingdead.mod.server.dedicated.ServerDist;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Implemented by each side of the game, one for the client and one for the
 * server
 * 
 * @author Sm0keySa1m0n
 *
 */
public interface ModDist {

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
	 * Called when a remote network connection is offered.
	 * 
	 * @param mods - all mods and their versions on the remote host
	 * @param side - the side of the remote host
	 * @return if the remote host is acceptable
	 */
	default boolean networkCheck(Map<String, String> mods, Side side) {
		return true;
	}

	/**
	 * Signals the shutdown of the game, do any shutdown tasks here.<br>
	 * <b>Runs in a separate shutdown thread.<b>
	 */
	default void shutdown() {
		;
	}

	/**
	 * Get a {@link Supplier} for the {@link LogicalServer} associated with this
	 * side.
	 * 
	 * @return the {@link Supplier}
	 */
	Supplier<? extends LogicalServer> getLogicalServerSupplier();

	/**
	 * Gets the folder associated with the current side.
	 * 
	 * @return the {@link File} instance
	 */
	default File getSidedFolder() {
		return new File(CraftingDead.instance().getModFolder(), FMLLaunchHandler.side().name());
	}

	/**
	 * Casts the {@link ModDist} instance to a {@link ClientDist} instance.<br>
	 * <b>Throws an exception if called from the incorrect side.
	 * 
	 * @return the instance
	 */
	default ClientDist getClientDist() {
		if (this instanceof ClientDist)
			return (ClientDist) this;
		else
			throw new RuntimeException("Accessing physical client on wrong side");
	}

	/**
	 * Casts the {@link ModDist} instance to a {@link ServerDist} instance.<br>
	 * <b>Throws an exception if called from the incorrect side.
	 * 
	 * @return the instance
	 */
	default ServerDist getServerDist() {
		if (this instanceof ServerDist)
			return (ServerDist) this;
		else
			throw new RuntimeException("Accessing physical server on wrong side");
	}

}
