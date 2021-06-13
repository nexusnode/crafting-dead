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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GameRenderer;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

  /**
   * Renders first person items for spectating entity.
   */
  @Inject(at = @At("RETURN"), method = "renderItemInHand")
  private void renderItemInHand(MatrixStack matrixStack, ActiveRenderInfo activeRenderInfo,
      float partialTicks, CallbackInfo callbackInfo) {
    final Minecraft mc = Minecraft.getInstance();
    if (mc.getCameraEntity() instanceof RemoteClientPlayerEntity) {
      AbstractClientPlayerEntity playerEntity =
          (AbstractClientPlayerEntity) mc.getCameraEntity();
      CraftingDeadImmerse.getInstance().getClientDist().getSpectatorRenderer()
          .renderItemInFirstPerson(partialTicks, matrixStack,
              mc.renderBuffers().bufferSource(), playerEntity,
              mc.getEntityRenderDispatcher().getPackedLightCoords(playerEntity, partialTicks));
    }
  }
}
