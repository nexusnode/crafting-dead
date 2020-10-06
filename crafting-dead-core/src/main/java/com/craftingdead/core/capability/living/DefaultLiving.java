/**
 * Crafting Dead Copyright (C) 2020 Nexus Node
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */
package com.craftingdead.core.capability.living;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import com.craftingdead.core.action.IAction;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.animationprovider.IAnimationProvider;
import com.craftingdead.core.inventory.InventorySlotType;
import com.craftingdead.core.item.ModItems;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.message.main.CancelActionMessage;
import com.craftingdead.core.network.message.main.CrouchMessage;
import com.craftingdead.core.network.message.main.PerformActionMessage;
import com.craftingdead.core.network.message.main.SetSlotMessage;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.world.GameRules;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.PacketDistributor.PacketTarget;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class DefaultLiving<E extends LivingEntity> implements ILiving<E> {

  /**
   * The vanilla entity.
   */
  protected final E entity;

  /**
   * The last held {@link ItemStack} - used to check if the entity has switched item.
   */
  protected ItemStack lastHeldStack = null;

  private List<Integer> dirtySlots = new IntArrayList();

  private final Long2ObjectLinkedOpenHashMap<Snapshot> snapshots =
      new Long2ObjectLinkedOpenHashMap<>();

  private final ItemStackHandler itemHandler =
      new ItemStackHandler(InventorySlotType.values().length) {
        @Override
        public void onContentsChanged(int slot) {
          if (!DefaultLiving.this.entity.getEntityWorld().isRemote()) {
            DefaultLiving.this.dirtySlots.add(slot);
          }
        }
      };

  private IAction action;

  private IActionProgress actionProgress;

  private boolean freezeMovement;

  private boolean crouching;

  public DefaultLiving() {
    throw new IllegalStateException("No entity provided");
  }

  public DefaultLiving(E entity) {
    this.entity = entity;
  }

  @Override
  public boolean performAction(IAction action, boolean force, boolean sendUpdate) {
    final IActionProgress targetActionProgress =
        action.getTarget().flatMap(ILiving::getActionProgress).orElse(null);
    if (this.actionProgress != null || targetActionProgress != null) {
      if (!force) {
        return false;
      }
      this.actionProgress.stop();
      if (targetActionProgress != this.actionProgress) {
        targetActionProgress.stop();
      }
    }
    if ((this.action != null && !force) || !action.start()) {
      return false;
    }
    this.cancelAction(true);
    this.action = action;
    this.actionProgress = action.getPerformerProgress();
    action.getTarget().ifPresent(target -> target.setActionProgress(action.getTargetProgress()));
    if (sendUpdate) {
      PacketTarget target =
          this.getEntity().getEntityWorld().isRemote() ? PacketDistributor.SERVER.noArg()
              : PacketDistributor.TRACKING_ENTITY_AND_SELF.with(this::getEntity);
      NetworkChannel.PLAY.getSimpleChannel().send(target,
          new PerformActionMessage(action.getActionType(), this.getEntity().getEntityId(),
              action.getTarget().map(ILiving::getEntity).map(Entity::getEntityId).orElse(-1)));
    }
    return true;
  }

  @Override
  public void cancelAction(boolean sendUpdate) {
    if (this.action == null) {
      return;
    }
    this.action.cancel();
    this.removeAction();
    if (sendUpdate) {
      PacketTarget target =
          this.getEntity().getEntityWorld().isRemote() ? PacketDistributor.SERVER.noArg()
              : PacketDistributor.TRACKING_ENTITY_AND_SELF.with(this::getEntity);
      NetworkChannel.PLAY.getSimpleChannel().send(target,
          new CancelActionMessage(this.getEntity().getEntityId()));
    }
  }

  @Override
  public void setActionProgress(IActionProgress actionProgress) {
    this.actionProgress = actionProgress;
  }

  @Override
  public Optional<IActionProgress> getActionProgress() {
    return Optional.ofNullable(this.actionProgress);
  }

  private void removeAction() {
    if (this.action != null) {
      this.actionProgress = null;
      this.action.getTarget().ifPresent(target -> target.setActionProgress(null));
      this.action = null;
    }
  }

  @Override
  public void setFreezeMovement(boolean movementFrozen) {
    this.freezeMovement = movementFrozen;
  }

  @Override
  public boolean getFreezeMovement() {
    return this.freezeMovement;
  }

  @Override
  public void tick() {
    ItemStack heldStack = this.entity.getHeldItemMainhand();
    if (heldStack != this.lastHeldStack) {
      this.getActionProgress().ifPresent(IActionProgress::stop);
      heldStack.getCapability(ModCapabilities.GUN)
          .ifPresent(gun -> gun.setTriggerPressed(this, heldStack, false, false));
      this.lastHeldStack = heldStack;
    }

    if (this.action != null && this.action.tick()) {
      this.removeAction();
    }

    heldStack.getCapability(ModCapabilities.GUN).ifPresent(gun -> gun.tick(this, heldStack));
    heldStack.getCapability(ModCapabilities.ANIMATION_PROVIDER).map(Optional::of)
        .orElse(Optional.empty()).flatMap(IAnimationProvider::getAnimationController)
        .ifPresent(c -> c.tick(this.getEntity(), heldStack));

    if (this.freezeMovement) {
      this.entity.forceSetPosition(this.entity.prevPosX, this.entity.prevPosY,
          this.entity.prevPosZ);
      this.freezeMovement = false;
    }

    if (this.crouching) {
      this.getEntity().setPose(Pose.SWIMMING);
    }

    this.updateGeneralClothingEffects();
    this.updateScubaClothing();
    this.updateScubaMask();

    if (!this.entity.getEntityWorld().isRemote()) {
      for (int slot : this.dirtySlots) {
        NetworkChannel.PLAY.getSimpleChannel().send(
            PacketDistributor.TRACKING_ENTITY_AND_SELF.with(this::getEntity),
            new SetSlotMessage(this.entity.getEntityId(), slot,
                this.itemHandler.getStackInSlot(slot)));
      }
      this.dirtySlots.clear();

      if (this.snapshots.size() >= 20) {
        this.snapshots.removeFirst();
      }
      final long gameTime = this.getEntity().getEntityWorld().getGameTime();
      this.snapshots.put(gameTime, new Snapshot(gameTime, this.getEntity().getPositionVector(),
          this.getEntity().getBoundingBox().expand(0, 1.0D, 0), this.getEntity().getPitchYaw(),
          this.getEntity().getEyeHeight()));
    }
  }

  private void updateScubaClothing() {
    ItemStack clothingStack =
        this.itemHandler.getStackInSlot(InventorySlotType.CLOTHING.getIndex());
    if (clothingStack.getItem() == ModItems.SCUBA_CLOTHING.get()
        && this.entity.areEyesInFluid(FluidTags.WATER)) {
      this.entity
          .addPotionEffect(new EffectInstance(Effects.DOLPHINS_GRACE, 2, 0, false, false, false));
    }
  }

  private void updateScubaMask() {
    ItemStack headStack = this.itemHandler.getStackInSlot(InventorySlotType.HAT.getIndex());
    if (headStack.getItem() == ModItems.SCUBA_MASK.get()
        && this.entity.areEyesInFluid(FluidTags.WATER)) {
      this.entity
          .addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 2, 0, false, false, false));
    }
  }

  private void updateGeneralClothingEffects() {
    ItemStack clothingStack =
        this.itemHandler.getStackInSlot(InventorySlotType.CLOTHING.getIndex());
    clothingStack.getCapability(ModCapabilities.CLOTHING).ifPresent(clothing -> {
      // Fire immunity
      if (clothing.hasFireImmunity()) {
        if (this.entity.getFireTimer() > 0) {
          this.entity.extinguish();
        }

        this.entity.addPotionEffect(
            new EffectInstance(Effects.FIRE_RESISTANCE, 2, 0, false, false, false));
      }

      // Movement speed
      clothing.getSlownessAmplifier()
          .ifPresent(amplifier -> this.entity.addPotionEffect(
              new EffectInstance(Effects.SLOWNESS, 2, amplifier, false, false, false)));
    });
  }

  public float onDamaged(DamageSource source, float amount) {
    return amount;
  }

  public boolean onAttacked(DamageSource source, float amount) {
    return false;
  }

  public boolean onKill(Entity target) {
    return false;
  }

  public boolean onDeath(DamageSource cause) {
    return false;
  }

  public boolean onDeathDrops(DamageSource cause, Collection<ItemEntity> drops) {
    boolean shouldKeepInventory =
        this.getEntity().getEntityWorld().getGameRules().getBoolean(GameRules.KEEP_INVENTORY);
    if (!shouldKeepInventory) {
      // Adds items from CD inventory
      for (int i = 0; i < this.itemHandler.getSlots(); i++) {
        ItemEntity itemEntity = new ItemEntity(this.getEntity().world, this.getEntity().getPosX(),
            this.getEntity().getPosY(), this.getEntity().getPosZ(),
            this.itemHandler.extractItem(i, this.itemHandler.getStackInSlot(i).getCount(), false));
        itemEntity.setDefaultPickupDelay();
        drops.add(itemEntity);
      }
    }
    return false;
  }

  @Override
  public IItemHandlerModifiable getItemHandler() {
    return this.itemHandler;
  }

  @Override
  public Optional<Snapshot> getSnapshot(long gameTime) {
    Snapshot snapshot = this.snapshots.get(gameTime);
    if (snapshot == null && gameTime >= this.snapshots.firstLongKey()
        && gameTime <= this.snapshots.lastLongKey()) {
      ObjectBidirectionalIterator<Long2ObjectMap.Entry<Snapshot>> it =
          this.snapshots.long2ObjectEntrySet().fastIterator();
      while (it.hasNext()) {
        Snapshot nextSnapshot = it.next().getValue();
        if (nextSnapshot.getGameTime() > gameTime) {
          if (snapshot == null) {
            snapshot = nextSnapshot;
          }
          break;
        }
        snapshot = nextSnapshot;
      }
    }
    return Optional.ofNullable(snapshot);
  }

  @Override
  public boolean isCrouching() {
    return this.crouching;
  }

  @Override
  public void setCrouching(boolean crouching, boolean sendUpdate) {
    this.crouching = crouching;
    if (sendUpdate) {
      PacketTarget target =
          this.getEntity().getEntityWorld().isRemote() ? PacketDistributor.SERVER.noArg()
              : PacketDistributor.TRACKING_ENTITY_AND_SELF.with(this::getEntity);
      NetworkChannel.PLAY.getSimpleChannel().send(target,
          new CrouchMessage(this.getEntity().getEntityId(), crouching));
    }
  }

  @Override
  public E getEntity() {
    return this.entity;
  }

  @Override
  public CompoundNBT serializeNBT() {
    return new CompoundNBT();
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {}

  @Override
  public int hashCode() {
    return this.entity.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj)
        || (obj instanceof DefaultLiving && ((DefaultLiving<?>) obj).entity.equals(this.entity));
  }
}
