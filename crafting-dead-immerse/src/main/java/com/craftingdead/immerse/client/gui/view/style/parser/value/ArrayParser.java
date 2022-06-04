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

package com.craftingdead.immerse.client.gui.view.style.parser.value;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ArrayParser<T> implements ValueParser<T[]> {

  private final Class<T> type;
  private final ValueParser<T> elementParser;

  public ArrayParser(Class<T> type, ValueParser<T> elementParser) {
    this.type = type;
    this.elementParser = elementParser;
  }

  @Override
  public int validate(String style) {
    return style.length();
  }

  @SuppressWarnings("unchecked")
  @Override
  public T[] parse(String style) {
    List<T> elements = new ArrayList<>();
    int index = 0;
    while (index < style.length()) {
      var elementLength = this.elementParser.validate(style);
      var endIndex = index + elementLength;
      elements.add(this.elementParser.parse(style.substring(index, endIndex)));
      index += elementLength;
    }
    return elements.toArray(length -> (T[]) Array.newInstance(this.type, length));
  }
}
