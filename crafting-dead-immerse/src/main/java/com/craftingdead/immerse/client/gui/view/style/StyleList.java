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

package com.craftingdead.immerse.client.gui.view.style;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import com.craftingdead.immerse.client.gui.view.style.selector.StyleNodeState;
import com.craftingdead.immerse.client.gui.view.style.selector.StyleSelector;
import io.github.humbleui.skija.FontMgr;
import io.github.humbleui.skija.Typeface;
import io.github.humbleui.skija.paragraph.TypefaceFontProvider;

public class StyleList {

  private final List<StyleRule> rules = new ArrayList<>();
  private final Map<String, Typeface> fonts = new HashMap<>();

  public StyleList() {}

  public StyleList(StyleList original) {
    this.merge(original);
  }

  public StyleList merge(StyleList source) {
    source.rules.forEach(rule -> this.addRule(rule.selectorList(), rule.properties()));
    this.fonts.putAll(source.fonts);
    return this;
  }

  public FontMgr createFontManager() {
    var fontManager = new TypefaceFontProvider();
    this.fonts.forEach((name, typeface) -> fontManager.registerTypeface(typeface, name));
    return fontManager;
  }

  public void addFont(String name, Typeface typeface) {
    this.fonts.put(name, typeface);
  }

  public void addRule(StyleSelector selectorList, List<StyleProperty> properties) {
    var match = this.rules.stream()
        .filter(styleEntry -> styleEntry.selectorList().equals(selectorList))
        .findFirst();

    StyleRule lastAdded;
    if (!match.isPresent()) {
      var newEntry = new StyleRule(selectorList);
      this.rules.add(newEntry);
      lastAdded = newEntry;
    } else {
      lastAdded = match.get();
    }
    lastAdded.mergeProperties(properties);
  }

  public Map<StyleRule, List<StyleNodeState>> getRulesMatching(StyleNode node) {
    var rules = new HashMap<StyleRule, List<StyleNodeState>>();
    this.rules.stream().forEach(rule -> rule.selectorList().match(node)
        .map(Stream::toList)
        .ifPresent(nodeState -> rules.put(rule, nodeState)));
    return rules;
  }
}
