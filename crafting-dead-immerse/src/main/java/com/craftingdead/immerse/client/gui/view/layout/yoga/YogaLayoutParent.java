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
}
