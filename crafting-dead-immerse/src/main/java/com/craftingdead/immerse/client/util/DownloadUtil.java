/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.client.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import com.craftingdead.immerse.CraftingDeadImmerse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.SharedConstants;
import net.minecraft.Util;

public class DownloadUtil {

  private static final Logger logger = LogUtils.getLogger();

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
