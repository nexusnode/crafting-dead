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

package net.rocketpowered.connector.client.gui;

import java.util.EnumMap;
import java.util.Map;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class OverlayView extends ParentView {

  private final Map<MinecraftProfileTexture.Type, ResourceLocation> textureLocations =
      new EnumMap<>(MinecraftProfileTexture.Type.class);


  public OverlayView() {
    super(new Properties<>());
    this.minecraft.getSkinManager().registerSkins(this.minecraft.getUser().getGameProfile(),
        (type, textureLocation, texture) -> this.textureLocations.put(type, textureLocation), true);

    // this.setBackgroundColour(new Colour(0.25F, 0.25F, 0.25F, 0.6F));

  }

  @Override
  protected void renderContent(PoseStack matrixStack, int mouseX, int mouseY,
      float partialTicks) {
    int opacity = Mth.ceil(this.getAlpha() * 255.0F) << 24;
    if ((opacity & 0xFC000000) != 0) {

      fill(matrixStack, 0, 0,
          (int) this.getScaledContentWidth(),
          (int) this.getScaledContentHeight(),
          0x505050 | (Mth.ceil(this.getAlpha() * 96.0F) << 24));

      this.fillGradient(matrixStack, 0, 0,
          (int) this.getScaledContentWidth(),
          (int) this.getScaledContentHeight(), 0,
          0x101010 | opacity);

      RenderSystem.setShaderTexture(0, new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/rocket.png"));

      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.getAlpha());
      RenderSystem.enableBlend();

      matrixStack.pushPose();
      {
        matrixStack.translate(this.getScaledContentWidth() / 2 - 150,
            this.getScaledContentHeight() - 40 - 20, 0.0D);

        matrixStack.scale(0.5F, 0.5F, 0.5F);
        RenderUtil.blit(matrixStack, 0, 0, 243, 80);
      }
      matrixStack.popPose();


      RenderUtil.blitAvatar(
          this.textureLocations.getOrDefault(MinecraftProfileTexture.Type.SKIN,
              DefaultPlayerSkin.getDefaultSkin(this.minecraft.getUser().getGameProfile().getId())),
          matrixStack, (int) this.getScaledContentWidth() / 2 + 48,
          (int) this.getScaledContentHeight() - 48, 16, 16);
      RenderSystem.disableBlend();


      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

      this.minecraft.font.drawShadow(matrixStack, this.minecraft.getUser().getName(),
          this.getScaledContentWidth() / 2 + 20, this.getScaledContentHeight() - 48,
          0xFFFFFF | opacity);

      super.renderContent(matrixStack, mouseX, mouseY, partialTicks);

    }

  }
}
