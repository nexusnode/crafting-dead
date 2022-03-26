package com.craftingdead.core.network.message.play;

import com.craftingdead.core.world.action.Action.StopReason;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;

public record ActionCompletionSync() {

  public void encode(FriendlyByteBuf out) {
  }

  public static ActionCompletionSync decode(FriendlyByteBuf in) {
    return new ActionCompletionSync();
  }

  public boolean handle(Supplier<Context> ctx) {
    if (ctx.get().getDirection().getReceptionSide().isClient()) {
      ctx.get().enqueueWork(ActionCompletionSync::doClientSync);
    }
    return true;
  }

  private static void doClientSync() {
    var player = Objects.requireNonNull(PlayerExtension.get(Minecraft.getInstance().player));
    // We can't directly mark an action as completed, so we need to tell it directly that it was completed.
    player.getAction().ifPresent(action -> {
      action.stop(StopReason.COMPLETED);
      // And now we cancel it on the client to be sure it's not around anymore
      player.cancelAction(false);
    });
  }
}
