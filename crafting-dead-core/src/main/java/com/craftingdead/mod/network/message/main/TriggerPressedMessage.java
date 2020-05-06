package com.craftingdead.mod.network.message.main;

import java.util.function.Supplier;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.network.util.NetworkUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class TriggerPressedMessage {

  private final int entityId;
  private final boolean triggerPressed;

  public TriggerPressedMessage(int entityId, boolean triggerPressed) {
    this.entityId = entityId;
    this.triggerPressed = triggerPressed;
  }

  public static void encode(TriggerPressedMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.entityId);
    out.writeBoolean(msg.triggerPressed);
  }

  public static TriggerPressedMessage decode(PacketBuffer in) {
    return new TriggerPressedMessage(in.readVarInt(), in.readBoolean());
  }

  public static void handle(TriggerPressedMessage msg, Supplier<NetworkEvent.Context> ctx) {
    NetworkUtil
        .getEntity(ctx.get(), msg.entityId)
        .filter(entity -> entity instanceof LivingEntity)
        .ifPresent(entity -> {
          LivingEntity livingEntity = (LivingEntity) entity;
          ItemStack heldStack = livingEntity.getHeldItemMainhand();
          heldStack
              .getCapability(ModCapabilities.GUN)
              .ifPresent(gun -> gun
                  .setTriggerPressed(entity, heldStack, msg.triggerPressed,
                      ctx.get().getDirection().getReceptionSide().isServer()));
        });
    ctx.get().setPacketHandled(true);
  }
}
