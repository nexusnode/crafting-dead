package com.craftingdead.immerse.client.gui.component;

import com.craftingdead.immerse.client.gui.property.ColourProperty;
import com.craftingdead.immerse.client.gui.property.Property;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.ResourceLocation;

public class ImageComponent extends Component<ImageComponent> {

  private final Property<ResourceLocation> image;
  private final ColourProperty colour;

  public ImageComponent(RegionBuilder regionBuilder, Property<ResourceLocation> image,
      ColourProperty colour) {
    super(regionBuilder);
    this.image = image;
    this.colour = colour;
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    RenderSystem.enableBlend();
    final float[] colour = this.colour.getAnimatedValues();
    RenderSystem.color4f(colour[0], colour[1], colour[2], colour[4]);
    RenderUtil.bind(this.image.get());
    RenderUtil.blit(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.disableBlend();
  }

  @Override
  public void tick() {}
}
