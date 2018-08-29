package com.craftingdead.mod.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.util.HashMap;
import java.util.Map;

public class CDMenu extends GuiScreen {

    private Map<GuiButton, ButtonCallback> customButtons;

    public CDMenu() {
        customButtons = new HashMap<>();
    }

    public void addCustomButton(GuiButton button, ButtonCallback callback) {
        buttonList.add(button);
        customButtons.putIfAbsent(button, callback);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        for (Map.Entry<GuiButton, ButtonCallback> entry : customButtons.entrySet()) {
            if (button.id == entry.getKey().id) {
                entry.getValue().onClick(button);
            }
        }
    }
}
