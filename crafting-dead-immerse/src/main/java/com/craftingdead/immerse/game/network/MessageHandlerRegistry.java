/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
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
