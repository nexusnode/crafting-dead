package com.craftingdead.mod.client.gui.widget;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;

public class GuiWidgetListSlotText extends GuiWidgetListSlot {

    private String displayText;

    public GuiWidgetListSlotText(String par1) {
        this.displayText = par1;
    }

    public boolean canSelect() {

        if (this.displayText == null || this.displayText.equals("")) {

            return false;
        }

        return true;
    }

    /**
     * Called when a slot is clicked twice
     */
    public void onDoubleClick() {

        String[] split = displayText.split(" ");

        if (split.length >= 1) {

            for (String var1 : split) {

                if (var1.contains(".") && (var1.contains(".com") || var1.contains(".net") || var1.contains("https://") || var1.startsWith("http://"))) {

                    this.openURL(var1);
                }
            }
        }
    }

    public void doRender(int x, int y, boolean selected, int width, int height) {
        this.drawString(Minecraft.getMinecraft().fontRenderer, (selected ? ChatFormatting.GRAY : ChatFormatting.WHITE) + displayText, x + 2, y, 0);
    }

    /**
     * Convert a list of strings to a list of slots
     */
    public static ArrayList<GuiWidgetListSlot> getListFromStrings(ArrayList<String> par1) {

        ArrayList<GuiWidgetListSlot> list = new ArrayList<GuiWidgetListSlot>();

        for (String var1 : par1) {
            GuiWidgetListSlot slot = new GuiWidgetListSlotText(var1);
            list.add(slot);
        }

        return list;
    }
}
