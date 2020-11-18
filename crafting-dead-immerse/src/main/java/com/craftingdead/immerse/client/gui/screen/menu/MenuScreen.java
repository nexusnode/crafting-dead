/**
 * Crafting Dead Copyright (C) 2020 Nexus Node
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */
package com.craftingdead.immerse.client.gui.screen.menu;

import java.util.function.Supplier;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.gui.component.Align;
import com.craftingdead.immerse.client.gui.component.BlurComponent;
import com.craftingdead.immerse.client.gui.component.Colour;
import com.craftingdead.immerse.client.gui.component.Component;
import com.craftingdead.immerse.client.gui.component.ComponentScreen;
import com.craftingdead.immerse.client.gui.component.ContainerComponent;
import com.craftingdead.immerse.client.gui.component.FlexDirection;
import com.craftingdead.immerse.client.gui.component.ImageComponent;
import com.craftingdead.immerse.client.gui.component.PanoramaComponent;
import com.craftingdead.immerse.client.gui.component.PositionType;
import com.craftingdead.immerse.client.gui.component.RectangleComponent;
import com.craftingdead.immerse.client.gui.component.Tooltip;
import com.craftingdead.immerse.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.OptionsScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TranslationTextComponent;

public class MenuScreen extends ComponentScreen {

  private final ContainerComponent contentContainer = new ContainerComponent().setFlex(1);

  public MenuScreen(Page page) {
    super(new TranslationTextComponent("narrator.screen.title"));

    final Minecraft mc = Minecraft.getInstance();
    this.font = mc.fontRenderer;
    this.getRoot().addChild(new PanoramaComponent(new ResourceLocation(CraftingDeadImmerse.ID,
        "textures/gui/title/background/panorama"))
            .setPositionType(PositionType.ABSOLUTE)
            .setWidthPercent(100.0F)
            .setHeightPercent(100.0F));

    ContainerComponent sideBar = new ContainerComponent()
        .setHeightPercent(100.0F)
        .setWidth(25.0F)
        .setAlignItems(Align.CENTER);

    sideBar.addChild(new BlurComponent()
        .setPositionType(PositionType.ABSOLUTE)
        .setHeightPercent(100.0F)
        .setWidthPercent(100.0F));
    sideBar.addChild(new RectangleComponent()
        .setPositionType(PositionType.ABSOLUTE)
        .setHeightPercent(100.0F)
        .setWidthPercent(100.0F)
        .setBackgroundColour(new Colour(0x40999999)));

    sideBar.addChild(new RectangleComponent()
        .setUnscaleWidth()
        .setPositionType(PositionType.ABSOLUTE)
        .setRight(0.0F)
        .setWidth(1.0F)
        .setHeightPercent(100.0F)
        .setBackgroundColour(new Colour(0X80B5B5B5)));

    sideBar.addChild(new ImageComponent()
        .setImage(new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/home.png"))
        .setMargin(5)
        .setWidth(10)
        .setHeight(10)
        .addHoverAnimation(ImageComponent.COLOUR,
            RenderUtil.getColour4f(RenderUtil.getColour4i(0xFF181818)), 150.0F)
        .addHoverAnimation(Component.X_SCALE, new float[] {1.15F}, 150.0F)
        .addHoverAnimation(Component.Y_SCALE, new float[] {1.15F}, 150.0F)
        .addClickSound(SoundEvents.UI_BUTTON_CLICK)
        .setTooltip(new Tooltip(new TranslationTextComponent("menu.home"))));

    sideBar.addChild(new RectangleComponent()
        .setUnscaleHeight()
        .setHeight(1)
        .setWidthPercent(100.0F)
        .setBackgroundColour(new Colour(0X80B5B5B5)));

    sideBar.addChild(new ImageComponent()
        .setImage(new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/play.png"))
        .setMargin(5)
        .setWidth(10)
        .setHeight(10)
        .addHoverAnimation(ImageComponent.COLOUR,
            RenderUtil.getColour4f(RenderUtil.getColour4i(0xFF181818)), 150.0F)
        .addHoverAnimation(Component.X_SCALE, new float[] {1.15F}, 150.0F)
        .addHoverAnimation(Component.Y_SCALE, new float[] {1.15F}, 150.0F)
        .addClickSound(SoundEvents.UI_BUTTON_CLICK)
        .setTooltip(new Tooltip(new TranslationTextComponent("menu.play"))));

    sideBar.addChild(new RectangleComponent()
        .setUnscaleHeight()
        .setWidthPercent(100.0F)
        .setHeight(1)
        .setBackgroundColour(new Colour(0X80B5B5B5)));

    sideBar.addChild(new ImageComponent()
        .setImage(new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/settings.png"))
        .setMargin(5)
        .setWidth(10)
        .setHeight(10)
        .addHoverAnimation(ImageComponent.COLOUR,
            RenderUtil.getColour4f(RenderUtil.getColour4i(0xFF181818)), 150.0F)
        .addHoverAnimation(Component.X_SCALE, new float[] {1.15F}, 150.0F)
        .addHoverAnimation(Component.Y_SCALE, new float[] {1.15F}, 150.0F)
        .addClickSound(SoundEvents.UI_BUTTON_CLICK)
        .addActionListener(c -> this.minecraft
            .displayGuiScreen(new OptionsScreen(this, this.minecraft.gameSettings)))
        .setTooltip(new Tooltip(new TranslationTextComponent("menu.options"))));

    sideBar.addChild(new ImageComponent()
        .setImage(new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/power.png"))
        .setMargin(5)
        .setTopMarginAuto()
        .setWidth(10)
        .setHeight(10)
        .addHoverAnimation(ImageComponent.COLOUR,
            RenderUtil.getColour4f(RenderUtil.getColour4i(0xFF181818)), 150.0F)
        .addHoverAnimation(Component.X_SCALE, new float[] {1.15F}, 150.0F)
        .addHoverAnimation(Component.Y_SCALE, new float[] {1.15F}, 150.0F)
        .addClickSound(SoundEvents.UI_BUTTON_CLICK)
        .addActionListener(c -> this.minecraft.shutdown())
        .setTooltip(new Tooltip(new TranslationTextComponent("menu.quit"))));

    this.getRoot().addChild(sideBar);

    this.getRoot().addChild(this.contentContainer);

    this.getRoot().setFlexDirection(FlexDirection.ROW);

    this.displayPage(page);
  }

  public void displayPage(Page page) {
    this.contentContainer.clearChildren();
    this.contentContainer.addChild(page.create().setFlex(1));
  }

  public static enum Page {
    HOME(HomeComponent::new);

    private final Supplier<ContainerComponent> factory;

    private Page(Supplier<ContainerComponent> factory) {
      this.factory = factory;
    }

    public ContainerComponent create() {
      return this.factory.get();
    }
  }
}
