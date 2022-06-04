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

package com.craftingdead.immerse.client.gui.view.style.selector.combinator;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import com.craftingdead.immerse.client.gui.view.style.StyleNode;
import com.craftingdead.immerse.client.gui.view.style.selector.Selector;
import com.craftingdead.immerse.client.gui.view.style.selector.StyleNodeState;

/**
 * Represents a CSS selector combinator.
 * 
 * @see <a href=
 *      "https://drafts.csswg.org/selectors/#selector-combinator">https://drafts.csswg.org/selectors/#selector-combinator</a>
 * 
 * @author Sm0keySa1m0n
 */
public interface Combinator {

  /**
   * Represents a logical AND operator. Used to combine two selectors.
   */
  public static final Combinator AND = new Combinator() {

    @Override
    public Optional<Result> match(StyleNode node, Selector selector) {
      return selector.match(node).map(nodeStates -> new Result(node, nodeStates));
    }

    @Override
    public List<Result> reverseMatch(StyleNode node, Selector selector) {
      return selector.match(node)
          .map(nodeStates -> List.of(new Result(node, nodeStates)))
          .orElse(List.of());
    }

    @Override
    public String toString() {
      return "Combinator[and]";
    }
  };

  /**
   * Locate the next {@link StyleNode} that matches the specified {@link Selector}.
   * 
   * @param node - the originating {@link StyleNode} to match from.
   * @param selector - the {@link Selector} to match against.
   * @return {@link Optional#empty()} if no match was found, otherwise a {@link Result}.
   */
  Optional<Result> match(StyleNode node, Selector selector);

  List<Result> reverseMatch(StyleNode node, Selector selector);

  record Result(StyleNode node, Set<StyleNodeState> nodeStates) {};
}
