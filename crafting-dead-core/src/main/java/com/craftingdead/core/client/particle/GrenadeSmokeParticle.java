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

import com.craftingdead.core.particle.GrenadeSmokeParticleData;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;

/**
 * Lightweight smoke particle for grenades.
 */
public class GrenadeSmokeParticle extends SpriteTexturedParticle {
  private final IAnimatedSprite animatedSprite;

  private GrenadeSmokeParticle(GrenadeSmokeParticleData data, IAnimatedSprite animatedSprite,
      ClientWorld world, double x, double y, double z) {
    super(world, x, y, z);
    this.animatedSprite = animatedSprite;
    float colorScale = 1.0F - (float) (Math.random() * (double) 0.3F);
    this.particleRed = colorScale * data.getRed();
    this.particleGreen = colorScale * data.getGreen();
    this.particleBlue = colorScale * data.getBlue();
    this.particleScale *= data.getScale();

    // From vanilla's Cloud particle
    int i = (int) (10.0D / (Math.random() * 0.8D + 0.3D));
    this.maxAge = (int) Math.max((float) i * 2.5F, 1.0F);

    this.canCollide = false;
    this.selectSpriteWithAge(animatedSprite);
  }

  @Override
  public IParticleRenderType getRenderType() {
    return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  @Override
  public float getScale(float partialTicks) {
    return this.particleScale * MathHelper
        .clamp(((float) this.age + partialTicks) / (float) this.maxAge * 32.0F, 0.0F, 1.0F);
  }

  @Override
  public void tick() {
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    if (this.age++ >= this.maxAge) {
      this.setExpired();
    } else {
      this.selectSpriteWithAge(this.animatedSprite);
    }
  }

  public static class Factory implements IParticleFactory<GrenadeSmokeParticleData> {
    private final IAnimatedSprite spriteSet;

    public Factory(IAnimatedSprite animatedSprite) {
      this.spriteSet = animatedSprite;
    }

    @Override
    public Particle makeParticle(GrenadeSmokeParticleData data, ClientWorld world, double xPos,
        double yPos, double zPos, double xVelocity, double yVelocity, double zVelocity) {
      return new GrenadeSmokeParticle(data, this.spriteSet, world, xPos, yPos, zPos);
    }
  }
}
