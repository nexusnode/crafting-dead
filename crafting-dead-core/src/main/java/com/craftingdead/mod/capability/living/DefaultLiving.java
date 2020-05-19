package com.craftingdead.mod.capability.living;

import java.util.Collection;
import java.util.UUID;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.inventory.InventorySlotType;
import com.craftingdead.mod.network.NetworkChannel;
import com.craftingdead.mod.network.message.main.SyncInventoryMessage;
import com.craftingdead.mod.network.message.main.ToggleAimingMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.world.GameRules;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.PacketDistributor.PacketTarget;

public class DefaultLiving<E extends LivingEntity> implements ILiving<E> {

  /**
   * The vanilla entity.
   */
  protected final E entity;

  protected final Inventory inventory = new Inventory(InventorySlotType.values().length);

  /**
   * The last held {@link ItemStack} - used to check if the entity has switched item.
   */
  protected ItemStack lastHeldStack = null;

  private boolean aiming;

  private boolean inventoryDirty = true;

  public DefaultLiving() {
    this(null);
  }

  public DefaultLiving(E entity) {
    this.entity = entity;
    this.inventory.addListener(inventory -> this.inventoryDirty = true);
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
    NonNullList<ItemStack> items =
        NonNullList.withSize(this.inventory.getSizeInventory(), ItemStack.EMPTY);
    for (int i = 0; i < this.inventory.getSizeInventory(); i++) {
      items.set(i, this.inventory.getStackInSlot(i));
    }
    nbt.put("inventory", ItemStackHelper.saveAllItems(nbt.getCompound("inventory"), items));
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    NonNullList<ItemStack> items =
        NonNullList.withSize(this.inventory.getSizeInventory(), ItemStack.EMPTY);
    ItemStackHelper.loadAllItems(nbt.getCompound("inventory"), items);
    for (int i = 0; i < items.size(); i++) {
      this.inventory.setInventorySlotContents(i, items.get(i));
    }
  }

  @Override
  public void tick() {
    ItemStack heldStack = this.entity.getHeldItemMainhand();
    if (heldStack != this.lastHeldStack) {
      heldStack
          .getCapability(ModCapabilities.GUN)
          .ifPresent(gunController -> gunController.cancelActions(this.entity, heldStack));
      this.lastHeldStack = heldStack;
    }

    heldStack
        .getCapability(ModCapabilities.GUN)
        .ifPresent(gunController -> gunController.tick(this.entity, heldStack));

    if (!this.entity.getEntityWorld().isRemote() && this.inventoryDirty) {
      NonNullList<ItemStack> inventoryContents =
          NonNullList.withSize(this.inventory.getSizeInventory(), ItemStack.EMPTY);
      for (int i = 0; i < this.inventory.getSizeInventory(); i++) {
        inventoryContents.set(i, this.inventory.getStackInSlot(i));
      }
      NetworkChannel.MAIN
          .getSimpleChannel()
          .send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(this::getEntity),
              new SyncInventoryMessage(this.entity.getEntityId(), inventoryContents));
      this.inventoryDirty = false;
    }
  }

  @Override
  public float onDamaged(DamageSource source, float amount) {
    return amount;
  }

  @Override
  public boolean onAttacked(DamageSource source, float amount) {
    return false;
  }

  @Override
  public boolean onKill(Entity target) {
    return false;
  }

  @Override
  public boolean onDeath(DamageSource cause) {
    return false;
  }

  @Override
  public boolean onDeathDrops(DamageSource cause, Collection<ItemEntity> drops) {
    boolean shouldKeepInventory = (this.entity instanceof PlayerEntity
        && this.entity.world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY));
    if (!shouldKeepInventory) {
      // Adds items from CD inventory
      for (int i = 0; i < this.inventory.getSizeInventory(); i++) {
        ItemEntity itemEntity = new ItemEntity(entity.world, entity.getX(), entity.getY(),
            entity.getZ(), this.inventory.getStackInSlot(i));
        itemEntity.setDefaultPickupDelay();

        drops.add(itemEntity);
      }

      // Clears CD inventory
      this.inventory.clear();
    }
    return false;
  }

  @Override
  public void toggleAiming(boolean sendUpdate) {
    this.aiming = !this.aiming;
    if (sendUpdate) {
      PacketTarget target =
          this.entity.getEntityWorld().isRemote() ? PacketDistributor.SERVER.noArg()
              : PacketDistributor.TRACKING_ENTITY.with(this::getEntity);
      NetworkChannel.MAIN
          .getSimpleChannel()
          .send(target, new ToggleAimingMessage(this.entity.getEntityId()));
    }
  }

  @Override
  public boolean isAiming() {
    return this.aiming;
  }

  @Override
  public IInventory getInventory() {
    return this.inventory;
  }

  @Override
  public E getEntity() {
    return this.entity;
  }

  @Override
  public UUID getId() {
    return this.entity != null ? this.entity.getUniqueID() : null;
  }
}
