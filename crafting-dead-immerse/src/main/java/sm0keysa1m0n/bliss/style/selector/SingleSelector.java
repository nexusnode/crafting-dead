package sm0keysa1m0n.bliss.style.selector;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import org.jetbrains.annotations.Nullable;
import com.google.common.base.Predicates;
import sm0keysa1m0n.bliss.style.States;
import sm0keysa1m0n.bliss.style.StyleNode;

/**
 * Represents an single selector. E.g. <code>#id</code> or <code>:hover</code>.
 * 
 * @author Sm0keySa1m0n
 */
public record SingleSelector(String id, int specificity,
    Function<StyleNode, Optional<Set<StyleNodeState>>> matcher) implements Selector {

  public static final int WILDCARD_SPECIFICITY = 0;
  public static final int TYPE_SPECIFICITY = 1;
  public static final int CLASS_SPECIFICITY = 1_000;
  public static final int ID_SPECIFICITY = 1_000_000;
  public static final int PSEUDOCLASS_SPECIFICITY = 1_000;
  public static final int STRUCTURAL_PSEUDOCLASS_SPECIFICITY = 1_000;

  public static final SingleSelector WILDCARD =
      predicate("*", WILDCARD_SPECIFICITY, Predicates.alwaysTrue());

  public static final SingleSelector FIRST_CHILD = structural(":first-child",
      style -> style.getIndex() == 0);

  public static final SingleSelector LAST_CHILD = structural(":last-child", style -> {
    var parent = style.getParent();
    if (parent == null) {
      return false;
    }
    var childCount = parent.getChildStyles().size();
    return childCount == 1 || style.getIndex() == childCount - 1;
  });

  public static final SingleSelector ONLY_CHILD = structural(":only-child",
      style -> style.getParent().getChildStyles().size() == 1);

  public static final SingleSelector FIRST_OF_TYPE = structural(":first-of-type", style -> {
    var parent = style.getParent();
    if (parent == null) {
      return false;
    }

    var childStyles = parent.getChildStyles();
    if (childStyles.size() == 1) {
      return true;
    }

    for (var childStyle : childStyles) {
      if (childStyle == style) {
        return true;
      } else if (childStyle.getType().equals(style.getType())) {
        return false;
      }
    }
    return false;
  });

  public static final SingleSelector LAST_OF_TYPE = structural(":last-of-type", style -> {
    var parent = style.getParent();
    if (parent == null) {
      return false;
    }

    var childStyles = parent.getChildStyles();
    if (childStyles.size() == 1) {
      return true;
    }

    for (int i = childStyles.size() - 1; i >= 0; i--) {
      var childStyle = childStyles.get(i);
      if (childStyle == style) {
        return true;
      } else if (childStyle.getType().equals(style.getType())) {
        return false;
      }
    }
    return false;
  });

  public static final SingleSelector ONLY_OF_TYPE = structural(":only-of-type", style -> {
    var parent = style.getParent();
    if (parent == null) {
      return false;
    }

    var childStyles = parent.getChildStyles();
    if (childStyles.size() == 1) {
      return true;
    }

    for (var childStyle : childStyles) {
      if (childStyle.getType().equals(style.getType())
          && childStyle != style) {
        return false;
      }
    }

    return true;
  });

  private static SingleSelector structural(String id, Predicate<StyleNode> predicate) {
    return predicate(id, STRUCTURAL_PSEUDOCLASS_SPECIFICITY, predicate);
  }

  private static SingleSelector predicate(String id, int specificity,
      Predicate<StyleNode> predicate) {
    return new SingleSelector(id, specificity,
        node -> predicate.test(node) ? Optional.of(Set.of()) : Optional.empty());
  }

  @Override
  public Optional<Set<StyleNodeState>> match(StyleNode node) {
    return this.matcher.apply(node);
  }

  @Override
  public int getSpecificity() {
    return this.specificity;
  }

  @Override
  public String toString() {
    return "Selector[" + this.id + "]";
  }

  @Override
  public boolean equals(Object o) {
    return this == o || o instanceof SingleSelector that && that.id.equals(this.id);
  }

  @Override
  public int hashCode() {
    return this.id.hashCode();
  }

  public static SingleSelector ofType(String type) {
    return predicate(type, TYPE_SPECIFICITY,
        node -> type.equals(node.getType()));
  }

  public static SingleSelector ofClass(String styleClass) {
    return predicate("." + styleClass, CLASS_SPECIFICITY,
        node -> node.getStyleClasses().contains(styleClass));
  }

  public static SingleSelector ofId(String id) {
    return predicate("#" + id, ID_SPECIFICITY, node -> id.equals(node.getId()));
  }

  public static SingleSelector ofState(String pseudoClass) {
    int state = States.get(pseudoClass)
        .orElseThrow(() -> new IllegalStateException("Invalid pseudo class: " + pseudoClass));
    return new SingleSelector(":" + pseudoClass, PSEUDOCLASS_SPECIFICITY,
        node -> Optional.of(Set.of(new StyleNodeState(node, state))));
  }

  @Nullable
  public static SingleSelector parseStructural(String selector,
      Function<String, Selector[]> selectorListParser) {
    return switch (selector) {
      case "first-child" -> FIRST_CHILD;
      case "last-child" -> LAST_CHILD;
      case "only-child" -> ONLY_CHILD;
      case "first-of-type" -> FIRST_OF_TYPE;
      case "last-of-type" -> LAST_OF_TYPE;
      case "only-of-type" -> ONLY_OF_TYPE;
      default -> {
        var nthChild = "nth-child(";
        if (selector.startsWith(nthChild)) {
          var n =
              Integer.parseInt(selector.substring(nthChild.length(), selector.lastIndexOf(')')));
          yield structural(":nth-child(" + n + ")", style -> style.getIndex() == n - 1);
        }
        // var has = "has(";
        // if (selector.startsWith(has)) {
        // var selectorListStr = selector.substring(has.length(), selector.lastIndexOf(')'));
        // var selectorList = selectorListParser.apply(selectorListStr);
        // yield new ElementMatcher(":has(" + selectorListStr + ")",
        // STRUCTURAL_PSEUDOCLASS_SPECIFICITY,
        // node -> {
        // for (var innerSelector : selectorList) {
        // var result = innerSelector.match(node);
        // if (result.isPresent()) {
        // return result;
        // }
        // }
        // return Optional.empty();
        // });
        // }
        yield null;
      }
    };
  }
}
