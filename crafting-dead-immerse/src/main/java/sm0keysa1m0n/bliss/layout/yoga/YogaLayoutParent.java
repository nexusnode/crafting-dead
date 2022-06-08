package sm0keysa1m0n.bliss.layout.yoga;

import org.lwjgl.util.yoga.Yoga;
import sm0keysa1m0n.bliss.layout.Align;
import sm0keysa1m0n.bliss.layout.FlexDirection;
import sm0keysa1m0n.bliss.layout.Justify;
import sm0keysa1m0n.bliss.layout.Layout;
import sm0keysa1m0n.bliss.layout.LayoutParent;
import sm0keysa1m0n.bliss.layout.Wrap;
import sm0keysa1m0n.bliss.view.ViewStyle;

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
