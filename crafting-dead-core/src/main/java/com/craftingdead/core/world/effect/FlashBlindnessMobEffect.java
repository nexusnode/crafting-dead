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
