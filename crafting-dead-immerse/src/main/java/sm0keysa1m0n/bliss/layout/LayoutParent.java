package sm0keysa1m0n.bliss.layout;

import sm0keysa1m0n.bliss.view.ViewStyle;

public interface LayoutParent {

  LayoutParent NILL = new LayoutParent() {

    @Override
    public Layout addChild(int index) {
      return Layout.NILL;
    }

    @Override
    public void removeChild(Layout layout) {}

    @Override
    public void layout(float width, float height) {}

    @Override
    public float getContentWidth() {
      return 0.0F;
    }

    @Override
    public float getContentHeight() {
      return 0.0F;
    }

    @Override
    public void close() {}
  };

  default void setAll(ViewStyle style) {}

  default void setFlexDirection(FlexDirection flexDirection) {}

  default void setFlexWrap(Wrap flexDirection) {}

  default void setAlignItems(Align align) {}

  default void setAlignContent(Align align) {}

  default void setJustifyContent(Justify justify) {}

  Layout addChild(int index);

  void removeChild(Layout layout);

  /**
   * Calculate the layout.
   * 
   * @param width - width of container or {@link Float#NaN} to let layout decide.
   * @param height - height of container or {@link Float#NaN} to let layout decide.
   */
  void layout(float width, float height);

  float getContentWidth();

  float getContentHeight();

  void close();
}
