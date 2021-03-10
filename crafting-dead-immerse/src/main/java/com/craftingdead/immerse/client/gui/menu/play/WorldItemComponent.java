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

package com.craftingdead.immerse.client.gui.menu.play;

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
import com.craftingdead.core.util.Text;
import com.craftingdead.immerse.client.gui.component.Colour;
import com.craftingdead.immerse.client.gui.component.ContainerComponent;
import com.craftingdead.immerse.client.gui.component.ImageComponent;
import com.craftingdead.immerse.client.gui.component.ParentComponent;
import com.craftingdead.immerse.client.gui.component.TextBlockComponent;
import com.craftingdead.immerse.client.gui.component.event.ActionEvent;
import com.craftingdead.immerse.client.gui.component.type.FlexDirection;
import com.craftingdead.immerse.client.gui.component.type.Justify;
import com.google.common.hash.Hashing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.screen.AlertScreen;
import net.minecraft.client.gui.screen.ConfirmBackupScreen;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.CreateWorldScreen;
import net.minecraft.client.gui.screen.DirtMessageScreen;
import net.minecraft.client.gui.screen.EditWorldScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WorkingScreen;
import net.minecraft.client.gui.screen.WorldSelectionList;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.codec.DatapackCodec;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.gen.settings.DimensionGeneratorSettings;
import net.minecraft.world.storage.FolderName;
import net.minecraft.world.storage.SaveFormat;
import net.minecraft.world.storage.WorldSummary;

public class WorldItemComponent extends ParentComponent<WorldItemComponent> {

  private static final Logger logger = LogManager.getLogger();
  private static final DateFormat dateFormat = new SimpleDateFormat();
  private static final ResourceLocation UNKOWN_SERVER_ICON =
      new ResourceLocation("textures/misc/unknown_server.png");

  private final WorldSummary worldSummary;
  private final WorldListComponent parentWorldList;

  private boolean selected = false;

  public WorldItemComponent(WorldSummary worldSummary, WorldListComponent parentWorldList) {
    this.worldSummary = worldSummary;
    this.parentWorldList = parentWorldList;
    String displayName = worldSummary.getLevelName();
    String info = worldSummary.getLevelId() + " (" + dateFormat.format(
        new Date(worldSummary.getLastPlayed())) + ")";
    String description = worldSummary.getInfo().getString();
    String levelId = worldSummary.getLevelId();
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

    this.setFlexDirection(FlexDirection.ROW)
        .setHeight(46F)
        .setTopMargin(6F)
        .setMaxWidth(300F)
        .setBackgroundColour(new Colour(0X882C2C2C))
        .setPadding(4F)
        .setFocusable(true)
        .setDoubleClick(true)
        .addListener(ActionEvent.class, (c, e) -> this.joinWorld())
        .addChild(new ImageComponent()
            .setImage(worldIcon)
            .setHeight(38F)
            .setWidth(38F)
            .setRightMargin(5F))
        .addChild(new ContainerComponent()
            .setWidth(120F)
            .setFlexGrow(1F)
            .setFlexDirection(FlexDirection.COLUMN)
            .setJustifyContent(Justify.CENTER)
            .setBottomPadding(1F)
            .addChild(new TextBlockComponent(Text.of(displayName))
                .setShadow(false))
            .addChild(new TextBlockComponent(Text.of(info)
                .withStyle(TextFormatting.GRAY))
                    .setTopMargin(2F)
                    .setShadow(false))
            .addChild(new TextBlockComponent(Text.of(description)
                .withStyle(TextFormatting.GRAY))
                    .setShadow(false)));
  }

  @Nullable
  private DynamicTexture loadIconTexture(WorldSummary worldSummary,
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
    if (keyCode == GLFW.GLFW_KEY_SPACE && this.focused) {
      this.selected = !this.selected;
      this.updateBorder();
      return true;
    }
    return super.keyPressed(keyCode, scanCode, modifiers);
  }

  @Override
  protected void mouseEntered(double mouseX, double mouseY) {
    super.mouseEntered(mouseX, mouseY);
    this.updateBorder();
  }

  @Override
  protected void mouseLeft(double mouseX, double mouseY) {
    super.mouseLeft(mouseX, mouseY);
    this.updateBorder();
  }

  private void updateBorder() {
    if (this.selected) {
      this.setBorderWidth(1.5F);
    } else if (this.isHovered() || this.isFocused()) {
      this.setBorderWidth(0.7F);
    } else {
      this.setBorderWidth(0F);
    }
  }

  public void removeSelect() {
    this.selected = false;
    this.updateBorder();
  }

  public boolean isSelected() {
    return this.selected;
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (super.mouseClicked(mouseX, mouseY, button)) {
      return true;
    }

    if (this.isHovered()) {
      this.selected = true;
      this.updateBorder();
      return true;
    } else {
      this.selected = false;
      this.updateBorder();
    }

    return false;
  }

  /**
   * A slightly edited copy of {@link WorldSelectionList.Entry#joinWorld}
   */
  public void joinWorld() {
    if (!this.worldSummary.isLocked()) {
      if (this.worldSummary.shouldBackup()) {
        ITextComponent itextcomponent = new TranslationTextComponent("selectWorld.backupQuestion");
        ITextComponent itextcomponent1 = new TranslationTextComponent("selectWorld.backupWarning",
            this.worldSummary.getWorldVersionName(), SharedConstants.getCurrentVersion().getName());
        this.minecraft.setScreen(
            new ConfirmBackupScreen(this.getScreen(), (p_214436_1_, p_214436_2_) -> {
              if (p_214436_1_) {
                String s = this.worldSummary.getLevelName();

                try (SaveFormat.LevelSave levelSave =
                    this.minecraft.getLevelSource().createAccess(s)) {
                  EditWorldScreen.makeBackupAndShowToast(levelSave);
                } catch (IOException ioexception) {
                  SystemToast.onWorldAccessFailure(this.minecraft, s);
                  logger.error("Failed to backup level {}", s, ioexception);
                }
              }

              this.loadWorld();
            }, itextcomponent, itextcomponent1, false));
      } else if (this.worldSummary.askToOpenWorld()) {
        this.minecraft.setScreen(new ConfirmScreen((p_214434_1_) -> {
          if (p_214434_1_) {
            try {
              this.loadWorld();
            } catch (Exception exception) {
              logger.error("Failure to open 'future world'", (Throwable) exception);
              this.minecraft.setScreen(new AlertScreen(() -> {
                this.minecraft.setScreen(this.getScreen());
              }, new TranslationTextComponent("selectWorld.futureworld.error.title"),
                  new TranslationTextComponent("selectWorld.futureworld.error.text")));
            }
          } else {
            this.minecraft.setScreen(this.getScreen());
          }

        }, new TranslationTextComponent("selectWorld.versionQuestion"),
            new TranslationTextComponent("selectWorld.versionWarning",
                this.worldSummary.getWorldVersionName(),
                new TranslationTextComponent("selectWorld.versionJoinButton"),
                DialogTexts.GUI_CANCEL)));
      } else {
        this.loadWorld();
      }
    }
  }

  private void loadWorld() {
    if (this.minecraft.getLevelSource().levelExists(this.worldSummary.getLevelName())) {
      this.minecraft.forceSetScreen(
          new DirtMessageScreen(new TranslationTextComponent("selectWorld.data_read")));
      this.minecraft.loadLevel(this.worldSummary.getLevelId());
    }
  }

  /**
   * A slightly edited copy of {@link WorldSelectionList.Entry#editWorld}
   */
  public void editWorld() {
    String fileName = this.worldSummary.getLevelName();
    try {
      SaveFormat.LevelSave levelSave = this.minecraft.getLevelSource().createAccess(fileName);
      this.minecraft.setScreen(new EditWorldScreen((backupFailed) -> {
        try {
          levelSave.close();
        } catch (IOException ioexception1) {
          logger.error("Failed to unlock level {}", fileName, ioexception1);
        }
        Screen screen = this.getScreen();
        this.parentWorldList.reloadWorlds();
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
    this.minecraft.setScreen(new ConfirmScreen((confirmed) -> {
      Screen screen = this.getScreen();
      if (confirmed) {
        this.minecraft.setScreen(new WorkingScreen());
        SaveFormat levelSource = this.minecraft.getLevelSource();
        String s = this.worldSummary.getLevelName();
        try (SaveFormat.LevelSave levelSave = levelSource.createAccess(s)) {
          levelSave.deleteLevel();
        } catch (IOException ioexception) {
          SystemToast.onWorldDeleteFailure(this.minecraft, s);
          logger.error("Failed to delete world {}", s, ioexception);
        }
        this.parentWorldList.reloadWorlds();
      }
      this.minecraft.setScreen(screen);
    }, new TranslationTextComponent("selectWorld.deleteQuestion"),
        new TranslationTextComponent("selectWorld.deleteWarning",
            this.worldSummary.getLevelName()),
        new TranslationTextComponent("selectWorld.deleteButton"),
        DialogTexts.GUI_CANCEL));
  }

  /**
   * A slightly edited copy of {@link WorldSelectionList.Entry#recreateWorld}
   */
  public void recreateWorld() {
    this.minecraft.forceSetScreen(
        new DirtMessageScreen(new TranslationTextComponent("selectWorld.data_read")));
    DynamicRegistries.Impl dynamicRegistries = DynamicRegistries.builtin();

    try (
        SaveFormat.LevelSave levelSave =
            this.minecraft.getLevelSource().createAccess(this.worldSummary.getLevelName());
        Minecraft.PackManager serverStem = this.minecraft.makeServerStem(dynamicRegistries,
            Minecraft::loadDataPacks, Minecraft::loadWorldData, false, levelSave);) {
      WorldSettings levelSettings = serverStem.worldData().getLevelSettings();
      DatapackCodec dataPackCodec = levelSettings.getDataPackConfig();
      DimensionGeneratorSettings worldGenSettings = serverStem
          .worldData().worldGenSettings();
      Path path =
          CreateWorldScreen.createTempDataPackDirFromExistingWorld(
              levelSave.getLevelPath(FolderName.DATAPACK_DIR),
              this.minecraft);
      if (worldGenSettings.isOldCustomizedWorld()) {
        this.minecraft.setScreen(new ConfirmScreen((p_239095_6_) -> {
          this.minecraft.setScreen(p_239095_6_
              ? new CreateWorldScreen(this.getScreen(), levelSettings,
                  worldGenSettings, path, dataPackCodec, dynamicRegistries)
              : this.getScreen());
        }, new TranslationTextComponent("selectWorld.recreate.customized.title"),
            new TranslationTextComponent("selectWorld.recreate.customized.text"),
            DialogTexts.GUI_PROCEED, DialogTexts.GUI_CANCEL));
      } else {
        this.minecraft.setScreen(new CreateWorldScreen(this.getScreen(), levelSettings,
            worldGenSettings, path, dataPackCodec, dynamicRegistries));
      }
    } catch (Exception e) {
      logger.error("Unable to recreate world", e);
      this.minecraft.setScreen(new AlertScreen(() -> {
        this.minecraft.setScreen(this.getScreen());
      }, new TranslationTextComponent("selectWorld.recreate.error.title"),
          new TranslationTextComponent("selectWorld.recreate.error.text")));
    }
  }
}
