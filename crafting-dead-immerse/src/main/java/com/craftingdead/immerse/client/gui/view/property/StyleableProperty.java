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

package com.craftingdead.immerse.client.gui.view.property;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import com.craftingdead.immerse.client.gui.view.style.PropertyDispatcher;
import com.craftingdead.immerse.client.gui.view.style.StyleSource;
import com.craftingdead.immerse.client.gui.view.style.adapter.StyleDecoder;
import com.craftingdead.immerse.client.gui.view.style.adapter.StyleTranslatorRegistry;
import com.craftingdead.immerse.client.gui.view.style.adapter.StyleValidator;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class StyleableProperty<T> extends StatefulProperty<T> implements PropertyDispatcher<T> {

  private final Int2ObjectMap<StyleSource> sources = new Int2ObjectOpenHashMap<>();

  private final Map<String, T> styleCache = new HashMap<>();

  private final StyleValidator<T> validator;
  private final StyleDecoder<T> decoder;

  public StyleableProperty(String name, Class<T> type, ValueAccessor<T> accessor) {
    super(name, type, accessor);
    this.validator = StyleTranslatorRegistry.getInstance().getValidator(type);
    this.decoder = StyleTranslatorRegistry.getInstance().getDecoder(type);
  }

  @Override
  public void defineState(T value, int state) {
    super.defineState(value, state);
    this.sources.put(state, StyleSource.CODE);
  }

  @Override
  public boolean defineState(StyleSource source, String style, int state) {
    var current = this.sources.get(state);
    if (current == null || source.compareTo(current) > -1) {
      this.sources.put(state, source);
      var value = this.styleCache.computeIfAbsent(style, this.decoder::decode);
      this.setState(state, value);
      return true;
    }
    return false;
  }

  @Override
  public void reset(Predicate<StyleSource> filter) {
    this.sources.int2ObjectEntrySet().removeIf(entry -> {
      if (filter.test(entry.getValue())) {
        this.resetState(entry.getIntKey());
        return true;
      }
      return false;
    });
  }

  @Override
  public int validate(String style) {
    return this.validator.validate(style);
  }

  public static <T> StyleableProperty<T> create(String name, Class<T> type, T defaultValue) {
    return create(name, type, defaultValue, null);
  }

  public static <T> StyleableProperty<T> create(String name, Class<T> type, T defaultValue,
      @Nullable Consumer<T> listener) {
    return new StyleableProperty<>(name, type, ValueAccessor.simple(defaultValue, listener));
  }
}
