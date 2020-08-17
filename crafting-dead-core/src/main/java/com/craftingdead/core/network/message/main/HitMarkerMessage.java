package com.craftingdead.core.network.message.main;

import java.util.function.Supplier;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.ClientDist;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.network.NetworkEvent;

public class HitMarkerMessage {

  private final Vec3d hitPos;
  private final boolean dead;

  public HitMarkerMessage(Vec3d hitPos, boolean dead) {
    this.hitPos = hitPos;
    this.dead = dead;
  }

  public static void encode(HitMarkerMessage msg, PacketBuffer out) {
    out.writeDouble(msg.hitPos.getX());
    out.writeDouble(msg.hitPos.getY());
    out.writeDouble(msg.hitPos.getZ());
    out.writeBoolean(msg.dead);
  }

  public static HitMarkerMessage decode(PacketBuffer in) {
    return new HitMarkerMessage(new Vec3d(in.readDouble(), in.readDouble(), in.readDouble()),
        in.readBoolean());
  }

  public static boolean handle(HitMarkerMessage msg, Supplier<NetworkEvent.Context> ctx) {
    ((ClientDist) CraftingDead.getInstance().getModDist()).getIngameGui().displayHitMarker(
        msg.hitPos, msg.dead ? 0xFFB30C00 : 0xFFFFFFFF);
    return true;
  }
}
