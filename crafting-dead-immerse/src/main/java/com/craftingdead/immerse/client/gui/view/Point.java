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
