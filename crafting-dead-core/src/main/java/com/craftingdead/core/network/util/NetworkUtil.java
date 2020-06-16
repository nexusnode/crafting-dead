package com.craftingdead.core.network.util;

import java.util.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

public class NetworkUtil {

  public static Optional<Entity> getEntity(NetworkEvent.Context ctx, int entityId) {
    switch (ctx.getDirection().getReceptionSide()) {
      case CLIENT:
        Optional<World> world =
            LogicalSidedProvider.CLIENTWORLD.get(ctx.getDirection().getReceptionSide());
        return world.map(w -> w.getEntityByID(entityId));
      case SERVER:
        return Optional.ofNullable(ctx.getSender());
      default:
        return Optional.empty();
    }
  }
}
