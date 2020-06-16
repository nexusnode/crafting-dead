package com.craftingdead.core.entity.ai;

import java.util.EnumSet;
import java.util.List;
import com.craftingdead.core.potion.ModEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;

public class LookAtEntityGoal<T extends Entity> extends Goal {

  private final MobEntity goalOwner;
  private final float maxDistance;
  private final float chance;
  private final Class<T> watchedClass;
  private int lookDuration;
  private Entity watchedEntity;

  public LookAtEntityGoal(MobEntity goalowner, Class<T> watchedClass, float maxDistance) {
    this(goalowner, watchedClass, maxDistance, 0.02F);
  }

  public LookAtEntityGoal(MobEntity goalOwner, Class<T> watchedClass, float maxDistance,
      float chance) {
    this.goalOwner = goalOwner;
    this.watchedClass = watchedClass;
    this.maxDistance = maxDistance;
    this.chance = chance;
    this.setMutexFlags(EnumSet.of(Goal.Flag.LOOK));
  }

  @Override
  public boolean shouldExecute() {
    if (this.goalOwner.isPotionActive(ModEffects.FLASH_BLINDNESS.get())) {
      return false;
    }

    if (this.goalOwner.getRNG().nextFloat() >= this.chance) {
      return false;
    }

    List<Entity> possibleEntities =
        this.goalOwner.world.getEntitiesWithinAABB(this.watchedClass, this.goalOwner
            .getBoundingBox().grow((double) this.maxDistance, 4.0D, (double) this.maxDistance));

    double lastSqDistance = Double.MAX_VALUE;

    for (Entity grenade : possibleEntities) {
      double sqDistance = this.goalOwner.getDistanceSq(grenade);
      if (sqDistance <= lastSqDistance) {
        lastSqDistance = sqDistance;
        this.watchedEntity = grenade;
      }
    }

    return this.watchedEntity != null;
  }

  @Override
  public boolean shouldContinueExecuting() {
    if (!this.watchedEntity.isAlive()) {
      return false;
    }

    if (this.goalOwner.isPotionActive(ModEffects.FLASH_BLINDNESS.get())) {
      return false;
    }

    if (this.goalOwner
        .getDistanceSq(this.watchedEntity) > (double) (this.maxDistance * this.maxDistance)) {
      return false;
    }

    return this.lookDuration > 0;
  }

  @Override
  public void startExecuting() {
    this.lookDuration = 40 + this.goalOwner.getRNG().nextInt(40);
  }

  @Override
  public void resetTask() {
    this.watchedEntity = null;
  }

  @Override
  public void tick() {
    this.goalOwner.getLookController().setLookPosition(this.watchedEntity.getX(),
        this.watchedEntity.getEyeY(), this.watchedEntity.getZ());
    --this.lookDuration;
  }
}
