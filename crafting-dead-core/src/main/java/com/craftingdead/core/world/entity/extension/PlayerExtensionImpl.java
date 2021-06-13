/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.core.world.entity.extension;

import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.event.OpenEquipmentMenuEvent;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.SynchedData;
import com.craftingdead.core.network.message.play.KillFeedMessage;
import com.craftingdead.core.world.damagesource.KillFeedProvider;
import com.craftingdead.core.world.inventory.EquipmentMenu;
import com.craftingdead.core.world.inventory.ModEquipmentSlotType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.PacketDistributor;

class PlayerExtensionImpl<E extends PlayerEntity>
    extends LivingExtensionImpl<E, PlayerHandler> implements PlayerExtension<E> {

  private final SynchedData dataManager = new SynchedData();

  private static final DataParameter<Boolean> COMBAT_MODE_ENABLED =
      new DataParameter<>(0x02, DataSerializers.BOOLEAN);

  private boolean cachedCombatModeEnabled;

  PlayerExtensionImpl(E entity) {
    super(entity);
    this.dataManager.register(COMBAT_MODE_ENABLED, false);
  }

  @Override
  public void tick() {
    this.cachedCombatModeEnabled = false;
    super.tick();
  }

  @Override
  protected void tickHandler(ResourceLocation extensionId, PlayerHandler extension) {
    super.tickHandler(extensionId, extension);
    if (extension.isCombatModeEnabled()) {
      this.cachedCombatModeEnabled = true;
    }
  }

  @Override
  public void playerTick() {
    if (this.isCrouching()) {
      this.getEntity().setPose(Pose.SWIMMING);
    }
    for (PlayerHandler handler : this.handlers.values()) {
      handler.playerTick();
    }
  }

  @Override
  public boolean isMovementBlocked() {
    return !this.getEntity().isSpectator() && super.isMovementBlocked();
  }

  @Override
  public boolean isCombatModeEnabled() {
    return !this.getEntity().isSpectator()
        && (this.cachedCombatModeEnabled || this.dataManager.get(COMBAT_MODE_ENABLED));
  }

  @Override
  public void setCombatModeEnabled(boolean combatModeEnabled) {
    this.dataManager.set(COMBAT_MODE_ENABLED, combatModeEnabled);
  }

  @Override
  public void openEquipmentMenu() {
    if (MinecraftForge.EVENT_BUS.post(new OpenEquipmentMenuEvent(this))) {
      return;
    }
    this.getEntity().openMenu(new SimpleNamedContainerProvider(
        (windowId, playerInventory, playerEntity) -> new EquipmentMenu(windowId,
            this.getEntity().inventory),
        new TranslationTextComponent("container.equipment")));
  }

  @Override
  public void openStorage(ModEquipmentSlotType slotType) {
    ItemStack storageStack = this.getItemHandler().getStackInSlot(slotType.getIndex());
    storageStack.getCapability(Capabilities.STORAGE)
        .ifPresent(storage -> this.getEntity().openMenu(
            new SimpleNamedContainerProvider(storage, storageStack.getHoverName())));
  }

  @Override
  public boolean onDeath(DamageSource source) {
    if (super.onDeath(source)) {
      return true;
    } else if (source instanceof KillFeedProvider) {
      NetworkChannel.PLAY.getSimpleChannel().send(PacketDistributor.ALL.noArg(),
          new KillFeedMessage(((KillFeedProvider) source).createKillFeedEntry(this.getEntity())));
    }
    return false;
  }

  @Override
  protected boolean keepInventory() {
    return this.getLevel().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY);
  }

  @Override
  public void copyFrom(PlayerExtension<?> that, boolean wasDeath) {
    // Copies the inventory. Doesn't actually matter if it was death or not.
    // Death drops from 'that' should be cleared on death drops to prevent item duplication.
    for (int i = 0; i < that.getItemHandler().getSlots(); i++) {
      this.getItemHandler().setStackInSlot(i, that.getItemHandler().getStackInSlot(i));
    }

    for (PlayerHandler extension : this.handlers.values()) {
      extension.copyFrom(that, wasDeath);
    }
  }

  public SynchedData getDataManager() {
    return this.dataManager;
  }

  @Override
  public void encode(PacketBuffer out, boolean writeAll) {
    super.encode(out, writeAll);
    SynchedData.writeEntries(
        writeAll ? this.dataManager.getAll() : this.dataManager.getDirty(), out);
  }

  @Override
  public void decode(PacketBuffer in) {
    super.decode(in);
    this.dataManager.setEntryValues(SynchedData.readEntries(in));
  }

  @Override
  public boolean requiresSync() {
    return super.requiresSync() || this.dataManager.isDirty();
  }
}
