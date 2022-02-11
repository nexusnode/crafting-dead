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
