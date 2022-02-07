package com.craftingdead.immerse.client.gui.view.style.shorthand;

import java.util.ArrayList;
import java.util.function.Predicate;
import com.craftingdead.immerse.client.gui.view.style.PropertyDispatcher;
import com.craftingdead.immerse.client.gui.view.style.StyleSource;
import com.craftingdead.immerse.client.gui.view.style.adapter.StyleTranslatorRegistry;
import com.craftingdead.immerse.client.gui.view.style.adapter.StyleValidator;

public class ShorthandDispatcher<T> implements PropertyDispatcher<T> {

  private final String name;
  private final StyleValidator<T> validator;
  private final ShorthandArgMapper argMapper;
  private final PropertyDispatcher<T>[] properties;

  @SafeVarargs
  public ShorthandDispatcher(String name, StyleValidator<T> validator, ShorthandArgMapper argMapper,
      PropertyDispatcher<T>... properties) {
    this.name = name;
    this.validator = validator;
    this.argMapper = argMapper;
    this.properties = properties;
  }

  @Override
  public boolean defineState(StyleSource source, String rawValue, int state) {
    var values = new ArrayList<String>();
    var current = rawValue;
    while (!current.isEmpty()) {
      var validated = this.validator.validate(current);
      if (validated == 0) {
        break;
      }

      values.add(current.substring(0, validated));
      current = current.substring(validated).trim();
    }

    for (int i = 0; i < values.size(); i++) {
      for (int childIndex : this.argMapper.map(i, values.size())) {
        this.properties[childIndex].defineState(source, values.get(i), state);
      }
    }

    return true;
  }

  @Override
  public void reset(Predicate<StyleSource> filter) {}

  @Override
  public String getName() {
    return this.name;
  }

  @SafeVarargs
  public static <T> ShorthandDispatcher<T> create(String name, Class<T> type,
      ShorthandArgMapper argMapper, PropertyDispatcher<T>... properties) {
    return new ShorthandDispatcher<>(name, StyleTranslatorRegistry.getInstance().getValidator(type),
        argMapper, properties);
  }
}
