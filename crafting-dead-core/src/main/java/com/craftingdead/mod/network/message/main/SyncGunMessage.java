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
  private final int magazineSize;

  public SyncGunMessage(int entityId, int magazineSize) {
    this.entityId = entityId;
    this.magazineSize = magazineSize;
  }

  public static void encode(SyncGunMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.entityId);
    out.writeVarInt(msg.magazineSize);
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
        heldStack.getCapability(ModCapabilities.GUN).ifPresent(gunController -> {
          gunController.setMagazineSize(msg.magazineSize);
        });
      }
    });
    ctx.get().setPacketHandled(true);
  }
}
