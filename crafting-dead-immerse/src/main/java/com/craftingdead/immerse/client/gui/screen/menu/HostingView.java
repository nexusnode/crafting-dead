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

package com.craftingdead.immerse.client.gui.screen.menu;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.gui.view.Color;
import com.craftingdead.immerse.client.gui.view.ImageView;
import com.craftingdead.immerse.client.gui.view.Overflow;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.States;
import com.craftingdead.immerse.client.gui.view.TextView;
import com.craftingdead.immerse.client.gui.view.Transition;
import com.craftingdead.immerse.client.gui.view.View;
import com.craftingdead.immerse.client.gui.view.event.ActionEvent;
import com.craftingdead.immerse.client.gui.view.layout.yoga.Align;
import com.craftingdead.immerse.client.gui.view.layout.yoga.FlexDirection;
import com.craftingdead.immerse.client.gui.view.layout.yoga.FlexWrap;
import com.craftingdead.immerse.client.gui.view.layout.yoga.Justify;
import com.craftingdead.immerse.client.gui.view.layout.yoga.PositionType;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayoutParent;
import com.craftingdead.immerse.client.util.FitType;
import com.craftingdead.immerse.sounds.ImmerseSoundEvents;
import com.google.common.collect.Streams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class HostingView extends ParentView<HostingView, YogaLayout, YogaLayout> {

  private static final Logger logger = LogManager.getLogger();

  private static final Gson gson = new GsonBuilder()
      .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
      .create();

  private static final Component ORDER_NOW_TEXT =
      new TranslatableComponent("view.hosting.order_now");

  private static final Path PLANS_FILE = Paths.get("plans.json");

  public HostingView() {
    super(new YogaLayout().setOverflow(Overflow.SCROLL), new YogaLayoutParent());

    this.setBackgroundBlur();

    this.addChild(
        new ParentView<>(new YogaLayout()
            .setFlexBasis(50)
            .setTopMargin(10)
            .setBottomMargin(10),
            new YogaLayoutParent().setAlignItems(Align.CENTER))
                .addChild(
                    new TextView<>(new YogaLayout())
                        .setText(new TranslatableComponent("view.hosting.partners")
                            .withStyle(ChatFormatting.ITALIC))
                        .setCentered(true))
                .addChild(new ImageView<>(new YogaLayout()
                    .setWidth(200))
                        .setFitType(FitType.CONTAIN)
                        .setBilinearFiltering(true)
                        .setImage(
                            new ResourceLocation(CraftingDeadImmerse.ID,
                                "textures/gui/view/hosting/banner.png"))));

    AsynchronousFileChannel fileChannel;
    try {
      fileChannel = AsynchronousFileChannel.open(
          PLANS_FILE, StandardOpenOption.READ);
    } catch (IOException e) {
      logger.warn("Failed to open plans file", e);
      return;
    }

    var buffer = ByteBuffer.allocate(1048576);

    fileChannel.read(buffer, 0, null, new CompletionHandler<Integer, Void>() {

      @Override
      public void completed(Integer result, Void attachment) {
        buffer.flip();
        var bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        var jsonString = new String(bytes, StandardCharsets.UTF_8).trim();
        var plansJson = gson.fromJson(jsonString, JsonArray.class);

        var plansView = new ParentView<>(
            new YogaLayout()
                .setFlexGrow(1)
                .setFlexShrink(1)
                .setTopPadding(10)
                .setBottomPadding(10)
                .setOverflow(Overflow.SCROLL),
            new YogaLayoutParent()
                .setJustifyContent(Justify.SPACE_EVENLY)
                .setAlignItems(Align.CENTER)
                .setFlexDirection(FlexDirection.ROW)
                .setFlexWrap(FlexWrap.WRAP));
        plansView.getBackgroundColorProperty().setBaseValue(new Color(0X2F2C2C2C));

        for (var jsonElement : plansJson) {
          var planJson = jsonElement.getAsJsonObject();
          var recommendedJson = planJson.get("recommended");
          plansView.addChild(
              HostingView.this.createPlan(recommendedJson != null && recommendedJson.getAsBoolean(),
                  gson.fromJson(planJson.get("image"), ResourceLocation.class),
                  new TextComponent(planJson.get("title").getAsString())
                      .withStyle(ChatFormatting.BOLD),
                  new TextComponent(planJson.get("subtitle").getAsString()),
                  new TextComponent(planJson.get("price").getAsString())
                      .withStyle(ChatFormatting.BOLD),
                  URI.create(planJson.get("link").getAsString()),
                  Streams.stream(planJson.get("details").getAsJsonArray())
                      .map(JsonElement::getAsString)
                      .map(TextComponent::new)
                      .collect(Collectors.toList())));
        }

        HostingView.this.addChild(plansView);
        HostingView.this.layout();

      }

      @Override
      public void failed(Throwable exc, Void attachment) {
        logger.warn("Failed to read plans file", exc);
      }
    });
  }

  private View<?, YogaLayout> createPlan(boolean recommended, ResourceLocation image,
      Component title,
      Component subtitle,
      Component price,
      URI link, Collection<Component> specs) {

    var view = new ParentView<>(
        new YogaLayout()
            .setWidth(100)
            .setHeight(165)
            .setTopMargin(30)
            .setTopPadding(10),
        new YogaLayoutParent().setJustifyContent(Justify.CENTER).setAlignItems(Align.CENTER))
            .configure(v -> v.getBackgroundColorProperty().setBaseValue(new Color(0x70777777)))
            .setBackgroundBlur(50.0F)
            .configure(v -> v.getBorderRadiusProperty().setBaseValue(3.0F))
            .addChild(new ImageView<>(new YogaLayout().setPositionType(PositionType.ABSOLUTE)
                .setBottom(150).setWidth(30).setHeight(30))
                    .setImage(image)
                    .setBilinearFiltering(true))
            .addChild(new TextView<>(new YogaLayout())
                .setText(title)
                .setCentered(true))
            .addChild(new TextView<>(new YogaLayout()
                .setMargin(2))
                    .setText(subtitle)
                    .setCentered(true))
            .addChild(new TextView<>(new YogaLayout()
                .setTopMargin(10)
                .setBottomMargin(10))
                    .setText(price)
                    .setCentered(true)
                    .configure(v -> v.getScaleProperty().setBaseValue(1.25F)))
            .addChild(new TextView<>(
                new YogaLayout()
                    .setWidthPercent(80)
                    .setHeight(20)
                    .setBottomMargin(10))
                        .setText(ORDER_NOW_TEXT)
                        .setCentered(true)
                        .setShadow(false)
                        .configure(v -> v.getBackgroundColorProperty()
                            .setBaseValue(new Color(0x66EE0000))
                            .defineState(new Color(0x88EE0000), States.HOVERED, States.ENABLED)
                            .setTransition(Transition.linear(150L)))
                        .configure(v -> v.getBorderRadiusProperty().setBaseValue(3.0F))
                        .addActionSound(ImmerseSoundEvents.BUTTON_CLICK.get())
                        .addListener(ActionEvent.class,
                            (__, event) -> this.getScreen().keepOpenAndSetScreen(
                                new ConfirmLinkScreen(
                                    result -> {
                                      if (result) {
                                        Util.getPlatform().openUri(link);
                                      }
                                      this.minecraft.setScreen(this.getScreen());
                                    }, link.toString(), true))));

    for (var text : specs) {
      view.addChild(
          new TextView<>(new YogaLayout().setMargin(2)).setText(text).setCentered(true));
    }

    if (recommended) {
      view.getBorderColorProperty().setBaseValue(Color.DARK_RED);
      view.getLayout().setBorderWidth(10);
    }

    return view;
  }
}
