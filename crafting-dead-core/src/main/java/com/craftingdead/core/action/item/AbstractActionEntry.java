/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
package com.craftingdead.core.action.item;

import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.craftingdead.core.capability.living.ILiving;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.SoundEvent;

public abstract class AbstractActionEntry<P extends AbstractActionEntry.Properties<P>>
    implements IActionEntry {

  private final boolean shrinkStack;
  @Nullable
  private final IItemProvider returnItem;
  @Nullable
  private final Supplier<SoundEvent> finishSound;

  private final boolean shrinkStackInCreative;

  private final boolean returnItemInCreative;

  protected AbstractActionEntry(P properties) {
    this.shrinkStack = properties.shrinkStack;
    this.returnItem = properties.returnItem;
    this.finishSound = properties.finishSound;
    this.shrinkStackInCreative = properties.shrinkStackInCreative;
    this.returnItemInCreative = properties.returnItemInCreative;
  }

  @Override
  public boolean shouldShrinkStack(ILiving<?, ?> performer) {
    return (!this.shrinkStackInCreative && performer.getEntity() instanceof PlayerEntity
        && ((PlayerEntity) performer.getEntity()).isCreative()) ? false : this.shrinkStack;
  }

  @Override
  public IItemProvider getReturnItem(ILiving<?, ?> performer) {
    return (!this.returnItemInCreative && performer.getEntity() instanceof PlayerEntity
        && ((PlayerEntity) performer.getEntity()).isCreative()) ? null : this.returnItem;
  }

  @Override
  public SoundEvent getFinishSound() {
    return this.finishSound == null ? null : this.finishSound.get();
  }

  protected static abstract class Properties<SELF extends Properties<SELF>> {
    protected boolean shrinkStack = true;
    @Nullable
    protected IItemProvider returnItem;
    @Nullable
    protected Supplier<SoundEvent> finishSound;

    protected boolean shrinkStackInCreative;

    protected boolean returnItemInCreative;

    public SELF setShrinkStack(boolean shrinkStack) {
      this.shrinkStack = shrinkStack;
      return this.self();
    }

    public SELF setReturnItem(Supplier<Item> returnItem) {
      return this.setReturnItem(new IItemProvider() {
        @Override
        public Item asItem() {
          return returnItem.get();
        }
      });
    }

    public SELF setReturnItem(IItemProvider returnItem) {
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

    @SuppressWarnings("unchecked")
    protected SELF self() {
      return (SELF) this;
    }
  }
}
