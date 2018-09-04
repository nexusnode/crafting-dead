package com.craftingdead.mod.client.renderer.loading;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.Drawable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.SharedDrawable;

import com.craftingdead.mod.common.core.CraftingDead;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class LoadingRenderer {

	private static final int TIMING_FRAME_COUNT = 200;
	private static final int TIMING_FRAME_THRESHOLD = TIMING_FRAME_COUNT * 5 * 1000000; // 5 ms per frame, scaled to //
																						// nanos
	private static final Lock lock = new ReentrantLock(true);
	private static final Semaphore mutex = new Semaphore(1);

	private static volatile boolean pause = false;
	private static volatile boolean running;

	private static boolean isDisplayVSyncForced = false;

	private static Drawable d;
	private static Thread renderThread;
	private static volatile Throwable threadError;

	public static void start(Minecraft mc, AbstractLoadingScreen gui) {
		if (running) {
			return;
		}
		gui.mc = mc;
		try {
			d = new SharedDrawable(Display.getDrawable());
			Display.getDrawable().releaseContext();
			d.makeCurrent();
		} catch (LWJGLException e) {
			return;
		}

		net.minecraftforge.fml.client.SplashProgress.getMaxTextureSize();

		renderThread = new Thread(new Runnable() {
			private long updateTiming;
			private long framecount;

			@Override
			public void run() {
				makeCurrent();
				running = true;
				while (running) {
					framecount++;

					if (gui != null) {
						gui.handleInput();

						RawScaledResolution res = new RawScaledResolution(2);

						GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
						GL11.glLoadIdentity();
						GlStateManager.viewport(0, 0, Display.getWidth(), Display.getHeight());
						GL11.glOrtho(0.0D, res.getScaledWidth(), res.getScaledHeight(), 0.0D, -1000.0D, 0.0D);

						gui.width = res.getScaledWidth();
						gui.height = res.getScaledHeight();

						final int mouseX = Mouse.getX() * res.getScaledWidth() / Display.getWidth();
						final int mouseY = res.getScaledHeight()
								- Mouse.getY() * res.getScaledHeight() / Display.getHeight() - 1;
						gui.drawScreen(mouseX, mouseY);
					}

					update();
					sync();
				}
				releaseContext();
			}

			private void update() {
				// We use mutex to indicate safely to the main thread that we're taking the
				// display global lock
				// So the main thread can skip processing messages while we're updating.
				// There are system setups where this call can pause for a while, because the GL
				// implementation
				// is trying to impose a framerate or other thing is occurring. Without the
				// mutex, the main
				// thread would delay waiting for the same global display lock
				mutex.acquireUninterruptibly();
				long updateStart = System.nanoTime();
				Display.update();
				// As soon as we're done, we release the mutex. The other thread can now ping
				// the processmessages
				// call as often as it wants until we get get back here again
				long dur = System.nanoTime() - updateStart;
				if (framecount < TIMING_FRAME_COUNT) {
					updateTiming += dur;
				}
				mutex.release();
				if (pause) {
					releaseContext();
					makeCurrent();
				}
			}

			private void sync() {
				// Such a hack - if the time taken is greater than 10 milliseconds, we're gonna
				// guess that we're on a
				// system where vsync is forced through the swapBuffers call - so we have to
				// force a sleep and let the
				// loading thread have a turn - some badly designed mods access Keyboard and
				// therefore GlobalLock.lock
				// during splash screen, and mutex against the above Display.update call as a
				// result.
				// 4 milliseconds is a guess - but it should be enough to trigger in most
				// circumstances. (Maybe if
				// 240FPS is possible, this won't fire?)
				if (framecount >= TIMING_FRAME_COUNT && updateTiming > TIMING_FRAME_THRESHOLD) {
					if (!isDisplayVSyncForced) {
						isDisplayVSyncForced = true;
						CraftingDead.LOGGER.info(
								"Using alternative sync timing : {} frames of Display.update took {} nanos",
								TIMING_FRAME_COUNT, updateTiming);
					}
					try {
						Thread.sleep(16);
					} catch (InterruptedException e) {
						;
					}
				} else {
					if (framecount == TIMING_FRAME_COUNT) {
						CraftingDead.LOGGER.info("Using sync timing. {} frames of Display.update took {} nanos",
								TIMING_FRAME_COUNT, updateTiming);
					}
					Display.sync(100);
				}
			}

			private void makeCurrent() {
				lock.lock();
				try {
					Display.getDrawable().makeCurrent();
				} catch (LWJGLException e) {
					CraftingDead.LOGGER.error("Error setting GL context:", e);
					throw new RuntimeException(e);
				}
				GL11.glClearColor(0, 0, 0, 1);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			}

			private void releaseContext() {
				Minecraft mc = Minecraft.getMinecraft();
				mc.displayWidth = Display.getWidth();
				mc.displayHeight = Display.getHeight();
				mc.resize(mc.displayWidth, mc.displayHeight);
				GL11.glClearColor(0, 0, 0, 1);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11.glDepthFunc(GL11.GL_LEQUAL);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glAlphaFunc(GL11.GL_GREATER, .1f);
				try {
					Display.getDrawable().releaseContext();
				} catch (LWJGLException e) {
					CraftingDead.LOGGER.error("Error releasing GL context:", e);
					throw new RuntimeException(e);
				} finally {
					lock.unlock();
				}
			}
		});

		renderThread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				CraftingDead.LOGGER.error("Loading thread Exception", e);
				threadError = e;
			}
		});

		renderThread.start();
		checkThreadState();
	}

	/**
	 * Call before you need to explicitly modify GL context state during loading.
	 * Resource loading doesn't usually require this call. Call {@link #resume()}
	 * when you're done.
	 * 
	 * @deprecated not a stable API, will break, don't use this yet
	 */
	@Deprecated
	public static void pause() {
		if (!running)
			return;
		checkThreadState();
		pause = true;
		lock.lock();
		try {
			d.releaseContext();
			Display.getDrawable().makeCurrent();
		} catch (LWJGLException e) {
			CraftingDead.LOGGER.error("Error setting GL context:", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * @deprecated not a stable API, will break, don't use this yet
	 */
	@Deprecated
	public static void resume() {
		if (!running)
			return;
		checkThreadState();
		pause = false;
		try {
			Display.getDrawable().releaseContext();
			d.makeCurrent();
		} catch (LWJGLException e) {
			CraftingDead.LOGGER.error("Error releasing GL context:", e);
			throw new RuntimeException(e);
		}
		lock.unlock();
	}

	public static void finish() {
		if (!running)
			return;
		try {
			checkThreadState();
			running = false;
			renderThread.join();
			GL11.glFlush(); // process any remaining GL calls before releaseContext (prevents missing
			// textures on mac)
			d.releaseContext();
			Display.getDrawable().makeCurrent();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void checkThreadState() {
		if (renderThread.getState() == Thread.State.TERMINATED || threadError != null) {
			throw new IllegalStateException("Loading thread", threadError);
		}
	}

}
