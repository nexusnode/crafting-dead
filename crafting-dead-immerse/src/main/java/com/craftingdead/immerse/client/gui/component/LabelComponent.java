package com.craftingdead.immerse.client.gui.component;

import com.craftingdead.immerse.client.gui.property.ColourProperty;
import com.craftingdead.immerse.client.gui.property.IProperty;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.TransformationMatrix;
import net.minecraft.util.text.ITextComponent;

public class LabelComponent extends Component<LabelComponent> {

  private final static ISize FONT_SIZE = new ISize() {

    @Override
    public int getWidth(Component<?> component) {
      LabelComponent label = (LabelComponent) component;
      return label.fontRenderer.get().getStringWidth(label.text.getFormattedText());
    }

    @Override
    public int getHeight(Component<?> component) {
      FontRenderer font = ((LabelComponent) component).fontRenderer.get();
      return font.FONT_HEIGHT;
    }

  };

  private final IProperty<FontRenderer> fontRenderer;

  private final ITextComponent text;

  private final boolean shadow;

  private ColourProperty colour;

  public LabelComponent(RegionBuilder regionBuilder, IProperty<FontRenderer> fontRenderer,
      ITextComponent text, ColourProperty colour, boolean shadow, boolean fit) {
    super(fit ? regionBuilder.setSize(FONT_SIZE) : regionBuilder);
    this.fontRenderer = fontRenderer;
    this.text = text;
    this.colour = colour;
    this.shadow = shadow;
  }

  public LabelComponent(RegionBuilder regionBuilder, IProperty<FontRenderer> fontRenderer,
      ITextComponent text, ColourProperty colour, boolean shadow) {
    this(regionBuilder, fontRenderer, text, colour, shadow, true);
  }

  public ColourProperty getColourProperty() {
    return this.colour;
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    super.render(mouseX, mouseY, partialTicks);
    RenderSystem.enableAlphaTest();
    IRenderTypeBuffer.Impl renderTypeBuffer =
        IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuffer());
    final FontRenderer font = this.fontRenderer.get();
    font
        .draw(this.text.getFormattedText(), this.getX(), this.getY(), this.colour.get(),
            this.shadow, TransformationMatrix.identity().getMatrix(), renderTypeBuffer, false, 0,
            0xF000F0);
    renderTypeBuffer.draw();
//    fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(),
//        0xFFFFFFFF);
    RenderSystem.disableAlphaTest();

  }

  @Override
  public void tick() {}
}
