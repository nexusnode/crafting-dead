package com.craftingdead.core.util;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Class with utility methods for ITextComponent.
 */
public class Text {
  /**
   * Shortest way to instantiate a {@link TranslationTextComponent}.
   *
   * @param key - The key in the lang file
   * @param objects - Objects that will be used by String.format()
   */
  public static TranslationTextComponent translate(String key, Object... objects) {
    return new TranslationTextComponent(key, objects);
  }

  /**
   * Shortest way to instantiate a {@link StringTextComponent}.
   *
   * @param object - An object that will be turned into a string.
   */
  public static StringTextComponent of(Object object) {
    if (object instanceof StringTextComponent) {
      return (StringTextComponent) object;
    }
    return new StringTextComponent(object.toString());
  }

  public static ITextComponent copyAndJoin(ITextComponent... texts) {
    ITextComponent fullText = texts[0].deepCopy();
    for (int i = 1; i < texts.length; i++) {
      fullText = fullText.appendSibling(texts[i].deepCopy());
    }
    return fullText;
  }
}
