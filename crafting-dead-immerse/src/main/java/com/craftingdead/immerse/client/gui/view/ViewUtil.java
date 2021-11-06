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

package com.craftingdead.immerse.client.gui.view;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayout;
import com.craftingdead.immerse.client.util.DownloadUtil;
import com.craftingdead.immerse.client.util.FitType;
import com.craftingdead.immerse.client.util.LoggingErrorHandler;
import com.google.common.base.Strings;
import com.mojang.datafixers.util.Either;
import net.minecraft.util.Util;
import net.minecraftforge.common.ForgeHooks;

public class ViewUtil {

  private static final Logger logger = LogManager.getLogger();

  /**
   * Add all the {@link View}s specified in the passed {@link File}, spacing them evenly with
   * {@code flex: 1;}
   * 
   * @param file - the {@link File} to read {@link View}s from
   * @return ourself
   */
  public static <T extends ParentView<?, ?, YogaLayout>> T addAll(T parentView, File file) {
    return addAll(parentView, file, c -> c.getLayout().setFlex(1.0F));
  }

  /**
   * Add all the {@link View}s specified in the passed {@link File}.
   * 
   * @param file - the {@link File} to read {@link View}s from
   * @param configurer - a {@link Consumer} used to configure {@link View}s before they're added
   * @return ourself
   */
  public static <T extends ParentView<?, ?, YogaLayout>> T addAll(T parentView, File file,
      Consumer<View<?, YogaLayout>> configurer) {
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
      return parentView;
    }

    documentFuture.thenAcceptAsync(
        document -> parseDocument(document, file, parentView, configurer), parentView.minecraft);

    return parentView;
  }

  private static <T extends ParentView<?, ?, YogaLayout>> void parseDocument(Document document,
      File file, T parentView, Consumer<View<?, YogaLayout>> configurer) {
    NodeList nodes = document.getDocumentElement().getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      switch (node.getNodeName()) {
        case "text":
          String text = node.getTextContent();
          boolean shadow = true;
          float scale = 1.0F;
          Node scaleNode = node.getAttributes().getNamedItem("scale");
          if (scaleNode != null && scaleNode.getNodeValue() != null) {
            try {
              scale = Float.valueOf(scaleNode.getNodeValue());
            } catch (NumberFormatException | NullPointerException e) {
              logger.warn("Float expected for property 'scale' in {}", file.getAbsolutePath());
            }
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
            View<?, YogaLayout> view = new TextView<>(
                new YogaLayout().setWidthPercent(100.0F),
                ForgeHooks.newChatWithLinks(text))
                    .setShadow(shadow);

            view.getScaleProperty().setBaseValue(scale);

            parentView.addChild(view);
          }
          break;
        case "image":
          final Either<Integer, Float> width;
          final Either<Integer, Float> height;
          String url = null;
          FitType fitType = FitType.FILL;

          Node urlNode = node.getAttributes().getNamedItem("url");
          if (urlNode != null && urlNode.getNodeValue() != null) {
            url = urlNode.getNodeValue();
          } else {
            logger.warn("No URL declared for image in {}", file.getAbsolutePath());
            break;
          }

          Node widthNode = node.getAttributes().getNamedItem("width");
          if (widthNode != null && widthNode.getNodeValue() != null) {
            String widthString = widthNode.getNodeValue();
            width = extractSize(widthString);
            if (width == null) {
              logger.warn("Invalid width '{}' for image in {}", widthString,
                  file.getAbsolutePath());
            }
          } else {
            width = null;
          }

          Node heightNode = node.getAttributes().getNamedItem("height");
          if (heightNode != null && heightNode.getNodeValue() != null) {
            String heightString = heightNode.getNodeValue();
            height = extractSize(heightString);
            if (height == null) {
              logger.warn("Invalid height '{}' for image in {}", heightString,
                  file.getAbsolutePath());
            }
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
            try {
              fitType = FitType.valueOf(fitNode.getNodeValue());
            } catch (IllegalArgumentException e) {
              logger.warn("Invalid fit type {} in {}", heightNode.getNodeValue(),
                  file.getAbsolutePath());
            }
          }
          YogaLayout layout = new YogaLayout();
          if (width != null) {
            width.ifLeft(layout::setWidth).ifRight(layout::setWidthPercent);
          }
          if (height != null) {
            height.ifLeft(layout::setHeight).ifRight(layout::setHeightPercent);
          }
          ImageView<YogaLayout> view = new ImageView<>(layout).setFitType(fitType);
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

  @Nullable
  private static Either<Integer, Float> extractSize(String size) {
    if (Strings.isNullOrEmpty(size)) {
      return null;
    }
    if (size.contains("%")) {
      String pctString = size.split("%")[0];
      try {
        return Either.right(Float.valueOf(pctString));
      } catch (NumberFormatException e) {
        return null;
      }
    } else if (size.contains("px")) {
      String pxString = size.split("px")[0];
      try {
        return Either.left(Integer.valueOf(pxString));
      } catch (NumberFormatException e) {
        return null;
      }
    } else {
      return null;
    }
  }
}
