/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.entity.extension;

import java.util.Optional;
import java.util.Random;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.Nullable;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.CapabilityUtil;
import com.craftingdead.core.world.action.Action;
import com.craftingdead.core.world.action.ActionObserver;
import com.craftingdead.core.world.item.equipment.Equipment;
import com.craftingdead.core.world.item.gun.Gun;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;

public interface LivingExtension<E extends LivingEntity, H extends LivingHandler>
    extends LivingHandler {

  Capability<LivingExtension<?, ?>> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

  /**
   * @see {@link net.minecraftforge.event.AttachCapabilitiesEvent}
   */
  ResourceLocation CAPABILITY_KEY = new ResourceLocation(CraftingDead.ID, "living_extension");

  @SuppressWarnings("unchecked")
  static <E extends LivingEntity> LivingExtension<E, ?> getOrThrow(E entity) {
    return CapabilityUtil.getOrThrow(CAPABILITY, entity, LivingExtension.class);
  }

  @Nullable
  @SuppressWarnings("unchecked")
  static <E extends LivingEntity> LivingExtension<E, ?> get(E entity) {
    return CapabilityUtil.get(CAPABILITY, entity, LivingExtension.class);
  }

  /**
   * Load this {@link LivingExtension} - called upon capability attachment.
   */
  void load();

  /**
   * Get a handler by its ID.
   * 
   * @param id - the handler's ID
   * @return an {@link Optional} handler
   */
  <T extends LivingHandler> Optional<T> getHandler(LivingHandlerType<T> type);

  /**
   * Get a handler or throw an exception if not present.
   * 
   * @param id - the handler's ID
   * @throws IllegalStateException if not present
   * @return the handler
   */
  @Nonnull
  <T extends LivingHandler> T getHandlerOrThrow(LivingHandlerType<T> type)
      throws IllegalStateException;

  /**
   * Register a handler.
   * 
   * @param type - the handler's type
   * @param handler - the handler to attach
   */
  <T extends H> void registerHandler(LivingHandlerType<T> type, T handler);

  /**
   * Remove a handler.
   * 
   * @param type - the handler's type
   */
  <T extends H> void removeHandler(LivingHandlerType<T> type);

  Optional<Action> getAction();

  /**
   * Perform an unforced action.
   * 
   * @param action - the {@link Action} to perform
   * @param sendUpdate - alert the other side (client/server) of this change
   * @return whether the {@link Action} is being performed
   */
  default boolean performAction(Action action, boolean sendUpdate) {
    return this.performAction(action, false, sendUpdate);
  }

  /**
   * Perform the specified {@link Action}.
   * 
   * @param action - the {@link Action} to perform
   * @param force - whether this {@link Action} will be forced (will override an existing action)
   * @param sendUpdate - alert the other side (client/server) of this change
   * @return whether the {@link Action} is being performed
   */
  <T extends Action> boolean performAction(T action, boolean force, boolean sendUpdate);

  /**
   * Cancel the current {@link Action} being performed.
   * 
   * @param sendUpdate- alert the other side (client/server) of this change
   */
  void cancelAction(boolean sendUpdate);

  /**
   * Set the action observer.
   * 
   * @param actionObserver - the {@link ActionObserver} to attach
   */
  void setActionObserver(@Nullable ActionObserver actionObserver);

  /**
   * Get the action observer.
   * 
   * @return an optional {@link ActionObserver}
   * @see #setActionObserver(ActionObserver)
   */
  Optional<ActionObserver> getActionObserver();

  /**
   * Whether this {@link LivingExtension} is currently monitoring an action.
   * 
   * @return true if it is monitoring an action.
   */
  default boolean isObservingAction() {
    return this.getActionObserver().isPresent();
  }

  /**
   * Prevent this {@link LivingExtension} from moving (for the current tick).
   * 
   * @param movementBlocked - if the movement should be blocked
   */
  void setMovementBlocked(boolean movementBlocked);

  /**
   * Whether this {@link LivingExtension} is currently moving.
   * 
   * @return true if moving
   */
  boolean isMoving();

  /**
   * Get an {@link EntitySnapshot} for the specified tick time.
   * 
   * @param tick - the tick in which to retrieve the {@link EntitySnapshot} for
   * @return the snapshot
   * @throws IndexOutOfBoundsException if the tick is too small
   */
  EntitySnapshot getSnapshot(int tick) throws IndexOutOfBoundsException;

  /**
   * Whether this {@link LivingExtension} is crouching.
   * 
   * @return true if crouching
   */
  boolean isCrouching();

  /**
   * Makes this {@link LivingExtension} crouch.
   * 
   * @param crouching - crouching state
   * @param sendUpdate - alert the other side (client/server) of this change
   */
  void setCrouching(boolean crouching, boolean sendUpdate);

  /**
   * Get the drop chance for the entity equipment slot.
   *
   * <p>
   *
   * NOTE: Drop chances follows the vanilla drop chance formula
   * <code>Math.max(randomFloat - (lootingLevel * 0.01F), 0.0F) < dropChance</code> Use
   * <code>2.0F</code> for guarantee drop
   *
   * @param slot - the equipment slot to get the drop chance
   * @return the drop chance for the provided equipment slot
   */
  float getEquipmentDropChance(Equipment.Slot slot);

  /**
   * Defines the new drop chance for the entity equipment slot.
   *
   * <p>
   *
   * NOTE: Drop chances follows the vanilla drop chance scheme
   * <code>Math.max(randomFloat - (lootingLevel * 0.01F), 0.0F) < dropChance</code> Use
   * <code>2.0F</code> for guarantee drop
   *
   * @param slot - the equipment slot to set the new drop chance
   * @param chance - the new drop chance
   */
  void setEquipmentDropChance(Equipment.Slot slot, float chance);

  /**
   * Get the {@link LivingEntity} associated with this {@link LivingExtension}.
   * 
   * @return the {@link LivingEntity}
   */
  E entity();

  /**
   * Shorthand for {@link LivingEntity#getLevel()}.
   * 
   * @return the {@link Level}
   */
  default Level level() {
    return this.entity().getLevel();
  }

  /**
   * Shorthand for {@link LivingEntity#getRandom()}.
   * 
   * @return the {@link Random}
   */
  default Random random() {
    return this.entity().getRandom();
  }

  /**
   * Shorthand for {@link LivingEntity#getMainHandItem}.
   * 
   * @return the main hand {@link ItemStack}
   */
  default ItemStack mainHandItem() {
    final var item = this.entity().getMainHandItem();
    return item.isEmpty() ? ItemStack.EMPTY : item;
  }

  /**
   * Helper method to retrieve a gun in the entity's main hand.
   * 
   * @return a {@link LazyOptional} gun.
   */
  default LazyOptional<Gun> mainHandGun() {
    return this.mainHandItem().getCapability(Gun.CAPABILITY);
  }

  ItemStack getItemInSlot(Equipment.Slot slot);

  default LazyOptional<Equipment> getEquipmentInSlot(Equipment.Slot slot) {
    return this.getItemInSlot(slot).getCapability(Equipment.CAPABILITY);
  }

  default <T extends Equipment> Optional<T> getEquipmentInSlot(Equipment.Slot slot, Class<T> type) {
    return this.getEquipmentInSlot(slot)
        .filter(type::isInstance)
        .map(type::cast);
  }

  ItemStack setItemInSlot(Equipment.Slot slot, ItemStack itemStack);

  default void breakItem(ItemStack itemStack) {
    if (!itemStack.isEmpty()) {
      if (!this.entity().isSilent()) {
        this.level().playLocalSound(this.entity().getX(), this.entity().getY(),
            this.entity().getZ(), SoundEvents.ITEM_BREAK,
            this.entity().getSoundSource(), 0.8F,
            0.8F + this.level().getRandom().nextFloat() * 0.4F, false);
      }

      this.spawnItemParticles(itemStack, 5);
    }
  }

  default void spawnItemParticles(ItemStack itemStack, int count) {
    for (int i = 0; i < count; ++i) {
      var velocity = new Vec3((this.random().nextFloat() - 0.5D) * 0.1D,
          Math.random() * 0.1D + 0.1D, 0.0D);
      velocity = velocity.xRot(-this.entity().getXRot() * ((float) Math.PI / 180F));
      velocity = velocity.yRot(-this.entity().getYRot() * ((float) Math.PI / 180F));
      var yPos = -this.random().nextFloat() * 0.6D - 0.3D;
      var pos = new Vec3((this.random().nextFloat() - 0.5D) * 0.3D, yPos, 0.6D);
      pos = pos.xRot(-this.entity().getXRot() * ((float) Math.PI / 180F));
      pos = pos.yRot(-this.entity().getYRot() * ((float) Math.PI / 180F));
      pos =
          pos.add(this.entity().getX(), this.entity().getEyeY(), this.entity().getZ());
      if (this.level() instanceof ServerLevel level) {
        level.sendParticles(
            new ItemParticleOption(ParticleTypes.ITEM, itemStack), pos.x, pos.y, pos.z, 1,
            velocity.x, velocity.y + 0.05D, velocity.z, 0.0D);
      } else {
        this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, itemStack), pos.x,
            pos.y, pos.z, velocity.x, velocity.y + 0.05D, velocity.z);
      }
    }
  }

  default Vec3 getVelocity() {
    // 0.98 is a magic number used in Minecraft's movement calculations.
    var gravity = this.entity().getAttributeValue(ForgeMod.ENTITY_GRAVITY.get()) * 0.98F;
    return this.entity().getDeltaMovement().add(0, gravity, 0);
  }

  default EntitySnapshot makeSnapshot(float partialTick) {
    var entity = this.entity();
    return new EntitySnapshot(entity.getPosition(partialTick),
        entity.getBoundingBox(),
        new Vec2(entity.getViewXRot(partialTick), entity.getViewYRot(partialTick)),
        this.getVelocity(), this.isCrouching(),
        entity.getEyeHeight(), true);
  }
}
