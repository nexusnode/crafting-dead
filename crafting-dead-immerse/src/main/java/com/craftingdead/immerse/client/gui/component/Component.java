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

package com.craftingdead.immerse.client.gui.component;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.annotation.Nullable;

import com.craftingdead.immerse.client.gui.component.event.CharTypeEvent;
import com.craftingdead.immerse.client.gui.component.event.KeyEvent;
import com.craftingdead.immerse.client.gui.component.event.MouseEnterEvent;
import com.craftingdead.immerse.client.gui.component.event.MouseEvent;
import com.craftingdead.immerse.client.gui.component.event.MouseLeaveEvent;
import com.craftingdead.immerse.client.gui.component.event.ZLevelChangeEvent;
import com.craftingdead.immerse.client.gui.component.type.Align;
import com.craftingdead.immerse.client.gui.component.type.MeasureMode;
import com.craftingdead.immerse.client.gui.component.type.PositionType;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.yoga.YGMeasureFunc;
import org.lwjgl.util.yoga.YGSize;
import org.lwjgl.util.yoga.Yoga;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.ClientDist;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
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
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;

public abstract class Component<SELF extends Component<SELF>> extends AbstractGui
    implements IRenderable, IGuiEventListener, IView {

  public static final TweenType<Component<?>> X_SCALE =
      new SimpleTweenType<>(
          (SimpleTweenType.FloatGetter<Component<?>>) Component::getXScale,
          (SimpleTweenType.FloatSetter<Component<?>>) Component::setXScale);
  public static final TweenType<Component<?>> Y_SCALE =
      new SimpleTweenType<>(
          (SimpleTweenType.FloatGetter<Component<?>>) Component::getYScale,
          (SimpleTweenType.FloatSetter<Component<?>>) Component::setYScale);
  public static final TweenType<Component<?>> X_TRANSLATION =
      new SimpleTweenType<>(
          (SimpleTweenType.FloatGetter<Component<?>>) c -> c.xTranslation,
          (SimpleTweenType.FloatSetter<Component<?>>) (c, v) -> c.xTranslation = v);
  public static final TweenType<Component<?>> Y_TRANSLATION =
      new SimpleTweenType<>(
          (SimpleTweenType.FloatGetter<Component<?>>) c -> c.yTranslation,
          (SimpleTweenType.FloatSetter<Component<?>>) (c, v) -> c.yTranslation = v);
  public static final TweenType<Component<?>> BORDER_WIDTH_BOTTOM =
      new SimpleTweenType<>(
          (SimpleTweenType.FloatGetter<Component<?>>) c -> c.bottomBorderWidth,
          (SimpleTweenType.FloatSetter<Component<?>>) (c, v) -> c.bottomBorderWidth = v);
  public static final TweenType<Component<?>> BACKGROUND_COLOUR =
      new SimpleTweenType<>(4, t -> t.backgroundColour.getColour4f(),
          (t, v) -> t.backgroundColour.setColour4f(v));
  public static final TweenType<Component<?>> BORDER_WIDTH =
      new SimpleTweenType<>(4,
          t -> new float[] {t.topBorderWidth, t.rightBorderWidth, t.bottomBorderWidth,
              t.leftBorderWidth},
          (t, v) -> {
            t.topBorderWidth = v[0];
            t.rightBorderWidth = v[1];
            t.bottomBorderWidth = v[2];
            t.leftBorderWidth = v[3];
          });

  protected final Minecraft minecraft = Minecraft.getInstance();

  protected final MainWindow mainWindow = this.minecraft.getMainWindow();

  protected final ClientDist clientDist =
      (ClientDist) CraftingDeadImmerse.getInstance().getModDist();

  private final IEventBus eventBus = BusBuilder.builder().build();

  private final TweenManager tweenManager = TweenManager.create();

  private float xScale = 1.0F;
  private float yScale = 1.0F;
  private float xTranslation = 0.0F;
  private float yTranslation = 0.0F;

  private float topBorderWidth = 0F;
  private Colour topBorderColour = Colour.WHITE;
  private float rightBorderWidth = 0F;
  private Colour rightBorderColour = Colour.WHITE;
  private float bottomBorderWidth = 0F;
  private Colour bottomBorderColour = Colour.WHITE;
  private float leftBorderWidth = 0F;
  private Colour leftBorderColour = Colour.WHITE;

  protected final long node;

  @Nullable
  private Tooltip tooltip;

  @Nullable
  private Colour backgroundColour;

  @Nullable
  private Blur backgroundBlur;

  protected IView parent;

  private boolean mouseOver;

  private float lastTime = 0F;

  protected boolean focused;

  private boolean unscaleWidth;
  private boolean unscaleHeight;

  private int zLevel = 0;

  public Component() {
    this.node = Yoga.YGNodeNew();
    Yoga.YGNodeSetMeasureFunc(this.node, (node, width, widthMode, height, heightMode) -> {
      Vector2f size = this.measure(MeasureMode.fromYogaType(widthMode), width,
          MeasureMode.fromYogaType(heightMode), height);
      return YGMeasureFunc.toLong(YGSize.create().width(size.x).height(size.y));
    });
  }

  protected Vector2f measure(MeasureMode widthMode, float width, MeasureMode heightMode,
      float height) {
    return new Vector2f(width, height);
  }

  protected void zLevelChanged() {
    this.post(new ZLevelChangeEvent());
  }

  protected void added() {}

  protected void removed() {}

  protected void layout() {
    Yoga.YGNodeMarkDirty(this.node);
  }

  public void tick() {
    if (this.backgroundBlur != null) {
      this.backgroundBlur.tick();
    }
  }

  protected void focusChanged(boolean focused) {
    this.focused = focused;
  }

  @Override
  public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    float currentTime = this.lastTime + partialTicks;
    float deltaTime = (currentTime - this.lastTime) * 50;
    this.lastTime = currentTime;
    this.tweenManager.update(deltaTime);

    if (this.backgroundBlur != null) {
      this.backgroundBlur.render(this.getScaledContentX(), this.getScaledContentY(),
          this.getScaledWidth(), this.getScaledHeight(), partialTicks);
    }

    if (this.backgroundColour != null) {
      RenderUtil.fill(this.getScaledX(), this.getScaledY(),
          this.getScaledX() + this.getScaledWidth(),
          this.getScaledY() + this.getScaledHeight(), this.backgroundColour.getHexColour());
    }

    RenderSystem.enableDepthTest();
    if (this.topBorderWidth > 0F) {
      RenderUtil.fill(this.getScaledX(), this.getScaledY(),
          this.getScaledX() + this.getScaledWidth(),
          this.getScaledY() + this.topBorderWidth, this.topBorderColour.getHexColour(), 0.5D);
    }
    if (this.rightBorderWidth > 0F) {
      RenderUtil.fill(this.getScaledX() + this.getScaledWidth() - this.rightBorderWidth,
          this.getScaledY(),
          this.getScaledX() + this.getScaledWidth(), this.getScaledY() + this.getScaledHeight(),
          this.rightBorderColour.getHexColour(), 0.5D);
    }
    if (this.bottomBorderWidth > 0F) {
      RenderUtil.fill(this.getScaledX(),
          this.getScaledY() + this.getScaledHeight() - this.bottomBorderWidth,
          this.getScaledX() + this.getScaledWidth(), this.getScaledY() + this.getScaledHeight(),
          this.bottomBorderColour.getHexColour(), 0.5D);
    }
    if (this.leftBorderWidth > 0F) {
      RenderUtil.fill(this.getScaledX(), this.getScaledY(),
          this.getScaledX() + this.leftBorderWidth,
          this.getScaledY() + this.getScaledHeight(), this.leftBorderColour.getHexColour(), 0.5D);
    }
    RenderSystem.disableDepthTest();

    if (this.tooltip != null && this.isMouseOver()) {
      this.tooltip.render(this.minecraft.fontRenderer, matrixStack,
          10.0D + this.getX() + this.getWidth(), this.getY());
    }
  }

  protected void mouseEntered(double mouseX, double mouseY) {
    this.mouseOver = true;
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

  protected void mouseLeft(double mouseX, double mouseY) {
    this.mouseOver = false;
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
    if (this.parent == null || !(this.parent instanceof ParentComponent)) {
      boolean mouseOver = this.isMouseOver((int) mouseX, (int) mouseY);
      boolean mouseWasOver = this.mouseOver;
      if (mouseOver && !mouseWasOver) {
        this.mouseEntered(mouseX, mouseY);
      } else if (this.mouseOver && !mouseOver) {
        this.mouseLeft(mouseX, mouseY);
      }
      this.mouseOver = mouseOver;
      this.post(new MouseEvent.MoveEvent(mouseX, mouseY));
    }
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    return this.post(new MouseEvent.ButtonEvent(mouseX, mouseY, button, GLFW.GLFW_PRESS));
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    return this.post(new MouseEvent.ButtonEvent(mouseX, mouseY, button, GLFW.GLFW_RELEASE));
  }

  @Override
  public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX,
      double deltaY) {
    return this.post(new MouseEvent.DragEvent(mouseX, mouseY, button, deltaX, deltaY));
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) {
    return this.post(new MouseEvent.ScrollEvent(mouseX, mouseY, scrollDelta));
  }

  @Override
  public boolean keyPressed(int key, int scancode, int mods) {
    return this.post(new KeyEvent(key, scancode, GLFW.GLFW_PRESS, mods));
  }

  @Override
  public boolean keyReleased(int key, int scancode, int mods) {
    return this.post(new KeyEvent(key, scancode, GLFW.GLFW_RELEASE, mods));
  }

  @Override
  public boolean charTyped(char character, int mods) {
    return this.post(new CharTypeEvent(character, mods));
  }

  @Override
  public boolean changeFocus(boolean forward) {
    this.focusChanged(!this.focused);
    return this.focused;
  }

  /**
   * Checks if mouse is over a component based on mouse position. Doesn't check whether there are
   * other components over this one.
   * 
   * @see #isMouseOver()
   */
  @Override
  public boolean isMouseOver(double mouseX, double mouseY) {
    return mouseX > this.getX() && mouseX < this.getX() + this.getWidth()
        && mouseY > this.getY() && mouseY < this.getY() + this.getHeight();
  }

  /**
   * Similar to {@link #isMouseOver(double, double)} but checks for components over it
   * 
   * @return {@code false} if mouse is not over the component or if there's another component over
   *         it
   * @see #isMouseOver(double, double)
   */
  protected boolean isMouseOver() {
    return this.mouseOver;
  }

  public final TweenManager getTweenManager() {
    return this.tweenManager;
  }

  public void close() {
    if (this.backgroundBlur != null) {
      this.backgroundBlur.close();
    }
    Yoga.YGNodeFree(this.node);
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

  public final SELF addClickSound(SoundEvent soundEvent) {
    return this.addListener(MouseEvent.ButtonEvent.class, (component, event) -> {
      if (event.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT
          && event.getAction() == GLFW.GLFW_PRESS) {
        Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(soundEvent, 1.0F));
      }
    });
  }

  public final SELF addActionListener(Consumer<SELF> consumer) {
    this.addListener(MouseEvent.ButtonEvent.class, (component, event) -> {
      if (event.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT
          && event.getAction() == GLFW.GLFW_PRESS) {
        consumer.accept(this.self());
      }
    });
    return this.self();
  }

  public final <T extends Event> SELF addListener(Class<T> eventType,
      BiConsumer<SELF, T> consumer) {
    this.eventBus
        .addListener(EventPriority.NORMAL, true, eventType,
            event -> consumer.accept(this.self(), event));
    return this.self();
  }

  /**
   * Submit the event for dispatch to appropriate listeners.
   *
   * @param event - The event to dispatch to listeners
   * @return true if the event was cancelled
   */
  public final boolean post(Event event) {
    return this.eventBus.post(event);
  }

  public final float getScaledX() {
    return this.getX() + (this.getWidth() - this.getScaledWidth()) / 2.0F;
  }

  public final float getScaledContentX() {
    return this.getContentX()
        + (this.getContentWidth() - this.getScaledContentWidth()) / 2.0F;
  }

  @Override
  public float getContentX() {
    return this.getX() + Yoga.YGNodeLayoutGetPadding(this.node, Yoga.YGEdgeLeft);
  }

  public final float getX() {
    return (float) (Yoga.YGNodeLayoutGetLeft(this.node) + this.xTranslation
        + (this.parent == null ? 0f : this.parent.getContentX())
        + (this.unscaleWidth ? this.getWidth() * this.mainWindow.getGuiScaleFactor() : 0.0F));
  }

  public final float getScaledContentY() {
    return this.getContentY()
        + (this.getContentHeight() - this.getScaledContentHeight()) / 2.0F;
  }

  public final float getScaledY() {
    return this.getY() + (this.getHeight() - this.getScaledHeight()) / 2.0F;
  }

  @Override
  public float getContentY() {
    return this.getY() + Yoga.YGNodeLayoutGetPadding(this.node, Yoga.YGEdgeTop);
  }

  public final float getY() {
    return (float) (Yoga.YGNodeLayoutGetTop(this.node) + this.yTranslation
        + (this.parent == null ? 0f : this.parent.getContentY())
        + (this.unscaleHeight ? this.getHeight() / 2.0F * this.mainWindow.getGuiScaleFactor()
            : 0.0F));
  }

  public final float getScaledWidth() {
    return this.getWidth() * this.xScale;
  }

  public final float getScaledContentWidth() {
    return this.getContentWidth() * this.xScale;
  }

  public float getContentWidth() {
    return this.getWidth() - Yoga.YGNodeLayoutGetPadding(this.node, Yoga.YGEdgeRight);
  }

  public final float getWidth() {
    return (float) (Yoga.YGNodeLayoutGetWidth(this.node)
        / (this.unscaleWidth ? this.mainWindow.getGuiScaleFactor() : 1.0F));
  }

  public float getScaledHeight() {
    return this.getHeight() * this.yScale;
  }

  public final float getScaledContentHeight() {
    return this.getContentHeight() * this.yScale;
  }

  public final float getContentHeight() {
    return this.getHeight() - Yoga.YGNodeLayoutGetPadding(this.node, Yoga.YGEdgeBottom);
  }

  public final float getHeight() {
    return (float) (Yoga.YGNodeLayoutGetHeight(this.node)
        / (this.unscaleHeight ? this.mainWindow.getGuiScaleFactor() : 1.0F));
  }

  public final float getXScale() {
    return this.xScale;
  }

  public SELF setZLevel(int zLevel) {
    if (this.zLevel != zLevel) {
      this.zLevel = zLevel;
      this.zLevelChanged();
    }
    return this.self();
  }

  public int getZLevel() {
    return zLevel;
  }

  public final SELF setXScale(float xScale) {
    this.xScale = xScale;
    return this.self();
  }

  public final float getYScale() {
    return this.yScale;
  }

  public final float getTopMargin() {
    return Yoga.YGNodeLayoutGetMargin(this.node, Yoga.YGEdgeTop);
  }

  public final float getRightMargin() {
    return Yoga.YGNodeLayoutGetMargin(this.node, Yoga.YGEdgeRight);
  }

  public final float getBottomMargin() {
    return Yoga.YGNodeLayoutGetMargin(this.node, Yoga.YGEdgeBottom);
  }

  public final float getLeftMargin() {
    return Yoga.YGNodeLayoutGetMargin(this.node, Yoga.YGEdgeLeft);
  }

  public final float getTopPadding() {
    return Yoga.YGNodeLayoutGetPadding(this.node, Yoga.YGEdgeTop);
  }

  public final float getRightPadding() {
    return Yoga.YGNodeLayoutGetPadding(this.node, Yoga.YGEdgeRight);
  }

  public final float getBottomPadding() {
    return Yoga.YGNodeLayoutGetPadding(this.node, Yoga.YGEdgeBottom);
  }

  public final float getLeftPadding() {
    return Yoga.YGNodeLayoutGetPadding(this.node, Yoga.YGEdgeLeft);
  }

  public final SELF setYScale(float yScale) {
    this.yScale = yScale;
    return this.self();
  }

  public final SELF setScale(float scale) {
    return this.setXScale(scale).setYScale(scale);
  }

  public SELF setWidth(float width) {
    Yoga.YGNodeStyleSetWidth(this.node, width);
    return this.self();
  }

  public final SELF setWidthPercent(float widthPercent) {
    Yoga.YGNodeStyleSetWidthPercent(this.node, widthPercent);
    return this.self();
  }

  public final SELF setWidthAuto() {
    Yoga.YGNodeStyleSetWidthAuto(this.node);
    return this.self();
  }

  public SELF setMaxWidth(float maxWidth) {
    Yoga.YGNodeStyleSetMaxWidth(this.node, maxWidth);
    return this.self();
  }

  public final SELF setHeight(float height) {
    Yoga.YGNodeStyleSetHeight(this.node, height);
    return this.self();
  }

  public final SELF setHeightPercent(float heightPercent) {
    Yoga.YGNodeStyleSetHeightPercent(this.node, heightPercent);
    return this.self();
  }

  public final SELF setHeightAuto() {
    Yoga.YGNodeStyleSetHeightAuto(this.node);
    return this.self();
  }

  public SELF setMaxHeight(float maxHeight) {
    Yoga.YGNodeStyleSetMaxHeight(this.node, maxHeight);
    return this.self();
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

  public final SELF testDirection() {
    Yoga.YGNodeStyleSetDirection(this.node, Yoga.YGDirectionRTL);
    return this.self();
  }

  public final SELF setLeft(float left) {
    Yoga.YGNodeStyleSetPosition(this.node, Yoga.YGEdgeLeft, left);
    return this.self();
  }

  public final SELF setLeftPercent(float leftPercent) {
    Yoga.YGNodeStyleSetPositionPercent(this.node, Yoga.YGEdgeLeft, leftPercent);
    return this.self();
  }

  public final SELF setRight(float right) {
    Yoga.YGNodeStyleSetPosition(this.node, Yoga.YGEdgeRight, right);
    return this.self();
  }

  public final SELF setRightPercent(float rightPercent) {
    Yoga.YGNodeStyleSetPositionPercent(this.node, Yoga.YGEdgeRight, rightPercent);
    return this.self();
  }

  public final SELF setTop(float top) {
    Yoga.YGNodeStyleSetPosition(this.node, Yoga.YGEdgeTop, top);
    return this.self();
  }

  public final SELF setTopPercent(float topPercent) {
    Yoga.YGNodeStyleSetPositionPercent(this.node, Yoga.YGEdgeTop, topPercent);
    return this.self();
  }

  public final SELF setBottom(float bottom) {
    Yoga.YGNodeStyleSetPosition(this.node, Yoga.YGEdgeBottom, bottom);
    return this.self();
  }

  public final SELF setBottomPercent(float bottomPercent) {
    Yoga.YGNodeStyleSetPositionPercent(this.node, Yoga.YGEdgeBottom, bottomPercent);
    return this.self();
  }

  public final SELF setLeftPadding(float leftPadding) {
    Yoga.YGNodeStyleSetPadding(this.node, Yoga.YGEdgeLeft, leftPadding);
    return this.self();
  }

  public final SELF setLeftPaddingPercent(float leftPaddingPercent) {
    Yoga.YGNodeStyleSetPaddingPercent(this.node, Yoga.YGEdgeLeft, leftPaddingPercent);
    return this.self();
  }

  public final SELF setRightPadding(float rightPadding) {
    Yoga.YGNodeStyleSetPadding(this.node, Yoga.YGEdgeRight, rightPadding);
    return this.self();
  }

  public final SELF setRightPaddingPercent(float rightPaddingPercent) {
    Yoga.YGNodeStyleSetPaddingPercent(this.node, Yoga.YGEdgeRight, rightPaddingPercent);
    return this.self();
  }

  public final SELF setTopPadding(float topPadding) {
    Yoga.YGNodeStyleSetPadding(this.node, Yoga.YGEdgeTop, topPadding);
    return this.self();
  }

  public final SELF setTopPaddingPercent(float topPaddingPercent) {
    Yoga.YGNodeStyleSetPaddingPercent(this.node, Yoga.YGEdgeTop, topPaddingPercent);
    return this.self();
  }

  public final SELF setBottomPadding(float bottomPadding) {
    Yoga.YGNodeStyleSetPadding(this.node, Yoga.YGEdgeBottom, bottomPadding);
    return this.self();
  }

  public final SELF setBottomPaddingPercent(float bottomPaddingPercent) {
    Yoga.YGNodeStyleSetPaddingPercent(this.node, Yoga.YGEdgeBottom, bottomPaddingPercent);
    return this.self();
  }

  public final SELF setPadding(float padding) {
    Yoga.YGNodeStyleSetPadding(this.node, Yoga.YGEdgeAll, padding);
    return this.self();
  }

  public final SELF setPaddingPercent(float paddingPercent) {
    Yoga.YGNodeStyleSetPaddingPercent(this.node, Yoga.YGEdgeAll, paddingPercent);
    return this.self();
  }

  public final SELF setLeftMargin(float leftMargin) {
    Yoga.YGNodeStyleSetMargin(this.node, Yoga.YGEdgeLeft, leftMargin);
    return this.self();
  }

  public final SELF setLeftMarginPercent(float leftMarginPercent) {
    Yoga.YGNodeStyleSetMarginPercent(this.node, Yoga.YGEdgeLeft, leftMarginPercent);
    return this.self();
  }

  public final SELF setRightMargin(float rightMargin) {
    Yoga.YGNodeStyleSetMargin(this.node, Yoga.YGEdgeRight, rightMargin);
    return this.self();
  }

  public final SELF setRightMarginPercent(float rightMarginPercent) {
    Yoga.YGNodeStyleSetMarginPercent(this.node, Yoga.YGEdgeRight, rightMarginPercent);
    return this.self();
  }

  public final SELF setTopMargin(float topMargin) {
    Yoga.YGNodeStyleSetMargin(this.node, Yoga.YGEdgeTop, topMargin);
    return this.self();
  }

  public final SELF setTopMarginPercent(float topMarginPercent) {
    Yoga.YGNodeStyleSetMarginPercent(this.node, Yoga.YGEdgeTop, topMarginPercent);
    return this.self();
  }

  public final SELF setBottomMargin(float bottomMargin) {
    Yoga.YGNodeStyleSetMargin(this.node, Yoga.YGEdgeBottom, bottomMargin);
    return this.self();
  }

  public final SELF setBottomMarginPercent(float bottomMarginPercent) {
    Yoga.YGNodeStyleSetMarginPercent(this.node, Yoga.YGEdgeBottom, bottomMarginPercent);
    return this.self();
  }

  public final SELF setMargin(float margin) {
    Yoga.YGNodeStyleSetMargin(this.node, Yoga.YGEdgeAll, margin);
    return this.self();
  }

  public final SELF setMarginPercent(float marginPercent) {
    Yoga.YGNodeStyleSetMarginPercent(this.node, Yoga.YGEdgeAll, marginPercent);
    return this.self();
  }

  public final SELF setLeftMarginAuto() {
    Yoga.YGNodeStyleSetMarginAuto(this.node, Yoga.YGEdgeLeft);
    return this.self();
  }

  public final SELF setRightMarginAuto() {
    Yoga.YGNodeStyleSetMarginAuto(this.node, Yoga.YGEdgeRight);
    return this.self();
  }

  public final SELF setTopMarginAuto() {
    Yoga.YGNodeStyleSetMarginAuto(this.node, Yoga.YGEdgeTop);
    return this.self();
  }

  public final SELF setBottomMarginAuto() {
    Yoga.YGNodeStyleSetMarginAuto(this.node, Yoga.YGEdgeBottom);
    return this.self();
  }

  public final SELF setMarginAuto() {
    Yoga.YGNodeStyleSetMarginAuto(this.node, Yoga.YGEdgeAll);
    return this.self();
  }

  public final SELF setPositionType(PositionType positionType) {
    Yoga.YGNodeStyleSetPositionType(this.node,
        positionType == PositionType.ABSOLUTE ? Yoga.YGPositionTypeAbsolute
            : Yoga.YGPositionTypeRelative);
    return this.self();
  }

  public final SELF setFlexGrow(float flexGrow) {
    Yoga.YGNodeStyleSetFlexGrow(this.node, flexGrow);
    return this.self();
  }

  public final SELF setFlexShrink(float flexShrink) {
    Yoga.YGNodeStyleSetFlexShrink(this.node, flexShrink);
    return this.self();
  }

  public final SELF setFlexBasis(float flexBasis) {
    Yoga.YGNodeStyleSetFlexBasis(this.node, flexBasis);
    return this.self();
  }

  public final SELF setFlex(float flex) {
    Yoga.YGNodeStyleSetFlex(this.node, flex);
    return this.self();
  }

  public final SELF setAspectRatio(float aspectRatio) {
    Yoga.YGNodeStyleSetAspectRatio(this.node, aspectRatio);
    return this.self();
  }

  public final SELF setAlignSelf(Align align) {
    Yoga.YGNodeStyleSetAlignSelf(this.node, align.getYogaType());
    return this.self();
  }

  public final SELF setDisplay(Display display) {
    Yoga.nYGNodeStyleSetDisplay(this.node, display.getYogaType());
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

  public final SELF setUnscaleWidth() {
    this.unscaleWidth = true;
    return this.self();
  }

  public final SELF setUnscaleHeight() {
    this.unscaleHeight = true;
    return this.self();
  }

  public final Screen getScreen() {
    return this.parent.getScreen();
  }

  @SuppressWarnings("unchecked")
  protected final SELF self() {
    return (SELF) this;
  }
}
