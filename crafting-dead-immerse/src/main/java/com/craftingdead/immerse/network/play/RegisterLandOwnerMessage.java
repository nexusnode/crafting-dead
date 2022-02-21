package com.craftingdead.immerse.network.play;

import java.util.function.Supplier;
import com.craftingdead.immerse.world.level.extension.LandOwner;
import com.craftingdead.immerse.world.level.extension.LevelExtension;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.network.NetworkEvent;

public record RegisterLandOwnerMessage(LandOwner landOwner) {

  public void encode(FriendlyByteBuf out) {
    out.writeWithCodec(LandOwner.NETWORK_CODEC, this.landOwner);
  }

  public static RegisterLandOwnerMessage decode(FriendlyByteBuf in) {
    return new RegisterLandOwnerMessage(in.readWithCodec(LandOwner.NETWORK_CODEC));
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    context.get().enqueueWork(() -> LogicalSidedProvider.CLIENTWORLD
        .get(context.get().getDirection().getReceptionSide())
        .map(LevelExtension::getOrThrow)
        .map(LevelExtension::getLandManager)
        .ifPresent(landManager -> landManager.registerLandOwner(this.landOwner)));
    return true;
  }
}
