package com.craftingdead.immerse.client.gui.property;

import com.craftingdead.immerse.client.util.RenderUtil;

public class ColourProperty implements IAnimatableProperty<Integer> {

  private int value;
  private int[] value4i;
  private float[] value4f;

  public ColourProperty(int value) {
    this.set(value);
  }

  public ColourProperty(int[] value4i) {
    this.setColour4i(value4i);
  }

  public ColourProperty(float[] value4f) {
    this.setAnimatedValues(value4f);
  }

  @Override
  public Integer get() {
    return this.value;
  }

  @Override
  public void set(Integer value) {
    this.value = value;
    this.value4i = RenderUtil.getColour4i(value);
    this.value4f = RenderUtil.getColour4f(this.value4i);
  }

  @Override
  public float[] getAnimatedValues() {
    return this.value4f;
  }

  @Override
  public void setAnimatedValues(float[] value) {
    System.arraycopy(value, 0, this.value4f, 0, 4);
    this.value4i = RenderUtil.getColour4i(this.value4f);
    this.value = RenderUtil.getColour(this.value4i);
  }

  public int[] getColour4i() {
    return this.value4i;
  }

  public void setColour4i(int[] value4i) {
    System.arraycopy(value4i, 0, this.value4i, 0, 4);
    this.value = RenderUtil.getColour(value4i);
    this.value4f = RenderUtil.getColour4f(this.value4i);
  }
}
