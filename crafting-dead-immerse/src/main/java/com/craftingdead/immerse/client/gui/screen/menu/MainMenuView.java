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

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.Animator.RepeatBehavior;
import org.jdesktop.core.animation.timing.KeyFrames;
import org.jdesktop.core.animation.timing.interpolators.SplineInterpolator;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.inventory.ModEquipmentSlot;
import com.craftingdead.core.world.item.HatItem;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.fake.FakePlayerEntity;
import com.craftingdead.immerse.client.gui.screen.menu.play.PlayView;
import com.craftingdead.immerse.client.gui.view.Animation;
import com.craftingdead.immerse.client.gui.view.Color;
import com.craftingdead.immerse.client.gui.view.EntityView;
import com.craftingdead.immerse.client.gui.view.FogView;
import com.craftingdead.immerse.client.gui.view.ImageView;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.States;
import com.craftingdead.immerse.client.gui.view.Tooltip;
import com.craftingdead.immerse.client.gui.view.Transition;
import com.craftingdead.immerse.client.gui.view.View;
import com.craftingdead.immerse.client.gui.view.ViewScreen;
import com.craftingdead.immerse.client.gui.view.event.ActionEvent;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.Align;
import com.craftingdead.immerse.client.gui.view.layout.yoga.FlexDirection;
import com.craftingdead.immerse.client.gui.view.layout.yoga.PositionType;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayoutParent;
import com.craftingdead.immerse.client.util.FitType;
import com.craftingdead.immerse.sounds.ImmerseSoundEvents;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.registries.ForgeRegistries;

public class MainMenuView extends ParentView<MainMenuView, Layout, YogaLayout> {

  private static final Component TITLE = new TranslatableComponent("menu.home.title");

  private final ParentView<?, YogaLayout, YogaLayout> contentContainer =
      new ParentView<>(new YogaLayout().setFlex(1), new YogaLayoutParent());

  public MainMenuView(Layout layout) {
    super(layout,
        new YogaLayoutParent().setFlexDirection(FlexDirection.ROW_REVERSE));

    var homeView = new HomeView()
        .setZOffset(-2)
        .configure(view -> view.getLayout()
            .setWidthPercent(100)
            .setHeightPercent(100)
            .setPositionType(PositionType.ABSOLUTE));

    var playView = new PlayView();

    this.addChild(createBackgroundView());

    this.addChild(new FogView<>(new YogaLayout()
        .setPositionType(PositionType.ABSOLUTE)
        .setHeightPercent(100)
        .setWidthPercent(100)));

    var sideBar = new ParentView<>(new YogaLayout()
        .setRightBorderWidth(1)
        .setHeightPercent(100.0F)
        .setWidth(30.0F), new YogaLayoutParent().setAlignItems(Align.CENTER))
            .configure(
                view -> view.getBackgroundColorProperty().setBaseValue(new Color(0x70777777)))
            .setBackgroundBlur(50.0F)
            .configure(
                view -> view.getRightBorderColorProperty().setBaseValue(new Color(0X80B5B5B5)));

    sideBar.addChild(new ImageView<>(new YogaLayout()
        .setMargin(5)
        .setWidth(20)
        .setAspectRatio(1))
            .setImage(new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/icon.png"))
            .configure(view -> view.getColorProperty()
                .setTransition(Transition.linear(150L))
                .defineState(new Color(0xFF666666), States.HOVERED))
            .addActionSound(ImmerseSoundEvents.BUTTON_CLICK.get())
            .addHoverSound(ImmerseSoundEvents.MAIN_MENU_HOVER.get())
            .addListener(ActionEvent.class, (c, e) -> this.setContentView(homeView))
            .setFocusable(true)
            .setTooltip(new Tooltip(new TranslatableComponent("menu.home_button"))));

    sideBar.addChild(new View<>(new YogaLayout()
        .setHeight(1)
        .setWidthPercent(100.0F))
            .setUnscaleHeight(true)
            .configure(
                view -> view.getBackgroundColorProperty().setBaseValue(new Color(0X80B5B5B5))));

    sideBar.addChild(new ImageView<>(new YogaLayout()
        .setMargin(5)
        .setWidth(14)
        .setAspectRatio(1))
            .setImage(new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/play.png"))
            .setBilinearFiltering(true)
            .configure(view -> {
              view.getColorProperty()
                  .setTransition(Transition.linear(150L))
                  .defineState(new Color(0xFF181818), States.HOVERED);
              view.getScaleProperty()
                  .setTransition(Transition.linear(150L))
                  .defineState(1.15F, States.HOVERED);
            })
            .addHoverSound(ImmerseSoundEvents.MAIN_MENU_HOVER.get())
            .addListener(ActionEvent.class, (c, e) -> this.setContentView(playView))
            .setFocusable(true)
            .setTooltip(new Tooltip(new TranslatableComponent("menu.play_button"))));

    sideBar.addChild(new ImageView<>(new YogaLayout()
        .setMargin(5)
        .setWidth(12)
        .setAspectRatio(1))
            .setImage(new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/settings.png"))
            .setBilinearFiltering(true)
            .configure(view -> {
              view.getColorProperty()
                  .setTransition(Transition.linear(150L))
                  .defineState(new Color(0xFF181818), States.HOVERED);

              view.getScaleProperty()
                  .setTransition(Transition.linear(150L))
                  .defineState(1.15F, States.HOVERED);
            })
            .addActionSound(ImmerseSoundEvents.BUTTON_CLICK.get())
            .addHoverSound(ImmerseSoundEvents.MAIN_MENU_HOVER.get())
            .addListener(ActionEvent.class,
                (c, e) -> this.getScreen().keepOpenAndSetScreen(
                    new OptionsScreen(this.getScreen(), this.minecraft.options)))
            .setFocusable(true)
            .setTooltip(new Tooltip(new TranslatableComponent("menu.options"))));

    sideBar.addChild(new ImageView<>(new YogaLayout()
        .setMargin(5)
        .setTopMarginAuto()
        .setWidth(12)
        .setAspectRatio(1))
            .setImage(new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/power.png"))
            .setBilinearFiltering(true)
            .configure(view -> {
              view.getColorProperty()
                  .setTransition(Transition.linear(150L))
                  .defineState(new Color(0xFF181818), States.HOVERED);
              view.getScaleProperty()
                  .setTransition(Transition.linear(150L))
                  .defineState(1.15F, States.HOVERED);
            })
            .addActionSound(ImmerseSoundEvents.BUTTON_CLICK.get())
            .addHoverSound(ImmerseSoundEvents.MAIN_MENU_HOVER.get())
            .addListener(ActionEvent.class, (c, e) -> this.minecraft.stop())
            .setFocusable(true)
            .setTooltip(new Tooltip(new TranslatableComponent("menu.quit"))));

    final FakePlayerEntity fakePlayerEntity =
        new FakePlayerEntity(this.minecraft.getUser().getGameProfile());

    List<Item> hatItems = ForgeRegistries.ITEMS.getValues()
        .stream()
        .filter(item -> item instanceof HatItem)
        .collect(Collectors.toList());
    var randomHatItem = hatItems.get(ThreadLocalRandom.current().nextInt(hatItems.size()));

    var livingExtension = LivingExtension.getOrThrow(fakePlayerEntity);
    livingExtension.getItemHandler().insertItem(ModEquipmentSlot.HAT.getIndex(),
        randomHatItem.getDefaultInstance(), false);

    this.addChild(new ParentView<>(new YogaLayout()
        .setWidthPercent(10)
        .setHeightPercent(50)
        .setBottomPercent(15)
        .setLeftPercent(75)
        .setPositionType(PositionType.ABSOLUTE), new YogaLayoutParent().setAlignItems(Align.CENTER))
            .addChild(
                new EntityView<>(new YogaLayout().setFlex(1).setAspectRatio(1), fakePlayerEntity)));

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

  public static View<?, YogaLayout> createBackgroundView() {
    var view = new ImageView<>(new YogaLayout()
        .setPositionType(PositionType.ABSOLUTE)
        .setHeightPercent(100)
        .setWidthPercent(100))
            .setImage(
                new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/background.png"))
            .setFitType(FitType.COVER)
            .setBilinearFiltering(true)
            .configure(v -> v.getScaleProperty().setBaseValue(1.25F));

    new Animator.Builder()
        .addTarget(Animation.forProperty(view.getXTranslationProperty())
            .keyFrames(new KeyFrames.Builder<Float>()
                .addFrames(1.0F, -3.0F, 5.0F, 15.0F)
                .build())
            .build())
        .addTarget(Animation.forProperty(view.getYTranslationProperty())
            .keyFrames(new KeyFrames.Builder<Float>()
                .addFrames(10.0F, 5.0F, 1.0F, 3.0F, 5.0F, 1.0F, -10.0F, -7.0F, -5.0F)
                .build())
            .build())
        .addTarget(Animation.forProperty(view.getXScaleProperty())
            .keyFrames(new KeyFrames.Builder<Float>()
                .addFrames(1.3F, 1.2F, 1.25F, 1.15F)
                .build())
            .build())
        .addTarget(Animation.forProperty(view.getYScaleProperty())
            .keyFrames(new KeyFrames.Builder<Float>()
                .addFrames(1.3F, 1.2F, 1.25F, 1.15F)
                .build())
            .build())
        .setInterpolator(new SplineInterpolator(0.25, 0.1, 0.25, 1))
        .setDuration(20L, TimeUnit.SECONDS)
        .setRepeatCount(Animator.INFINITE)
        .setRepeatBehavior(RepeatBehavior.REVERSE)
        .build()
        .start();
    return view;
  }
}
