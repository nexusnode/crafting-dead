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

import java.util.Locale;

public class EnumParser<T extends Enum<T>> implements ValueParser<T> {

  private final Class<T> type;

  public EnumParser(Class<T> type) {
    this.type = type;
  }

  @Override
  public int validate(String style) {
    return style.length();
  }

  @Override
  public T parse(String style) {
    return Enum.valueOf(this.type, style.toUpperCase(Locale.ROOT).replace('-', '_'));
  }
}
