package com.craftingdead.core.client.gui;

import java.io.IOException;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.gui.widget.button.SimpleImageButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.opposingforces.mod.client.gui.library.controls.button.TextButton;
import net.opposingforces.mod.client.renderer.BackgroundRenderer;
import net.opposingforces.mod.common.core.OpposingForces;
import net.opposingforces.mod.common.network.message.client.CMessageChooseTeamResult;

public class SelectTeamScreen extends Screen {

  private boolean cancellable;
  private float menuWidth;
  private float menuHeight;
  private float menuX;
  private float menuY;
  private SimpleImageButton alphaButton;
  private SimpleImageButton omegaButton;

  public SelectTeamScreen(boolean cancellable) {
    // TODO add translation
    super(new TranslationTextComponent("select_team.title"));
    this.cancellable = cancellable;
  }

  @Override
  public void init() {
    this.menuWidth = this.width / 1.5F;
    this.menuHeight = this.height / 1.25F;
    this.menuX = this.width / 2 - menuWidth / 2;
    this.menuY = this.height / 2 - menuHeight / 2;

    int teamLogoWidth = (int) (menuWidth / 2.75F);
    int teamLogoHeight = (int) (menuHeight - menuHeight / 3);

    int teamLogoX = (int) (menuX + menuWidth / 2 - teamLogoWidth - 10);
    int teamLogoY = (int) (menuY + menuHeight - teamLogoHeight - 25);

    ResourceLocation emptyTexture =
        new ResourceLocation(CraftingDead.ID, "textures/gui/team/empty.png");
    this.alphaButton =
        new SimpleImageButton(teamLogoX, teamLogoY, teamLogoWidth, teamLogoHeight, emptyTexture);
    this.omegaButton = new SimpleImageButton(teamLogoX + teamLogoWidth + 20, teamLogoY,
        teamLogoWidth, teamLogoHeight, emptyTexture);

    this.addButton(this.alphaButton);
    this.addButton(this.omegaButton);
    this.addButton(new TextButton(2,
        (int) (menuX + menuWidth - mc.fontRenderer.getStringWidth(TextFormatting.BOLD + "Spectate")
            - 5),
        (int) (menuY + menuHeight - mc.fontRenderer.FONT_HEIGHT - 5),
        TextFormatting.BOLD + "Spectate"));
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    if (!hideWorld) {
      RenderHelper.drawRectangle(0, 0, this.width, this.height, 0, 0.5F);
      RenderHelper.drawRectangle(menuX, menuY, menuWidth, menuHeight, 0, 0.5F);
    } else {
      BackgroundRenderer.drawBackground(width, height, partialTicks);
      RenderHelper.drawRectangle(0, menuY, this.width, menuHeight, 0, 0.5F);
    }

    RenderHelper.drawTextScaled(I18n.format("menu.chooseTeam"), menuX + 10, menuY + 10, 0xFFFFFF,
        false, 1.5F);

    RenderHelper.drawTextScaled(Team.ALPHA.toString(), menuX + 40, menuY + 35, 0xFFFFFF, true,
        1.25F);
    RenderHelper.drawTextScaled(Team.OMEGA.toString(), menuX + 40 + menuWidth / 2 - 30, menuY + 35,
        0xFFFFFF, true,
        1.25F);

    super.drawScreen(mouseX, mouseY, partialTicks);

    if (this.alphaButton.isMouseOver()) {
      RenderHelper.drawRectangle(this.alphaButton.x, this.alphaButton.y, this.alphaButton.width,
          this.alphaButton.height, 0, 0.5F);
    }

    if (this.omegaButton.isMouseOver()) {
      RenderHelper.drawRectangle(this.omegaButton.x, this.omegaButton.y, this.omegaButton.width,
          this.omegaButton.height, 0, 0.5F);
    }
  }

  @Override
  protected void actionPerformed(GuiButton button) {
    switch (button.id) {
      case 0:
        OpposingForces.NETWORK_WRAPPER.sendToServer(new CMessageChooseTeamResult(Team.ALPHA));
        mc.displayGuiScreen(null);
        break;
      case 1:
        OpposingForces.NETWORK_WRAPPER.sendToServer(new CMessageChooseTeamResult(Team.OMEGA));
        mc.displayGuiScreen(null);
        break;
      case 2:
        OpposingForces.NETWORK_WRAPPER.sendToServer(new CMessageChooseTeamResult(Team.SPECTATOR));
        mc.displayGuiScreen(null);
        break;
    }
  }

  @Override
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    if (this.cancellable && keyCode == 1) {
      this.mc.displayGuiScreen((GuiScreen) null);

      if (this.mc.currentScreen == null) {
        this.mc.setIngameFocus();
      }
    }
  }
}
