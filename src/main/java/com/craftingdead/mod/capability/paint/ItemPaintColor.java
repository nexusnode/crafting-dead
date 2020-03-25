package com.craftingdead.mod.capability.paint;

import java.util.Optional;
import com.craftingdead.mod.item.Color;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.NonNullSupplier;

public class ItemPaintColor implements IPaintColor {

  private Optional<Color> color;

  public ItemPaintColor(Optional<NonNullSupplier<Color>> colorGenerator) {
    this.color = colorGenerator.map(NonNullSupplier::get);
  }

  @Override
  public Optional<Color> getColor() {
    return this.color;
  }

  @Override
  public void setColor(Optional<Color> color) {
    this.color = color;
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT compound = new CompoundNBT();
    this.color.ifPresent(color -> color.write(compound));
    return compound;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    this.color = Color.fromCompound(nbt);
  }

}
