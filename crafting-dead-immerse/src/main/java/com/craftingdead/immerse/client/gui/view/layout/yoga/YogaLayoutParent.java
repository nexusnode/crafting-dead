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

import org.lwjgl.util.yoga.Yoga;
import com.craftingdead.immerse.client.gui.view.ViewStyle;
import com.craftingdead.immerse.client.gui.view.layout.Align;
import com.craftingdead.immerse.client.gui.view.layout.FlexDirection;
import com.craftingdead.immerse.client.gui.view.layout.Justify;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.craftingdead.immerse.client.gui.view.layout.LayoutParent;
import com.craftingdead.immerse.client.gui.view.layout.Wrap;

public final class YogaLayoutParent implements LayoutParent {

  private final long node;

  public YogaLayoutParent() {
    this.node = Yoga.YGNodeNew();
  }

  @Override
  public void setAll(ViewStyle style) {
    this.setFlexDirection(style.flexDirection.get());
    this.setFlexWrap(style.flexWrap.get());
    this.setAlignItems(style.alignItems.get());
    this.setAlignContent(style.alignContent.get());
    this.setJustifyContent(style.justifyContent.get());
  }

  @Override
  public void setFlexDirection(FlexDirection flexDirection) {
    Yoga.YGNodeStyleSetFlexDirection(this.node, YogaUtil.getFlexDirection(flexDirection));
  }

  @Override
  public void setFlexWrap(Wrap wrap) {
    Yoga.YGNodeStyleSetFlexWrap(this.node, YogaUtil.getWrap(wrap));
  }

  @Override
  public void setAlignItems(Align align) {
    Yoga.YGNodeStyleSetAlignItems(this.node, YogaUtil.getAlign(align));
  }

  @Override
  public void setAlignContent(Align align) {
    Yoga.YGNodeStyleSetAlignContent(this.node, YogaUtil.getAlign(align));
  }

  @Override
  public void setJustifyContent(Justify justify) {
    Yoga.YGNodeStyleSetJustifyContent(this.node, YogaUtil.getJustify(justify));
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
    Yoga.YGNodeMarkDirtyAndPropogateToDescendants(this.node);
    Yoga.YGNodeCalculateLayout(this.node, width, height, Yoga.YGDirectionLTR);
  }

  @Override
  public float getContentWidth() {
    return Yoga.YGNodeLayoutGetWidth(this.node);
  }

  @Override
  public float getContentHeight() {
    return Yoga.YGNodeLayoutGetHeight(this.node);
  }

  @Override
  public void close() {
    Yoga.YGNodeFree(this.node);
  }
}
