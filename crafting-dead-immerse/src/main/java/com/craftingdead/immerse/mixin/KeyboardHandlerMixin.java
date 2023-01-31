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

import java.util.List;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.craftingdead.immerse.CraftingDeadImmerse;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.rocketpowered.connector.client.gui.guild.GuildView;
import net.rocketpowered.sdk.Rocket;
import sm0keysa1m0n.bliss.minecraft.view.MinecraftViewScreen;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {

  @Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
  public void keyPress(long window, int keyCode, int scanCode, int action, int modifiers,
      CallbackInfo callbackInfo) {
    if (action == GLFW.GLFW_PRESS) {
      if (keyCode == GLFW.GLFW_KEY_TAB
          && (modifiers & GLFW.GLFW_MOD_SHIFT) == GLFW.GLFW_MOD_SHIFT
          && Rocket.gameClientInterface().isPresent()) {
        callbackInfo.cancel();
        var screen = new MinecraftViewScreen(TextComponent.EMPTY, new GuildView());
        screen.setStylesheets(List.of(new ResourceLocation(CraftingDeadImmerse.ID, "guild")));
        Minecraft.getInstance().setScreen(screen);
      }
    }
  }
}
