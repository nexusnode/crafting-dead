package com.craftingdead.mod.capability.paint;

import java.util.Optional;
import net.minecraft.util.ResourceLocation;

public interface IPaint {

  Optional<ResourceLocation> getSkin();

  Optional<Integer> getColour();
}
