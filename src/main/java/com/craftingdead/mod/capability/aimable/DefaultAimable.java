package com.craftingdead.mod.capability.aimable;

public class DefaultAimable implements IAimable {

  @Override
  public float getAccuracy() {
    return 0;
  }

  @Override
  public float getCameraZoom() {
    return 1.0F;
  }
}
