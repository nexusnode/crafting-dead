/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.client.gui.view.layout;

import java.util.function.Consumer;
import javax.annotation.Nullable;
import com.craftingdead.immerse.client.gui.view.Overflow;
import com.craftingdead.immerse.client.gui.view.state.StateListener;
import com.craftingdead.immerse.client.gui.view.style.PropertyDispatcher;
import net.minecraft.world.phys.Vec2;

public interface Layout {

  default Overflow getOverflow() {
    return Overflow.VISIBLE;
  }

  default float getLeft() {
    return 0;
  }

  default float getLeftPadding() {
    return 0;
  }

  default float getLeftBorder() {
    return 0;
  }

  default float getRight() {
    return 0;
  }

  default float getRightPadding() {
    return 0;
  }

  default float getRightBorder() {
    return 0;
  }

  default float getTop() {
    return 0;
  }

  default float getTopPadding() {
    return 0;
  }

  default float getTopBorder() {
    return 0;
  }

  default float getBottom() {
    return 0;
  }

  default float getBottomPadding() {
    return 0;
  }

  default float getBottomBorder() {
    return 0;
  }

  float getWidth();

  float getHeight();

  default void setMeasureFunction(@Nullable MeasureFunction measureFunction) {}

  default void markDirty() {}

  default void close() {}

  default void gatherDispatchers(Consumer<PropertyDispatcher<?>> consumer) {}

  default void gatherListeners(Consumer<StateListener> consumer) {}

  interface MeasureFunction {

    Vec2 measure(MeasureMode widthMode, float width, MeasureMode heightMode, float height);
  }
}
