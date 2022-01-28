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

package com.craftingdead.immerse.client.gui.screen.menu.play.list.world;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import com.craftingdead.immerse.client.gui.view.Color;
import com.craftingdead.immerse.client.gui.view.ImageView;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.States;
import com.craftingdead.immerse.client.gui.view.TextView;
import com.craftingdead.immerse.client.gui.view.ViewScreen;
import com.craftingdead.immerse.client.gui.view.event.ActionEvent;
import com.craftingdead.immerse.client.gui.view.layout.yoga.FlexDirection;
import com.craftingdead.immerse.client.gui.view.layout.yoga.Justify;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayoutParent;
import com.google.common.hash.Hashing;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.AlertScreen;
import net.minecraft.client.gui.screens.BackupConfirmScreen;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.EditWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.LevelSummary;

class WorldItemView
    extends ParentView<WorldItemView, YogaLayout, YogaLayout> {

  private static final Logger logger = LogManager.getLogger();
  private static final DateFormat dateFormat = new SimpleDateFormat();

  private static final ResourceLocation UNKOWN_SERVER_ICON =
      new ResourceLocation("textures/misc/unknown_server.png");

  private final LevelSummary worldSummary;
  private final WorldListView<?> parentWorldList;

  WorldItemView(LevelSummary worldSummary, WorldListView<?> parentWorldList) {
    super(
        new YogaLayout()
            .setWidthPercent(100)
            .setHeight(46F)
            .setTopMargin(6F)
            .setMaxWidth(300F)
            .setPadding(4F),
        new YogaLayoutParent()
            .setFlexDirection(FlexDirection.ROW));
    this.worldSummary = worldSummary;
    this.parentWorldList = parentWorldList;
    String displayName = worldSummary.getLevelName();
    String info = worldSummary.getLevelId() + " (" + dateFormat.format(
        new Date(worldSummary.getLastPlayed())) + ")";
    String description = worldSummary.getInfo().getString();
    String levelId = worldSummary.getLevelId();
    @SuppressWarnings("deprecation")
    ResourceLocation dynamicWorldIcon =
        new ResourceLocation("worlds/" + Util.sanitizeName(levelId,
            ResourceLocation::validPathChar) + "/" + Hashing.sha1().hashUnencodedChars(levelId)
            + "/icon");
    ResourceLocation worldIcon;
    if (this.loadIconTexture(worldSummary, dynamicWorldIcon) != null) {
      worldIcon = dynamicWorldIcon;
    } else {
      worldIcon = UNKOWN_SERVER_ICON;
    }

    this.getOutlineWidthProperty()
        .defineState(1.0F, States.SELECTED)
        .defineState(1.0F, States.HOVERED)
        .defineState(1.0F, States.FOCUSED);
    this.getOutlineColorProperty()
        .defineState(Color.WHITE, States.SELECTED)
        .defineState(Color.GRAY, States.HOVERED)
        .defineState(Color.GRAY, States.FOCUSED);

    this.getBackgroundColorProperty().setBaseValue(new Color(0X882C2C2C));

    this
        .setFocusable(true)
        .setDoubleClick(true)
        .addListener(ActionEvent.class, (c, e) -> this.joinWorld())
        .addChild(new ImageView<>(new YogaLayout().setHeight(38F)
            .setWidth(38F)
            .setRightMargin(5F))
                .setImage(worldIcon))
        .addChild(new ParentView<>(
            new YogaLayout()
                .setWidth(120F)
                .setFlexGrow(1F)
                .setBottomPadding(1F),
            new YogaLayoutParent()
                .setFlexDirection(FlexDirection.COLUMN)
                .setJustifyContent(Justify.CENTER))
                    .addChild(new TextView<>(new YogaLayout())
                        .setText(displayName)
                        .setShadow(false))
                    .addChild(new TextView<>(new YogaLayout().setTopMargin(2F))
                        .setText(new TextComponent(info)
                            .withStyle(TextFormatting.GRAY))
                        .setShadow(false))
                    .addChild(new TextView<>(new YogaLayout())
                        .setText(new TextComponent(description)
                            .withStyle(TextFormatting.GRAY))
                        .setShadow(false)));
  }

  @Nullable
  private DynamicTexture loadIconTexture(LevelSummary worldSummary,
      ResourceLocation textureLocation) {
    File iconFile = worldSummary.getIcon();
    if (iconFile.isFile()) {
      try (InputStream inputStream = new FileInputStream(iconFile)) {
        NativeImage image = NativeImage.read(inputStream);
        Validate.validState(image.getWidth() == 64, "Must be 64 pixels wide");
        Validate.validState(image.getHeight() == 64, "Must be 64 pixels high");
        DynamicTexture texture = new DynamicTexture(image);
        this.minecraft.getTextureManager().register(textureLocation, texture);
        return texture;
      } catch (Throwable throwable) {
        logger.error("Invalid icon for world {}", worldSummary.getLevelId(), throwable);
        return null;
      }
    } else {
      this.minecraft.getTextureManager().release(textureLocation);
      return null;
    }
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (keyCode == GLFW.GLFW_KEY_SPACE && this.isFocused()) {
      this.toggleState(States.SELECTED);
      this.updateProperties(false);
      return true;
    }
    return super.keyPressed(keyCode, scanCode, modifiers);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    boolean consume = super.mouseClicked(mouseX, mouseY, button);
    this.setSelected(this.isHovered());
    return consume;
  }

  /**
   * A slightly edited copy of {@link WorldSelectionList.Entry#joinWorld}
   */
  public void joinWorld() {
    if (this.worldSummary.isDisabled()) {
      return;
    }

    var screen = (ViewScreen) this.getScreen();
    var backupStatus = this.worldSummary.backupStatus();
    if (backupStatus.shouldBackup()) {
      var backupQuestionKey = "selectWorld.backupQuestion." + backupStatus.getTranslationKey();
      var backupWarningKey = "selectWorld.backupWarning." + backupStatus.getTranslationKey();
      var backupQuestion = new TranslatableComponent(backupQuestionKey);
      if (backupStatus.isSevere()) {
        backupQuestion.withStyle(ChatFormatting.BOLD, ChatFormatting.RED);
      }

      var backupWarning =
          new TranslatableComponent(backupWarningKey, this.worldSummary.getWorldVersionName(),
              SharedConstants.getCurrentVersion().getName());

      screen.keepOpenAndSetScreen(
          new BackupConfirmScreen(screen, (backup, eraseCache) -> {
            if (backup) {
              var levelName = this.worldSummary.getLevelName();
              try (var levelSave = this.minecraft.getLevelSource().createAccess(levelName)) {
                EditWorldScreen.makeBackupAndShowToast(levelSave);
              } catch (IOException ioexception) {
                SystemToast.onWorldAccessFailure(this.minecraft, levelName);
                logger.error("Failed to backup level {}", levelName, ioexception);
              }
            }

            this.loadWorld();
            screen.removed();
          }, backupQuestion, backupWarning, false));
    } else if (this.worldSummary.askToOpenWorld()) {
      screen.keepOpenAndSetScreen(new ConfirmScreen((confirm) -> {
        if (confirm) {
          try {
            this.loadWorld();
            screen.removed();
          } catch (Exception exception) {
            logger.error("Failure to open 'future world'", (Throwable) exception);
            this.minecraft.setScreen(new AlertScreen(() -> {
              this.minecraft.setScreen(this.minecraft.screen);
            }, new TranslatableComponent("selectWorld.futureworld.error.title"),
                new TranslatableComponent("selectWorld.futureworld.error.text")));
          }
        } else {
          this.minecraft.setScreen(screen);
        }

      }, new TranslatableComponent("selectWorld.versionQuestion"),
          new TranslatableComponent("selectWorld.versionWarning",
              this.worldSummary.getWorldVersionName(),
              new TranslatableComponent("selectWorld.versionJoinButton"),
              CommonComponents.GUI_CANCEL)));
    } else {
      this.loadWorld();
    }

  }

  private void loadWorld() {
    if (this.minecraft.getLevelSource().levelExists(this.worldSummary.getLevelName())) {
      this.minecraft.forceSetScreen(
          new GenericDirtMessageScreen(new TranslatableComponent("selectWorld.data_read")));
      this.minecraft.loadLevel(this.worldSummary.getLevelId());
    }
  }

  /**
   * A slightly edited copy of {@link WorldSelectionList.Entry#editWorld}
   */
  public void editWorld() {
    var fileName = this.worldSummary.getLevelName();
    try {
      var levelSave = this.minecraft.getLevelSource().createAccess(fileName);
      var screen = this.getScreen();
      screen.keepOpenAndSetScreen(new EditWorldScreen(confirm -> {
        try {
          levelSave.close();
        } catch (IOException ioexception1) {
          logger.error("Failed to unlock level {}", fileName, ioexception1);
        }
        if (confirm) {
          this.parentWorldList.reloadWorlds();
        }
        this.minecraft.setScreen(screen);
      }, levelSave));
    } catch (IOException ioexception) {
      SystemToast.onWorldAccessFailure(this.minecraft, fileName);
      logger.error("Failed to access level {}", fileName, ioexception);
      this.parentWorldList.reloadWorlds();
    }
  }

  /**
   * A slightly edited copy of {@link WorldSelectionList.Entry#deleteWorld}
   */
  public void deleteWorld() {
    ViewScreen screen = this.getScreen();
    screen.keepOpenAndSetScreen(new ConfirmScreen(confirmed -> {
      if (confirmed) {
        this.minecraft.setScreen(new ProgressScreen(true));
        LevelStorageSource levelSource = this.minecraft.getLevelSource();
        String s = this.worldSummary.getLevelName();
        try (var levelSave = levelSource.createAccess(s)) {
          levelSave.deleteLevel();
        } catch (IOException ioexception) {
          SystemToast.onWorldDeleteFailure(this.minecraft, s);
          logger.error("Failed to delete world {}", s, ioexception);
        }
        this.parentWorldList.reloadWorlds();
      }
      this.minecraft.setScreen(screen);
    }, new TranslatableComponent("selectWorld.deleteQuestion"),
        new TranslatableComponent("selectWorld.deleteWarning",
            this.worldSummary.getLevelName()),
        new TranslatableComponent("selectWorld.deleteButton"),
        CommonComponents.GUI_CANCEL));
  }

  /**
   * A slightly edited copy of {@link WorldSelectionList.Entry#recreateWorld}
   */
  public void recreateWorld() {
    ViewScreen screen = this.getScreen();
    screen.keepOpen();

    this.minecraft.forceSetScreen(
        new GenericDirtMessageScreen(new TranslatableComponent("selectWorld.data_read")));
    RegistryAccess.RegistryHolder dynamicRegistries = RegistryAccess.builtin();

    try (
        LevelStorageSource.LevelStorageAccess levelSave =
            this.minecraft.getLevelSource().createAccess(this.worldSummary.getLevelName());
        Minecraft.ServerStem serverStem = this.minecraft.makeServerStem(dynamicRegistries,
            Minecraft::loadDataPacks, Minecraft::loadWorldData, false, levelSave);) {
      LevelSettings levelSettings = serverStem.worldData().getLevelSettings();
      DataPackConfig dataPackCodec = levelSettings.getDataPackConfig();
      WorldGenSettings worldGenSettings = serverStem
          .worldData().worldGenSettings();
      Path path =
          CreateWorldScreen.createTempDataPackDirFromExistingWorld(
              levelSave.getLevelPath(LevelResource.DATAPACK_DIR),
              this.minecraft);
      if (worldGenSettings.isOldCustomizedWorld()) {
        this.minecraft.setScreen(new ConfirmScreen(confirm -> this.minecraft.setScreen(confirm
            ? new CreateWorldScreen(screen, levelSettings,
                worldGenSettings, path, dataPackCodec, dynamicRegistries)
            : screen),
            new TranslatableComponent("selectWorld.recreate.customized.title"),
            new TranslatableComponent("selectWorld.recreate.customized.text"),
            CommonComponents.GUI_PROCEED, CommonComponents.GUI_CANCEL));
      } else {
        this.minecraft.setScreen(new CreateWorldScreen(screen, levelSettings,
            worldGenSettings, path, dataPackCodec, dynamicRegistries));
      }
    } catch (Exception e) {
      logger.error("Unable to recreate world", e);
      this.minecraft.setScreen(new AlertScreen(() -> this.minecraft.setScreen(screen),
          new TranslatableComponent("selectWorld.recreate.error.title"),
          new TranslatableComponent("selectWorld.recreate.error.text")));
    }
  }
}
