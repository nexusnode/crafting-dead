package sm0keysa1m0n.bliss;

import net.minecraft.world.phys.Vec2;

public enum ObjectFit {

  FILL, COVER {
    @Override
    public Vec2 getSize(float contentWidth, float contentHeight, float containerWidth,
        float containerHeight) {
      float widthScale = containerWidth / contentWidth;
      float heightScale = containerHeight / contentHeight;
      float finalScale = contentHeight * widthScale < containerHeight ? heightScale : widthScale;
      return new Vec2(contentWidth * finalScale, contentHeight * finalScale);
    }
  },
  CONTAIN {
    @Override
    public Vec2 getSize(float contentWidth, float contentHeight, float containerWidth,
        float containerHeight) {
      float widthScale = containerWidth / contentWidth;
      float heightScale = containerHeight / contentHeight;
      float finalScale = contentHeight * widthScale > containerHeight ? heightScale : widthScale;
      if (finalScale * contentWidth > containerWidth) {
        contentWidth = finalScale * contentWidth;
        contentHeight = finalScale * contentHeight;
        finalScale = containerWidth / (contentWidth);
      }
      return new Vec2(contentWidth * finalScale, contentHeight * finalScale);
    }
  },
  NONE {
    @Override
    public Vec2 getSize(float contentWidth, float contentHeight, float containerWidth,
        float containerHeight) {
      return new Vec2(contentWidth, contentHeight);
    }
  };

  public Vec2 getSize(float contentWidth, float contentHeight, float containerWidth,
      float containerHeight) {
    return new Vec2(containerWidth, containerHeight);
  }
}
