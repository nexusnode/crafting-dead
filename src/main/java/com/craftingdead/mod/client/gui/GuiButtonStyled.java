package com.craftingdead.mod.client.gui;

import com.craftingdead.mod.client.renderer.RenderHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiButtonStyled extends GuiButton {

	public GuiButtonStyled(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
	}

	public GuiButtonStyled(int buttonId, int x, int y, String buttonText) {
		super(buttonId, x, y, 100, 20, buttonText);
	}

	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (this.visible) {

			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
					&& mouseY < this.y + this.height;

			int buttonState = this.getHoverState(this.hovered);

			float buttonAlpha = 0;
			int buttonColour = 0;
			switch (buttonState) {
			case 1:
				buttonColour = 0xFFFFFF;
				buttonAlpha = 0.25F;
				break;
			case 2:
				buttonColour = 0x950000;
				buttonAlpha = 0.5F;
				break;
			}

			RenderHelper.drawRectangle(this.x, this.y, this.width, this.height, 0, buttonAlpha, false);

			this.drawCenteredString(mc.fontRenderer, this.displayString, this.x + this.width / 2,
					this.y + (this.height - 8) / 2, buttonColour);
		}
	}

}
