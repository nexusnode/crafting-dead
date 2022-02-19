/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.game;

import java.util.function.Consumer;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.game.module.GameModule;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;

public interface GameClient extends Game {

  default void registerClientModules(Consumer<GameModule> consumer) {};

  /**
   * Disable off-hand usage.
   * 
   * @return <code>true</code> to disable, false</code> otherwise
   */
  default boolean disableSwapHands() {
    return false;
  }

  /**
   * Render a custom in-game overlay.
   * 
   * @param player - the local player
   * @param poseStack - the {@link MatrixStack}
   * @param width - screen width
   * @param height - screen height
   * @param partialTick - partialTicks
   * @return <code>true</code> to remove the vanilla overlay, <code>false</code> otherwise
   */
  default boolean renderOverlay(PlayerExtension<? extends AbstractClientPlayer> player,
      PoseStack poseStack, int width, int height, float partialTick) {
    return false;
  }

  /**
   * Render a custom player list.
   * 
   * @param player - the local player
   * @param poseStack - the {@link MatrixStack}
   * @param width - screen width
   * @param height - screen height
   * @param partialTick - partialTicks
   * @return <code>true</code> to remove the vanilla player list, <code>false</code> otherwise
   */
  default boolean renderPlayerList(PlayerExtension<? extends AbstractClientPlayer> player,
      PoseStack poseStack, int width, int height, float partialTick) {
    return false;
  }
}
