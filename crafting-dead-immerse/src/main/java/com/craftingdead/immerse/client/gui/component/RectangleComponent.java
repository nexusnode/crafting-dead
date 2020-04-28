package com.craftingdead.immerse.client.gui.component;

import com.craftingdead.immerse.client.gui.property.ColourProperty;

public class RectangleComponent extends Component<RectangleComponent> {

  private final ColourProperty colour;

  public RectangleComponent(RegionBuilder regionBuilder, ColourProperty colour) {
    super(regionBuilder);
    this.colour = colour;
  }

  public ColourProperty getColourProperty() {
    return this.colour;
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    super.render(mouseX, mouseY, partialTicks);
    fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(),
        this.colour.get());
  }

  @Override
  public void tick() {}
}
