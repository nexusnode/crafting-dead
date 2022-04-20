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

package com.craftingdead.immerse.client.gui.view;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.PropertySetter;
import org.jdesktop.core.animation.timing.interpolators.LinearInterpolator;
import org.jdesktop.core.animation.timing.interpolators.SplineInterpolator;
import org.lwjgl.glfw.GLFW;
import com.craftingdead.immerse.client.ClientDist;
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
import com.craftingdead.immerse.client.gui.view.property.StyleableProperty;
import com.craftingdead.immerse.client.gui.view.state.StateManager;
import com.craftingdead.immerse.client.gui.view.state.States;
import com.craftingdead.immerse.client.gui.view.style.CascadeStyleable;
import com.craftingdead.immerse.client.gui.view.style.StyleHolder;
import com.craftingdead.immerse.client.gui.view.style.shorthand.ShorthandArgMapper;
import com.craftingdead.immerse.client.gui.view.style.shorthand.ShorthandDispatcher;
import com.craftingdead.immerse.client.gui.view.style.tree.StyleList;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableSet;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;

public class View extends GuiComponent
    implements GuiEventListener, Widget, Comparable<View>, CascadeStyleable {

  public static final boolean DEBUG = false;

  private static final int SCROLLBAR_WIDTH = 4;

  private static final float SCROLL_CHUNK = 50F;

  private static final float SCROLL_MOMENTUM_DAMPING = 3F;

  private static final int DOUBLE_CLICK_DURATION_MS = 500;

  protected final Minecraft minecraft = Minecraft.getInstance();

  protected final Window window = this.minecraft.getWindow();

  private final IEventBus eventBus = BusBuilder.builder().build();

  private final StyleHolder styleHolder = new StyleHolder(this);

  protected final StateManager stateManager = new StateManager();

  /*
   * ========== State ==========
   */

  @Nullable
  ViewScreen screen;
  @Nullable
  ParentView parent;
  int index;
  @Nullable
  private Layout layout;
  private float lastScrollOffset;
  private float scrollOffset;
  private float scrollVelocity;
  private float fullHeight;
  private long lastClickTimeMs;
  private boolean closed;
  private boolean added;

  /*
   * ========== Style ==========
   */

  private final StyleableProperty<Integer> zIndex =
      Util.make(StyleableProperty.create("z-index", Integer.class, 1, value -> {
        if (this.parent != null) {
          this.parent.sortChildren();
        }
      }), this::registerProperty);

  private final StyleableProperty<Float> xScale =
      Util.make(StyleableProperty.create("x-scale", Float.class, 1.0F),
          this::registerProperty);
  private final StyleableProperty<Float> yScale =
      Util.make(StyleableProperty.create("y-scale", Float.class, 1.0F),
          this::registerProperty);

  private final StyleableProperty<Float> xTranslation =
      Util.make(StyleableProperty.create("x-translation", Float.class, 0.0F),
          this::registerProperty);
  private final StyleableProperty<Float> yTranslation =
      Util.make(StyleableProperty.create("y-translation", Float.class, 0.0F),
          this::registerProperty);

  private final StyleableProperty<Float> alpha =
      Util.make(StyleableProperty.create("alpha", Float.class, 1.0F), this::registerProperty);

  private final StyleableProperty<Color> backgroundColor =
      Util.make(StyleableProperty.create("background-color", Color.class, Color.TRANSPARENT),
          this::registerProperty);

  private final StyleableProperty<Float> outlineWidth =
      Util.make(StyleableProperty.create("outline-width", Float.class, 0.0F),
          this::registerProperty);
  private final StyleableProperty<Color> outlineColor =
      Util.make(StyleableProperty.create("outline-color", Color.class, Color.BLACK),
          this::registerProperty);

  private final StyleableProperty<Float> borderRadius =
      Util.make(StyleableProperty.create("border-radius", Float.class, 0.0F),
          this::registerProperty);
  private final StyleableProperty<Color> leftBorderColor =
      Util.make(StyleableProperty.create("border-left-color", Color.class, Color.BLACK),
          this::registerProperty);
  private final StyleableProperty<Color> rightBorderColor =
      Util.make(StyleableProperty.create("border-right-color", Color.class, Color.BLACK),
          this::registerProperty);
  private final StyleableProperty<Color> topBorderColor =
      Util.make(StyleableProperty.create("border-top-color", Color.class, Color.BLACK),
          this::registerProperty);
  private final StyleableProperty<Color> bottomBorderColor =
      Util.make(StyleableProperty.create("border-bottom-color", Color.class, Color.BLACK),
          this::registerProperty);

  /*
   * ========== Properties ==========
   */
  @Nullable
  private final String id;
  private final Set<String> styleClasses;
  @Nullable
  private final Tooltip tooltip;
  @Nullable
  private final Blur backgroundBlur;
  private final boolean doubleClick;
  private final boolean unscaleWidth;
  private final boolean unscaleHeight;
  private final boolean unscaleBorder;
  private final boolean unscaleOutline;
  private final boolean focusable;

  public View(Properties<?> properties) {
    this.id = properties.id;
    this.styleClasses = properties.styleClasses.build();
    this.tooltip = properties.tooltip;
    this.backgroundBlur = properties.backgroundBlur;
    this.doubleClick = properties.doubleClick;
    this.unscaleWidth = properties.unscaleWidth;
    this.unscaleHeight = properties.unscaleHeight;
    this.unscaleBorder = properties.unscaleBorder;
    this.unscaleOutline = properties.unscaleOutline;
    this.focusable = properties.focusable;

    this.styleHolder.registerDispatcher(
        ShorthandDispatcher.create("scale", Float.class, ShorthandArgMapper.TWO,
            this.xScale, this.yScale));

    this.styleHolder.registerDispatcher(
        ShorthandDispatcher.create("border-color", Color.class, ShorthandArgMapper.BOX_MAPPER,
            this.leftBorderColor, this.rightBorderColor,
            this.topBorderColor, this.bottomBorderColor));

    this.eventBus.start();
    this.stateManager.addState(States.ENABLED);
  }

  protected <T> void registerProperty(StyleableProperty<T> property) {
    this.stateManager.addListener(property);
    this.styleHolder.registerDispatcher(property);
  }

  protected <T> void removeProperty(StyleableProperty<T> property) {
    this.stateManager.removeListener(property);
    this.styleHolder.removeProperty(property.getName());
  }

  protected float computeFullHeight() {
    return this.getContentHeight();
  }

  protected void added() {
    this.added = true;
    Supplier<StyleList> styleSupplier =
        this.parent == null ? this.screen::getStyleList : this.parent.getStyle().getStyleSupplier();
    this.getStyle().setStyleSupplier(styleSupplier);
    this.getStyle().refresh();
    this.stateManager.notifyListeners(false);
    this.post(new AddedEvent());
  }

  protected void removed() {
    this.added = false;
    this.post(new RemovedEvent());
  }

  protected void setLayout(@Nullable Layout layout) {
    if (this.layout != null) {
      this.layout.gatherListeners(this.stateManager::removeListener);
      this.layout.gatherDispatchers(this.styleHolder::removeDispatcher);
      this.layout.close();
    }
    this.layout = layout;
    if (this.layout != null) {
      this.layout.gatherListeners(this.stateManager::addListener);
      this.layout.gatherDispatchers(this.styleHolder::registerDispatcher);
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
  public final void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
    final var topBorderWidth = this.unscale(this.layout.getTopBorder(), this.unscaleBorder);
    final var bottomBorderWidth = this.unscale(this.layout.getBottomBorder(), this.unscaleBorder);
    final var leftBorderWidth = this.unscale(this.layout.getLeftBorder(), this.unscaleBorder);
    final var rightBorderWidth = this.unscale(this.layout.getRightBorder(), this.unscaleBorder);
    final var borderRadius = this.unscale(this.borderRadius.get(), this.unscaleBorder);
    final var outlineWidth = this.unscale(this.outlineWidth.get(), this.unscaleOutline);

    if (this.backgroundBlur != null) {
      this.backgroundBlur.process(partialTick);
      if (borderRadius > 0.0F) {
        final var shader = ClientDist.getRoundedTexShader();
        RenderSystem.setShader(shader::instance);
        shader.radius().set(borderRadius, borderRadius, borderRadius, borderRadius);
        shader.position().set(this.getScaledX() - outlineWidth, this.getScaledY() - outlineWidth);
        shader.size().set(this.getScaledWidth() + (outlineWidth * 2.0F),
            this.getScaledHeight() + (outlineWidth * 2.0F));
      } else {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
      }
      RenderSystem.enableBlend();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.getAlpha());
      this.backgroundBlur.render(poseStack, this.getScaledX() - outlineWidth,
          this.getScaledY() - outlineWidth,
          this.getScaledWidth() + (outlineWidth * 2.0F),
          this.getScaledHeight() + (outlineWidth * 2.0F));
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.disableBlend();
    }

    final var roundedRectShader = ClientDist.getRoundedRectShader();
    RenderSystem.setShader(roundedRectShader::instance);
    roundedRectShader.radius()
        .set(borderRadius, borderRadius, borderRadius, borderRadius);
    roundedRectShader.position()
        .set(this.getScaledX() - outlineWidth, this.getScaledY() - outlineWidth);
    roundedRectShader.size()
        .set(this.getScaledWidth() + (outlineWidth * 2.0F),
            this.getScaledHeight() + (outlineWidth * 2.0F));
    roundedRectShader.outlineWidth().set(outlineWidth);
    roundedRectShader.outlineColor().set(this.outlineColor.get().getValue4f());

    if (!this.backgroundColor.get().isTransparent() || !this.outlineColor.get().isTransparent()) {
      var color = this.backgroundColor.get().getValue4f();
      color[3] *= this.getAlpha();
      var x = this.getScaledX() - outlineWidth;
      var y = this.getScaledY() - outlineWidth;
      var x2 = this.getScaledX() + this.getScaledWidth() + outlineWidth;
      var y2 = this.getScaledY() + this.getScaledHeight() + outlineWidth;
      RenderUtil.fill(poseStack.last().pose(), x, y,
          this.getZOffset(), x2, y2, color[0], color[1], color[2], color[3]);
    }

    RenderSystem.disableTexture();
    RenderSystem.enableBlend();
    RenderSystem.defaultBlendFunc();

    final var tessellator = Tesselator.getInstance();
    final var builder = tessellator.getBuilder();

    if (leftBorderWidth > 0F) {
      var x2 = this.getScaledX() + leftBorderWidth;
      var x = this.getScaledX();
      var y2 = this.getScaledY() + this.getScaledHeight();

      var color = this.leftBorderColor.get().getValue4f();

      builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
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
      var x2 = this.getScaledX() + this.getScaledWidth();
      var x = x2 - rightBorderWidth;
      var y2 = this.getScaledY() + this.getScaledHeight();

      var color = this.rightBorderColor.get().getValue4f();

      builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
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
      var x2 = this.getScaledX() + this.getScaledWidth();
      var y2 = this.getScaledY() + topBorderWidth;

      var color = this.topBorderColor.get().getValue4f();

      builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
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

      builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
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

    if (this.tooltip != null && this.isHovered()) {
      this.tooltip.render(this.minecraft.font, poseStack,
          10.0F + this.getX() + this.getWidth(), this.getY());
    }

    // ---- Render Content----

    final boolean scissor = this.layout.getOverflow().shouldScissor();
    if (scissor) {
      final var scale = this.window.getGuiScale();
      ScissorStack.push(
          (int) (this.getScaledX() * scale),
          (int) (this.window.getHeight()
              - (this.getScaledY() + this.getScaledHeight()) * scale),
          (int) (this.getScaledWidth() * scale),
          (int) (this.getScaledHeight() * scale));
    }

    this.renderContent(poseStack, mouseX, mouseY, partialTick);

    if (scissor) {
      ScissorStack.pop();
    }

    // ---- Render Scrollbar ----

    if (this.isScrollbarEnabled()) {
      final var scrollbarX = this.getScrollbarX();
      final var scaledY = this.getScaledY();

      final var scrollbarWidth = SCROLLBAR_WIDTH * this.getXScale();

      final var scrollbarRadius = scrollbarWidth / 2.0F + 0.75F;

      final var scrollbarX2 = scrollbarX + scrollbarWidth;
      final var scaledY2 = scaledY + this.getScaledHeight();

      RenderSystem.setShader(roundedRectShader::instance);

      roundedRectShader.radius().set(scrollbarRadius, scrollbarRadius, scrollbarRadius,
          scrollbarRadius);
      roundedRectShader.outlineWidth().set(0.0F);

      // Background
      roundedRectShader.position().set(scrollbarX, scaledY);
      roundedRectShader.size().set(scrollbarWidth, this.getScaledHeight());
      RenderUtil.fill(poseStack, scrollbarX, scaledY, scrollbarX2, scaledY2, 0x40000000);

      // Bar
      final var scrollbarY = this.getScrollbarY();
      final var scrollbarY2 = scrollbarY + this.getScrollbarHeight();
      roundedRectShader.position().set(scrollbarX, scrollbarY);
      roundedRectShader.size().set(scrollbarWidth, this.getScrollbarHeight());
      RenderUtil.fill(poseStack, scrollbarX, scrollbarY, scrollbarX2, scrollbarY2, 0x4CFFFFFF);
    }
  }

  protected final boolean isScrollbarEnabled() {
    return this.layout.getOverflow() == Overflow.SCROLL
        && this.fullHeight > this.getContentHeight();
  }

  @SuppressWarnings("unused")
  protected void renderContent(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
    if (DEBUG && this.isHovered()) {
      RenderUtil.fillWidthHeight(poseStack, this.getScaledContentX(), this.getScaledContentY(),
          this.getScaledContentWidth(), this.getScaledContentHeight(), 0x333495eb);

      // Left border
      RenderUtil.fill(poseStack, this.getScaledX(), this.getScaledY(),
          this.getScaledX() + this.unscale(this.layout.getLeftBorder(), this.unscaleBorder),
          this.getScaledY() + this.getScaledHeight(), 0x33e3b352);
      // Left padding
      RenderUtil.fill(poseStack,
          this.getScaledX() + this.unscale(this.layout.getLeftBorder(), this.unscaleBorder),
          this.getScaledY() + this.unscale(this.layout.getTopBorder(), this.unscaleBorder),
          this.getScaledContentX(),
          this.getScaledY() + this.getScaledHeight()
              - this.unscale(this.layout.getBottomBorder(), this.unscaleBorder),
          0x3384ab05);

      // Right border
      RenderUtil.fill(poseStack,
          this.getScaledX() + this.getScaledWidth()
              - this.unscale(this.layout.getRightBorder(), this.unscaleBorder),
          this.getScaledY(),
          this.getScaledX() + this.getScaledWidth(),
          this.getScaledY() + this.getScaledHeight(), 0x33e3b352);
      // Right padding
      RenderUtil.fill(poseStack,
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
      RenderUtil.fill(poseStack, this.getScaledX(),
          this.getScaledY(),
          this.getScaledX() + this.getScaledWidth(),
          this.getScaledY() + this.unscale(this.layout.getTopBorder(), this.unscaleBorder),
          0x33e3b352);
      // Top padding
      RenderUtil.fill(poseStack,
          this.getScaledX() + this.unscale(this.layout.getLeftBorder(), this.unscaleBorder),
          this.getScaledY() + this.unscale(this.layout.getTopBorder(), this.unscaleBorder),
          this.getScaledX() + this.getScaledWidth()
              - this.unscale(this.layout.getRightBorder(), this.unscaleBorder),
          this.getScaledContentY(), 0x3384ab05);

      // Bottom border
      RenderUtil.fill(poseStack, this.getScaledX(),
          this.getScaledY() + this.getScaledHeight()
              - this.unscale(this.layout.getBottomBorder(), this.unscaleBorder),
          this.getScaledX() + this.getScaledWidth(),
          this.getScaledY() + this.getScaledHeight(), 0x33e3b352);
      // Bottom padding
      RenderUtil.fill(poseStack,
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

    this.stateManager.addState(States.HOVER);
    this.stateManager.notifyListeners(true);

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

    this.stateManager.removeState(States.HOVER);
    this.stateManager.notifyListeners(true);

    this.post(new MouseLeaveEvent());
  }

  @Override
  public void mouseMoved(double mouseX, double mouseY) {
    this.post(new MouseEvent.MoveEvent(mouseX, mouseY));
  }

  public boolean tryFocus(boolean visible) {
    if (this.isFocused()) {
      return true;
    }

    if (!this.focusable || this.stateManager.hasState(States.DISABLED)) {
      return false;
    }

    if (visible) {
      this.stateManager.addState(States.FOCUS_VISIBLE);
    }
    this.stateManager.addState(States.FOCUS);
    this.stateManager.notifyListeners(true);
    this.focusChanged();

    return true;
  }

  public void removeFocus() {
    if (this.isFocused()) {
      this.stateManager.removeState(States.FOCUS);
      this.stateManager.removeState(States.FOCUS_VISIBLE);
      this.stateManager.notifyListeners(true);
      this.focusChanged();
    }
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (this.post(new MouseEvent.ButtonEvent(mouseX, mouseY, button,
        GLFW.GLFW_PRESS)) == Event.Result.ALLOW) {
      return true;
    }

    // Clicked on scrollbar region
    if (this.isScrollbarEnabled()
        && mouseX >= this.getScaledX() + this.getScaledWidth() - SCROLLBAR_WIDTH
        && mouseX <= this.getScaledX() + this.getScaledWidth()
        && mouseY >= this.getScaledY()
        && mouseY <= this.getScaledY() + this.getScaledHeight()) {
      // Clicked outside of scrollbar
      if (mouseY < this.getScrollbarY()
          || mouseY > this.getScrollbarY() + this.getScrollbarHeight()) {
        this.scrollTo(this.scrollOffset
            + (mouseY > this.getScrollbarY() ? this.getScaledHeight() : -this.getScaledHeight()),
            200);
      }
      return true;
    }

    if (!this.isHovered() || !this.tryFocus(false)) {
      return false;
    }

    var currentTime = Util.getMillis();
    var deltaTime = currentTime - this.lastClickTimeMs;
    this.lastClickTimeMs = currentTime;
    if (!this.doubleClick || deltaTime < DOUBLE_CLICK_DURATION_MS) {
      this.post(new ActionEvent());
    }

    return true;
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
    if (this.focusable && this.stateManager.hasState(States.ENABLED)) {
      var focused = this.stateManager.toggleState(States.FOCUS);
      if (focused) {
        this.stateManager.addState(States.FOCUS_VISIBLE);
      } else {
        this.stateManager.removeState(States.FOCUS_VISIBLE);
      }
      this.stateManager.notifyListeners(true);
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
    if (this.closed) {
      throw new IllegalStateException("Already closed");
    }
    if (this.backgroundBlur != null) {
      this.backgroundBlur.close();
    }
    this.layout.close();
    this.closed = true;
  }

  public final void addActionSound(SoundEvent soundEvent) {
    this.addListener(ActionEvent.class, event -> this.minecraft.getSoundManager()
        .play(SimpleSoundInstance.forUI(soundEvent, 1.0F)));
  }

  public final void addHoverSound(SoundEvent soundEvent) {
    this.addListener(MouseEnterEvent.class, event -> this.minecraft.getSoundManager()
        .play(SimpleSoundInstance.forUI(soundEvent, 1.0F)));
  }

  public final <T extends Event> void addListener(Class<T> eventType, Consumer<T> consumer) {
    this.addListener(eventType, consumer, true);
  }

  public final <T extends Event> void addListener(Class<T> eventType, Consumer<T> consumer,
      boolean consumeEvent) {
    this.eventBus.addListener(EventPriority.NORMAL, true, eventType, event -> {
      if (event.hasResult() && consumeEvent) {
        event.setResult(Event.Result.ALLOW);
      }
      consumer.accept(event);
    });
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

  public float getZOffset() {
    return this.index + this.zIndex.get();
  }

  public final float getXScale() {
    return (this.parent == null ? 1.0F : this.parent.getXScale()) * this.xScale.get();
  }

  public final float getYScale() {
    return (this.parent == null ? 1.0F : this.parent.getYScale()) * this.yScale.get();
  }

  public final float getAlpha() {
    return (this.parent == null ? 1.0F : this.parent.getAlpha()) * this.alpha.get();
  }

  public final StyleableProperty<Float> getXTranslationProperty() {
    return this.xTranslation;
  }

  public final StyleableProperty<Float> getYTranslationProperty() {
    return this.yTranslation;
  }

  public final StyleableProperty<Float> getXScaleProperty() {
    return this.xScale;
  }

  public final StyleableProperty<Float> getYScaleProperty() {
    return this.yScale;
  }

  public final StyleableProperty<Color> getTopBorderColorProperty() {
    return this.topBorderColor;
  }

  public final StyleableProperty<Color> getRightBorderColorProperty() {
    return this.rightBorderColor;
  }

  public final StyleableProperty<Color> getBottomBorderColorProperty() {
    return this.bottomBorderColor;
  }

  public final StyleableProperty<Color> getLeftBorderColorProperty() {
    return this.leftBorderColor;
  }

  public final StyleableProperty<Float> getOutlineWidthProperty() {
    return this.outlineWidth;
  }

  public final StyleableProperty<Color> getOutlineColorProperty() {
    return this.outlineColor;
  }

  public final StyleableProperty<Color> getBackgroundColorProperty() {
    return this.backgroundColor;
  }

  public final StyleableProperty<Float> getBorderRadiusProperty() {
    return this.borderRadius;
  }

  public final StyleableProperty<Float> getAlphaProperty() {
    return this.alpha;
  }

  public final void defineBorderColorState(Color color, int... states) {
    this.topBorderColor.defineState(color, states);
    this.rightBorderColor.defineState(color, states);
    this.bottomBorderColor.defineState(color, states);
    this.leftBorderColor.defineState(color, states);
  }

  public final void setEnabled(boolean enabled) {
    if (enabled) {
      this.stateManager.addState(States.ENABLED);
      this.stateManager.removeState(States.DISABLED);
    } else {
      this.stateManager.removeState(States.ENABLED);
      this.stateManager.addState(States.DISABLED);
      if (this.stateManager.hasState(States.FOCUS)) {
        this.stateManager.removeState(States.FOCUS);
      }
    }
    this.stateManager.notifyListeners(this.added);
  }

  public final boolean isHovered() {
    return this.stateManager.hasState(States.HOVER);
  }

  public final boolean isFocused() {
    return this.stateManager.hasState(States.FOCUS);
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

  public final boolean hasParent() {
    return this.parent != null;
  }

  public final ParentView getParent() {
    return this.parent;
  }

  public final boolean hasLayout() {
    return this.layout != null;
  }

  public final Layout getLayout() {
    return this.layout;
  }

  public final boolean isClosed() {
    return this.closed;
  }

  public final boolean isAdded() {
    return this.added;
  }

  protected final float unscaleOffset(float value, boolean condition) {
    return condition
        ? value * (float) this.window.getGuiScale() - value
        : 0.0F;
  }

  protected final float unscale(float value, boolean condition) {
    return condition ? value / (float) this.window.getGuiScale() : value;
  }

  @Override
  public final int compareTo(View another) {
    return Float.compare(this.getZOffset(), another.getZOffset());
  }

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public Set<String> getStyleClasses() {
    return this.styleClasses;
  }

  @Override
  public StyleHolder getStyle() {
    return this.styleHolder;
  }

  @Override
  public final String getType() {
    if (this.getClass().isAnonymousClass()) {
      return this.getClass().getSuperclass().getSimpleName();
    }
    return this.getClass().getSimpleName();
  }

  public void refreshStyle() {
    this.styleHolder.refresh();
  }

  public static class Properties<SELF extends Properties<SELF>> {

    @Nullable
    private String id;
    private final ImmutableSet.Builder<String> styleClasses = ImmutableSet.builder();
    @Nullable
    private Tooltip tooltip;
    @Nullable
    private Blur backgroundBlur;
    private boolean doubleClick;
    private boolean unscaleWidth;
    private boolean unscaleHeight;
    private boolean unscaleBorder = true;
    private boolean unscaleOutline = true;
    private boolean focusable;

    public SELF id(String id) {
      this.id = id;
      return this.self();
    }

    public SELF styleClasses(String... styleClasses) {
      this.styleClasses.add(styleClasses);
      return this.self();
    }

    public SELF styleClasses(Iterable<String> styleClasses) {
      this.styleClasses.addAll(styleClasses);
      return this.self();
    }

    public final SELF tooltip(@Nullable Tooltip tooltip) {
      this.tooltip = tooltip;
      return this.self();
    }

    public final SELF backgroundBlur(float blurRadius) {
      if (this.backgroundBlur != null) {
        this.backgroundBlur.close();
      }
      this.backgroundBlur = new Blur(blurRadius);
      return this.self();
    }

    public final SELF doubleClick(boolean doubleClick) {
      this.doubleClick = doubleClick;
      return this.self();
    }

    public final SELF unscaleWidth(boolean unscaleWidth) {
      this.unscaleWidth = unscaleWidth;
      return this.self();
    }

    public final SELF unscaleHeight(boolean unscaleHeight) {
      this.unscaleHeight = unscaleHeight;
      return this.self();
    }

    public final SELF unscaleBorder(boolean unscaleBorder) {
      this.unscaleBorder = unscaleBorder;
      return this.self();
    }

    public final SELF unscaleOutline(boolean unscaleOutline) {
      this.unscaleOutline = unscaleOutline;
      return this.self();
    }

    public final SELF focusable(boolean focusable) {
      this.focusable = focusable;
      return this.self();
    }

    @SuppressWarnings("unchecked")
    protected SELF self() {
      return (SELF) this;
    }
  }
}
