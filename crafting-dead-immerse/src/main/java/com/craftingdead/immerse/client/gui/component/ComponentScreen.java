package com.craftingdead.immerse.client.gui.component;

import java.util.List;
import com.craftingdead.immerse.client.util.IFramebufferResizeListener;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

public class ComponentScreen extends Screen implements IFramebufferResizeListener {

  private final ContainerComponent root = new ContainerComponent();

  protected ComponentScreen(ITextComponent title) {
    super(title);
  }

  public ContainerComponent getRoot() {
    return this.root;
  }

  @Override
  protected void init() {
    double mouseX = this.minecraft.mouseHelper.getMouseX() * (double) this.width
        / (double) this.minecraft.getWindow().getWidth();
    double mouseY = this.minecraft.mouseHelper.getMouseY() * (double) this.height
        / (double) this.minecraft.getWindow().getHeight();
    this.root.mouseMoved(mouseX, mouseY);
  }

  @Override
  public void mouseMoved(double mouseX, double mouseY) {
    this.root.mouseMoved(mouseX, mouseY);
  }

  @Override
  public void tick() {
    this.root.tick();
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    this.root.render(mouseX, mouseY, partialTicks);
  }

  @Override
  public List<Component<?>> children() {
    return Lists.newArrayList(this.root);
  }

  @Override
  public void framebufferResized() {
    this.root.resized();
  }
}
