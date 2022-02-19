/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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

package com.craftingdead.immerse.client.gui.view.style.selector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;
import com.craftingdead.immerse.client.gui.view.style.StyleHolder;

public class SimpleStyleSelector implements StyleSelector {

  private final List<Pair<StyleSelectorType, String>> selectors;
  private final Set<String> pseudoClasses = new HashSet<>();
  private final List<StructuralSelector> structuralSelectors;
  protected int computedSpecificity;

  public SimpleStyleSelector() {
    this.selectors = new ArrayList<>();
    this.structuralSelectors = new ArrayList<>(1);
    this.computedSpecificity = -1;
  }

  public SimpleStyleSelector addWildcard() {
    this.add(StyleSelectorType.WILDCARD, "*");
    this.computedSpecificity = 0;
    return this;
  }

  @Override
  public SimpleStyleSelector add(StyleSelectorType type, String selector) {
    if (type == StyleSelectorType.STRUCTURAL_PSEUDOCLASS) {
      this.structuralSelectors.add(StructuralSelector.parse(selector));
    } else if (type == StyleSelectorType.PSEUDOCLASS) {
      this.pseudoClasses.add(selector);
    } else {
      this.selectors.add(Pair.of(type, selector));
    }

    this.computedSpecificity = -1;
    return this;
  }

  @Override
  public int getSpecificity() {
    if (this.computedSpecificity == -1) {
      this.computedSpecificity = 0;
      for (var selector : this.selectors) {
        this.computedSpecificity += selector.getKey().getSpecificity();
      }

      this.computedSpecificity +=
          this.pseudoClasses.size() * StyleSelectorType.PSEUDOCLASS.getSpecificity();

      this.computedSpecificity +=
          this.structuralSelectors.size()
              * StyleSelectorType.STRUCTURAL_PSEUDOCLASS.getSpecificity();
    }
    return this.computedSpecificity;
  }

  public List<Pair<StyleSelectorType, String>> getSelectors() {
    return selectors;
  }

  public boolean isSupersetOf(SimpleStyleSelector selector) {
    // This check does not use the structuralSelectors since they cannot be compared effectively
    return this.selectors.containsAll(selector.selectors);
  }

  @Override
  public boolean match(StyleHolder styleHolder) {
    for (var selector : this.selectors) {
      if (selector.getKey() == StyleSelectorType.WILDCARD) {
        return true;
      } else if (!this.checkSelector(selector, styleHolder)) {
        return false;
      }
    }
    for (var structuralSelector : this.structuralSelectors) {
      if (!structuralSelector.test(styleHolder)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean match(StyleSelector selector) {
    if (selector == this) {
      return true;
    }

    return selector instanceof SimpleStyleSelector other
        && this.selectors.size() == other.selectors.size()
        && this.selectors.containsAll(other.selectors)
        && this.structuralSelectors.size() == other.structuralSelectors.size()
        && this.structuralSelectors.containsAll(other.structuralSelectors)
        && this.pseudoClasses.size() == other.pseudoClasses.size()
        && this.pseudoClasses.containsAll(other.pseudoClasses);
  }

  protected boolean checkSelector(Pair<StyleSelectorType, String> selector,
      StyleHolder styleHolder) {
    switch (selector.getKey()) {
      case WILDCARD:
        return true;
      case TYPE:
        if (!selector.getValue().equals(styleHolder.getOwner().getType())) {
          return false;
        }
        break;
      case CLASS:
        if (!styleHolder.getOwner().getStyleClasses().contains(selector.getValue())) {
          return false;
        }
        break;
      case ID:
        if (!selector.getValue().equals(styleHolder.getOwner().getId())) {
          return false;
        }
        break;
      default:
        break;
    }
    return true;
  }

  @Override
  public Set<String> getPseudoClasses() {
    return this.pseudoClasses;
  }

  @Override
  public String toString() {
    return "StyleSelector[" +
        "selectors=" + this.selectors +
        ", structuralSelectors=" + this.structuralSelectors +
        ", pseudoClasses=" + this.pseudoClasses +
        ", computedSpecificity=" + this.computedSpecificity +
        ']';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    return o instanceof SimpleStyleSelector that
        && Objects.equals(this.selectors, that.selectors)
        && Objects.equals(this.structuralSelectors, that.structuralSelectors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.selectors, this.structuralSelectors);
  }
}
