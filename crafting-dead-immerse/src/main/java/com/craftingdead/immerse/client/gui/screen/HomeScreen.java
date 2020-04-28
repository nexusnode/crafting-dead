package com.craftingdead.immerse.client.gui.screen;

import net.minecraft.util.text.TranslationTextComponent;

public class HomeScreen extends MenuScreen {

  public HomeScreen() {
    super(new TranslationTextComponent("narrator.screen.title"));
  }

  @Override
  protected void init() {
    super.init();
  }

  @Override
  public void tick() {
    super.tick();
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    super.renderBackground();
    super.render(mouseX, mouseY, partialTicks);
  }
}
