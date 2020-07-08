package com.craftingdead.core.network.message.main;

import java.util.function.Supplier;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.network.util.NetworkUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class ToggleFireModeMessage {

  private final int entityId;

  public ToggleFireModeMessage(int entityId) {
    this.entityId = entityId;
  }

  public static void encode(ToggleFireModeMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.entityId);
  }

  public static ToggleFireModeMessage decode(PacketBuffer in) {
    return new ToggleFireModeMessage(in.readVarInt());
  }

  public static boolean handle(ToggleFireModeMessage msg, Supplier<NetworkEvent.Context> ctx) {
    NetworkUtil
        .getEntity(ctx.get(), msg.entityId)
        .filter(entity -> entity instanceof LivingEntity)
        .ifPresent(entity -> {
          LivingEntity livingEntity = (LivingEntity) entity;
          ItemStack heldStack = livingEntity.getHeldItemMainhand();
          livingEntity.getCapability(ModCapabilities.LIVING)
              .ifPresent(living -> heldStack
                  .getCapability(ModCapabilities.GUN)
                  .ifPresent(gun -> gun
                      .toggleFireMode(living,
                          ctx.get().getDirection().getReceptionSide().isServer())));
        });
    return true;
  }
}
