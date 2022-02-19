/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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

package com.craftingdead.immerse.client.gui.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import org.lwjgl.glfw.GLFW;
import com.craftingdead.immerse.client.gui.view.event.ActionEvent;
import com.craftingdead.immerse.client.gui.view.layout.MeasureMode;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;

public class DropdownView extends View implements ContainerEventHandler {

  public static final int DEFAULT_HEIGHT = 14;
  public static final int DEFAULT_ITEM_BACKGROUND_COLOUR = 0x444444;
  public static final int DEFAULT_SELECTED_ITEM_BACKGROUND_COLOUR = 0x222222;
  public static final int DEFAULT_HOVERED_ITEM_BACKGROUND_COLOUR = 0x333333;

  public static final int Z_OFFSET = 5;
  public static final float DEFAULT_ARROW_WIDTH = 12.0F;
  public static final float DEFAULT_ARROW_HEIGHT = 5.0F;
  public static final float DEFAULT_ARROW_LINE_WIDTH = 1.6F;
  public static final float DEFAULT_X_ARROW_OFFSET = 0.18F;

  private final List<Item> items = new ArrayList<>();

  private int itemBackgroundColour;
  private int selectedItemBackgroundColour;
  private int hoveredItemBackgroundColour;

  private boolean expanded = false;
  private int focusedItemIndex;
  private Item selectedItem;

  private float arrowWidth;
  private float arrowHeight;
  private float arrowLineWidth;
  private float arrowLineWidthX;
  private float arrowLineWidthY;
  private float xArrowOffset;

  private boolean dragging;

  private long fadeStartTimeMs;

  @Nullable
  private SoundEvent expandSound;
  @Nullable
  private SoundEvent itemHoverSound;

  @Nullable
  private GuiEventListener lastHoveredListener;

  public DropdownView(Properties<?> properties) {
    super(properties);
    this.addListener(ActionEvent.class, event -> {
      if (this.expanded) {
        this.getFocusedItem().select();
      }
      this.toggleExpanded();
    });

    this.itemBackgroundColour = DEFAULT_ITEM_BACKGROUND_COLOUR;
    this.selectedItemBackgroundColour = DEFAULT_SELECTED_ITEM_BACKGROUND_COLOUR;
    this.hoveredItemBackgroundColour = DEFAULT_HOVERED_ITEM_BACKGROUND_COLOUR;
    this.arrowWidth = DEFAULT_ARROW_WIDTH;
    this.arrowHeight = DEFAULT_ARROW_HEIGHT;
    this.arrowLineWidth = DEFAULT_ARROW_LINE_WIDTH;
    this.calculateArrowLineWidthProjections();
    this.xArrowOffset = DEFAULT_X_ARROW_OFFSET;
  }

  @Override
  public float getZOffset() {
    return super.getZOffset() + Z_OFFSET;
  }

  @Override
  protected boolean isFocusable() {
    return true;
  }

  protected Vec2 measure(MeasureMode widthMode, float width, MeasureMode heightMode,
      float height) {
    return new Vec2(width, DEFAULT_HEIGHT);
  }

  public DropdownView setArrowWidth(float arrowWidth) {
    this.arrowWidth = arrowWidth;
    this.calculateArrowLineWidthProjections();
    return this;
  }

  public DropdownView setArrowHeight(float arrowHeight) {
    this.arrowHeight = arrowHeight;
    this.calculateArrowLineWidthProjections();
    return this;
  }

  public DropdownView setArrowLineWidth(float arrowLineWidth) {
    this.arrowLineWidth = arrowLineWidth;
    this.calculateArrowLineWidthProjections();
    return this;
  }

  /**
   * @param xArrowOffset RTL offset from 0.0F to 1.0F (something like percent margin right)
   */
  public DropdownView setXArrowOffset(float xArrowOffset) {
    this.xArrowOffset = xArrowOffset;
    return this;
  }

  public DropdownView setItemBackgroundColour(int itemBackgroundColour) {
    this.itemBackgroundColour = itemBackgroundColour;
    return this;
  }

  public DropdownView setSelectedItemBackgroundColour(int selectedItemBackgroundColour) {
    this.selectedItemBackgroundColour = selectedItemBackgroundColour;
    return this;
  }

  public DropdownView setHoveredItemBackgroundColour(int hoveredItemBackgroundColour) {
    this.hoveredItemBackgroundColour = hoveredItemBackgroundColour;
    return this;
  }

  public DropdownView addItem(Component text, Runnable actionListener) {
    this.items.add(new Item(this.items.size(), text, actionListener));
    return this;
  }

  public DropdownView setDisabled(int itemId, boolean disabled) {
    this.items.get(itemId).setDisabled(disabled);
    return this;
  }

  public DropdownView setExpandSound(@Nullable SoundEvent expandSound) {
    this.expandSound = expandSound;
    return this;
  }

  public DropdownView setItemHoverSound(@Nullable SoundEvent itemHoverSound) {
    this.itemHoverSound = itemHoverSound;
    return this;
  }

  private Item getFocusedItem() {
    return this.items.get(this.focusedItemIndex);
  }

  @Override
  public void mouseMoved(double mouseX, double mouseY) {
    super.mouseMoved(mouseX, mouseY);
    var hoveredListener = this.getChildAt(mouseX, mouseY).orElse(null);
    if (hoveredListener instanceof DropdownView.Item
        && hoveredListener != this.lastHoveredListener
        && this.itemHoverSound != null) {
      this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(this.itemHoverSound, 1.0F));
    }
    this.lastHoveredListener = hoveredListener;
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (this.expanded) {
      if (!ContainerEventHandler.super.mouseClicked(mouseX, mouseY, button) && !this.isHovered()) {
        this.toggleExpanded();
      }
    }
    return super.mouseClicked(mouseX, mouseY, button);
  }

  @Override
  protected void layout() {
    super.layout();
  }

  @Override
  protected void added() {
    super.added();
    if (this.items.size() > 0) {
      this.items.get(0).select();
    }
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (keyCode == GLFW.GLFW_KEY_DOWN) {
      this.focusedItemIndex = Math.min(this.focusedItemIndex + 1, this.items.size() - 1);
    } else if (keyCode == GLFW.GLFW_KEY_UP) {
      this.focusedItemIndex = Math.max(this.focusedItemIndex - 1, 0);
    }

    if (!this.expanded) {
      this.getFocusedItem().select();
    }

    return super.keyPressed(keyCode, scanCode, modifiers);
  }

  @Override
  public List<? extends GuiEventListener> children() {
    return Collections.unmodifiableList(this.items);
  }

  protected void toggleExpanded() {
    this.fadeStartTimeMs = Util.getMillis();
    this.expanded = !this.expanded;
    if (this.expandSound != null) {
      this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(this.expandSound, 1.0F));
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
  public void renderContent(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
    super.renderContent(poseStack, mouseX, mouseY, partialTicks);

    this.selectedItem.render(poseStack, Type.SELECTED, 255 << 24);
    this.renderArrow(poseStack);

    float alpha = Mth.clamp((Util.getMillis() - this.fadeStartTimeMs) / 100.0F, 0.0F, 1.0F);
    if (!this.expanded) {
      alpha = 1.0F - alpha;
    }
    int opacity = Mth.ceil(alpha * 255.0F) << 24;
    if ((opacity & 0xFC000000) != 0) {
      for (var item : this.items) {
        Type type;
        if (item.disabled) {
          type = Type.DISABLED;
        } else if (item == this.selectedItem) {
          type = Type.HIGHLIGHTED;
        } else if (item.isMouseOver(mouseX, mouseY) || this.focusedItemIndex == item.index) {
          type = Type.HOVERED;
        } else {
          type = Type.NONE;
        }

        item.render(poseStack, type, opacity);
      }
    }
  }

  private void renderArrow(PoseStack poseStack) {
    // TODO make it smoother around the edges?
    poseStack.pushPose();
    {
      var xOffset =
          this.getScaledContentX() + this.getScaledContentWidth() * (1 - this.xArrowOffset);
      var yOffset =
          (this.getScaledContentY() + (this.getScaledContentHeight() - this.arrowHeight) / 2d);
      poseStack.translate(xOffset, yOffset, 0);
      RenderSystem.setShader(GameRenderer::getPositionShader);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      final var matrix = poseStack.last().pose();
      final var tessellator = Tesselator.getInstance();
      final var builder = tessellator.getBuilder();
      builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
      builder.vertex(matrix, 0.0F, 0.0F, 0.0F).endVertex();
      builder.vertex(matrix, this.arrowWidth / 2.0F, this.arrowHeight, 0.0F).endVertex();
      builder.vertex(matrix, this.arrowWidth / 2.0F, this.arrowHeight - this.arrowLineWidthY, 0.0F)
          .endVertex();
      builder.vertex(matrix, this.arrowLineWidthX, 0.0F, 0.0F).endVertex();
      tessellator.end();
      builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
      builder.vertex(matrix, this.arrowWidth - this.arrowLineWidthX, 0.0F, 0.0F).endVertex();
      builder.vertex(matrix, this.arrowWidth / 2.0F, this.arrowHeight - this.arrowLineWidthY, 0.0F)
          .endVertex();
      builder.vertex(matrix, this.arrowWidth / 2.0F, this.arrowHeight, 0.0F).endVertex();
      builder.vertex(matrix, this.arrowWidth, 0.0F, 0.0F).endVertex();
      tessellator.end();
    }
    poseStack.popPose();
  }

  private void calculateArrowLineWidthProjections() {
    var arrowLinePitchRad =
        Math.toRadians(90) - Math.atan(this.arrowWidth / 2.0F / this.arrowHeight);
    this.arrowLineWidthX = this.arrowLineWidth / (float) Math.sin(arrowLinePitchRad);
    this.arrowLineWidthY =
        this.arrowHeight
            - (float) Math.tan(arrowLinePitchRad) * (this.arrowWidth / 2.0F - this.arrowLineWidthX);
  }

  private enum Type {
    HIGHLIGHTED, SELECTED, DISABLED, HOVERED, NONE;
  }

  public class Item implements GuiEventListener {

    private final int index;
    private final Component text;
    private final Runnable actionListener;

    private boolean disabled = false;

    public Item(int index, Component text, Runnable actionListener) {
      this.index = index;
      this.text = text;
      this.actionListener = actionListener;
    }

    private void render(PoseStack poseStack, Type type, int opacity) {
      float y = this.getY();

      int backgroundColour = DropdownView.this.itemBackgroundColour;
      int textColour = ChatFormatting.GRAY.getColor();

      switch (type) {
        case SELECTED:
          y = DropdownView.this.getScaledContentY();
          backgroundColour ^= 0x000000;
          backgroundColour += 128 << 24;
          textColour = ChatFormatting.WHITE.getColor();
          break;
        case HIGHLIGHTED:
          backgroundColour = DropdownView.this.selectedItemBackgroundColour;
          break;
        case DISABLED:
          textColour = ChatFormatting.DARK_GRAY.getColor();
          break;
        case HOVERED:
          backgroundColour = DropdownView.this.hoveredItemBackgroundColour;
          break;
        default:
          break;
      }

      this.render(poseStack, DropdownView.this.getScaledContentX(), y,
          DropdownView.this.getScaledContentWidth(), DropdownView.this.getItemHeight(),
          backgroundColour | opacity, textColour | opacity);
    }

    private void render(PoseStack poseStack, float x, float y, float width, float height,
        int backgroundColour, int textColour) {
      RenderSystem.setShader(GameRenderer::getPositionColorShader);
      RenderUtil.fillWidthHeight(poseStack, x, y, width, height, backgroundColour);

      var font = DropdownView.this.minecraft.font;

      poseStack.pushPose();
      {
        poseStack.translate(0.0D, 0.0D, 400.0D);
        float textY = y + (height - DropdownView.this.minecraft.font.lineHeight) / 2 + 1;
        for (FormattedCharSequence line : font.split(this.text, (int) width)) {
          font.draw(poseStack, line, x + 3, textY, textColour);
          textY += DropdownView.this.minecraft.font.lineHeight;
        }
      }
      poseStack.popPose();
    }

    private void select() {
      if (DropdownView.this.selectedItem != this) {
        this.actionListener.run();
        DropdownView.this.selectedItem = this;
      }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (!this.disabled && this.isMouseOver(mouseX, mouseY)) {
        DropdownView.this.focusedItemIndex = this.index;
        return true;
      }
      return false;
    }

    public void setDisabled(boolean disabled) {
      this.disabled = disabled;
    }

    private float getY() {
      return DropdownView.this.getScaledContentY()
          + DropdownView.this.getScaledContentHeight()
          + DropdownView.this.getItemHeight() * this.index;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
      final float y = this.getY();
      return DropdownView.this.isMouseOver(mouseX, mouseY) && mouseY >= y
          && mouseY <= y + DropdownView.this.getItemHeight();
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
  public GuiEventListener getFocused() {
    return null;
  }

  @Override
  public void setFocused(@Nullable GuiEventListener focusedListener) {}
}
