/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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

package com.craftingdead.core.world.action.delegate;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.world.item.Item;
import net.minecraft.sounds.SoundEvent;

public abstract class AbstractDelegateActionType implements DelegateActionType {

  private final boolean consumeItem;
  @Nullable
  private final Supplier<Item> returnItem;
  @Nullable
  private final Supplier<SoundEvent> finishSound;

  private final boolean consumeItemInCreative;

  private final boolean useResultItemInCreative;

  protected AbstractDelegateActionType(Builder<?> builder) {
    this.consumeItem = builder.consumeItem;
    this.returnItem = builder.returnItem;
    this.finishSound = builder.finishSound;
    this.consumeItemInCreative = builder.consumeItemInCreative;
    this.useResultItemInCreative = builder.useResultItemInCreative;
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

  protected static abstract class Builder<SELF extends Builder<SELF>> {

    private final Function<SELF, AbstractDelegateActionType> factory;

    protected boolean consumeItem = true;
    @Nullable
    protected Supplier<Item> returnItem;
    @Nullable
    protected Supplier<SoundEvent> finishSound;

    protected boolean consumeItemInCreative;

    protected boolean useResultItemInCreative = true;

    protected Builder(Function<SELF, AbstractDelegateActionType> factory) {
      this.factory = factory;
    }

    public SELF consumeItem(boolean consumeItem) {
      this.consumeItem = consumeItem;
      return this.self();
    }

    public SELF returnItem(Supplier<Item> returnItem) {
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

    public AbstractDelegateActionType build() {
      return this.factory.apply(this.self());
    }

    @SuppressWarnings("unchecked")
    protected SELF self() {
      return (SELF) this;
    }
  }
}
