package com.craftingdead.mod.capability.player;

import com.craftingdead.mod.entity.CorpseEntity;
import com.craftingdead.mod.net.NetworkChannel;
import com.craftingdead.mod.net.message.main.TriggerPressedMessage;
import com.craftingdead.mod.net.message.main.UpdateStatisticsMessage;
import com.craftingdead.mod.util.ModDamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Difficulty;
import net.minecraftforge.fml.network.PacketDistributor;

public class ServerPlayer extends DefaultPlayer<ServerPlayerEntity> {

  private static final int WATER_DAMAGE_DELAY_TICKS = 20 * 6;

  /**
   * Used to calculate if a day has passed by.
   */
  private boolean lastDay;

  /**
   * Used to determine whether a data sync packet should be sent to the client.
   */
  private boolean dirty = true;

  private int waterDamageTicks;

  public ServerPlayer(ServerPlayerEntity entity) {
    super(entity);
    this.lastDay = entity.getEntityWorld().isDaytime();
  }

  /**
   * Update the player.
   */
  @Override
  public void tick() {
    super.tick();
    this.updateDaysSurvived();
    this.updateWater();
    if (this.dirty) {
      NetworkChannel.MAIN.getSimpleChannel().send(PacketDistributor.PLAYER.with(this::getEntity),
          new UpdateStatisticsMessage(this.daysSurvived, this.zombiesKilled, this.playersKilled,
              this.water, this.maxWater));
      this.dirty = false;
    }
  }

  private void updateDaysSurvived() {
    boolean isDay = this.entity.getEntityWorld().isDaytime();
    // If it was night time and is now day time then increment their days survived
    // by 1
    if (!this.lastDay && isDay) {
      this.setDaysSurvived(this.getDaysSurvived() + 1);
    }
    this.lastDay = isDay;
  }

  private void updateWater() {
    if (this.entity.world.getDifficulty() != Difficulty.PEACEFUL) {
      if (this.water > 0) {
        this.setWater(this.getWater() - 1);
        if (this.entity.isSprinting()) {
          this.setWater(this.getWater() - 1);
        }
      }

      if (this.water == 0) {
        if (this.waterDamageTicks++ >= WATER_DAMAGE_DELAY_TICKS) {
          this.entity.attackEntityFrom(ModDamageSource.DEHYDRATION, 1.0F);
          this.waterDamageTicks = 0;
        }
      }
    }
  }

  @Override
  public boolean onKill(Entity target) {
    if (target instanceof ZombieEntity) {
      this.setZombiesKilled(this.getZombiesKilled() + 1);
    } else if (target instanceof ServerPlayerEntity) {
      this.setPlayersKilled(this.getPlayersKilled() + 1);
    }
    return false;
  }

  @Override
  public boolean onDeath(DamageSource cause) {
    CorpseEntity corpse = new CorpseEntity(this.entity);
    this.entity.world.addEntity(corpse);
    return false;
  }

  @Override
  public void setTriggerPressed(boolean triggerPressed) {
    super.setTriggerPressed(triggerPressed);
    NetworkChannel.MAIN.getSimpleChannel().send(
        PacketDistributor.TRACKING_ENTITY.with(this::getEntity),
        new TriggerPressedMessage(this.entity.getEntityId(), triggerPressed));
  }

  @Override
  public void setDaysSurvived(int daysSurvived) {
    super.setDaysSurvived(daysSurvived);
    this.dirty = true;
  }

  @Override
  public void setZombiesKilled(int zombiesKilled) {
    super.setZombiesKilled(zombiesKilled);
    this.dirty = true;
  }

  @Override
  public void setPlayersKilled(int playersKilled) {
    super.setPlayersKilled(playersKilled);
    this.dirty = true;
  }

  @Override
  public void setWater(int water) {
    super.setWater(water);
    this.dirty = true;
  }

  @Override
  public void setMaxWater(int maxWater) {
    super.setMaxWater(maxWater);
    this.dirty = true;
  }

  public void copyFrom(ServerPlayer that, boolean wasDeath) {
    if (!wasDeath) {
      this.daysSurvived = that.daysSurvived;
      this.zombiesKilled = that.zombiesKilled;
      this.playersKilled = that.playersKilled;
    }
  }
}
