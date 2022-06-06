package sm0keysa1m0n.bliss.style.selector.combinator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import sm0keysa1m0n.bliss.style.StyleNode;
import sm0keysa1m0n.bliss.style.selector.Selector;

/**
 * Implements behaviour for the CSS descendant and child combinators.
 * 
 * @see <a href="https://drafts.csswg.org/selectors/#descendant-combinators">
 *      https://drafts.csswg.org/selectors/#descendant-combinators</a>
 *      <a href="https://drafts.csswg.org/selectors/#child-combinators">
 *      https://drafts.csswg.org/selectors/#child-combinators</a>
 * 
 * @author Sm0keySa1m0n
 */
public class DescendantCombinator implements Combinator {

  private final boolean direct;

  public DescendantCombinator(boolean direct) {
    this.direct = direct;
  }

  @Override
  public Optional<Result> match(StyleNode node, Selector selector) {
    var parent = node.getParent();
    if (parent == null) {
      return Optional.empty();
    }

    if (this.direct) {
      return selector.match(parent).map(nodeStates -> new Result(parent, nodeStates));
    }

    var current = parent;
    Result result = null;
    while (current != null) {
      var currentResult = selector.match(current);
      if (currentResult.isPresent()) {
        result = new Result(current, currentResult.get());
      }
      current = current.getParent();
    }

    return Optional.ofNullable(result);
  }

  @Override
  public List<Result> reverseMatch(StyleNode node, Selector selector) {
    return this.matchChildren(node, selector);
  }

  private List<Result> matchChildren(StyleNode node, Selector selector) {
    var results = new ArrayList<Result>();
    for (var child : node.getChildStyles()) {
      selector.match(child).ifPresent(nodeStates -> results.add(new Result(child, nodeStates)));
      results.addAll(this.matchChildren(child, selector));
    }
    return results;
  }

  @Override
  public String toString() {
    return "Combinator[" + (this.direct ? ">" : " ") + "]";
  }
}
