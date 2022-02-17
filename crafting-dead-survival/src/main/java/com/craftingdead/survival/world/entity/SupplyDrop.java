/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.survival.world.entity;

import javax.annotation.Nullable;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class SupplyDrop extends Entity implements MenuProvider {

  private SimpleContainer inventory = new SimpleContainer(54);

  @Nullable
  private ResourceLocation lootTable;
  private long lootTableSeed;

  public SupplyDrop(EntityType<?> entityType, Level world) {
    super(entityType, world);
  }

  public SupplyDrop(EntityType<?> entityType, Level world, ResourceLocation lootTable,
      long lootTableSeed, double x, double y, double z) {
    this(entityType, world);
    this.lootTable = lootTable;
    this.lootTableSeed = lootTableSeed;
    this.setPos(x, y, z);
    this.setDeltaMovement(Vec3.ZERO);
    this.xo = x;
    this.yo = y;
    this.zo = z;
  }

  public SupplyDrop(PlayMessages.SpawnEntity packet, Level world) {
    this(SurvivalEntityTypes.SUPPLY_DROP.get(), world);
  }

  @Override
  protected void defineSynchedData() {}

  @Override
  public void tick() {
    super.baseTick();
    if (!this.isNoGravity()) {
      this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.009D, 0.0D));
    }

    if (this.onGround) {
      this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
    }

    this.move(MoverType.SELF, this.getDeltaMovement());

  }

  @Override
  public void remove(RemovalReason reason) {
    if (!this.level.isClientSide() && reason.shouldDestroy()) {
      Containers.dropContents(this.level, this, this.inventory);
    }
    super.remove(reason);
  }

  @Override
  protected void readAdditionalSaveData(CompoundTag compound) {
    if (compound.contains("lootTable", Tag.TAG_STRING)) {
      this.lootTable = new ResourceLocation(compound.getString("lootTable"));
      this.lootTableSeed = compound.getLong("lootTableSeed");
    } else if (compound.contains("inventory")) {
      var items = NonNullList.withSize(this.inventory.getContainerSize(), ItemStack.EMPTY);
      ContainerHelper.loadAllItems(compound.getCompound("inventory"), items);
      this.inventory = new SimpleContainer(items.toArray(new ItemStack[0]));
    }
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag compound) {
    if (this.lootTable != null) {
      compound.putString("lootTable", this.lootTable.toString());
      if (this.lootTableSeed != 0L) {
        compound.putLong("lootTableSeed", this.lootTableSeed);
      }
    } else {
      var items = NonNullList.withSize(this.inventory.getContainerSize(), ItemStack.EMPTY);
      for (int i = 0; i < this.inventory.getContainerSize(); i++) {
        items.set(i, this.inventory.getItem(i));
      }
      compound.put("inventory",
          ContainerHelper.saveAllItems(compound.getCompound("inventory"), items));
    }
  }

  @Override
  public boolean isPickable() {
    return true;
  }

  @Override
  public Packet<?> getAddEntityPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }

  @Override
  public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
    player.openMenu(this);
    return InteractionResult.PASS;
  }

  @Override
  public boolean hurt(DamageSource source, float amount) {
    if (!this.level.isClientSide && this.isAlive()) {
      if (this.isInvulnerableTo(source)) {
        return false;
      }
      this.kill();
      return true;
    }
    return true;
  }

  private void addLoot(@Nullable Player player) {
    if (this.lootTable != null && this.level.getServer() != null) {
      LootTable lootTable =
          this.level.getServer().getLootTables().get(this.lootTable);
      this.lootTable = null;
      LootContext.Builder builder = new LootContext.Builder((ServerLevel) this.level)
          .withParameter(LootContextParams.ORIGIN, this.position())
          .withOptionalRandomSeed(this.lootTableSeed);
      builder.withParameter(LootContextParams.KILLER_ENTITY, this);
      if (player != null) {
        builder.withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player);
      }
      lootTable.fill(this.inventory, builder.create(LootContextParamSets.CHEST));
    }
  }

  @Override
  public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity) {
    this.addLoot(playerEntity);
    return ChestMenu.sixRows(id, playerInventory, this.inventory);
  }
}
