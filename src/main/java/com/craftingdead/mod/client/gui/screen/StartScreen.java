package com.craftingdead.mod.client.gui.screen;

import java.util.Random;
import javax.vecmath.Vector2d;
import org.lwjgl.glfw.GLFW;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.client.gui.transition.ITransition;
import com.craftingdead.mod.client.util.RenderUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;

public class StartScreen extends ModScreen {

  // 5 seconds
  private static final float FADE_LENGTH = 20 * 5F;
  private float fadeTimer;

  private float lastLogoAlpha = 1.0F;

  public StartScreen() {
    // TODO add narrator.screen.start translation
    super(new TranslationTextComponent("narrator.screen.start"));
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    super.render(mouseX, mouseY, partialTicks);
    this.renderBackground();

    if (this.fadeTimer < FADE_LENGTH) {
      this.fadeTimer++;
    }

    float fadePct = this.fadeTimer / FADE_LENGTH;

    if (new Random().nextInt(15) == 5 || this.lastLogoAlpha < 0.5F) {
      this.lastLogoAlpha = new Random().nextFloat() * 1.5F + 0.5F;
    }

    Vector2d logoSize = RenderUtil.scaleToFit(1920, 1080);

    GlStateManager.enableBlend();
    GlStateManager.color4f(1.0F, 1.0F, 1.0F, fadePct);
    RenderUtil.bind(new ResourceLocation(CraftingDead.ID, "textures/gui/craftingdead.png"));
    RenderUtil
        .drawTexturedRectangle(this.width / 2 - logoSize.getX() / 2,
            this.height / 2 - logoSize.getY() / 2, logoSize.getX(), logoSize.getY());

    this.font
        .drawString(I18n.format("menu.start"),
            this.width / 2 - this.font.getStringWidth(I18n.format("menu.start")) / 2,
            this.height / 2 + this.height / 4, 0xFFFFFF | MathHelper.ceil(fadePct * 255.0F) << 24);

    GlStateManager.disableBlend();
  }

  @Override
  public boolean keyPressed(int key, int scanCode, int modifiers) {
    if (key == GLFW.GLFW_KEY_SPACE) {
      this.minecraft.displayGuiScreen(new ModMainMenuScreen());
    }
    return super.keyPressed(key, scanCode, modifiers);
  }

  @Override
  public ITransition getTransition() {
    return null;
  }
}
