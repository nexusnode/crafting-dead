package sm0keysa1m0n.bliss.layout;

import org.jetbrains.annotations.Nullable;
import net.minecraft.world.phys.Vec2;
import sm0keysa1m0n.bliss.Overflow;
import sm0keysa1m0n.bliss.view.ViewStyle;

public interface Layout {

  Layout NILL = new Layout() {};

  default void setAll(ViewStyle style) {}

  default void setOverflow(Overflow overflow) {}

  default void setBorderWidth(float width) {}

  default void setLeftBorderWidth(float width) {}

  default void setRightBorderWidth(float width) {}

  default void setTopBorderWidth(float width) {}

  default void setBottomBorderWidth(float width) {}

  default void setLeftPadding(float leftPadding) {}

  default void setLeftPaddingPercent(float leftPaddingPercent) {}

  default void setRightPadding(float rightPadding) {}

  default void setRightPaddingPercent(float rightPaddingPercent) {}

  default void setTopPadding(float topPadding) {}

  default void setTopPaddingPercent(float topPaddingPercent) {}

  default void setBottomPadding(float bottomPadding) {}

  default void setBottomPaddingPercent(float bottomPaddingPercent) {}

  default void setPadding(float padding) {}

  default void setPaddingPercent(float paddingPercent) {}

  default void setLeftMargin(float leftMargin) {}

  default void setLeftMarginPercent(float leftMarginPercent) {}

  default void setRightMargin(float rightMargin) {}

  default void setRightMarginPercent(float rightMarginPercent) {}

  default void setTopMargin(float topMargin) {}

  default void setTopMarginPercent(float topMarginPercent) {}

  default void setBottomMargin(float bottomMargin) {}

  default void setBottomMarginPercent(float bottomMarginPercent) {}

  default void setMargin(float margin) {}

  default void setMarginPercent(float marginPercent) {}

  default void setLeftMarginAuto() {}

  default void setRightMarginAuto() {}

  default void setTopMarginAuto() {}

  default void setBottomMarginAuto() {}

  default void setMarginAuto() {}

  default void setPositionType(PositionType positionType) {}

  default void setFlexGrow(float flexGrow) {}

  default void setFlexShrink(float flexShrink) {}

  default void setFlexBasis(float flexBasis) {}

  default void setFlexBasisPercent(float flexBasisPercent) {}

  default void setFlexBasisAuto() {}

  default void setFlex(float flex) {}

  default void setAspectRatio(float aspectRatio) {}

  default void setAlignSelf(Align align) {}

  default void setWidth(float width) {}

  default void setWidthPercent(float widthPercent) {}

  default void setWidthAuto() {}

  default void setMaxWidth(float maxWidth) {}

  default void setMaxWidthPercent(float maxWidthPercent) {}

  default void setHeight(float height) {}

  default void setHeightPercent(float heightPercent) {}

  default void setHeightAuto() {}

  default void setMaxHeight(float maxHeight) {}

  default void setMaxHeightPercent(float maxHeightPercent) {}

  default void setMinWidth(float minWidth) {}

  default void setMinWidthPercent(float minWidthPercent) {}

  default void setMinHeight(float minHeight) {}

  default void setMinHeightPercent(float minHeightPercent) {}

  default Overflow getOverflow() {
    return Overflow.VISIBLE;
  }

  default float getX() {
    throw new UnsupportedOperationException();
  }

  default float getLeftPadding() {
    return 0;
  }

  default float getLeftBorder() {
    return 0;
  }

  default float getRightPadding() {
    return 0;
  }

  default float getRightBorder() {
    return 0;
  }

  default float getY() {
    throw new UnsupportedOperationException();
  }

  default float getTopPadding() {
    return 0;
  }

  default float getTopBorder() {
    return 0;
  }

  default float getBottomPadding() {
    return 0;
  }

  default float getBottomBorder() {
    return 0;
  }

  default float getWidth() {
    throw new UnsupportedOperationException();
  }

  default float getHeight() {
    throw new UnsupportedOperationException();
  }

  default void setMeasureFunction(@Nullable MeasureFunction measureFunction) {}

  default void markDirty() {}

  default void close() {}

  interface MeasureFunction {

    Vec2 measure(MeasureMode widthMode, float width, MeasureMode heightMode, float height);
  }
}
