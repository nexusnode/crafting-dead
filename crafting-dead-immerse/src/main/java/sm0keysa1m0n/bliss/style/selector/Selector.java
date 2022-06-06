package sm0keysa1m0n.bliss.style.selector;

import java.util.Optional;
import java.util.Set;
import sm0keysa1m0n.bliss.style.StyleNode;

/**
 * Represents a CSS selector.
 * 
 * @see <a href="https://drafts.csswg.org/selectors/">https://drafts.csswg.org/selectors/</a>
 * 
 * @author Sm0keySa1m0n
 */
public interface Selector {

  /**
   * Attempt to match the specified {@link StyleNode} against this selector.
   * 
   * @param node - the node to match
   * @return {@link Optional#empty()} if there is no match, otherwise a set of dependent states.
   */
  Optional<Set<StyleNodeState>> match(StyleNode node);

  /**
   * The specificity of this selector.
   * 
   * @return the specificity
   */
  int getSpecificity();
}
