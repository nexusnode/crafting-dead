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

package com.craftingdead.immerse.client.gui.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.PropertySetter;
import org.jdesktop.core.animation.timing.interpolators.LinearInterpolator;
import org.jdesktop.core.animation.timing.interpolators.SplineInterpolator;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL20;
import com.craftingdead.immerse.client.gui.view.event.ActionEvent;
import com.craftingdead.immerse.client.gui.view.event.AddedEvent;
import com.craftingdead.immerse.client.gui.view.event.CharTypeEvent;
import com.craftingdead.immerse.client.gui.view.event.FocusChangedEvent;
import com.craftingdead.immerse.client.gui.view.event.KeyEvent;
import com.craftingdead.immerse.client.gui.view.event.MouseEnterEvent;
import com.craftingdead.immerse.client.gui.view.event.MouseEvent;
import com.craftingdead.immerse.client.gui.view.event.MouseLeaveEvent;
import com.craftingdead.immerse.client.gui.view.event.RemovedEvent;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.craftingdead.immerse.client.shader.ShaderPrograms;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;

public class View<SELF extends View<SELF, L>, L extends Layout> extends GuiComponent
    implements GuiEventListener, Widget, Comparable<View<?, ?>> {

  public static final boolean DEBUG = true;

  private static final int SCROLLBAR_WIDTH = 4;

  private static final float SCROLL_CHUNK = 50F;

  private static final float SCROLL_MOMENTUM_DAMPING = 3F;

  private static final int DOUBLE_CLICK_DURATION_MS = 500;

  protected final Minecraft minecraft = Minecraft.getInstance();

  protected final Window window = this.minecraft.getWindow();

  private final IEventBus eventBus = BusBuilder.builder().build();

  protected final L layout;

  private final Set<State> states = new HashSet<>();

  private final Map<String, ValueStyleProperty<?>> valueProperties = new HashMap<>();

  @Nullable
  ViewScreen screen;

  @Nullable
  ParentView<?, ?, L> parent;
  int index;
  boolean pendingRemoval;
  protected boolean visible;

  private float lastScrollOffset;
  private float scrollOffset;
  private float scrollVelocity;

  private float fullHeight;

  private long lastClickTimeMs;

  private final ValueStyleProperty<Float> xScale =
      Util.make(ValueStyleProperty.create("x-scale", Float.class, 1.0F),
          this::registerValueProperty);
  private final ValueStyleProperty<Float> yScale =
      Util.make(ValueStyleProperty.create("y-scale", Float.class, 1.0F),
          this::registerValueProperty);
  private final CompositeStyleProperty<Float> scale =
      CompositeStyleProperty.create("scale", this.xScale, this.yScale);

  private final ValueStyleProperty<Float> xTranslation =
      Util.make(ValueStyleProperty.create("x-translation", Float.class, 0.0F),
          this::registerValueProperty);
  private final ValueStyleProperty<Float> yTranslation =
      Util.make(ValueStyleProperty.create("y-translation", Float.class, 0.0F),
          this::registerValueProperty);

  private final ValueStyleProperty<Float> alpha =
      Util.make(ValueStyleProperty.create("alpha", Float.class, 1.0F), this::registerValueProperty);

  private final ValueStyleProperty<Color> backgroundColor =
      Util.make(ValueStyleProperty.create("background-color", Color.class, Color.INVISIBLE),
          this::registerValueProperty);

  private final ValueStyleProperty<Float> outlineWidth =
      Util.make(ValueStyleProperty.create("outline-width", Float.class, 0.0F),
          this::registerValueProperty);
  private final ValueStyleProperty<Color> outlineColor =
      Util.make(ValueStyleProperty.create("outline-color", Color.class, Color.BLACK),
          this::registerValueProperty);

  private final ValueStyleProperty<Float> borderRadius =
      Util.make(ValueStyleProperty.create("border-radius", Float.class, 0.0F),
          this::registerValueProperty);
  private final ValueStyleProperty<Color> leftBorderColor =
      Util.make(ValueStyleProperty.create("border-left-color", Color.class, Color.BLACK),
          this::registerValueProperty);
  private final ValueStyleProperty<Color> rightBorderColor =
      Util.make(ValueStyleProperty.create("border-right-color", Color.class, Color.BLACK),
          this::registerValueProperty);
  private final ValueStyleProperty<Color> topBorderColor =
      Util.make(ValueStyleProperty.create("border-top-color", Color.class, Color.BLACK),
          this::registerValueProperty);
  private final ValueStyleProperty<Color> bottomBorderColor =
      Util.make(ValueStyleProperty.create("border-bottom-color", Color.class, Color.BLACK),
          this::registerValueProperty);
  private final CompositeStyleProperty<Color> borderColor =
      CompositeStyleProperty.create("border-color", this.leftBorderColor, this.rightBorderColor,
          this.topBorderColor, this.bottomBorderColor);

  private int zIndex = 0;

  @Nullable
  private Tooltip tooltip;

  @Nullable
  private Blur backgroundBlur;

  private boolean focusable;

  private boolean unscaleWidth;
  private boolean unscaleHeight;
  private boolean unscaleBorder = true;
  private boolean unscaleOutline = true;

  private boolean doubleClick;

  public View(L layout) {
    this.layout = layout;
    this.eventBus.start();

    this.states.add(States.ENABLED);

    this.outlineWidth
        .defineState(1.0F, States.FOCUSED)
        .defineState(1.0F, States.HOVERED, States.FOCUSED);
    this.outlineColor.defineState(Color.BLUE_C, States.FOCUSED);
  }

  protected boolean hasState(State state) {
    return this.states.contains(state);
  }

  protected boolean toggleState(State state) {
    if (!this.addState(state)) {
      this.removeState(state);
      return false;
    }
    return true;
  }

  protected boolean addState(State state) {
    return this.states.add(state);
  }

  protected boolean removeState(State state) {
    return this.states.remove(state);
  }

  protected void registerValueProperty(ValueStyleProperty<?> property) {
    this.valueProperties.put(property.getName(), property);
  }

  protected void updateProperties(boolean instant) {
    List<Set<State>> powerSet = new ArrayList<>(Sets.powerSet(this.states));
    Collections.reverse(powerSet);
    for (ValueStyleProperty<?> property : this.valueProperties.values()) {
      for (Set<State> subset : powerSet) {
        if (property.transition(subset, instant)) {
          break;
        }
      }
    }
  }

  protected float computeFullHeight() {
    return this.getContentHeight();
  }

  protected void added() {
    this.updateProperties(true);
    this.post(new AddedEvent());
  }

  protected void removed() {
    this.post(new RemovedEvent.Post());
  }

  protected void queueRemoval(Runnable remove) {
    if (this.post(new RemovedEvent.Pre(remove)) == Event.Result.DEFAULT) {
      remove.run();
    }
  }

  protected void layout() {
    this.fullHeight = this.computeFullHeight();
    this.scrollOffset = this.clampScrollOffset(this.scrollOffset);
  }

  public void tick() {
    this.lastScrollOffset = this.scrollOffset;

    this.scrollVelocity *= (1.0F / SCROLL_MOMENTUM_DAMPING);
    if (Math.abs(this.scrollVelocity) < 0.08F) {
      this.scrollVelocity = 0.0F;
    }

    this.scrollOffset += this.scrollVelocity;

    if (this.scrollOffset < 0.0F
        || this.scrollOffset > this.fullHeight - this.getContentHeight()) {
      this.scrollVelocity = 0.0F;
    }
    this.scrollOffset = this.clampScrollOffset(this.scrollOffset);

    if (this.backgroundBlur != null) {
      this.backgroundBlur.tick();
    }
  }

  @Override
  public final void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
    final float topBorderWidth = this.unscale(this.layout.getTopBorder(), this.unscaleBorder);
    final float bottomBorderWidth = this.unscale(this.layout.getBottomBorder(), this.unscaleBorder);
    final float leftBorderWidth = this.unscale(this.layout.getLeftBorder(), this.unscaleBorder);
    final float rightBorderWidth = this.unscale(this.layout.getRightBorder(), this.unscaleBorder);
    final float borderRadius = this.unscale(this.borderRadius.get(), this.unscaleBorder);

    final float outlineWidth = this.unscale(this.outlineWidth.get(), this.unscaleOutline);
    final float[] outlineColor = this.outlineColor.get().getValue4f();



    if (this.backgroundBlur != null) {
      this.backgroundBlur.renderToTempTarget(partialTicks);
      if (borderRadius > 0.0F) {
        ShaderPrograms.ROUNDED_TEX.use();
        GL20.glUniform4f(0, borderRadius, borderRadius, borderRadius, borderRadius);
        GL20.glUniform2f(1, this.getScaledX() - outlineWidth, this.getScaledY() - outlineWidth);
        GL20.glUniform2f(2, this.getScaledWidth() + (outlineWidth * 2.0F),
            this.getScaledHeight() + (outlineWidth * 2.0F));
      }
      RenderSystem.enableBlend();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.getAlpha());
      this.backgroundBlur.render(matrixStack, this.getScaledX() - outlineWidth,
          this.getScaledY() - outlineWidth,
          this.getScaledWidth() + (outlineWidth * 2.0F),
          this.getScaledHeight() + (outlineWidth * 2.0F));
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.disableBlend();
      if (borderRadius > 0.0F) {
        RenderUtil.resetShader();
      }
    }

    ShaderPrograms.ROUNDED_RECT.use();
    GL20.glUniform4f(0, borderRadius, borderRadius, borderRadius, borderRadius);
    GL20.glUniform2f(1, this.getScaledX() - outlineWidth, this.getScaledY() - outlineWidth);
    GL20.glUniform2f(2, this.getScaledWidth() + (outlineWidth * 2.0F),
        this.getScaledHeight() + (outlineWidth * 2.0F));
    GL20.glUniform1f(3, outlineWidth);
    GL20.glUniform4fv(4, outlineColor);

    if (this.backgroundColor.isDefined()) {
      float[] color = this.backgroundColor.get().getValue4f();
      color[3] *= this.getAlpha();
      float x = this.getScaledX() - outlineWidth;
      float y = this.getScaledY() - outlineWidth;
      float x2 = this.getScaledX() + this.getScaledWidth() + outlineWidth;
      float y2 = this.getScaledY() + this.getScaledHeight() + outlineWidth;
      RenderUtil.fill(matrixStack.last().pose(), x, y,
          this.getZOffset(), x2, y2, color[0], color[1], color[2], color[3]);
    }

    RenderSystem.disableTexture();
    RenderSystem.enableBlend();
    RenderSystem.defaultBlendFunc();

    if (leftBorderWidth > 0F) {
      float x2 = this.getScaledX() + leftBorderWidth;
      float x = this.getScaledX();
      float y2 = this.getScaledY() + this.getScaledHeight();

      float[] color = this.leftBorderColor.get().getValue4f();

      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder builder = tessellator.getBuilder();
      builder.begin(7, DefaultVertexFormats.POSITION_COLOR);
      builder.vertex(x, y2, this.getZOffset())
          .color(color[0], color[1], color[2], color[3])
          .endVertex();
      builder.vertex(x2, y2 - bottomBorderWidth, this.getZOffset())
          .color(color[0], color[1], color[2], color[3])
          .endVertex();
      builder.vertex(x2, this.getScaledY() + topBorderWidth, this.getZOffset())
          .color(color[0], color[1], color[2], color[3])
          .endVertex();
      builder.vertex(x, this.getScaledY(), this.getZOffset())
          .color(color[0], color[1], color[2], color[3])
          .endVertex();
      tessellator.end();
    }
    if (rightBorderWidth > 0F) {
      float x2 = this.getScaledX() + this.getScaledWidth();
      float x = x2 - rightBorderWidth;
      float y2 = this.getScaledY() + this.getScaledHeight();

      float[] color = this.rightBorderColor.get().getValue4f();

      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder builder = tessellator.getBuilder();
      builder.begin(7, DefaultVertexFormats.POSITION_COLOR);
      builder.vertex(x, y2 - bottomBorderWidth, this.getZOffset())
          .color(color[0], color[1], color[2], color[3])
          .endVertex();
      builder.vertex(x2, y2, this.getZOffset())
          .color(color[0], color[1], color[2], color[3])
          .endVertex();
      builder.vertex(x2, this.getScaledY(), this.getZOffset())
          .color(color[0], color[1], color[2], color[3])
          .endVertex();
      builder.vertex(x, this.getScaledY() + topBorderWidth, this.getZOffset())
          .color(color[0], color[1], color[2], color[3])
          .endVertex();
      tessellator.end();
    }

    if (topBorderWidth > 0F) {
      float x2 = this.getScaledX() + this.getScaledWidth();
      float y2 = this.getScaledY() + topBorderWidth;

      float[] color = this.topBorderColor.get().getValue4f();

      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder builder = tessellator.getBuilder();
      builder.begin(7, DefaultVertexFormats.POSITION_COLOR);
      builder.vertex(this.getScaledX() + leftBorderWidth, y2, this.getZOffset())
          .color(color[0], color[1], color[2], color[3])
          .endVertex();
      builder.vertex(x2 - rightBorderWidth, y2, this.getZOffset())
          .color(color[0], color[1], color[2], color[3])
          .endVertex();
      builder.vertex(x2, this.getScaledY(), this.getZOffset())
          .color(color[0], color[1], color[2], color[3])
          .endVertex();
      builder.vertex(this.getScaledX(), this.getScaledY(), this.getZOffset())
          .color(color[0], color[1], color[2], color[3])
          .endVertex();
      tessellator.end();
    }
    if (bottomBorderWidth > 0F) {
      float x2 = this.getScaledX() + this.getScaledWidth();
      float y2 = this.getScaledY() + this.getScaledHeight();
      float y = y2 - bottomBorderWidth;

      float[] color = this.bottomBorderColor.get().getValue4f();

      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder builder = tessellator.getBuilder();
      builder.begin(7, DefaultVertexFormats.POSITION_COLOR);
      builder.vertex(this.getScaledX(), y2, this.getZOffset())
          .color(color[0], color[1], color[2], color[3])
          .endVertex();
      builder.vertex(x2, y2, this.getZOffset())
          .color(color[0], color[1], color[2], color[3])
          .endVertex();
      builder.vertex(x2 - rightBorderWidth, y, this.getZOffset())
          .color(color[0], color[1], color[2], color[3])
          .endVertex();
      builder.vertex(this.getScaledX() + leftBorderWidth, y, this.getZOffset())
          .color(color[0], color[1], color[2], color[3])
          .endVertex();
      tessellator.end();
    }


    RenderSystem.enableTexture();
    RenderSystem.disableBlend();

    RenderUtil.resetShader();


    if (this.tooltip != null && this.isHovered()) {
      this.tooltip.render(this.minecraft.font, matrixStack,
          10.0F + this.getX() + this.getWidth(), this.getY());
    }

    // ---- Render Content----

    final boolean scissor = this.layout.getOverflow().shouldScissor();
    if (scissor) {
      final double scale = this.window.getGuiScale();
      ScissorStack.push(
          (int) (this.getScaledX() * scale),
          (int) (this.window.getHeight()
              - (this.getScaledY() + this.getScaledHeight()) * scale),
          (int) (this.getScaledWidth() * scale),
          (int) (this.getScaledHeight() * scale));
    }

    this.renderContent(poseStack, mouseX, mouseY, partialTicks);

    if (scissor) {
      ScissorStack.pop();
    }

    // ---- Render Scrollbar ----

    if (this.isScrollbarEnabled()) {
      float scrollbarX = this.getScrollbarX();
      float scaledY = this.getScaledY();

      float scrollbarWidth = SCROLLBAR_WIDTH * this.getXScale();
      float scrollbarRadius = scrollbarWidth / 2.0F;

      float scrollbarX2 = scrollbarX + scrollbarWidth;
      float scaledY2 = scaledY + this.getScaledHeight();

      ShaderPrograms.ROUNDED_RECT.use();
      GL20.glUniform4f(0, scrollbarRadius, scrollbarRadius, scrollbarRadius, scrollbarRadius);
      GL20.glUniform1f(3, 0.0F); // No outline

      // Background
      GL20.glUniform2f(1, scrollbarX, scaledY);
      GL20.glUniform2f(2, scrollbarWidth, this.getScaledHeight());
      RenderUtil.fill(matrixStack, scrollbarX, scaledY, scrollbarX2, scaledY2, 0x40000000);

      // Bar
      float scrollbarY = this.getScrollbarY();
      float scrollbarY2 = scrollbarY + this.getScrollbarHeight();
      GL20.glUniform2f(1, scrollbarX, scrollbarY);
      GL20.glUniform2f(2, scrollbarWidth, this.getScrollbarHeight());
      RenderUtil.fill(matrixStack, scrollbarX, scrollbarY, scrollbarX2, scrollbarY2, 0x4CFFFFFF);

      RenderUtil.resetShader();
    }
  }

  protected final boolean isScrollbarEnabled() {
    return this.layout.getOverflow() == Overflow.SCROLL
        && this.fullHeight > this.getContentHeight();
  }

  @SuppressWarnings("unused")
  protected void renderContent(PoseStack matrixStack, int mouseX, int mouseY,
      float partialTicks) {


    if (DEBUG && this.isHovered()) {
      RenderUtil.fillWidthHeight(matrixStack, this.getScaledContentX(), this.getScaledContentY(),
          this.getScaledContentWidth(), this.getScaledContentHeight(), 0x333495eb);

      // Left border
      RenderUtil.fill(matrixStack, this.getScaledX(), this.getScaledY(),
          this.getScaledX() + this.unscale(this.layout.getLeftBorder(), this.unscaleBorder),
          this.getScaledY() + this.getScaledHeight(), 0x33e3b352);
      // Left padding
      RenderUtil.fill(matrixStack,
          this.getScaledX() + this.unscale(this.layout.getLeftBorder(), this.unscaleBorder),
          this.getScaledY() + this.unscale(this.layout.getTopBorder(), this.unscaleBorder),
          this.getScaledContentX(),
          this.getScaledY() + this.getScaledHeight()
              - this.unscale(this.layout.getBottomBorder(), this.unscaleBorder),
          0x3384ab05);

      // Right border
      RenderUtil.fill(matrixStack,
          this.getScaledX() + this.getScaledWidth()
              - this.unscale(this.layout.getRightBorder(), this.unscaleBorder),
          this.getScaledY(),
          this.getScaledX() + this.getScaledWidth(),
          this.getScaledY() + this.getScaledHeight(), 0x33e3b352);
      // Right padding
      RenderUtil.fill(matrixStack,
          this.getScaledX() + this.getScaledWidth()
              - this.unscale(this.layout.getRightBorder(), this.unscaleBorder)
              - this.layout.getRightPadding(),
          this.getScaledY() + this.unscale(this.layout.getTopBorder(), this.unscaleBorder),
          this.getScaledX() + this.getScaledWidth()
              - this.unscale(this.layout.getRightBorder(), this.unscaleBorder),
          this.getScaledY() + this.getScaledHeight()
              - this.unscale(this.layout.getBottomBorder(), this.unscaleBorder),
          0x3384ab05);

      // Top border
      RenderUtil.fill(matrixStack, this.getScaledX(),
          this.getScaledY(),
          this.getScaledX() + this.getScaledWidth(),
          this.getScaledY() + this.unscale(this.layout.getTopBorder(), this.unscaleBorder),
          0x33e3b352);
      // Top padding
      RenderUtil.fill(matrixStack,
          this.getScaledX() + this.unscale(this.layout.getLeftBorder(), this.unscaleBorder),
          this.getScaledY() + this.unscale(this.layout.getTopBorder(), this.unscaleBorder),
          this.getScaledX() + this.getScaledWidth()
              - this.unscale(this.layout.getRightBorder(), this.unscaleBorder),
          this.getScaledContentY(), 0x3384ab05);

      // Bottom border
      RenderUtil.fill(matrixStack, this.getScaledX(),
          this.getScaledY() + this.getScaledHeight()
              - this.unscale(this.layout.getBottomBorder(), this.unscaleBorder),
          this.getScaledX() + this.getScaledWidth(),
          this.getScaledY() + this.getScaledHeight(), 0x33e3b352);
      // Bottom padding
      RenderUtil.fill(matrixStack,
          this.getScaledX() + this.unscale(this.layout.getLeftBorder(), this.unscaleBorder),
          this.getScaledContentY() + this.getScaledContentHeight(),
          this.getScaledX() + this.getScaledWidth()
              - this.unscale(this.layout.getRightBorder(), this.unscaleBorder),
          this.getScaledY() + this.getScaledHeight()
              - this.unscale(this.layout.getBottomBorder(), this.unscaleBorder),
          0x3384ab05);
    }
  }

  private final float getScrollbarX() {
    return this.getScaledX() + this.getScaledWidth() - SCROLLBAR_WIDTH * this.getXScale();
  }

  private final float getScrollbarY() {
    return this.getScaledY()
        + (this.getScrollOffset(this.minecraft.getFrameTime()) / this.fullHeight)
            * this.getScaledHeight();
  }

  private final float getScrollOffset(float partialTicks) {
    return Mth.lerp(partialTicks, this.lastScrollOffset, this.scrollOffset);
  }

  // Visible for property getter in scrollTo animation.
  @VisibleForTesting
  public final float getScrollOffset() {
    return this.scrollOffset;
  }

  // Visible for property setter in scrollTo animation.
  @VisibleForTesting
  public final void setScrollOffset(float scrollOffset) {
    this.scrollOffset = scrollOffset;
  }

  private final float getScrollbarHeight() {
    return Mth.clamp(
        this.getScaledHeight() * (this.getContentHeight() / this.fullHeight),
        10.0F * this.getYScale(),
        this.getScaledHeight());
  }

  private final void scrollTo(float y, long durationMs) {
    new Animator.Builder()
        .addTarget(PropertySetter.getTargetTo(this, "scrollOffset",
            new SplineInterpolator(0.25, 0.1, 0.25, 1),
            this.clampScrollOffset(y)))
        .setDuration(durationMs, TimeUnit.MILLISECONDS)
        .build()
        .start();
  }

  private final float clampScrollOffset(float scrollOffset) {
    return Mth.clamp(scrollOffset, 0.0F, this.fullHeight - this.getContentHeight());
  }

  public void mouseEntered(double mouseX, double mouseY) {
    if (this.tooltip != null) {
      new Animator.Builder()
          .addTarget(Animation.forProperty(this.tooltip.getOpacityProperty())
              .to(255)
              .build())
          .setInterpolator(LinearInterpolator.getInstance())
          .setDuration(150L, TimeUnit.MILLISECONDS)
          .build()
          .start();

      new Animator.Builder()
          .addTarget(Animation.forProperty(this.tooltip.getTextOpacityProperty())
              .to(255)
              .build())
          .setInterpolator(LinearInterpolator.getInstance())
          .setStartDelay(75L, TimeUnit.MILLISECONDS)
          .setDuration(150L, TimeUnit.MILLISECONDS)
          .build()
          .start();
    }

    this.states.add(States.HOVERED);
    this.updateProperties(false);

    this.post(new MouseEnterEvent());
  }

  public void mouseLeft(double mouseX, double mouseY) {
    if (this.tooltip != null) {
      new Animator.Builder()
          .addTarget(Animation.forProperty(this.tooltip.getOpacityProperty())
              .to(0)
              .build())
          .addTarget(Animation.forProperty(this.tooltip.getTextOpacityProperty())
              .to(0)
              .build())
          .setInterpolator(LinearInterpolator.getInstance())
          .setDuration(250L, TimeUnit.MILLISECONDS)
          .build()
          .start();
    }

    this.states.remove(States.HOVERED);
    this.updateProperties(false);

    this.post(new MouseLeaveEvent());
  }

  @Override
  public void mouseMoved(double mouseX, double mouseY) {
    this.post(new MouseEvent.MoveEvent(mouseX, mouseY));
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (this.post(new MouseEvent.ButtonEvent(mouseX, mouseY, button,
        GLFW.GLFW_PRESS)) == Event.Result.ALLOW) {
      return true;
    }

    if (!this.isHovered()) {
      if (this.isFocused()) {
        this.removeState(States.FOCUSED);
        this.updateProperties(false);
      }
      return false;
    }

    if (!this.isFocused() && this.focusable && this.hasState(States.ENABLED)) {
      this.addState(States.FOCUSED);
      this.updateProperties(false);
    }

    // Clicked on scrollbar region
    if (this.isScrollbarEnabled()
        && mouseX >= this.getScaledX() + this.getScaledWidth() - SCROLLBAR_WIDTH
        && mouseX <= this.getScaledX() + this.getScaledWidth() && mouseY >= this
            .getScaledY()
        && mouseY <= this.getScaledY() + this.getScaledHeight()) {
      // Clicked on actual scrollbar
      if (mouseX >= this.getScrollbarX() && mouseX <= this.getScrollbarX() + SCROLLBAR_WIDTH
          && mouseY >= this.getScrollbarY()
          && mouseY <= this.getScrollbarY() + this.getScrollbarHeight()) {
      } else {
        this.scrollTo(this.scrollOffset
            + (mouseY > this.getScrollbarY() ? this.getScaledHeight() : -this.getScaledHeight()),
            200);
      }
      return true;
    }

    long currentTime = Util.getMillis();
    long deltaTime = currentTime - this.lastClickTimeMs;
    this.lastClickTimeMs = currentTime;
    return this.hasState(States.ENABLED)
        && (!this.doubleClick || deltaTime < DOUBLE_CLICK_DURATION_MS)
        && this.post(new ActionEvent()) == Event.Result.ALLOW;
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    return this.post(new MouseEvent.ButtonEvent(mouseX, mouseY, button,
        GLFW.GLFW_RELEASE)) == Event.Result.ALLOW;
  }

  @Override
  public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX,
      double deltaY) {
    if (this.isScrollbarEnabled() &&
        mouseY >= this.getScaledY() && mouseY <= this.getScaledY() + this.getScaledHeight()) {
      this.scrollOffset = this.clampScrollOffset(this.scrollOffset
          + ((float) deltaY) * this.fullHeight / this.getScaledHeight());
      return true;
    }
    return this.post(
        new MouseEvent.DragEvent(mouseX, mouseY, button, deltaX, deltaY)) == Event.Result.ALLOW;
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) {
    if (this.isScrollbarEnabled()) {
      this.scrollVelocity += (float) (-scrollDelta * SCROLL_CHUNK);
    }
    return this.post(
        new MouseEvent.ScrollEvent(mouseX, mouseY, scrollDelta)) == Event.Result.ALLOW;
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (this.post(
        new KeyEvent(keyCode, scanCode, GLFW.GLFW_PRESS, modifiers)) == Event.Result.ALLOW) {
      return true;
    }

    if (keyCode == GLFW.GLFW_KEY_ENTER && this.post(new ActionEvent()) == Event.Result.ALLOW) {
      return true;
    }

    return false;
  }

  @Override
  public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
    return this.post(
        new KeyEvent(keyCode, scanCode, GLFW.GLFW_RELEASE, modifiers)) == Event.Result.ALLOW;
  }

  @Override
  public boolean charTyped(char codePoint, int modifiers) {
    return this.post(new CharTypeEvent(codePoint, modifiers)) == Event.Result.ALLOW;
  }

  @Override
  public boolean changeFocus(boolean forward) {
    if (this.focusable && this.hasState(States.ENABLED)) {
      boolean focused = this.toggleState(States.FOCUSED);
      this.updateProperties(false);
      this.focusChanged();
      return focused;
    }
    return false;
  }

  protected void focusChanged() {
    this.post(new FocusChangedEvent());
  }

  /**
   * Checks if mouse is over a component based on mouse position. Doesn't check whether there are
   * other components over this one.
   * 
   * @see #isHovered()
   */
  @Override
  public boolean isMouseOver(double mouseX, double mouseY) {
    return mouseX > this.getScaledX() && mouseX < this.getScaledX() + this.getScaledWidth()
        && mouseY > this.getScaledY() && mouseY < this.getScaledY() + this.getScaledHeight();
  }

  public void close() {
    if (this.backgroundBlur != null) {
      this.backgroundBlur.close();
    }
    this.layout.close();
  }

  public final SELF setDisabledBackgroundColor(Color color) {
    this.backgroundColor.defineState(color, States.DISABLED);
    return this.self();
  }

  public final SELF addActionSound(SoundEvent soundEvent) {
    return this.addListener(ActionEvent.class, (component, event) -> this.minecraft
        .getSoundManager().play(SimpleSoundInstance.forUI(soundEvent, 1.0F)));
  }

  public final SELF addHoverSound(SoundEvent soundEvent) {
    return this.addListener(MouseEnterEvent.class, (component, event) -> this.minecraft
        .getSoundManager().play(SimpleSoundInstance.forUI(soundEvent, 1.0F)));
  }

  public final <T extends Event> SELF addListener(Class<T> eventType,
      BiConsumer<SELF, T> consumer) {
    return this.addListener(eventType, consumer, true);
  }

  public final <T extends Event> SELF addListener(Class<T> eventType,
      BiConsumer<SELF, T> consumer, boolean consumeEvent) {
    this.eventBus.addListener(EventPriority.NORMAL, true, eventType,
        event -> {
          if (event.hasResult() && consumeEvent) {
            event.setResult(Event.Result.ALLOW);
          }
          consumer.accept(this.self(), event);
        });
    return this.self();
  }

  /**
   * Submit the event for dispatch to appropriate listeners.
   *
   * @param event - The event to dispatch to listeners
   * @return true if the event was cancelled
   */
  public final Event.Result post(Event event) {
    this.eventBus.post(event);
    return event.getResult();
  }

  public final float getScaledContentX() {
    return this.getContentX()
        + (this.getContentWidth() - (this.getContentWidth() * this.xScale.get())) / 2.0F
        + (this.layout.getLeftPadding() - (this.layout.getLeftPadding() * this.xScale.get())) / 2.0F
        + (this.layout.getRightPadding() - (this.layout.getRightPadding() * this.xScale.get()))
            / 2.0F
        + (this.isScrollbarEnabled()
            ? (SCROLLBAR_WIDTH - (SCROLLBAR_WIDTH * this.xScale.get())) / 2.0F
            : 0.0F);
  }

  public float getContentX() {
    return this.getX()
        + this.layout.getLeftPadding() * this.getXScale()
        + this.unscale(this.layout.getLeftBorder(), this.unscaleBorder) * this.getXScale();
  }

  public final float getScaledX() {
    return this.getX() + (this.getWidth() - (this.getWidth() * this.xScale.get())) / 2.0F;
  }

  public final float getX() {
    final float left = this.layout.getLeft();
    return left
        + (left * this.getXScale()) - (left * this.xScale.get())
        + this.xTranslation.get()
        + (this.parent == null
            ? 0.0F
            : this.parent.getScaledContentX())
        + this.unscaleOffset(this.getWidth(), this.unscaleWidth);
  }

  public final float getScaledContentY() {
    return this.getContentY()
        + (this.getContentHeight() - (this.getContentHeight() * this.yScale.get())) / 2.0F
        + (this.layout.getTopPadding() - (this.layout.getTopPadding() * this.yScale.get())) / 2.0F
        + (this.layout.getBottomPadding() - (this.layout.getBottomPadding() * this.yScale.get()))
            / 2.0F;
  }

  public float getContentY() {
    return this.getY()
        + this.layout.getTopPadding() * this.getYScale()
        + this.unscale(this.layout.getTopBorder(), this.unscaleBorder) * this.getYScale()
        - (this.isScrollbarEnabled()
            ? this.getScrollOffset(this.minecraft.getFrameTime()) * this.getYScale()
            : 0.0F);
  }

  public final float getScaledY() {
    return this.getY() + (this.getHeight() - (this.getHeight() * this.yScale.get())) / 2.0F;
  }

  public final float getY() {
    final float top = this.layout.getTop();
    return top
        + (top * this.getYScale()) - (top * this.yScale.get())
        + this.yTranslation.get()
        + (this.parent == null
            ? 0.0F
            : this.parent.getScaledContentY())
        + (this.unscaleHeight
            ? this.getHeight() * (float) this.window.getGuiScale() - this.getHeight()
            : 0.0F);
  }

  public final float getScaledContentWidth() {
    return this.getContentWidth() * this.getXScale();
  }

  public float getContentWidth() {
    return this.getWidth()
        - (this.isScrollbarEnabled() ? SCROLLBAR_WIDTH : 0.0F)
        - this.layout.getRightPadding()
        - this.layout.getLeftPadding()
        - this.unscale(this.layout.getRightBorder(), this.unscaleBorder)
        - this.unscale(this.layout.getLeftBorder(), this.unscaleBorder);
  }

  public final float getScaledWidth() {
    return this.getWidth() * this.getXScale();
  }

  public final float getWidth() {
    return this.unscale(this.layout.getWidth(), this.unscaleWidth);
  }

  public final float getScaledContentHeight() {
    return this.getContentHeight() * this.getYScale();
  }

  public final float getContentHeight() {
    return this.getHeight()
        - this.layout.getBottomPadding()
        - this.layout.getTopPadding()
        - this.unscale(this.layout.getBottomBorder(), this.unscaleBorder)
        - this.unscale(this.layout.getTopBorder(), this.unscaleBorder);
  }

  public float getScaledHeight() {
    return this.getHeight() * this.getYScale();
  }

  public final float getHeight() {
    return this.unscale(this.layout.getHeight(), this.unscaleHeight);
  }

  public final ValueStyleProperty<Float> getXTranslationProperty() {
    return this.xTranslation;
  }

  public final ValueStyleProperty<Float> getYTranslationProperty() {
    return this.yTranslation;
  }

  public final float getZOffset() {
    return this.index + this.zIndex;
  }

  public final SELF setZOffset(int zIndex) {
    if (this.zIndex != zIndex) {
      this.zIndex = zIndex;
      if (this.parent != null) {
        this.parent.sortChildren();
      }
    }
    return this.self();
  }

  public final float getXScale() {
    return (this.parent == null ? 1.0F : this.parent.getXScale()) * this.xScale.get();
  }

  public final float getYScale() {
    return (this.parent == null ? 1.0F : this.parent.getYScale()) * this.yScale.get();
  }

  public final ValueStyleProperty<Float> getXScaleProperty() {
    return this.xScale;
  }

  public final ValueStyleProperty<Float> getYScaleProperty() {
    return this.yScale;
  }

  public final CompositeStyleProperty<Float> getScaleProperty() {
    return this.scale;
  }

  public final ValueStyleProperty<Color> getTopBorderColorProperty() {
    return this.topBorderColor;
  }

  public final ValueStyleProperty<Color> getRightBorderColorProperty() {
    return this.rightBorderColor;
  }

  public final ValueStyleProperty<Color> getBottomBorderColorProperty() {
    return this.bottomBorderColor;
  }

  public final ValueStyleProperty<Color> getLeftBorderColorProperty() {
    return this.leftBorderColor;
  }

  public final CompositeStyleProperty<Color> getBorderColorProperty() {
    return this.borderColor;
  }

  public final ValueStyleProperty<Float> getOutlineWidthProperty() {
    return this.outlineWidth;
  }

  public final ValueStyleProperty<Color> getOutlineColorProperty() {
    return this.outlineColor;
  }

  public final SELF setTooltip(@Nullable Tooltip tooltip) {
    this.tooltip = tooltip;
    return this.self();
  }

  public final ValueStyleProperty<Color> getBackgroundColorProperty() {
    return this.backgroundColor;
  }

  public final SELF setBackgroundBlur() {
    return this.setBackgroundBlur(0);
  }

  public final SELF setBackgroundBlur(float blurRadius) {
    if (this.backgroundBlur != null) {
      this.backgroundBlur.close();
    }
    this.backgroundBlur = this.minecraft.submit(() -> new Blur(blurRadius)).join();
    return this.self();
  }

  public final ValueStyleProperty<Float> getBorderRadiusProperty() {
    return this.borderRadius;
  }

  public final SELF setFocusable(boolean focusable) {
    this.focusable = focusable;
    return this.self();
  }

  public final SELF setDoubleClick(boolean doubleClick) {
    this.doubleClick = doubleClick;
    return this.self();
  }

  public final SELF setUnscaleWidth(boolean unscaleWidth) {
    this.unscaleWidth = unscaleWidth;
    return this.self();
  }

  public final SELF setUnscaleHeight(boolean unscaleHeight) {
    this.unscaleHeight = unscaleHeight;
    return this.self();
  }

  public final SELF setUnscaleBorder(boolean unscaleBorder) {
    this.unscaleBorder = unscaleBorder;
    return this.self();
  }

  public final SELF setUnscaleOutline(boolean unscaleOutline) {
    this.unscaleOutline = unscaleOutline;
    return this.self();
  }

  public final ValueStyleProperty<Float> getAlphaProperty() {
    return this.alpha;
  }

  public final float getAlpha() {
    return (this.parent == null ? 1.0F : this.parent.getAlpha()) * this.alpha.get();
  }

  public final SELF setEnabled(boolean enabled) {
    if (enabled) {
      this.addState(States.ENABLED);
      this.removeState(States.DISABLED);
    } else {
      this.removeState(States.ENABLED);
      this.addState(States.DISABLED);
      if (this.hasState(States.FOCUSED)) {
        this.removeState(States.FOCUSED);
      }
    }
    this.updateProperties(!this.visible);
    return this.self();
  }

  public final boolean isHovered() {
    return this.hasState(States.HOVERED);
  }

  public final boolean isFocused() {
    return this.hasState(States.FOCUSED);
  }

  public final boolean isSelected() {
    return this.hasState(States.SELECTED);
  }

  public SELF setSelected(boolean selected) {
    if (selected) {
      this.addState(States.SELECTED);
    } else {
      this.removeState(States.SELECTED);
    }
    this.updateProperties(!this.visible);
    return this.self();
  }

  public final SELF configure(Consumer<SELF> configurer) {
    configurer.accept(this.self());
    return this.self();
  }

  public final L getLayout() {
    return this.layout;
  }

  public final ViewScreen getScreen() {
    if (this.screen == null) {
      if (this.parent == null) {
        throw new UnsupportedOperationException("Root view has no screen");
      }
      return this.parent.getScreen();
    }
    return this.screen;
  }

  public final ParentView<?, ?, L> getParent() {
    return this.parent;
  }

  public final boolean isAdded() {
    return this.parent != null || this.screen != null;
  }

  protected SELF setVisible(boolean visible) {
    this.visible = visible;
    return this.self();
  }

  @Override
  public int compareTo(View<?, ?> another) {
    if (another == null) {
      return 1;
    }
    if (this.getZOffset() < another.getZOffset()) {
      return -1;
    } else if (this.getZOffset() > another.getZOffset()) {
      return 1;
    }
    return 0;
  }

  @SuppressWarnings("unchecked")
  protected final SELF self() {
    return (SELF) this;
  }

  protected final float unscaleOffset(float value, boolean condition) {
    return condition
        ? value * (float) this.window.getGuiScale() - value
        : 0.0F;
  }

  protected final float unscale(float value, boolean condition) {
    return condition ? value / (float) this.window.getGuiScale() : value;
  }
}
