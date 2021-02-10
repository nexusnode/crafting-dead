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

package com.craftingdead.core.capability.hat;

public class DefaultHat implements IHat {

  private final boolean nightVision;
  private final float headshotReductionPercentage;
  private final boolean immuneToFlashes;
  private final boolean immuneToGas;

  public DefaultHat() {
    this(false, 0.0F, false, false);
  }

  public DefaultHat(boolean nightVision, float headshotReductionPercentage, boolean immuneToFlashes,
      boolean immuneToGas) {
    this.nightVision = nightVision;
    this.headshotReductionPercentage = headshotReductionPercentage;
    this.immuneToFlashes = immuneToFlashes;
    this.immuneToGas = immuneToGas;
  }

  @Override
  public boolean hasNightVision() {
    return this.nightVision;
  }

  @Override
  public float getHeadshotReductionPercentage() {
    return this.headshotReductionPercentage;
  }

  @Override
  public boolean isImmuneToFlashes() {
    return this.immuneToFlashes;
  }

  @Override
  public boolean isImmuneToGas() {
    return this.immuneToGas;
  }
}
