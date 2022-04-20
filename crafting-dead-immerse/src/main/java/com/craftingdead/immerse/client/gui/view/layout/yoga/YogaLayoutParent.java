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

package com.craftingdead.immerse.client.gui.view.layout.yoga;

import java.util.function.Consumer;
import org.lwjgl.util.yoga.Yoga;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.craftingdead.immerse.client.gui.view.layout.LayoutParent;
import com.craftingdead.immerse.client.gui.view.property.StyleableProperty;
import com.craftingdead.immerse.client.gui.view.state.StateListener;
import com.craftingdead.immerse.client.gui.view.style.PropertyDispatcher;

public class YogaLayoutParent implements LayoutParent {

  private final StyleableProperty<YogaFlexDirection> flexDirection;
  private final StyleableProperty<YogaWrap> flexWrap;
  private final StyleableProperty<YogaAlign> alignItems;
  private final StyleableProperty<YogaAlign> alignContent;
  private final StyleableProperty<YogaJustify> justifyContent;

  private final long node;

  public YogaLayoutParent() {
    this.node = Yoga.YGNodeNew();
    this.flexDirection = StyleableProperty.create("flex-direction", YogaFlexDirection.class,
        YogaFlexDirection.COLUMN, this::setFlexDirection);
    this.flexWrap = StyleableProperty.create("flex-wrap", YogaWrap.class,
        YogaWrap.NO_WRAP, this::setFlexWrap);
    this.alignItems = StyleableProperty.create("align-items", YogaAlign.class,
        YogaAlign.STRETCH, this::setAlignItems);
    this.alignContent = StyleableProperty.create("align-content", YogaAlign.class,
        YogaAlign.FLEX_START, this::setAlignContent);
    this.justifyContent = StyleableProperty.create("justify-content", YogaJustify.class,
        YogaJustify.FLEX_START, this::setJustifyContent);
  }

  public final YogaLayoutParent setFlexDirection(YogaFlexDirection flexDirection) {
    Yoga.YGNodeStyleSetFlexDirection(this.node, flexDirection.getYogaType());
    return this;
  }

  public final YogaLayoutParent setFlexWrap(YogaWrap flexDirection) {
    Yoga.YGNodeStyleSetFlexWrap(this.node, flexDirection.getYogaType());
    return this;
  }

  public final YogaLayoutParent setAlignItems(YogaAlign align) {
    Yoga.YGNodeStyleSetAlignItems(this.node, align.getYogaType());
    return this;
  }

  public final YogaLayoutParent setAlignContent(YogaAlign align) {
    Yoga.YGNodeStyleSetAlignContent(this.node, align.getYogaType());
    return this;
  }

  public final YogaLayoutParent setJustifyContent(YogaJustify justify) {
    Yoga.YGNodeStyleSetJustifyContent(this.node, justify.getYogaType());
    return this;
  }

  @Override
  public Layout addChild(int index) {
    if (Yoga.YGNodeHasMeasureFunc(this.node)) {
      throw new IllegalStateException("This shouldn't happen...");
    }
    var layout = new YogaLayout();
    Yoga.YGNodeInsertChild(this.node, layout.node, index);
    return layout;
  }

  @Override
  public void removeChild(Layout layout) {
    Yoga.YGNodeRemoveChild(this.node, ((YogaLayout) layout).node);
  }

  @Override
  public void layout(float width, float height) {
    Yoga.YGNodeCalculateLayout(this.node, width, height, Yoga.YGDirectionLTR);
  }

  @Override
  public void close() {
    Yoga.YGNodeFree(this.node);
  }

  @Override
  public void gatherDispatchers(Consumer<PropertyDispatcher<?>> consumer) {
    this.gatherStyleProperties(consumer);
  }

  @Override
  public void gatherListeners(Consumer<StateListener> consumer) {
    this.gatherStyleProperties(consumer);
  }

  private void gatherStyleProperties(Consumer<? super StyleableProperty<?>> consumer) {
    consumer.accept(this.flexDirection);
    consumer.accept(this.flexWrap);
    consumer.accept(this.alignItems);
    consumer.accept(this.alignContent);
    consumer.accept(this.justifyContent);
  }
}
