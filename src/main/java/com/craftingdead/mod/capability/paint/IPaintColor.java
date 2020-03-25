package com.craftingdead.mod.capability.paint;

import java.util.Optional;
import com.craftingdead.mod.item.Color;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IPaintColor extends INBTSerializable<CompoundNBT> {

  Optional<Color> getColor();

  void setColor(Optional<Color> color);
}
