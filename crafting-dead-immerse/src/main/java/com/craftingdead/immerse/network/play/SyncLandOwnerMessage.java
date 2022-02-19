package com.craftingdead.immerse.network.play;

import java.util.UUID;
import java.util.function.Supplier;
import com.craftingdead.immerse.world.level.extension.LevelExtension;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.network.NetworkEvent;

public record SyncLandOwnerMessage(UUID landOwnerId, CompoundTag tag) {

  public void encode(FriendlyByteBuf out) {
    out.writeUUID(this.landOwnerId);
    out.writeNbt(this.tag);
  }

  public static SyncLandOwnerMessage decode(FriendlyByteBuf in) {
    return new SyncLandOwnerMessage(in.readUUID(), in.readNbt());
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    context.get().enqueueWork(() -> LogicalSidedProvider.CLIENTWORLD
        .get(context.get().getDirection().getReceptionSide())
        .map(LevelExtension::getOrThrow)
        .map(LevelExtension::getLandManager)
        .ifPresent(landManager -> {
          var landOwner = landManager.getLandOwner(this.landOwnerId);
          if (landOwner == null) {
            throw new IllegalStateException(
                "Missing land owner: " + this.landOwnerId.toString());
          }
          landOwner.handleUpdateTag(this.tag);
        }));
    return true;
  }
}
