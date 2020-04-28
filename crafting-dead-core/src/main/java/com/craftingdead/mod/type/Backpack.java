package com.craftingdead.mod.type;

import java.util.function.Function;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.item.Rarity;
import net.minecraft.util.IStringSerializable;

public enum Backpack implements IStringSerializable {
  SMALL("small", Rarity.COMMON,
      (inventory) -> (id, playerInventory, playerEntity) -> new ChestContainer(
          ContainerType.GENERIC_9X2, id, playerInventory, inventory, 2),
      18, 44, 24, 8, 82, 176, 164), MEDIUM("medium", Rarity.UNCOMMON,
          (inventory) -> (id, playerInventory, playerEntity) -> new ChestContainer(
              ContainerType.GENERIC_9X4, id, playerInventory, inventory, 4),
          36, 8, 24, 8, 136, 176, 218), LARGE("large", Rarity.RARE,
              (inventory) -> (id, playerInventory, playerEntity) -> new ChestContainer(
                  ContainerType.GENERIC_9X6, id, playerInventory, inventory, 6),
              54, 8, 24, 44, 190, 248, 272);

  private final String name;
  private final Rarity rarity;
  private final Function<IInventory, IContainerProvider> containerFactory;
  private final int size;
  private final int slotBackpackX;
  private final int slotBackpackY;
  private final int slotPlayerX;
  private final int slotPlayerY;
  private final int textureSizeX;
  private final int textureSizeY;

  private Backpack(String name, Rarity rarity,
      Function<IInventory, IContainerProvider> containerFactory, int size, int slotBackpackX,
      int slotBackpackY, int slotPlayerX, int slotPlayerY, int textureSizeX, int textureSizeY) {
    this.name = name;
    this.rarity = rarity;
    this.containerFactory = containerFactory;
    this.size = size;
    this.slotBackpackX = slotBackpackX;
    this.slotBackpackY = slotBackpackY;
    this.slotPlayerX = slotPlayerX;
    this.slotPlayerY = slotPlayerY;
    this.textureSizeX = textureSizeX;
    this.textureSizeY = textureSizeY;
  }

  public Rarity getRarity() {
    return this.rarity;
  }

  public IContainerProvider getContainerProvider(IInventory inventory) {
    return this.containerFactory.apply(inventory);
  }

  public int getInventorySize() {
    return this.size;
  }

  public int getSlotBackpackX() {
    return this.slotBackpackX;
  }

  public int getSlotBackpackY() {
    return this.slotBackpackY;
  }

  public int getSlotPlayerX() {
    return this.slotPlayerX;
  }

  public int getSlotPlayerY() {
    return this.slotPlayerY;
  }

  public int getTextureSizeX() {
    return this.textureSizeX;
  }

  public int getTextureSizeY() {
    return this.textureSizeY;
  }

  @Override
  public String getName() {
    return this.name;
  }
}
