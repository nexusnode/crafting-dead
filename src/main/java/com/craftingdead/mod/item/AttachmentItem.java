package com.craftingdead.mod.item;

import net.minecraft.item.Item;

public class AttachmentItem extends Item {

  private final float accuracy;
  private final float damage;
  private final float cameraZoom;
  private final Type type;

  public AttachmentItem(Properties properties) {
    super(properties);
    this.accuracy = properties.accuracy;
    this.damage = properties.damage;
    this.cameraZoom = properties.cameraZoom;
    this.type = properties.type;
  }

  public float getAccuracy() {
    return this.accuracy;
  }

  public float getDamage() {
    return this.damage;
  }

  public float getCameraZoom() {
    return this.cameraZoom;
  }

  public Type getType() {
    return this.type;
  }

  public static enum Type {
    SIGHT, UNDERBARREL, SUPPRESSOR, LASER;
  }

  public static class Properties extends Item.Properties {

    private float accuracy;
    private float damage;
    private float cameraZoom = 1.0F;
    private Type type;

    public void setAccuracy(float accuracy) {
      this.accuracy = accuracy;
    }

    public void setDamage(float damage) {
      this.damage = damage;
    }

    public void setCameraZoom(float cameraZoom) {
      this.cameraZoom = cameraZoom;
    }

    public void setType(Type type) {
      this.type = type;
    }
  }
}
