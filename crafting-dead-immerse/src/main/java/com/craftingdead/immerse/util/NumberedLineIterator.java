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

package com.craftingdead.immerse.util;

import org.apache.commons.io.LineIterator;

import java.io.Reader;

public class NumberedLineIterator extends LineIterator {

  private int lineNumber;

  public NumberedLineIterator(Reader reader) throws IllegalArgumentException {
    super(reader);
    this.lineNumber = -1;
  }

  @Override
  public String nextLine() {
    this.lineNumber++;
    return super.nextLine();
  }

  public int getLineNumber() {
    return this.lineNumber;
  }
}
