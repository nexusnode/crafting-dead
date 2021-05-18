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

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import org.lwjgl.glfw.GLFW;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.ClientDist;
import com.craftingdead.immerse.client.gui.tween.ColourTweenType;
import com.craftingdead.immerse.client.gui.tween.FloatArrayTweenType;
import com.craftingdead.immerse.client.gui.tween.FloatTweenType;
import com.craftingdead.immerse.client.gui.view.event.ActionEvent;
import com.craftingdead.immerse.client.gui.view.event.CharTypeEvent;
import com.craftingdead.immerse.client.gui.view.event.FocusChangedEvent;
import com.craftingdead.immerse.client.gui.view.event.KeyEvent;
import com.craftingdead.immerse.client.gui.view.event.MouseEnterEvent;
import com.craftingdead.immerse.client.gui.view.event.MouseEvent;
import com.craftingdead.immerse.client.gui.view.event.MouseLeaveEvent;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.craftingdead.immerse.client.gui.view.layout.MeasureMode;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import io.noties.tumbleweed.Timeline;
import io.noties.tumbleweed.Tween;
import io.noties.tumbleweed.TweenManager;
import io.noties.tumbleweed.TweenType;
import io.noties.tumbleweed.equations.Sine;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;

public class View<SELF extends View<SELF, L>, L extends Layout> extends AbstractGui
    implements IGuiEventListener, IRenderable, Comparable<View<?, ?>> {

  public static final TweenType<View<?, ?>> X_SCALE =
      new FloatTweenType<>(View::getXScale, View::setXScale);

  public static final TweenType<View<?, ?>> Y_SCALE =
      new FloatTweenType<>(View::getYScale, View::setYScale);

  public static final TweenType<View<?, ?>> X_TRANSLATION =
      new FloatTweenType<>(c -> c.xOffset, View::setXOffset);

  public static final TweenType<View<?, ?>> Y_TRANSLATION =
      new FloatTweenType<>(c -> c.yOffset, View::setYOffset);

  public static final TweenType<View<?, ?>> BACKGROUND_COLOUR =
      new ColourTweenType<>(t -> t.backgroundColour);

  public static final TweenType<View<?, ?>> BORDER_WIDTH =
      new FloatArrayTweenType<>(4,
          t -> new float[] {t.topBorderWidth, t.rightBorderWidth, t.bottomBorderWidth,
              t.leftBorderWidth},
          (t, v) -> {
            t.topBorderWidth = v[0];
            t.rightBorderWidth = v[1];
            t.bottomBorderWidth = v[2];
            t.leftBorderWidth = v[3];
          });

  public static final TweenType<View<?, ?>> ALPHA =
      new FloatTweenType<>(c -> c.alpha, View::setAlpha);

  private static final TweenType<View<?, ?>> SCROLL_OFFSET =
      new FloatTweenType<>(c -> c.scrollOffset,
          (View<?, ?> c, Float v) -> c.scrollOffset = v);

  private static final int SCROLLBAR_WIDTH = 4;

  private static final float SCROLL_CHUNK = 50F;

  private static final float SCROLL_MOMENTUM_DAMPING = 3F;

  private static final int DOUBLE_CLICK_DURATION_MS = 500;

  protected final Minecraft minecraft = Minecraft.getInstance();

  protected final MainWindow mainWindow = this.minecraft.getWindow();

  protected final ClientDist clientDist =
      (ClientDist) CraftingDeadImmerse.getInstance().getModDist();

  private final IEventBus eventBus = BusBuilder.builder().build();

  private final TweenManager tweenManager = TweenManager.create();

  protected final L layout;

  @Nullable
  ViewScreen screen;

  @Nullable
  ParentView<?, ?, ? super L> parent;
  int index;

  private float lastScrollOffset;
  private float scrollOffset;
  private float scrollVelocity;

  private float fullHeight;

  private float xScale = 1.0F;
  private float yScale = 1.0F;
  private float xOffset = 0.0F;
  private float yOffset = 0.0F;
  private float zOffset = 0.0F;

  private Overflow overflow = Overflow.VISIBLE;

  private float alpha = 1.0F;

  private float topBorderWidth = 0F;
  private Colour topBorderColour = Colour.WHITE;
  private float rightBorderWidth = 0F;
  private Colour rightBorderColour = Colour.WHITE;
  private float bottomBorderWidth = 0F;
  private Colour bottomBorderColour = Colour.WHITE;
  private float leftBorderWidth = 0F;
  private Colour leftBorderColour = Colour.WHITE;

  @Nullable
  private Tooltip tooltip;

  @Nullable
  private Colour backgroundColour;

  @Nullable
  private Blur backgroundBlur;

  protected boolean pendingRemoval;

  private boolean hovered;
  protected boolean focused;
  protected boolean focusable;

  private boolean unscaleWidth;
  private boolean unscaleHeight;

  private boolean doubleClick;
  private long lastClickTimeMs;

  public View(L layout) {
    this.layout = layout;
    this.layout.setMeasureFunction(this::measure);
  }

  protected Vector2f measure(MeasureMode widthMode, float width, MeasureMode heightMode,
      float height) {
    return new Vector2f(width, height);
  }

  protected float computeFullHeight() {
    return this.getContentHeight();
  }

  protected void added() {}

  protected void removed(Runnable remove) {
    remove.run();
  }

  protected void layout() {
    this.fullHeight = this.computeFullHeight();
    this.scrollOffset = this.clampScrollOffset(this.scrollOffset);
    this.layout.layout();
  }

  public void tick() {
    this.lastScrollOffset = this.scrollOffset;

    this.scrollVelocity *= (1.0F / SCROLL_MOMENTUM_DAMPING);
    if (Math.abs(this.scrollVelocity) < 0.08F) {
      this.scrollVelocity = 0.0F;
    }

    this.scrollOffset += this.scrollVelocity;

    if (this.scrollOffset < 0.0F
        || this.scrollOffset > this.fullHeight - this.getHeight()) {
      this.scrollVelocity = 0.0F;
    }
    this.scrollOffset = this.clampScrollOffset(this.scrollOffset);

    if (this.backgroundBlur != null) {
      this.backgroundBlur.tick();
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    if (this.pendingRemoval) {
      return;
    }

    this.tweenManager.update(partialTicks * 100.0F);

    if (this.backgroundBlur != null) {
      RenderSystem.enableBlend();
      RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.getAlpha());
      this.backgroundBlur.render(matrixStack, this.getScaledX(), this.getScaledY(),
          this.getScaledWidth(), this.getScaledHeight(), partialTicks);
      RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.disableBlend();
    }

    if (this.backgroundColour != null) {
      RenderSystem.enableBlend();
      float[] colour = this.backgroundColour.getColour4f();
      colour[3] *= this.getAlpha();
      RenderUtil.fill(this.getScaledX(), this.getScaledY(), this.getZOffset(),
          this.getScaledX() + this.getScaledWidth(),
          this.getScaledY() + this.getScaledHeight(), colour[0], colour[1], colour[2], colour[3]);
      RenderSystem.disableBlend();
    }

    if (this.topBorderWidth > 0F) {
      RenderUtil.fill(this.getScaledX(), this.getScaledY(),
          this.getScaledX() + this.getScaledWidth(),
          this.getScaledY() + this.topBorderWidth, this.topBorderColour.getHexColour());
    }
    if (this.rightBorderWidth > 0F) {
      RenderUtil.fill(this.getScaledX() + this.getScaledWidth() - this.rightBorderWidth,
          this.getScaledY(),
          this.getScaledX() + this.getScaledWidth(), this.getScaledY() + this.getScaledHeight(),
          this.rightBorderColour.getHexColour());
    }
    if (this.bottomBorderWidth > 0F) {
      RenderUtil.fill(this.getScaledX(),
          this.getScaledY() + this.getScaledHeight() - this.bottomBorderWidth,
          this.getScaledX() + this.getScaledWidth(), this.getScaledY() + this.getScaledHeight(),
          this.bottomBorderColour.getHexColour());
    }
    if (this.leftBorderWidth > 0F) {
      RenderUtil.fill(this.getScaledX(), this.getScaledY(),
          this.getScaledX() + this.leftBorderWidth,
          this.getScaledY() + this.getScaledHeight(), this.leftBorderColour.getHexColour());
    }

    if (this.tooltip != null && this.isHovered()) {
      this.tooltip.render(this.minecraft.font, matrixStack,
          10.0D + this.getX() + this.getWidth(), this.getY());
    }

    // ---- Render Content----

    final double scale = this.minecraft.getWindow().getGuiScale();
    final boolean scissor =
        this.getOverflow() == Overflow.HIDDEN || this.getOverflow() == Overflow.SCROLL;

    if (scissor) {
      RenderSystem.enableScissor((int) (this.getScaledContentX() * scale),
          (int) (this.mainWindow.getHeight()
              - (this.getScaledY() + this.getScaledHeight()) * scale),
          (int) (this.getScaledContentWidth() * scale),
          (int) (this.getScaledHeight() * scale));
    }

    this.renderContent(matrixStack, mouseX, mouseY, partialTicks);
    if (scissor) {
      RenderSystem.disableScissor();
    }

    // ---- Render Scrollbar ----

    if (this.isScrollbarEnabled()) {
      RenderUtil.roundedFill(this.getScrollbarX(), this.getScaledY(),
          this.getScrollbarX() + SCROLLBAR_WIDTH,
          this.getScaledY() + this.getScaledHeight(), 0x40000000, SCROLLBAR_WIDTH / 2.0F);
      RenderUtil.roundedFill(this.getScrollbarX(), this.getScrollbarY(),
          this.getScrollbarX() + SCROLLBAR_WIDTH, this.getScrollbarY() + this.getScrollbarHeight(),
          0x4CFFFFFF, SCROLLBAR_WIDTH / 2.0F);
    }
  }

  protected final boolean isScrollbarEnabled() {
    return this.getOverflow() == Overflow.SCROLL && this.fullHeight > this.getHeight();
  }

  protected void renderContent(MatrixStack matrixStack, int mouseX, int mouseY,
      float partialTicks) {}

  private final double getScrollbarX() {
    return this.getScaledX() + this.getScaledWidth() - SCROLLBAR_WIDTH;
  }

  private final double getScrollbarY() {
    return this.getScaledY()
        + (this.getScrollOffset(this.minecraft.getFrameTime()) / this.fullHeight)
            * this.getScaledHeight();
  }

  private final float getScrollOffset(float partialTicks) {
    return MathHelper.lerp(partialTicks, this.lastScrollOffset, this.scrollOffset);
  }

  private final float getScrollbarHeight() {
    return MathHelper.clamp(
        this.getScaledHeight() * (this.getContentHeight() / this.fullHeight),
        10.0F,
        this.getScaledHeight());
  }

  private final void scrollTo(float y, float duration) {
    Tween.to(this, SCROLL_OFFSET, duration)
        .target(this.clampScrollOffset(y))
        .ease(Sine.OUT)
        .start(this.getTweenManager());
  }

  private final float clampScrollOffset(float scrollOffset) {
    return MathHelper.clamp(scrollOffset, 0.0F, this.fullHeight - this.getContentHeight());
  }

  public void mouseEntered(double mouseX, double mouseY) {
    this.hovered = true;
    if (this.tooltip != null) {
      Timeline.createParallel(150)
          .push(Tween.to(this.tooltip, Tooltip.ALPHA)
              .ease(Sine.INOUT)
              .target(1.0F))
          .push(Tween.to(this.tooltip, Tooltip.TEXT_ALPHA)
              .delay(100.0F)
              .target(1.0F))
          .start(this.getTweenManager());
    }

    this.post(new MouseEnterEvent());
  }

  public void mouseLeft(double mouseX, double mouseY) {
    this.hovered = false;
    if (this.tooltip != null) {
      Timeline.createParallel(250)
          .push(Tween.to(this.tooltip, Tooltip.ALPHA)
              .target(0.0F))
          .push(Tween.to(this.tooltip, Tooltip.TEXT_ALPHA)
              .target(0.0F))
          .start(this.getTweenManager());
    }
    this.post(new MouseLeaveEvent());
  }

  @Override
  public void mouseMoved(double mouseX, double mouseY) {
    this.post(new MouseEvent.MoveEvent(mouseX, mouseY));
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (!this.isHovered()) {
      return false;
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

    if (this.post(new MouseEvent.ButtonEvent(mouseX, mouseY, button,
        GLFW.GLFW_PRESS)) == Event.Result.ALLOW) {
      return true;
    }

    long currentTime = Util.getMillis();
    long deltaTime = currentTime - this.lastClickTimeMs;
    this.lastClickTimeMs = currentTime;
    return (!this.doubleClick || deltaTime < DOUBLE_CLICK_DURATION_MS)
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
    if (mouseY >= this.getScaledY() && mouseY <= this.getScaledY() + this.getScaledHeight()) {
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
    if (this.focusable) {
      this.focused = !this.focused;
      this.focusChanged();
      return this.focused;
    }
    return false;
  }

  protected void focusChanged() {
    if (this.focused) {
      this.setBorderWidth(0.7F);
    } else {
      this.setBorderWidth(0.0F);
    }
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

  public boolean isHovered() {
    return this.hovered;
  }

  public boolean isFocused() {
    return this.focused;
  }

  public final TweenManager getTweenManager() {
    return this.tweenManager;
  }

  public void close() {
    if (this.backgroundBlur != null) {
      this.backgroundBlur.close();
    }
    this.layout.close();
  }

  /**
   * Helper method to add an animation triggered by {@link MouseEnterEvent} that will be reversed
   * upon {@link MouseLeaveEvent}.
   * 
   * @param tweenType - the {@link TweenType} to animate
   * @param to - the target value
   * @param duration - the duration of the animation
   * @return instance of self for easy construction
   */
  public final SELF addHoverAnimation(TweenType<? super SELF> tweenType, float[] to,
      float duration) {
    float[] from = new float[tweenType.getValuesSize()];
    tweenType.getValues(this.self(), from);
    this
        .addListener(MouseEnterEvent.class,
            (component, event) -> Tween
                .to(this.self(), tweenType, duration)
                .target(to)
                .start(this.tweenManager))
        .addListener(MouseLeaveEvent.class,
            (component, event) -> Tween
                .to(this.self(), tweenType, duration)
                .target(from)
                .start(this.tweenManager));
    return this.self();
  }

  public final SELF addActionSound(SoundEvent soundEvent) {
    return this.addListener(ActionEvent.class, (component, event) -> this.minecraft
        .getSoundManager().play(SimpleSound.forUI(soundEvent, 1.0F)));
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
        + (this.getContentWidth() - (this.getContentWidth() * this.xScale)) / 2.0F;
  }

  public float getContentX() {
    return this.getX()
        + this.layout.getLeftPadding() * this.getXScale();
  }

  public final float getScaledX() {
    return this.getX() + (this.getWidth() - (this.getWidth() * this.xScale)) / 2.0F;
  }

  public final float getX() {
    final float left = this.layout.getLeft();
    return left
        + (left * this.getXScale()) - (left * this.xScale)
        + this.xOffset
        + (this.parent == null
            ? 0.0F
            : this.parent.getScaledContentX())
        + (this.unscaleWidth
            ? this.getWidth() * (float) this.mainWindow.getGuiScale()
            : 0.0F);
  }

  public final float getScaledContentY() {
    return this.getContentY()
        + (this.getContentHeight() - (this.getContentHeight() * this.yScale)) / 2.0F;
  }

  public float getContentY() {
    return this.getY()
        + this.layout.getTopPadding() * this.getYScale()
        - (this.fullHeight > this.getHeight()
            ? this.getScrollOffset(this.minecraft.getFrameTime())
            : 0.0F);
  }

  public final float getScaledY() {
    return this.getY() + (this.getHeight() - (this.getHeight() * this.yScale)) / 2.0F;
  }

  public final float getY() {
    final float top = this.layout.getTop();
    return top
        + (top * this.getYScale()) - (top * this.yScale)
        + this.yOffset
        + (this.parent == null
            ? 0.0F
            : this.parent.getScaledContentY())
        + (this.unscaleHeight
            ? this.getHeight() * (float) this.mainWindow.getGuiScale()
            : 0.0F);
  }

  public final float getScaledContentWidth() {
    return this.getContentWidth() * this.getXScale();
  }

  public float getContentWidth() {
    return this.getWidth() - this.layout.getRightPadding()
        - (this.isScrollbarEnabled() ? SCROLLBAR_WIDTH : 0.0F);
  }

  public final float getScaledWidth() {
    return this.getWidth() * this.getXScale();
  }

  public final float getWidth() {
    return (float) (this.layout.getWidth()
        / (this.unscaleWidth ? this.mainWindow.getGuiScale() : 1.0F));
  }

  public final float getScaledContentHeight() {
    return this.getContentHeight() * this.getYScale();
  }

  public final float getContentHeight() {
    return this.getHeight() - this.layout.getBottomPadding();
  }

  public float getScaledHeight() {
    return this.getHeight() * this.getYScale();
  }

  public final float getHeight() {
    return (float) (this.layout.getHeight()
        / (this.unscaleHeight ? this.mainWindow.getGuiScale() : 1.0F));
  }

  public final SELF setXOffset(float xOffset) {
    this.xOffset = xOffset;
    return this.self();
  }

  public final SELF setYOffset(float yOffset) {
    this.yOffset = yOffset;
    return this.self();
  }

  public final float getZOffset() {
    return this.index + this.zOffset;
  }

  public final SELF setZOffset(float zOffset) {
    if (this.zOffset != zOffset) {
      this.zOffset = zOffset;
      if (this.parent != null) {
        this.parent.sortChildren();
      }
    }
    return this.self();
  }

  public final float getXScale() {
    return (this.parent == null ? 1.0F : this.parent.getXScale()) * this.xScale;
  }

  public final SELF setXScale(float xScale) {
    this.xScale = xScale;
    return this.self();
  }

  public final float getYScale() {
    return (this.parent == null ? 1.0F : this.parent.getYScale()) * this.yScale;
  }

  public final SELF setYScale(float yScale) {
    this.yScale = yScale;
    return this.self();
  }

  public final Overflow getOverflow() {
    return this.overflow;
  }

  public final SELF setOverflow(Overflow overflow) {
    this.overflow = overflow;
    return this.self();
  }

  public final SELF setScale(float scale) {
    return this.setXScale(scale).setYScale(scale);
  }

  public final SELF setTopBorderWidth(float width) {
    this.topBorderWidth = width;
    return this.self();
  }

  public final SELF setRightBorderWidth(float width) {
    this.rightBorderWidth = width;
    return this.self();
  }

  public final SELF setBotBorderWidth(float width) {
    this.bottomBorderWidth = width;
    return this.self();
  }

  public final SELF setLeftBorderWidth(float width) {
    this.leftBorderWidth = width;
    return this.self();
  }

  public final SELF setTopBorderColour(Colour colour) {
    this.topBorderColour = colour;
    return this.self();
  }

  public final SELF setRightBorderColour(Colour colour) {
    this.rightBorderColour = colour;
    return this.self();
  }

  public final SELF setBotBorderColour(Colour colour) {
    this.bottomBorderColour = colour;
    return this.self();
  }

  public final SELF setLeftBorderColour(Colour colour) {
    this.leftBorderColour = colour;
    return this.self();
  }

  public final SELF setBorderWidth(float width) {
    this.topBorderWidth = width;
    this.rightBorderWidth = width;
    this.bottomBorderWidth = width;
    this.leftBorderWidth = width;
    return this.self();
  }

  public final SELF setBorderColour(Colour colour) {
    this.topBorderColour = colour;
    this.rightBorderColour = colour;
    this.bottomBorderColour = colour;
    this.leftBorderColour = colour;
    return this.self();
  }



  public final SELF setTooltip(@Nullable Tooltip tooltip) {
    this.tooltip = tooltip;
    return this.self();
  }

  public final SELF setBackgroundColour(@Nullable Colour backgroundColour) {
    this.backgroundColour = backgroundColour;
    return this.self();
  }

  public final SELF setBackgroundBlur() {
    return this.setBackgroundBlur(0);
  }

  public final SELF setBackgroundBlur(float blurRadius) {
    if (this.backgroundBlur != null) {
      this.backgroundBlur.close();
    }
    this.backgroundBlur = new Blur(blurRadius);
    return this.self();
  }

  public final SELF setFocusable(boolean focusable) {
    this.focusable = focusable;
    return this.self();
  }

  public final SELF setDoubleClick(boolean doubleClick) {
    this.doubleClick = doubleClick;
    return this.self();
  }

  public final SELF setUnscaleWidth() {
    this.unscaleWidth = true;
    return this.self();
  }

  public final SELF setUnscaleHeight() {
    this.unscaleHeight = true;
    return this.self();
  }

  public final float getAlpha() {
    return (this.parent == null ? 1.0F : this.parent.getAlpha()) * this.alpha;
  }

  public final SELF setAlpha(float alpha) {
    this.alpha = alpha;
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
        throw new IllegalStateException("Root view has no screen");
      }
      return this.parent.getScreen();
    }
    return this.screen;
  }

  public final ParentView<?, ?, ? super L> getParent() {
    return this.parent;
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
}
