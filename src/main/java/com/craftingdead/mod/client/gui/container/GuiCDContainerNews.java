package com.craftingdead.mod.client.gui.container;

import java.util.ArrayList;

import com.craftingdead.mod.client.gui.GuiCDScreen;
import com.craftingdead.mod.util.StringListHelper;
import com.craftingdead.network.modclient.SessionModClient;

public class GuiCDContainerNews extends GuiCDContainerList {
	
	private static int delay = 0;
	private static ArrayList<String> text = new ArrayList<String>();

	public GuiCDContainerNews(int par1, int par2, int par3, int par4, int par5, GuiCDScreen par6) {
		super(par1, par2, par3, par4, par5, par6);
		this.updateDisplayedSlots(GuiCDContainerListSlotText.getListFromStrings(text));
	}
	
	public void updateScreen() {
		super.updateScreen();
		if(delay++ > 20) {
			text.clear();
			SessionModClient session = this.parentGUI.modClient.getNetHandler().getSession();
			if(session != null) {
				text.addAll(StringListHelper.convertListToLimitedWidth(session.getNews(), 210));
			} else {
				text.clear();
				text.add(StringListHelper.colorizeString("&7&m---------------------------------------"));
				text.add(StringListHelper.colorizeString("&4Not connected to the Crafting Dead Network"));
				text.add(StringListHelper.colorizeString("&7&m---------------------------------------"));
				text.add("1. Check your Internet connection.");
				text.add("2. Make sure you have a legitimate Minecraft account.");
				text.add("3. Maybe the servers are down? Check their status at the URL below.");
				text.add("4. Did you edit any of the game files?");
				text.add(StringListHelper.colorizeString("&7&m---------------------------------------"));
				text.add(StringListHelper.colorizeString("&d      &n http://www.craftingdead.com/status &r      "));
				text.add(StringListHelper.colorizeString("&7&m---------------------------------------"));
			}
			
			this.updateDisplayedSlots(GuiCDContainerListSlotText.getListFromStrings(text));
			delay = 0;
		}
	}
}
