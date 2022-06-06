package sm0keysa1m0n.bliss.style;

import java.util.Set;
import sm0keysa1m0n.bliss.style.selector.Selector;

public record StyleRule(Selector selector, Set<StyleProperty> properties) {}
