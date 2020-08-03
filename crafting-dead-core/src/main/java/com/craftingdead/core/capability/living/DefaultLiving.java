package com.craftingdead.core.capability.living;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import com.craftingdead.core.action.IAction;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.inventory.InventorySlotType;
import com.craftingdead.core.item.ModItems;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.message.main.CancelActionMessage;
import com.craftingdead.core.network.message.main.PerformActionMessage;
import com.craftingdead.core.network.message.main.SetSlotMessage;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
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
      NetworkChannel.MAIN.getSimpleChannel().send(target,
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
      NetworkChannel.MAIN.getSimpleChannel().send(target,
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
    heldStack.getCapability(ModCapabilities.ANIMATION_PROVIDER)
        .ifPresent(provider -> provider.getAnimationController().tick(this.getEntity(), heldStack));

    if (this.freezeMovement) {
      this.entity.forceSetPosition(this.entity.prevPosX, this.entity.prevPosY,
          this.entity.prevPosZ);
      this.freezeMovement = false;
    }

    this.updateGeneralClothingEffects();
    this.updateScubaClothing();
    this.updateScubaMask();

    for (int slot : this.dirtySlots) {
      NetworkChannel.MAIN.getSimpleChannel().send(
          PacketDistributor.TRACKING_ENTITY_AND_SELF.with(this::getEntity),
          new SetSlotMessage(this.entity.getEntityId(), slot,
              this.itemHandler.getStackInSlot(slot)));
    }
    this.dirtySlots.clear();
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
  public CompoundNBT serializeNBT() {
    return new CompoundNBT();
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {}

  @Override
  public IItemHandlerModifiable getItemHandler() {
    return this.itemHandler;
  }

  @Override
  public E getEntity() {
    return this.entity;
  }

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
