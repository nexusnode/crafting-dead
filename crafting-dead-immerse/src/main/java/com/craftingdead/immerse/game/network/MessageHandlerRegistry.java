package com.craftingdead.immerse.game.network;

import java.util.Map;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageHandlerRegistry<T> {

  private final Map<Class<?>, MessageHandler<T, ?>> handlers = new Object2ObjectOpenHashMap<>();

  public <MSG> void register(Class<MSG> type, MessageHandler<T, MSG> handler) {
    this.handlers.put(type, handler);
  }

  public <MSG> void handle(T parent, MSG message, NetworkEvent.Context context) {
    @SuppressWarnings("unchecked")
    MessageHandler<T, MSG> handler = (MessageHandler<T, MSG>) this.handlers.get(message.getClass());
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
