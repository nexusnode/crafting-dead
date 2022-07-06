/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.entity.extension;

import java.util.Collection;
import java.util.function.Consumer;
import org.apache.commons.lang3.mutable.MutableInt;
import com.craftingdead.core.event.OpenEquipmentMenuEvent;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.SynchedData;
import com.craftingdead.core.network.message.play.EnableCombatModeMessage;
import com.craftingdead.core.world.action.Action;
import com.craftingdead.core.world.inventory.EquipmentMenu;
import com.craftingdead.core.world.inventory.ModEquipmentSlot;
import com.craftingdead.core.world.inventory.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

final class PlayerExtensionImpl<E extends Player>
    extends BaseLivingExtension<E, PlayerHandler> implements PlayerExtension<E> {

  private static final float HANDCUFFS_DAMAGE_CHANCE = 0.1F;

  private final SynchedData data = new SynchedData();

  private static final EntityDataAccessor<Boolean> COMBAT_MODE_ENABLED =
      new EntityDataAccessor<>(0x00, EntityDataSerializers.BOOLEAN);

  private static final EntityDataAccessor<ItemStack> HANDCUFFS =
      new EntityDataAccessor<>(0x01, EntityDataSerializers.ITEM_STACK);

  private boolean cachedCombatModeEnabled;

  PlayerExtensionImpl(E entity) {
    super(entity);
    this.data.register(COMBAT_MODE_ENABLED, false);
    this.data.register(HANDCUFFS, ItemStack.EMPTY);
  }

  @Override
  public void tick() {
    this.cachedCombatModeEnabled = false;
    super.tick();
  }

  @Override
  protected void tickHandler(LivingHandlerType<? extends PlayerHandler> type,
      PlayerHandler extension) {
    super.tickHandler(type, extension);
    if (extension.isCombatModeEnabled()) {
      this.cachedCombatModeEnabled = true;
    }
  }

  @Override
  public boolean performAction(Action action, boolean force, boolean sendUpdate) {
    return !this.isHandcuffed() && super.performAction(action, force, sendUpdate);
  }

  @Override
  public void playerTick() {
    if (this.isCrouching()) {
      this.entity().setPose(Pose.SWIMMING);
    }
    for (PlayerHandler handler : this.handlers.values()) {
      handler.playerTick();
    }
  }

  @Override
  public boolean isMovementBlocked() {
    return !this.entity().isSpectator() && super.isMovementBlocked();
  }

  @Override
  public boolean handleAttack(Entity target) {
    if (this.handlers.values().stream().anyMatch(e -> e.handleAttack(target))) {
      return true;
    }

    if (this.isHandcuffed()) {
      if (!this.level().isClientSide()) {
        this.handcuffInteract(HANDCUFFS_DAMAGE_CHANCE);
      }
      return true;
    }

    return false;
  }

  @Override
  public boolean handleInteract(InteractionHand hand, Entity target) {
    if (this.handlers.values().stream().anyMatch(e -> e.handleInteract(hand, target))) {
      return true;
    }

    if (this.isHandcuffed()) {
      if (!this.level().isClientSide()) {
        this.handcuffInteract(HANDCUFFS_DAMAGE_CHANCE);
      }
      return true;
    }

    return false;
  }

  @Override
  public boolean handleLeftClickBlock(BlockPos pos, Direction face,
      Consumer<Event.Result> attackResult, Consumer<Event.Result> mineResult) {
    if (this.handlers.values().stream()
        .anyMatch(e -> e.handleLeftClickBlock(pos, face, attackResult, mineResult))) {
      return true;
    }

    if (this.isHandcuffed()) {
      if (this.level().isClientSide()) {
        mineResult.accept(Event.Result.DENY);
      } else {
        this.handcuffInteract(HANDCUFFS_DAMAGE_CHANCE);
      }
      return true;
    }

    return false;
  }

  @Override
  public boolean handleRightClickBlock(InteractionHand hand, BlockPos pos, Direction face) {
    if (this.handlers.values().stream().anyMatch(e -> e.handleRightClickBlock(hand, pos, face))) {
      return true;
    }

    if (this.isHandcuffed()) {
      if (!this.level().isClientSide()) {
        this.handcuffInteract(HANDCUFFS_DAMAGE_CHANCE);
      }
      return true;
    }

    return false;
  }

  @Override
  public boolean handleRightClickItem(InteractionHand hand) {
    if (this.handlers.values().stream().anyMatch(e -> e.handleRightClickItem(hand))) {
      return true;
    }

    if (this.isHandcuffed()) {
      if (!this.level().isClientSide()) {
        this.handcuffInteract(HANDCUFFS_DAMAGE_CHANCE);
      }
      return true;
    }

    return false;
  }

  public boolean handleBlockBreak(BlockPos pos, BlockState block, MutableInt xp) {
    return this.handlers.values().stream().anyMatch(e -> e.handleBlockBreak(pos, block, xp));
  }

  private void handcuffInteract(float chance) {
    if (this.random().nextFloat() < chance
        && !this.damageHandcuffs(1)
        && !this.entity().isSilent()) {
      this.level().playSound(null, this.entity().getX(), this.entity().getY(),
          this.entity().getZ(), SoundEvents.ITEM_BREAK, this.entity().getSoundSource(), 0.8F,
          0.8F + this.level().getRandom().nextFloat() * 0.4F);
    }
  }

  @Override
  public boolean isCombatModeEnabled() {
    return !this.entity().isSpectator()
        && (this.cachedCombatModeEnabled || this.data.get(COMBAT_MODE_ENABLED));
  }

  @Override
  public void setCombatModeEnabled(boolean combatModeEnabled) {
    if (this.level().isClientSide() && combatModeEnabled != this.isCombatModeEnabled()) {
      NetworkChannel.PLAY.getSimpleChannel().sendToServer(
          new EnableCombatModeMessage(combatModeEnabled));
    } else {
      this.data.set(COMBAT_MODE_ENABLED, combatModeEnabled);
    }
  }

  @Override
  public void openEquipmentMenu() {
    if (MinecraftForge.EVENT_BUS.post(new OpenEquipmentMenuEvent(this))) {
      return;
    }
    this.entity().openMenu(new SimpleMenuProvider(
        (windowId, inventory, player) -> new EquipmentMenu(windowId,
            this.entity().getInventory(), this.getItemHandler()),
        new TranslatableComponent("container.equipment")));
  }

  @Override
  public void openStorage(ModEquipmentSlot slotType) {
    var storageStack = this.getItemHandler().getStackInSlot(slotType.getIndex());
    storageStack.getCapability(Storage.CAPABILITY)
        .ifPresent(storage -> this.entity().openMenu(
            new SimpleMenuProvider(storage, storageStack.getHoverName())));
  }

  @Override
  public boolean handleDeathLoot(DamageSource cause, Collection<ItemEntity> drops, int lootingLevel) {
    if (super.handleDeathLoot(cause, drops, lootingLevel)) {
      return true;
    }

    var handcuffs = this.getHandcuffs();
    if (!handcuffs.isEmpty()) {
      var itemEntity = new ItemEntity(this.level(), this.entity().getX(),
          this.entity().getY(), this.entity().getZ(), handcuffs);
      itemEntity.setDefaultPickUpDelay();
      drops.add(itemEntity);
    }

    return false;
  }

  @Override
  protected boolean keepInventory() {
    return this.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY);
  }

  @Override
  public void copyFrom(PlayerExtension<ServerPlayer> that, boolean wasDeath) {
    // Copies the inventory. Doesn't actually matter if it was death or not.
    // Death drops from 'that' should be cleared on death drops to prevent item duplication.
    for (int i = 0; i < that.getItemHandler().getSlots(); i++) {
      this.getItemHandler().setStackInSlot(i, that.getItemHandler().getStackInSlot(i));
    }

    for (var extension : this.handlers.values()) {
      extension.copyFrom(that, wasDeath);
    }

    this.setCombatModeEnabled(that.isCombatModeEnabled());

    if (!wasDeath) {
      this.setHandcuffs(that.getHandcuffs());
    }
  }

  @Override
  public CompoundTag serializeNBT() {
    var tag = super.serializeNBT();
    tag.put("handcuffs", this.getHandcuffs().serializeNBT());
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag tag) {
    super.deserializeNBT(tag);
    if (tag.contains("handcuffs", Tag.TAG_COMPOUND)) {
      this.setHandcuffs(ItemStack.of(tag.getCompound("handcuffs")));
    }
  }

  @Override
  public void encode(FriendlyByteBuf out, boolean writeAll) {
    super.encode(out, writeAll);
    SynchedData.pack(
        writeAll ? this.data.getAll() : this.data.packDirty(), out);

  }

  @Override
  public void decode(FriendlyByteBuf in) {
    super.decode(in);
    this.data.assignValues(SynchedData.unpack(in));
  }

  @Override
  public boolean requiresSync() {
    return super.requiresSync() || this.data.isDirty();
  }

  @Override
  public ItemStack getHandcuffs() {
    return this.data.get(HANDCUFFS);
  }

  @Override
  public void setHandcuffs(ItemStack itemStack) {
    this.data.set(HANDCUFFS, itemStack);
  }
}
