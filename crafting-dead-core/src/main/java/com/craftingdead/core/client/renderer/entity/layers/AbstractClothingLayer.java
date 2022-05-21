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

package com.craftingdead.core.client.renderer.entity.layers;

import org.jetbrains.annotations.Nullable;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractClothingLayer<T extends LivingEntity, M extends HumanoidModel<T>>
    extends RenderLayer<T, M> {

  public AbstractClothingLayer(RenderLayerParent<T, M> renderer) {
    super(renderer);
  }

  @Nullable
  protected abstract ResourceLocation getClothingTexture(LivingEntity livingEntity,
      String skinType);

  @Override
  public void render(PoseStack matrixStack, MultiBufferSource renderTypeBuffer,
      int packedLight, T livingEntity, float limbSwing, float limbSwingAmount,
      float partialTicks, float ageTicks, float headYaw, float headPitch) {
    Minecraft minecraft = Minecraft.getInstance();
    boolean invisible = livingEntity.isInvisible();
    boolean partiallyVisible =
        livingEntity.isInvisible() && !livingEntity.isInvisibleTo(minecraft.player);
    if (partiallyVisible || !invisible) {
      String skinType = livingEntity instanceof LocalPlayer
          ? ((LocalPlayer) livingEntity).getModelName()
          : "default";
      ResourceLocation texture = this.getClothingTexture(livingEntity, skinType);
      if (texture != null) {
        RenderType renderType = partiallyVisible ? RenderType.itemEntityTranslucentCull(texture)
            : this.getParentModel().renderType(texture);
        this.getParentModel().renderToBuffer(matrixStack, renderTypeBuffer.getBuffer(renderType),
            packedLight, LivingEntityRenderer.getOverlayCoords(livingEntity, 0.0F), 1.0F, 1.0F, 1.0F,
            partiallyVisible ? 0.15F : 1.0F);
      }
    }
  }
}
