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
package com.craftingdead.immerse.client.gui.screen.menu;

import java.util.List;
import java.util.stream.Collectors;
import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.inventory.ModEquipmentSlotType;
import com.craftingdead.core.world.item.HatItem;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.fake.FakePlayerEntity;
import com.craftingdead.immerse.client.gui.screen.menu.play.PlayView;
import com.craftingdead.immerse.client.gui.view.Colour;
import com.craftingdead.immerse.client.gui.view.EntityView;
import com.craftingdead.immerse.client.gui.view.FogView;
import com.craftingdead.immerse.client.gui.view.ImageView;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.Tooltip;
import com.craftingdead.immerse.client.gui.view.View;
import com.craftingdead.immerse.client.gui.view.ViewScreen;
import com.craftingdead.immerse.client.gui.view.event.ActionEvent;
import com.craftingdead.immerse.client.gui.view.layout.yoga.Align;
import com.craftingdead.immerse.client.gui.view.layout.yoga.FlexDirection;
import com.craftingdead.immerse.client.gui.view.layout.yoga.PositionType;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayoutParent;
import com.craftingdead.immerse.client.util.FitType;
import com.craftingdead.immerse.util.ModSoundEvents;
import io.netty.util.internal.ThreadLocalRandom;
import io.noties.tumbleweed.Timeline;
import io.noties.tumbleweed.Tween;
import io.noties.tumbleweed.paths.CatmullRom;
import net.minecraft.client.gui.screen.OptionsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

public class MainMenuView extends ParentView<MainMenuView, ViewScreen, YogaLayout> {

  private static final ITextComponent TITLE = new TranslationTextComponent("menu.home.title");

  private final ParentView<?, YogaLayout, YogaLayout> contentContainer =
      new ParentView<>(new YogaLayout().setFlex(1), new YogaLayoutParent());

  public MainMenuView(ViewScreen screen) {
    super(screen,
        new YogaLayoutParent().setFlexDirection(FlexDirection.ROW_REVERSE));

    HomeView homeView = new HomeView()
        .configure(view -> view.getLayout()
            .setWidthPercent(100)
            .setHeightPercent(100)
            .setPositionType(PositionType.ABSOLUTE));

    PlayView playView = new PlayView()
        .configure(view -> view.getLayout()
            .setWidthPercent(100)
            .setHeightPercent(100)
            .setPositionType(PositionType.ABSOLUTE));

    ImageView<YogaLayout> backgroundComponent =
        new ImageView<>(new YogaLayout()
            .setPositionType(PositionType.ABSOLUTE)
            .setHeightPercent(100)
            .setWidthPercent(100))
                .setImage(
                    new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/background.png"))
                .setFitType(FitType.COVER)
                .setBilinearFiltering(true)
                .setScale(1.25F);
    this.addChild(backgroundComponent);

    // Background animation
    Timeline.createParallel()
        .push(Tween.to(backgroundComponent, View.X_TRANSLATION, 20000)
            .target(15)
            .waypoint(5)
            .waypoint(1)
            .waypoint(3)
            .path(CatmullRom.instance()))
        .push(Tween.to(backgroundComponent, View.Y_TRANSLATION, 20000)
            .target(-10)
            .waypoint(5)
            .waypoint(1)
            .waypoint(3))
        .push(Tween.to(backgroundComponent, View.X_SCALE, 20000)
            .target(1.15F)
            .waypoint(1.2F))
        .push(Tween.to(backgroundComponent, View.Y_SCALE, 20000)
            .target(1.15F)
            .waypoint(1.2F))
        .repeatYoyo(-1, 0)
        .start(backgroundComponent.getTweenManager());

    this.addChild(new FogView<>(new YogaLayout()
        .setPositionType(PositionType.ABSOLUTE)
        .setHeightPercent(100)
        .setWidthPercent(100)));

    ParentView<?, YogaLayout, YogaLayout> sideBar = new ParentView<>(new YogaLayout()
        .setHeightPercent(100.0F)
        .setWidth(30.0F), new YogaLayoutParent().setAlignItems(Align.CENTER))
            .setBackgroundColour(new Colour(0x70777777))
            .setBackgroundBlur(50.0F);

    sideBar.addChild(new View<>(new YogaLayout()
        .setPositionType(PositionType.ABSOLUTE)
        .setRight(0.0F)
        .setWidth(1.0F)
        .setHeightPercent(100.0F))
            .setUnscaleWidth()
            .setBackgroundColour(new Colour(0X80B5B5B5)));

    sideBar.addChild(new ImageView<>(new YogaLayout()
        .setMargin(5)
        .setWidth(20)
        .setAspectRatio(1))
            .setImage(new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/icon.png"))
            .addColourHoverAnimation(new Colour(0xFF666666), 150.0F)
            .addActionSound(ModSoundEvents.BUTTON_CLICK.get())
            .addListener(ActionEvent.class, (c, e) -> this.setContentView(homeView))
            .setFocusable(true)
            .setTooltip(new Tooltip(new TranslationTextComponent("menu.home_button"))));

    sideBar.addChild(new View<>(new YogaLayout()
        .setHeight(1)
        .setWidthPercent(100.0F))
            .setUnscaleHeight()
            .setBackgroundColour(new Colour(0X80B5B5B5)));

    sideBar.addChild(new ImageView<>(new YogaLayout()
        .setMargin(5)
        .setWidth(14)
        .setAspectRatio(1))
            .setImage(new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/play.png"))
            .setBilinearFiltering(true)
            .addColourHoverAnimation(new Colour(0xFF181818), 150.0F)
            .addHoverAnimation(View.X_SCALE, new float[] {1.15F}, 150.0F)
            .addHoverAnimation(View.Y_SCALE, new float[] {1.15F}, 150.0F)
            .addActionSound(ModSoundEvents.BUTTON_CLICK.get())
            .addListener(ActionEvent.class, (c, e) -> this.setContentView(playView))
            .setFocusable(true)
            .setTooltip(new Tooltip(new TranslationTextComponent("menu.play_button"))));

    sideBar.addChild(new ImageView<>(new YogaLayout()
        .setMargin(5)
        .setWidth(12)
        .setAspectRatio(1))
            .setImage(new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/settings.png"))
            .setBilinearFiltering(true)
            .addColourHoverAnimation(new Colour(0xFF181818), 150.0F)
            .addHoverAnimation(View.X_SCALE, new float[] {1.15F}, 150.0F)
            .addHoverAnimation(View.Y_SCALE, new float[] {1.15F}, 150.0F)
            .addActionSound(ModSoundEvents.BUTTON_CLICK.get())
            .addListener(ActionEvent.class, (c, e) -> this.getScreen().keepOpenAndSetScreen(
                new OptionsScreen(this.getScreen(), this.minecraft.options)))
            .setFocusable(true)
            .setTooltip(new Tooltip(new TranslationTextComponent("menu.options"))));

    sideBar.addChild(new ImageView<>(new YogaLayout()
        .setMargin(5)
        .setTopMarginAuto()
        .setWidth(12)
        .setAspectRatio(1))
            .setImage(new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/power.png"))
            .setBilinearFiltering(true)
            .addColourHoverAnimation(new Colour(0xFF181818), 150.0F)
            .addHoverAnimation(View.X_SCALE, new float[] {1.15F}, 150.0F)
            .addHoverAnimation(View.Y_SCALE, new float[] {1.15F}, 150.0F)
            .addActionSound(ModSoundEvents.BUTTON_CLICK.get())
            .addListener(ActionEvent.class, (c, e) -> this.minecraft.stop())
            .setFocusable(true)
            .setTooltip(new Tooltip(new TranslationTextComponent("menu.quit"))));

    final FakePlayerEntity fakePlayerEntity =
        new FakePlayerEntity(this.minecraft.getUser().getGameProfile());

    List<Item> hatItems = ForgeRegistries.ITEMS.getValues()
        .stream()
        .filter(item -> item instanceof HatItem)
        .collect(Collectors.toList());
    Item randomHatItem = hatItems.get(ThreadLocalRandom.current().nextInt(hatItems.size()));

    LivingExtension<?, ?> livingExtension = Capabilities.getOrThrow(Capabilities.LIVING,
        fakePlayerEntity, LivingExtension.class);
    livingExtension.getItemHandler().insertItem(ModEquipmentSlotType.HAT.getIndex(),
        randomHatItem.getDefaultInstance(), false);

    this.addChild(new ParentView<>(new YogaLayout()
        .setWidthPercent(10)
        .setHeightPercent(50)
        .setBottomPercent(15)
        .setLeftPercent(75)
        .setPositionType(PositionType.ABSOLUTE), new YogaLayoutParent().setAlignItems(Align.CENTER))
            .setScale(1.0F)
            .addChild(new EntityView<>(new YogaLayout().setAspectRatio(1), fakePlayerEntity)));

    this.addChild(this.contentContainer);

    this.addChild(sideBar);

    this.setContentView(homeView);
  }

  private void setContentView(View<?, YogaLayout> view) {
    // Already added
    if (view.getParent() == this.contentContainer) {
      return;
    }
    this.contentContainer
        .queueAllForRemoval()
        .addChild(view)
        .layout();
  }

  public static Screen createScreen() {
    return new ViewScreen(TITLE, MainMenuView::new);
  }
}
