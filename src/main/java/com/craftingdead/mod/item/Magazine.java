package com.craftingdead.mod.item;

public class Magazine {

  private int size;
  private int penetration;

  public Magazine(Magazine ammo) {
    this.size = ammo.size;
    this.penetration = ammo.penetration;
  }

  public Magazine(int size, int penetration) {
    this.size = size;
    this.penetration = penetration;
  }

  public int getSize() {
    return size;
  }

  public int getSizeAndDecrement() {
    return this.size--;
  }

  public int getPenetration() {
    return penetration;
  }
}
