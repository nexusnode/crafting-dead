/*
 * Crafting Dead Copyright (C) 2021 NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
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
