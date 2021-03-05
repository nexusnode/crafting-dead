/*
 * Crafting Dead
 * Copyright (C)  2021  Nexus Node
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

package com.craftingdead.immerse.client.gui.menu.play;

import com.craftingdead.immerse.client.gui.component.*;
import com.craftingdead.immerse.client.gui.component.type.Align;
import com.craftingdead.immerse.client.gui.component.type.FlexDirection;
import com.craftingdead.immerse.client.gui.component.type.Justify;
import com.craftingdead.immerse.client.gui.component.type.Overflow;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.craftingdead.immerse.network.ServerPinger;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import net.minecraft.util.DefaultUncaughtExceptionHandler;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ServerListComponent extends ContainerComponent {

  private static final Logger logger = LogManager.getLogger();

  private final Gson gson = new Gson();
  private final String serverListKey;
  private final Executor executor = Executors.newFixedThreadPool(5, new ThreadFactoryBuilder()
      .setNameFormat("Server Pinger #%d")
      .setDaemon(true)
      .setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(logger))
      .build());
  private final ServerPinger serverPinger = new ServerPinger();

  public ServerListComponent(String serverListKey) {
    this.serverListKey = serverListKey;
    this.setFlexGrow(1F);
    this.setFlexDirection(FlexDirection.COLUMN);
    ContainerComponent listContainer = new ContainerComponent()
        .addChild(this.firstTableRow())
        .setFlexDirection(FlexDirection.COLUMN)
        .setAlignItems(Align.CENTER)
        .setOverflow(Overflow.SCROLL)
        .setBottomPadding(7F)
        .setHeight(60F)
        .setFlexGrow(1F)
        .addActionListener(this::resetSelection);

    this.addChild(listContainer);
    this.addChild(this.controlsContainer(listContainer));
    this.loadServers(listContainer);
  }

  private Component<?> controlsContainer(ContainerComponent listContainer) {
    return new ContainerComponent()
        .setHeight(30F)
        .setJustifyContent(Justify.CENTER)
        .setAlignItems(Align.CENTER)
        .setFlexDirection(FlexDirection.ROW)
        .setBackgroundColour(new Colour(0x40121212))
        .addChild(new ContainerComponent()
            .addChild(new TextBlockComponent(
                new TranslationTextComponent("menu.play.server_list.button.refresh"))
                    .setShadow(false)
                    .setHeight(8F))
            .setJustifyContent(Justify.CENTER)
            .setAlignItems(Align.CENTER)
            .setBackgroundColour(new Colour(PlayComponent.BLUE))
            .addHoverAnimation(Component.BACKGROUND_COLOUR,
                RenderUtil.getColour4f(RenderUtil.getColour4i(PlayComponent.BLUE_HIGHLIGHTED)), 60F)
            .setHeight(21F)
            .setRightMargin(15F)
            .setWidth(70F)
            .setTopPadding(1F)
            .addActionListener(containerComponent -> this.refresh(listContainer)))
        .addChild(new ContainerComponent()
            .addChild(new TextBlockComponent(
                new TranslationTextComponent("menu.play.server_list.button.play"))
                    .setShadow(false)
                    .setHeight(8F))
            .setJustifyContent(Justify.CENTER)
            .setAlignItems(Align.CENTER)
            .setBackgroundColour(new Colour(PlayComponent.GREEN))
            .addHoverAnimation(Component.BACKGROUND_COLOUR,
                RenderUtil.getColour4f(RenderUtil.getColour4i(PlayComponent.GREEN_HIGHLIGHTED)),
                60F)
            .setHeight(21F)
            .setWidth(70F)
            .setTopPadding(1F)
            .addActionListener(containerComponent -> this.connectToSelected(listContainer)));
  }

  private Component<?> firstTableRow() {
    return new ContainerComponent()
        .setTopMargin(5F)
        .setBottomMargin(1F)
        .setLeftMargin(7F)
        .setRightMargin(7F)
        .setLeftPadding(10F)
        .setRightPadding(20F)
        .setHeight(22F)
        .setBackgroundColour(new Colour(0x40121212))
        .setMaxWidth(520F)
        .setFlexDirection(FlexDirection.ROW)
        .setAlignItems(Align.CENTER)
        .addChild(
            new TextBlockComponent(new TranslationTextComponent("menu.play.server_list.server"))
                .setFlexGrow(1F)
                .setShadow(false)
                .setCentered(true)
                .setHeight(8))
        .addChild(new TextBlockComponent(new TranslationTextComponent("menu.play.server_list.ping"))
            .setWidth(60F)
            .setShadow(false)
            .setCentered(true)
            .setLeftMargin(10F)
            .setHeight(8))
        .addChild(
            new TextBlockComponent(new TranslationTextComponent("menu.play.server_list.players"))
                .setWidth(60F)
                .setShadow(false)
                .setCentered(true)
                .setLeftMargin(10F)
                .setHeight(8));
  }

  private void connectToSelected(ParentComponent<?> parent) {
    parent.getChildrenComponents().stream()
        .filter(component -> component instanceof ServerItemComponent
            && ((ServerItemComponent) component).isSelected())
        .findFirst()
        .ifPresent(component -> ((ServerItemComponent) component).connect());
  }

  private void refresh(ParentComponent<?> parent) {
    for (Component<?> child : parent.getChildrenComponents()) {
      if (child instanceof ServerItemComponent) {
        ((ServerItemComponent) child).refreshPing();
      }
    }
  }

  private void loadServers(ContainerComponent listContainer) {
    File file = new File("server_list.json");
    try {
      FileReader fileReader = new FileReader(file);
      JsonReader jsonReader = new JsonReader(fileReader);
      JsonObject jsonObject = gson.fromJson(jsonReader, JsonObject.class);
      JsonArray jsonArray = jsonObject.getAsJsonArray(this.serverListKey);
      if (jsonArray == null) {
        logger.info("Missing server list with key {}", this.serverListKey);
        return;
      }

      for (JsonElement jsonElement : jsonArray) {
        JsonObject serverObj = jsonElement.getAsJsonObject();
        listContainer.addChild(new ServerItemComponent(this, serverObj.get("name").getAsString(),
            serverObj.get("hostName").getAsString(), serverObj.get("port").getAsInt()));
      }

    } catch (FileNotFoundException | JsonSyntaxException | JsonIOException e) {
      logger.warn("Unable to load server list from {} - {}", file.getAbsolutePath(),
          e.getMessage());
      return;
    }
  }

  private void resetSelection(ParentComponent<?> parent) {
    for (Component<?> child : parent.getChildrenComponents()) {
      if (child instanceof ServerItemComponent) {
        ((ServerItemComponent) child).removeSelect();
      }
    }
  }

  @Override
  public void tick() {
    super.tick();
    serverPinger.pingPendingNetworks();
  }

  @Override
  public void close() {
    super.close();
    serverPinger.clearPendingNetworks();
  }

  public Executor getExecutor() {
    return executor;
  }

  public ServerPinger getServerPinger() {
    return serverPinger;
  }

}
