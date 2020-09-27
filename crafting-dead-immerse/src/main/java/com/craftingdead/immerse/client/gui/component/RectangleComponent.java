package com.craftingdead.immerse.client.gui.component;

public class RectangleComponent extends Component<RectangleComponent> {

  private final Colour colour;

  public RectangleComponent(Colour colour) {
    this.colour = colour;
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    super.render(mouseX, mouseY, partialTicks);
    final int[] colour4i = this.colour.getColour4i();
    this.canvas.setRGBA(colour4i[0], colour4i[0], colour4i[0], colour4i[0]);
    this.canvas.drawRect(this.getXFloat(), this.getYFloat(),
        this.getXFloat() + this.getWidthFloat(),
        this.getYFloat() + this.getHeightFloat());
  }
}
