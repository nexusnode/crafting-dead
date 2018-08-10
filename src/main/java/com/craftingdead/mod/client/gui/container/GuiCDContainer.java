package com.craftingdead.mod.client.gui.container;

import com.craftingdead.mod.client.gui.GuiCDScreen;
import com.craftingdead.mod.client.renderer.RenderHelper;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;

public class GuiCDContainer extends Gui {
	
	public int containerID;
	
	protected int posX;
	protected int posY;
	
	protected int width;
	protected int height;
	
	protected GuiCDScreen parentGUI;
	
	protected int color = 0x333333;
	public float colorOpacity = 0.5F;
	
	public GuiCDContainer(int par1, int par2, int par3, int par4, int par5, GuiCDScreen par6) {
		this.containerID = par1;
		this.posX = par2;
		this.posY = par3;
		this.width = par4;
		this.height = par5;
		this.parentGUI = par6;
	}
	
	public GuiCDContainer setColor(int color) {
		this.color = color;
		return this;
	}	
	
	public GuiCDContainer setOpacity(float opacity) {
		this.colorOpacity = opacity;
		return this;
	}

	public void initGui() {
		
	}

	public void actionPerformed(GuiButton par1GuiButton) {
		
	}
	
	public void mouseClicked(int par1, int par2, int par3) {
		
	}

	public void updateScreen() {
		
	}
	
	public void drawScreen(int par1, int par2, float par3) {
		this.drawBackground();
	}
	
	public void drawBackground() {
		RenderHelper.drawRectangleWithShadow(this.posX, this.posY, this.width, this.height, this.color, this.colorOpacity);
	}
}