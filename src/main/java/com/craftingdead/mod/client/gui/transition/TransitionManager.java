package com.craftingdead.mod.client.gui.transition;

import com.craftingdead.mod.client.gui.screen.ModScreen;
import com.craftingdead.mod.client.util.RenderUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.Util;

public class TransitionManager {
  /**
   * {@link Minecraft} instance
   */
  private final Minecraft minecraft;
  /**
   * The default {@link Transitions} to use if no others are available
   */
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

  public boolean checkDrawTransition(int mouseX, int mouseY, float partialTicks, Screen screen) {
    ITransition transition =
        screen instanceof ModScreen ? ((ModScreen) screen).getTransition() : this.defaultTransition;

    // Blending issues in world
    if (this.minecraft.world != null || transition == null)
      return false;

    if (screen != this.lastScreen) {
      this.transitionProgress = 0.0F;
      this.transitionBeginTime = Util.milliTime();
      this.transitionFramebuffer = new Framebuffer(this.minecraft.getWindow().getFramebufferWidth(),
          this.minecraft.getWindow().getFramebufferHeight(), true, Minecraft.IS_RUNNING_ON_MAC);
      this.transitionFramebuffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
      this.lastScreen = screen;
    } else {
      long deltaTime = Util.milliTime() - this.transitionBeginTime;
      float percentComplete = deltaTime / (transition.getTransitionTime() * 2.0F);
      this.transitionProgress = transition.getTransitionType().interpolate(percentComplete);
    }

    if (this.transitionProgress < 1.0F) {
      this.renderTransition(mouseX, mouseY, partialTicks, screen, transition);
      return true;
    }

    return false;
  }

  private void renderTransition(int mouseX, int mouseY, float partialTicks, Screen screen,
      ITransition transition) {
    RenderSystem.pushMatrix();
    {
      this.minecraft.getFramebuffer().unbindFramebuffer();

      this.transitionFramebuffer.framebufferClear(Minecraft.IS_RUNNING_ON_MAC);
      this.transitionFramebuffer.bindFramebuffer(false);

      RenderSystem.pushMatrix();
      {
        screen.render(mouseX, mouseY, partialTicks);
      }
      RenderSystem.popMatrix();
      this.transitionFramebuffer.unbindFramebuffer();

      this.minecraft.getFramebuffer().bindFramebuffer(false);
    }
    RenderSystem.popMatrix();

    MainWindow window = this.minecraft.getWindow();
    double width = window.getScaledWidth();
    double height = window.getScaledHeight();

    RenderSystem.pushMatrix();
    {
      transition.transform(width, height, this.transitionProgress);
      RenderSystem.disableAlphaTest();
      RenderSystem.disableBlend();
      this.transitionFramebuffer.bindFramebufferTexture();
      RenderUtil.drawTexturedRectangle(0, 0, width, height, 0.0F, 0.0F, 1.0F, 1.0F);
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
