package com.craftingdead.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.craftingdead.core.world.item.scope.Scope;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.Items;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

  @Redirect(at = @At(value = "INVOKE",
      target = "Lnet/minecraft/client/player/LocalPlayer;isScoping()Z"),
      method = "turnPlayer")
  public boolean isScoping(LocalPlayer player) {
    var vanillaScoping = player.isUsingItem() && player.getUseItem().is(Items.SPYGLASS);
    return vanillaScoping || player.getMainHandItem().getCapability(Scope.CAPABILITY)
        .map(scope -> scope.isScoping(player) && scope.shouldReduceMouseSensitivity(player))
        .orElse(false);
  }
}
