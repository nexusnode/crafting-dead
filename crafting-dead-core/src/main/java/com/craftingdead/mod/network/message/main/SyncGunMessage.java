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
  private final ItemStack paintStack;
  private final ItemStack magazineStack;
  private final int magazineSize;

  public SyncGunMessage(int entityId, ItemStack paintStack, ItemStack magazineStack,
      int magazineSize) {
    this.entityId = entityId;
    this.paintStack = paintStack;
    this.magazineStack = magazineStack;
    this.magazineSize = magazineSize;
  }

  public static void encode(SyncGunMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.entityId);
    out.writeItemStack(msg.paintStack);
    out.writeItemStack(msg.magazineStack);
    out.writeVarInt(msg.magazineSize);
  }

  public static SyncGunMessage decode(PacketBuffer in) {
    int entityId = in.readVarInt();
    ItemStack paintStack = in.readItemStack();
    ItemStack magazineStack = in.readItemStack();
    int size = in.readVarInt();
    return new SyncGunMessage(entityId, paintStack, magazineStack, size);
  }

  public static boolean handle(SyncGunMessage msg, Supplier<NetworkEvent.Context> ctx) {
    Optional<World> world =
        LogicalSidedProvider.CLIENTWORLD.get(ctx.get().getDirection().getReceptionSide());
    world.map(w -> w.getEntityByID(msg.entityId)).ifPresent(entity -> {
      if (entity instanceof LivingEntity) {
        ItemStack heldStack = ((LivingEntity) entity).getHeldItemMainhand();
        heldStack.getCapability(ModCapabilities.GUN).ifPresent(gunController -> {
          gunController.setMagazineStack(msg.magazineStack);
          gunController.setPaintStack(msg.paintStack);
          gunController.setMagazineSize(msg.magazineSize);
        });
      }
    });
    return true;
  }
}
