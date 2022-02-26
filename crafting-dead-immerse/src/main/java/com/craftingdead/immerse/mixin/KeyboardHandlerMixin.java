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

import java.util.List;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.gui.view.ViewScreen;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.rocketpowered.connector.RocketConnector;
import net.rocketpowered.connector.client.gui.guild.GuildView;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {

  @Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
  public void keyPress(long window, int keyCode, int scanCode, int action, int modifiers,
      CallbackInfo callbackInfo) {
    if (action == GLFW.GLFW_PRESS) {
      if (keyCode == GLFW.GLFW_KEY_TAB
          && (modifiers & GLFW.GLFW_MOD_SHIFT) == GLFW.GLFW_MOD_SHIFT
          && RocketConnector.getInstance().getConnectionManager().getConnection().isPresent()) {
        callbackInfo.cancel();
        var screen = new ViewScreen(TextComponent.EMPTY, new GuildView());
        screen.setStylesheets(
            List.of(new ResourceLocation(CraftingDeadImmerse.ID, "css/guild.css")));
        Minecraft.getInstance().setScreen(screen);
      }
    }
  }
}
