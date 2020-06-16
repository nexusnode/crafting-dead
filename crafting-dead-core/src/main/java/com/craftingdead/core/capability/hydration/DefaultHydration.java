package com.craftingdead.core.capability.hydration;

public class DefaultHydration implements IHydration {

  private final int hydration;

  public DefaultHydration() {
    this(0);
  }

  public DefaultHydration(int hydration) {
    this.hydration = hydration;
  }

  @Override
  public int getHydration() {
    return this.hydration;
  }
}
