package com.craftingdead.immerse.client.gui.component;

import java.util.function.BiConsumer;
import java.util.function.Function;
import io.noties.tumbleweed.TweenType;

public class SimpleTweenType<T> implements TweenType<T> {

  private final int size;
  private final Function<T, float[]> getter;
  private final BiConsumer<T, float[]> setter;

  public SimpleTweenType(int size, Function<T, float[]> getter, BiConsumer<T, float[]> setter) {
    this.size = size;
    this.getter = getter;
    this.setter = setter;
  }

  @Override
  public int getValuesSize() {
    return this.size;
  }

  @Override
  public void getValues(T t, float[] values) {
    System.arraycopy(this.getter.apply(t), 0, values, 0, this.size);
  }

  @Override
  public void setValues(T t, float[] values) {
    this.setter.accept(t, values);
  }
}
