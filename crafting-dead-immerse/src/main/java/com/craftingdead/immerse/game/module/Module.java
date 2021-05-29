package com.craftingdead.immerse.game.module;

import net.minecraftforge.fml.network.NetworkEvent;

public interface Module {

  default void load() {}

  default void unload() {}

  default <MSG> void handleMessage(MSG message, NetworkEvent.Context context) {}

  ModuleType getType();

  interface Tickable {

    void tick();
  }
}
