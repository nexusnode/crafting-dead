/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.immerse.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.living.IPlayer;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.game.IGameClient;
import com.craftingdead.immerse.game.team.ITeam;
import com.craftingdead.immerse.game.team.ITeamGame;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.util.ResourceLocation;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {

  @Inject(at = @At("HEAD"), method = "getEntityTexture", cancellable = true)
  private void getEntityTexture(AbstractClientPlayerEntity playerEntity,
      CallbackInfoReturnable<ResourceLocation> callbackInfo) {
    IGameClient game = CraftingDeadImmerse.getInstance().getClientDist().getGameClient();
    if (game != null && game instanceof ITeamGame) {
      ITeam team = playerEntity.getCapability(ModCapabilities.LIVING).<IPlayer<?>>cast().resolve()
          .flatMap(((ITeamGame<?>) game)::getPlayerTeam).orElse(null);
      if (team != null) {
        team.getSkin().ifPresent(callbackInfo::setReturnValue);
      }
    }
  }
}
