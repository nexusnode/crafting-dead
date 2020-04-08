package com.craftingdead.mod.type;

import java.util.function.Function;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.item.Rarity;
import net.minecraft.util.IStringSerializable;

public enum Vest implements IStringSerializable {
  NORM("normal", Rarity.COMMON, (inventory) -> (id, playerInventory, playerEntity) -> 
  new ChestContainer(ContainerType.GENERIC_9X2, id, playerInventory, inventory, 2), 18, 44, 24, 8, 82, 176, 164),
  QUIVER("quiver", Rarity.COMMON, (inventory) -> (id, playerInventory, playerEntity) -> 
  new ChestContainer(ContainerType.GENERIC_9X2, id, playerInventory, inventory, 2), 18, 44, 24, 8, 82, 176, 164),
  AMMO("ammo", Rarity.COMMON, (inventory) -> (id, playerInventory, playerEntity) -> 
  new ChestContainer(ContainerType.GENERIC_9X2, id, playerInventory, inventory, 2), 18, 44, 24, 8, 82, 176, 164);

  private final String name;
  private final Rarity rarity;
  private final Function<IInventory, IContainerProvider> containerFactory;
  private final int size;
  private final int slotVestX;
  private final int slotVestY;
  private final int slotPlayerX;
  private final int slotPlayerY;
  private final int textureSizeX;
  private final int textureSizeY;
  
  private Vest(String name, Rarity rarity,  Function<IInventory, IContainerProvider> containerFactory,
      int size, int slotVestX,  int slotVestY, int slotPlayerX, int slotPlayerY, int textureSizeX, int textureSizeY) {
    this.name = name;
    this.rarity = rarity;
    this.containerFactory = containerFactory;
    this.size = size;
    this.slotVestX = slotVestX;
    this.slotVestY = slotVestY;
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

  public int getSlotVestX() {
    return this.slotVestX;
  }

  public int getSlotVestY() {
    return this.slotVestY;
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
