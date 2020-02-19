package com.craftingdead.mod.network.message.main;

import java.util.Optional;
import java.util.function.Supplier;
import com.craftingdead.mod.capability.ModCapabilities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

public class SyncGunMessage {

  private final int entityId;
  private final int ammoCount;

  public SyncGunMessage(int entityId, int ammoCount) {
    this.entityId = entityId;
    this.ammoCount = ammoCount;
  }

  public static void encode(SyncGunMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.entityId);
    out.writeVarInt(msg.ammoCount);
  }

  public static SyncGunMessage decode(PacketBuffer in) {
    int entityId = in.readVarInt();
    int size = in.readVarInt();
    return new SyncGunMessage(entityId, size);
  }

  public static void handle(SyncGunMessage msg, Supplier<NetworkEvent.Context> ctx) {
    Optional<World> world =
        LogicalSidedProvider.CLIENTWORLD.get(ctx.get().getDirection().getReceptionSide());
    world.map(w -> w.getEntityByID(msg.entityId)).ifPresent(entity -> {
      if (entity instanceof LivingEntity) {
        ItemStack heldStack = ((LivingEntity) entity).getHeldItemMainhand();
        heldStack.getCapability(ModCapabilities.GUN_CONTROLLER).ifPresent(gunController -> {
          gunController.setAmmo(msg.ammoCount);
        });
      }
    });
    ctx.get().setPacketHandled(true);
  }
}
