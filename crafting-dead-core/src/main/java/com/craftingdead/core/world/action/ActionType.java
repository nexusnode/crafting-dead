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

public class ActionType<T extends Action> extends ForgeRegistryEntry<ActionType<?>> {

  private final boolean triggeredByClient;
  private final IFactory<T> factory;

  public ActionType(boolean triggeredByClient, IFactory<T> factory) {
    this.triggeredByClient = triggeredByClient;
    this.factory = factory;
  }

  public T createAction(LivingExtension<?, ?> performer, @Nullable LivingExtension<?, ?> target) {
    return this.factory.create(this, performer, target);
  }

  public boolean isTriggeredByClient() {
    return this.triggeredByClient;
  }

  public interface IFactory<T extends Action> {
    T create(ActionType<T> actionType, LivingExtension<?, ?> performer,
        @Nullable LivingExtension<?, ?> target);
  }
}
