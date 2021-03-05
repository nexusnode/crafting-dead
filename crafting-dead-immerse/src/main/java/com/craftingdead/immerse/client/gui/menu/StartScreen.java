/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.craftingdead.immerse.client.gui.menu;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.gui.component.type.FitType;
import com.craftingdead.immerse.client.gui.menu.MenuScreen.Page;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.TranslationTextComponent;

public class StartScreen extends ModScreen {

  private float fadeStartTime = 0L;

  public StartScreen() {
    super(new TranslationTextComponent("narrator.screen.start"));
  }

  @SuppressWarnings("deprecation")
  @Override
  public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    super.renderBackground(matrixStack);

    if (this.fadeStartTime == 0L) {
      this.fadeStartTime = Util.milliTime();
    }

    float fadePct = MathHelper.clamp((Util.milliTime() - this.fadeStartTime) / 1000.0F, 0.0F, 1.0F);

    Vector2f logoSize = FitType.CONTAIN.getSize(2000, 440, this.width, this.height);
    double logoWidth = logoSize.x / 1.5F;
    double logoHeight = logoSize.y / 1.5F;

    RenderSystem.enableBlend();
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, fadePct);
    RenderUtil.bind(new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/banner_4x.png"));
    RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
    RenderUtil.blit(this.width / 2 - logoWidth / 2, this.height / 2 - logoHeight / 2, logoWidth,
        logoHeight);
    RenderSystem.disableBlend();

    String messageText = I18n.format("gui.screen.start.message");
    this.font.drawString(matrixStack, messageText,
        this.width / 2 - this.font.getStringWidth(messageText) / 2,
        this.height / 2 + this.height / 4, 0xFFFFFF | MathHelper.ceil(fadePct * 255.0F) << 24);
  }

  @Override
  public boolean keyPressed(int key, int scanCode, int modifiers) {
    if (key == GLFW.GLFW_KEY_SPACE) {
      this.minecraft.displayGuiScreen(new MenuScreen(Page.HOME));
    }
    return super.keyPressed(key, scanCode, modifiers);
  }
}
