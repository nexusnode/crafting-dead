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

package com.craftingdead.immerse.client.gui.screen.menu.play.list.world;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import io.github.humbleui.skija.Image;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.AlertScreen;
import net.minecraft.client.gui.screens.BackupConfirmScreen;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.EditWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.LevelSummary;
import sm0keysa1m0n.bliss.view.ImageView;
import sm0keysa1m0n.bliss.view.ParentView;
import sm0keysa1m0n.bliss.view.TextView;
import sm0keysa1m0n.bliss.view.ViewScreen;
import sm0keysa1m0n.bliss.view.event.ActionEvent;

class WorldItemView extends ParentView {

  private static final Logger logger = LogUtils.getLogger();
  private static final DateFormat dateFormat = new SimpleDateFormat();

  private static final ResourceLocation UNKOWN_SERVER_ICON =
      new ResourceLocation("textures/misc/unknown_server.png");

  private final LevelSummary worldSummary;
  private final WorldListView parentWorldList;

  private Image iconImage;

  WorldItemView(LevelSummary levelSummary, WorldListView parentWorldList) {
    super(new Properties().styleClasses("item").doubleClick(true).focusable(true));
    this.worldSummary = levelSummary;
    this.parentWorldList = parentWorldList;

    this.iconImage = this.loadIconTexture(levelSummary);
    if (this.iconImage == null) {
      try (var inputStream =
          this.minecraft.getResourceManager().getResource(UNKOWN_SERVER_ICON).getInputStream()) {
        this.iconImage = Image.makeFromEncoded(inputStream.readAllBytes());
      } catch (IOException e) {
        // This shouldn't happen, if it does we'll crash the game.
        throw new UncheckedIOException(e);
      }
    }

    this.addListener(ActionEvent.class, event -> this.joinWorld(), true);
    this.addChild(new ImageView(new Properties().id("icon")).setImage(this.iconImage));

    var texts = new ParentView(new Properties().id("texts"));
    texts.addChild(new TextView(new Properties())
        .setText(levelSummary.getLevelName()));
    texts.addChild(new TextView(new Properties().id("details"))
        .setText(new TextComponent(levelSummary.getLevelId() + " (" + dateFormat.format(
            new Date(levelSummary.getLastPlayed())) + ")")
                .withStyle(ChatFormatting.GRAY)));
    texts.addChild(new TextView(new Properties())
        .setText(levelSummary.getInfo().copy()
            .withStyle(ChatFormatting.GRAY)));
    this.addChild(texts);
  }

  @Nullable
  private Image loadIconTexture(LevelSummary levelSummary) {
    var iconFile = levelSummary.getIcon();
    if (!iconFile.isFile()) {
      return null;
    }

    try (InputStream inputStream = new FileInputStream(iconFile)) {
      var image = Image.makeFromEncoded(inputStream.readAllBytes());
      Validate.validState(image.getWidth() == 64, "Must be 64 pixels wide");
      Validate.validState(image.getHeight() == 64, "Must be 64 pixels high");
      return image;
    } catch (Throwable throwable) {
      logger.error("Invalid icon for world {}", levelSummary.getLevelId(), throwable);
      return null;
    }
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

    try (
        var levelSave =
            this.minecraft.getLevelSource().createAccess(this.worldSummary.getLevelName());
        var worldStem = this.minecraft.makeWorldStem(levelSave, false);) {
      var worldGenSettings = worldStem.worldData().worldGenSettings();
      var path = CreateWorldScreen.createTempDataPackDirFromExistingWorld(
          levelSave.getLevelPath(LevelResource.DATAPACK_DIR),
          this.minecraft);
      if (worldGenSettings.isOldCustomizedWorld()) {
        this.minecraft.setScreen(new ConfirmScreen(confirm -> this.minecraft.setScreen(confirm
            ? CreateWorldScreen.createFromExisting(screen, worldStem, path)
            : screen),
            new TranslatableComponent("selectWorld.recreate.customized.title"),
            new TranslatableComponent("selectWorld.recreate.customized.text"),
            CommonComponents.GUI_PROCEED, CommonComponents.GUI_CANCEL));
      } else {
        this.minecraft.setScreen(CreateWorldScreen.createFromExisting(screen, worldStem, path));
      }
    } catch (Exception e) {
      logger.error("Unable to recreate world", e);
      this.minecraft.setScreen(new AlertScreen(() -> this.minecraft.setScreen(screen),
          new TranslatableComponent("selectWorld.recreate.error.title"),
          new TranslatableComponent("selectWorld.recreate.error.text")));
    }
  }
}
