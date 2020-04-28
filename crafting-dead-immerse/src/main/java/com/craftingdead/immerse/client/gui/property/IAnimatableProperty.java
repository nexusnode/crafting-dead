package com.craftingdead.immerse.client.gui.property;

import aurelienribon.tweenengine.TweenAccessor;

public interface IAnimatableProperty<T>
    extends IProperty<T>, TweenAccessor<IAnimatableProperty<?>> {

  float[] getAnimatedValues();

  void setAnimatedValues(float[] value);

  @Override
  default int getValues(IAnimatableProperty<?> target, int tweenType, float[] returnValues) {
    float[] values = target.getAnimatedValues();
    System.arraycopy(values, 0, returnValues, 0, values.length);
    return values.length;
  }

  @Override
  default void setValues(IAnimatableProperty<?> target, int tweenType, float[] newValues) {
    target.setAnimatedValues(newValues);
  }
}
