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
import java.util.List;
import javax.annotation.Nullable;
import com.craftingdead.immerse.client.gui.view.event.ActionEvent;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.FlexDirection;
import com.craftingdead.immerse.client.gui.view.layout.yoga.FlexWrap;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayoutParent;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;

public class TabsView<L extends Layout>
    extends ParentView<TabsView<L>, L, YogaLayout> {

  private final List<TabView> tabList = new ArrayList<>();
  private TabView selectedTab = null;
  private float tabWidth = 60f;
  private float tabHeight = 20f;
  private boolean init = false;

  public TabsView(L layout) {
    super(layout, new YogaLayoutParent()
        .setFlexDirection(FlexDirection.ROW)
        .setFlexWrap(FlexWrap.WRAP));
  }

  /**
   * If called after adding this component as a child,
   */
  public TabsView<L> addTab(TabView tab) {
    this.tabList.add(tab);
    return this;
  }

  @Override
  public void layout() {
    this.init();
    super.layout();
  }

  public void init() {
    if (this.init) {
      return;
    }
    this.init = true;
    TabView newSelectedTab = null;
    for (TabView tab : this.tabList) {
      tab.getLayout().setWidth(this.tabWidth);
      float y = (this.tabHeight - this.minecraft.font.lineHeight) / 2F;
      tab.getLayout().setTopPadding(y);
      tab.getLayout().setBottomPadding(y);
      this.addChild(tab);
      if (tab.isSelected() && this.selectedTab == null) {
        newSelectedTab = tab;
      } else if (tab.isSelected()) {
        tab.setSelected(false);
      }
      tab.layout();
      tab.addListener(ActionEvent.class, (c, e) -> this.changeTab((TabView) c));
    }

    if (newSelectedTab != null) {
      this.changeTab(newSelectedTab);
    } else if (this.tabList.size() > 0) {
      this.changeTab(this.tabList.get(0));
    }
  }

  private void changeTab(TabView newTab) {
    TabView previousTab = this.selectedTab;
    this.selectedTab = newTab;
    if (previousTab != newTab) {
      if (previousTab != null) {
        previousTab.setSelected(false);
      }
      newTab.setSelected(true);
    }
  }

  public static class TabView extends TextView<YogaLayout> {

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

    public TabView() {
      super(new YogaLayout());
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
      if (this.isSelected()) {
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

    @Override
    public TabView setSelected(boolean selected) {
      if (selected && this.selectedListener != null) {
        this.selectedListener.run();
      }
      super.setSelected(selected);
      return this;
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
