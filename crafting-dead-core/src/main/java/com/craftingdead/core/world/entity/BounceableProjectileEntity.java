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

package com.craftingdead.core.world.entity;

import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

public abstract class BounceableProjectileEntity extends Entity
    implements IEntityAdditionalSpawnData {

  @Nullable
  private UUID sourceId;
  private BlockState blockStanding;
  private boolean stoppedMoving;
  private int totalTicksInAir;
  private int motionStopCount;
  private Direction hitDirection = Direction.UP;
  protected boolean isSticky;

  public BounceableProjectileEntity(EntityType<? extends BounceableProjectileEntity> type,
      Level level) {
    super(type, level);
  }

  public BounceableProjectileEntity(EntityType<? extends BounceableProjectileEntity> type,
      double x, double y, double z, Level level) {
    this(type, level);
    this.setPos(x, y, z);
  }

  public BounceableProjectileEntity(EntityType<? extends BounceableProjectileEntity> type,
      LivingEntity throwerEntity, Level level) {
    this(type, throwerEntity.getX(), throwerEntity.getEyeY(), throwerEntity.getZ(), level);
    this.setSource(throwerEntity);
  }

  public void setSource(Entity source) {
    this.setSourceId(source.getUUID());
  }

  public void setSourceId(UUID sourceId) {
    this.sourceId = sourceId;
  }

  public void setSticky(boolean isSticky) {
    this.isSticky = isSticky;
  }

  @Override
  public void tick() {
    super.tick();

    BlockPos currentBlockPos = this.blockPosition();
    BlockState currentBlockState = this.level.getBlockState(currentBlockPos);

    if (this.stoppedMoving) {
      // Places the current inBlockState if it is not present
      if (this.blockStanding == null) {
        this.blockStanding = currentBlockState;
      }

      boolean notCollided = (this.blockStanding != currentBlockState
          && this.level.noCollision((new AABB(this.position(), this.position())).inflate(0.06D)));
      boolean shouldMove = !this.getDeltaMovement().equals(Vec3.ZERO) || notCollided;

      if (shouldMove) {
        this.stoppedMoving = false;
        this.blockStanding = null;
      }
    }

    if (!this.stoppedMoving) {
      // Natural loss of speed
      this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));

      // Gravity speed
      if (!this.isNoGravity()) {
        this.setDeltaMovement(this.getDeltaMovement().subtract(0, 0.04D, 0));
      }

      this.totalTicksInAir++;
      Vec3 position = this.position();
      Vec3 motionBeforeHit = this.getDeltaMovement();

      BlockHitResult blockRayTraceResult =
          this.level.clip(new ClipContext(position, position.add(motionBeforeHit),
              ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));

      Vec3 nextBounceMotion = null;

      if (blockRayTraceResult.getType() != HitResult.Type.MISS) {
        BlockState blockHitState = this.level.getBlockState(blockRayTraceResult.getBlockPos());

        Vec3 difference =
            blockRayTraceResult.getLocation().subtract(this.getX(), this.getY(),
                this.getZ());
        this.setDeltaMovement(difference);

        // From vanilla's AbstractArrowEntity logic.
        // I didn't found a good name for the variable.
        Vec3 vec = difference.normalize().scale(0.05D);
        this.setPos(this.getX() - vec.x, this.getY() - vec.y, this.getZ() - vec.z);

        Optional<Float> optional = this.getBounceFactor(blockRayTraceResult);
        if (optional.isPresent()) {
          final float bounceFactor = optional.get();
          switch (blockRayTraceResult.getDirection()) {
            case UP:
              // If the grenade is going up too slowly, set the next bounce to zero
              nextBounceMotion = Math.abs(motionBeforeHit.y) > 0.1D
                  ? motionBeforeHit.multiply(0.9D * 0.7D, (-bounceFactor), 0.9D * 0.7D)
                  : Vec3.ZERO;
              break;
            case DOWN:
              nextBounceMotion = motionBeforeHit.multiply(0.9D, -bounceFactor, 0.9D);
              break;
            case WEST:
            case EAST:
              nextBounceMotion = motionBeforeHit.multiply(-bounceFactor, 0.9D, 0.9D);
              break;
            case SOUTH:
            case NORTH:
              nextBounceMotion = motionBeforeHit.multiply(0.9D, 0.9D, -bounceFactor);
              break;
            default:
              break;
          }
        } else {
          nextBounceMotion = Vec3.ZERO;
        }
        this.onSurfaceHit(blockRayTraceResult);

        // Check if the entity is still alive after hitting the surface
        if (this.isAlive()) {
          if ((nextBounceMotion != null && blockRayTraceResult.getDirection() == Direction.UP)
              || this.isSticky) {
            // Stops if the next bounce is too slow
            if ((nextBounceMotion.length() < 0.1D) || this.isSticky) {
              nextBounceMotion = Vec3.ZERO;
              this.stoppedMoving = true;
              this.blockStanding = blockHitState;
              this.onMotionStop(++this.motionStopCount);
              this.hitDirection = blockRayTraceResult.getDirection();
            }
          }
        }
      }

      Vec3 currentMotion = this.getDeltaMovement();

      double nextX = this.getX() + currentMotion.x;
      double nextY = this.getY() + currentMotion.y;
      double nextZ = this.getZ() + currentMotion.z;

      // Use .setPosition instead of .move() for a better collision detection.
      // Otherwise the grenade will bounce imprecisely.
      this.setPos(nextX, nextY, nextZ);
      this.checkInsideBlocks();

      if (nextBounceMotion != null) {
        this.setDeltaMovement(nextBounceMotion);
      }
    }
  }

  /**
   * Called when the projectile hits a surface
   */
  public abstract void onSurfaceHit(BlockHitResult blockRayTraceResult);

  /**
   * Called when the projectile's motion gets zeroed.
   */
  public abstract void onMotionStop(int stopsCount);

  public void shootFromEntity(Entity entity, float x, float y, float z, float force,
      float p_184538_6_) {
    float f = -Mth.sin(y * ((float) Math.PI / 180F))
        * Mth.cos(x * ((float) Math.PI / 180F));
    float f1 = -Mth.sin((x + z) * ((float) Math.PI / 180F));
    float f2 =
        Mth.cos(y * ((float) Math.PI / 180F)) * Mth.cos(x * ((float) Math.PI / 180F));
    this.shoot((double) f, (double) f1, (double) f2, force, p_184538_6_);
    Vec3 vec3d = entity.getDeltaMovement();
    this.setDeltaMovement(
        this.getDeltaMovement().add(vec3d.x, entity.isOnGround() ? 0.0D : vec3d.y, vec3d.z));
  }

  public void shoot(double x, double y, double z, float force, float p_70186_8_) {
    Vec3 vec = (new Vec3(x, y, z)).normalize()
        .add(this.random.nextGaussian() * (double) 0.0075F * (double) p_70186_8_,
            this.random.nextGaussian() * (double) 0.0075F * (double) p_70186_8_,
            this.random.nextGaussian() * (double) 0.0075F * (double) p_70186_8_)
        .scale((double) force);
    this.setDeltaMovement(vec);
    float f = Mth.sqrt((float) this.distanceToSqr(vec));
    this.setYRot((float) (Mth.atan2(vec.x, vec.z) * (double) (180F / (float) Math.PI)));
    this.setXRot((float) (Mth.atan2(vec.y, (double) f) * (double) (180F / (float) Math.PI)));
    this.yRotO = this.getYRot();
    this.xRotO = this.getXRot();
  }

  public int getTotalTicksInAir() {
    return this.totalTicksInAir;
  }

  /**
   * Gets the {@link BlockState} in which this projectile is standing while stopped.
   */
  public BlockState getBlockStanding() {
    return this.blockStanding;
  }

  /**
   * Whether this projectile is in the ground, without any movements.
   */
  public boolean hasStoppedMoving() {
    return this.stoppedMoving;
  }

  public int getMotionStopCount() {
    return motionStopCount;
  }

  /**
   * Gets the amount of gravity to apply to the thrown entity with each tick.
   */
  protected float getGravityVelocity() {
    return 1.04F;
  }

  /**
   * @return the bounce factor
   */
  public Optional<Float> getBounceFactor(BlockHitResult blockRayTraceResult) {
    return Optional.of(0.375F);
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag compound) {
    if (this.sourceId != null) {
      compound.putUUID("sourceId", this.sourceId);
    }

    compound.putBoolean("stoppedMoving", this.stoppedMoving);
    compound.putInt("motionStopCount", this.motionStopCount);
    compound.putBoolean("isSticky", this.isSticky);
    compound.putString("hitDirection", this.hitDirection.getSerializedName());
  }

  @Override
  protected void readAdditionalSaveData(CompoundTag compound) {
    if (compound.hasUUID("sourceId")) {
      this.sourceId = compound.getUUID("sourceId");
    }
    this.stoppedMoving = compound.getBoolean("stoppedMoving");
    this.motionStopCount = compound.getInt("motionStopCount");
    this.isSticky = compound.getBoolean("isSticky");
    var hitDirection = Direction.byName(compound.getString("hitDirection"));
    if (hitDirection != null) {
      this.hitDirection = hitDirection;
    }
  }

  @Override
  public void writeSpawnData(FriendlyByteBuf buffer) {
    buffer.writeBoolean(this.stoppedMoving);
    buffer.writeInt(this.totalTicksInAir);
    buffer.writeInt(this.motionStopCount);
    buffer.writeBoolean(this.isSticky);
    buffer.writeEnum(this.hitDirection);
  }

  @Override
  public void readSpawnData(FriendlyByteBuf buffer) {
    this.stoppedMoving = buffer.readBoolean();
    this.totalTicksInAir = buffer.readInt();
    this.motionStopCount = buffer.readInt();
    this.isSticky = buffer.readBoolean();
    this.hitDirection = buffer.readEnum(Direction.class);
  }

  public Optional<Entity> getSource() {
    return this.level instanceof ServerLevel level
        ? Optional.ofNullable(level.getEntity(this.sourceId))
        : Optional.empty();
  }

  public Direction getHitDirection() {
    return hitDirection;
  }
}
