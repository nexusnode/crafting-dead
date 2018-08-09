package com.craftingdead.mod.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import net.minecraft.util.ResourceLocation;

public class DownloadUtil {

	private static ArrayList<URL> currentlyDownloading = new ArrayList<URL>();

	/**
	 * Downloads an image from the specified {@link URL} and loads it into the specified {@link ResourceLocation}
	 * @param url - the {@link URL} to the image
	 * @param resourceLocation - the {@link ResourceLocation} to save the image to
	 * @return the {@link ResourceLocation} passed in
	 */
	public static ResourceLocation loadImageAsync(URL url, ResourceLocation resourceLocation) {
		if (!currentlyDownloading.contains(url)) {
			currentlyDownloading.add(url);
		} else {
			return resourceLocation;
		}

		new Thread() {
			@Override
			public void run() {
				try {
					BufferedImage avatar = ImageIO.read(url);
					FileUtil.loadImageResource(avatar, resourceLocation);
					currentlyDownloading.remove(url);
				} catch (IOException e) {
					;
				}
			}
		}.run();

		return resourceLocation;
	}

}
