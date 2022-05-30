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

import java.util.Optional;
import java.util.stream.Stream;
import com.craftingdead.immerse.client.gui.view.style.StyleNode;

public record StyleSelectorHierarchic(StyleSelector parentSelector,
    StyleSelector childSelectorList, boolean directChild) implements StyleSelector {

  @Override
  public int getSpecificity() {
    return this.childSelectorList.getSpecificity() + this.parentSelector.getSpecificity();
  }

  @Override
  public StyleSelector addMatcher(ElementMatcher selector) {
    return this.addChild(selector);
  }

  public StyleSelector addChild(ElementMatcher selector) {
    this.childSelectorList.addMatcher(selector);
    return this;
  }

  public StyleSelector addParent(ElementMatcher selector) {
    this.parentSelector.addMatcher(selector);
    return this;
  }

  @Override
  public Optional<Stream<StyleNodeState>> match(StyleNode node) {
    if (node.getParent() == null) {
      return Optional.empty();
    }

    if (this.directChild) {
      return this.parentSelector.match(node.getParent())
          .flatMap(a -> this.childSelectorList.match(node).map(b -> Stream.concat(a, b)));
    }

    var current = node.getParent();
    Stream<StyleNodeState> result = null;
    while (current != null) {
      var currentResult = this.parentSelector.match(current);
      if (currentResult.isPresent()) {
        result = currentResult.get();
      }
      current = current.getParent();
    }

    final var finalResult = result;
    return result == null
        ? Optional.empty()
        : this.childSelectorList.match(node).map(b -> Stream.concat(finalResult, b));
  }
}
