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

package com.craftingdead.survival.world.entity;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class SupplyDropEntity extends Entity implements INamedContainerProvider {

  private Inventory inventory = new Inventory(54);

  @Nullable
  private ResourceLocation lootTable;
  private long lootTableSeed;

  public SupplyDropEntity(EntityType<?> entityType, World world) {
    super(entityType, world);
  }

  public SupplyDropEntity(EntityType<?> entityType, World world, ResourceLocation lootTable,
      long lootTableSeed, double x, double y, double z) {
    this(entityType, world);
    this.lootTable = lootTable;
    this.lootTableSeed = lootTableSeed;
    this.setPos(x, y, z);
    this.setDeltaMovement(Vector3d.ZERO);
    this.xo = x;
    this.yo = y;
    this.zo = z;
  }

  public SupplyDropEntity(FMLPlayMessages.SpawnEntity packet, World world) {
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
  public void remove(boolean keepData) {
    if (!this.level.isClientSide && !keepData) {
      InventoryHelper.dropContents(this.level, this, this.inventory);
    }
    super.remove(keepData);
  }

  @Override
  protected void readAdditionalSaveData(CompoundNBT compound) {
    if (compound.contains("lootTable", NBT.TAG_STRING)) {
      this.lootTable = new ResourceLocation(compound.getString("lootTable"));
      this.lootTableSeed = compound.getLong("lootTableSeed");
    } else if (compound.contains("inventory")) {
      NonNullList<ItemStack> items =
          NonNullList.withSize(this.inventory.getContainerSize(), ItemStack.EMPTY);
      ItemStackHelper.loadAllItems(compound.getCompound("inventory"), items);
      this.inventory = new Inventory(items.toArray(new ItemStack[0]));
    }
  }

  @Override
  protected void addAdditionalSaveData(CompoundNBT compound) {
    if (this.lootTable != null) {
      compound.putString("lootTable", this.lootTable.toString());
      if (this.lootTableSeed != 0L) {
        compound.putLong("lootTableSeed", this.lootTableSeed);
      }
    } else {
      NonNullList<ItemStack> items =
          NonNullList.withSize(this.inventory.getContainerSize(), ItemStack.EMPTY);
      for (int i = 0; i < this.inventory.getContainerSize(); i++) {
        items.set(i, this.inventory.getItem(i));
      }
      compound.put("inventory",
          ItemStackHelper.saveAllItems(compound.getCompound("inventory"), items));
    }
  }

  @Override
  public boolean isPickable() {
    return true;
  }

  @Override
  public IPacket<?> getAddEntityPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }

  @Override
  public ActionResultType interactAt(PlayerEntity player, Vector3d vec, Hand hand) {
    player.openMenu(this);
    return ActionResultType.PASS;
  }

  @Override
  public boolean hurt(DamageSource source, float amount) {
    if (!this.level.isClientSide && this.isAlive()) {
      if (this.isInvulnerableTo(source)) {
        return false;
      }
      this.remove();
      return true;
    }
    return true;
  }

  private void addLoot(@Nullable PlayerEntity player) {
    if (this.lootTable != null && this.level.getServer() != null) {
      LootTable lootTable =
          this.level.getServer().getLootTables().get(this.lootTable);
      this.lootTable = null;
      LootContext.Builder builder = new LootContext.Builder((ServerWorld) this.level)
          .withParameter(LootParameters.ORIGIN, this.position())
          .withOptionalRandomSeed(this.lootTableSeed);
      builder.withParameter(LootParameters.KILLER_ENTITY, this);
      if (player != null) {
        builder.withLuck(player.getLuck()).withParameter(LootParameters.THIS_ENTITY, player);
      }
      lootTable.fill(this.inventory, builder.create(LootParameterSets.CHEST));
    }
  }

  @Override
  public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    this.addLoot(playerEntity);
    return ChestContainer.sixRows(id, playerInventory, this.inventory);
  }
}
