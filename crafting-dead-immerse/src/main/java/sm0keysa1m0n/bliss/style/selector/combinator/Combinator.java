package sm0keysa1m0n.bliss.style.selector.combinator;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import sm0keysa1m0n.bliss.style.StyleNode;
import sm0keysa1m0n.bliss.style.selector.Selector;
import sm0keysa1m0n.bliss.style.selector.StyleNodeState;

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
