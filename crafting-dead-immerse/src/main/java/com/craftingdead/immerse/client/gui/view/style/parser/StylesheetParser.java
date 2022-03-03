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

package com.craftingdead.immerse.client.gui.view.style.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import com.craftingdead.immerse.client.gui.view.style.StylesheetManager;
import com.craftingdead.immerse.client.gui.view.style.tree.StyleList;
import com.craftingdead.immerse.client.gui.view.style.tree.StyleProperty;
import com.craftingdead.immerse.util.NumberedLineIterator;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class StylesheetParser {

  private static final Logger logger = LogUtils.getLogger();

  private static final String IMPORT = "@import";

  private StylesheetParser() {}

  public static StyleList loadStylesheet(ResourceLocation styleSheet) throws IOException {
    var resource = Minecraft.getInstance().getResourceManager().getResource(styleSheet);
    return loadStylesheet(resource.getInputStream());
  }

  public static StyleList loadStylesheet(InputStream inputStream) throws IOException {
    var list = new StyleList();
    var dependencies = new ArrayList<ResourceLocation>();
    try (var reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
      var iterator = new NumberedLineIterator(reader);
      boolean insideComment = false;
      while (iterator.hasNext()) {
        var line = iterator.nextLine().trim();
        if (StringUtils.isEmpty(line)) {
          continue;
        }

        if (line.startsWith("/*")) {
          insideComment = !line.endsWith("*/");
          continue;
        }

        if (insideComment && line.startsWith("*/")) {
          insideComment = false;
          continue;
        }

        if (line.startsWith(IMPORT)) {
          var url = line.substring(IMPORT.length())
              .replace("url(", "")
              .replace(")", "")
              .replace(";", "")
              .replace("\"", "")
              .trim();
          dependencies.add(new ResourceLocation(url));
          continue;
        }

        if (!dependencies.isEmpty()) {
          list.merge(StylesheetManager.getInstance().loadStylesheets(dependencies));
          dependencies.clear();
        }
        if (line.contains("{")) {
          var selectors = StyleSelectorParser.readSelectors(line);
          var rules = readBlock(iterator);

          for (var selector : selectors) {
            list.addRule(selector, rules);
          }
        } else {
          logger.error("Expected { at line " + iterator.getLineNumber());
        }
      }

      // Load dependencies even if the styleSheet is empty
      if (!dependencies.isEmpty()) {
        list.merge(StylesheetManager.getInstance().loadStylesheets(dependencies));
        dependencies.clear();
      }
      return list;
    }
  }

  private static List<StyleProperty> readBlock(NumberedLineIterator content) {
    if (!content.hasNext()) {
      return Collections.emptyList();
    }
    var currentLine = content.nextLine();
    var elements = new ArrayList<StyleProperty>();
    while (!StringUtils.contains(currentLine, "}")) {
      if (StringUtils.contains(currentLine, "{")) {
        logger.error(
            "Found opening bracket at line " + content.getLineNumber() + " while inside a block");
        return Collections.emptyList();
      }
      if (currentLine.isBlank()) {
        currentLine = content.nextLine();
        continue;
      }
      var rule = currentLine.replace(';', ' ').trim().split(":", 2);
      elements.add(new StyleProperty(rule[0].trim(), rule[1].trim()));
      if (!content.hasNext()) {
        return Collections.emptyList();
      }
      currentLine = content.nextLine();
    }
    return elements;
  }
}
