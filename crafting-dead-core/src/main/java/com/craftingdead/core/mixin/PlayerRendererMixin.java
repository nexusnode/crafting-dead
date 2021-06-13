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

package com.craftingdead.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.craftingdead.core.client.ClientDist;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.model.ModelRenderer;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin {

  /**
   * Renders first person arm with custom clothing.
   */
  @Inject(at = @At("RETURN"), method = "renderHand")
  private void renderHand(MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer,
      int packedLight, AbstractClientPlayerEntity playerEntity, ModelRenderer armRenderer,
      ModelRenderer armwearRenderer,
      CallbackInfo callbackInfo) {
    final PlayerRenderer playerRenderer = (PlayerRenderer) (Object) this;
    ClientDist.renderArmWithClothing(playerRenderer, matrixStack, renderTypeBuffer, packedLight,
        playerEntity, armRenderer, armwearRenderer);
  }
}
