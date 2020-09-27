package com.craftingdead.core.network.util;

import java.io.IOException;
import net.minecraft.network.PacketBuffer;

public interface INetworkSerializable {

  void write(PacketBuffer packetBuffer, boolean writeAll) throws IOException;

  void read(PacketBuffer packetBuffer) throws IOException;

  boolean isDirty();
}
