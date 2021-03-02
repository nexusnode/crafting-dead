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
import com.craftingdead.core.CraftingDead;
import net.minecraft.client.Minecraft;
import net.minecraft.util.SharedConstants;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

  @Inject(method = "getWindowTitle", at = @At("HEAD"), cancellable = true)
  private void getWindowTitle(CallbackInfoReturnable<String> callbackInfo) {
    callbackInfo.setReturnValue("Minecraft " + SharedConstants.getVersion().getName() + " - "
        + "Crafting Dead " + CraftingDead.VERSION);
  }
}
