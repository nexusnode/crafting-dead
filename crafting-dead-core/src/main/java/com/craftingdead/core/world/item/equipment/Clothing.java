package com.craftingdead.core.world.item.equipment;

import net.minecraft.resources.ResourceLocation;

public interface Clothing extends Equipment {

  ResourceLocation getTexture(String skinType);

  default boolean fireImmunity() {
    return false;
  }

  default boolean enhancesSwimming() {
    return false;
  }

  @Override
  default boolean isValidForSlot(Slot slot) {
    return slot == Slot.CLOTHING;
  }
}
