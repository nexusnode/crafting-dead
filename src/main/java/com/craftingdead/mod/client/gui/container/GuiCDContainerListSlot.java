package com.craftingdead.mod.client.gui.container;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import net.minecraft.client.gui.Gui;

public class GuiCDContainerListSlot extends Gui {
	
	public int posX;
	public int posY;
	
	/** Update tick */
	public void onUpdate() {
		
	}
	
	/** Called when a slot is clicked twice */
	public void onDoubleClick() {
		
	}
	
	/** Called to see if the slot can be selected or not */
	public boolean canSelect() {
		
		return true;
	}
	
	/** Render tick */
	public void doRender(int x, int y, boolean selected, int width, int height) {
		
	}
	
    protected void openURL(String par1) {
		try {
			if (Desktop.isDesktopSupported()) {
				try {
					Desktop.getDesktop().browse(new URI(par1));
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
