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

import org.apache.commons.lang3.StringUtils;
import com.craftingdead.immerse.client.gui.view.style.selector.SimpleStyleSelector;
import com.craftingdead.immerse.client.gui.view.style.selector.StructuralSelector;
import com.craftingdead.immerse.client.gui.view.style.selector.StyleSelector;
import com.craftingdead.immerse.client.gui.view.style.selector.StyleSelectorHierarchic;
import com.craftingdead.immerse.client.gui.view.style.selector.StyleSelectorType;
import com.craftingdead.immerse.util.NumberedLineIterator;

class StyleSelectorParser {

  private StyleSelectorParser() {}

  static StyleSelector[] readSelectors(String currentLine, NumberedLineIterator iterator) {
    var selectorBuilder = new StringBuilder(currentLine.trim());
    while (!currentLine.endsWith("{")) {
      currentLine = iterator.nextLine().trim();
      selectorBuilder.append(currentLine);
    }

    var selector = selectorBuilder.toString().replace("{", "").trim();
    if (selector.contains(",")) {
      var selectors = new StyleSelector[StringUtils.countMatches(selector, ",") + 1];
      int i = 0;
      for (var subSelector : selector.split(",")) {
        selectors[i] = readSingleSelector(cleanSelector(subSelector.trim()));
        i++;
      }
      return selectors;
    }

    return new StyleSelector[] {readSingleSelector(cleanSelector(selector))};
  }

  static StyleSelector readSingleSelector(String selector) {
    return selector.contains(">") ? parseHierarchic(selector) : parseSimple(selector);
  }

  static String cleanSelector(String selector) {
    var cleanSelector = new StringBuilder(selector);

    int i = 0;
    while (i < cleanSelector.length()) {
      if (i != 0 && cleanSelector.charAt(i) == ' ') {
        int count = 1;
        while (cleanSelector.charAt(i + count) == ' ') {
          count++;
        }

        if (!isToken(cleanSelector.charAt(i + count))
            && !isToken(cleanSelector.charAt(i - 1))) {
          cleanSelector.replace(i, i + count, ">>");
        } else {
          cleanSelector.delete(i, i + count);
        }
      }
      i++;
    }
    return cleanSelector.toString();
  }

  static StyleSelector parseHierarchic(String selector) {
    var direct = selector.charAt(selector.lastIndexOf('>') - 1) != '>';
    var split = selector.split("[>]+(?=[^>]*$)");
    if (split.length != 2) {
      throw new IllegalStateException("Invalid selector: " + selector);
    }
    return new StyleSelectorHierarchic(readSingleSelector(split[0]),
        parseSimple(split[1]), direct);
  }

  static StyleSelector parseSimple(String selectorStr) {
    var selector = new SimpleStyleSelector();
    for (var part : selectorStr.split("(?=[.#:])")) {
      String pseudoClass = null;
      if (part.contains(":")) {
        pseudoClass = part.split(":")[1];
        part = part.split(":")[0];
      }
      if (part.startsWith("*")) {
        selector.add(StyleSelectorType.WILDCARD, "*");
      } else if (part.startsWith("#")) {
        selector.add(StyleSelectorType.ID, part.substring(1));
      } else if (part.startsWith(".")) {
        selector.add(StyleSelectorType.CLASS, part.substring(1));
      } else if (pseudoClass == null) {
        selector.add(StyleSelectorType.TYPE, part);
      }
      if (pseudoClass != null) {
        if (StructuralSelector.isStructural(pseudoClass)) {
          selector.add(StyleSelectorType.STRUCTURAL_PSEUDOCLASS, pseudoClass);
        } else {
          selector.add(StyleSelectorType.PSEUDOCLASS, pseudoClass);
        }
      }
    }
    return selector;
  }

  static boolean isToken(char c) {
    return StringUtils.contains(">", c);
  }
}
