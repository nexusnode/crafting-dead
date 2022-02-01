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
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.sounds.SoundEvent;

public abstract class AbstractDelegateAction<T extends AbstractDelegateActionType>
    implements DelegateAction {

  protected final T type;

  public AbstractDelegateAction(T type) {
    this.type = type;
  }

  @Override
  public boolean shouldConsumeItem(LivingExtension<?, ?> performer) {
    return this.type.shouldConsumeItemInCreative()
        && !(performer.getEntity() instanceof Player player && player.isCreative())
        && this.type.shouldConsumeItem();
  }

  @Override
  public Optional<Item> getResultItem(LivingExtension<?, ?> performer) {
    return (!this.type.useResultItemInCreative()
        && performer.getEntity() instanceof Player player
        && player.isCreative()) ? Optional.empty() : this.type.getReturnItem();
  }

  @Override
  public Optional<SoundEvent> getFinishSound() {
    return this.type.getFinishSound();
  }
}
