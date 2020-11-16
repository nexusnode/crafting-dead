package com.craftingdead.core.capability.gun;

import net.minecraft.util.math.Vec3d;

public class Hit {
  private long gameTime;
  private int entityId;
  private Vec3d hitVec;

  public Hit(long gameTime, int entityId, Vec3d hitVec) {
    this.gameTime = gameTime;
    this.entityId = entityId;
    this.hitVec = hitVec;
    
  }

  public long getGameTime() {
    return gameTime;
  }

  public int getEntityId() {
    return entityId;
  }

  public Vec3d getHitVec() {
    return hitVec;
  }
}
