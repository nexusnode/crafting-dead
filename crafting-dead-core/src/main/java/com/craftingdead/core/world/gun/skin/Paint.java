package com.craftingdead.core.world.gun.skin;

import com.craftingdead.core.capability.Capabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;

public interface Paint {

  RegistryKey<Skin> getSkin();

  static boolean isValid(ItemStack gunStack, ItemStack itemStack) {
    return isValid(gunStack.getItem().getRegistryName(), itemStack);
  }

  static boolean isValid(ResourceLocation gunName, ItemStack itemStack) {
    return itemStack.getCapability(Capabilities.PAINT)
        .map(Paint::getSkin)
        .map(Skins.REGISTRY::get)
        .filter(skin -> skin.getAcceptedGuns().contains(gunName))
        .isPresent();
  }
}
