package com.craftingdead.mod.client.gui.screen;

import com.craftingdead.mod.client.gui.widget.TextBlock;
import com.craftingdead.mod.client.gui.widget.button.ModButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TranslationTextComponent;

public class ModMainMenuScreen extends ModScreen {

  private TextBlock test;

  public ModMainMenuScreen() {
    super(new TranslationTextComponent("narrator.screen.title"));
  }

  @Override
  protected void init() {
    this
        .addButton(new ModButton(this.width / 2 - 150 / 2, this.height / 2 - 50 / 2, 150, 50,
            I18n.format("menu.play"), (btn) -> {
            }));
    this.test = new TextBlock(0, 0, 250, 150);
  }

  @Override
  public void tick() {

  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    super.render(mouseX, mouseY, partialTicks);
    this.test.render(mouseX, mouseY, partialTicks);
  }
}
