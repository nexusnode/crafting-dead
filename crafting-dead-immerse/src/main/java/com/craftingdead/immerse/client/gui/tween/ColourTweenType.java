package com.craftingdead.immerse.client.gui.tween;

import java.util.function.Function;
import com.craftingdead.immerse.client.gui.view.Colour;
import io.noties.tumbleweed.TweenType;

public class ColourTweenType<T> implements TweenType<T> {

  private final Function<T, Colour> getter;

  public ColourTweenType(Function<T, Colour> getter) {
    this.getter = getter;
  }

  @Override
  public int getValuesSize() {
    return 4;
  }

  @Override
  public void getValues(T t, float[] values) {
    System.arraycopy(this.getter.apply(t).getColour4f(), 0, values, 0, 4);
  }

  @Override
  public void setValues(T t, float[] values) {
    this.getter.apply(t).setColour4f(values);
  }
}
