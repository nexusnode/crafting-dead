/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
package com.craftingdead.core.entity;

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
    this.setPosition(x, y, z);
    this.setMotion(Vector3d.ZERO);
    this.prevPosX = x;
    this.prevPosY = y;
    this.prevPosZ = z;
  }

  public SupplyDropEntity(FMLPlayMessages.SpawnEntity packet, World world) {
    this(ModEntityTypes.supplyDrop, world);
  }

  @Override
  protected void registerData() {}

  @Override
  public void tick() {
    super.baseTick();
    if (!this.hasNoGravity()) {
      this.setMotion(this.getMotion().add(0.0D, -0.009D, 0.0D));
    }

    if (this.onGround) {
      this.setMotion(this.getMotion().scale(0.5D));
    }

    this.move(MoverType.SELF, this.getMotion());

  }

  @Override
  public void remove(boolean keepData) {
    if (!this.world.isRemote && !keepData) {
      InventoryHelper.dropInventoryItems(this.world, this, this.inventory);
    }
    super.remove(keepData);
  }

  @Override
  protected void readAdditional(CompoundNBT compound) {
    if (compound.contains("lootTable", NBT.TAG_STRING)) {
      this.lootTable = new ResourceLocation(compound.getString("lootTable"));
      this.lootTableSeed = compound.getLong("lootTableSeed");
    } else if (compound.contains("inventory")) {
      NonNullList<ItemStack> items =
          NonNullList.withSize(this.inventory.getSizeInventory(), ItemStack.EMPTY);
      ItemStackHelper.loadAllItems(compound.getCompound("inventory"), items);
      this.inventory = new Inventory(items.toArray(new ItemStack[0]));
    }
  }

  @Override
  protected void writeAdditional(CompoundNBT compound) {
    if (this.lootTable != null) {
      compound.putString("lootTable", this.lootTable.toString());
      if (this.lootTableSeed != 0L) {
        compound.putLong("lootTableSeed", this.lootTableSeed);
      }
    } else {
      NonNullList<ItemStack> items =
          NonNullList.withSize(this.inventory.getSizeInventory(), ItemStack.EMPTY);
      for (int i = 0; i < this.inventory.getSizeInventory(); i++) {
        items.set(i, this.inventory.getStackInSlot(i));
      }
      compound
          .put("inventory", ItemStackHelper.saveAllItems(compound.getCompound("inventory"), items));
    }
  }

  @Override
  public boolean canBeCollidedWith() {
    return true;
  }

  @Override
  public IPacket<?> createSpawnPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }

  @Override
  public ActionResultType applyPlayerInteraction(PlayerEntity player, Vector3d vec, Hand hand) {
    player.openContainer(this);
    return ActionResultType.PASS;
  }

  @Override
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (!this.world.isRemote && this.isAlive()) {
      if (this.isInvulnerableTo(source)) {
        return false;
      }
      this.remove();
      return true;
    }
    return true;
  }

  private void addLoot(@Nullable PlayerEntity player) {
    if (this.lootTable != null && this.world.getServer() != null) {
      LootTable lootTable =
          this.world.getServer().getLootTableManager().getLootTableFromLocation(this.lootTable);
      this.lootTable = null;
      LootContext.Builder builder = new LootContext.Builder((ServerWorld) this.world)
          .withParameter(LootParameters.field_237457_g_, this.getPositionVec())
          .withSeed(this.lootTableSeed);
      builder.withParameter(LootParameters.KILLER_ENTITY, this);
      if (player != null) {
        builder.withLuck(player.getLuck()).withParameter(LootParameters.THIS_ENTITY, player);
      }
      lootTable.fillInventory(this.inventory, builder.build(LootParameterSets.CHEST));
    }
  }

  @Override
  public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    this.addLoot(playerEntity);
    return ChestContainer.createGeneric9X6(id, playerInventory, this.inventory);
  }
}
