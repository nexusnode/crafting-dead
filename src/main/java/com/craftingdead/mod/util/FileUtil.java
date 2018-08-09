package com.craftingdead.mod.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class FileUtil {

	public static ResourceLocation loadImageResource(BufferedImage image, ResourceLocation resourceLocation)
			throws IOException {
		TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
		ITextureObject object = texturemanager.getTexture(resourceLocation);
		if (object == null) {
			object = new DynamicTexture(image);
			texturemanager.loadTexture(resourceLocation, object);
		}
		return resourceLocation;
	}

	public static ByteBuffer readImage(InputStream imageInputStream) throws IOException {
		BufferedImage bufferedImage = ImageIO.read(imageInputStream);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "jpg", baos);
		return ByteBuffer.wrap(baos.toByteArray());
	}

	public static InputStream getResourceStream(ResourceLocation resourceLocation) {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		return classloader.getResourceAsStream("assets" + File.separator + resourceLocation.getResourceDomain()
				+ File.separator + resourceLocation.getResourcePath());
	}

}
