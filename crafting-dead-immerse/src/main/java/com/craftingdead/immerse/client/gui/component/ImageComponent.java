package com.craftingdead.immerse.client.gui.component;

import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import io.noties.tumbleweed.TweenType;
import net.minecraft.util.ResourceLocation;

public class ImageComponent extends Component<ImageComponent> {

  public static final TweenType<ImageComponent> COLOUR =
      new SimpleTweenType<>(4, t -> t.colour.getColour4f(), (t, v) -> t.colour.setColour4f(v));

  private final ResourceLocation image;
  private final Colour colour;

  public ImageComponent(ResourceLocation image) {
    this(image, new Colour());
  }

  public ImageComponent(ResourceLocation image, Colour colour) {
    this.image = image;
    this.colour = colour;
  }

  public Colour getColour() {
    return this.colour;
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    super.render(mouseX, mouseY, partialTicks);
    RenderSystem.enableBlend();
    final float[] colour = this.colour.getColour4f();
    RenderSystem.color4f(colour[0], colour[1], colour[2], colour[3]);
    RenderUtil.bind(this.image);
    RenderUtil.blit(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.disableBlend();
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
