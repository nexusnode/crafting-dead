package com.craftingdead.core.network.message.main;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import com.craftingdead.core.capability.ModCapabilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class ValidateLivingHitMessage {

  private final Map<Long, Integer> hits;

  public ValidateLivingHitMessage(Map<Long, Integer> hits) {
    this.hits = hits;
  }

  public static void encode(ValidateLivingHitMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.hits.size());
    for (Map.Entry<Long, Integer> hit : msg.hits.entrySet()) {
      out.writeVarLong(hit.getKey());
      out.writeVarInt(hit.getValue());
    }
  }

  public static ValidateLivingHitMessage decode(PacketBuffer in) {
    final int hitsSize = in.readVarInt();
    Map<Long, Integer> hits = new HashMap<>();
    for (int i = 0; i < hitsSize; i++) {
      hits.put(in.readVarLong(), in.readVarInt());
    }
    return new ValidateLivingHitMessage(hits);
  }

  public static boolean handle(ValidateLivingHitMessage msg, Supplier<NetworkEvent.Context> ctx) {
    ServerPlayerEntity playerEntity = ctx.get().getSender();
    playerEntity.getCapability(ModCapabilities.LIVING).ifPresent(living -> {
      ItemStack heldStack = playerEntity.getHeldItemMainhand();
      heldStack.getCapability(ModCapabilities.GUN).ifPresent(gun -> {
        for (Map.Entry<Long, Integer> hit : msg.hits.entrySet()) {
          Entity hitEntity = playerEntity.getEntityWorld().getEntityByID(hit.getValue());
          hitEntity.getCapability(ModCapabilities.LIVING).ifPresent(hitLiving -> {
            gun.validateLivingHit(living, heldStack, hitLiving, hit.getKey());
          });
        }
      });
    });
    return true;
  }
}
