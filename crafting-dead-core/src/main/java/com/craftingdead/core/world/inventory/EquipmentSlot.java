package com.craftingdead.core.world.inventory;

import javax.annotation.Nonnull;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.item.equipment.Equipment;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class EquipmentSlot extends Slot {

  private static Container emptyInventory = new SimpleContainer(0);
  private final LivingExtension<?, ?> living;
  private final Equipment.Slot slot;

  public EquipmentSlot(LivingExtension<?, ?> living, Equipment.Slot slot, int xPosition,
      int yPosition) {
    super(emptyInventory, slot.getIndex(), xPosition, yPosition);
    this.living = living;
    this.slot = slot;
  }

  @Override
  public boolean mayPlace(@Nonnull ItemStack stack) {
    return !stack.isEmpty() && stack.getCapability(Equipment.CAPABILITY)
        .lazyMap(equipment -> equipment.isValidForSlot(this.slot))
        .orElse(false);
  }

  @Override
  @Nonnull
  public ItemStack getItem() {
    return this.living.getItemInSlot(this.slot);
  }

  @Override
  public void set(@Nonnull ItemStack stack) {
    this.living.setItemInSlot(this.slot, stack);
    this.setChanged();
  }

  @Override
  public void onQuickCraft(@Nonnull ItemStack oldStackIn, @Nonnull ItemStack newStackIn) {}

  @Override
  public int getMaxStackSize() {
    return 1;
  }

  @Override
  public int getMaxStackSize(@Nonnull ItemStack stack) {
    return 1;
  }

  @Override
  public boolean mayPickup(Player playerIn) {
    return !this.living.getItemInSlot(this.slot).isEmpty();
  }

  @Override
  @Nonnull
  public ItemStack remove(int amount) {
    return amount > 0 ? this.living.setItemInSlot(this.slot, ItemStack.EMPTY) : ItemStack.EMPTY;
  }
}
