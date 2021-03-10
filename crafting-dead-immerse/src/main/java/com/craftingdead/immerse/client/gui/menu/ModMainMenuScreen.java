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

/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
package com.craftingdead.immerse.client.gui.menu;

import java.util.function.Supplier;
import javax.annotation.Nonnull;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.gui.component.Colour;
import com.craftingdead.immerse.client.gui.component.Component;
import com.craftingdead.immerse.client.gui.component.ComponentScreen;
import com.craftingdead.immerse.client.gui.component.ContainerComponent;
import com.craftingdead.immerse.client.gui.component.FogComponent;
import com.craftingdead.immerse.client.gui.component.ImageComponent;
import com.craftingdead.immerse.client.gui.component.RectangleComponent;
import com.craftingdead.immerse.client.gui.component.Tooltip;
import com.craftingdead.immerse.client.gui.component.event.ActionEvent;
import com.craftingdead.immerse.client.gui.component.type.Align;
import com.craftingdead.immerse.client.gui.component.type.FitType;
import com.craftingdead.immerse.client.gui.component.type.FlexDirection;
import com.craftingdead.immerse.client.gui.component.type.PositionType;
import com.craftingdead.immerse.client.gui.menu.play.PlayComponent;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.craftingdead.immerse.util.ModSoundEvents;
import io.noties.tumbleweed.Timeline;
import io.noties.tumbleweed.Tween;
import io.noties.tumbleweed.paths.CatmullRom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.OptionsScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class ModMainMenuScreen extends ComponentScreen {

  private final ContainerComponent contentContainer = new ContainerComponent().setFlex(1);

  private Page currentPage;

  public ModMainMenuScreen(Page page) {
    super(new TranslationTextComponent("menu.home.title"));

    // Fix null font field in constructor
    final Minecraft mc = Minecraft.getInstance();
    this.font = mc.font;

    ImageComponent backgroundComponent = new ImageComponent()
        .setImage(new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/background.png"))
        .setPositionType(PositionType.ABSOLUTE)
        .setFitType(FitType.COVER)
        .setHeightPercent(100)
        .setWidthPercent(100)
        .setBilinearFiltering(true)
        .setScale(1.25F);
    this.getRoot().addChild(backgroundComponent);

    // Background animation
    Timeline.createParallel()
        .push(Tween.to(backgroundComponent, Component.X_TRANSLATION, 20000)
            .target(15)
            .waypoint(5)
            .waypoint(1)
            .waypoint(3)
            .path(CatmullRom.instance()))
        .push(Tween.to(backgroundComponent, Component.Y_TRANSLATION, 20000)
            .target(-10)
            .waypoint(5)
            .waypoint(1)
            .waypoint(3))
        .push(Tween.to(backgroundComponent, Component.X_SCALE, 20000)
            .target(1.15F)
            .waypoint(1.2F))
        .push(Tween.to(backgroundComponent, Component.Y_SCALE, 20000)
            .target(1.15F)
            .waypoint(1.2F))
        .repeatYoyo(-1, 0)
        .start(backgroundComponent.getTweenManager());

    this.getRoot().addChild(new FogComponent()
        .setPositionType(PositionType.ABSOLUTE)
        .setHeightPercent(100)
        .setWidthPercent(100));

    ContainerComponent sideBar = new ContainerComponent()
        .setBackgroundColour(new Colour(0x70777777))
        .setBackgroundBlur(50.0F)
        .setHeightPercent(100.0F)
        .setWidth(30.0F)
        .setAlignItems(Align.CENTER);

    sideBar.addChild(new RectangleComponent()
        .setUnscaleWidth()
        .setPositionType(PositionType.ABSOLUTE)
        .setRight(0.0F)
        .setWidth(1.0F)
        .setHeightPercent(100.0F)
        .setBackgroundColour(new Colour(0X80B5B5B5)));

    sideBar.addChild(new ImageComponent()
        .setImage(new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/icon.png"))
        .setMargin(5)
        .setWidth(20)
        .setHeight(20)
        .addHoverAnimation(ImageComponent.COLOUR,
            RenderUtil.getColour4f(RenderUtil.getColour4i(0xFF666666)), 150.0F)
        .addActionSound(ModSoundEvents.BUTTON_CLICK.get())
        .addListener(ActionEvent.class, (c, e) -> this.displayPage(Page.HOME))
        .setFocusable(true)
        .setTooltip(new Tooltip(new TranslationTextComponent("menu.home_button"))));

    sideBar.addChild(new RectangleComponent()
        .setUnscaleHeight()
        .setHeight(1)
        .setWidthPercent(100.0F)
        .setBackgroundColour(new Colour(0X80B5B5B5)));

    sideBar.addChild(new ImageComponent()
        .setImage(new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/play.png"))
        .setMargin(5)
        .setWidth(12)
        .setHeight(12)
        .setBilinearFiltering(true)
        .addHoverAnimation(ImageComponent.COLOUR,
            RenderUtil.getColour4f(RenderUtil.getColour4i(0xFF181818)), 150.0F)
        .addHoverAnimation(Component.X_SCALE, new float[] {1.15F}, 150.0F)
        .addHoverAnimation(Component.Y_SCALE, new float[] {1.15F}, 150.0F)
        .addActionSound(ModSoundEvents.BUTTON_CLICK.get())
        .addListener(ActionEvent.class, (c, e) -> this.displayPage(Page.PLAY))
        .setFocusable(true)
        .setTooltip(new Tooltip(new TranslationTextComponent("menu.play_button"))));

    sideBar.addChild(new ImageComponent()
        .setImage(new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/settings.png"))
        .setMargin(5)
        .setWidth(12)
        .setHeight(12)
        .setBilinearFiltering(true)
        .addHoverAnimation(ImageComponent.COLOUR,
            RenderUtil.getColour4f(RenderUtil.getColour4i(0xFF181818)), 150.0F)
        .addHoverAnimation(Component.X_SCALE, new float[] {1.15F}, 150.0F)
        .addHoverAnimation(Component.Y_SCALE, new float[] {1.15F}, 150.0F)
        .addActionSound(ModSoundEvents.BUTTON_CLICK.get())
        .addListener(ActionEvent.class, (c, e) -> this.minecraft
            .setScreen(new OptionsScreen(this, this.minecraft.options)))
        .setFocusable(true)
        .setTooltip(new Tooltip(new TranslationTextComponent("menu.options"))));

    sideBar.addChild(new ImageComponent()
        .setImage(new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/power.png"))
        .setMargin(5)
        .setTopMarginAuto()
        .setWidth(12)
        .setHeight(12)
        .setBilinearFiltering(true)
        .addHoverAnimation(ImageComponent.COLOUR,
            RenderUtil.getColour4f(RenderUtil.getColour4i(0xFF181818)), 150.0F)
        .addHoverAnimation(Component.X_SCALE, new float[] {1.15F}, 150.0F)
        .addHoverAnimation(Component.Y_SCALE, new float[] {1.15F}, 150.0F)
        .addActionSound(ModSoundEvents.BUTTON_CLICK.get())
        .addListener(ActionEvent.class, (c, e) -> this.minecraft.stop())
        .setFocusable(true)
        .setTooltip(new Tooltip(new TranslationTextComponent("menu.quit"))));

    this.getRoot().addChild(this.contentContainer);

    this.getRoot().addChild(sideBar);

    this.getRoot().setFlexDirection(FlexDirection.ROW_REVERSE);

    this.displayPage(page);
  }

  public void displayPage(@Nonnull Page page) {
    if (this.currentPage == page) {
      return;
    }
    this.currentPage = page;
    this.contentContainer
        .clearChildrenClosing()
        .addChild(page.create().setFlex(1))
        .layout();
  }

  public static enum Page {
    HOME(HomeComponent::new), PLAY(PlayComponent::new);

    private final Supplier<ContainerComponent> factory;

    private Page(Supplier<ContainerComponent> factory) {
      this.factory = factory;
    }

    public ContainerComponent create() {
      return this.factory.get();
    }
  }
}
