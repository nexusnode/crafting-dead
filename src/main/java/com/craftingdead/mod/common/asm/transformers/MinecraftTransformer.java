package com.craftingdead.mod.common.asm.transformers;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.PixelFormat;

import com.craftingdead.asm.Callback;
import com.craftingdead.asm.CallbackInjectionTransformer;
import com.craftingdead.mod.common.core.CraftingDead;

import net.minecraft.client.Minecraft;

/**
 * Transforms any methods inside {@link Minecraft}
 * 
 * @author Sm0keySa1m0n
 *
 */
public class MinecraftTransformer extends CallbackInjectionTransformer {

	private static final String[] ICON_LOCATIONS = new String[] {
			"assets/craftingdead/textures/gui/icons/icon_16x16.png",
			"assets/craftingdead/textures/gui/icons/icon_32x32.png",
			"assets/craftingdead/textures/gui/icons/icon_64x64.png" };

	@Override
	protected void addCallbacks() {
		addCallback("net.minecraft.client.Minecraft", "createDisplay", "()V",
				new Callback(Callback.CallbackType.REDIRECT, "createDisplay", this.getClass().getCanonicalName()));
	}

	public static void createDisplay(Minecraft mc) throws LWJGLException {
		mc.gameSettings.guiScale = 2;
		Display.setResizable(true);
		Display.setTitle(CraftingDead.MOD_NAME);

		List<ByteBuffer> icons = new ArrayList<ByteBuffer>();
		for (String location : ICON_LOCATIONS) {
			try {
				ClassLoader classloader = Thread.currentThread().getContextClassLoader();
				InputStream inputstream = classloader.getResourceAsStream(location);
				icons.add(readImageToBuffer(inputstream));
				inputstream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Display.setIcon(icons.toArray(new ByteBuffer[0]));

		try {
			Display.create((new PixelFormat()).withDepthBits(24));
		} catch (LWJGLException lwjglexception) {
			CraftingDead.LOGGER.error("Couldn't set pixel format", (Throwable) lwjglexception);

			try {
				Thread.sleep(1000L);
			} catch (InterruptedException var3) {
				;
			}

			if (mc.isFullScreen()) {
				mc.updateDisplayMode();
			}

			Display.create();
		}
	}

	private static ByteBuffer readImageToBuffer(InputStream imageStream) throws IOException {
		BufferedImage bufferedimage = ImageIO.read(imageStream);
		int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), (int[]) null, 0,
				bufferedimage.getWidth());
		ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);

		for (int i : aint) {
			bytebuffer.putInt(i << 8 | i >> 24 & 255);
		}

		bytebuffer.flip();
		return bytebuffer;
	}

}
