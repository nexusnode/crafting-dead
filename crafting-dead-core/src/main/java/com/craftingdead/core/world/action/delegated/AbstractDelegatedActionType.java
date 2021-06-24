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

package com.craftingdead.core.world.action.delegated;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;

public abstract class AbstractDelegatedActionType implements DelegatedActionType {

  private final boolean shrinkStack;
  @Nullable
  private final Supplier<Item> returnItem;
  @Nullable
  private final Supplier<SoundEvent> finishSound;

  private final boolean shrinkStackInCreative;

  private final boolean returnItemInCreative;

  protected AbstractDelegatedActionType(Builder<?> builder) {
    this.shrinkStack = builder.shrinkStack;
    this.returnItem = builder.returnItem;
    this.finishSound = builder.finishSound;
    this.shrinkStackInCreative = builder.shrinkStackInCreative;
    this.returnItemInCreative = builder.returnItemInCreative;
  }

  public boolean isShrinkStack() {
    return this.shrinkStack;
  }

  public Optional<Item> getReturnItem() {
    return Optional.ofNullable(this.returnItem).map(Supplier::get);
  }

  public Optional<SoundEvent> getFinishSound() {
    return Optional.ofNullable(this.finishSound).map(Supplier::get);
  }

  public boolean isShrinkStackInCreative() {
    return this.shrinkStackInCreative;
  }

  public boolean isReturnItemInCreative() {
    return this.returnItemInCreative;
  }

  protected static abstract class Builder<SELF extends Builder<SELF>> {

    private final Function<SELF, AbstractDelegatedActionType> factory;

    protected boolean shrinkStack = true;
    @Nullable
    protected Supplier<Item> returnItem;
    @Nullable
    protected Supplier<SoundEvent> finishSound;

    protected boolean shrinkStackInCreative;

    protected boolean returnItemInCreative = true;

    protected Builder(Function<SELF, AbstractDelegatedActionType> factory) {
      this.factory = factory;
    }

    public SELF setShrinkStack(boolean shrinkStack) {
      this.shrinkStack = shrinkStack;
      return this.self();
    }

    public SELF setReturnItem(Supplier<Item> returnItem) {
      this.returnItem = returnItem;
      return this.self();
    }

    public SELF setFinishSound(SoundEvent finishSound) {
      return this.setFinishSound(() -> finishSound);
    }

    public SELF setFinishSound(Supplier<SoundEvent> finishSound) {
      this.finishSound = finishSound;
      return this.self();
    }

    public SELF setShrinkStackInCreative(boolean shrinkStackInCreative) {
      this.shrinkStackInCreative = shrinkStackInCreative;
      return this.self();
    }

    public SELF setReturnItemInCreative(boolean returnItemInCreative) {
      this.returnItemInCreative = returnItemInCreative;
      return this.self();
    }

    public AbstractDelegatedActionType build() {
      return this.factory.apply(this.self());
    }

    @SuppressWarnings("unchecked")
    protected SELF self() {
      return (SELF) this;
    }
  }
}
