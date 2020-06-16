package com.craftingdead.core.capability.paint;

import java.util.Optional;
import net.minecraft.util.ResourceLocation;

public class DefaultPaint implements IPaint {

  private final Optional<ResourceLocation> skin;
  private final Optional<Integer> colour;

  public DefaultPaint() {
    this(null, null);
  }

  public DefaultPaint(ResourceLocation skin, Integer colour) {
    this.skin = Optional.ofNullable(skin);
    this.colour = Optional.ofNullable(colour);
  }

  @Override
  public Optional<ResourceLocation> getSkin() {
    return this.skin;
  }

  @Override
  public Optional<Integer> getColour() {
    return this.colour;
  }
}
