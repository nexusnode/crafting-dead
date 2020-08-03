package com.craftingdead.core.entity;

import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IProjectile;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public abstract class BounceableProjectileEntity extends Entity
    implements IProjectile, IEntityAdditionalSpawnData {

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
    this.setPosition(x, y, z);
  }

  public BounceableProjectileEntity(EntityType<? extends BounceableProjectileEntity> entityType,
      LivingEntity throwerEntity, World world) {
    this(entityType, throwerEntity.getPosX(), throwerEntity.getPosYEye(), throwerEntity.getPosZ(),
        world);
    this.ownerId = throwerEntity.getUniqueID();
  }

  @Override
  public void tick() {
    super.tick();

    BlockPos currentBlockPos = new BlockPos(this);
    BlockState currentBlockState = this.world.getBlockState(currentBlockPos);

    if (this.stoppedInGround) {
      // Places the current inBlockState if it is not present
      if (this.blockStanding == null) {
        this.blockStanding = currentBlockState;
      }

      boolean notCollided = (this.blockStanding != currentBlockState
          && this.world.hasNoCollisions(this.getBoundingBox().grow(0.0625D)));
      boolean shouldMove = !this.getMotion().equals(Vec3d.ZERO) || notCollided;

      if (shouldMove) {
        this.stoppedInGround = false;
        this.blockStanding = null;
      }
    }

    if (!this.stoppedInGround) {
      // Natural loss of speed
      this.setMotion(this.getMotion().scale(0.98D));

      // Gravity speed
      if (!this.hasNoGravity()) {
        this.setMotion(this.getMotion().subtract(0, 0.04D, 0));
      }

      this.totalTicksInAir++;
      Vec3d position = this.getPositionVec();
      Vec3d motionBeforeHit = this.getMotion();

      BlockRayTraceResult blockRayTraceResult =
          this.world.rayTraceBlocks(new RayTraceContext(position, position.add(motionBeforeHit),
              RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));

      Vec3d nextBounceMotion = null;

      if (blockRayTraceResult.getType() != RayTraceResult.Type.MISS) {
        BlockState blockHitState = this.world.getBlockState(blockRayTraceResult.getPos());

        Vec3d difference =
            blockRayTraceResult.getHitVec().subtract(this.getPosX(), this.getPosY(),
                this.getPosZ());
        this.setMotion(difference);

        // From vanilla's AbstractArrowEntity logic.
        // I didn't found a good name for the variable.
        Vec3d vec = difference.normalize().scale(0.05D);
        this.setPosition(this.getPosX() - vec.x, this.getPosY() - vec.y, this.getPosZ() - vec.z);

        Float bounceFactor = this.getBounceFactor(blockRayTraceResult);
        if (bounceFactor != null) {
          switch (blockRayTraceResult.getFace()) {
            case UP:
              // If the grenade is going up too slowly, set the next bounce to zero
              nextBounceMotion = Math.abs(motionBeforeHit.y) > 0.1D
                  ? motionBeforeHit.mul(0.9D * 0.7D, (-bounceFactor), 0.9D * 0.7D)
                  : Vec3d.ZERO;
              break;
            case DOWN:
              nextBounceMotion = motionBeforeHit.mul(0.9D, -bounceFactor, 0.9D);
              break;
            case WEST:
            case EAST:
              nextBounceMotion = motionBeforeHit.mul(-bounceFactor, 0.9D, 0.9D);
              break;
            case SOUTH:
            case NORTH:
              nextBounceMotion = motionBeforeHit.mul(0.9D, 0.9D, -bounceFactor);
              break;
            default:
              break;
          }
        } else {
          nextBounceMotion = Vec3d.ZERO;
        }

        this.onSurfaceHit(blockRayTraceResult);

        // Check if the entity is still alive after hitting the surface
        if (this.isAlive()) {
          if (nextBounceMotion != null && blockRayTraceResult.getFace() == Direction.UP) {
            // Stops if the next bounce is too slow
            if (nextBounceMotion.length() < 0.1D) {
              nextBounceMotion = Vec3d.ZERO;
              this.stoppedInGround = true;
              this.blockStanding = blockHitState;
              this.onMotionStop(++this.motionStopCount);
            }
          }
        }
      }

      Vec3d currentMotion = this.getMotion();

      double nextX = this.getPosX() + currentMotion.x;
      double nextY = this.getPosY() + currentMotion.y;
      double nextZ = this.getPosZ() + currentMotion.z;

      // Use .setPosition instead of .move() for a better collision detection.
      // Otherwise the grenade will bounce imprecisely.
      this.setPosition(nextX, nextY, nextZ);
      this.doBlockCollisions();

      if (nextBounceMotion != null) {
        this.setMotion(nextBounceMotion);
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
    Vec3d vec3d = entity.getMotion();
    this.setMotion(this.getMotion().add(vec3d.x, entity.onGround ? 0.0D : vec3d.y, vec3d.z));
  }

  @Override
  public void shoot(double x, double y, double z, float force, float p_70186_8_) {
    Vec3d vec = (new Vec3d(x, y, z)).normalize()
        .add(this.rand.nextGaussian() * (double) 0.0075F * (double) p_70186_8_,
            this.rand.nextGaussian() * (double) 0.0075F * (double) p_70186_8_,
            this.rand.nextGaussian() * (double) 0.0075F * (double) p_70186_8_)
        .scale((double) force);
    this.setMotion(vec);
    float f = MathHelper.sqrt(horizontalMag(vec));
    this.rotationYaw = (float) (MathHelper.atan2(vec.x, vec.z) * (double) (180F / (float) Math.PI));
    this.rotationPitch =
        (float) (MathHelper.atan2(vec.y, (double) f) * (double) (180F / (float) Math.PI));
    this.prevRotationYaw = this.rotationYaw;
    this.prevRotationPitch = this.rotationPitch;
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
  protected void writeAdditional(CompoundNBT compound) {
    if (this.ownerId != null) {
      compound.put("owner", NBTUtil.writeUniqueId(this.ownerId));
    }

    compound.putBoolean("inGround", this.stoppedInGround);
    compound.putInt("motionStopCount", this.motionStopCount);
  }

  @Override
  protected void readAdditional(CompoundNBT compound) {
    if (compound.contains("owner", 10)) {
      this.ownerId = NBTUtil.readUniqueId(compound.getCompound("owner"));
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
    if (this.world instanceof ServerWorld) {
      return Optional.ofNullable(((ServerWorld) this.world).getEntityByUuid(this.ownerId));
    }
    return Optional.empty();
  }
}
