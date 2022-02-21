/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
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

package com.craftingdead.core.world.effect;

import java.util.function.Consumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.client.EffectRenderer;

public class FlashBlindnessMobEffect extends MobEffect {

  protected FlashBlindnessMobEffect() {
    super(MobEffectCategory.HARMFUL, 0xFFFFFF);
  }

  @Override
  public boolean isDurationEffectTick(int duration, int amplifier) {
    return true;
  }

  @Override
  public void initializeClient(Consumer<EffectRenderer> consumer) {
    consumer.accept(new EffectRenderer() {

      @Override
      public void renderInventoryEffect(MobEffectInstance effect,
          EffectRenderingInventoryScreen<?> gui, PoseStack mStack, int x, int y, float z) {}

      @Override
      public void renderHUDEffect(MobEffectInstance effect, GuiComponent gui, PoseStack mStack,
          int x, int y, float z, float alpha) {}

      @Override
      public boolean shouldRender(MobEffectInstance effect) {
        return false;
      }

      @Override
      public boolean shouldRenderHUD(MobEffectInstance effect) {
        return false;
      }

      @Override
      public boolean shouldRenderInvText(MobEffectInstance effect) {
        return false;
      }
    });
  }
}
