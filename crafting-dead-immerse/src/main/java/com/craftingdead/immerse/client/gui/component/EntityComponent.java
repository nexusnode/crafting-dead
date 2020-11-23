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
package com.craftingdead.immerse.client.gui.component;

import java.util.Arrays;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import io.noties.tumbleweed.TweenType;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;

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

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    super.render(mouseX, mouseY, partialTicks);

    this.minecraft.getRenderManager().cacheActiveRenderInfo(FakeWorld.getInstance(),
        this.minecraft.gameRenderer.getActiveRenderInfo(), null);
    RenderSystem.pushMatrix();
    {
      RenderSystem.translated(0, 0, 1050.0F);
      RenderSystem.scalef(1.0F, 1.0F, -1.0F);
      MatrixStack matrixStack = new MatrixStack();
      matrixStack.translate(this.getScaledContentX() + this.getScaledContentWidth() / 2,
          this.getScaledContentY() + this.getScaledContentHeight(), 1000.0D);
      matrixStack.scale(this.getScaledContentWidth() / 2, this.getScaledContentHeight() / 2, 50);
      matrixStack.rotate(Vector3f.ZP.rotationDegrees(180.0F));

      final float oldYawOffset = this.livingEntity.renderYawOffset;
      final float oldYaw = this.livingEntity.rotationYaw;
      final float oldPitch = this.livingEntity.rotationPitch;
      final float oldPrevHeadYaw = this.livingEntity.prevRotationYawHead;
      final float oldHeadYaw = this.livingEntity.rotationYawHead;

      float headYaw = (float) Math
          .atan((this.getScaledContentX() + this.getScaledContentWidth() / 2 - mouseX) / 40.0F);
      float headPitch = (float) Math
          .atan((this.getScaledContentY() + this.getScaledContentHeight() / 4 - mouseY) / 40.0F);
      this.livingEntity.renderYawOffset = 180.0F + headYaw * 20.0F;
      this.livingEntity.rotationYaw = 180.0F + headYaw * 40.0F;
      this.livingEntity.rotationPitch = -headPitch * 20.0F;
      this.livingEntity.rotationYawHead = this.livingEntity.rotationYaw;
      this.livingEntity.prevRotationYawHead = this.livingEntity.rotationYaw;
      final EntityRendererManager entityRendererManager = this.minecraft.getRenderManager();

      entityRendererManager.setRenderShadow(false);
      IRenderTypeBuffer.Impl renderTypeBufferImpl =
          this.minecraft.getRenderTypeBuffers().getBufferSource();
      entityRendererManager.renderEntityStatic(this.livingEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F,
          matrixStack, renderTypeBufferImpl, 0xF000F0);
      renderTypeBufferImpl.finish();
      entityRendererManager.setRenderShadow(false);

      this.livingEntity.renderYawOffset = oldYawOffset;
      this.livingEntity.rotationYaw = oldYaw;
      this.livingEntity.rotationPitch = oldPitch;
      this.livingEntity.prevRotationYawHead = oldPrevHeadYaw;
      this.livingEntity.rotationYawHead = oldHeadYaw;
    }
    RenderSystem.popMatrix();
  }
}
