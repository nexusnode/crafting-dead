/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.client.gui.view;

import com.craftingdead.immerse.client.gui.view.calc.CalcExpressionNode;
import it.unimi.dsi.fastutil.floats.FloatConsumer;

public interface Length {

  public static final Length ZERO = new Fixed(0.0F);
  public static final Length AUTO = new Length() {

    @Override
    public void dispatch(FloatConsumer fixed, FloatConsumer percentage, Runnable auto) {
      auto.run();
    }

    @Override
    public float valueForLength(float maximumValue) {
      return maximumValue;
    }

    @Override
    public Type type() {
      return Type.AUTO;
    }
  };
  public static final Length UNDEFINED = new Length() {

    @Override
    public void dispatch(FloatConsumer fixed, FloatConsumer percentage, Runnable auto) {
      throw new IllegalStateException("Undefined.");
    }

    @Override
    public float valueForLength(float maximumValue) {
      throw new IllegalStateException("Undefined.");
    }

    @Override
    public Type type() {
      return Type.UNDEFINED;
    }
  };

  default void dispatch(FloatConsumer fixed, FloatConsumer percentage) {
    this.dispatch(fixed, percentage, () -> {
      throw new IllegalStateException("Auto not supported.");
    });
  }

  void dispatch(FloatConsumer fixed, FloatConsumer percentage, Runnable auto);

  default float percent() {
    throw new IllegalStateException("Not a percentage.");
  }

  float valueForLength(float maximumValue);

  Type type();

  public static Length fixed(float value) {
    return new Fixed(value);
  }

  public static Length percentage(float value) {
    return new Percentage(value);
  }

  public static Length calculated(CalcExpressionNode root) {
    return new Calculated(root);
  }

  record Fixed(float value) implements Length {

    @Override
    public void dispatch(FloatConsumer fixed, FloatConsumer percentage, Runnable auto) {
      fixed.accept(this.value);
    }

    @Override
    public float valueForLength(float maximumValue) {
      return this.value;
    }

    @Override
    public Type type() {
      return Type.FIXED;
    }
  }

  record Percentage(float value) implements Length {

    @Override
    public void dispatch(FloatConsumer fixed, FloatConsumer percentage, Runnable auto) {
      percentage.accept(this.value);
    }

    @Override
    public float percent() {
      return this.value;
    }

    @Override
    public float valueForLength(float maximumValue) {
      return maximumValue * this.value / 100.0F;
    }

    @Override
    public Type type() {
      return Type.PERCENTAGE;
    }
  }

  record Calculated(CalcExpressionNode root) implements Length {

    @Override
    public void dispatch(FloatConsumer fixed, FloatConsumer percentage, Runnable auto) {
      throw new IllegalStateException("Undefined.");
    }

    @Override
    public float valueForLength(float maximumValue) {
      return this.root.evaluate(maximumValue);
    }

    @Override
    public Type type() {
      return Type.CALCULATED;
    }
  }

  enum ValueRange {
    ALL,
    NON_NEGATIVE;
  }

  enum Type {

    FIXED,
    PERCENTAGE,
    AUTO,
    CALCULATED,
    UNDEFINED;
  }
}
