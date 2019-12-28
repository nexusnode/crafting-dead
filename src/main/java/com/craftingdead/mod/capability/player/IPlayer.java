package com.craftingdead.mod.capability.player;

import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.INBTSerializable;

public interface IPlayer<E extends PlayerEntity> extends INBTSerializable<CompoundNBT> {

  void tick();

  /**
   * When the player kills another entity.
   *
   * @param target - the {@link Entity} killed
   * @return if the event should be cancelled
   */
  boolean onKill(Entity target);

  /**
   * When the player's health reaches 0.
   *
   * @param cause - the source of the damage
   * @return if the event should be cancelled
   */
  boolean onDeath(DamageSource cause);

  int getDaysSurvived();

  void setDaysSurvived(int daysSurvived);

  int getZombiesKilled();

  void setZombiesKilled(int zombiesKilled);

  int getPlayersKilled();

  void setPlayersKilled(int playersKilled);

  int getWater();

  void setWater(int water);

  int getMaxWater();

  void setMaxWater(int maxWater);

  int getStamina();

  void setStamina(int stamina);

  int getMaxStamina();

  void setMaxStamina(int stamina);

  E getEntity();

  UUID getId();
}
