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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.craftingdead.immerse.client.gui.view.style.selector.Selector;
import com.craftingdead.immerse.client.gui.view.style.selector.StyleNodeState;
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
    source.rules.forEach(rule -> this.addRule(rule.selector(), rule.properties()));
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

  public void addRule(Selector selector, Set<StyleProperty> properties) {
    this.rules.add(new StyleRule(selector, properties));
  }

  public Map<StyleRule, Set<StyleNodeState>> getRulesMatching(StyleNode node) {
    var rules = new LinkedHashMap<StyleRule, Set<StyleNodeState>>();
    this.rules.forEach(rule -> rule.selector().match(node)
        .ifPresent(nodeState -> rules.put(rule, nodeState)));
    return rules;
  }
}
