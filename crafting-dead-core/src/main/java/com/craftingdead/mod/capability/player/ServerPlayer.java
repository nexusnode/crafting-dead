package com.craftingdead.mod.capability.player;

import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.entity.CorpseEntity;
import com.craftingdead.mod.network.NetworkChannel;
import com.craftingdead.mod.network.message.main.PlayerActionMessage;
import com.craftingdead.mod.network.message.main.SyncGunMessage;
import com.craftingdead.mod.network.message.main.SyncInventoryMessage;
import com.craftingdead.mod.network.message.main.SyncStatisticsMessage;
import com.craftingdead.mod.util.ModDamageSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.world.Difficulty;
import net.minecraftforge.fml.network.PacketDistributor;

public class ServerPlayer extends DefaultPlayer<ServerPlayerEntity> {

  private static final int WATER_DAMAGE_DELAY_TICKS = 20 * 6;

  private static final int WATER_DELAY_TICKS = 20 * 40;

  /**
   * Used to determine whether a data sync packet should be sent to the client.
   */
  private boolean statisticsDirty = true;

  private int waterTimer;

  private boolean inventoryDirty = true;

  public ServerPlayer(ServerPlayerEntity entity) {
    super(entity);
    this.inventory.addListener(inventory -> this.inventoryDirty = true);
  }

  /**
   * Update the player.
   */
  @Override
  public void tick() {
    super.tick();
    this.updateWater();
    this.updateStamina();

    int aliveTicks = this.entity.getStats().getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_DEATH));
    int aliveDays = aliveTicks / 20 / 60 / 20;
    if (this.daysSurvived != aliveDays) {
      this.setDaysSurvived(aliveDays);
    }

    if (this.statisticsDirty) {
      NetworkChannel.MAIN
          .getSimpleChannel()
          .send(PacketDistributor.PLAYER.with(this::getEntity),
              new SyncStatisticsMessage(this.daysSurvived, this.zombiesKilled, this.playersKilled,
                  this.water, this.maxWater, this.stamina, this.maxStamina));
      this.statisticsDirty = false;
    }

    if (this.inventoryDirty) {
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

  private void updateWater() {
    if (this.entity.world.getDifficulty() != Difficulty.PEACEFUL
        && !this.entity.abilities.disableDamage) {
      this.waterTimer++;
      if (this.water <= 0) {
        if (this.waterTimer >= WATER_DAMAGE_DELAY_TICKS && this.water == 0) {
          this.entity.attackEntityFrom(ModDamageSource.DEHYDRATION, 1.0F);
          this.waterTimer = 0;
        }
      } else if (this.waterTimer >= WATER_DELAY_TICKS) {
        this.setWater(this.getWater() - 1);
        if (this.entity.isSprinting()) {
          this.setWater(this.getWater() - 1);
        }
        this.waterTimer = 0;
      }
    }
  }

  private void updateStamina() {
    if (this.entity.isSprinting()) {
      this.setStamina(this.getStamina() - 3);
    } else {
      this.setStamina(this.getStamina() + 8);
    }
  }

  @Override
  public boolean onDeath(DamageSource cause) {
    CorpseEntity corpse = new CorpseEntity(this.entity);
    this.entity.world.addEntity(corpse);
    return false;
  }

  @Override
  public void setTriggerPressed(boolean triggerPressed, boolean sendUpdate) {
    super.setTriggerPressed(triggerPressed, sendUpdate);
    if (sendUpdate) {
      NetworkChannel.MAIN
          .getSimpleChannel()
          .send(PacketDistributor.TRACKING_ENTITY.with(this::getEntity),
              new PlayerActionMessage(this.entity.getEntityId(),
                  triggerPressed ? PlayerActionMessage.Action.TRIGGER_PRESSED
                      : PlayerActionMessage.Action.TRIGGER_RELEASED));

      ItemStack heldStack = this.entity.getHeldItemMainhand();
      heldStack.getCapability(ModCapabilities.GUN_CONTROLLER).ifPresent(gunController -> {
        NetworkChannel.MAIN
            .getSimpleChannel()
            .send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(this::getEntity),
                new SyncGunMessage(this.entity.getEntityId(), gunController.getAmmo()));
      });
    }
  }

  @Override
  public void toggleAiming(boolean sendUpdate) {
    super.toggleAiming(sendUpdate);
    if (sendUpdate) {
      NetworkChannel.MAIN
          .getSimpleChannel()
          .send(PacketDistributor.TRACKING_ENTITY.with(this::getEntity), new PlayerActionMessage(
              this.entity.getEntityId(), PlayerActionMessage.Action.TOGGLE_AIMING));
    }
  }

  @Override
  public void toggleFireMode(boolean sendUpdate) {
    super.toggleFireMode(sendUpdate);
    if (sendUpdate) {
      NetworkChannel.MAIN
          .getSimpleChannel()
          .send(PacketDistributor.TRACKING_ENTITY.with(this::getEntity),
              new PlayerActionMessage(0, PlayerActionMessage.Action.TOGGLE_FIRE_MODE));
    }
  }

  @Override
  public void reload(boolean sendUpdate) {
    super.reload(sendUpdate);
    if (sendUpdate) {
      NetworkChannel.MAIN
          .getSimpleChannel()
          .send(PacketDistributor.TRACKING_ENTITY.with(this::getEntity), new PlayerActionMessage(
              this.entity.getEntityId(), PlayerActionMessage.Action.RELOAD));
    }
  }

  @Override
  public void setDaysSurvived(int daysSurvived) {
    super.setDaysSurvived(daysSurvived);
    this.statisticsDirty = true;
  }

  @Override
  public void setZombiesKilled(int zombiesKilled) {
    super.setZombiesKilled(zombiesKilled);
    this.statisticsDirty = true;
  }

  @Override
  public void setPlayersKilled(int playersKilled) {
    super.setPlayersKilled(playersKilled);
    this.statisticsDirty = true;
  }

  @Override
  public void setWater(int water) {
    super.setWater(water);
    this.statisticsDirty = true;
  }

  @Override
  public void setMaxWater(int maxWater) {
    super.setMaxWater(maxWater);
    this.statisticsDirty = true;
  }

  @Override
  public void setStamina(int stamina) {
    super.setStamina(stamina);
    this.statisticsDirty = true;
  }

  @Override
  public void setMaxStamina(int maxStamina) {
    super.setMaxStamina(maxStamina);
    this.statisticsDirty = true;
  }

  public void copyFrom(ServerPlayer that, boolean wasDeath) {
    if (!wasDeath) {
      this.zombiesKilled = that.zombiesKilled;
      this.playersKilled = that.playersKilled;
    }
  }
}
