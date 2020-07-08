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
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.LootTable;
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
    this.setMotion(Vec3d.ZERO);
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

  @Nullable
  @Override
  public AxisAlignedBB getCollisionBoundingBox() {
    return this.getBoundingBox();
  }

  @Override
  public ActionResultType applyPlayerInteraction(PlayerEntity player, Vec3d vec, Hand hand) {
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
          .withParameter(LootParameters.POSITION, new BlockPos(this))
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
