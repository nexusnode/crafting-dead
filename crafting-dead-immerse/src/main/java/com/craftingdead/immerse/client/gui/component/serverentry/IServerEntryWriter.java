package com.craftingdead.immerse.client.gui.component.serverentry;

public interface IServerEntryWriter {

  void write(ServerEntry entry);

  void delete(ServerEntry entry);
}
