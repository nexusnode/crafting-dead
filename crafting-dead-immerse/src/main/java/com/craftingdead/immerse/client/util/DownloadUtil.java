/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.immerse.client.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.immerse.CraftingDeadImmerse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.Util;

public class DownloadUtil {

  private static final Logger logger = LogManager.getLogger();

  private static final String USER_AGENT =
      "Minecraft Java/" + SharedConstants.getCurrentVersion().getName();

  public static CompletableFuture<Optional<ResourceLocation>> downloadImageAsTexture(
      String imageUrl) {
    return downloadImage(imageUrl).thenApplyAsync(result -> result.map(image -> {
      TextureManager textureManager = Minecraft.getInstance().getTextureManager();
      ResourceLocation textureLocation =
          new ResourceLocation(CraftingDeadImmerse.ID, String.valueOf(imageUrl.hashCode()));
      textureManager.register(textureLocation, new DynamicTexture(image));
      return textureLocation;
    }), Minecraft.getInstance());
  }

  public static CompletableFuture<Optional<NativeImage>> downloadImage(String imageUrl) {
    return CompletableFuture.supplyAsync(() -> {
      HttpURLConnection httpUrlConnection = null;
      logger.debug("Downloading image from {} to {}", imageUrl);
      try {
        httpUrlConnection = (HttpURLConnection) new URL(imageUrl)
            .openConnection(Minecraft.getInstance().getProxy());
        httpUrlConnection.setRequestProperty("User-Agent", USER_AGENT);
        httpUrlConnection.setDoInput(true);
        httpUrlConnection.setDoOutput(false);
        httpUrlConnection.connect();
        if (httpUrlConnection.getResponseCode() / 100 == 2) {
          return Optional.of(NativeImage.read(httpUrlConnection.getInputStream()));
        }
      } catch (Throwable t) {
        logger.error("Couldn't download image", t);
      } finally {
        if (httpUrlConnection != null) {
          httpUrlConnection.disconnect();
        }
      }
      return Optional.empty();
    }, Util.backgroundExecutor());
  }
}
