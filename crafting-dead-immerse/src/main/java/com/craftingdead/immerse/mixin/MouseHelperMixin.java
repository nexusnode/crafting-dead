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
import net.minecraft.client.MouseHelper;
import net.rocketpowered.connector.client.gui.OverlayManager;

@Mixin(MouseHelper.class)
public class MouseHelperMixin {

  @Inject(method = "onMove", at = @At("HEAD"), cancellable = true)
  private void onMove(long window, double mouseX, double mouseY, CallbackInfo callbackInfo) {
//    if (OverlayManager.INSTANCE.isVisible()) {
//      callbackInfo.cancel();
//      OverlayManager.INSTANCE.mouseMoved(mouseX, mouseY);
//    }
  }
}
