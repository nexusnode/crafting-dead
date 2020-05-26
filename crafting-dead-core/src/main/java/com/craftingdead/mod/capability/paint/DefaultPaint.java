package com.craftingdead.mod.capability.paint;

import java.util.Optional;
import net.minecraft.util.ResourceLocation;

public class DefaultPaint implements IPaint {

  private Optional<ResourceLocation> skin;
  private Optional<Integer> colour;

  public DefaultPaint() {
    this(null, null);
  }

  public DefaultPaint(ResourceLocation skin, Integer colour) {
    this.setSkin(skin);
    this.setColour(colour);
  }

  @Override
  public Optional<ResourceLocation> getSkin() {
    return this.skin;
  }

  @Override
  public void setSkin(ResourceLocation skin) {
    this.skin = Optional.ofNullable(skin);
  }

  @Override
  public Optional<Integer> getColour() {
    return this.colour;
  }

  @Override
  public void setColour(Integer colour) {
    this.colour = Optional.ofNullable(colour);
  }
}
