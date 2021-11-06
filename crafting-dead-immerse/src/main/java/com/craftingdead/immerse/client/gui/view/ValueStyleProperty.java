package com.craftingdead.immerse.client.gui.view;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;
import com.google.common.collect.Sets;

public class ValueStyleProperty<T> extends StyleProperty<T> {

  private final Class<T> type;

  private boolean defined;

  private T baseValue;

  private final Map<Set<State>, T> stateValues = new HashMap<>();

  private T value;

  private Transition<T> transition = Transition.instant();

  protected ValueStyleProperty(String name, Class<T> type, @Nonnull T baseValue) {
    super(name);
    this.type = type;
    this.setBaseValue(baseValue, false);
  }

  @Override
  protected StyleProperty<T> setBaseValue(@Nonnull T baseValue, boolean defined) {
    Objects.nonNull(baseValue);
    this.baseValue = baseValue;
    this.defined = defined;
    this.stateValues.put(Collections.emptySet(), this.baseValue);
    this.value = this.baseValue;
    return this;
  }

  @Override
  public StyleProperty<T> setTransition(Transition<T> transition) {
    this.transition = transition;
    return this;
  }

  @Override
  public StyleProperty<T> registerState(T value, State... states) {
    if (states.length == 0) {
      return this;
    }
    this.stateValues.put(Sets.newHashSet(states), value);
    return this;
  }

  @Override
  public StyleProperty<T> set(@Nonnull T value) {
    this.value = value;
    return this;
  }

  public boolean isDefined() {
    return this.defined;
  }

  public T get() {
    return this.value;
  }

  public void transition(Set<State> states, boolean instant) {
    T newValue = this.stateValues.get(states);
    if (newValue != null && !Objects.equals(newValue, this.value)) {
      if (instant) {
        this.set(newValue);
      } else {


//        this.value = newValue;
         this.transition.transition(this, newValue);
      }
    }
  }

  public Class<T> getValueType() {
    return this.type;
  }

  public static <T> ValueStyleProperty<T> create(String name, Class<T> type, @Nonnull T baseValue) {
    return new ValueStyleProperty<>(name, type, baseValue);
  }
}
