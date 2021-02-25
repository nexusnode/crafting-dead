/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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

package com.craftingdead.immerse.client.gui.screen;

import org.lwjgl.opengl.GL11;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.gui.component.ComponentScreen;
import com.craftingdead.immerse.client.gui.component.FitType;
import com.craftingdead.immerse.client.gui.transition.ITransition;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.ITextComponent;

public abstract class ModScreen extends ComponentScreen {

  private static final ResourceLocation BACKGROUND_TEXTURE =
      new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/background.png");

  private static final ResourceLocation SMOKE_TEXTURE =
      new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/smoke.png");

  private static long backgroundStartTime = Util.milliTime();

  private static long fogStartTime = Util.milliTime();

  protected ModScreen(ITextComponent title) {
    super(title);
  }

  @Override
  public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(matrixStack);
    super.render(matrixStack, mouseX, mouseY, partialTicks);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void renderBackground(MatrixStack matrixStack) {
    if (this.minecraft.world != null) {
      this.fillGradient(matrixStack, 0, 0, this.width, this.height, -1072689136, -804253680);
      return;
    }

    double pct =
        MathHelper.clamp(((Util.milliTime() - backgroundStartTime) / 1000.0F) / 10, 0.0F, 1.0F);
    if (pct == 1.0D) {
      backgroundStartTime = Util.milliTime();
    }

    RenderSystem.pushMatrix();
    {
      RenderSystem.translated(Math.sin(Math.toRadians(360 * pct)) * 2.5D,
          Math.cos(Math.toRadians(360 * pct)) * 2.5D, 0);
      double scale = 1.25D + Math.cos(Math.toRadians(360 * pct)) * 1.5D / 100.0D;
      RenderSystem.scaled(scale, scale, scale);

      Vector2f backgroundSize = FitType.COVER.getSize(2048, 1152, this.width, this.height);
      double backgroundWidth = backgroundSize.x;
      double backgroundHeight = backgroundSize.y;

      RenderUtil.bind(BACKGROUND_TEXTURE);
      // Enable bilinear filtering
      RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
      RenderUtil.blit((this.width / 2 - (backgroundWidth * scale) / 2),
          this.height / 2 - (backgroundHeight * scale) / 2, backgroundWidth, backgroundHeight);
    }
    RenderSystem.popMatrix();

    this.renderFog();
  }

  @SuppressWarnings("deprecation")
  private void renderFog() {
    Vector2f fogSize = FitType.COVER.getSize(1920, 1080, this.width, this.height);
    double fogWidth = fogSize.x;
    double fogHeight = fogSize.y;

    final double pct =
        MathHelper.clamp((Util.milliTime() - fogStartTime) / (1000.0D * 100.0D * 2.0D), 0.0D, 1.0D);
    if (pct == 1.0D) {
      fogStartTime = Util.milliTime();
    }

    RenderSystem.pushMatrix();
    {
      RenderSystem.scalef(4F, 4F, 4F);
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.color4f(1.0F, 1.0F, 1.0F, 0.3F);

      RenderUtil.bind(SMOKE_TEXTURE);

      final double smokeX = pct * this.width;

      RenderUtil.blit(smokeX, 0, fogWidth, fogHeight);
      RenderUtil.blit(smokeX - fogWidth, 0, fogWidth, fogHeight);

      RenderSystem.disableBlend();
    }
    RenderSystem.popMatrix();
  }

  public ITransition getTransition() {
    return null;
  }
}
