package com.craftingdead.mod.client.gui.main;

import com.craftingdead.mod.client.gui.CDMenu;
import net.minecraft.client.gui.GuiButton;

public class GuiMainMenu extends CDMenu {

    @Override
    public void initGui() {
        addCustomButton(new GuiButton(1, (width / 2), (height / 2), 10, 10, "Click me for dicks"), button -> {

        });
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
