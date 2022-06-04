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

import com.craftingdead.immerse.client.gui.view.style.Percentage;
import com.craftingdead.immerse.util.StringCountUtil;

public class PercentageParser implements ValueParser<Percentage> {

  public static final PercentageParser INSTANCE = new PercentageParser();

  private PercentageParser() {}

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

  @Override
  public Percentage parse(String style) {
    return style.contains("%")
        ? new Percentage(Float.parseFloat(style.replace("%", "")) / 100.0F)
        : new Percentage(Float.parseFloat(style));
  }
}
