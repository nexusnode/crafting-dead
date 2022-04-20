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

package com.craftingdead.immerse.client.gui;

import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.Util;

public class IngameGui {

  private static final int KILLED_MESSAGE_LIFE_MS = 5 * 1000;

  private final Minecraft minecraft = Minecraft.getInstance();

  private long killedMessageVisibleTimeMs;

  private KilledMessage killedMessage;

  public void renderOverlay(PlayerExtension<AbstractClientPlayer> player, PoseStack matrixStack,
      int width, int height, float partialTicks) {
    this.renderKilledMessage(matrixStack, width, height);
  }

  private void renderKilledMessage(PoseStack matrixStack, int width, int height) {
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
