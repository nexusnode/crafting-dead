package com.craftingdead.mod.item;

import java.util.EnumMap;
import java.util.Map;
import com.craftingdead.mod.inventory.CraftingInventorySlotType;
import net.minecraft.item.Item;

public class AttachmentItem extends Item {

  private final Map<MultiplierType, Float> multipliers;
  private final CraftingInventorySlotType inventorySlot;

  public AttachmentItem(Properties properties) {
    super(properties);
    this.multipliers = properties.multipliers;
    this.inventorySlot = properties.inventorySlot;
  }

  public Float getMultiplier(MultiplierType multiplierType) {
    return this.multipliers.getOrDefault(multiplierType, 1.0F);
  }

  public CraftingInventorySlotType getInventorySlot() {
    return this.inventorySlot;
  }

  public static enum MultiplierType {
    DAMAGE, ACCURACY, FOV;
  }

  public static class Properties extends Item.Properties {

    private final Map<MultiplierType, Float> multipliers = new EnumMap<>(MultiplierType.class);
    private CraftingInventorySlotType inventorySlot;

    public Properties addMultiplier(MultiplierType modifierType, float multiplier) {
      this.multipliers.put(modifierType, multiplier);
      return this;
    }

    public Properties setInventorySlot(CraftingInventorySlotType inventorySlot) {
      this.inventorySlot = inventorySlot;
      return this;
    }
  }
}
