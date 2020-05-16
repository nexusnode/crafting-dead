package com.craftingdead.mod.entity.grenade;

import java.util.List;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.entity.ModEntityTypes;
import com.craftingdead.mod.inventory.InventorySlotType;
import com.craftingdead.mod.item.GrenadeItem;
import com.craftingdead.mod.item.ModItems;
import com.craftingdead.mod.particle.GrenadeSmokeParticleData;
import com.craftingdead.mod.potion.ModEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class GasGrenadeEntity extends GrenadeEntity {

  private static final GrenadeSmokeParticleData MEDIUM_DARK_GREEN_SMOKE =
      new GrenadeSmokeParticleData(0.12F, 0.35F, 0.12F, 4F);
  private static final float START_DECREASING_PITCH_AT = 0.65F; // in %

  public GasGrenadeEntity(EntityType<? extends GrenadeEntity> entityIn, World worldIn) {
    super(entityIn, worldIn);
  }

  public GasGrenadeEntity(LivingEntity thrower, World worldIn) {
    super(ModEntityTypes.gasGrenade, thrower, worldIn);
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
    double radius = MathHelper.lerp(Math.min(activatedTicksCount, 80D) / 80D, 0.5D, 5D);
    double height = 1.8D;

    if (this.world.isRemote()) {
      if (activatedTicksCount % 10 == 0) {
        int maximumDuration = this.getMinimumTicksUntilAutoDeactivation();
        float progress =
            Math.max(activatedTicksCount - (maximumDuration * START_DECREASING_PITCH_AT), 0)
                / (float) (maximumDuration * (1F - START_DECREASING_PITCH_AT));
        float gradualPitch = MathHelper.lerp(1F - progress, 0.5F, 1.7F);
        this.world.playSound(this.getX(), this.getY(), this.getZ(),
            SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.HOSTILE, 1.5F,
            gradualPitch, false);
      }

      if (activatedTicksCount % 10 == 0) {
        double cylinderVolume = Math.PI * Math.pow(radius, 2) * height;

        for (int i = 0; i < cylinderVolume; i++) {
          double theta = this.rand.nextDouble() * 2 * Math.PI;
          double phi = Math.acos(2 * this.rand.nextDouble() - 1);

          double extraX = radius * Math.sin(phi) * Math.cos(theta);
          double extraY = height * this.rand.nextDouble();
          double extraZ = radius * Math.cos(phi);

          this.world.addParticle(MEDIUM_DARK_GREEN_SMOKE, true, this.getX() + extraX,
              this.getY() + extraY, this.getZ() + extraZ, 0, 0, 0);
        }
      }
    } else {
      if (activatedTicksCount % 5 == 0) {
        // Extra size for better detection
        double detectionRadius = radius + 0.75D;

        List<Entity> foundEntities = this.world.getEntitiesInAABBexcluding(this,
            this.getBoundingBox().grow(detectionRadius, height, detectionRadius),
            (foundEntity) -> {
              if (!(foundEntity instanceof LivingEntity)) {
                return false;
              }

              double xDiff = this.getX() - foundEntity.getX();
              double zDiff = this.getZ() - foundEntity.getZ();
              double distance2D = MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff);

              // Entity should be inside the cylinder's radius,
              // as BoundingBox is a cube
              if (distance2D > detectionRadius) {
                return false;
              }

              return true;
            });

        foundEntities.forEach(entity -> {
          LivingEntity livingEntity = (LivingEntity) entity;
          if (this.canInhaleGas(livingEntity)) {
            boolean wasPoisonApplied = ModEffects.applyOrOverrideIfLonger(livingEntity,
                new EffectInstance(Effects.POISON, 100, 2));
            if (wasPoisonApplied) {
              ModEffects.applyOrOverrideIfLonger(livingEntity,
                  new EffectInstance(Effects.SLOWNESS, 100, 2));
            }
          }
        });
      }
    }
  }

  public boolean canInhaleGas(LivingEntity livingEntity) {
    boolean hasMask = livingEntity.getCapability(ModCapabilities.LIVING)
        .map(entity -> entity.getInventory()
        .getStackInSlot(InventorySlotType.HAT.getIndex()).getItem() == ModItems.GAS_MASK.get())
        .orElse(false);

    return !hasMask
        && !livingEntity.areEyesInFluid(FluidTags.WATER)
        && !livingEntity.areEyesInFluid(FluidTags.LAVA);
  }

  @Override
  public GrenadeItem asItem() {
    return ModItems.GAS_GRENADE.get();
  }
}
