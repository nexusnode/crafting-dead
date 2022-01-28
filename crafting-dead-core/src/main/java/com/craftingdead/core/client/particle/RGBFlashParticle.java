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

package com.craftingdead.core.client.particle;

import com.craftingdead.core.particle.RGBFlashParticleData;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;

public class RGBFlashParticle extends TextureSheetParticle {

  private RGBFlashParticle(RGBFlashParticleData data, ClientLevel world, double x, double y,
      double z) {
    super(world, x, y, z);
    this.lifetime = 4;
    this.rCol = data.getRed();
    this.gCol = data.getGreen();
    this.bCol = data.getBlue();
    this.quadSize = data.getScale();
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  @Override
  public void render(VertexConsumer vertexBuilder, Camera activeRenderInfo,
      float partialTicks) {
    this.setAlpha(0.6F - ((float) this.age + partialTicks - 1.0F) * 0.25F * 0.5F);
    super.render(vertexBuilder, activeRenderInfo, partialTicks);
  }

  @Override
  public float getQuadSize(float partialTicks) {
    return 3.1F * Mth.sin(((float) this.age + partialTicks - 1.0F) * 0.25F * (float) Math.PI)
        * this.quadSize;
  }

  public static class Factory implements ParticleProvider<RGBFlashParticleData> {

    private final SpriteSet animatedSprite;

    public Factory(SpriteSet animatedSprite) {
      this.animatedSprite = animatedSprite;
    }

    @Override
    public Particle createParticle(RGBFlashParticleData data, ClientLevel world, double xPos,
        double yPos, double zPos, double xVelocity, double yVelocity, double zVelocity) {
      RGBFlashParticle flashParticle = new RGBFlashParticle(data, world, xPos, yPos, zPos);
      flashParticle.pickSprite(this.animatedSprite);
      return flashParticle;
    }
  }
}
