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

package com.craftingdead.immerse.client.gui.view.style.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.craftingdead.immerse.client.gui.view.style.StyleHolder;
import com.craftingdead.immerse.client.gui.view.style.selector.SimpleStyleSelector;
import com.craftingdead.immerse.client.gui.view.style.selector.StyleSelector;
import com.google.common.collect.Lists;
import io.github.humbleui.skija.FontMgr;
import io.github.humbleui.skija.Typeface;
import io.github.humbleui.skija.paragraph.TypefaceFontProvider;

public class StyleList {

  private final StyleRule wildcard = new StyleRule(new SimpleStyleSelector().addWildcard());
  private final List<StyleRule> rules = Lists.newArrayList(this.wildcard);

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

  public void addRule(StyleSelector selector, List<StyleProperty> properties) {
    var match = this.rules.stream()
        .filter(styleEntry -> styleEntry.selector().match(selector))
        .findFirst();

    StyleRule lastAdded;
    if (!match.isPresent()) {
      var newEntry = new StyleRule(selector);
      this.rules.add(newEntry);
      lastAdded = newEntry;
    } else {
      lastAdded = match.get();
    }
    lastAdded.mergeProperties(properties);
  }

  public List<StyleRule> getRules() {
    return this.rules;
  }

  public StyleRule getWildcard() {
    return this.wildcard;
  }

  void clear() {
    this.rules.clear();
    this.rules.add(this.wildcard);
    this.wildcard.properties().clear();
  }

  boolean isEmpty() {
    return this.rules.size() == 1 && this.wildcard.properties().isEmpty();
  }

  public List<StyleRule> getRulesMatching(StyleHolder styleHolder) {
    var rules = new ArrayList<StyleRule>();
    rules.add(this.wildcard);
    this.rules.forEach(rule -> {
      if (rule.selector().match(styleHolder)) {
        rules.add(rule);
      }
    });
    return rules;
  }
}
