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

package com.craftingdead.immerse.client.gui.view.style.selector;

import java.util.HashSet;
import java.util.Set;
import com.craftingdead.immerse.client.gui.view.style.StyleHolder;

public class StyleSelectorHierarchic implements StyleSelector {

  private final StyleSelector parentSelector;
  private final StyleSelector childSelector;
  private final boolean directChild;

  private final Set<String> pseudoClasses = new HashSet<>();

  public StyleSelectorHierarchic(StyleSelector parentSelector, StyleSelector childSelector,
      boolean directChild) {
    this.parentSelector = parentSelector;
    this.childSelector = childSelector;
    this.directChild = directChild;

    this.pseudoClasses.addAll(parentSelector.getPseudoClasses());
    this.pseudoClasses.addAll(childSelector.getPseudoClasses());
  }

  @Override
  public int getSpecificity() {
    return childSelector.getSpecificity() + parentSelector.getSpecificity();
  }

  @Override
  public StyleSelector add(StyleSelectorType type, String selector) {
    return this.addChild(type, selector);
  }

  public StyleSelector addChild(StyleSelectorType type, String selector) {
    this.childSelector.add(type, selector);
    return this;
  }

  public StyleSelector addParent(StyleSelectorType type, String selector) {
    this.parentSelector.add(type, selector);
    return this;
  }

  @Override
  public boolean match(StyleHolder styleHolder) {
    if (styleHolder.getParent() == null)
      return false;

    if (this.isDirectChild()) {
      if (!this.parentSelector.match(styleHolder.getParent().getStyle())) {
        return false;
      }
    } else {
      var current = styleHolder.getParent();

      var matched = false;
      while (current != null) {
        if (this.parentSelector.match(current.getStyle())) {
          matched = true;
          break;
        }
        current = current.getStyle().getParent();
      }
      if (!matched) {
        return false;
      }
    }
    return this.childSelector.match(styleHolder);
  }

  @Override
  public boolean match(StyleSelector selector) {
    if (selector == this) {
      return true;
    }
    return selector instanceof StyleSelectorHierarchic other
        && this.isDirectChild() == other.isDirectChild()
        && this.childSelector.match(other.childSelector) &&
        this.parentSelector.match(other.parentSelector);
  }

  @Override
  public Set<String> getPseudoClasses() {
    return this.pseudoClasses;
  }

  public boolean isDirectChild() {
    return directChild;
  }

  public StyleSelector getParentSelector() {
    return parentSelector;
  }

  public StyleSelector getChildSelector() {
    return childSelector;
  }

  @Override
  public String toString() {
    return "StyleSelectorHierarchic{" + "parentSelector=" + parentSelector + ", childSelector="
        + childSelector +
        ", directChild=" + directChild + "}";
  }
}
