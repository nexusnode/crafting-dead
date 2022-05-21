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

import org.jetbrains.annotations.Nullable;
import com.craftingdead.immerse.client.gui.view.event.ActionEvent;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;

public class TabsView extends ParentView {

  private TabView selectedTab = null;

  public TabsView(Properties<?> properties) {
    super(properties);
  }

  public TabsView addTab(TabView tab) {
    this.addChild(tab);
    tab.addListener(ActionEvent.class, event -> this.changeTab(tab));
    return this;
  }

  @Override
  public boolean changeFocus(boolean forward) {
    return super.changeFocus(forward);
  }

  @Override
  protected void added() {
    super.added();
    if (this.getChildViews().size() > 0) {
      this.changeTab((TabView) this.getChildViews().get(0));
    }
  }

  private void changeTab(TabView newTab) {
    var previousTab = this.selectedTab;
    this.selectedTab = newTab;
    if (previousTab != newTab) {
      if (previousTab != null) {
        previousTab.setSelected(false);
      }
      newTab.setSelected(true);
    }
  }

  public static class TabView extends TextView {

    public static final Color DEFAULT_UNDERSCORE_COLOR = Color.WHITE;
    public static final float DEFAULT_UNDERSCORE_HEIGHT = 2.5F;
    public static final int DEFAULT_UNDERSCORE_OFFSET = 1;
    public static final boolean DEFAULT_DISABLED = false;
    public static final boolean DEFAULT_SHADOW = false;
    public static final boolean DEFAULT_CENTERED = true;

    private Color underscoreColor;
    private float underscoreHeight;
    private boolean disabled;
    private float underscoreYOffset;

    @Nullable
    private Runnable selectedListener;

    private boolean selected;

    public TabView() {
      super(new Properties<>().focusable(true));
      this.underscoreColor = DEFAULT_UNDERSCORE_COLOR;
      this.underscoreHeight = DEFAULT_UNDERSCORE_HEIGHT;
      this.underscoreYOffset = DEFAULT_UNDERSCORE_OFFSET;
      this.disabled = DEFAULT_DISABLED;
      this.setShadow(DEFAULT_SHADOW);
      this.setCentered(DEFAULT_CENTERED);
    }

    @Override
    public void renderContent(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
      super.renderContent(matrixStack, mouseX, mouseY, partialTicks);

      RenderSystem.setShader(GameRenderer::getPositionColorShader);
      if (this.selected) {
        RenderUtil.fill(matrixStack, this.getScaledX(),
            this.getScaledY() + this.getScaledHeight() - this.underscoreHeight
                + this.underscoreYOffset,
            this.getScaledX() + this.getScaledWidth(),
            this.getScaledY() + this.getScaledHeight() + this.underscoreYOffset,
            this.underscoreColor.getHex());
      } else if (this.isHovered()) {
        RenderUtil.fill(matrixStack, this.getScaledX(),
            this.getScaledY() + this.getScaledHeight() - this.underscoreHeight / 1.5F
                + this.underscoreYOffset,
            this.getScaledX() + this.getScaledWidth(),
            this.getScaledY() + this.getScaledHeight() + this.underscoreYOffset,
            this.underscoreColor.getHex());
      }
    }

    private void setSelected(boolean selected) {
      this.selected = selected;
      if (selected && this.selectedListener != null) {
        this.selectedListener.run();
      }
    }

    public TabView setSelectedListener(Runnable selectedListener) {
      this.selectedListener = selectedListener;
      return this;
    }

    public Color getUnderscoreColor() {
      return this.underscoreColor;
    }

    public TabView setUnderscoreColor(Color underscoreColor) {
      this.underscoreColor = underscoreColor;
      return this;
    }

    public float getUnderscoreHeight() {
      return this.underscoreHeight;
    }

    public TabView setUnderscoreHeight(float underscoreHeight) {
      this.underscoreHeight = underscoreHeight;
      return this;
    }

    public boolean isDisabled() {
      return this.disabled;
    }

    public TabView setDisabled(boolean disabled) {
      this.disabled = disabled;
      return this;
    }

    public float getUnderscoreYOffset() {
      return this.underscoreYOffset;
    }

    public TabView setUnderscoreYOffset(float underscoreYOffset) {
      this.underscoreYOffset = underscoreYOffset;
      return this;
    }
  }
}
