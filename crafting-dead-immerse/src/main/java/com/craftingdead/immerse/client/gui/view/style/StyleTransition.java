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

import java.util.Collections;
import java.util.Map;
import org.jdesktop.core.animation.timing.Interpolator;
import com.craftingdead.immerse.client.gui.view.property.Transition;

public record StyleTransition(TargetSelector[] selectors, long[] delays, long[] durations,
    Interpolator[] interpolators) {

  public void apply(Map<String, PropertyDispatcher<?>> dispatchers) {
    for (int i = 0; i < this.selectors.length; i++) {
      var selector = this.selectors[i];

      var delay = this.delays[i % this.delays.length];
      var duration = this.durations[i % this.durations.length];
      var interpolator = this.interpolators[i % this.interpolators.length];

      var transition = Transition.create(delay, duration, interpolator);

      for (var dispatcher : selector.select(dispatchers)) {
        dispatcher.setTransition(transition);
      }
    }
  }

  @FunctionalInterface
  public interface TargetSelector {

    TargetSelector ALL = Map::values;

    Iterable<PropertyDispatcher<?>> select(Map<String, PropertyDispatcher<?>> dispatchers);

    static TargetSelector forProperty(String property) {
      return dispatchers -> Collections.singleton(dispatchers.get(property));
    }
  }
}
