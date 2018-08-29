package com.craftingdead.mod.client.gui.container;

import com.craftingdead.mod.client.gui.GuiCDScreen;
import com.craftingdead.mod.client.renderer.RenderHelper;
import net.minecraft.client.Minecraft;

public class GuiCDContainerText extends GuiCDContainer {

    private String[] text;
    private boolean centerText = false;

    public GuiCDContainerText(int par1, int par2, int par3, int par4, int par5, GuiCDScreen par6) {
        super(par1, par2, par3, par4, par5, par6);
    }

    public GuiCDContainerText setText(String... par1) {
        this.text = par1;
        return this;
    }

    public GuiCDContainerText setCentered() {
        this.centerText = true;
        return this;
    }

    public void drawScreen(int par1, int par2, float par3) {
        this.drawBackground();

        for (int i = 0; i < text.length; i++) {

            String var1 = text[i];

            if (centerText) {
                RenderHelper.drawCenteredString(Minecraft.getMinecraft().fontRenderer, var1, this.posX + this.width / 2, this.posY + 3 + (10 * i), 0xFFFFFF);
                continue;
            }

            this.drawString(Minecraft.getMinecraft().fontRenderer, var1, this.posX + 3, this.posY + 3 + (10 * i), 0xFFFFFF);
        }
    }
}
