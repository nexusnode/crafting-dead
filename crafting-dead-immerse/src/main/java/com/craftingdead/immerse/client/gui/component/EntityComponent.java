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

package com.craftingdead.immerse.client.gui.component;

import java.util.Arrays;
import com.craftingdead.core.client.util.RenderUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import io.noties.tumbleweed.TweenType;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3f;

public class EntityComponent extends Component<EntityComponent> {

  public static final TweenType<EntityComponent> X_ROTATION =
      new SimpleTweenType<>(1, t -> Arrays.copyOf(t.rotation, 1), (t, v) -> t.rotation[0] = v[0]);
  public static final TweenType<EntityComponent> Y_ROTATION =
      new SimpleTweenType<>(1, t -> Arrays.copyOfRange(t.rotation, 1, 2),
          (t, v) -> t.rotation[0] = v[0]);
  public static final TweenType<EntityComponent> Z_ROTATION =
      new SimpleTweenType<>(1, t -> Arrays.copyOf(t.rotation, 1), (t, v) -> t.rotation[0] = v[0]);

  private final LivingEntity livingEntity;

  private float[] rotation = new float[3];

  public EntityComponent(LivingEntity entity) {
    this.livingEntity = entity;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void renderContent(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    super.renderContent(matrixStack, mouseX, mouseY, partialTicks);

    this.minecraft.getEntityRenderDispatcher().prepare(FakeLevel.getInstance(),
        this.minecraft.gameRenderer.getMainCamera(), null);
    matrixStack.pushPose();
    {
      matrixStack.translate(0, 0, 1050.0F);
      matrixStack.scale(1.0F, 1.0F, -1.0F);

      matrixStack.translate(this.getScaledContentX() + this.getScaledContentWidth() / 2.0F,
          this.getScaledContentY() + this.getScaledContentHeight(), 1000.0D);
      matrixStack.scale(this.getScaledContentWidth() / 2.0F, this.getScaledContentHeight() / 2.0F,
          1.0F);
      matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));

      final float oldYawOffset = this.livingEntity.yBodyRot;
      final float oldYaw = this.livingEntity.yRot;
      final float oldPitch = this.livingEntity.xRot;
      final float oldPrevHeadYaw = this.livingEntity.yHeadRotO;
      final float oldHeadYaw = this.livingEntity.yHeadRot;

      float headYaw = (float) Math
          .atan((this.getScaledContentX() + this.getScaledContentWidth() / 2.0F - mouseX) / 40.0F);
      float headPitch = (float) Math
          .atan((this.getScaledContentY() + this.getScaledContentHeight() / 4.0F - mouseY) / 40.0F);
      this.livingEntity.yBodyRot = 180.0F + headYaw * 20.0F;
      this.livingEntity.yRot = 180.0F + headYaw * 40.0F;
      this.livingEntity.xRot = -headPitch * 20.0F;
      this.livingEntity.yHeadRot = this.livingEntity.yRot;
      this.livingEntity.yHeadRotO = this.livingEntity.yRot;
      final EntityRendererManager entityRendererManager =
          this.minecraft.getEntityRenderDispatcher();

      entityRendererManager.setRenderShadow(false);
      IRenderTypeBuffer.Impl renderTypeBufferImpl =
          this.minecraft.renderBuffers().bufferSource();

      RenderSystem.runAsFancy(() -> entityRendererManager.render(this.livingEntity, 0.0D, 0.0D,
          0.0D, 0.0F, 1.0F, matrixStack, renderTypeBufferImpl, RenderUtil.FULL_LIGHT));

      renderTypeBufferImpl.endBatch();
      entityRendererManager.setRenderShadow(false);

      this.livingEntity.yBodyRot = oldYawOffset;
      this.livingEntity.yRot = oldYaw;
      this.livingEntity.xRot = oldPitch;
      this.livingEntity.yHeadRotO = oldPrevHeadYaw;
      this.livingEntity.yHeadRot = oldHeadYaw;
    }
    matrixStack.popPose();
  }
}
