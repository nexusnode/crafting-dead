/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.core.util;

import net.minecraft.util.text.IFormattableTextComponent;
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
    IFormattableTextComponent fullText = texts[0].copy();
    for (int i = 1; i < texts.length; i++) {
      fullText = fullText.append(texts[i].copy());
    }
    return fullText;
  }
}
