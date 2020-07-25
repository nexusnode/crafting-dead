package com.craftingdead.core.capability.paint;

import java.util.Optional;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

public class DefaultPaint implements IPaint {

  private final Optional<ResourceLocation> skin;
  private Optional<Integer> colour;

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

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
    this.colour.ifPresent(c -> nbt.putInt("colour", c));
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    this.colour = nbt.contains("colour", Constants.NBT.TAG_INT) ? Optional.of(nbt.getInt("colour"))
        : Optional.empty();
  }
}
