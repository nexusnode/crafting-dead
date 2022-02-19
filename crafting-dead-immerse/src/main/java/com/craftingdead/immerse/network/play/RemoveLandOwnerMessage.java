package com.craftingdead.immerse.network.play;

import java.util.UUID;
import java.util.function.Supplier;
import com.craftingdead.immerse.world.level.extension.LevelExtension;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.network.NetworkEvent;

public record RemoveLandOwnerMessage(UUID landOwnerId) {

  public void encode(FriendlyByteBuf out) {
    out.writeUUID(this.landOwnerId);
  }

  public static RemoveLandOwnerMessage decode(FriendlyByteBuf in) {
    return new RemoveLandOwnerMessage(in.readUUID());
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    context.get().enqueueWork(() -> LogicalSidedProvider.CLIENTWORLD
        .get(context.get().getDirection().getReceptionSide())
        .map(LevelExtension::getOrThrow)
        .map(LevelExtension::getLandManager)
        .ifPresent(landManager -> landManager.removeLandOwner(this.landOwnerId)));
    return true;
  }
}
