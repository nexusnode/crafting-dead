package com.craftingdead.mod.client.gui.main;

import com.craftingdead.mod.client.gui.CDMenu;
import net.minecraft.client.gui.GuiButton;

public class GuiMainMenu extends CDMenu {

    @Override
    public void initGui() {

        // Custom buttons
        addCustomButton(new GuiButton(1, (width / 2), (height / 2), 10, 10, "Click me for dicks"), button -> {

        });
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        // TODO enter draw code here

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
