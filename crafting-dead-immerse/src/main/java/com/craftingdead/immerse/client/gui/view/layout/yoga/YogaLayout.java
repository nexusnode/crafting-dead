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

import javax.annotation.Nullable;
import org.lwjgl.util.yoga.YGMeasureFunc;
import org.lwjgl.util.yoga.YGSize;
import org.lwjgl.util.yoga.Yoga;
import com.craftingdead.immerse.client.gui.view.Overflow;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.craftingdead.immerse.client.gui.view.layout.MeasureMode;

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

  private boolean measureFunctionPresent;
  private boolean closed;

  public YogaLayout() {
    this.node = Yoga.YGNodeNew();
  }

  public final YogaLayout setOverflow(Overflow overflow) {
    this.checkClosed();
    for (int i = 0; i < overflows.length; i++) {
      if (overflow == overflows[i]) {
        Yoga.YGNodeStyleSetOverflow(this.node, i);
        return this;
      }
    }
    throw new IllegalStateException("Unknown value: " + overflow);
  }

  public final YogaLayout setBorderWidth(float width) {
    this.checkClosed();
    Yoga.YGNodeStyleSetBorder(this.node, Yoga.YGEdgeAll, width);
    return this;
  }

  public final YogaLayout setLeftBorderWidth(float width) {
    this.checkClosed();
    Yoga.YGNodeStyleSetBorder(this.node, Yoga.YGEdgeLeft, width);
    return this;
  }

  public final YogaLayout setRightBorderWidth(float width) {
    this.checkClosed();
    Yoga.YGNodeStyleSetBorder(this.node, Yoga.YGEdgeRight, width);
    return this;
  }

  public final YogaLayout setTopBorderWidth(float width) {
    this.checkClosed();
    Yoga.YGNodeStyleSetBorder(this.node, Yoga.YGEdgeTop, width);
    return this;
  }

  public final YogaLayout setBottomBorderWidth(float width) {
    this.checkClosed();
    Yoga.YGNodeStyleSetBorder(this.node, Yoga.YGEdgeBottom, width);
    return this;
  }

  public final YogaLayout setLeft(float left) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPosition(this.node, Yoga.YGEdgeLeft, left);
    return this;
  }

  public final YogaLayout setLeftPercent(float leftPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPositionPercent(this.node, Yoga.YGEdgeLeft, leftPercent);
    return this;
  }

  public final YogaLayout setRight(float right) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPosition(this.node, Yoga.YGEdgeRight, right);
    return this;
  }

  public final YogaLayout setRightPercent(float rightPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPositionPercent(this.node, Yoga.YGEdgeRight, rightPercent);
    return this;
  }

  public final YogaLayout setTop(float top) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPosition(this.node, Yoga.YGEdgeTop, top);
    return this;
  }

  public final YogaLayout setTopPercent(float topPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPositionPercent(this.node, Yoga.YGEdgeTop, topPercent);
    return this;
  }

  public final YogaLayout setBottom(float bottom) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPosition(this.node, Yoga.YGEdgeBottom, bottom);
    return this;
  }

  public final YogaLayout setBottomPercent(float bottomPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPositionPercent(this.node, Yoga.YGEdgeBottom, bottomPercent);
    return this;
  }

  public final YogaLayout setLeftPadding(float leftPadding) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPadding(this.node, Yoga.YGEdgeLeft, leftPadding);
    return this;
  }

  public final YogaLayout setLeftPaddingPercent(float leftPaddingPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPaddingPercent(this.node, Yoga.YGEdgeLeft, leftPaddingPercent);
    return this;
  }

  public final YogaLayout setRightPadding(float rightPadding) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPadding(this.node, Yoga.YGEdgeRight, rightPadding);
    return this;
  }

  public final YogaLayout setRightPaddingPercent(float rightPaddingPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPaddingPercent(this.node, Yoga.YGEdgeRight, rightPaddingPercent);
    return this;
  }

  public final YogaLayout setTopPadding(float topPadding) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPadding(this.node, Yoga.YGEdgeTop, topPadding);
    return this;
  }

  public final YogaLayout setTopPaddingPercent(float topPaddingPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPaddingPercent(this.node, Yoga.YGEdgeTop, topPaddingPercent);
    return this;
  }

  public final YogaLayout setBottomPadding(float bottomPadding) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPadding(this.node, Yoga.YGEdgeBottom, bottomPadding);
    return this;
  }

  public final YogaLayout setBottomPaddingPercent(float bottomPaddingPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPaddingPercent(this.node, Yoga.YGEdgeBottom, bottomPaddingPercent);
    return this;
  }

  public final YogaLayout setPadding(float padding) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPadding(this.node, Yoga.YGEdgeAll, padding);
    return this;
  }

  public final YogaLayout setPaddingPercent(float paddingPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPaddingPercent(this.node, Yoga.YGEdgeAll, paddingPercent);
    return this;
  }

  public final YogaLayout setLeftMargin(float leftMargin) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMargin(this.node, Yoga.YGEdgeLeft, leftMargin);
    return this;
  }

  public final YogaLayout setLeftMarginPercent(float leftMarginPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMarginPercent(this.node, Yoga.YGEdgeLeft, leftMarginPercent);
    return this;
  }

  public final YogaLayout setRightMargin(float rightMargin) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMargin(this.node, Yoga.YGEdgeRight, rightMargin);
    return this;
  }

  public final YogaLayout setRightMarginPercent(float rightMarginPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMarginPercent(this.node, Yoga.YGEdgeRight, rightMarginPercent);
    return this;
  }

  public final YogaLayout setTopMargin(float topMargin) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMargin(this.node, Yoga.YGEdgeTop, topMargin);
    return this;
  }

  public final YogaLayout setTopMarginPercent(float topMarginPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMarginPercent(this.node, Yoga.YGEdgeTop, topMarginPercent);
    return this;
  }

  public final YogaLayout setBottomMargin(float bottomMargin) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMargin(this.node, Yoga.YGEdgeBottom, bottomMargin);
    return this;
  }

  public final YogaLayout setBottomMarginPercent(float bottomMarginPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMarginPercent(this.node, Yoga.YGEdgeBottom, bottomMarginPercent);
    return this;
  }

  public final YogaLayout setMargin(float margin) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMargin(this.node, Yoga.YGEdgeAll, margin);
    return this;
  }

  public final YogaLayout setMarginPercent(float marginPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMarginPercent(this.node, Yoga.YGEdgeAll, marginPercent);
    return this;
  }

  public final YogaLayout setLeftMarginAuto() {
    this.checkClosed();
    Yoga.YGNodeStyleSetMarginAuto(this.node, Yoga.YGEdgeLeft);
    return this;
  }

  public final YogaLayout setRightMarginAuto() {
    this.checkClosed();
    Yoga.YGNodeStyleSetMarginAuto(this.node, Yoga.YGEdgeRight);
    return this;
  }

  public final YogaLayout setTopMarginAuto() {
    this.checkClosed();
    Yoga.YGNodeStyleSetMarginAuto(this.node, Yoga.YGEdgeTop);
    return this;
  }

  public final YogaLayout setBottomMarginAuto() {
    this.checkClosed();
    Yoga.YGNodeStyleSetMarginAuto(this.node, Yoga.YGEdgeBottom);
    return this;
  }

  public final YogaLayout setMarginAuto() {
    this.checkClosed();
    Yoga.YGNodeStyleSetMarginAuto(this.node, Yoga.YGEdgeAll);
    return this;
  }

  public final YogaLayout setPositionType(PositionType positionType) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPositionType(this.node, positionType == PositionType.ABSOLUTE
        ? Yoga.YGPositionTypeAbsolute
        : Yoga.YGPositionTypeRelative);
    return this;
  }

  public final YogaLayout setFlexGrow(float flexGrow) {
    this.checkClosed();
    Yoga.YGNodeStyleSetFlexGrow(this.node, flexGrow);
    return this;
  }

  public final YogaLayout setFlexShrink(float flexShrink) {
    this.checkClosed();
    Yoga.YGNodeStyleSetFlexShrink(this.node, flexShrink);
    return this;
  }

  public final YogaLayout setFlexBasis(float flexBasis) {
    this.checkClosed();
    Yoga.YGNodeStyleSetFlexBasis(this.node, flexBasis);
    return this;
  }

  public final YogaLayout setFlex(float flex) {
    this.checkClosed();
    Yoga.YGNodeStyleSetFlex(this.node, flex);
    return this;
  }

  public final YogaLayout setAspectRatio(float aspectRatio) {
    this.checkClosed();
    Yoga.YGNodeStyleSetAspectRatio(this.node, aspectRatio);
    return this;
  }

  public final YogaLayout setAlignSelf(Align align) {
    this.checkClosed();
    Yoga.YGNodeStyleSetAlignSelf(this.node, align.getYogaType());
    return this;
  }

  public final YogaLayout setDisplay(Display display) {
    this.checkClosed();
    Yoga.nYGNodeStyleSetDisplay(this.node, display.getYogaType());
    return this;
  }

  public final YogaLayout setWidth(float width) {
    this.checkClosed();
    Yoga.YGNodeStyleSetWidth(this.node, width);
    return this;
  }

  public final YogaLayout setWidthPercent(float widthPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetWidthPercent(this.node, widthPercent);
    return this;
  }

  public final YogaLayout setWidthAuto() {
    this.checkClosed();
    Yoga.YGNodeStyleSetWidthAuto(this.node);
    return this;
  }

  public YogaLayout setMaxWidth(float maxWidth) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMaxWidth(this.node, maxWidth);
    return this;
  }

  public YogaLayout setMaxWidthPercent(float maxWidthPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMaxWidthPercent(this.node, maxWidthPercent);
    return this;
  }

  public final YogaLayout setHeight(float height) {
    this.checkClosed();
    Yoga.YGNodeStyleSetHeight(this.node, height);
    return this;
  }

  public final YogaLayout setHeightPercent(float heightPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetHeightPercent(this.node, heightPercent);
    return this;
  }

  public final YogaLayout setHeightAuto() {
    this.checkClosed();
    Yoga.YGNodeStyleSetHeightAuto(this.node);
    return this;
  }

  public YogaLayout setMaxHeight(float maxHeight) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMaxHeight(this.node, maxHeight);
    return this;
  }

  public YogaLayout setMaxHeightPercent(float maxHeightPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMaxHeightPercent(this.node, maxHeightPercent);
    return this;
  }

  public YogaLayout setMinWidth(float minWidth) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMinWidth(this.node, minWidth);
    return this;
  }

  public YogaLayout setMinHeight(float minHeight) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMinHeight(this.node, minHeight);
    return this;
  }

  @Override
  public Overflow getOverflow() {
    this.checkClosed();
    return overflows[Yoga.YGNodeStyleGetOverflow(this.node)];
  }

  @Override
  public float getLeft() {
    this.checkClosed();
    return Yoga.YGNodeLayoutGetLeft(this.node);
  }

  @Override
  public float getLeftPadding() {
    this.checkClosed();
    return Yoga.YGNodeLayoutGetPadding(this.node, Yoga.YGEdgeLeft);
  }

  @Override
  public float getLeftBorder() {
    this.checkClosed();
    return Yoga.YGNodeLayoutGetBorder(this.node, Yoga.YGEdgeLeft);
  }

  @Override
  public float getRight() {
    this.checkClosed();
    return Yoga.YGNodeLayoutGetRight(this.node);
  }

  @Override
  public float getRightPadding() {
    this.checkClosed();
    return Yoga.YGNodeLayoutGetPadding(this.node, Yoga.YGEdgeRight);
  }

  @Override
  public float getRightBorder() {
    this.checkClosed();
    return Yoga.YGNodeLayoutGetBorder(this.node, Yoga.YGEdgeRight);
  }

  @Override
  public float getTop() {
    this.checkClosed();
    return Yoga.YGNodeLayoutGetTop(this.node);
  }

  @Override
  public float getTopPadding() {
    this.checkClosed();
    return Yoga.YGNodeLayoutGetPadding(this.node, Yoga.YGEdgeTop);
  }

  @Override
  public float getTopBorder() {
    this.checkClosed();
    return Yoga.YGNodeLayoutGetBorder(this.node, Yoga.YGEdgeTop);
  }

  @Override
  public float getBottom() {
    this.checkClosed();
    return Yoga.YGNodeLayoutGetBottom(this.node);
  }

  @Override
  public float getBottomPadding() {
    this.checkClosed();
    return Yoga.YGNodeLayoutGetPadding(this.node, Yoga.YGEdgeBottom);
  }

  @Override
  public float getBottomBorder() {
    this.checkClosed();
    return Yoga.YGNodeLayoutGetBorder(this.node, Yoga.YGEdgeBottom);
  }

  @Override
  public float getWidth() {
    this.checkClosed();
    return Yoga.YGNodeLayoutGetWidth(this.node);
  }

  @Override
  public float getHeight() {
    this.checkClosed();
    return Yoga.YGNodeLayoutGetHeight(this.node);
  }

  @Override
  public void markDirty() {
    this.checkClosed();
    if (!this.measureFunctionPresent) {
      throw new IllegalStateException(
          "Layout must have a measure function in order to mark it as dirty.");
    }
    Yoga.YGNodeMarkDirty(this.node);
  }

  @Override
  public void close() {
    this.checkClosed();
    this.closed = true;
    Yoga.YGNodeFree(this.node);
  }

  private void checkClosed() {
    if (this.closed) {
      throw new IllegalStateException("Layout has been closed.");
    }
  }

  @Override
  public void setMeasureFunction(@Nullable MeasureFunction measureFunction) {
    this.checkClosed();
    this.measureFunctionPresent = measureFunction != null;
    Yoga.YGNodeSetMeasureFunc(this.node,
        measureFunction == null ? null : (node, width, widthMode, height, heightMode) -> {
          var size = measureFunction.measure(measureModes[widthMode], width,
              measureModes[heightMode], height);
          return YGMeasureFunc.toLong(YGSize.create()
              .width(size.x)
              .height(size.y));
        });
  }
}
