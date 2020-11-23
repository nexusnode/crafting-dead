/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
