/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.client.gui.view;

import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;

public class AvatarView extends View {

  private ResourceLocation textureLocation;

  public AvatarView(Properties<?> properties, GameProfile gameProfile) {
    super(properties);
    this.textureLocation = DefaultPlayerSkin.getDefaultSkin(gameProfile.getId());
    this.minecraft.getSkinManager().registerSkins(gameProfile,
        (type, textureLocation, texture) -> {
          if (type == MinecraftProfileTexture.Type.SKIN) {
            this.textureLocation = textureLocation;
          }
        }, true);
  }

  @Override
  protected void renderContent(PoseStack matrixStack, int mouseX, int mouseY, float partialTick) {
    super.renderContent(matrixStack, mouseX, mouseY, partialTick);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.getAlpha());
    RenderUtil.blitAvatar(this.textureLocation, matrixStack,
        this.getScaledContentX(), this.getScaledContentY(),
        this.getScaledContentWidth(), this.getScaledContentHeight());
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
  }
}
