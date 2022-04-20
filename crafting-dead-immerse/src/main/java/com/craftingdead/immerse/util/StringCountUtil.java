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

package com.craftingdead.immerse.util;

public class StringCountUtil {

  public static int integerAtStart(String str) {
    var count = 0;

    for (int index = 0; index < str.length(); index++) {
      // Early return to prevent parsing a floating number
      if (str.charAt(index) == '.') {
        return 0;
      }

      if (!Character.isDigit(str.charAt(index))
          && ((index != 0 || str.charAt(index) != '+') && str.charAt(index) != '-')) {
        break;
      }
      count++;
    }
    return count;
  }

  public static int floatAtStart(String str) {
    var count = 0;
    var decimalPart = false;

    for (int index = 0; index < str.length(); index++) {
      if (str.charAt(index) == '.' && !decimalPart) {
        decimalPart = true;
      } else if (!Character.isDigit(str.charAt(index))
          && ((index != 0 || str.charAt(index) != '+') && str.charAt(index) != '-')) {
        break;
      }
      count++;
    }
    return count;
  }

  public static int boolAtStart(String str) {
    return str.equalsIgnoreCase("true") ? 4 : 5;
  }
}
