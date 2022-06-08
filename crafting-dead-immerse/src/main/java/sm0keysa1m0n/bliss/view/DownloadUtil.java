/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package sm0keysa1m0n.bliss.view;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.humbleui.skija.Image;
import net.minecraft.client.Minecraft;
import sm0keysa1m0n.bliss.Bliss;

public class DownloadUtil {

  private static final Logger logger = LoggerFactory.getLogger(DownloadUtil.class);

  private static final String USER_AGENT = "Bliss";

  public static CompletableFuture<Optional<Image>> downloadImage(String imageUrl) {
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
          return Optional.of(
              Image.makeFromEncoded(httpUrlConnection.getInputStream().readAllBytes()));
        }
      } catch (Throwable t) {
        logger.error("Couldn't download image", t);
      } finally {
        if (httpUrlConnection != null) {
          httpUrlConnection.disconnect();
        }
      }
      return Optional.empty();
    }, Bliss.instance().platform().backgroundExecutor());
  }
}
