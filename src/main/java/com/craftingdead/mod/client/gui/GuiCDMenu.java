package com.craftingdead.mod.client.gui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import org.lwjgl.opengl.GL11;

import com.craftingdead.mod.client.gui.library.GuiFGScreen;
import com.craftingdead.mod.client.renderer.RenderHelper;
import com.craftingdead.mod.core.CraftingDead;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class GuiCDMenu extends GuiFGScreen {
	
	protected static final ResourceLocation BACKGROUND = null;
	protected static final ResourceLocation ICON_HOME = new ResourceLocation(CraftingDead.MOD_ID, "textures/gui/home.png");
	protected static final ResourceLocation ICON_SETTINGS = new ResourceLocation(CraftingDead.MOD_ID, "textures/gui/settings.png");
	protected static final ResourceLocation ICON_POWER = new ResourceLocation(CraftingDead.MOD_ID, "textures/gui/power.png");
	
	ResourceLocation smoke1 = new ResourceLocation(CraftingDead.MOD_ID, "textures/gui/smoke.png");
	ResourceLocation smoke2 = new ResourceLocation(CraftingDead.MOD_ID, "textures/gui/smoke.png");
	private double smokeX = 0;
	private double lastSmokeX;
	
	public void initGui() {
		super.initGui();
	}

	public void actionPerformed(GuiButton par1GuiButton) {
		super.actionPerformed(par1GuiButton);
	}

	public void updateScreen() {
		super.updateScreen();
		
		if(this.smokeX > -this.width) {
			this.lastSmokeX = this.smokeX;
			this.smokeX -= 0.3D;
		} else {
			this.lastSmokeX = 0;
			this.smokeX = 0;
		}
	}
	
	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);
	}
	
	public void addOverheadMenuActionPerformed(GuiButton par1GuiButton) {}
	
	public void drawIntendedBackground(float par1, boolean par2) {
		if (this.mc != null && this.mc.world != null) {
			RenderHelper.drawRectangle(0, 0, this.width, this.height, 0, 0.5F);
		} else {
			if(BACKGROUND == null) {
				BACKGROUND = new ResourceLocation(CraftingDead.MOD_ID, "textures/gui/background_21.png");
			}
			
			GL11.glPushMatrix();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			RenderHelper.drawImage(0, 0, this.BACKGROUND, this.width, this.height, 1);
			
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.01F);
			float opacity = 0.2f;
			double smoothSmokeX = this.lastSmokeX + (this.smokeX - this.lastSmokeX) * par1;
			GL11.glColor4f(1.0F, 1.0F, 1.0F, opacity);
			CDRenderHelper.drawImage(smoothSmokeX, 0, smoke1, this.width, this.height);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, opacity);
			CDRenderHelper.drawImage(smoothSmokeX + this.width, 0, smoke2, this.width, this.height);
			GL11.glPopMatrix();
			
			if(par2) {
				//Draw menu bar
				int column = this.width / 12;
				CDRenderHelper.drawRect(column, 0, column * 2, this.height, "0x000000", 0.2F);
			}
		}
	}
	
	public void drawIntendedBackground(float par1) {
		drawIntendedBackground(par1, true);
	}
	
	public void drawBackground(float par1) {
		drawIntendedBackground(par1);
	}
	
	public void DrawLogoAndCompany(float par1) {
		int column = this.width / 12;
		int row = this.height / 12;
		//Draw logo and version
		ResourceLocation iconCD = new ResourceLocation(CDOrigins.resLocation + ":textures/gui/logodark.png");
		CDRenderHelper.drawImage(column * 7 - (column * 7 / 2), row - 23, iconCD, column * 7, column * 3);
		CDRenderHelper.renderCenteredText(EnumChatFormatting.GRAY + "Crafting Dead Origins " + CDOrigins.MOD_VERSION + " (C) Ferullo Gaming", column * 7, this.height - 10);
	}
	
	public void drawButtons(int par1, int par2, float par3) {
		for (int k = 0; k < this.buttonList.size(); ++k) {
			GuiButton guibutton = (GuiButton) this.buttonList.get(k);
			guibutton.drawButton(this.mc, par1, par2);
		}
	}
	
	public void initOverheadMenu() {
		int x = (this.width / 2) - 173;
		int y = 1;
		int buttonWidth = 85;
		
		this.buttonList.add(new GuiFGButton(1, x + 1, y, 30, 20, ICON_HOME));
		this.buttonList.add(new GuiFGButton(2, x + 32, y, buttonWidth, 20, EnumChatFormatting.BOLD + "PLAY"));
		this.buttonList.add(new GuiFGButton(3, x + 33 + buttonWidth, y, buttonWidth, 20, EnumChatFormatting.BOLD + "FACTION"));
		this.buttonList.add(new GuiFGButton(4, x + 34 + (2 * buttonWidth), y, buttonWidth, 20, EnumChatFormatting.BOLD + "COMMUNITY"));
		this.buttonList.add(new GuiFGButton(5, x + 35 + (3 * buttonWidth), y, 30, 20, ICON_SETTINGS));
		this.buttonList.add(new GuiFGButton(6, x + 66 + (3 * buttonWidth), y, 30, 20, ICON_POWER));
	}
	
	/** Opens a URL in the default web browser */
    public static void openURL(String par1) {
    	if(par1.startsWith("http://") == false && par1.startsWith("https://") == false) {
    		par1 = "http://" + par1;
    	}
    	
		try {
			if (Desktop.isDesktopSupported()) {
				try {
					Desktop.getDesktop().browse(new URI(par1));
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
