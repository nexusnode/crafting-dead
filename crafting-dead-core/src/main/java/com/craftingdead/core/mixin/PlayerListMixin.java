package com.craftingdead.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.craftingdead.core.CraftingDead;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.management.PlayerList;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

  @Inject(at = @At("HEAD"), method = "initializeConnectionToPlayer", cancellable = true)
  private void renderItem(NetworkManager networkManager, ServerPlayerEntity playerEntity,
      CallbackInfo callbackInfo) {
    callbackInfo.cancel();
    CraftingDead.getInstance().getLogicalServer().initializeConnectionToPlayer(networkManager,
        playerEntity);
  }
}
