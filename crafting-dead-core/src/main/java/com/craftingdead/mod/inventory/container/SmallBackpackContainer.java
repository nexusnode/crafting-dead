package com.craftingdead.mod.inventory.container;

import java.util.function.BiPredicate;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class SmallBackpackContainer extends GenericContainer {

  public SmallBackpackContainer(ContainerType<?> containerType, int windowId,
      PlayerInventory playerInventory, int rows,
      BiPredicate<PredicateItemHandlerSlot, ItemStack> predicate) {
    super(containerType, windowId, playerInventory, rows, predicate);
  }


  public SmallBackpackContainer(ContainerType<?> containerType, int windowId,
      PlayerInventory playerInventory, IItemHandler itemHandler, int rows,
      BiPredicate<PredicateItemHandlerSlot, ItemStack> predicate) {
    super(containerType, windowId, playerInventory, itemHandler, rows, predicate);
  }
}
