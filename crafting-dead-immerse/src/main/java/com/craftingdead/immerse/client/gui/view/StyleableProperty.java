package com.craftingdead.immerse.client.gui.view;

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

public class StyleableProperty<T> extends ValueProperty<T> implements PropertyDispatcher<T> {

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
  public boolean defineState(StyleSource source, String style, int state) {
    var current = this.sources.get(state);
    if (current == null || source.compareTo(current) > -1) {
      this.sources.put(state, source);
      var value = this.styleCache.computeIfAbsent(style, this.decoder::decode);
      this.defineState(value, state);
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
