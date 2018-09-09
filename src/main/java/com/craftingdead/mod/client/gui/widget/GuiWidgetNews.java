package com.craftingdead.mod.client.gui.widget;

import com.craftingdead.mod.client.gui.GuiScreen;
import com.craftingdead.mod.client.network.SessionModClient;
import com.craftingdead.mod.util.ListUtil;

import java.util.ArrayList;

public class GuiWidgetNews extends GuiWidgetList {

    private static int delay = 0;
    private static ArrayList<String> text = new ArrayList<String>();

    public GuiWidgetNews(int par1, int par2, int par3, int par4, int par5, GuiScreen par6) {
        super(par1, par2, par3, par4, par5, par6);
        this.updateDisplayedSlots(GuiWidgetListSlotText.getListFromStrings(text));
    }

    public void updateScreen() {
        super.updateScreen();
        if (delay++ > 20) {
            text.clear();
            SessionModClient session = this.parentGUI.modClient.getNetHandler().getSession();
            if (session != null) {
                text.addAll(ListUtil.convertListToLimitedWidth(session.getNews(), 210));
            } else {
                text.clear();
                text.add(ListUtil.colorizeString("&7&m---------------------------------------"));
                text.add(ListUtil.colorizeString("&4Not connected to the Crafting Dead Network"));
                text.add(ListUtil.colorizeString("&7&m---------------------------------------"));
                text.add("1. Check your Internet connection.");
                text.add("2. Make sure you have a legitimate Minecraft account.");
                text.add("3. Maybe the servers are down? Check their status at the URL below.");
                text.add("4. Did you edit any of the game files?");
                text.add(ListUtil.colorizeString("&7&m---------------------------------------"));
                text.add(ListUtil.colorizeString("&d      &n http://www.craftingdead.com/status &r      "));
                text.add(ListUtil.colorizeString("&7&m---------------------------------------"));
            }

            this.updateDisplayedSlots(GuiWidgetListSlotText.getListFromStrings(text));
            delay = 0;
        }
    }
}
