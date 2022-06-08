package sm0keysa1m0n.bliss.style.selector.combinator;

import java.util.List;
import java.util.Optional;
import sm0keysa1m0n.bliss.style.StyleNode;
import sm0keysa1m0n.bliss.style.selector.Selector;

/**
 * Implements behaviour for the CSS next-sibling and subsequent-sibling combinators.
 * 
 * @see <a href="https://drafts.csswg.org/selectors/#adjacent-sibling-combinators">
 *      https://drafts.csswg.org/selectors/#adjacent-sibling-combinators</a>
 *      <a href="https://drafts.csswg.org/selectors/#general-sibling-combinators">
 *      https://drafts.csswg.org/selectors/#general-sibling-combinators</a>
 * 
 * @author Sm0keySa1m0n
 */
public class SiblingCombinator implements Combinator {

  private final boolean direct;

  public SiblingCombinator(boolean direct) {
    this.direct = direct;
  }

  @Override
  public Optional<Result> match(StyleNode node, Selector selector) {
    return this.checkSibling(node, false, selector);
  }

  private Optional<Result> checkSibling(StyleNode node, boolean reverse, Selector selector) {
    var parent = node.getParent();
    if (parent == null) {
      return Optional.empty();
    }
    var siblingIndex = node.getIndex() + (reverse ? 1 : -1);
    var children = parent.getChildStyles();
    if (siblingIndex == children.size() || siblingIndex < 0) {
      return Optional.empty();
    }

    var sibling = parent.getChildStyles().get(siblingIndex);
    var result = selector.match(sibling).map(nodeStates -> new Result(sibling, nodeStates));
    return this.direct
        ? result
        : result.or(() -> this.checkSibling(sibling, reverse, selector));
  }

  @Override
  public List<Result> reverseMatch(StyleNode node, Selector selector) {
    return this.checkSibling(node, true, selector).map(List::of).orElse(List.of());
  }

  @Override
  public String toString() {
    return "Combinator[" + (this.direct ? "+" : "~") + "]";
  }
}
