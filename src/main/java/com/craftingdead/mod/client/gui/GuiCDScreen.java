package com.craftingdead.mod.client.gui;

import com.craftingdead.mod.client.ModClient;
import com.craftingdead.mod.client.gui.container.GuiCDContainer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;

public class GuiCDScreen extends GuiScreen {

    public ModClient modClient;

    protected ArrayList<GuiCDContainer> guiContainers = new ArrayList<GuiCDContainer>();

    @Override
    public void initGui() {
        guiContainers.clear();
    }

    @Override
    public void actionPerformed(GuiButton button) {
        for (GuiCDContainer gui : this.guiContainers) {
            gui.actionPerformed(button);
        }
    }

    @Override
    public void updateScreen() {
        for (GuiCDContainer gui : this.guiContainers) {
            gui.updateScreen();
        }
    }

    public void onGuiClosed() {
    }

    protected void addContainer(GuiCDContainer container) {
        container.initGui();
        this.guiContainers.add(container);
    }

    @Nullable
    protected GuiCDContainer getContainer(int id) {
        for (GuiCDContainer cont : this.guiContainers) {
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
        for (GuiCDContainer gui : this.guiContainers) {
            gui.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        for (GuiCDContainer gui : this.guiContainers) {
            gui.drawScreen(mouseX, mouseY, partialTicks);
        }
    }

}