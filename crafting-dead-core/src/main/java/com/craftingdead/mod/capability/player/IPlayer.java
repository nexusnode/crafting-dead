package com.craftingdead.mod.capability.player;

import com.craftingdead.mod.capability.living.ILiving;
import net.minecraft.entity.player.PlayerEntity;

public interface IPlayer<E extends PlayerEntity> extends ILiving<E> {

  void openPlayerContainer();

  void infect(float chance);

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
}
