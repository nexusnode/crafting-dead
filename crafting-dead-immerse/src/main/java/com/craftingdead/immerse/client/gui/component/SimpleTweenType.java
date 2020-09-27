package com.craftingdead.immerse.client.gui.component;

import java.util.function.BiConsumer;
import java.util.function.Function;
import io.noties.tumbleweed.TweenType;
import net.minecraft.util.math.Vec2f;

public class SimpleTweenType<T> implements TweenType<T> {

  private final int size;
  private final Function<T, float[]> getter;
  private final BiConsumer<T, float[]> setter;

  public SimpleTweenType(Function<T, Float> getter, BiConsumer<T, Float> setter) {
    this(1, t -> new float[] {getter.apply(t)}, (t, v) -> setter.accept(t, v[0]));
  }

  public SimpleTweenType(Vec2fGetter<T> getter, Vec2fSetter<T> setter) {
    this(1, t -> {
      Vec2f v = getter.get(t);
      return new float[] {v.x, v.y};
    }, (t, v) -> setter.set(t, new Vec2f(v[0], v[1])));
  }

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

  @FunctionalInterface
  public static interface Vec2fGetter<T> {
    Vec2f get(T t);
  }

  @FunctionalInterface
  public static interface Vec2fSetter<T> {
    void set(T t, Vec2f v);
  }
}
