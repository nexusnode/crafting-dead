/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
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

import java.text.NumberFormat;
import java.util.regex.Pattern;
import com.craftingdead.immerse.client.gui.view.Color;

public class ColorTranslator
    implements StyleDecoder<Color>, StyleEncoder<Color>, StyleValidator<Color> {

  private final NumberFormat colorFormat;

  private final Pattern hexColorPattern = Pattern.compile("^#\\d{6}");
  private final Pattern hexAlphaColorPattern = Pattern.compile("^#\\d{6}\\s+\\d{2}%");
  private final Pattern rgbColorPattern =
      Pattern.compile("^rgb\\((\\s?\\d+\\s?,\\s?){2}(\\s?\\d+\\s?)\\)");
  private final Pattern rgbaColorPattern =
      Pattern.compile("^rgba\\((\\s?\\d+\\s?,\\s?){3}(\\s?\\d+\\s?)\\)");

  public ColorTranslator() {
    this.colorFormat = NumberFormat.getInstance();
    this.colorFormat.setMinimumFractionDigits(0);
    this.colorFormat.setMaximumFractionDigits(1);
  }

  @Override
  public String encode(Color value, boolean prettyPrint) {
    var builder = new StringBuilder();

    if (prettyPrint) {
      builder.append(value.getHex())
          .append(" (")
          .append(colorFormat.format(value.getRed()))
          .append(",")
          .append(colorFormat.format(value.getGreen()))
          .append(",")
          .append(colorFormat.format(value.getBlue()))
          .append(",")
          .append(colorFormat.format(value.getAlpha()))
          .append(")");
    } else {
      builder.append(value.getHex())
          .append(" ")
          .append(value.getAlpha() * 100)
          .append("%");
    }
    return builder.toString();
  }

  @Override
  public Color decode(String style) {
    // Hexa Color ex: #FF0011 20%
    if (style.startsWith("#")) {
      if (!style.contains(" ")) {
        return Color.fromHex(style);
      } else {
        var split = style.split(" ");
        var rgb = split[0];
        var alpha = Float.parseFloat(split[1].substring(0, split[1].length() - 1)) / 100;
        return Color.fromHex(rgb, alpha);
      }
    }
    // RGB or RGBA Color ex: rgba(255, 255, 255, 255)
    if (style.startsWith("rgb")) {
      var alpha = style.startsWith("rgba");

      var colorNames = style.substring(alpha ? 5 : 4, style.length() - 1).split(",");

      var redValue = 0.0F;
      var greenValue = 0.0F;
      var blueValue = 0.0F;
      var alphaValue = 1.0F;
      var i = 0;
      for (var value : colorNames) {
        if (i != 3) {
          float colorValue;
          value = value.trim();
          if (value.endsWith("%")) {
            colorValue = Float.valueOf(value.substring(0, value.length() - 1)) / 100f;
          } else {
            colorValue = Integer.valueOf(value) / 255.0F;
          }

          if (i == 0) {
            redValue = colorValue;
          } else if (i == 1) {
            greenValue = colorValue;
          } else if (i == 2) {
            blueValue = colorValue;
          }
        } else if (alpha) {
          alphaValue = Float.valueOf(value);
        }
        i++;
      }
      return new Color(redValue, greenValue, blueValue, alphaValue);
    }
    if (ColorConstants.hasConstant(style)) {
      return ColorConstants.getColor(style);
    }
    throw new RuntimeException("Invalid color: " + style);
  }

  @Override
  public int validate(String style) {
    if (this.hexColorPattern.matcher(style).matches()) {
      if (this.hexAlphaColorPattern.matcher(style).matches()) {
        return style.substring(0, style.indexOf("%") + 1).length();
      }
      return 7;
    }
    if (!this.rgbColorPattern.matcher(style).matches()
        && !rgbaColorPattern.matcher(style).matches()) {
      return 0;
    }
    return style.substring(0, style.indexOf(")") + 1).length();
  }
}
