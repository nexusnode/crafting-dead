package com.craftingdead.mod.capability.paint;

import java.util.Optional;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

public interface IPaint extends INBTSerializable<CompoundNBT> {

  Optional<ResourceLocation> getSkin();

  void setSkin(ResourceLocation skin);

  Optional<Integer> getColour();

  void setColour(Integer colour);

  @Override
  default CompoundNBT serializeNBT() {
    CompoundNBT compound = new CompoundNBT();
    this.getSkin().ifPresent(skin -> compound.putString("skin", skin.toString()));
    this.getColour().ifPresent(colour -> compound.putInt("colour", colour));
    return compound;
  }

  @Override
  default void deserializeNBT(CompoundNBT nbt) {
    if (nbt.contains("skin")) {
      this.setSkin(new ResourceLocation(nbt.getString("skin")));
    }
    if (nbt.contains("colour")) {
      this.setColour(nbt.getInt("colour"));
    }
  }
}
