package com.craftingdead.mod.client.gui.screen;

import com.craftingdead.mod.client.gui.widget.button.ModButton;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;

public class ModMainMenuScreen extends ModScreen {

  private static boolean firstLoad = true;

  private final boolean showFadeInAnimation;
  private long firstRenderTime;

  public ModMainMenuScreen() {
    this(firstLoad);
    firstLoad = false;
  }

  public ModMainMenuScreen(boolean showFadeInAnimation) {
    super(new TranslationTextComponent("narrator.screen.title"));
    this.showFadeInAnimation = showFadeInAnimation;
  }

  @Override
  protected void init() {
    this.addButton(new ModButton(this.width / 2 - 150 / 2, this.height / 2 - 50 / 2, 150, 50, I18n.format("menu.play"), (btn) -> {
    }));
  }

  @Override
  public void tick() {

  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    if (this.firstRenderTime == 0L && this.showFadeInAnimation) {
      this.firstRenderTime = Util.milliTime();
    }

    float fadeProgress =
        this.showFadeInAnimation ? (float) (Util.milliTime() - this.firstRenderTime) / 1000.0F
            : 1.0F;
    float alpha =
        this.showFadeInAnimation ? MathHelper.clamp(fadeProgress - 1.0F, 0.0F, 1.0F) : 1.0F;

    for (Widget widget : this.buttons) {
      widget.setAlpha(alpha);
    }

    super.render(mouseX, mouseY, partialTicks);
  }
}
