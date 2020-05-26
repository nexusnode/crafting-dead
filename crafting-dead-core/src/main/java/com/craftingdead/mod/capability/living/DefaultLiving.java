package com.craftingdead.mod.capability.living;

import java.util.UUID;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.inventory.InventorySlotType;
import com.craftingdead.mod.network.NetworkChannel;
import com.craftingdead.mod.network.message.main.SetSlotMessage;
import com.craftingdead.mod.network.message.main.ToggleAimingMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.PacketDistributor.PacketTarget;
import net.minecraftforge.items.ItemStackHandler;

public class DefaultLiving<E extends LivingEntity> extends ItemStackHandler implements ILiving<E> {

  /**
   * The vanilla entity.
   */
  protected final E entity;

  /**
   * The last held {@link ItemStack} - used to check if the entity has switched item.
   */
  protected ItemStack lastHeldStack = null;

  private boolean aiming;

  public DefaultLiving() {
    this(null);
  }

  public DefaultLiving(E entity) {
    super(InventorySlotType.values().length);
    this.entity = entity;
  }

  @Override
  public void onContentsChanged(int slot) {
    if (!this.entity.getEntityWorld().isRemote()) {
      NetworkChannel.MAIN
          .getSimpleChannel()
          .send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(this::getEntity),
              new SetSlotMessage(this.entity.getEntityId(), slot, this.getStackInSlot(slot)));
    }
  }

  @Override
  public void tick() {
    ItemStack heldStack = this.entity.getHeldItemMainhand();
    if (heldStack != this.lastHeldStack) {
      heldStack
          .getCapability(ModCapabilities.GUN)
          .ifPresent(gunController -> gunController.cancelActions(this.entity, heldStack));
      this.lastHeldStack = heldStack;
    }

    heldStack
        .getCapability(ModCapabilities.GUN)
        .ifPresent(gunController -> gunController.tick(this.entity, heldStack));
  }

  @Override
  public float onDamaged(DamageSource source, float amount) {
    return amount;
  }

  @Override
  public boolean onAttacked(DamageSource source, float amount) {
    return false;
  }

  @Override
  public boolean onKill(Entity target) {
    return false;
  }

  @Override
  public boolean onDeath(DamageSource cause) {
    return false;
  }

  @Override
  public void toggleAiming(boolean sendUpdate) {
    this.aiming = !this.aiming;
    if (sendUpdate) {
      PacketTarget target =
          this.entity.getEntityWorld().isRemote() ? PacketDistributor.SERVER.noArg()
              : PacketDistributor.TRACKING_ENTITY.with(this::getEntity);
      NetworkChannel.MAIN
          .getSimpleChannel()
          .send(target, new ToggleAimingMessage(this.entity.getEntityId()));
    }
  }

  @Override
  public boolean isAiming() {
    return this.aiming;
  }

  @Override
  public E getEntity() {
    return this.entity;
  }

  @Override
  public UUID getId() {
    return this.entity != null ? this.entity.getUniqueID() : null;
  }
}
