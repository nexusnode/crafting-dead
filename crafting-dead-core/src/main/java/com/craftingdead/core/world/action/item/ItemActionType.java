/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.action.item;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.craftingdead.core.world.action.Action;
import com.craftingdead.core.world.action.ActionType;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.google.common.base.Predicates;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class ItemActionType<T extends ItemAction>
    extends ForgeRegistryEntry<ActionType<?>> implements ActionType<T> {

  private final boolean triggeredByClient;
  private final boolean freezeMovement;
  private final int totalDurationTicks;
  private final Predicate<ItemStack> heldItemPredicate;
  private final boolean consumeItem;
  @Nullable
  private final Supplier<? extends Item> returnItem;
  @Nullable
  private final Supplier<SoundEvent> finishSound;
  private final boolean consumeItemInCreative;
  private final boolean useResultItemInCreative;

  protected ItemActionType(Builder<?> builder) {
    this.triggeredByClient = builder.triggeredByClient;
    this.freezeMovement = builder.freezeMovement;
    this.totalDurationTicks = builder.totalDurationTicks;
    this.heldItemPredicate = builder.heldItemPredicate;
    this.consumeItem = builder.consumeItem;
    this.returnItem = builder.returnItem;
    this.finishSound = builder.finishSound;
    this.consumeItemInCreative = builder.consumeItemInCreative;
    this.useResultItemInCreative = builder.useResultItemInCreative;
  }

  public boolean isFreezeMovement() {
    return this.freezeMovement;
  }

  public int getTotalDurationTicks() {
    return this.totalDurationTicks;
  }

  public Predicate<ItemStack> getHeldItemPredicate() {
    return this.heldItemPredicate;
  }

  public boolean shouldConsumeItem() {
    return this.consumeItem;
  }

  public Optional<Item> getReturnItem() {
    return Optional.ofNullable(this.returnItem).map(Supplier::get);
  }

  public Optional<SoundEvent> getFinishSound() {
    return Optional.ofNullable(this.finishSound).map(Supplier::get);
  }

  public boolean shouldConsumeItemInCreative() {
    return this.consumeItemInCreative;
  }

  public boolean useResultItemInCreative() {
    return this.useResultItemInCreative;
  }

  @Override
  public boolean isTriggeredByClient() {
    return this.triggeredByClient;
  }

  public Optional<Action> createBlockAction(LivingExtension<?, ?> performer,
      UseOnContext context) {
    return Optional.empty();
  }

  public Optional<Action> createEntityAction(LivingExtension<?, ?> performer,
      LivingExtension<?, ?> target, InteractionHand hand) {
    return Optional.empty();
  }

  public Optional<Action> createAction(LivingExtension<?, ?> performer, InteractionHand hand) {
    return Optional.empty();
  }

  public static abstract class Builder<SELF extends Builder<SELF>> {

    private boolean triggeredByClient;
    private boolean freezeMovement;
    private int totalDurationTicks = 32;
    private Predicate<ItemStack> heldItemPredicate = Predicates.alwaysTrue();

    private boolean consumeItem = true;
    @Nullable
    private Supplier<? extends Item> returnItem;
    @Nullable
    private Supplier<SoundEvent> finishSound;

    private boolean consumeItemInCreative;

    private boolean useResultItemInCreative = true;

    public SELF setTriggeredByClient(boolean triggeredByClient) {
      this.triggeredByClient = triggeredByClient;
      return this.self();
    }

    public SELF setFreezeMovement(boolean freezeMovement) {
      this.freezeMovement = freezeMovement;
      return this.self();
    }

    public SELF duration(int totalDurationTicks) {
      this.totalDurationTicks = totalDurationTicks;
      return this.self();
    }

    public SELF forItem(Supplier<? extends Item> item) {
      return this.forItem(itemStack -> itemStack.is(item.get()));
    }

    public SELF forItem(Predicate<ItemStack> heldItemPredicate) {
      this.heldItemPredicate = heldItemPredicate;
      return this.self();
    }

    public SELF consumeItem(boolean consumeItem) {
      this.consumeItem = consumeItem;
      return this.self();
    }

    public SELF returnItem(Supplier<? extends Item> returnItem) {
      this.returnItem = returnItem;
      return this.self();
    }

    public SELF finishSound(SoundEvent finishSound) {
      return this.finishSound(() -> finishSound);
    }

    public SELF finishSound(Supplier<SoundEvent> finishSound) {
      this.finishSound = finishSound;
      return this.self();
    }

    public SELF consumeItemInCreative(boolean consumeItemInCreative) {
      this.consumeItemInCreative = consumeItemInCreative;
      return this.self();
    }

    public SELF useResultItemInCreative(boolean useResultItemInCreative) {
      this.useResultItemInCreative = useResultItemInCreative;
      return this.self();
    }

    public abstract ItemActionType<?> build();

    @SuppressWarnings("unchecked")
    protected SELF self() {
      return (SELF) this;
    }
  }
}
