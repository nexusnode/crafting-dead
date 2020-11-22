package com.craftingdead.core.capability.gun;

import com.craftingdead.core.capability.living.EntitySnapshot;

public class PendingHit {

  private final byte tickOffset;
  private final EntitySnapshot playerSnapshot;
  private final EntitySnapshot hitSnapshot;
  private final long randomSeed;

  public PendingHit(byte tickOffset, EntitySnapshot playerSnapshot, EntitySnapshot hitSnapshot,
      long randomSeed) {
    this.tickOffset = tickOffset;
    this.playerSnapshot = playerSnapshot;
    this.hitSnapshot = hitSnapshot;
    this.randomSeed = randomSeed;
  }

  public byte getTickOffset() {
    return this.tickOffset;
  }

  public EntitySnapshot getPlayerSnapshot() {
    return this.playerSnapshot;
  }

  public EntitySnapshot getHitSnapshot() {
    return this.hitSnapshot;
  }

  public long getRandomSeed() {
    return this.randomSeed;
  }
}
