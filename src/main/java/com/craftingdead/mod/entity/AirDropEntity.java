package com.craftingdead.mod.entity;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.item.minecart.ContainerMinecartEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;


public abstract class AirDropEntity extends ContainerMinecartEntity {

  public AirDropEntity(EntityType<?> p_i48536_1_, World p_i48536_2_) {
    super(p_i48536_1_, p_i48536_2_);
  }

  public AirDropEntity(EntityType<?> p_i48537_1_, double p_i48537_2_, double p_i48537_4_,
      double p_i48537_6_, World p_i48537_8_) {
    super(p_i48537_1_, p_i48537_2_, p_i48537_4_, p_i48537_6_, p_i48537_8_);
  }

  /**
   * "Y" determines the rate of fall
   */
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

  /**
   * Returns true if other Entities should be prevented from moving through this Entity.
   */
  @Override
  public boolean canBeCollidedWith() {
    return true;
  }

  /**
   * Returns true if this entity should push and be pushed by other entities when colliding.
   */
  @Override
  public boolean canBePushed() {
    return false;
  }

  @Override
  public IPacket<?> createSpawnPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }

  @Override
  public Type getMinecartType() {
    return AbstractMinecartEntity.Type.CHEST;
  }

  @Override
  public void killMinecart(DamageSource source) {
    this.remove();
  }

  @Override
  protected Container func_213968_a(int id, PlayerInventory playerInventory) {
    return ChestContainer.createGeneric9X3(id, playerInventory, this);
  }

  @Override
  public int getSizeInventory() {
    return 27;
  }

  @Nullable
  @Override
  public AxisAlignedBB getCollisionBox(Entity entityIn) {
    return entityIn.canBePushed() ? entityIn.getBoundingBox() : null;
  }

  @Nullable
  @Override
  public AxisAlignedBB getCollisionBoundingBox() {
    return this.getBoundingBox();
  }

  /**
   * Here you can specify the loot that will be inside the drop
   */
  public abstract void randomLoot();

  public void addRandomItemWithChance(Item par1ID, int par2Chance, int par3Amount) {

    Random rand = new Random();

    for (int i = 0; i < par3Amount; i++) {

      if (rand.nextInt(100) <= par2Chance) {

        int slot = rand.nextInt(27);

        if (this.getStackInSlot(slot) == null || rand.nextBoolean()) {
          ItemStack stack = new ItemStack(par1ID, 1);
          this.setInventorySlotContents(slot, stack.copy());
        }
      }
    }
  }

}
