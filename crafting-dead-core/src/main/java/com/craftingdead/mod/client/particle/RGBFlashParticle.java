package com.craftingdead.mod.client.particle;

import com.craftingdead.mod.particle.RGBFlashParticleData;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class RGBFlashParticle extends SpriteTexturedParticle {
  private RGBFlashParticle(RGBFlashParticleData data, World world, double x, double y, double z) {
    super(world, x, y, z);
    this.maxAge = 4;
    this.particleRed = data.getRed();
    this.particleGreen = data.getGreen();
    this.particleBlue = data.getBlue();
    this.particleScale = data.getScale();
  }

  @Override
  public IParticleRenderType getRenderType() {
    return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  @Override
  public void buildGeometry(IVertexBuilder vertexBuilder, ActiveRenderInfo activeRenderInfo,
      float partialTicks) {
    this.setAlphaF(0.6F - ((float) this.age + partialTicks - 1.0F) * 0.25F * 0.5F);
    super.buildGeometry(vertexBuilder, activeRenderInfo, partialTicks);
  }

  @Override
  public float getScale(float partialTicks) {
    return 3.1F * MathHelper.sin(((float) this.age + partialTicks - 1.0F) * 0.25F * (float) Math.PI)
        * this.particleScale;
  }

  public static class Factory implements IParticleFactory<RGBFlashParticleData> {
    private final IAnimatedSprite animatedSprite;

    public Factory(IAnimatedSprite animatedSprite) {
      this.animatedSprite = animatedSprite;
    }

    @Override
    public Particle makeParticle(RGBFlashParticleData data, World world, double xPos, double yPos,
        double zPos, double xVelocity, double yVelocity, double zVelocity) {
      RGBFlashParticle flashParticle = new RGBFlashParticle(data, world, xPos, yPos, zPos);
      flashParticle.selectSpriteRandomly(this.animatedSprite);
      return flashParticle;
    }
  }
}
