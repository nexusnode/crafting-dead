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

import org.lwjgl.util.yoga.YGMeasureFunc;
import org.lwjgl.util.yoga.YGSize;
import org.lwjgl.util.yoga.Yoga;
import com.craftingdead.immerse.client.gui.view.Overflow;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.craftingdead.immerse.client.gui.view.layout.MeasureMode;
import net.minecraft.world.phys.Vec2;

public class YogaLayout implements Layout {

  /**
   * Maps a {@link Yoga} YGMeasureMode to a {@link MeasureMode}.
   */
  private static final MeasureMode[] measureModes =
      new MeasureMode[] {MeasureMode.UNDEFINED, MeasureMode.EXACTLY, MeasureMode.AT_MOST};

  /**
   * Maps a {@link Yoga} YGMeasureMode to a {@link MeasureMode}.
   */
  private static final Overflow[] overflows =
      new Overflow[] {Overflow.VISIBLE, Overflow.HIDDEN, Overflow.SCROLL};

  final long node;

  public YogaLayout() {
    this.node = Yoga.YGNodeNew();
  }

  public final YogaLayout setOverflow(Overflow overflow) {
    for (int i = 0; i < overflows.length; i++) {
      if (overflow == overflows[i]) {
        Yoga.YGNodeStyleSetOverflow(this.node, i);
        return this;
      }
    }
    throw new IllegalStateException("Unknown value: " + overflow);
  }

  public final YogaLayout setBorderWidth(float width) {
    Yoga.YGNodeStyleSetBorder(this.node, Yoga.YGEdgeAll, width);
    return this;
  }

  public final YogaLayout setLeftBorderWidth(float width) {
    Yoga.YGNodeStyleSetBorder(this.node, Yoga.YGEdgeLeft, width);
    return this;
  }

  public final YogaLayout setRightBorderWidth(float width) {
    Yoga.YGNodeStyleSetBorder(this.node, Yoga.YGEdgeRight, width);
    return this;
  }

  public final YogaLayout setTopBorderWidth(float width) {
    Yoga.YGNodeStyleSetBorder(this.node, Yoga.YGEdgeTop, width);
    return this;
  }

  public final YogaLayout setBottomBorderWidth(float width) {
    Yoga.YGNodeStyleSetBorder(this.node, Yoga.YGEdgeBottom, width);
    return this;
  }

  public final YogaLayout setLeft(float left) {
    Yoga.YGNodeStyleSetPosition(this.node, Yoga.YGEdgeLeft, left);
    return this;
  }

  public final YogaLayout setLeftPercent(float leftPercent) {
    Yoga.YGNodeStyleSetPositionPercent(this.node, Yoga.YGEdgeLeft, leftPercent);
    return this;
  }

  public final YogaLayout setRight(float right) {
    Yoga.YGNodeStyleSetPosition(this.node, Yoga.YGEdgeRight, right);
    return this;
  }

  public final YogaLayout setRightPercent(float rightPercent) {
    Yoga.YGNodeStyleSetPositionPercent(this.node, Yoga.YGEdgeRight, rightPercent);
    return this;
  }

  public final YogaLayout setTop(float top) {
    Yoga.YGNodeStyleSetPosition(this.node, Yoga.YGEdgeTop, top);
    return this;
  }

  public final YogaLayout setTopPercent(float topPercent) {
    Yoga.YGNodeStyleSetPositionPercent(this.node, Yoga.YGEdgeTop, topPercent);
    return this;
  }

  public final YogaLayout setBottom(float bottom) {
    Yoga.YGNodeStyleSetPosition(this.node, Yoga.YGEdgeBottom, bottom);
    return this;
  }

  public final YogaLayout setBottomPercent(float bottomPercent) {
    Yoga.YGNodeStyleSetPositionPercent(this.node, Yoga.YGEdgeBottom, bottomPercent);
    return this;
  }

  public final YogaLayout setLeftPadding(float leftPadding) {
    Yoga.YGNodeStyleSetPadding(this.node, Yoga.YGEdgeLeft, leftPadding);
    return this;
  }

  public final YogaLayout setLeftPaddingPercent(float leftPaddingPercent) {
    Yoga.YGNodeStyleSetPaddingPercent(this.node, Yoga.YGEdgeLeft, leftPaddingPercent);
    return this;
  }

  public final YogaLayout setRightPadding(float rightPadding) {
    Yoga.YGNodeStyleSetPadding(this.node, Yoga.YGEdgeRight, rightPadding);
    return this;
  }

  public final YogaLayout setRightPaddingPercent(float rightPaddingPercent) {
    Yoga.YGNodeStyleSetPaddingPercent(this.node, Yoga.YGEdgeRight, rightPaddingPercent);
    return this;
  }

  public final YogaLayout setTopPadding(float topPadding) {
    Yoga.YGNodeStyleSetPadding(this.node, Yoga.YGEdgeTop, topPadding);
    return this;
  }

  public final YogaLayout setTopPaddingPercent(float topPaddingPercent) {
    Yoga.YGNodeStyleSetPaddingPercent(this.node, Yoga.YGEdgeTop, topPaddingPercent);
    return this;
  }

  public final YogaLayout setBottomPadding(float bottomPadding) {
    Yoga.YGNodeStyleSetPadding(this.node, Yoga.YGEdgeBottom, bottomPadding);
    return this;
  }

  public final YogaLayout setBottomPaddingPercent(float bottomPaddingPercent) {
    Yoga.YGNodeStyleSetPaddingPercent(this.node, Yoga.YGEdgeBottom, bottomPaddingPercent);
    return this;
  }

  public final YogaLayout setPadding(float padding) {
    Yoga.YGNodeStyleSetPadding(this.node, Yoga.YGEdgeAll, padding);
    return this;
  }

  public final YogaLayout setPaddingPercent(float paddingPercent) {
    Yoga.YGNodeStyleSetPaddingPercent(this.node, Yoga.YGEdgeAll, paddingPercent);
    return this;
  }

  public final YogaLayout setLeftMargin(float leftMargin) {
    Yoga.YGNodeStyleSetMargin(this.node, Yoga.YGEdgeLeft, leftMargin);
    return this;
  }

  public final YogaLayout setLeftMarginPercent(float leftMarginPercent) {
    Yoga.YGNodeStyleSetMarginPercent(this.node, Yoga.YGEdgeLeft, leftMarginPercent);
    return this;
  }

  public final YogaLayout setRightMargin(float rightMargin) {
    Yoga.YGNodeStyleSetMargin(this.node, Yoga.YGEdgeRight, rightMargin);
    return this;
  }

  public final YogaLayout setRightMarginPercent(float rightMarginPercent) {
    Yoga.YGNodeStyleSetMarginPercent(this.node, Yoga.YGEdgeRight, rightMarginPercent);
    return this;
  }

  public final YogaLayout setTopMargin(float topMargin) {
    Yoga.YGNodeStyleSetMargin(this.node, Yoga.YGEdgeTop, topMargin);
    return this;
  }

  public final YogaLayout setTopMarginPercent(float topMarginPercent) {
    Yoga.YGNodeStyleSetMarginPercent(this.node, Yoga.YGEdgeTop, topMarginPercent);
    return this;
  }

  public final YogaLayout setBottomMargin(float bottomMargin) {
    Yoga.YGNodeStyleSetMargin(this.node, Yoga.YGEdgeBottom, bottomMargin);
    return this;
  }

  public final YogaLayout setBottomMarginPercent(float bottomMarginPercent) {
    Yoga.YGNodeStyleSetMarginPercent(this.node, Yoga.YGEdgeBottom, bottomMarginPercent);
    return this;
  }

  public final YogaLayout setMargin(float margin) {
    Yoga.YGNodeStyleSetMargin(this.node, Yoga.YGEdgeAll, margin);
    return this;
  }

  public final YogaLayout setMarginPercent(float marginPercent) {
    Yoga.YGNodeStyleSetMarginPercent(this.node, Yoga.YGEdgeAll, marginPercent);
    return this;
  }

  public final YogaLayout setLeftMarginAuto() {
    Yoga.YGNodeStyleSetMarginAuto(this.node, Yoga.YGEdgeLeft);
    return this;
  }

  public final YogaLayout setRightMarginAuto() {
    Yoga.YGNodeStyleSetMarginAuto(this.node, Yoga.YGEdgeRight);
    return this;
  }

  public final YogaLayout setTopMarginAuto() {
    Yoga.YGNodeStyleSetMarginAuto(this.node, Yoga.YGEdgeTop);
    return this;
  }

  public final YogaLayout setBottomMarginAuto() {
    Yoga.YGNodeStyleSetMarginAuto(this.node, Yoga.YGEdgeBottom);
    return this;
  }

  public final YogaLayout setMarginAuto() {
    Yoga.YGNodeStyleSetMarginAuto(this.node, Yoga.YGEdgeAll);
    return this;
  }

  public final YogaLayout setPositionType(PositionType positionType) {
    Yoga.YGNodeStyleSetPositionType(this.node, positionType == PositionType.ABSOLUTE
        ? Yoga.YGPositionTypeAbsolute
        : Yoga.YGPositionTypeRelative);
    return this;
  }

  public final YogaLayout setFlexGrow(float flexGrow) {
    Yoga.YGNodeStyleSetFlexGrow(this.node, flexGrow);
    return this;
  }

  public final YogaLayout setFlexShrink(float flexShrink) {
    Yoga.YGNodeStyleSetFlexShrink(this.node, flexShrink);
    return this;
  }

  public final YogaLayout setFlexBasis(float flexBasis) {
    Yoga.YGNodeStyleSetFlexBasis(this.node, flexBasis);
    return this;
  }

  public final YogaLayout setFlex(float flex) {
    Yoga.YGNodeStyleSetFlex(this.node, flex);
    return this;
  }

  public final YogaLayout setAspectRatio(float aspectRatio) {
    Yoga.YGNodeStyleSetAspectRatio(this.node, aspectRatio);
    return this;
  }

  public final YogaLayout setAlignSelf(Align align) {
    Yoga.YGNodeStyleSetAlignSelf(this.node, align.getYogaType());
    return this;
  }

  public final YogaLayout setDisplay(Display display) {
    Yoga.nYGNodeStyleSetDisplay(this.node, display.getYogaType());
    return this;
  }

  public YogaLayout setWidth(float width) {
    Yoga.YGNodeStyleSetWidth(this.node, width);
    return this;
  }

  public final YogaLayout setWidthPercent(float widthPercent) {
    Yoga.YGNodeStyleSetWidthPercent(this.node, widthPercent);
    return this;
  }

  public final YogaLayout setWidthAuto() {
    Yoga.YGNodeStyleSetWidthAuto(this.node);
    return this;
  }

  public YogaLayout setMaxWidth(float maxWidth) {
    Yoga.YGNodeStyleSetMaxWidth(this.node, maxWidth);
    return this;
  }

  public final YogaLayout setHeight(float height) {
    Yoga.YGNodeStyleSetHeight(this.node, height);
    return this;
  }

  public final YogaLayout setHeightPercent(float heightPercent) {
    Yoga.YGNodeStyleSetHeightPercent(this.node, heightPercent);
    return this;
  }

  public final YogaLayout setHeightAuto() {
    Yoga.YGNodeStyleSetHeightAuto(this.node);
    return this;
  }

  public YogaLayout setMaxHeight(float maxHeight) {
    Yoga.YGNodeStyleSetMaxHeight(this.node, maxHeight);
    return this;
  }

  public YogaLayout setMinWidth(float minWidth) {
    Yoga.YGNodeStyleSetMinWidth(this.node, minWidth);
    return this;
  }

  public YogaLayout setMinHeight(float minHeight) {
    Yoga.YGNodeStyleSetMinHeight(this.node, minHeight);
    return this;
  }

  @Override
  public Overflow getOverflow() {
    return overflows[Yoga.YGNodeStyleGetOverflow(this.node)];
  }

  @Override
  public float getLeft() {
    return Yoga.YGNodeLayoutGetLeft(this.node);
  }

  @Override
  public float getLeftPadding() {
    return Yoga.YGNodeLayoutGetPadding(this.node, Yoga.YGEdgeLeft);
  }

  @Override
  public float getLeftBorder() {
    return Yoga.YGNodeLayoutGetBorder(this.node, Yoga.YGEdgeLeft);
  }

  @Override
  public float getRight() {
    return Yoga.YGNodeLayoutGetRight(this.node);
  }

  @Override
  public float getRightPadding() {
    return Yoga.YGNodeLayoutGetPadding(this.node, Yoga.YGEdgeRight);
  }

  @Override
  public float getRightBorder() {
    return Yoga.YGNodeLayoutGetBorder(this.node, Yoga.YGEdgeRight);
  }

  @Override
  public float getTop() {
    return Yoga.YGNodeLayoutGetTop(this.node);
  }

  @Override
  public float getTopPadding() {
    return Yoga.YGNodeLayoutGetPadding(this.node, Yoga.YGEdgeTop);
  }

  @Override
  public float getTopBorder() {
    return Yoga.YGNodeLayoutGetBorder(this.node, Yoga.YGEdgeTop);
  }

  @Override
  public float getBottom() {
    return Yoga.YGNodeLayoutGetBottom(this.node);
  }

  @Override
  public float getBottomPadding() {
    return Yoga.YGNodeLayoutGetPadding(this.node, Yoga.YGEdgeBottom);
  }

  @Override
  public float getBottomBorder() {
    return Yoga.YGNodeLayoutGetBorder(this.node, Yoga.YGEdgeBottom);
  }

  @Override
  public float getWidth() {
    return Yoga.YGNodeLayoutGetWidth(this.node);
  }

  @Override
  public float getHeight() {
    return Yoga.YGNodeLayoutGetHeight(this.node);
  }

  @Override
  public void layout() {
    Yoga.YGNodeMarkDirty(this.node);
  }

  @Override
  public void close() {
    Yoga.YGNodeFree(this.node);
  }

  @Override
  public void setMeasureFunction(MeasureFunction measureFunction) {
    Yoga.YGNodeSetMeasureFunc(this.node, (node, width, widthMode, height, heightMode) -> {
      Vec2 size =
          measureFunction.measure(measureModes[widthMode], width, measureModes[heightMode], height);
      return YGMeasureFunc.toLong(YGSize.create()
          .width(size.x)
          .height(size.y));
    });
  }
}
