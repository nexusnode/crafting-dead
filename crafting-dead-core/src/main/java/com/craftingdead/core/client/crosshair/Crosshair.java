package com.craftingdead.core.client.crosshair;

import net.minecraft.util.ResourceLocation;

public class Crosshair {

  private final ResourceLocation name;

  private final boolean isStatic;

  private final ResourceLocation top;
  private final ResourceLocation bottom;
  private final ResourceLocation left;
  private final ResourceLocation right;
  private final ResourceLocation middle;

  public Crosshair(ResourceLocation name, boolean isStatic, ResourceLocation top,
      ResourceLocation bottom, ResourceLocation left, ResourceLocation right,
      ResourceLocation middle) {
    this.name = name;
    this.isStatic = isStatic;
    this.top = top;
    this.bottom = bottom;
    this.left = left;
    this.right = right;
    this.middle = middle;
  }

  public ResourceLocation getName() {
    return name;
  }

  public boolean isStatic() {
    return isStatic;
  }

  public ResourceLocation getTop() {
    return top;
  }

  public ResourceLocation getBottom() {
    return bottom;
  }

  public ResourceLocation getLeft() {
    return left;
  }

  public ResourceLocation getRight() {
    return right;
  }

  public ResourceLocation getMiddle() {
    return middle;
  }
}
