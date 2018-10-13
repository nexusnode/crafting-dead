package com.craftingdead.mod.client.gui.widget;

import java.util.ArrayList;

import com.craftingdead.mod.client.gui.GuiScreen;
import com.craftingdead.mod.util.ListUtil;

public class GuiWidgetNews extends GuiWidgetList {

	private static int delay = 0;
	private static ArrayList<String> text = new ArrayList<String>();

	public GuiWidgetNews(int par1, int par2, int par3, int par4, int par5, GuiScreen par6) {
		super(par1, par2, par3, par4, par5, par6);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		if (delay++ > 20) {
			text.clear();
			text.addAll(ListUtil.convertListToLimitedWidth(this.parentGUI.client.getNews(), 210));
			this.updateDisplayedSlots(GuiWidgetListSlotText.getListFromStrings(text));
			delay = 0;
		}
	}
}
