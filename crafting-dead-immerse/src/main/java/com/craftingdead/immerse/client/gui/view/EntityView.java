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
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.world.entity.LivingEntity;

public class EntityView<L extends Layout> extends View<EntityView<L>, L> {

  private final LivingEntity livingEntity;

  public EntityView(L layout, LivingEntity entity) {
    super(layout);
    this.livingEntity = entity;
  }

  @Override
  public void renderContent(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
    super.renderContent(poseStack, mouseX, mouseY, partialTicks);
    this.minecraft.getEntityRenderDispatcher().prepare(FakeLevel.getInstance(),
        this.minecraft.gameRenderer.getMainCamera(), null);

    final var x = this.getScaledContentX() + this.getScaledContentWidth() / 2.0F;
    final var y = this.getScaledContentY() + this.getScaledContentHeight();

    final var yaw = (float) Math.atan((x - mouseX) / 40.0F);
    final var pitch = (float) Math.atan((y / (2.0F * this.getYScale()) + 4 - mouseY) / 40.0F);

    final var modelViewStack = RenderSystem.getModelViewStack();
    modelViewStack.pushPose();
    {
      modelViewStack.translate(x, y, 1050.0D);
      modelViewStack.scale(1.0F, 1.0F, -1.0F);
      RenderSystem.applyModelViewMatrix();

      poseStack.pushPose();
      {
        poseStack.translate(0.0D, 0.0D, 1000.0D);
        poseStack.scale(this.getXScale(), this.getYScale(), 1.0F);
        poseStack.scale(this.getContentWidth() / 2.0F, this.getContentHeight() / 2.0F, 1.0F);
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        final var lastYBodyRot = this.livingEntity.yBodyRot;
        final var lastYRot = this.livingEntity.getYRot();
        final var lastXRot = this.livingEntity.getXRot();
        final var lastYHeadRotO = this.livingEntity.yHeadRotO;
        final var lastYHeadRot = this.livingEntity.yHeadRot;
        this.livingEntity.yBodyRot = 180.0F + yaw * 20.0F;
        this.livingEntity.setYRot(180.0F + yaw * 40.0F);
        this.livingEntity.setXRot(-pitch * 20.0F);
        this.livingEntity.yHeadRot = this.livingEntity.getYRot();
        this.livingEntity.yHeadRotO = this.livingEntity.getYRot();
        Lighting.setupForEntityInInventory();
        final var entityRenderDispatcher = this.minecraft.getEntityRenderDispatcher();
        entityRenderDispatcher.setRenderShadow(false);
        final var bufferSource = this.minecraft.renderBuffers().bufferSource();
        entityRenderDispatcher.render(this.livingEntity, 0.0D, 0.0D, 0.0D,
            0.0F, 1.0F, poseStack, bufferSource, RenderUtil.FULL_LIGHT);
        bufferSource.endBatch();
        entityRenderDispatcher.setRenderShadow(true);
        this.livingEntity.yBodyRot = lastYBodyRot;
        this.livingEntity.setYRot(lastYRot);
        this.livingEntity.setXRot(lastXRot);
        this.livingEntity.yHeadRotO = lastYHeadRotO;
        this.livingEntity.yHeadRot = lastYHeadRot;
        Lighting.setupFor3DItems();
      }
      poseStack.popPose();
    }
    modelViewStack.popPose();
    RenderSystem.applyModelViewMatrix();
  }
}
