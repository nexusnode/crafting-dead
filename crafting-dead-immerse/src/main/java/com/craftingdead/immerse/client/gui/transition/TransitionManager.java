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

package com.craftingdead.immerse.client.gui.transition;

import org.lwjgl.opengl.GL11;
import com.craftingdead.immerse.client.gui.menu.ModScreen;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.Util;

// TODO go over this
public class TransitionManager {
  /**
   * {@link Minecraft} instance
   */
  private final Minecraft minecraft;
  /**
   * The default {@link Transitions} to use if no others are available
   */
  @SuppressWarnings("unused")
  private final ITransition defaultTransition;
  /**
   * The {@link Framebuffer} used to draw the transition to
   */
  private Framebuffer transitionFramebuffer;
  /**
   * The last recorded {@link GuiScreen} - used to check if the current screen has changed
   */
  private Screen lastScreen;
  /**
   * The time at which the transition began - used to calculate the delta time
   */
  private long transitionBeginTime;
  /**
   * The progress of the current transition - ranges from 0.0F to 1.0F
   */
  private float transitionProgress;

  public TransitionManager(Minecraft minecraft, ITransition defaultTransition) {
    this.minecraft = minecraft;
    this.defaultTransition = defaultTransition;
  }

  public boolean checkDrawTransition(MatrixStack matrixStack, int mouseX, int mouseY,
      float partialTicks, Screen screen) {
    ITransition transition =
        screen instanceof ModScreen ? ((ModScreen) screen).getTransition() : null;

    // Blending issues in world
    if (this.minecraft.world != null || transition == null) {
      return false;
    }

    if (screen != this.lastScreen) {
      this.transitionProgress = 0.0F;
      this.transitionBeginTime = Util.milliTime();
      this.transitionFramebuffer = new Framebuffer(
          this.minecraft.getMainWindow().getFramebufferWidth(),
          this.minecraft.getMainWindow().getFramebufferHeight(), true, Minecraft.IS_RUNNING_ON_MAC);
      this.transitionFramebuffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
      this.lastScreen = screen;
    } else {
      long deltaTime = Util.milliTime() - this.transitionBeginTime;
      float percentComplete = deltaTime / (transition.getTransitionTime() * 2.0F);
      this.transitionProgress = transition.getTransitionType().interpolate(percentComplete);
    }

    if (this.transitionProgress < 1.0F) {
      this.renderTransition(matrixStack, mouseX, mouseY, partialTicks, screen, transition);
      return true;
    }

    return false;
  }

  @SuppressWarnings("deprecation")
  private void renderTransition(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks,
      Screen screen,
      ITransition transition) {
    RenderSystem.pushMatrix();
    {
      this.minecraft.getFramebuffer().unbindFramebuffer();

      this.transitionFramebuffer.framebufferClear(Minecraft.IS_RUNNING_ON_MAC);
      this.transitionFramebuffer.bindFramebuffer(false);

      RenderSystem.pushMatrix();
      {
        screen.render(matrixStack, mouseX, mouseY, partialTicks);
      }
      RenderSystem.popMatrix();
      this.transitionFramebuffer.unbindFramebuffer();

      this.minecraft.getFramebuffer().bindFramebuffer(false);
    }
    RenderSystem.popMatrix();

    RenderSystem.pushMatrix();
    {
      transition.transform(screen.width, screen.height, this.transitionProgress);
      this.transitionFramebuffer.bindFramebufferTexture();
      RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
      RenderUtil.blit(0, 0, screen.width, screen.height, 0.0F, 0.0F, 1.0F, 1.0F);
    }
    RenderSystem.popMatrix();
  }

  public static enum TransitionType {
    LINEAR {
      @Override
      public float interpolate(float percentComplete) {
        return Math.min(1.0F, percentComplete);
      }
    },
    SINE {
      @Override
      public float interpolate(float percentComplete) {
        return (float) Math.min(1.0F, Math.sin(Math.PI * Math.min(0.5f, percentComplete)));
      }
    },
    COSINE {
      @Override
      public float interpolate(float percentComplete) {
        return (float) Math.min(1.0F, 1.0 - Math.cos(Math.PI * Math.min(0.5F, percentComplete)));
      }
    },
    SMOOTH {
      @Override
      public float interpolate(float percentComplete) {
        return (float) Math
            .min(1.0, (-Math.cos(Math.PI * 2 * Math.min(0.5F, percentComplete)) + 1.0) * 0.5);
      }
    };

    abstract float interpolate(float percentComplete);
  }
}
