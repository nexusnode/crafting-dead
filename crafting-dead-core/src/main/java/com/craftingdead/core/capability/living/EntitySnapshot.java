package com.craftingdead.core.capability.living;

import java.util.Optional;
import java.util.Random;
import com.craftingdead.core.util.RayTraceUtil;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntitySnapshot {

  private final long tick;
  private final Vec3d pos;
  private final AxisAlignedBB collisionBox;
  private final Vec2f pitchYaw;
  private final float eyeHeight;

  public EntitySnapshot(long tick, Vec3d pos, AxisAlignedBB collisionBox, Vec2f pitchYaw,
      float eyeHeight) {
    this.tick = tick;
    this.pos = pos;
    this.collisionBox = collisionBox;
    this.pitchYaw = pitchYaw;
    this.eyeHeight = eyeHeight;
  }

  public Optional<Vec3d> rayTrace(World world, EntitySnapshot entitySnapshot, double distance,
      float accuracy, Random random) {
    Vec3d start = entitySnapshot.getPos().add(0.0D, entitySnapshot.eyeHeight, 0.0D);
    Vec3d look = entitySnapshot.getVectorForRotation(entitySnapshot.getPitchYaw().x,
        entitySnapshot.getPitchYaw().y);

    look = look.add((1.0F - accuracy) / 7.5F * (random.nextBoolean() ? -1.0F : 1.0F),
        0, (1.0F - accuracy) / 7.5F * (random.nextBoolean() ? -1.0F : 1.0F));

    Optional<BlockRayTraceResult> blockRayTraceResult =
        RayTraceUtil.rayTraceBlocksPiercing(start, distance, look, world);

    Vec3d scaledLook = look.scale(distance);

    Vec3d end = blockRayTraceResult
        .map(RayTraceResult::getHitVec)
        .orElse(start.add(scaledLook));

    Optional<Vec3d> potentialHit = this.getCollisionBox().rayTrace(start, end);
    if (this.getCollisionBox().contains(start)) {
      return Optional.of(potentialHit.orElse(start));
    } else {
      return potentialHit;
    }
  }

  protected final Vec3d getVectorForRotation(float pitch, float yaw) {
    float f = pitch * ((float) Math.PI / 180F);
    float f1 = -yaw * ((float) Math.PI / 180F);
    float f2 = MathHelper.cos(f1);
    float f3 = MathHelper.sin(f1);
    float f4 = MathHelper.cos(f);
    float f5 = MathHelper.sin(f);
    return new Vec3d((double) (f3 * f4), (double) (-f5), (double) (f2 * f4));
  }

  public long getTick() {
    return this.tick;
  }

  public Vec3d getPos() {
    return this.pos;
  }

  public AxisAlignedBB getCollisionBox() {
    return this.collisionBox;
  }

  public Vec2f getPitchYaw() {
    return this.pitchYaw;
  }

  public float getEyeHeight() {
    return this.eyeHeight;
  }
}
