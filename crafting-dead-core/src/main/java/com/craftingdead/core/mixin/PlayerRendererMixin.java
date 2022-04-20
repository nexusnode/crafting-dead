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

package com.craftingdead.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.craftingdead.core.client.ClientDist;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin {

  /**
   * Renders first person arm with custom clothing.
   */
  @Inject(at = @At("RETURN"), method = "renderHand")
  private void renderHand(PoseStack poseStack, MultiBufferSource bufferSource,
      int packedLight, AbstractClientPlayer playerEntity, ModelPart arm,
      ModelPart sleeve, CallbackInfo callbackInfo) {
    final var playerRenderer = (PlayerRenderer) (Object) this;
    ClientDist.renderArmWithClothing(playerRenderer, poseStack, bufferSource, packedLight,
        playerEntity, arm, sleeve);
  }
}
