package com.craftingdead.core.world.gun.skin;

import net.minecraft.util.RegistryKey;

public class SimplePaint implements Paint {

  private final RegistryKey<Skin> skin;

  public SimplePaint(RegistryKey<Skin> skin) {
    this.skin = skin;
  }

  @Override
  public RegistryKey<Skin> getSkin() {
    return this.skin;
  }
}
