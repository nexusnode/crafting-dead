package com.craftingdead.mod.item;

public interface IFireMode {

  void tick(boolean triggerPressed);

  boolean canShoot();
}
