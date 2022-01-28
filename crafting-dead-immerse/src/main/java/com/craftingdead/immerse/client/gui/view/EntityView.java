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

import com.craftingdead.core.client.util.RenderUtil;
import com.craftingdead.immerse.client.fake.FakeLevel;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.LivingEntity;
import com.mojang.math.Vector3f;

public class EntityView<L extends Layout> extends View<EntityView<L>, L> {

  private final LivingEntity livingEntity;

  public EntityView(L layout, LivingEntity entity) {
    super(layout);
    this.livingEntity = entity;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void renderContent(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    super.renderContent(matrixStack, mouseX, mouseY, partialTicks);
    this.minecraft.getEntityRenderDispatcher().prepare(FakeLevel.getInstance(),
        this.minecraft.gameRenderer.getMainCamera(), null);
    matrixStack.pushPose();
    {
      matrixStack.translate(0, 0, 1050.0F);
      matrixStack.scale(1.0F, 1.0F, -1.0F);
      matrixStack.translate(
          this.getScaledContentX() + this.getScaledContentWidth() / 2.0F,
          this.getScaledContentY() + this.getScaledContentHeight(), 1000.0D);
      matrixStack.scale(this.getXScale(), this.getYScale(), 1.0F);
      matrixStack.scale(this.getContentWidth() / 2.0F, this.getContentHeight() / 2.0F,
          1.0F);
      matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));

      final float oldYawOffset = this.livingEntity.yBodyRot;
      final float oldYaw = this.livingEntity.getYRot();
      final float oldPitch = this.livingEntity.getXRot();
      final float oldPrevHeadYaw = this.livingEntity.yHeadRotO;
      final float oldHeadYaw = this.livingEntity.yHeadRot;

      float headYaw = (float) Math
          .atan((this.getScaledContentX() + this.getScaledContentWidth() / 2.0F - mouseX) / 40.0F);
      float headPitch = (float) Math
          .atan((this.getScaledContentY() + this.getScaledContentHeight() / 4.0F - mouseY) / 40.0F);
      this.livingEntity.yBodyRot = 180.0F + headYaw * 20.0F;
      this.livingEntity.setYRot(180.0F + headYaw * 40.0F);
      this.livingEntity.setXRot(-headPitch * 20.0F);
      this.livingEntity.yHeadRot = this.livingEntity.getYRot();
      this.livingEntity.yHeadRotO = this.livingEntity.getYRot();
      final EntityRenderDispatcher entityRendererManager =
          this.minecraft.getEntityRenderDispatcher();

      entityRendererManager.setRenderShadow(false);
      MultiBufferSource.BufferSource renderTypeBufferImpl =
          this.minecraft.renderBuffers().bufferSource();

      RenderSystem.runAsFancy(() -> entityRendererManager.render(this.livingEntity, 0.0D, 0.0D,
          0.0D, 0.0F, 1.0F, matrixStack, renderTypeBufferImpl, RenderUtil.FULL_LIGHT));

      renderTypeBufferImpl.endBatch();
      entityRendererManager.setRenderShadow(false);

      this.livingEntity.yBodyRot = oldYawOffset;
      this.livingEntity.setYRot(oldYaw);
      this.livingEntity.setXRot(oldPitch);
      this.livingEntity.yHeadRotO = oldPrevHeadYaw;
      this.livingEntity.yHeadRot = oldHeadYaw;
    }
    matrixStack.popPose();
  }
}
