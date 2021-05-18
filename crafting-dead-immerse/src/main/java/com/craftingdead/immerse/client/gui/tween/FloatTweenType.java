package com.craftingdead.immerse.client.gui.tween;

import java.util.function.BiConsumer;
import java.util.function.Function;
import io.noties.tumbleweed.TweenType;

public class FloatTweenType<T> implements TweenType<T> {

  private final Function<T, Float> getter;
  private final BiConsumer<T, Float> setter;

  public FloatTweenType(Function<T, Float> getter, BiConsumer<T, Float> setter) {
    this.getter = getter;
    this.setter = setter;
  }

  @Override
  public int getValuesSize() {
    return 1;
  }

  @Override
  public void getValues(T t, float[] values) {
    values[0] = this.getter.apply(t);
  }

  @Override
  public void setValues(T t, float[] values) {
    this.setter.accept(t, values[0]);
  }
}
