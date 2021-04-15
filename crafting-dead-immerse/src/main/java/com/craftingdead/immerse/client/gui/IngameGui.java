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

package com.craftingdead.immerse.client.gui;

import com.craftingdead.core.living.IPlayer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.util.Util;

public class IngameGui {

  private static final int KILLED_MESSAGE_LIFE_MS = 5 * 1000;

  private final Minecraft minecraft = Minecraft.getInstance();

  private long killedMessageVisibleTimeMs;

  private KilledMessage killedMessage;

  public void renderOverlay(IPlayer<AbstractClientPlayerEntity> player, MatrixStack matrixStack,
      int width, int height, float partialTicks) {
    this.renderKilledMessage(matrixStack, width, height);
  }

  private void renderKilledMessage(MatrixStack matrixStack, int width, int height) {
    if (this.killedMessage != null) {
      final long currentTime = Util.getMillis();
      if (currentTime - this.killedMessageVisibleTimeMs > KILLED_MESSAGE_LIFE_MS) {
        this.killedMessage = null;
        return;
      }
      this.killedMessage.render(matrixStack, this.minecraft.font, width, height);
    }
  }

  public void displayKilledMessage(KilledMessage killedMessage) {
    this.killedMessageVisibleTimeMs = Util.getMillis();
    this.killedMessage = killedMessage;
  }
}
