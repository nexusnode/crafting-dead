package com.craftingdead.mod.entity.grenade;

import com.craftingdead.mod.entity.ModEntityTypes;
import com.craftingdead.mod.item.GrenadeItem;
import com.craftingdead.mod.item.ModItems;
import com.craftingdead.mod.particle.GrenadeSmokeParticleData;
import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class SmokeGrenadeEntity extends GrenadeEntity {

  public static final GrenadeSmokeParticleData LARGE_WHITE_SMOKE =
      new GrenadeSmokeParticleData(1F, 1F, 1F, 10F);
  private static final float START_DECREASING_PITCH_AT = 0.65F; // in %
  private static final int FIRE_BLOCK_STATE_FLAGS = 1;

  public SmokeGrenadeEntity(EntityType<? extends GrenadeEntity> entityIn, World worldIn) {
    super(entityIn, worldIn);
  }

  public SmokeGrenadeEntity(LivingEntity thrower, World worldIn) {
    super(ModEntityTypes.smokeGrenade, thrower, worldIn);
  }

  @Override
  public Integer getMinimumTicksUntilAutoDeactivation() {
    return 500;
  }

  @Override
  public void onMotionStop(int stopsCount) {
    if (stopsCount == 1) {
      this.setActivated(true);
    }
  }

  @Override
  public void onActivationStateChange(boolean activated) {
    if (!activated) {
      if (!this.world.isRemote()) {
        this.remove();
      }
    }
  }

  @Override
  public void onGrenadeTick() {
    if (!this.isActivated()) {
      return;
    }

    int activatedTicksCount = this.getActivatedTicksCount();
    double radius = MathHelper.lerp(Math.min(activatedTicksCount, 30D) / 30D, 2D, 5D);

    if (this.world.isRemote()) {
      if (activatedTicksCount % 10 == 0) {
        int maximumDuration = this.getMinimumTicksUntilAutoDeactivation();
        float progress =
            Math.max(activatedTicksCount - (maximumDuration * START_DECREASING_PITCH_AT), 0)
                / (float) (maximumDuration * (1F - START_DECREASING_PITCH_AT));
        float gradualPitch = MathHelper.lerp(1F - progress, 0.5F, 1.7F);
        this.world.playSound(this.getX(), this.getY(), this.getZ(),
            SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.HOSTILE, 1.5F, gradualPitch, false);
      }

      if (activatedTicksCount % 5 == 0) {
        double halfSphereVolume = (4D / 3D) * Math.PI * Math.pow(radius / 2D, 3);
        for (int i = 0; i < halfSphereVolume; i++) {
          double theta = this.rand.nextDouble() * 2 * Math.PI;
          double phi = Math.acos(2 * this.rand.nextDouble() - 1);

          double extraX = radius * Math.sin(phi) * Math.cos(theta);
          double extraY =
              radius * Math.abs(Math.sin(phi) * Math.sin(theta)) * this.rand.nextDouble();
          double extraZ = radius * Math.cos(phi);

          this.world.addParticle(LARGE_WHITE_SMOKE, true, this.getX() + extraX,
              this.getY() + extraY, this.getZ() + extraZ, 0, 0, 0);
        }
      }
    } else {
      if (activatedTicksCount % 10 == 0) {
        // Extra size for better detection
        double detectionRadius = radius + 1.5D;

        BlockPos from = this.getPosition().add(-detectionRadius, 0, -detectionRadius);
        BlockPos to = this.getPosition().add(detectionRadius, detectionRadius, detectionRadius);
        BlockPos.getAllInBox(from, to).forEach(blockPos -> {

          double xDiff = this.getX() - blockPos.getX();
          double zDiff = this.getZ() - blockPos.getZ();
          double distance2D = MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff);

          if (distance2D <= detectionRadius) {
            BlockState blockState = this.world.getBlockState(blockPos);
            if (blockState.getBlock() instanceof FireBlock) {
              // Sets the fire block to its final age.
              // It should be extinguished in a short time.
              this.world.setBlockState(blockPos, blockState.with(FireBlock.AGE, 15),
                  FIRE_BLOCK_STATE_FLAGS);
            }
          }
        });
      }
    }
  }

  @Override
  public GrenadeItem asItem() {
    return ModItems.SMOKE_GRENADE.get();
  }
}
