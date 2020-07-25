package com.craftingdead.core.capability.paint;

import java.util.Optional;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

public interface IPaint extends INBTSerializable<CompoundNBT> {

  Optional<ResourceLocation> getSkin();

  Optional<Integer> getColour();
}
