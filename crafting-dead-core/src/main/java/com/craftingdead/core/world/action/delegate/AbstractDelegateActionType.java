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
