package sm0keysa1m0n.bliss.style.selector;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.jetbrains.annotations.Nullable;
import sm0keysa1m0n.bliss.style.StyleNode;
import sm0keysa1m0n.bliss.style.selector.combinator.Combinator;

/**
 * A {@link Selector} which combines multiple sub-selectors, joined by {@link Combinator}s. Roughly
 * equates to <a href= "https://drafts.csswg.org/selectors/#typedef-compound-selector">
 * https://drafts.csswg.org/selectors/#typedef-compound-selector</a>.
 * 
 * @author Sm0keySa1m0n
 */
public class CompoundSelector implements Selector {

  private final Deque<Element> elements = new ArrayDeque<>();
  private int specificity;

  public void push(Selector selector, Combinator combinator) {
    this.elements.push(new Element(selector, combinator));
    this.specificity += selector.getSpecificity();
  }

  @Override
  public Optional<Set<StyleNodeState>> match(StyleNode node) {
    Set<StyleNodeState> nodeStates = new HashSet<>();

    var combinator = Combinator.AND;
    for (var element : this.elements) {
      if (combinator == null) {
        throw new IllegalStateException("Missing combinator for element: " + element);
      }
      var result = combinator.match(node, element.selector()).orElse(null);
      if (result == null) {
        return Optional.empty();
      }
      nodeStates.addAll(result.nodeStates());
      node = result.node();
      combinator = element.combinator();
    }

    return Optional.of(nodeStates);
  }

  @Override
  public int getSpecificity() {
    return this.specificity;
  }

  @Override
  public String toString() {
    return "Selector[specificity=" + this.specificity + ", elements=" + this.elements.toString()
        + "]";
  }

  private record Element(Selector selector, @Nullable Combinator combinator) {}
}
