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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.core.util.Text;
import com.craftingdead.immerse.client.gui.component.Colour;
import com.craftingdead.immerse.client.gui.component.ContainerComponent;
import com.craftingdead.immerse.client.gui.component.ImageComponent;
import com.craftingdead.immerse.client.gui.component.TextBlockComponent;
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

public class WorldItemComponent extends ContainerComponent {

  private static final Logger LOGGER = LogManager.getLogger();
  private static final DateFormat dateFormat = new SimpleDateFormat();
  private static final ResourceLocation unknownServerIcon =
      new ResourceLocation("minecraft", "textures/misc/unknown_server.png");

  private final WorldSummary worldSummary;
  private final WorldListComponent parentWorldList;

  private boolean hovered = false;
  private boolean selected = false;
  private int doubleClickTicks = 0;

  public WorldItemComponent(WorldSummary worldSummary, WorldListComponent parentWorldList) {
    this.worldSummary = worldSummary;
    this.parentWorldList = parentWorldList;
    String displayName = worldSummary.getDisplayName();
    if (StringUtils.isEmpty(displayName)) {
      displayName = "Unnamed World";
    }
    String info = worldSummary.getFileName() + " (" + dateFormat.format(
        new Date(worldSummary.getLastTimePlayed())) + ")";
    String description = worldSummary.getDescription().getString();
    String fileName = worldSummary.getFileName();
    ResourceLocation dynamicWorldIcon =
        new ResourceLocation("minecraft", "worlds/" + Util.func_244361_a(fileName,
        ResourceLocation::validatePathChar) + "/" + Hashing.sha1().hashUnencodedChars(fileName) + "/icon");
    ResourceLocation worldIcon;
    if (loadIconTexture(worldSummary, dynamicWorldIcon) != null) {
      worldIcon = dynamicWorldIcon;
    } else {
      worldIcon = unknownServerIcon;
    }


    this.setFlexDirection(FlexDirection.ROW)
        .setHeight(46F)
        .setTopMargin(6F)
        .setMaxWidth(300F)
        .setBackgroundColour(new Colour(0x44393939))
        .setPadding(4F)
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
                    .mergeStyle(TextFormatting.GRAY))
                .setTopMargin(2F)
                .setShadow(false))
            .addChild(new TextBlockComponent(Text.of(description)
                    .mergeStyle(TextFormatting.GRAY))
                .setShadow(false))
          );
  }

  @Nullable
  private DynamicTexture loadIconTexture(WorldSummary worldSummary, ResourceLocation resourceLocation) {
    File iconFile = worldSummary.getIconFile();
    if (iconFile.isFile()) {
      try (InputStream inputstream = new FileInputStream(iconFile)) {
        NativeImage nativeimage = NativeImage.read(inputstream);
        Validate.validState(nativeimage.getWidth() == 64, "Must be 64 pixels wide");
        Validate.validState(nativeimage.getHeight() == 64, "Must be 64 pixels high");
        DynamicTexture dynamictexture = new DynamicTexture(nativeimage);
        this.minecraft.getTextureManager().loadTexture(resourceLocation, dynamictexture);
        return dynamictexture;
      } catch (Throwable throwable) {
        LOGGER.error("Invalid icon for world {}", worldSummary.getFileName(), throwable);
        return null;
      }
    } else {
      this.minecraft.getTextureManager().deleteTexture(resourceLocation);
      return null;
    }
  }

  @Override
  protected void mouseEntered(double mouseX, double mouseY) {
    super.mouseEntered(mouseX, mouseY);
    this.hovered = true;
    this.updateBorder();
  }

  @Override
  protected void mouseLeft(double mouseX, double mouseY) {
    super.mouseLeft(mouseX, mouseY);
    this.hovered = false;
    this.updateBorder();
  }

  private void updateBorder() {
    if (selected) {
      this.setBorderWidth(1.5F);
    } else if (hovered) {
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
    return selected;
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    this.selected = true;
    this.updateBorder();
    if (doubleClickTicks > 0) {
      this.joinWorld();
    } else {
      this.doubleClickTicks = 4;
    }
    return super.mouseClicked(mouseX, mouseY, button);
  }

  @Override
  public void tick() {
    super.tick();
    if (doubleClickTicks > 0) {
      doubleClickTicks--;
    }
  }

  /**
   * A slightly edited copy of {@link WorldSelectionList.Entry#func_214438_a}
   */
  public void joinWorld() {
    if (!this.worldSummary.isLocked()) {
      if (this.worldSummary.askToCreateBackup()) {
        ITextComponent itextcomponent = new TranslationTextComponent("selectWorld.backupQuestion");
        ITextComponent itextcomponent1 = new TranslationTextComponent("selectWorld.backupWarning",
            this.worldSummary.getVersionName(), SharedConstants.getVersion().getName());
        this.minecraft.displayGuiScreen(new ConfirmBackupScreen(this.getScreen(), (p_214436_1_, p_214436_2_) -> {
          if (p_214436_1_) {
            String s = this.worldSummary.getFileName();

            try (SaveFormat.LevelSave saveformat$levelsave = this.minecraft.getSaveLoader().getLevelSave(s)) {
              EditWorldScreen.func_239019_a_(saveformat$levelsave);
            } catch (IOException ioexception) {
              SystemToast.func_238535_a_(this.minecraft, s);
              LOGGER.error("Failed to backup level {}", s, ioexception);
            }
          }

          this.loadWorld();
        }, itextcomponent, itextcomponent1, false));
      } else if (this.worldSummary.askToOpenWorld()) {
        this.minecraft.displayGuiScreen(new ConfirmScreen((p_214434_1_) -> {
          if (p_214434_1_) {
            try {
              this.loadWorld();
            } catch (Exception exception) {
              LOGGER.error("Failure to open 'future world'", (Throwable)exception);
              this.minecraft.displayGuiScreen(new AlertScreen(() -> {
                this.minecraft.displayGuiScreen(this.getScreen());
              }, new TranslationTextComponent("selectWorld.futureworld.error.title"),
                  new TranslationTextComponent("selectWorld.futureworld.error.text")));
            }
          } else {
            this.minecraft.displayGuiScreen(this.getScreen());
          }

        }, new TranslationTextComponent("selectWorld.versionQuestion"),
            new TranslationTextComponent("selectWorld.versionWarning",
                this.worldSummary.getVersionName(), new TranslationTextComponent("selectWorld.versionJoinButton"),
                DialogTexts.GUI_CANCEL)));
      } else {
        this.loadWorld();
      }
    }
  }

  private void loadWorld() {
//    this.minecraft.getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    if (this.minecraft.getSaveLoader().canLoadWorld(this.worldSummary.getFileName())) {
      this.minecraft
          .forcedScreenTick(new DirtMessageScreen(new TranslationTextComponent("selectWorld.data_read")));
      this.minecraft.loadWorld(this.worldSummary.getFileName());
    }
  }

  /**
   * A slightly edited copy of {@link WorldSelectionList.Entry#func_214444_c}
   */
  public void editWorld() {
    String fileName = this.worldSummary.getFileName();
    try {
      SaveFormat.LevelSave levelSave = this.minecraft.getSaveLoader().getLevelSave(fileName);
      this.minecraft.displayGuiScreen(new EditWorldScreen((backupFailed) -> {
        try {
          levelSave.close();
        } catch (IOException ioexception1) {
          LOGGER.error("Failed to unlock level {}", fileName, ioexception1);
        }
        Screen screen = this.getScreen();
        this.parentWorldList.reloadWorlds();
        this.minecraft.displayGuiScreen(screen);
      }, levelSave));
    } catch (IOException ioexception) {
      SystemToast.func_238535_a_(this.minecraft, fileName);
      LOGGER.error("Failed to access level {}", fileName, ioexception);
      this.parentWorldList.reloadWorlds();
    }
  }

  /**
   * A slightly edited copy of {@link WorldSelectionList.Entry#func_214442_b}
   */
  public void deleteWorld() {
    this.minecraft.displayGuiScreen(new ConfirmScreen((confirmed) -> {
      Screen screen = this.getScreen();
      if (confirmed) {
        this.minecraft.displayGuiScreen(new WorkingScreen());
        SaveFormat saveformat = this.minecraft.getSaveLoader();
        String s = this.worldSummary.getFileName();
        try (SaveFormat.LevelSave levelSave = saveformat.getLevelSave(s)) {
          levelSave.deleteSave();
        } catch (IOException ioexception) {
          SystemToast.func_238538_b_(this.minecraft, s);
          LOGGER.error("Failed to delete world {}", s, ioexception);
        }
        this.parentWorldList.reloadWorlds();
      }
      this.minecraft.displayGuiScreen(screen);
    }, new TranslationTextComponent("selectWorld.deleteQuestion"),
        new TranslationTextComponent("selectWorld.deleteWarning",
            this.worldSummary.getDisplayName()), new TranslationTextComponent("selectWorld.deleteButton"),
        DialogTexts.GUI_CANCEL));
  }

  /**
   * A slightly edited copy of {@link WorldSelectionList.Entry#func_214445_d}
   */
  public void recreateWorld() {
    this.minecraft.forcedScreenTick(new DirtMessageScreen(new TranslationTextComponent("selectWorld.data_read")));
    DynamicRegistries.Impl dynamicRegistries = DynamicRegistries.func_239770_b_();

    try (
        SaveFormat.LevelSave levelSave = this.minecraft.getSaveLoader().getLevelSave(this.worldSummary.getFileName());
        Minecraft.PackManager packManager = this.minecraft.reloadDatapacks(dynamicRegistries,
            Minecraft::loadDataPackCodec, Minecraft::loadWorld, false, levelSave);
    ) {
      WorldSettings worldsettings = packManager.getServerConfiguration().getWorldSettings();
      DatapackCodec datapackcodec = worldsettings.getDatapackCodec();
      DimensionGeneratorSettings dimensiongeneratorsettings = packManager
          .getServerConfiguration()
          .getDimensionGeneratorSettings();
      Path path = CreateWorldScreen.func_238943_a_(levelSave.resolveFilePath(FolderName.DATAPACKS), this.minecraft);
      if (dimensiongeneratorsettings.func_236229_j_()) {
        this.minecraft.displayGuiScreen(new ConfirmScreen((p_239095_6_) -> {
          this.minecraft.displayGuiScreen(p_239095_6_ ? new CreateWorldScreen(this.getScreen(), worldsettings,
              dimensiongeneratorsettings, path, datapackcodec, dynamicRegistries) : this.getScreen());
        }, new TranslationTextComponent("selectWorld.recreate.customized.title"),
            new TranslationTextComponent("selectWorld.recreate.customized.text"),
            DialogTexts.GUI_PROCEED, DialogTexts.GUI_CANCEL));
      } else {
        this.minecraft.displayGuiScreen(new CreateWorldScreen(this.getScreen(), worldsettings,
            dimensiongeneratorsettings, path, datapackcodec, dynamicRegistries));
      }
    } catch (Exception exception) {
      LOGGER.error("Unable to recreate world", exception);
      this.minecraft.displayGuiScreen(new AlertScreen(() -> {
        this.minecraft.displayGuiScreen(this.getScreen());
      }, new TranslationTextComponent("selectWorld.recreate.error.title"),
          new TranslationTextComponent("selectWorld.recreate.error.text")));
    }

  }

}
