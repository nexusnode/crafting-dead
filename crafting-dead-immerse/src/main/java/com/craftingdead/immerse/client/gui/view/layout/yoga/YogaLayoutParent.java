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

import org.lwjgl.util.yoga.Yoga;
import com.craftingdead.immerse.client.gui.view.layout.LayoutParent;

public class YogaLayoutParent implements LayoutParent<YogaLayout> {

  private final long node;

  public YogaLayoutParent() {
    this.node = Yoga.YGNodeNew();
  }

  public final YogaLayoutParent setFlexDirection(FlexDirection flexDirection) {
    Yoga.YGNodeStyleSetFlexDirection(this.node, flexDirection.getYogaType());
    return this;
  }

  public final YogaLayoutParent setFlexWrap(FlexWrap flexDirection) {
    Yoga.YGNodeStyleSetFlexWrap(this.node, flexDirection.getYogaType());
    return this;
  }

  public final YogaLayoutParent setAlignItems(Align align) {
    Yoga.YGNodeStyleSetAlignItems(this.node, align.getYogaType());
    return this;
  }

  public final YogaLayoutParent setAlignContent(Align align) {
    Yoga.YGNodeStyleSetAlignContent(this.node, align.getYogaType());
    return this;
  }

  public final YogaLayoutParent setJustifyContent(Justify justify) {
    Yoga.YGNodeStyleSetJustifyContent(this.node, justify.getYogaType());
    return this;
  }

  @Override
  public void addChild(YogaLayout layout, int index) {
    Yoga.YGNodeInsertChild(this.node, layout.node, index);
  }

  @Override
  public void removeChild(YogaLayout layout) {
    Yoga.YGNodeRemoveChild(this.node, layout.node);
  }

  @Override
  public void layout(float width, float height) {
    Yoga.YGNodeCalculateLayout(this.node, width, height, Yoga.YGDirectionLTR);
  }

  @Override
  public void close() {
    Yoga.YGNodeFree(this.node);
  }
}
