package com.craftingdead.mod.util;

import java.util.ArrayList;
import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class StringListHelper {

	public static ArrayList<String> convertListToLimitedWidth(List<String> list, int par2Width) {
		return getListLimitWidth(convertListToString(new ArrayList<String>(list)), par2Width);
	}

	public static String convertListToString(ArrayList<String> par1) {
		String finalString = new String();
		for (String item : par1) {
			finalString += Character.SPACE_SEPARATOR + item.trim();
		}
		return finalString.trim();
	}

	public static ArrayList<String> getListLimitWidth(String par1, int maxWidth) {
		ArrayList<String> list = new ArrayList<String>();
		String theText = colorizeString(par1);

		String[] split = theText.split(" ");
		String line = "";
		int maxLineWidth = maxWidth;
		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;

		for (int i = 0; i < split.length; i++) {
			String var1 = split[i];

			if (var1.equals("[br]")) {
				if (fr.getStringWidth(line) > 0) {
					list.add(new String(line));
				}
				line = "";
				continue;
			}

			if (var1.equals("[nl]") || var1.equals("++")) {
				if (fr.getStringWidth(line) > 0) {
					list.add(new String(line));
				}
				list.add("");
				line = "";
				continue;
			}

			if (fr.getStringWidth(line) + fr.getStringWidth(var1) <= maxLineWidth) {
				line += var1 + " ";
			} else {
				list.add(new String(line));
				line = var1 + " ";
			}
		}

		if (fr.getStringWidth(line) > 0) {
			list.add(line);
		}

		return list;
	}

	public static String colorizeString(String string) {
		String finalString = "";
		boolean skipNext = false;

		for (int i = 0; i < string.length(); i++) {
			char character = string.charAt(i);

			if (skipNext) {
				skipNext = false;
				continue;
			}

			if (character == '&' && i != string.length()) {
				char colorCode = string.charAt(i + 1);
				boolean found = false;
				for (ChatFormatting color : ChatFormatting.values()) {
					if (color.getChar() == colorCode) {
						finalString += color;
						skipNext = true;
						found = true;
						break;
					}
				}

				if (found) {
					continue;
				}
			}

			finalString += character;
		}

		return finalString;
	}
}
