package com.craftingdead.mod.client.gui;

import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.Nullable;

import com.craftingdead.mod.client.ClientProxy;
import com.craftingdead.mod.client.gui.widget.GuiWidget;

import net.minecraft.client.gui.GuiButton;

public class GuiScreen extends net.minecraft.client.gui.GuiScreen {

	public ClientProxy client;

	protected ArrayList<GuiWidget> guiContainers = new ArrayList<GuiWidget>();

	@Override
	public void initGui() {
		guiContainers.clear();
	}

	@Override
	public void actionPerformed(GuiButton button) {
		for (GuiWidget gui : this.guiContainers) {
			gui.actionPerformed(button);
		}
	}

	@Override
	public void updateScreen() {
		for (GuiWidget gui : this.guiContainers) {
			gui.updateScreen();
		}
	}

	public void onGuiClosed() {
	}

	protected void addContainer(GuiWidget container) {
		container.initGui();
		this.guiContainers.add(container);
	}

	@Nullable
	protected GuiWidget getContainer(int id) {
		for (GuiWidget cont : this.guiContainers) {
			if (cont.containerID == id) {
				return cont;
			}
		}
		return null;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
		super.mouseClicked(mouseX, mouseY, button);
		for (GuiWidget gui : this.guiContainers) {
			gui.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		for (GuiWidget gui : this.guiContainers) {
			gui.drawScreen(mouseX, mouseY, partialTicks);
		}
	}

}