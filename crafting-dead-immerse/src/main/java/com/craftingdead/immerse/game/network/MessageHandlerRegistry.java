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

package com.craftingdead.immerse.game.network;

import java.util.Map;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraftforge.network.NetworkEvent;

public class MessageHandlerRegistry<T> {

  private final Map<Class<?>, MessageHandler<T, ?>> handlers = new Object2ObjectOpenHashMap<>();

  public <MSG> void register(Class<MSG> type, MessageHandler<T, MSG> handler) {
    this.handlers.put(type, handler);
  }

  public <MSG> void handle(T parent, MSG message, NetworkEvent.Context context) {
    @SuppressWarnings("unchecked")
    var handler = (MessageHandler<T, MSG>) this.handlers.get(message.getClass());
    if (handler == null) {
      throw new IllegalArgumentException(
          "No handler for message type: " + message.getClass().getName());
    }
    handler.handle(parent, message, context);
  }

  public static interface MessageHandler<T, MSG> {

    void handle(T parent, MSG message, NetworkEvent.Context context);
  }
}
