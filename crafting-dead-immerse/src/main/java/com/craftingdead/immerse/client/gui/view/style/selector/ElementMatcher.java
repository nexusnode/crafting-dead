package com.craftingdead.immerse.client.gui.view.style.selector;

import java.util.function.Predicate;
import org.jetbrains.annotations.Nullable;
import com.craftingdead.immerse.client.gui.view.style.StyleNode;
import com.craftingdead.immerse.client.gui.view.style.StyleParentNode;
import com.google.common.base.Predicates;

public record ElementMatcher(String id, int specificity, Predicate<StyleNode> predicate) {

  public static final int WILDCARD_SPECIFICITY = 0;
  public static final int TYPE_SPECIFICITY = 1;
  public static final int CLASS_SPECIFICITY = 1_000;
  public static final int ID_SPECIFICITY = 1_000_000;
  public static final int PSEUDOCLASS_SPECIFICITY = 1_000;
  public static final int STRUCTURAL_PSEUDOCLASS_SPECIFICITY = 1_000;

  public static final ElementMatcher WILDCARD =
      new ElementMatcher("*", WILDCARD_SPECIFICITY, Predicates.alwaysTrue());

  public static final ElementMatcher FIRST_CHILD = structural(":first-child",
      style -> style.getParent() instanceof StyleParentNode parent
          && (parent.getChildCount() == 1
              || parent.getChildStyles().get(0) == style));

  public static final ElementMatcher LAST_CHILD = structural(":last-child", style -> {
    if (style.getParent() instanceof StyleParentNode parent) {
      if (parent.getChildCount() == 1) {
        return true;
      }
      var childStyles = parent.getChildStyles();
      return childStyles.get(childStyles.size() - 1) == style;
    }
    return false;
  });

  public static final ElementMatcher ONLY_CHILD = structural(":only-child",
      style -> style.getParent() instanceof StyleParentNode parent && parent.getChildCount() == 1);

  public static final ElementMatcher FIRST_OF_TYPE = structural(":first-of-type", style -> {
    if (style.getParent() instanceof StyleParentNode parent) {
      if (parent.getChildCount() == 1) {
        return true;
      }

      var childStyles = parent.getChildStyles();
      for (var childStyle : childStyles) {
        if (childStyle == style) {
          return true;
        } else if (childStyle.getType().equals(style.getType())) {
          return false;
        }
      }
    }
    return false;
  });

  public static final ElementMatcher LAST_OF_TYPE = structural(":last-of-type", style -> {
    if (style.getParent() instanceof StyleParentNode parent) {
      if (parent.getChildCount() == 1) {
        return true;
      }

      var childStyles = parent.getChildStyles();

      for (int i = childStyles.size() - 1; i >= 0; i--) {
        var childStyle = childStyles.get(i);
        if (childStyle == style) {
          return true;
        } else if (childStyle.getType().equals(style.getType())) {
          return false;
        }
      }
    }
    return false;
  });

  public static final ElementMatcher ONLY_OF_TYPE = structural(":only-of-type", style -> {
    if (style.getParent() instanceof StyleParentNode parent) {
      if (parent.getChildCount() == 1) {
        return true;
      }

      var childStyles = parent.getChildStyles();
      for (var childStyle : childStyles) {
        if (childStyle.getType().equals(style.getType())
            && childStyle != style) {
          return false;
        }
      }

      return true;
    }
    return false;
  });

  private static ElementMatcher structural(String id, Predicate<StyleNode> predicate) {
    return new ElementMatcher(id, STRUCTURAL_PSEUDOCLASS_SPECIFICITY, predicate);
  }

  public boolean matches(StyleNode node) {
    return this.predicate.test(node);
  }

  @Override
  public String toString() {
    return this.id;
  }

  @Override
  public boolean equals(Object o) {
    return this == o || o instanceof ElementMatcher that && that.id.equals(this.id);
  }
  
  @Override
  public int hashCode() {
    return this.id.hashCode();
  }

  public static ElementMatcher ofType(String type) {
    return new ElementMatcher(type, TYPE_SPECIFICITY,
        node -> type.equals(node.getType()));
  }

  public static ElementMatcher ofClass(String styleClass) {
    return new ElementMatcher("." + styleClass, CLASS_SPECIFICITY,
        node -> node.getStyleClasses().contains(styleClass));
  }

  public static ElementMatcher ofId(String id) {
    return new ElementMatcher("#" + id, ID_SPECIFICITY, node -> id.equals(node.getId()));
  }

  public static boolean isStructural(String selector) {
    return switch (selector) {
      case "first-child", "last-child", "only-child", "first-of-type", "last-of-type", "only-of-type" -> true;
      default -> false;
    };
  }

  @Nullable
  public static ElementMatcher parseStructural(String selector) {
    return switch (selector) {
      case "first-child" -> FIRST_CHILD;
      case "last-child" -> LAST_CHILD;
      case "only-child" -> ONLY_CHILD;
      case "first-of-type" -> FIRST_OF_TYPE;
      case "last-of-type" -> LAST_OF_TYPE;
      case "only-of-type" -> ONLY_OF_TYPE;
      default -> {
        var str = "nth-child(";
        if (selector.startsWith(str)) {
          var n = Integer.parseInt(selector.substring(str.length()).replace(")", ""));
          yield structural(":nth-child(" + n + ")",
              style -> style.getParent() instanceof StyleParentNode parent &&
                  parent.getChildStyles().indexOf(style) == n - 1);
        }
        yield null;
      }
    };
  }
}
