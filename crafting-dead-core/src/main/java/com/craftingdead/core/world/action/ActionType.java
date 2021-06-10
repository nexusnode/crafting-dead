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

package com.craftingdead.core.world.action;

import javax.annotation.Nullable;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class ActionType extends ForgeRegistryEntry<ActionType> {

  private final boolean triggeredByClient;
  private final Factory<ActionType> factory;

  @SuppressWarnings("unchecked")
  public <SELF extends ActionType> ActionType(boolean triggeredByClient,
      Factory<? super SELF> factory) {
    this.triggeredByClient = triggeredByClient;
    this.factory = (Factory<ActionType>) factory;
  }

  public Action createAction(LivingExtension<?, ?> performer,
      @Nullable LivingExtension<?, ?> target) {
    return this.factory.create(this, performer, target);
  }

  public boolean isTriggeredByClient() {
    return this.triggeredByClient;
  }

  public interface Factory<T extends ActionType> {
    Action create(T actionType, LivingExtension<?, ?> performer,
        @Nullable LivingExtension<?, ?> target);
  }
}
