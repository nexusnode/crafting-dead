package com.craftingdead.immerse.client.gui.view;

import it.unimi.dsi.fastutil.floats.FloatConsumer;

public record Point(Type type, float value) {

  public static final Point ZERO = new Point(Type.FIXED, 0.0F);
  public static final Point AUTO = new Point(Type.AUTO, 0.0F);

  public void dispatch(FloatConsumer fixedConsumer, FloatConsumer percentageConsumer,
      Runnable autoConsumer) {
    switch (this.type) {
      case FIXED: {
        fixedConsumer.accept(this.value);
        break;
      }
      case PERCENTAGE: {
        percentageConsumer.accept(this.value);
        break;
      }
      case AUTO: {
        autoConsumer.run();
        break;
      }
    };
  }

  public static Point fixed(float value) {
    return new Point(Type.FIXED, value);
  }

  public static Point percentage(float value) {
    return new Point(Type.PERCENTAGE, value);
  }

  public enum Type {
    FIXED,
    PERCENTAGE,
    AUTO;
  }
}
