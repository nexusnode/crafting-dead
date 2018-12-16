package com.craftingdead.mod.util;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;

public final class DistExecutor {

	private DistExecutor() {
	}

	/**
	 * Run the callable in the supplier only on the specified {@link Side}
	 *
	 * @param dist  The dist to run on
	 * @param toRun A supplier of the callable to run (Supplier wrapper to ensure
	 *              classloading only on the appropriate dist)
	 * @param       <T> The return type from the callable
	 * @return The callable's result
	 */
	public static <T> T callWhenOn(Side dist, Supplier<Callable<T>> toRun) {
		if (dist == FMLLaunchHandler.side()) {
			try {
				return toRun.get().call();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	public static void runWhenOn(Side dist, Supplier<Runnable> toRun) {
		if (dist == FMLLaunchHandler.side()) {
			toRun.get().run();
		}
	}

	public static <T> T runForDist(Supplier<Supplier<T>> clientTarget, Supplier<Supplier<T>> serverTarget) {
		switch (FMLLaunchHandler.side()) {
		case CLIENT:
			return clientTarget.get().get();
		case SERVER:
			return serverTarget.get().get();
		default:
			throw new IllegalArgumentException("UNSIDED?");
		}
	}
}