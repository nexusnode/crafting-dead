package com.craftingdead.mod.client.gui.library;

import org.lwjgl.opengl.GL11;

import com.craftingdead.mod.client.renderer.RenderHelper;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiFGButton extends GuiButton {

	public int isOver = 2;
	private ResourceLocation iconTexture = null;

	public ItemStack renderStack = null;
	public String hoverText = "";

	public boolean drawBackground = true;
	public boolean drawShadow = true;
	public int buttonColor = 0x971a1a;
	public float buttonOpacity = 1.0F;
	
	/** 0 = left, 1 = centered, 2 = right */
	public int textAlignment = 1;

	public GuiFGButton(int par1, int par2, int par3, int par4, int par5, String par6Str) {
		super(par1, par2, par3, par4, par5, par6Str);
	}

	public GuiFGButton(int par1, int par2, int par3, ItemStack par4Stack) {
		super(par1, par2, par3, 18, 18, "");
		this.renderStack = par4Stack;
	}

	public GuiFGButton(int par1, int par2, int par3, int par4, int par5, ResourceLocation par6) {
		super(par1, par2, par3, par4, par5, "");
		this.iconTexture = par6;
	}

	public GuiFGButton setCenter(boolean par1) {
		this.textAlignment = 1;
		return this;
	}

	public GuiFGButton setColor(int color) {
		this.buttonColor = color;
		return this;
	}

	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (this.visible) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			this.isOver = this.getHoverState(this.hovered);

			if (this.drawBackground) {
				if (this.drawShadow) {
					RenderHelper.drawRectangleWithShadow(this.x, this.y, this.width, this.height, buttonColor, buttonOpacity);
				} else {
					RenderHelper.drawRectangle(this.x, this.y, this.width, this.height, buttonColor, buttonOpacity);
				}
			}

			this.mouseDragged(mc, mouseX, mouseY);
			String displayText = this.displayString;

			if (this.iconTexture != null) {
				if (this.isOver == 2) {
					RenderHelper.renderColor(0x808080);
				}

				RenderHelper.drawImage(this.x + this.width / 2 - 8, this.y + (this.height - 8) - 10, iconTexture, 16, 16);
				GL11.glColor4f(1, 1, 1, 1);
				return;
			}

			if (renderStack != null) {
				RenderHelper.drawItemStack(this.renderStack, this.x, this.y + 1);
				return;
			}

			if (this.enabled == false) {
				displayText = ChatFormatting.GRAY + displayText;
			}
			
			if(textAlignment == 1) {
				RenderHelper.renderCenteredText(displayText, this.x + this.width / 2, this.y + (this.height - 8) / 2, (isOver == 2 ? 0xAB1313 : 0xffffff));
				GL11.glColor4f(1, 1, 1, 1);
				return;
			}
			
			if(textAlignment == 2) {
				RenderHelper.renderTextRight(displayText, this.x + this.width, this.y + (this.height - 8) / 2, true, (isOver == 2 ? 0xAB1313 : 0xffffff));
				GL11.glColor4f(1, 1, 1, 1);
				return;
			}
			
			mc.fontRenderer.drawString(displayText, this.x + 4, this.y + (this.height - 8) / 2, (isOver == 2 ? 0xAB1313 : 0xffffff));
			GL11.glColor4f(1, 1, 1, 1);
		}
	}
}
