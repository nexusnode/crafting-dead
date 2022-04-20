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

import com.craftingdead.core.particle.FlashParticleOptions;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;

public class FlashParticle extends TextureSheetParticle {

  private FlashParticle(FlashParticleOptions options, ClientLevel level,
      double x, double y, double z) {
    super(level, x, y, z);
    this.lifetime = 4;
    this.rCol = options.red();
    this.gCol = options.green();
    this.bCol = options.blue();
    this.quadSize = options.scale();
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

  public static class Factory implements ParticleProvider<FlashParticleOptions> {

    private final SpriteSet animatedSprite;

    public Factory(SpriteSet animatedSprite) {
      this.animatedSprite = animatedSprite;
    }

    @Override
    public Particle createParticle(FlashParticleOptions data, ClientLevel world, double xPos,
        double yPos, double zPos, double xVelocity, double yVelocity, double zVelocity) {
      FlashParticle flashParticle = new FlashParticle(data, world, xPos, yPos, zPos);
      flashParticle.pickSprite(this.animatedSprite);
      return flashParticle;
    }
  }
}
