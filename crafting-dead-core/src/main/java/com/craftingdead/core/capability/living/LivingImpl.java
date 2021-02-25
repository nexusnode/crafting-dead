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

package com.craftingdead.core.capability.living;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.craftingdead.core.action.IAction;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.animationprovider.IAnimationProvider;
import com.craftingdead.core.event.LivingEvent;
import com.craftingdead.core.inventory.InventorySlotType;
import com.craftingdead.core.item.ModItems;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.message.play.CancelActionMessage;
import com.craftingdead.core.network.message.play.CrouchMessage;
import com.craftingdead.core.network.message.play.PerformActionMessage;
import com.craftingdead.core.util.ModSoundEvents;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.PacketDistributor.PacketTarget;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class LivingImpl<L extends LivingEntity, E extends ILivingExtension>
    implements ILiving<L, E> {

  /**
   * The vanilla entity.
   */
  protected final L entity;

  protected final Object2ObjectOpenHashMap<ResourceLocation, E> extensions =
      new Object2ObjectOpenHashMap<>();

  protected final Object2ObjectOpenHashMap<ResourceLocation, E> dirtyExtensions =
      new Object2ObjectOpenHashMap<>();

  /**
   * The last held {@link ItemStack} - used to check if the entity has switched item.
   */
  protected ItemStack lastHeldStack = null;

  private List<Integer> dirtySlots = new IntArrayList();

  private final Long2ObjectLinkedOpenHashMap<EntitySnapshot> snapshots =
      new Long2ObjectLinkedOpenHashMap<>();

  private final ItemStackHandler itemHandler =
      new ItemStackHandler(InventorySlotType.values().length) {
        @Override
        public void onContentsChanged(int slot) {
          if (!LivingImpl.this.entity.getEntityWorld().isRemote()) {
            LivingImpl.this.dirtySlots.add(slot);
          }
        }
      };

  private IAction action;

  private IProgressMonitor actionProgress;

  private boolean movementBlocked;

  private boolean crouching;

  private Vector3d lastPos;

  private boolean moving;

  private Visibility cachedVisibility = Visibility.VISIBLE;

  public LivingImpl() {
    throw new IllegalStateException("No entity provided");
  }

  public LivingImpl(L entity) {
    this.entity = entity;
  }

  @Override
  public void load() {
    MinecraftForge.EVENT_BUS.post(new LivingEvent.Load(this));
  }

  @Override
  public void registerExtension(ResourceLocation id, E extension) {
    if (this.extensions.containsKey(id)) {
      throw new IllegalArgumentException(
          "Extension with id " + id.toString() + " already registered");
    }
    this.extensions.put(id, extension);
  }

  @Override
  public Optional<E> getExtension(ResourceLocation id) {
    return Optional.ofNullable(this.extensions.get(id));
  }

  @Override
  public E getExpectedExtension(ResourceLocation id) {
    E extension = this.extensions.get(id);
    if (extension == null) {
      throw new IllegalStateException("Missing extension with ID: " + id.toString());
    }
    return extension;
  }

  @Override
  public boolean performAction(IAction action, boolean force, boolean sendUpdate) {
    if (MinecraftForge.EVENT_BUS.post(new LivingEvent.PerformAction<>(this, action))) {
      return false;
    }
    final IProgressMonitor targetProgressMonitor =
        action.getTarget().flatMap(ILiving::getProgressMonitor).orElse(null);
    if (this.actionProgress != null || targetProgressMonitor != null) {
      if (!force) {
        return false;
      }
      this.actionProgress.stop();
      if (targetProgressMonitor != this.actionProgress) {
        targetProgressMonitor.stop();
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
  public void setActionProgress(IProgressMonitor actionProgress) {
    this.actionProgress = actionProgress;
  }

  @Override
  public Optional<IProgressMonitor> getProgressMonitor() {
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
  public void setMovementBlocked(boolean movementBlocked) {
    this.movementBlocked = movementBlocked;
  }

  @Override
  public boolean isMovementBlocked() {
    return this.movementBlocked;
  }

  @Override
  public boolean isMoving() {
    return this.moving;
  }

  @Override
  public void tick() {
    ItemStack heldStack = this.entity.getHeldItemMainhand();
    if (heldStack != this.lastHeldStack) {
      this.getProgressMonitor().ifPresent(IProgressMonitor::stop);
      if (this.lastHeldStack != null) {
        this.lastHeldStack.getCapability(ModCapabilities.GUN)
            .ifPresent(gun -> gun.reset(this));
      }
      if (heldStack.getCapability(ModCapabilities.GUN).isPresent()) {
        this.entity.playSound(ModSoundEvents.GUN_EQUIP.get(), 0.8F, 1.0F);
      }
      this.lastHeldStack = heldStack;
    }

    // Reset this every tick
    this.movementBlocked = false;

    if (this.action != null && this.action.tick()) {
      this.removeAction();
    }

    heldStack.getCapability(ModCapabilities.GUN).ifPresent(gun -> gun.tick(this));
    heldStack.getCapability(ModCapabilities.ANIMATION_PROVIDER).map(Optional::of)
        .orElse(Optional.empty()).flatMap(IAnimationProvider::getAnimationController)
        .ifPresent(c -> c.tick(this.getEntity(), heldStack));

    this.updateGeneralClothingEffects();
    this.updateScubaClothing();
    this.updateScubaMask();

    if (!this.entity.getEntityWorld().isRemote()) {
      if (this.snapshots.size() >= 20) {
        this.snapshots.removeFirst();
      }
      // This is called at the start of the entity tick so it's equivalent of last tick's position.
      this.snapshots.put(this.entity.getServer().getTickCounter() - 1,
          new EntitySnapshot(this.entity));
    }

    this.moving = !this.entity.getPositionVec().equals(this.lastPos);
    this.lastPos = this.entity.getPositionVec();

    for (Map.Entry<ResourceLocation, E> entry : this.extensions.entrySet()) {
      this.tickExtension(entry.getKey(), entry.getValue());
    }
  }

  protected void tickExtension(ResourceLocation extensionId, E extension) {
    extension.tick();

    // Precedence = (1) INVISIBLE (2) PARTIALLY_VISIBLE (3) VISIBLE
    this.cachedVisibility = Visibility.VISIBLE;
    switch (extension.getVisibility()) {
      case INVISIBLE:
        this.cachedVisibility = Visibility.INVISIBLE;
      case PARTIALLY_VISIBLE:
        if (this.cachedVisibility == Visibility.VISIBLE) {
          this.cachedVisibility = Visibility.PARTIALLY_VISIBLE;
        }
        break;
      default:
        break;
    }

    if (extension.isMovementBlocked()) {
      this.movementBlocked = true;
    }

    if (extension.requiresSync()) {
      this.dirtyExtensions.put(extensionId, extension);
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


  @Override
  public float onDamaged(DamageSource source, float amount) {
    return this.extensions.values().stream().reduce(amount,
        (result, extension) -> extension.onDamaged(source, result), (u, t) -> t);
  }

  @Override
  public boolean onAttacked(DamageSource source, float amount) {
    return this.extensions.values().stream().anyMatch(e -> e.onAttacked(source, amount));
  }

  @Override
  public boolean onKill(Entity target) {
    return this.extensions.values().stream().anyMatch(e -> e.onKill(target));
  }

  @Override
  public boolean onDeath(DamageSource cause) {
    return this.extensions.values().stream().map(e -> e.onDeath(cause)).anyMatch(v -> v == true);
  }

  @Override
  public boolean onDeathDrops(DamageSource cause, Collection<ItemEntity> drops) {
    if (this.extensions.values().stream().map(e -> e.onDeathDrops(cause, drops))
        .anyMatch(v -> v == true)) {
      return true;
    }
    boolean shouldKeepInventory =
        this.getEntity().getEntityWorld().getGameRules().getBoolean(GameRules.KEEP_INVENTORY);
    if (!shouldKeepInventory) {
      // Adds items from CD inventory
      for (int i = 0; i < this.itemHandler.getSlots(); i++) {
        ItemStack itemStack =
            this.itemHandler.extractItem(i, Integer.MAX_VALUE, false);
        if (!itemStack.isEmpty()) {
          ItemEntity itemEntity = new ItemEntity(this.getEntity().world, this.getEntity().getPosX(),
              this.getEntity().getPosY(), this.getEntity().getPosZ(), itemStack);
          itemEntity.setDefaultPickupDelay();
          drops.add(itemEntity);
        }
      }
    }
    return false;
  }

  @Override
  public Visibility getVisibility() {
    return this.cachedVisibility;
  }

  @Override
  public IItemHandlerModifiable getItemHandler() {
    return this.itemHandler;
  }

  @Override
  public Optional<EntitySnapshot> getSnapshot(long tick) {
    if (tick >= this.entity.getServer().getTickCounter()) {
      return Optional.of(new EntitySnapshot(this.entity));
    }
    EntitySnapshot snapshot = this.snapshots.get(tick);
    if (snapshot == null && tick >= this.snapshots.firstLongKey()
        && tick <= this.snapshots.lastLongKey()) {
      ObjectBidirectionalIterator<Long2ObjectMap.Entry<EntitySnapshot>> it =
          this.snapshots.long2ObjectEntrySet().fastIterator();
      while (it.hasNext()) {
        Long2ObjectMap.Entry<EntitySnapshot> entry = it.next();
        EntitySnapshot nextSnapshot = entry.getValue();
        if (entry.getLongKey() > tick) {
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
  public L getEntity() {
    return this.entity;
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
    nbt.put("inventory", this.itemHandler.serializeNBT());
    for (Map.Entry<ResourceLocation, E> entry : this.extensions.entrySet()) {
      CompoundNBT extensionNbt = entry.getValue().serializeNBT();
      if (!extensionNbt.isEmpty()) {
        nbt.put(entry.getKey().toString(), extensionNbt);
      }
    }
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    this.itemHandler.deserializeNBT(nbt.getCompound("inventory"));
    for (Map.Entry<ResourceLocation, E> entry : this.extensions.entrySet()) {
      CompoundNBT extensionNbt = nbt.getCompound(entry.getKey().toString());
      if (!extensionNbt.isEmpty()) {
        entry.getValue().deserializeNBT(extensionNbt);
      }
    }
  }

  @Override
  public int hashCode() {
    return this.entity.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj)
        || (obj instanceof LivingImpl && ((LivingImpl<?, ?>) obj).entity.equals(this.entity));
  }

  @Override
  public void encode(PacketBuffer out, boolean writeAll) {
    // Item Handler
    if (writeAll) {
      out.writeShort(this.itemHandler.getSlots());
      for (int i = 0; i < this.itemHandler.getSlots(); i++) {
        out.writeShort(i);
        out.writeItemStack(this.itemHandler.getStackInSlot(i));
      }
    } else {
      out.writeShort(this.dirtySlots.size());
      for (int i : this.dirtySlots) {
        out.writeShort(i);
        out.writeItemStack(this.itemHandler.getStackInSlot(i));
      }
    }

    // Extensions
    ObjectSet<Map.Entry<ResourceLocation, E>> extensionsToSend =
        writeAll ? this.extensions.entrySet() : this.dirtyExtensions.entrySet();
    out.writeVarInt(extensionsToSend.size());
    for (Map.Entry<ResourceLocation, E> entry : extensionsToSend) {
      out.writeResourceLocation(entry.getKey());
      PacketBuffer extensionData = new PacketBuffer(Unpooled.buffer());
      entry.getValue().encode(extensionData, writeAll);
      out.writeVarInt(extensionData.readableBytes());
      out.writeBytes(extensionData);
    }
    this.dirtyExtensions.clear();
  }

  @Override
  public void decode(PacketBuffer in) {
    // Item Handler
    int itemsSize = in.readShort();
    for (int i = 0; i < itemsSize; i++) {
      this.itemHandler.setStackInSlot(in.readShort(), in.readItemStack());
    }

    // Extensions
    int extensionsSize = in.readVarInt();
    for (int x = 0; x < extensionsSize; x++) {
      ResourceLocation id = in.readResourceLocation();
      int dataSize = in.readVarInt();
      E extension = this.extensions.get(id);
      if (extension == null) {
        in.readerIndex(in.readerIndex() + dataSize);
        continue;
      }
      extension.decode(in);
    }
  }

  @Override
  public boolean requiresSync() {
    return !this.dirtySlots.isEmpty() || !this.dirtyExtensions.isEmpty();
  }
}
