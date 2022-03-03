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

package com.craftingdead.immerse.client.gui.view;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.craftingdead.immerse.client.util.DownloadUtil;
import com.craftingdead.immerse.client.util.LoggingErrorHandler;
import net.minecraft.Util;
import net.minecraftforge.common.ForgeHooks;

public class ViewUtil {

  private static final Logger logger = LogUtils.getLogger();

  /**
   * Add all the {@link View}s specified in the passed {@link File}, spacing them evenly with
   * {@code flex: 1;}
   * 
   * @param file - the {@link File} to read {@link View}s from
   * @return ourself
   */
  public static void addAll(ParentView parentView, File file) {
    addAll(parentView, file, view -> view.setStyle("flex: 1;"));
  }

  /**
   * Add all the {@link View}s specified in the passed {@link File}.
   * 
   * @param file - the {@link File} to read {@link View}s from
   * @param configurer - a {@link Consumer} used to configure {@link View}s before they're added
   * @return ourself
   */
  public static void addAll(ParentView parentView, File file, Consumer<View> configurer) {
    DocumentBuilder builder;
    CompletableFuture<Document> documentFuture;
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setIgnoringElementContentWhitespace(true);
      builder = factory.newDocumentBuilder();
      builder.setErrorHandler(LoggingErrorHandler.INSTANCE);
      documentFuture = CompletableFuture.supplyAsync(() -> {
        try {
          return builder.parse(file);
        } catch (IOException | SAXException e) {
          logger.warn("Failed to parse xml {} {}", file.getAbsolutePath(), e);
          return null;
        }
      }, Util.backgroundExecutor());
    } catch (ParserConfigurationException e) {
      logger.warn("Failed to create document builder", e);
      return;
    }

    documentFuture.thenAcceptAsync(
        document -> parseDocument(document, file, parentView, configurer), parentView.minecraft);
  }

  private static void parseDocument(Document document, File file, ParentView parentView,
      Consumer<View> configurer) {
    NodeList nodes = document.getDocumentElement().getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      switch (node.getNodeName()) {
        case "text":
          final var text = node.getTextContent();

          var shadow = true;
          String scale = null;
          Node scaleNode = node.getAttributes().getNamedItem("style");
          if (scaleNode != null && scaleNode.getNodeValue() != null) {
            scale = "scale: " + scaleNode.getNodeValue() + ";";
          }

          Node shadowNode = node.getAttributes().getNamedItem("shadow");
          if (shadowNode != null && shadowNode.getNodeValue() != null) {
            try {
              shadow = Boolean.valueOf(shadowNode.getNodeValue());
            } catch (NumberFormatException | NullPointerException e) {
              logger.warn("Boolean expected for property 'shadow' in {}", file.getAbsolutePath());
            }
          }

          if (text != null) {
            var view = new TextView(new TextView.Properties<>())
                .setText(ForgeHooks.newChatWithLinks(text))
                .setShadow(shadow);
            var style = "width: 100%;";
            if (scale != null) {
              style += scale;
            }
            view.setStyle(style);
            parentView.addChild(view);
          }
          break;
        case "image":
          final String width;
          final String height;
          String url = null;
          String fitType = null;

          Node urlNode = node.getAttributes().getNamedItem("url");
          if (urlNode != null && urlNode.getNodeValue() != null) {
            url = urlNode.getNodeValue();
          } else {
            logger.warn("No URL declared for image in {}", file.getAbsolutePath());
            break;
          }

          Node widthNode = node.getAttributes().getNamedItem("width");
          if (widthNode != null && widthNode.getNodeValue() != null) {
            width = widthNode.getNodeValue();
          } else {
            width = null;
          }

          Node heightNode = node.getAttributes().getNamedItem("height");
          if (heightNode != null && heightNode.getNodeValue() != null) {
            height = heightNode.getNodeValue();
          } else {
            height = null;
          }

          if (width == null && height == null) {
            logger.warn("Both width and height cannot be empty for image in {}",
                file.getAbsolutePath());
            break;
          }

          Node fitNode = node.getAttributes().getNamedItem("fit");
          if (fitNode != null && fitNode.getNodeValue() != null) {
            fitType = fitNode.getNodeValue();
          }
          var view = new ImageView(new ImageView.Properties<>());
          view.setStyle("width: %s;height: %s;object-fit: %s;".formatted(width, height, fitType));
          parentView.addChild(view);
          DownloadUtil.downloadImageAsTexture(url)
              .thenAcceptAsync(result -> result.ifPresent(image -> {
                view.setImage(image);
                if (parentView.isAdded()) {
                  parentView.layout();
                }
              }), parentView.minecraft);
          break;
        default:
          break;
      }
    }

    if (parentView.isAdded()) {
      parentView.layout();
    }
  }
}
