/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
