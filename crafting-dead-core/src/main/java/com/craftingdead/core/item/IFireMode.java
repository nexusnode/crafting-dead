package com.craftingdead.core.item;

public interface IFireMode {

  void tick(boolean triggerPressed);

  boolean canShoot();
}
