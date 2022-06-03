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

package com.craftingdead.immerse.client.gui.view;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import com.craftingdead.immerse.client.util.DownloadUtil;
import com.craftingdead.immerse.client.util.LoggingErrorHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.StringDecomposer;
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
    addAll(parentView, file, view -> view.getStyle().setStyle("flex: 1;"));
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
    var nodes = document.getDocumentElement().getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      var node = nodes.item(i);
      switch (node.getNodeName()) {
        case "text":
          final var text = node.getTextContent();

          if (text != null) {
            var builder = new ComponentBuilder();
            StringDecomposer.iterateFormatted(ForgeHooks.newChatWithLinks(text),
                Style.EMPTY, builder);
            var view = new TextView(new View.Properties()).setText(builder.getComponent());

            Node styleNode = node.getAttributes().getNamedItem("style");
            if (styleNode != null && styleNode.getNodeValue() != null) {
              view.getStyle().setStyle(styleNode.getNodeValue());
            }

            view.getStyle().width.set(Length.percentage(100.0F));
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
          var view = new ImageView(new View.Properties());
          view.getStyle().setStyle(
              "width: %s; height: %s; object-fit: %s;".formatted(width, height, fitType));
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

  private static class ComponentBuilder implements FormattedCharSink {

    private MutableComponent component = TextComponent.EMPTY.copy();

    private StringBuilder builder;
    private Style style;

    @Override
    public boolean accept(int index, Style style, int codePoint) {
      if (this.style != style) {
        if (this.builder != null) {
          this.component.append(new TextComponent(this.builder.toString()).setStyle(this.style));
        }

        this.builder = new StringBuilder();
        this.style = style;
      }
      this.builder.append((char) codePoint);
      return true;
    }

    public Component getComponent() {
      return this.component;
    }
  }
}
