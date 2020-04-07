package com.craftingdead.mod.item;

import net.minecraft.item.Item;

public class MagazineItem extends Item {

  private final int size;

  /**
   * The amount in % of how much this item will ignore the target's armor
   */
  private final double armorPenetrationPercentage;

  /**
   * The chance in % to drop the ammo at the hit location
   */
  private final double groundHitDropChance;

  /**
   * The chance in % to drop the ammo at the entity hit
   */
  private final double entityHitDropChance;

  public MagazineItem(Properties properties) {
    super(properties);
    this.size = properties.size;
    this.armorPenetrationPercentage = properties.armorPenetrationPercentage;
    this.groundHitDropChance = properties.groundHitDropChance;
    this.entityHitDropChance = properties.entityHitDropChance;
  }

  public int getSize() {
    return this.size;
  }

  public double getArmorPenetrationPercentage() {
    return armorPenetrationPercentage;
  }

  public double getGroundHitDropChance() {
    return groundHitDropChance;
  }

  public double getEntityHitDropChance() {
    return entityHitDropChance;
  }

  public static class Properties extends Item.Properties {

    private int size;

    private double armorPenetrationPercentage;

    private int groundHitDropChance;

    private int entityHitDropChance;

    public Properties setSize(int magazineSize) {
      this.size = magazineSize;
      return this;
    }

    public Properties setGroundHitDropChance(int percentage) {
      this.groundHitDropChance = percentage;
      return this;
    }

    public Properties setEntityHitDropChance(int percentage) {
      this.entityHitDropChance = percentage;
      return this;
    }

    public Properties setArmorPenetration(double percentage) {
      this.armorPenetrationPercentage = percentage;
      return this;
    }
  }
}
