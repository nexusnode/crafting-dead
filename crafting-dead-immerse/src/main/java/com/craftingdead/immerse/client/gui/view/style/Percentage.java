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

package com.craftingdead.immerse.client.gui.view.style;

import org.jdesktop.core.animation.timing.Evaluator;
import org.jdesktop.core.animation.timing.evaluators.KnownEvaluators;

/**
 * Value which can be parsed as either 0.0 to 1.0 or 0% to 100%.
 */
public record Percentage(float value) {

  public static final Percentage ZERO = new Percentage(0.0F);
  public static final Percentage ONE_HUNDRED = new Percentage(1.0F);

  static {
    KnownEvaluators.getInstance().register(new Evaluator<Percentage>() {

      @Override
      public Percentage evaluate(Percentage v0, Percentage v1, double fraction) {
        return new Percentage(v0.value + ((v1.value - v0.value) * (float) fraction));
      }

      @Override
      public Class<Percentage> getEvaluatorClass() {
        return Percentage.class;
      }
    });
  }
}
