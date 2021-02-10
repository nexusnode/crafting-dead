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

package com.craftingdead.core.client.renderer.item.model;

import org.lwjgl.opengl.GL11;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderState.LightmapState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class ModelMuzzleFlash extends Model {
  // fields
  ModelRenderer Box_0;
  ModelRenderer Box_1;
  ModelRenderer Box_3;

  public ModelMuzzleFlash() {
    super(ModelMuzzleFlash::getFlashRenderType);
    textureWidth = 64;
    textureHeight = 64;

    Box_0 = new ModelRenderer(this, 1, 1);
    Box_0.addBox(0F, 0F, 0F, 8, 8, 0);
    Box_0.setRotationPoint(-4F, -4F, 0F);
    Box_0.setTextureSize(64, 32);
    Box_0.mirror = true;
    setRotation(Box_0, 0F, 0F, 0F);
    Box_1 = new ModelRenderer(this, 9, 1);
    Box_1.addBox(-4F, 0F, 0F, 8, 0, 15);
    Box_1.setRotationPoint(0F, 0F, -15F);
    Box_1.setTextureSize(64, 32);
    Box_1.mirror = true;
    setRotation(Box_1, 0F, 0F, -0.7853982F);
    Box_3 = new ModelRenderer(this, 1, 17);
    Box_3.addBox(-4F, 0F, 0F, 8, 0, 15);
    Box_3.setRotationPoint(0F, 0F, -15F);
    Box_3.setTextureSize(64, 32);
    Box_3.mirror = true;
    setRotation(Box_3, 0F, 0F, -2.373648F);
  }

  private void setRotation(ModelRenderer model, float x, float y, float z) {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }

  @Override
  public void render(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int packedLight,
      int packedOverlay, float red, float green, float blue,
      float alpha) {
    Box_0.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Box_1.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
    Box_3.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green,
        blue, alpha);
  }

  private static RenderType getFlashRenderType(ResourceLocation texture) {
    return RenderType.makeType("flash",
        DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP, GL11.GL_QUADS, 256, false,
        true, RenderType.State.getBuilder()
            .lightmap(new LightmapState(true))
            .texture(new RenderState.TextureState(texture,
                false, false))
            .transparency(new RenderState.TransparencyState("translucent_transparency", () -> {
              RenderSystem.enableBlend();
              RenderSystem.defaultBlendFunc();
            }, () -> {
              RenderSystem.disableBlend();
            }))
            .build(true));
  }
}
