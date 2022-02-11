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

import javax.annotation.Nullable;
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
      super(new Properties<>());
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
