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
import java.util.List;
import javax.annotation.Nullable;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.craftingdead.immerse.client.gui.view.layout.MeasureMode;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class DropDownView<L extends Layout> extends View<DropDownView<L>, L>
    implements INestedGuiEventHandler {

  public static final int DEFAULT_HEIGHT = 14;
  public static final int DEFAULT_ITEM_BACKGROUND_COLOUR = 0x444444;
  public static final int DEFAULT_SELECTED_ITEM_BACKGROUND_COLOUR = 0x222222;
  public static final int DEFAULT_HOVERED_ITEM_BACKGROUND_COLOUR = 0x333333;

  public static final int DEFAULT_Z_LEVEL = 5;
  public static final double DEFAULT_ARROW_WIDTH = 12D;
  public static final double DEFAULT_ARROW_HEIGHT = 5D;
  public static final double DEFAULT_ARROW_LINE_WIDTH = 1.6D;
  public static final double DEFAULT_X_ARROW_OFFSET = 0.18D;

  private final List<Item> items = new ArrayList<>();

  private int itemBackgroundColour;
  private int selectedItemBackgroundColour;
  private int hoveredItemBackgroundColour;

  private boolean expanded = false;
  private int selectedItemIndex = -1;
  private boolean init = false;

  private double arrowWidth;
  private double arrowHeight;
  private double arrowLineWidth;
  private double arrowLineWidthX;
  private double arrowLineWidthY;
  private double xArrowOffset;

  @Nullable
  private IGuiEventListener focusedListener;
  private boolean dragging;

  private long fadeStartTimeMs;

  @Nullable
  private SoundEvent expandSound;
  @Nullable
  private SoundEvent itemHoverSound;

  @Nullable
  private IGuiEventListener lastHoveredListener;

  public DropDownView(L layout) {
    super(layout);
    this.itemBackgroundColour = DEFAULT_ITEM_BACKGROUND_COLOUR;
    this.selectedItemBackgroundColour = DEFAULT_SELECTED_ITEM_BACKGROUND_COLOUR;
    this.hoveredItemBackgroundColour = DEFAULT_HOVERED_ITEM_BACKGROUND_COLOUR;
    this.setZOffset(DEFAULT_Z_LEVEL);
    this.arrowWidth = DEFAULT_ARROW_WIDTH;
    this.arrowHeight = DEFAULT_ARROW_HEIGHT;
    this.arrowLineWidth = DEFAULT_ARROW_LINE_WIDTH;
    this.calculateArrowLineWidthProjections();
    this.xArrowOffset = DEFAULT_X_ARROW_OFFSET;
  }

  protected Vector2f measure(MeasureMode widthMode, float width, MeasureMode heightMode,
      float height) {
    return new Vector2f(width, DEFAULT_HEIGHT);
  }

  public DropDownView<L> setArrowWidth(double arrowWidth) {
    this.arrowWidth = arrowWidth;
    this.calculateArrowLineWidthProjections();
    return this;
  }

  public DropDownView<L> setArrowHeight(double arrowHeight) {
    this.arrowHeight = arrowHeight;
    this.calculateArrowLineWidthProjections();
    return this;
  }

  public DropDownView<L> setArrowLineWidth(double arrowLineWidth) {
    this.arrowLineWidth = arrowLineWidth;
    this.calculateArrowLineWidthProjections();
    return this;
  }

  /**
   * @param xArrowOffset RTL offset from 0.0D to 1.0D (something like percent margin right)
   */
  public DropDownView<L> setXArrowOffset(double xArrowOffset) {
    this.xArrowOffset = xArrowOffset;
    return this;
  }

  public DropDownView<L> setItemBackgroundColour(int itemBackgroundColour) {
    this.itemBackgroundColour = itemBackgroundColour;
    return this;
  }

  public DropDownView<L> setSelectedItemBackgroundColour(int selectedItemBackgroundColour) {
    this.selectedItemBackgroundColour = selectedItemBackgroundColour;
    return this;
  }

  public DropDownView<L> setHoveredItemBackgroundColour(int hoveredItemBackgroundColour) {
    this.hoveredItemBackgroundColour = hoveredItemBackgroundColour;
    return this;
  }

  public DropDownView<L> addItem(ITextComponent text, Runnable actionListener) {
    this.items.add(new Item(this.items.size(), text, actionListener));
    return this;
  }

  public DropDownView<L> setDisabled(int itemId, boolean disabled) {
    this.items.get(itemId).setDisabled(disabled);
    return this;
  }

  public DropDownView<L> setExpandSound(@Nullable SoundEvent expandSound) {
    this.expandSound = expandSound;
    return this;
  }

  public DropDownView<L> setItemHoverSound(@Nullable SoundEvent itemHoverSound) {
    this.itemHoverSound = itemHoverSound;
    return this;
  }

  public Item getSelectedItem() {
    return this.items.get(this.selectedItemIndex);
  }

  @Override
  public void mouseMoved(double mouseX, double mouseY) {
    super.mouseMoved(mouseX, mouseY);
    IGuiEventListener hoveredListener = this.getChildAt(mouseX, mouseY).orElse(null);
    if (hoveredListener instanceof DropDownView.Item
        && hoveredListener != this.lastHoveredListener
        && this.itemHoverSound != null) {
      this.minecraft.getSoundManager().play(SimpleSound.forUI(this.itemHoverSound, 1.0F));
    }
    this.lastHoveredListener = hoveredListener;
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (super.mouseClicked(mouseX, mouseY, button)) {
      return true;
    }

    if (this.expanded || this.isMouseOver(mouseX, mouseY)) {
      if (this.expanded) {
        INestedGuiEventHandler.super.mouseClicked(mouseX, mouseY, button);
      }
      this.toggleExpanded();
      return true;
    }

    return false;
  }

  @Override
  protected void layout() {
    super.layout();
    this.init();
  }

  private void init() {
    if (this.init) {
      return;
    }
    this.init = true;
    if (this.selectedItemIndex == -1 && this.items.size() > 0) {
      this.selectedItemIndex = 0;
      this.items.get(0).actionListener.run();
    }
  }

  @Override
  public boolean changeFocus(boolean forward) {
    if (this.expanded) {
      if (!INestedGuiEventHandler.super.changeFocus(forward)) {
        this.toggleExpanded();
      }
    } else {
      this.toggleExpanded();
    }
    return this.expanded;
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    return INestedGuiEventHandler.super.keyPressed(keyCode, scanCode, modifiers)
        || super.keyPressed(keyCode, scanCode, modifiers);
  }

  @Override
  public List<? extends IGuiEventListener> children() {
    return Collections.unmodifiableList(this.items);
  }

  protected void toggleExpanded() {
    this.fadeStartTimeMs = Util.getMillis();
    this.expanded = !this.expanded;
    if (this.expandSound != null) {
      this.minecraft.getSoundManager().play(SimpleSound.forUI(this.expandSound, 1.0F));
    }
  }

  @Override
  public boolean isMouseOver(double mouseX, double mouseY) {
    return mouseX > this.getScaledX() && mouseX < this.getScaledX() + this.getScaledWidth() &&
        mouseY > this.getScaledY() && mouseY < this.getScaledY() + this.getScaledHeight()
            + (this.expanded ? this.items.size() * this.getItemHeight() : 0);
  }

  protected int getItemHeight() {
    return (int) this.getScaledContentHeight();
  }

  @Override
  public void renderContent(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    super.renderContent(matrixStack, mouseX, mouseY, partialTicks);

    this.items.get(this.selectedItemIndex).render(matrixStack, Type.SELECTED, 255 << 24);
    this.renderArrow();

    float alpha = MathHelper.clamp((Util.getMillis() - this.fadeStartTimeMs) / 100.0F, 0.0F, 1.0F);
    if (!this.expanded) {
      alpha = 1.0F - alpha;
    }
    int opacity = MathHelper.ceil(alpha * 255.0F) << 24;
    if ((opacity & 0xFC000000) != 0) {
      for (Item item : this.items) {
        Type type;
        if (item.disabled) {
          type = Type.DISABLED;
        } else if (item.index == this.selectedItemIndex) {
          type = Type.HIGHLIGHTED;
        } else if (item.isMouseOver(mouseX, mouseY) || this.focusedListener == item) {
          type = Type.HOVERED;
        } else {
          type = Type.NONE;
        }

        item.render(matrixStack, type, opacity);
      }
    }
  }

  @SuppressWarnings("deprecation")
  private void renderArrow() {
    // TODO make it smoother around the edges?
    RenderSystem.pushMatrix();
    {
      double xOffset =
          this.getScaledContentX() + this.getScaledContentWidth() * (1 - this.xArrowOffset);
      double yOffset =
          (this.getScaledContentY() + (this.getScaledContentHeight() - this.arrowHeight) / 2d);
      RenderSystem.translated(xOffset, yOffset, 0);
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder builder = tessellator.getBuilder();
      builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
      builder.vertex(0, 0, 0.0D).endVertex();
      builder.vertex(this.arrowWidth / 2.0D, this.arrowHeight, 0.0D).endVertex();
      builder.vertex(this.arrowWidth / 2.0D, this.arrowHeight - this.arrowLineWidthY, 0.0D)
          .endVertex();
      builder.vertex(this.arrowLineWidthX, 0, 0.0D).endVertex();
      tessellator.end();
      builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
      builder.vertex(this.arrowWidth - this.arrowLineWidthX, 0, 0.0D).endVertex();
      builder.vertex(this.arrowWidth / 2.0D, this.arrowHeight - this.arrowLineWidthY, 0.0D)
          .endVertex();
      builder.vertex(this.arrowWidth / 2.0D, this.arrowHeight, 0.0D).endVertex();
      builder.vertex(this.arrowWidth, 0, 0.0D).endVertex();
      tessellator.end();
    }
    RenderSystem.popMatrix();
  }

  private void calculateArrowLineWidthProjections() {
    double arrowLinePitchRad =
        Math.toRadians(90) - Math.atan(this.arrowWidth / 2D / this.arrowHeight);
    this.arrowLineWidthX = this.arrowLineWidth / Math.sin(arrowLinePitchRad);
    this.arrowLineWidthY =
        arrowHeight - Math.tan(arrowLinePitchRad) * (this.arrowWidth / 2D - this.arrowLineWidthX);
  }

  private enum Type {
    HIGHLIGHTED, SELECTED, DISABLED, HOVERED, NONE;
  }

  public class Item implements IGuiEventListener {

    private final int index;
    private final ITextComponent text;
    private final Runnable actionListener;

    private boolean disabled = false;

    public Item(int index, ITextComponent text, Runnable actionListener) {
      this.index = index;
      this.text = text;
      this.actionListener = actionListener;
    }

    private void render(MatrixStack matrixStack, Type type, int opacity) {
      float y = this.getY();

      int backgroundColour = DropDownView.this.itemBackgroundColour;
      int textColour = TextFormatting.GRAY.getColor();

      switch (type) {
        case SELECTED:
          y = DropDownView.this.getScaledContentY();
          backgroundColour ^= 0x000000;
          backgroundColour += 128 << 24;
          textColour = TextFormatting.WHITE.getColor();
          break;
        case HIGHLIGHTED:
          backgroundColour = DropDownView.this.selectedItemBackgroundColour;
          break;
        case DISABLED:
          textColour = TextFormatting.DARK_GRAY.getColor();
          break;
        case HOVERED:
          backgroundColour = DropDownView.this.hoveredItemBackgroundColour;
          break;
        default:
          break;
      }

      this.render(matrixStack, DropDownView.this.getScaledContentX(), y,
          DropDownView.this.getScaledContentWidth(), DropDownView.this.getItemHeight(),
          backgroundColour | opacity, textColour | opacity);
    }

    private void render(MatrixStack matrixStack, float x, float y, float width, float height,
        int backgroundColour, int textColour) {
      RenderUtil.fillWidthHeight(matrixStack, x, y, width, height, backgroundColour);

      FontRenderer font = DropDownView.this.minecraft.font;

      matrixStack.pushPose();
      {
        matrixStack.translate(0.0D, 0.0D, 400.0D);
        float textY = y + (height - DropDownView.this.minecraft.font.lineHeight) / 2 + 1;
        for (IReorderingProcessor line : font.split(this.text, (int) width)) {
          font.draw(matrixStack, line, x + 3, textY, textColour);
          textY += DropDownView.this.minecraft.font.lineHeight;
        }
      }
      matrixStack.popPose();
    }

    private void click() {
      DropDownView.this.selectedItemIndex = this.index;
      this.actionListener.run();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (keyCode == GLFW.GLFW_KEY_ENTER) {
        this.click();
        return true;
      }
      return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (!this.disabled && this.index != DropDownView.this.selectedItemIndex
          && this.isMouseOver(mouseX, mouseY)) {
        this.click();
        return true;
      }
      return false;
    }

    @Override
    public boolean changeFocus(boolean forward) {
      return !this.disabled && DropDownView.this.selectedItemIndex != this.index
          && DropDownView.this.focusedListener != this;
    }

    public void setDisabled(boolean disabled) {
      this.disabled = disabled;
    }

    private float getY() {
      return DropDownView.this.getScaledContentY()
          + DropDownView.this.getScaledContentHeight()
          + DropDownView.this.getItemHeight() * this.index;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
      final float y = this.getY();
      return DropDownView.this.isMouseOver(mouseX, mouseY) && mouseY >= y
          && mouseY <= y + DropDownView.this.getItemHeight();
    }
  }

  @Override
  public final boolean isDragging() {
    return this.dragging;
  }

  @Override
  public final void setDragging(boolean dragging) {
    this.dragging = dragging;
  }

  @Nullable
  @Override
  public IGuiEventListener getFocused() {
    return this.focusedListener;
  }

  @Override
  public void setFocused(@Nullable IGuiEventListener focusedListener) {
    this.focusedListener = focusedListener;
  }
}
