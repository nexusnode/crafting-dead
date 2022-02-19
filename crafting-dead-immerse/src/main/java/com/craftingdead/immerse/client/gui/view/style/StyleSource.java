/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.client.gui.view.style;

public record StyleSource(Type type, int specificity) implements Comparable<StyleSource> {

  public static final StyleSource DEFAULT = new StyleSource(Type.USER_AGENT, 0);
  public static final StyleSource CODE = new StyleSource(Type.CODE, 10_000);

  public boolean is(Type type) {
    return this.type == type;
  }

  @Override
  public int compareTo(StyleSource o) {
    if (this.type.ordinal() > o.type.ordinal()) {
      return 1;
    }

    if (this.type.ordinal() == o.type.ordinal()) {
      if (this.specificity > o.specificity) {
        return 1;
      } else if (this.specificity == o.specificity) {
        return 0;
      }
    }

    return -1;
  }

  public enum Type {

    USER_AGENT, AUTHOR, INLINE, CODE
  }
}
