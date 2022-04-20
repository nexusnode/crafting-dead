/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

  /**
   * Renders first person items for spectating entity.
   */
  @Inject(at = @At("RETURN"), method = "renderItemInHand")
  private void renderItemInHand(PoseStack matrixStack, Camera activeRenderInfo,
      float partialTicks, CallbackInfo callbackInfo) {
    final Minecraft mc = Minecraft.getInstance();
    if (mc.getCameraEntity() instanceof RemotePlayer) {
      AbstractClientPlayer playerEntity =
          (AbstractClientPlayer) mc.getCameraEntity();
      CraftingDeadImmerse.getInstance().getClientDist().getSpectatorRenderer()
          .renderItemInFirstPerson(partialTicks, matrixStack,
              mc.renderBuffers().bufferSource(), playerEntity,
              mc.getEntityRenderDispatcher().getPackedLightCoords(playerEntity, partialTicks));
    }
  }
}
