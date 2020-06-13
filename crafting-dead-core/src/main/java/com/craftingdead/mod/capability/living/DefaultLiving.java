package com.craftingdead.mod.capability.living;

import java.util.Collection;
import java.util.UUID;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.inventory.InventorySlotType;
import com.craftingdead.mod.item.ClothingItem;
import com.craftingdead.mod.item.ModItems;
import com.craftingdead.mod.network.NetworkChannel;
import com.craftingdead.mod.network.message.main.SetSlotMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.network.PacketDistributor;
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

    this.updateGeneralClothingEffects();
    this.updateScubaClothing();
    this.updateScubaMask();
  }

  private void updateScubaClothing() {
    ItemStack clothingStack = this.getStackInSlot(InventorySlotType.CLOTHING.getIndex());
    if (clothingStack.getItem() == ModItems.SCUBA_CLOTHING.get()
        && this.entity.areEyesInFluid(FluidTags.WATER)) {
      this.entity
          .addPotionEffect(new EffectInstance(Effects.DOLPHINS_GRACE, 2, 0, false, false, false));
    }
  }

  private void updateScubaMask() {
    ItemStack headStack = this.getStackInSlot(InventorySlotType.HAT.getIndex());
    if (headStack.getItem() == ModItems.SCUBA_MASK.get()
        && this.entity.areEyesInFluid(FluidTags.WATER)) {
      this.entity
          .addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 2, 0, false, false, false));
    }
  }

  private void updateGeneralClothingEffects() {
    ItemStack clothingStack = this.getStackInSlot(InventorySlotType.CLOTHING.getIndex());
    if (!(clothingStack.getItem() instanceof ClothingItem)) {
      return;
    }
    ClothingItem clothingItem = (ClothingItem) clothingStack.getItem();

    // Fire immunity
    if (clothingItem.hasFireImmunity()) {
      if (this.entity.getFireTimer() > 0) {
        this.entity.extinguish();
      }

      this.entity
          .addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 2, 0, false, false, false));
    }

    // Movement speed
    if (clothingItem.getSlownessLevel() != null) {
      this.entity
          .addPotionEffect(new EffectInstance(Effects.SLOWNESS, 2, clothingItem.getSlownessLevel(),
              false, false, false));
    }
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
  public boolean onDeathDrops(DamageSource cause, Collection<ItemEntity> drops) {
    return false;
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
