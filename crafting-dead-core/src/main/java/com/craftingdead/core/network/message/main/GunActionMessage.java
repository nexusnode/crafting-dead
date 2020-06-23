package com.craftingdead.core.network.message.main;

import java.util.function.Supplier;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.network.util.NetworkUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class GunActionMessage {

  private final int entityId;

  private final boolean reloading;

  public GunActionMessage(int entityId, boolean reloading) {
    this.entityId = entityId;
    this.reloading = reloading;
  }

  public static void encode(GunActionMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.entityId);
    out.writeBoolean(msg.reloading);
  }

  public static GunActionMessage decode(PacketBuffer in) {
    return new GunActionMessage(in.readVarInt(), in.readBoolean());
  }

  public static boolean handle(GunActionMessage msg, Supplier<NetworkEvent.Context> ctx) {
    NetworkUtil
        .getEntity(ctx.get(), msg.entityId)
        .filter(entity -> entity instanceof LivingEntity)
        .ifPresent(entity -> {
          LivingEntity livingEntity = (LivingEntity) entity;
          ItemStack heldStack = livingEntity.getHeldItemMainhand();
          heldStack.getCapability(ModCapabilities.GUN).ifPresent(gun -> {
            if (msg.reloading) {
              gun.reload(entity, heldStack, ctx.get().getDirection().getReceptionSide().isServer());
            } else {
              gun
                  .removeMagazine(entity, heldStack,
                      ctx.get().getDirection().getReceptionSide().isServer());
            }
          });
        });
    return true;
  }
}
