package com.craftingdead.core.network.message.main;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import com.craftingdead.core.capability.living.Player;
import com.craftingdead.core.network.util.GenericDataManager;
import com.craftingdead.core.network.util.GenericDataManager.DataEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

public class SyncPlayerMessage {

  private int entityId;
  private List<GenericDataManager.DataEntry<?>> dataManagerEntries;

  public SyncPlayerMessage(int entityId, List<DataEntry<?>> dataManagerEntries) {
    this.entityId = entityId;
    this.dataManagerEntries = dataManagerEntries;
  }

  public static void encode(SyncPlayerMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.entityId);
    try {
      GenericDataManager.writeEntries(msg.dataManagerEntries, out);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static SyncPlayerMessage decode(PacketBuffer in) {
    try {
      return new SyncPlayerMessage(in.readVarInt(), GenericDataManager.readEntries(in));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static boolean handle(SyncPlayerMessage msg, Supplier<NetworkEvent.Context> ctx) {
    Optional<World> world =
        LogicalSidedProvider.CLIENTWORLD.get(ctx.get().getDirection().getReceptionSide());
    world.flatMap(w -> Optional.ofNullable(w.getEntityByID(msg.entityId)))
        .filter(e -> e instanceof PlayerEntity)
        .ifPresent(e -> Player.get((PlayerEntity) e).getDataManager()
            .setEntryValues(msg.dataManagerEntries));
    return true;
  }
}
