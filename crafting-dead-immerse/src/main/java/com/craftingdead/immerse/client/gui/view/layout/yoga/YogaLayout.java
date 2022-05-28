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

import org.jetbrains.annotations.Nullable;
import org.lwjgl.util.yoga.YGMeasureFunc;
import org.lwjgl.util.yoga.YGSize;
import org.lwjgl.util.yoga.Yoga;
import com.craftingdead.immerse.client.gui.view.Overflow;
import com.craftingdead.immerse.client.gui.view.ViewStyle;
import com.craftingdead.immerse.client.gui.view.layout.Align;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.craftingdead.immerse.client.gui.view.layout.MeasureMode;
import com.craftingdead.immerse.client.gui.view.layout.PositionType;

public final class YogaLayout implements Layout {

  /**
   * Maps a {@link Yoga} YGMeasureMode to a {@link MeasureMode}.
   */
  private static final MeasureMode[] measureModes =
      {MeasureMode.UNDEFINED, MeasureMode.EXACTLY, MeasureMode.AT_MOST};

  /**
   * Maps a {@link Yoga} YGMeasureMode to a {@link MeasureMode}.
   */
  private static final Overflow[] overflows =
      {Overflow.VISIBLE, Overflow.HIDDEN, Overflow.SCROLL};

  final long node;

  private boolean measureFunctionPresent;
  private boolean closed;

  public YogaLayout() {
    this.node = Yoga.YGNodeNew();
  }

  @Override
  public void setAll(ViewStyle style) {
    this.setTopBorderWidth(style.borderTopWidth.get());
    this.setRightBorderWidth(style.borderRightWidth.get());
    this.setBottomBorderWidth(style.borderBottomWidth.get());
    this.setLeftBorderWidth(style.borderLeftWidth.get());

    style.top.get().dispatch(this::setTop, this::setTopPercent);
    style.right.get().dispatch(this::setRight, this::setRightPercent);
    style.bottom.get().dispatch(this::setBottom, this::setBottomPercent);
    style.left.get().dispatch(this::setLeft, this::setLeftPercent);

    style.paddingTop.get().dispatch(this::setTopPadding, this::setTopPaddingPercent);
    style.paddingRight.get().dispatch(this::setRightPadding, this::setRightPaddingPercent);
    style.paddingBottom.get().dispatch(this::setBottomPadding, this::setBottomPaddingPercent);
    style.paddingLeft.get().dispatch(this::setLeftPadding, this::setLeftPaddingPercent);

    style.marginTop.get().dispatch(
        this::setTopMargin,
        this::setTopMarginPercent,
        this::setTopMarginAuto);
    style.marginRight.get().dispatch(
        this::setRightMargin,
        this::setRightMarginPercent,
        this::setRightMarginAuto);
    style.marginBottom.get().dispatch(
        this::setBottomMargin,
        this::setBottomMarginPercent,
        this::setBottomMarginAuto);
    style.marginLeft.get().dispatch(
        this::setLeftMargin,
        this::setLeftMarginPercent,
        this::setLeftMarginAuto);

    this.setPositionType(style.position.get());

    this.setFlexGrow(style.flexGrow.get());
    this.setFlexShrink(style.flexShrink.get());
    style.flexBasis.get().dispatch(this::setFlexBasis, this::setFlexBasisPercent,
        this::setFlexBasisAuto);
    this.setFlex(style.flex.get());

    this.setAspectRatio(style.aspectRatio.get());
    this.setAlignSelf(style.alignSelf.get());

    style.width.get().dispatch(
        this::setWidth,
        this::setWidthPercent,
        this::setWidthAuto);
    style.height.get().dispatch(
        this::setHeight,
        this::setHeightPercent,
        this::setHeightAuto);
    style.minWidth.get().dispatch(this::setMinWidth, this::setMinWidthPercent);
    style.minHeight.get().dispatch(this::setMinHeight, this::setMinHeightPercent);

    this.setOverflow(style.overflow.get());
  }

  @Override
  public void setOverflow(Overflow overflow) {
    this.checkClosed();
    for (int i = 0; i < overflows.length; i++) {
      if (overflow == overflows[i]) {
        Yoga.YGNodeStyleSetOverflow(this.node, i);
        return;
      }
    }
    throw new IllegalStateException("Unknown value: " + overflow);
  }

  @Override
  public void setBorderWidth(float width) {
    this.checkClosed();
    Yoga.YGNodeStyleSetBorder(this.node, Yoga.YGEdgeAll, width);
  }

  @Override
  public void setLeftBorderWidth(float width) {
    this.checkClosed();
    Yoga.YGNodeStyleSetBorder(this.node, Yoga.YGEdgeLeft, width);
  }

  @Override
  public void setRightBorderWidth(float width) {
    this.checkClosed();
    Yoga.YGNodeStyleSetBorder(this.node, Yoga.YGEdgeRight, width);
  }

  @Override
  public void setTopBorderWidth(float width) {
    this.checkClosed();
    Yoga.YGNodeStyleSetBorder(this.node, Yoga.YGEdgeTop, width);
  }

  @Override
  public void setBottomBorderWidth(float width) {
    this.checkClosed();
    Yoga.YGNodeStyleSetBorder(this.node, Yoga.YGEdgeBottom, width);
  }

  @Override
  public void setLeft(float left) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPosition(this.node, Yoga.YGEdgeLeft, left);
  }

  @Override
  public void setLeftPercent(float leftPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPositionPercent(this.node, Yoga.YGEdgeLeft, leftPercent);
  }

  @Override
  public void setRight(float right) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPosition(this.node, Yoga.YGEdgeRight, right);
  }

  @Override
  public void setRightPercent(float rightPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPositionPercent(this.node, Yoga.YGEdgeRight, rightPercent);
  }

  @Override
  public void setTop(float top) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPosition(this.node, Yoga.YGEdgeTop, top);
  }

  @Override
  public void setTopPercent(float topPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPositionPercent(this.node, Yoga.YGEdgeTop, topPercent);
  }

  @Override
  public void setBottom(float bottom) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPosition(this.node, Yoga.YGEdgeBottom, bottom);
  }

  @Override
  public void setBottomPercent(float bottomPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPositionPercent(this.node, Yoga.YGEdgeBottom, bottomPercent);
  }

  @Override
  public void setLeftPadding(float leftPadding) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPadding(this.node, Yoga.YGEdgeLeft, leftPadding);
  }

  @Override
  public void setLeftPaddingPercent(float leftPaddingPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPaddingPercent(this.node, Yoga.YGEdgeLeft, leftPaddingPercent);
  }

  @Override
  public void setRightPadding(float rightPadding) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPadding(this.node, Yoga.YGEdgeRight, rightPadding);
  }

  @Override
  public void setRightPaddingPercent(float rightPaddingPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPaddingPercent(this.node, Yoga.YGEdgeRight, rightPaddingPercent);
  }

  @Override
  public void setTopPadding(float topPadding) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPadding(this.node, Yoga.YGEdgeTop, topPadding);
  }

  @Override
  public void setTopPaddingPercent(float topPaddingPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPaddingPercent(this.node, Yoga.YGEdgeTop, topPaddingPercent);
  }

  @Override
  public void setBottomPadding(float bottomPadding) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPadding(this.node, Yoga.YGEdgeBottom, bottomPadding);
  }

  @Override
  public void setBottomPaddingPercent(float bottomPaddingPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPaddingPercent(this.node, Yoga.YGEdgeBottom, bottomPaddingPercent);
  }

  @Override
  public void setPadding(float padding) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPadding(this.node, Yoga.YGEdgeAll, padding);
  }

  @Override
  public void setPaddingPercent(float paddingPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPaddingPercent(this.node, Yoga.YGEdgeAll, paddingPercent);
  }

  @Override
  public void setLeftMargin(float leftMargin) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMargin(this.node, Yoga.YGEdgeLeft, leftMargin);
  }

  @Override
  public void setLeftMarginPercent(float leftMarginPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMarginPercent(this.node, Yoga.YGEdgeLeft, leftMarginPercent);
  }

  @Override
  public void setRightMargin(float rightMargin) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMargin(this.node, Yoga.YGEdgeRight, rightMargin);
  }

  @Override
  public void setRightMarginPercent(float rightMarginPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMarginPercent(this.node, Yoga.YGEdgeRight, rightMarginPercent);
  }

  @Override
  public void setTopMargin(float topMargin) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMargin(this.node, Yoga.YGEdgeTop, topMargin);
  }

  @Override
  public void setTopMarginPercent(float topMarginPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMarginPercent(this.node, Yoga.YGEdgeTop, topMarginPercent);
  }

  @Override
  public void setBottomMargin(float bottomMargin) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMargin(this.node, Yoga.YGEdgeBottom, bottomMargin);
  }

  @Override
  public void setBottomMarginPercent(float bottomMarginPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMarginPercent(this.node, Yoga.YGEdgeBottom, bottomMarginPercent);
  }

  @Override
  public void setMargin(float margin) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMargin(this.node, Yoga.YGEdgeAll, margin);
  }

  @Override
  public void setMarginPercent(float marginPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMarginPercent(this.node, Yoga.YGEdgeAll, marginPercent);
  }

  @Override
  public void setLeftMarginAuto() {
    this.checkClosed();
    Yoga.YGNodeStyleSetMarginAuto(this.node, Yoga.YGEdgeLeft);
  }

  @Override
  public void setRightMarginAuto() {
    this.checkClosed();
    Yoga.YGNodeStyleSetMarginAuto(this.node, Yoga.YGEdgeRight);
  }

  @Override
  public void setTopMarginAuto() {
    this.checkClosed();
    Yoga.YGNodeStyleSetMarginAuto(this.node, Yoga.YGEdgeTop);
  }

  @Override
  public void setBottomMarginAuto() {
    this.checkClosed();
    Yoga.YGNodeStyleSetMarginAuto(this.node, Yoga.YGEdgeBottom);
  }

  @Override
  public void setMarginAuto() {
    this.checkClosed();
    Yoga.YGNodeStyleSetMarginAuto(this.node, Yoga.YGEdgeAll);
  }

  @Override
  public void setPositionType(PositionType positionType) {
    this.checkClosed();
    Yoga.YGNodeStyleSetPositionType(this.node, YogaUtil.getPositionType(positionType));
  }

  @Override
  public void setFlexGrow(float flexGrow) {
    this.checkClosed();
    Yoga.YGNodeStyleSetFlexGrow(this.node, flexGrow);
  }

  @Override
  public void setFlexShrink(float flexShrink) {
    this.checkClosed();
    Yoga.YGNodeStyleSetFlexShrink(this.node, flexShrink);
  }

  @Override
  public void setFlexBasis(float flexBasis) {
    this.checkClosed();
    Yoga.YGNodeStyleSetFlexBasis(this.node, flexBasis);
  }

  @Override
  public void setFlexBasisPercent(float flexBasisPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetFlexBasisPercent(this.node, flexBasisPercent);
  }

  @Override
  public void setFlexBasisAuto() {
    this.checkClosed();
    Yoga.YGNodeStyleSetFlexBasisAuto(this.node);
  }

  @Override
  public void setFlex(float flex) {
    this.checkClosed();
    Yoga.YGNodeStyleSetFlex(this.node, flex);
  }

  @Override
  public void setAspectRatio(float aspectRatio) {
    this.checkClosed();
    Yoga.YGNodeStyleSetAspectRatio(this.node, aspectRatio);
  }

  @Override
  public void setAlignSelf(Align align) {
    this.checkClosed();
    Yoga.YGNodeStyleSetAlignSelf(this.node, YogaUtil.getAlign(align));
  }

  @Override
  public void setWidth(float width) {
    this.checkClosed();
    Yoga.YGNodeStyleSetWidth(this.node, width);
  }

  @Override
  public void setWidthPercent(float widthPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetWidthPercent(this.node, widthPercent);
  }

  @Override
  public void setWidthAuto() {
    this.checkClosed();
    Yoga.YGNodeStyleSetWidthAuto(this.node);
  }

  public void setMaxWidth(float maxWidth) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMaxWidth(this.node, maxWidth);
  }

  public void setMaxWidthPercent(float maxWidthPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMaxWidthPercent(this.node, maxWidthPercent);
  }

  @Override
  public void setHeight(float height) {
    this.checkClosed();
    Yoga.YGNodeStyleSetHeight(this.node, height);
  }

  @Override
  public void setHeightPercent(float heightPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetHeightPercent(this.node, heightPercent);
  }

  @Override
  public void setHeightAuto() {
    this.checkClosed();
    Yoga.YGNodeStyleSetHeightAuto(this.node);
  }

  public void setMaxHeight(float maxHeight) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMaxHeight(this.node, maxHeight);
  }

  public void setMaxHeightPercent(float maxHeightPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMaxHeightPercent(this.node, maxHeightPercent);
  }

  public void setMinWidth(float minWidth) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMinWidth(this.node, minWidth);
  }

  public void setMinWidthPercent(float minWidthPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMinWidthPercent(this.node, minWidthPercent);
  }

  public void setMinHeight(float minHeight) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMinHeight(this.node, minHeight);
  }

  public void setMinHeightPercent(float minHeightPercent) {
    this.checkClosed();
    Yoga.YGNodeStyleSetMinHeightPercent(this.node, minHeightPercent);
  }

  @Override
  public Overflow getOverflow() {
    this.checkClosed();
    return overflows[Yoga.YGNodeStyleGetOverflow(this.node)];
  }

  @Override
  public float getX() {
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
  public float getY() {
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
