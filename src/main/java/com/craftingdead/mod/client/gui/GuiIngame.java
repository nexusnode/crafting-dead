package com.craftingdead.mod.client.gui;

import com.craftingdead.mod.client.ModClient;
import com.craftingdead.mod.client.renderer.RenderHelper;

import net.minecraft.client.gui.ScaledResolution;

public class GuiIngame {

	private final ModClient client;

	public GuiIngame(ModClient client) {
		this.client = client;
	}

	public void renderGameOverlay(float partialTicks) {
		ScaledResolution scaledresolution = new ScaledResolution(client.getMinecraft());
		this.renderPlayerStats(scaledresolution);
	}

	private void renderPlayerStats(ScaledResolution scaledresolution) {
		RenderHelper.drawTextScaled("Days Survived: " + client.getPlayer().getDaysSurvived(), 0, 0, 0xFFFFFF, true,
				2.0F);
	}

}
