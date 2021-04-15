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
import com.craftingdead.core.action.IAction;
import com.craftingdead.core.living.ILiving;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IGenericEvent;

public abstract class LivingEvent extends Event {

  private final ILiving<?, ?> living;

  public LivingEvent(ILiving<?, ?> living) {
    this.living = living;
  }

  public ILiving<?, ?> getLiving() {
    return this.living;
  }

  public static class Load extends LivingEvent {

    public Load(ILiving<?, ?> living) {
      super(living);
    }
  }

  @Cancelable
  public static class PerformAction<T extends IAction> extends LivingEvent
      implements IGenericEvent<T> {

    private final T action;

    public PerformAction(ILiving<?, ?> living, T action) {
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
