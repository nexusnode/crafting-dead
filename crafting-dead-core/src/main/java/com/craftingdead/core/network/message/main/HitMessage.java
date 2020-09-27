package com.craftingdead.core.network.message.main;

import java.util.function.Supplier;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.ClientDist;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.network.NetworkEvent;

public class HitMessage {

  private final Vec3d hitPos;
  private final boolean dead;

  public HitMessage(Vec3d hitPos, boolean dead) {
    this.hitPos = hitPos;
    this.dead = dead;
  }

  public static void encode(HitMessage msg, PacketBuffer out) {
    out.writeDouble(msg.hitPos.getX());
    out.writeDouble(msg.hitPos.getY());
    out.writeDouble(msg.hitPos.getZ());
    out.writeBoolean(msg.dead);
  }

  public static HitMessage decode(PacketBuffer in) {
    return new HitMessage(new Vec3d(in.readDouble(), in.readDouble(), in.readDouble()),
        in.readBoolean());
  }

  public static boolean handle(HitMessage msg, Supplier<NetworkEvent.Context> ctx) {
    if (ctx.get().getDirection().getReceptionSide().isClient()) {
      ctx.get().enqueueWork(() -> {
        ((ClientDist) CraftingDead.getInstance().getModDist()).getIngameGui().displayHitMarker(
            msg.hitPos, msg.dead ? 0xFFB30C00 : 0xFFFFFFFF);
        if (msg.dead) {
          final Minecraft minecraft = Minecraft.getInstance();
          minecraft.player.playSound(SoundEvents.BLOCK_ANVIL_PLACE, 5.0F, 1.0F);
        }
      });
    }
    return true;
  }
}
