package com.craftingdead.immerse.client.gui.component.serverentry;

import java.util.function.Consumer;

public interface IServerEntryReader {

  void read(Consumer<ServerEntry> entryConsumer);
}
