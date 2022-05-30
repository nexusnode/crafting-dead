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

package com.craftingdead.immerse.client.gui.view.style.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import com.craftingdead.immerse.client.gui.view.style.tree.StyleList;
import com.craftingdead.immerse.client.gui.view.style.tree.StyleProperty;
import com.craftingdead.immerse.util.NumberedLineIterator;
import com.mojang.logging.LogUtils;
import io.github.humbleui.skija.Typeface;
import net.minecraft.resources.ResourceLocation;

public class StyleSheetParser {

  private static final Logger logger = LogUtils.getLogger();

  private static final String IMPORT = "@import";
  private static final String FONT_FACE = "@font-face";

  private StyleSheetParser() {}

  @FunctionalInterface
  public interface FontLoader {

    Typeface load(ResourceLocation location) throws IOException;
  }

  public static ParseResult loadStyleSheet(InputStream inputStream, FontLoader fontLoader)
      throws IOException {
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

        if (line.startsWith("@")) {
          if (line.startsWith(FONT_FACE)) {
            readFontFace(iterator, list, fontLoader);
          } else if (line.startsWith(IMPORT)) {
            dependencies.add(new ResourceLocation(line.substring(IMPORT.length())
                .replace("'", "")
                .replace("\"", "")
                .replace(";", "")
                .trim()));
          } else {
            logger.warn("Unknown at-rule: {}", line);
          }
          continue;
        }


        var selectors = StyleSelectorParser.readSelectors(line, iterator);
        var rules = readBlock(iterator);
        for (var selector : selectors) {
          list.addRule(selector, rules);
        }
      }

      return new ParseResult(list, dependencies);
    }
  }

  public record ParseResult(StyleList styleList, Collection<ResourceLocation> dependencies) {}

  private static void readFontFace(NumberedLineIterator content, StyleList styleList,
      FontLoader fontLoader) throws IOException {
    int startLineNumber = content.getLineNumber();
    var currentLine = content.nextLine().trim();
    String fontFamily = null;
    ResourceLocation location = null;
    while (!StringUtils.contains(currentLine, "}")) {
      if (currentLine.startsWith("font-family")) {
        char quote;
        if (currentLine.contains("\"")) {
          quote = '"';
        } else {
          quote = '\'';
        }
        fontFamily =
            currentLine.substring(currentLine.indexOf(quote) + 1, currentLine.lastIndexOf(quote));
        currentLine = content.nextLine().trim();
        continue;
      }

      if (currentLine.startsWith("src")) {
        var url = currentLine.substring(currentLine.indexOf(':') + 1)
            .replace("'", "")
            .replace("\"", "")
            .replace(";", "")
            .trim();
        location = new ResourceLocation(url);
      }
      currentLine = content.nextLine().trim();
    }

    if (fontFamily == null) {
      logger.error("No font-family property specified for font-face at line: {}", startLineNumber);
      return;
    }

    if (location == null) {
      logger.error("No src property specified for font-face at line: {}", startLineNumber);
      return;
    }

    styleList.addFont(fontFamily, fontLoader.load(location));
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
      var rule = currentLine.replace(';', ' ').strip().split(":", 2);
      elements.add(new StyleProperty(rule[0].strip(), rule[1].strip()));
      if (!content.hasNext()) {
        return Collections.emptyList();
      }
      currentLine = content.nextLine();
    }
    return elements;
  }
}
