package com.craftingdead.immerse.client.gui.component;

import java.util.List;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

public class ComponentScreen extends Screen implements ISize {

  private final ContainerComponent root = new ContainerComponent(
      new Component.RegionBuilder().setSize(this).setLocation(new AbsoluteLocation(0, 0)));

  protected ComponentScreen(ITextComponent title) {
    super(title);
  }

  public ContainerComponent getRoot() {
    return this.root;
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
  public int getWidth(Component<?> component) {
    return this.width;
  }

  @Override
  public int getHeight(Component<?> component) {
    return this.height;
  }
}
