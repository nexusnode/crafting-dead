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

package com.craftingdead.core.event;

import java.lang.reflect.Type;
import com.craftingdead.core.world.action.Action;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IGenericEvent;

public abstract class LivingExtensionEvent extends Event {

  private final LivingExtension<?, ?> living;

  public LivingExtensionEvent(LivingExtension<?, ?> living) {
    this.living = living;
  }

  public LivingExtension<?, ?> getLiving() {
    return this.living;
  }

  public static class Load extends LivingExtensionEvent {

    public Load(LivingExtension<?, ?> living) {
      super(living);
    }
  }

  @Cancelable
  public static class PerformAction<T extends Action> extends LivingExtensionEvent
      implements IGenericEvent<T> {

    private final T action;

    public PerformAction(LivingExtension<?, ?> living, T action) {
      super(living);
      this.action = action;
    }

    public T getAction() {
      return this.action;
    }

    @Override
    public Type getGenericType() {
      return this.action.getClass();
    }
  }
}
