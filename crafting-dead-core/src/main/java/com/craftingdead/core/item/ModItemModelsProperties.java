package com.craftingdead.core.item;

import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.inventory.InventorySlotType;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;

public class ModItemModelsProperties {

  public static void register() {
    ItemModelsProperties.registerGlobalProperty(new ResourceLocation("wearing"),
        (itemStack, world, entity) -> entity.getCapability(ModCapabilities.LIVING)
            .map(living -> living.getItemHandler()
                .getStackInSlot(InventorySlotType.HAT.getIndex()) == itemStack ? 1.0F : 0.0F)
            .orElse(0.0F));
  }
}
