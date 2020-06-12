package com.craftingdead.immerse.client.gui.component;

import com.craftingdead.immerse.client.util.RenderUtil;

public class RectangleComponent extends Component<RectangleComponent> {

  private final Colour colour;

  public RectangleComponent(Colour colour) {
    this.colour = colour;
  }

  public Colour getColourProperty() {
    return this.colour;
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    super.render(mouseX, mouseY, partialTicks);
    RenderUtil
        .fill(this.getX(), this.getY(), this.getX() + this.getWidth(),
            this.getY() + this.getHeight(), this.colour.getHexColour());
  }

  @Override
  protected void added() {}

  @Override
  protected void removed() {}

  @Override
  public void tick() {}

  @Override
  protected void resized() {}
}
