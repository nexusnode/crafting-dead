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

package com.craftingdead.immerse.client.gui.view.style.adapter;

import com.craftingdead.immerse.client.gui.view.Point;
import com.craftingdead.immerse.util.StringCountUtil;

public class PointTranslator
    implements StyleEncoder<Point>, StyleDecoder<Point>, StyleValidator<Point> {

  @Override
  public String encode(Point value, boolean prettyPrint) {
    return switch (value.type()) {
      case FIXED -> String.valueOf(value.value());
      case PERCENTAGE -> String.valueOf(value.value()) + "%";
      case AUTO -> "auto";
    };
  }

  @Override
  public Point decode(String style) {
    if (style.contains("%")) {
      return Point.percentage(Float.parseFloat(style.replace('%', '\0')));
    } else if (style.equals("auto")) {
      return Point.AUTO;
    } else {
      return Point.fixed(Float.parseFloat(style));
    }
  }

  @Override
  public int validate(String style) {
    int floatLength = StringCountUtil.floatAtStart(style);

    if (floatLength == 0) {
      return 0;
    }

    if (floatLength < style.length() && style.charAt(floatLength) == '%') {
      return floatLength + 1;
    }
    return floatLength;
  }
}
