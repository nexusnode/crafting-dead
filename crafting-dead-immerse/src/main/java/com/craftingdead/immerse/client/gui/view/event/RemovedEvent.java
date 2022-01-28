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

package com.craftingdead.immerse.client.gui.view.event;

import net.minecraftforge.eventbus.api.Event;

public abstract class RemovedEvent extends Event {

  @HasResult
  public static class Pre extends RemovedEvent {

    private final Runnable remove;

    public Pre(Runnable remove) {
      this.remove = remove;
    }

    public Runnable getRemove() {
      return this.remove;
    }
  }

  public static class Post extends RemovedEvent {}
}
