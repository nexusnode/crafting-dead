package com.craftingdead.immerse.client.gui.view.style.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.immerse.client.gui.view.style.StylesheetManager;
import com.craftingdead.immerse.client.gui.view.style.tree.StyleList;
import com.craftingdead.immerse.client.gui.view.style.tree.StyleProperty;
import com.craftingdead.immerse.util.NumberedLineIterator;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class StylesheetParser {

  private static final Logger logger = LogManager.getLogger();

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
          logger.fatal("Expected { at line " + iterator.getLineNumber());
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
        logger.fatal(
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
