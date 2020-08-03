package com.craftingdead.core.capability.living.player;

import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.inventory.InventorySlotType;
import com.craftingdead.core.inventory.container.ModInventoryContainer;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.message.main.SyncStatisticsMessage;
import com.craftingdead.core.util.ModDamageSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.text.TranslationTextComponent;
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

  public ServerPlayer(ServerPlayerEntity entity) {
    super(entity);
  }

  /**
   * Update the player.
   */
  @Override
  public void tick() {
    super.tick();
    this.updateWater();
    this.updateStamina();

    int aliveTicks = this.getEntity().getStats().getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_DEATH));
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
  }

  private void updateWater() {
    if (this.getEntity().world.getDifficulty() != Difficulty.PEACEFUL
        && !this.getEntity().abilities.disableDamage) {
      this.waterTimer++;
      if (this.water <= 0) {
        if (this.waterTimer >= WATER_DAMAGE_DELAY_TICKS && this.water == 0) {
          this.getEntity().attackEntityFrom(ModDamageSource.DEHYDRATION, 1.0F);
          this.waterTimer = 0;
        }
      } else if (this.waterTimer >= WATER_DELAY_TICKS) {
        this.setWater(this.getWater() - 1);
        if (this.getEntity().isSprinting()) {
          this.setWater(this.getWater() - 1);
        }
        this.waterTimer = 0;
      }
    }
  }

  private void updateStamina() {
    if (this.getEntity().isSprinting()) {
      this.setStamina(this.getStamina() - 3);
    } else {
      this.setStamina(this.getStamina() + 8);
    }
  }

  public void openInventory() {
    this.getEntity()
        .openContainer(new SimpleNamedContainerProvider((windowId, playerInventory,
            playerEntity) -> new ModInventoryContainer(windowId, this.getEntity().inventory),
            new TranslationTextComponent("container.player")));
  }

  public void openStorage(InventorySlotType slotType) {
    ItemStack storageStack = this.getItemHandler().getStackInSlot(slotType.getIndex());
    storageStack
        .getCapability(ModCapabilities.STORAGE)
        .ifPresent(storage -> this.getEntity()
            .openContainer(
                new SimpleNamedContainerProvider(storage, storageStack.getDisplayName())));
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

    // Copies the inventory. Doesn't actually matter if it was death or not.
    // Death drops from 'that' should be cleared on death drops to prevent item duplication.
    for (int i = 0; i < that.getItemHandler().getSlots() - 1; i++) {
      this.getItemHandler().setStackInSlot(i, that.getItemHandler().getStackInSlot(i));
    }
  }
}
