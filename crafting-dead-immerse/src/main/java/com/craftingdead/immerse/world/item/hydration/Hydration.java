package com.craftingdead.immerse.world.item.hydration;

import com.craftingdead.immerse.CraftingDeadImmerse;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

@FunctionalInterface
public interface Hydration {

  Capability<Hydration> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

  /**
   * @see {@link net.minecraftforge.event.AttachCapabilitiesEvent}
   */
  ResourceLocation CAPABILITY_KEY = new ResourceLocation(CraftingDeadImmerse.ID, "hydration");

  Hydration POTION = itemStack -> PotionUtils.getPotion(itemStack) == Potions.WATER ? 5 : 0;

  int getWater(ItemStack itemStack);

  static Hydration fixed(int water) {
    return __ -> water;
  }
}
