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

package com.craftingdead.core.world.entity;

import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public abstract class BounceableProjectileEntity extends Entity
    implements IEntityAdditionalSpawnData {

  private UUID ownerId;
  private BlockState blockStanding;
  private boolean stoppedInGround;
  private int totalTicksInAir;
  private int motionStopCount;

  public BounceableProjectileEntity(EntityType<? extends BounceableProjectileEntity> entityType,
      World world) {
    super(entityType, world);
  }

  public BounceableProjectileEntity(EntityType<? extends BounceableProjectileEntity> entityType,
      double x, double y, double z, World world) {
    this(entityType, world);
    this.setPos(x, y, z);
  }

  public BounceableProjectileEntity(EntityType<? extends BounceableProjectileEntity> entityType,
      LivingEntity throwerEntity, World world) {
    this(entityType, throwerEntity.getX(), throwerEntity.getEyeY(), throwerEntity.getZ(),
        world);
    this.ownerId = throwerEntity.getUUID();
  }

  @Override
  public void tick() {
    super.tick();

    BlockPos currentBlockPos = this.blockPosition();
    BlockState currentBlockState = this.level.getBlockState(currentBlockPos);

    if (this.stoppedInGround) {
      // Places the current inBlockState if it is not present
      if (this.blockStanding == null) {
        this.blockStanding = currentBlockState;
      }

      boolean notCollided = (this.blockStanding != currentBlockState
          && this.level.noCollision(this.getBoundingBox().inflate(0.0625D)));
      boolean shouldMove = !this.getDeltaMovement().equals(Vector3d.ZERO) || notCollided;

      if (shouldMove) {
        this.stoppedInGround = false;
        this.blockStanding = null;
      }
    }

    if (!this.stoppedInGround) {
      // Natural loss of speed
      this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));

      // Gravity speed
      if (!this.isNoGravity()) {
        this.setDeltaMovement(this.getDeltaMovement().subtract(0, 0.04D, 0));
      }

      this.totalTicksInAir++;
      Vector3d position = this.position();
      Vector3d motionBeforeHit = this.getDeltaMovement();

      BlockRayTraceResult blockRayTraceResult =
          this.level.clip(new RayTraceContext(position, position.add(motionBeforeHit),
              RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));

      Vector3d nextBounceMotion = null;

      if (blockRayTraceResult.getType() != RayTraceResult.Type.MISS) {
        BlockState blockHitState = this.level.getBlockState(blockRayTraceResult.getBlockPos());

        Vector3d difference =
            blockRayTraceResult.getLocation().subtract(this.getX(), this.getY(),
                this.getZ());
        this.setDeltaMovement(difference);

        // From vanilla's AbstractArrowEntity logic.
        // I didn't found a good name for the variable.
        Vector3d vec = difference.normalize().scale(0.05D);
        this.setPos(this.getX() - vec.x, this.getY() - vec.y, this.getZ() - vec.z);

        Float bounceFactor = this.getBounceFactor(blockRayTraceResult);
        if (bounceFactor != null) {
          switch (blockRayTraceResult.getDirection()) {
            case UP:
              // If the grenade is going up too slowly, set the next bounce to zero
              nextBounceMotion = Math.abs(motionBeforeHit.y) > 0.1D
                  ? motionBeforeHit.multiply(0.9D * 0.7D, (-bounceFactor), 0.9D * 0.7D)
                  : Vector3d.ZERO;
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
          nextBounceMotion = Vector3d.ZERO;
        }

        this.onSurfaceHit(blockRayTraceResult);

        // Check if the entity is still alive after hitting the surface
        if (this.isAlive()) {
          if (nextBounceMotion != null && blockRayTraceResult.getDirection() == Direction.UP) {
            // Stops if the next bounce is too slow
            if (nextBounceMotion.length() < 0.1D) {
              nextBounceMotion = Vector3d.ZERO;
              this.stoppedInGround = true;
              this.blockStanding = blockHitState;
              this.onMotionStop(++this.motionStopCount);
            }
          }
        }
      }

      Vector3d currentMotion = this.getDeltaMovement();

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
  public abstract void onSurfaceHit(BlockRayTraceResult blockRayTraceResult);

  /**
   * Called when the projectile's motion gets zeroed.
   */
  public abstract void onMotionStop(int stopsCount);

  public void shootFromEntity(Entity entity, float x, float y, float z, float force,
      float p_184538_6_) {
    float f = -MathHelper.sin(y * ((float) Math.PI / 180F))
        * MathHelper.cos(x * ((float) Math.PI / 180F));
    float f1 = -MathHelper.sin((x + z) * ((float) Math.PI / 180F));
    float f2 =
        MathHelper.cos(y * ((float) Math.PI / 180F)) * MathHelper.cos(x * ((float) Math.PI / 180F));
    this.shoot((double) f, (double) f1, (double) f2, force, p_184538_6_);
    Vector3d vec3d = entity.getDeltaMovement();
    this.setDeltaMovement(this.getDeltaMovement().add(vec3d.x, entity.isOnGround() ? 0.0D : vec3d.y, vec3d.z));
  }

  public void shoot(double x, double y, double z, float force, float p_70186_8_) {
    Vector3d vec = (new Vector3d(x, y, z)).normalize()
        .add(this.random.nextGaussian() * (double) 0.0075F * (double) p_70186_8_,
            this.random.nextGaussian() * (double) 0.0075F * (double) p_70186_8_,
            this.random.nextGaussian() * (double) 0.0075F * (double) p_70186_8_)
        .scale((double) force);
    this.setDeltaMovement(vec);
    float f = MathHelper.sqrt(getHorizontalDistanceSqr(vec));
    this.yRot = (float) (MathHelper.atan2(vec.x, vec.z) * (double) (180F / (float) Math.PI));
    this.xRot =
        (float) (MathHelper.atan2(vec.y, (double) f) * (double) (180F / (float) Math.PI));
    this.yRotO = this.yRot;
    this.xRotO = this.xRot;
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
  public boolean isStoppedInGround() {
    return this.stoppedInGround;
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
   * @return The bounce factor or <code>null</code> if the entity should not bounce.
   */
  public Float getBounceFactor(BlockRayTraceResult blockRayTraceResult) {
    return 0.375F;
  }

  @Override
  protected void addAdditionalSaveData(CompoundNBT compound) {
    if (this.ownerId != null) {
      compound.put("owner", NBTUtil.createUUID(this.ownerId));
    }

    compound.putBoolean("inGround", this.stoppedInGround);
    compound.putInt("motionStopCount", this.motionStopCount);
  }

  @Override
  protected void readAdditionalSaveData(CompoundNBT compound) {
    if (compound.contains("owner", 10)) {
      this.ownerId = NBTUtil.loadUUID(compound.getCompound("owner"));
    }
    this.stoppedInGround = compound.getBoolean("inGround");
    this.motionStopCount = compound.getInt("motionStopCount");
  }

  @Override
  public void writeSpawnData(PacketBuffer buffer) {
    buffer.writeBoolean(this.stoppedInGround);
    buffer.writeInt(this.totalTicksInAir);
    buffer.writeInt(this.motionStopCount);
  }

  @Override
  public void readSpawnData(PacketBuffer buffer) {
    this.stoppedInGround = buffer.readBoolean();
    this.totalTicksInAir = buffer.readInt();
    this.motionStopCount = buffer.readInt();
  }

  @Nullable
  public Optional<Entity> getThrower() {
    if (this.level instanceof ServerWorld) {
      return Optional.ofNullable(((ServerWorld) this.level).getEntity(this.ownerId));
    }
    return Optional.empty();
  }
}
