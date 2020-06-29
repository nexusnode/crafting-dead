package com.craftingdead.core.capability.clothing;

import java.util.Optional;
import net.minecraft.util.ResourceLocation;

public interface IClothing {

  int getArmorLevel();

  Optional<Integer> getSlownessAmplifier();

  boolean hasFireImmunity();

  ResourceLocation getTexture(String skinType);
}
