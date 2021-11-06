package com.craftingdead.immerse.client.gui.view;

import java.util.Set;
import javax.annotation.Nonnull;
import com.google.common.collect.Sets;

public class CompositeStyleProperty<T> extends StyleProperty<T> {

  private final Set<StyleProperty<T>> properties;

  @SafeVarargs
  protected CompositeStyleProperty(String name, StyleProperty<T>... properties) {
    super(name);
    this.properties = Sets.newHashSet(properties);
  }

  @Override
  protected StyleProperty<T> setBaseValue(@Nonnull T baseValue, boolean define) {
    this.properties.forEach(property -> property.setBaseValue(baseValue, define));
    return this;
  }

  @Override
  public StyleProperty<T> setTransition(Transition<T> transition) {
    this.properties.forEach(property -> property.setTransition(transition));
    return this;
  }

  @Override
  public StyleProperty<T> registerState(T value, State... states) {
    this.properties.forEach(property -> property.registerState(value, states));
    return this;
  }

  @Override
  public StyleProperty<T> set(@Nonnull T value) {
    this.properties.forEach(property -> property.set(value));
    return this;
  }

  @SafeVarargs
  public static <T> CompositeStyleProperty<T> create(String name, StyleProperty<T>... properties) {
    return new CompositeStyleProperty<>(name, properties);
  }
}
