package com.craftingdead.immerse.client.gui.tween;

import java.util.function.BiConsumer;
import java.util.function.Function;
import io.noties.tumbleweed.TweenType;
import net.minecraft.util.math.vector.Vector2f;

public class Vector2fTweenType<T> implements TweenType<T> {

  private final Function<T, Vector2f> getter;
  private final BiConsumer<T, Vector2f> setter;

  public Vector2fTweenType(Function<T, Vector2f> getter, BiConsumer<T, Vector2f> setter) {
    this.getter = getter;
    this.setter = setter;
  }

  @Override
  public int getValuesSize() {
    return 3;
  }

  @Override
  public void getValues(T t, float[] values) {
    Vector2f vec = this.getter.apply(t);
    values[0] = vec.x;
    values[1] = vec.y;
  }

  @Override
  public void setValues(T t, float[] values) {
    this.setter.accept(t, new Vector2f(values[0], values[1]));
  }
}
