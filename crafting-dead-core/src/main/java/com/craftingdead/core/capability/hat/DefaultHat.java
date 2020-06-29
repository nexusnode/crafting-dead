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
