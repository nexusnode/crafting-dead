package com.craftingdead.core.util;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;

public class BufUtil {

  public static void writeVec(PacketBuffer out, Vec3d vec) {
    out.writeDouble(vec.getX());
    out.writeDouble(vec.getY());
    out.writeDouble(vec.getZ());
  }

  public static Vec3d readVec(PacketBuffer in) {
    double x = in.readDouble();
    double y = in.readDouble();
    double z = in.readDouble();
    return new Vec3d(x, y, z);
  }
}
