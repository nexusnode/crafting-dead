package com.craftingdead.mod.capability.paint;

import java.util.Optional;
import com.craftingdead.mod.item.Color;
import net.minecraft.nbt.CompoundNBT;

public class DefaultPaintColor implements IPaintColor {

  @Override
  public CompoundNBT serializeNBT() {
    return null;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {}

  @Override
  public Optional<Color> getColor() {
    return Optional.empty();
  }

  @Override
  public void setColor(Optional<Color> color) {}

}
