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

package com.craftingdead.immerse.client.gui.view;

import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;

public class AvatarView<L extends Layout> extends View<AvatarView<L>, L> {

  private ResourceLocation textureLocation;

  public AvatarView(L layout, GameProfile gameProfile) {
    super(layout);
    this.textureLocation = DefaultPlayerSkin.getDefaultSkin(gameProfile.getId());
    this.minecraft.getSkinManager().registerSkins(gameProfile,
        (type, textureLocation, texture) -> {
          if (type == MinecraftProfileTexture.Type.SKIN) {
            this.textureLocation = textureLocation;
          }
        }, true);
  }

  @Override
  protected void renderContent(PoseStack matrixStack, int mouseX, int mouseY,
      float partialTicks) {
    super.renderContent(matrixStack, mouseX, mouseY, partialTicks);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.getAlpha());
    RenderUtil.blitAvatar(this.textureLocation, matrixStack,
        this.getScaledContentX(), this.getScaledContentY(),
        this.getScaledContentWidth(), this.getScaledContentHeight());
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
  }
}
