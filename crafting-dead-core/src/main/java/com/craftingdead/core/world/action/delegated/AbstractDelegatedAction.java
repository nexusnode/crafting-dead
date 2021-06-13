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
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;

public abstract class AbstractDelegatedAction<T extends AbstractDelegatedActionType>
    implements DelegatedAction {

  protected final T type;

  public AbstractDelegatedAction(T type) {
    this.type = type;
  }

  @Override
  public boolean shouldShrinkStack(LivingExtension<?, ?> performer) {
    return (!this.type.isShrinkStackInCreative() && performer.getEntity() instanceof PlayerEntity
        && ((PlayerEntity) performer.getEntity()).isCreative()) ? false : this.type.isShrinkStack();
  }

  @Override
  public Optional<Item> getReturnItem(LivingExtension<?, ?> performer) {
    return (!this.type.isReturnItemInCreative() && performer.getEntity() instanceof PlayerEntity
        && ((PlayerEntity) performer.getEntity()).isCreative())
            ? Optional.empty()
            : this.type.getReturnItem();
  }

  @Override
  public Optional<SoundEvent> getFinishSound() {
    return this.type.getFinishSound();
  }
}
