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

package com.craftingdead.core.client.particle;

import com.craftingdead.core.particle.GrenadeSmokeParticleData;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;

/**
 * Lightweight smoke particle for grenades.
 */
public class GrenadeSmokeParticle extends TextureSheetParticle {

  private final SpriteSet animatedSprite;

  private GrenadeSmokeParticle(GrenadeSmokeParticleData data, SpriteSet animatedSprite,
      ClientLevel level, double x, double y, double z) {
    super(level, x, y, z);
    this.animatedSprite = animatedSprite;
    float colorScale = 1.0F - (float) (Math.random() * (double) 0.3F);
    this.rCol = colorScale * data.getRed();
    this.gCol = colorScale * data.getGreen();
    this.bCol = colorScale * data.getBlue();
    this.quadSize *= data.getScale();

    // From vanilla's Cloud particle
    int i = (int) (10.0D / (Math.random() * 0.8D + 0.3D));
    this.lifetime = (int) Math.max((float) i * 2.5F, 1.0F);

    this.hasPhysics = false;
    this.setSpriteFromAge(animatedSprite);
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  @Override
  public float getQuadSize(float partialTicks) {
    return this.quadSize * Mth
        .clamp(((float) this.age + partialTicks) / (float) this.lifetime * 32.0F, 0.0F, 1.0F);
  }

  @Override
  public void tick() {
    this.xo = this.x;
    this.yo = this.y;
    this.zo = this.z;
    if (this.age++ >= this.lifetime) {
      this.remove();
    } else {
      this.setSpriteFromAge(this.animatedSprite);
    }
  }

  public static class Factory implements ParticleProvider<GrenadeSmokeParticleData> {

    private final SpriteSet spriteSet;

    public Factory(SpriteSet animatedSprite) {
      this.spriteSet = animatedSprite;
    }

    @Override
    public Particle createParticle(GrenadeSmokeParticleData data, ClientLevel world, double xPos,
        double yPos, double zPos, double xVelocity, double yVelocity, double zVelocity) {
      return new GrenadeSmokeParticle(data, this.spriteSet, world, xPos, yPos, zPos);
    }
  }
}
