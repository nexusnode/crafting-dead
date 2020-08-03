package com.craftingdead.immerse.client.gui.component;

import java.util.Optional;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.TransformationMatrix;
import net.minecraft.util.text.ITextComponent;

public class LabelComponent extends Component<LabelComponent> {

  private final FontRenderer fontRenderer;

  private final ITextComponent text;

  private final boolean shadow;

  private Colour colour;

  public LabelComponent(FontRenderer fontRenderer, ITextComponent text, Colour colour,
      boolean shadow) {
    this.fontRenderer = fontRenderer;
    this.text = text;
    this.colour = colour;
    this.shadow = shadow;
  }

  @Override
  public Optional<Double> getBestWidth() {
    return Optional.of((double) this.fontRenderer.getStringWidth(this.text.getFormattedText()));
  }

  @Override
  public Optional<Double> getBestHeight() {
    return Optional.of((double) this.fontRenderer.FONT_HEIGHT);
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    super.render(mouseX, mouseY, partialTicks);
    RenderSystem.pushMatrix();
    {
      RenderSystem.translated(this.getX(), this.getY(), 0.0D);
      RenderSystem.scalef(this.getXScale(), this.getYScale(), 1.0F);
      RenderSystem.enableAlphaTest();
      IRenderTypeBuffer.Impl renderTypeBuffer =
          IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
      this.fontRenderer
          .renderString(this.text.getFormattedText(), 0, 0, this.colour.getHexColour(), this.shadow,
              TransformationMatrix.identity().getMatrix(), renderTypeBuffer, false, 0, 0xF000F0);
      renderTypeBuffer.finish();
      RenderSystem.disableAlphaTest();
    }
    RenderSystem.popMatrix();
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
