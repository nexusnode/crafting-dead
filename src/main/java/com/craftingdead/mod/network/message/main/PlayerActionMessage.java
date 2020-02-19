package com.craftingdead.mod.network.message.main;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.capability.player.IPlayer;
import com.craftingdead.mod.capability.player.ServerPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

public class PlayerActionMessage {

  private final int entityId;
  private final Action action;

  public PlayerActionMessage(int entityId, Action action) {
    this.entityId = entityId;
    this.action = action;
  }

  public static void encode(PlayerActionMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.entityId);
    out.writeEnumValue(msg.action);
  }

  public static PlayerActionMessage decode(PacketBuffer in) {
    return new PlayerActionMessage(in.readVarInt(), in.readEnumValue(Action.class));
  }

  public static void handle(PlayerActionMessage msg, Supplier<NetworkEvent.Context> ctx) {
    Entity entity = null;
    switch (ctx.get().getDirection().getReceptionSide()) {
      case CLIENT:
        Optional<World> world =
            LogicalSidedProvider.CLIENTWORLD.get(ctx.get().getDirection().getReceptionSide());
        entity = world.map(w -> w.getEntityByID(msg.entityId)).orElse(null);
        break;
      case SERVER:
        entity = ctx.get().getSender();
        break;
      default:
        break;
    }

    if (entity != null) {
      entity.getCapability(ModCapabilities.PLAYER).ifPresent(msg.action.playerConsumer);
    }
    ctx.get().setPacketHandled(true);
  }

  public static enum Action {
    TRIGGER_PRESSED(
        (player, isServer) -> player.setTriggerPressed(true, isServer)), TRIGGER_RELEASED(
            (player, isServer) -> player.setTriggerPressed(false, isServer)), RELOAD(
                IPlayer::reload), TOGGLE_AIMING(IPlayer::toggleAiming), TOGGLE_FIRE_MODE(
                    IPlayer::toggleFireMode), OPEN_PLAYER_CONTAINER(IPlayer::openPlayerContainer);

    private final NonNullConsumer<? super IPlayer<?>> playerConsumer;

    private Action(BiConsumer<IPlayer<?>, Boolean> action) {
      this.playerConsumer = player -> action.accept(player, player instanceof ServerPlayer);
    }

    private Action(NonNullConsumer<IPlayer<?>> action) {
      this.playerConsumer = action;
    }
  }
}
