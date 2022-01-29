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

import com.craftingdead.core.event.OpenEquipmentMenuEvent;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.SynchedData;
import com.craftingdead.core.network.message.play.AddKillFeedEntryMessage;
import com.craftingdead.core.world.damagesource.KillFeedProvider;
import com.craftingdead.core.world.inventory.EquipmentMenu;
import com.craftingdead.core.world.inventory.ModEquipmentSlot;
import com.craftingdead.core.world.inventory.storage.Storage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.PacketDistributor;

class PlayerExtensionImpl<E extends Player>
    extends LivingExtensionImpl<E, PlayerHandler> implements PlayerExtension<E> {

  private final SynchedData dataManager = new SynchedData();

  private static final EntityDataAccessor<Boolean> COMBAT_MODE_ENABLED =
      new EntityDataAccessor<>(0x02, EntityDataSerializers.BOOLEAN);

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
    this.getEntity().openMenu(new SimpleMenuProvider(
        (windowId, inventory, player) -> new EquipmentMenu(windowId,
            this.getEntity().getInventory(), this.getItemHandler()),
        new TranslatableComponent("container.equipment")));
  }

  @Override
  public void openStorage(ModEquipmentSlot slotType) {
    var storageStack = this.getItemHandler().getStackInSlot(slotType.getIndex());
    storageStack.getCapability(Storage.CAPABILITY)
        .ifPresent(storage -> this.getEntity().openMenu(
            new SimpleMenuProvider(storage, storageStack.getHoverName())));
  }

  @Override
  public boolean handleDeath(DamageSource source) {
    if (super.handleDeath(source)) {
      return true;
    } else if (source instanceof KillFeedProvider provider) {
      NetworkChannel.PLAY.getSimpleChannel().send(PacketDistributor.ALL.noArg(),
          new AddKillFeedEntryMessage(provider.createKillFeedEntry(this.getEntity())));
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

    for (var extension : this.handlers.values()) {
      extension.copyFrom(that, wasDeath);
    }
  }

  public SynchedData getDataManager() {
    return this.dataManager;
  }

  @Override
  public void encode(FriendlyByteBuf out, boolean writeAll) {
    super.encode(out, writeAll);
    SynchedData.pack(
        writeAll ? this.dataManager.getAll() : this.dataManager.packDirty(), out);
  }

  @Override
  public void decode(FriendlyByteBuf in) {
    super.decode(in);
    this.dataManager.assignValues(SynchedData.unpack(in));
  }

  @Override
  public boolean requiresSync() {
    return super.requiresSync() || this.dataManager.isDirty();
  }
}
