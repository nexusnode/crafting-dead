package com.craftingdead.mod.capability.living;

import java.util.Collection;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.INBTSerializable;

public interface ILiving<E extends LivingEntity> extends INBTSerializable<CompoundNBT> {

  void tick();

  /**
   * When this entity is damaged; with potions and armour taken into account.
   *
   * @param source - the source of damage
   * @param amount - the amount of damage taken
   * @return the new damage amount
   */
  float onDamaged(DamageSource source, float amount);

  /**
   * When this entity is attacked.
   *
   * @param source - the source of damage
   * @param amount - the amount of damage taken
   * @return if the event should be cancelled
   */
  boolean onAttacked(DamageSource source, float amount);

  /**
   * When this entity kills another entity.
   *
   * @param target - the {@link Entity} killed
   * @return if the event should be cancelled
   */
  boolean onKill(Entity target);

  /**
   * When this entity's health reaches 0.
   *
   * @param cause - the cause of death
   * @return if the event should be cancelled
   */
  boolean onDeath(DamageSource cause);

  /**
   * When this entity's death causes dropped items to appear.
   *
   * @param cause - the DamageSource that caused the drop to occur
   * @param drops - a collections of EntityItems that will be dropped
   * @return if the event should be cancelled
   */
  boolean onDeathDrops(DamageSource cause, Collection<ItemEntity> drops);

  boolean isAiming();

  void toggleAiming(boolean sendUpdate);

  IInventory getInventory();

  E getEntity();

  UUID getId();
}
