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

import com.craftingdead.immerse.client.gui.view.Color;
import io.github.humbleui.skija.paragraph.Shadow;
import io.github.humbleui.types.Point;

public class ShadowParser implements ValueParser<Shadow> {

  public static final ShadowParser INSTANCE = new ShadowParser();

  private ShadowParser() {}

  @Override
  public int validate(String style) {
    return style.length();
  }

  @Override
  public Shadow parse(String style) {
    var values = style.split(" ");
    if (values.length == 4) {
      var offsetFirst = values[0].contains("px");
      Point offset;
      float blurRadius;
      Color color;
      if (offsetFirst) {
        offset = parseOffset(values[0], values[1]);
        blurRadius = Float.parseFloat(values[2].replace("px", ""));
        color = ColorParser.INSTANCE.parse(values[3]);
      } else {
        color = ColorParser.INSTANCE.parse(values[0]);
        offset = parseOffset(values[1], values[2]);
        blurRadius = Float.parseFloat(values[3].replace("px", ""));
      }
      return new Shadow(color.valueHex(), offset, blurRadius);
    }

    if (values.length == 3) {
      var offsetFirst = values[0].contains("px");
      Point offset;
      Color color;
      if (offsetFirst) {
        offset = parseOffset(values[0], values[1]);
        color = ColorParser.INSTANCE.parse(values[2]);
      } else {
        color = ColorParser.INSTANCE.parse(values[0]);
        offset = parseOffset(values[1], values[2]);
      }
      return new Shadow(color.valueHex(), offset, 0.0D);
    }

    if (values.length == 2) {
      return new Shadow(Color.BLACK.valueHex(), parseOffset(values[0], values[1]), 0.0D);
    }

    throw new IllegalArgumentException("Invalid shadow: " + style);
  }

  private static Point parseOffset(String offsetX, String offsetY) {
    return new Point(
        Float.parseFloat(offsetX.replace("px", "")),
        Float.parseFloat(offsetY.replace("px", "")));
  }
}
