package com.craftingdead.core.capability.living;

import java.util.Collection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.INBTSerializable;

public interface ILivingHandler extends INBTSerializable<CompoundNBT> {

  void tick();

  /**
   * When this entity is damaged; with potions and armour taken into account.
   * 
   * @param source - the source of damage
   * @param amount - the amount of damage taken
   * @return the new damage amount
   */
  default float onDamaged(DamageSource source, float amount) {
    return amount;
  }

  /**
   * When this entity is attacked.
   * 
   * @param source - the source of damage
   * @param amount - the amount of damage taken
   * @return if the event should be cancelled
   */
  default boolean onAttacked(DamageSource source, float amount) {
    return false;
  }

  /**
   * When this entity kills another entity.
   *
   * @param target - the {@link Entity} killed
   * @return if the event should be cancelled
   */
  default boolean onKill(Entity target) {
    return false;
  }

  /**
   * When this entity's health reaches 0.
   *
   * @param cause - the cause of death
   * @return if the event should be cancelled
   */
  default boolean onDeath(DamageSource cause) {
    return false;
  }

  /**
   * When this entity's death causes dropped items to appear.
   *
   * @param cause - the DamageSource that caused the drop to occur
   * @param drops - a collections of EntityItems that will be dropped
   * @return if the event should be cancelled
   */
  default boolean onDeathDrops(DamageSource cause, Collection<ItemEntity> drops) {
    return false;
  }
}
