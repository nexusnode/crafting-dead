package com.craftingdead.mod.client.gui;

import com.craftingdead.mod.client.renderer.RenderHelper;
import com.craftingdead.mod.common.core.CraftingDead;

import net.minecraft.util.ResourceLocation;

public class GuiMainMenu extends GuiCDScreen {

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderHelper.drawImage(0, 0, new ResourceLocation(CraftingDead.MOD_ID, "textures/gui/icons/icon_64x64.png"), width, height, 1);
    }

}
