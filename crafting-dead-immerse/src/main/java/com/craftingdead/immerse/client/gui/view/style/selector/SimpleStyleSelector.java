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
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import com.craftingdead.immerse.client.gui.view.style.StyleNode;

public class SimpleStyleSelector implements StyleSelector {

  private final Set<ElementMatcher> matchers = new HashSet<>();

  private int state;

  protected int computedSpecificity;

  public SimpleStyleSelector() {
    this.computedSpecificity = -1;
  }

  public void addSingleState(int state) {
    this.state |= state;
    this.computedSpecificity += ElementMatcher.PSEUDOCLASS_SPECIFICITY;
  }

  public SimpleStyleSelector addWildcard() {
    this.addMatcher(ElementMatcher.WILDCARD);
    this.computedSpecificity = 0;
    return this;
  }

  @Override
  public SimpleStyleSelector addMatcher(ElementMatcher selector) {
    this.matchers.add(selector);
    this.computedSpecificity = -1;
    return this;
  }

  @Override
  public int getSpecificity() {
    if (this.computedSpecificity == -1) {
      this.computedSpecificity = this.matchers.stream().mapToInt(ElementMatcher::specificity).sum();
    }
    return this.computedSpecificity;
  }

  public Set<ElementMatcher> getSelectors() {
    return matchers;
  }

  public boolean isSupersetOf(SimpleStyleSelector selector) {
    // This check does not use the structuralSelectors since they cannot be compared effectively
    return this.matchers.containsAll(selector.matchers);
  }

  @Override
  public Optional<Stream<StyleNodeState>> match(StyleNode node) {
    return this.matchers.stream().allMatch(matcher -> matcher.matches(node))
        ? Optional.of(this.state == 0
            ? Stream.empty()
            : Stream.of(new StyleNodeState(node, this.state)))
        : Optional.empty();
  }

  @Override
  public String toString() {
    return "StyleSelector[" +
        "selectors=" + this.matchers +
        ", computedSpecificity=" + this.computedSpecificity +
        ']';
  }

  @Override
  public boolean equals(Object o) {
    return o == this || o instanceof SimpleStyleSelector that
        && this.matchers.equals(that.matchers)
        && this.state == that.state;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.matchers, this.state);
  }
}
