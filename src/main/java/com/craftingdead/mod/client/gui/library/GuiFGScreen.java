package com.craftingdead.mod.client.gui.library;

import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiFGScreen extends GuiScreen {
	
	protected ArrayList<GuiFGContainer> guiContainers = new ArrayList<GuiFGContainer>();

	public void initGui() {
		guiContainers.clear();
	}

	public void actionPerformed(GuiButton par1GuiButton) {
		this.addContainerAction(par1GuiButton);
	}

	public void updateScreen() {
		this.addContainerUpdate();
	}

	public void onGuiClosed() {
	}
	
	public void addContainer(GuiFGContainer par1) {
		par1.initGui();
		this.guiContainers.add(par1);
	}
	
	public void addContainerUpdate() {
		for(GuiFGContainer gui : this.guiContainers) {
			gui.updateScreen();
		}
	}
	
	public void addContainerAction(GuiButton par1GuiButton) {
		for(GuiFGContainer gui : this.guiContainers) {
			gui.actionPerformed(par1GuiButton);
		}
	}
	
	public void addContainerDrawing(int par1, int par2, float par3) {
		for(GuiFGContainer gui : this.guiContainers) {
			gui.drawScreen(par1, par2, par3);
		}
	}
	
	/** ID given on init */
	public GuiFGContainer getContainer(int par1) {
		for(GuiFGContainer cont : this.guiContainers) {
			if(cont.containerID == par1) {
				return cont;
			}
		}
		
		return null;
	}

	public boolean doesGuiPauseGame() {
		return true;
	}

	protected void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
		super.mouseClicked(mouseX, mouseY, button);
		for(GuiFGContainer gui : this.guiContainers) {
			gui.mouseClicked(mouseX, mouseY, button);
		}
	}

	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);
		this.addContainerDrawing(par1, par2, par3);
	}
}