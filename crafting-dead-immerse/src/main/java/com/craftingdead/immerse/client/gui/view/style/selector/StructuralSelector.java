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

import java.util.function.Predicate;
import com.craftingdead.immerse.client.gui.view.style.StyleHolder;
import com.craftingdead.immerse.client.gui.view.style.StyleParent;

public interface StructuralSelector extends Predicate<StyleHolder> {

  StructuralSelector FIRST_CHILD =
      style -> style.getParent() instanceof StyleParent parent
          && (parent.getChildCount() == 1
              || parent.getChildStyles().get(0) == style);

  StructuralSelector LAST_CHILD = style -> {
    if (style.getParent() instanceof StyleParent parent) {
      if (parent.getChildCount() == 1) {
        return true;
      }
      var childStyles = parent.getChildStyles();
      return childStyles.get(childStyles.size() - 1) == style;
    }
    return false;
  };

  StructuralSelector ONLY_CHILD =
      style -> style.getParent() instanceof StyleParent parent && parent.getChildCount() == 1;


  StructuralSelector FIRST_OF_TYPE = style -> {
    if (style.getParent() instanceof StyleParent parent) {
      if (parent.getChildCount() == 1) {
        return true;
      }

      var childStyles = parent.getChildStyles();
      for (var childStyle : childStyles) {
        if (childStyle == style) {
          return true;
        } else if (childStyle.getOwner().getType().equals(style.getOwner().getType())) {
          return false;
        }
      }
    }
    return false;
  };

  StructuralSelector LAST_OF_TYPE = style -> {
    if (style.getParent() instanceof StyleParent parent) {
      if (parent.getChildCount() == 1) {
        return true;
      }

      var childStyles = parent.getChildStyles();

      for (int i = childStyles.size() - 1; i >= 0; i--) {
        var childStyle = childStyles.get(i);
        if (childStyle == style) {
          return true;
        } else if (childStyle.getOwner().getType().equals(style.getOwner().getType())) {
          return false;
        }
      }
    }
    return false;
  };

  StructuralSelector ONLY_OF_TYPE = style -> {
    if (style.getParent() instanceof StyleParent parent) {
      if (parent.getChildCount() == 1) {
        return true;
      }

      var childStyles = parent.getChildStyles();
      for (var childStyle : childStyles) {
        if (childStyle.getOwner().getType().equals(style.getOwner().getType())
            && childStyle != style) {
          return false;
        }
      }

      return true;
    }
    return false;
  };

  // Non functional until rework of the skin system. Cause stackoverflow from endless refreshStyle
  // at the node level.
  // StructuralSelector EMPTY =
  // style -> style.getOwner() instanceof StyleParent &&
  // ((StyleParent) style).getChildCount() == 0;

  static boolean isStructural(String selector) {
    return switch (selector) {
      case "first-child", "last-child", "only-child", "first-of-type", "last-of-type", "only-of-type" -> true;
      default -> false;
    };
  }

  static StructuralSelector parse(String selector) {
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
          yield style -> style.getParent() instanceof StyleParent parent &&
              parent.getChildStyles().indexOf(style) == n - 1;
        }
        yield null;
      }
    };
  }
}
