package com.craftingdead.mod.client.gui;

import org.lwjgl.input.Mouse;

import com.craftingdead.mod.client.ModClient;
import com.craftingdead.mod.client.renderer.RenderHelper;
import com.craftingdead.mod.common.CraftingDead;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class GuiIngame {

	private static final ResourceLocation DAYS_SURVIVED = new ResourceLocation(CraftingDead.MOD_ID,
			"textures/gui/hud/days_survived.png");
	private static final ResourceLocation ZOMBIE_KILLS = new ResourceLocation(CraftingDead.MOD_ID,
			"textures/gui/hud/zombie_kills.png");
	private static final ResourceLocation PLAYER_KILLS = new ResourceLocation(CraftingDead.MOD_ID,
			"textures/gui/hud/player_kills.png");

	private final ModClient client;

	public GuiIngame(ModClient client) {
		this.client = client;
	}

	public void renderGameOverlay(ScaledResolution resolution, float partialTicks) {
		int width = resolution.getScaledWidth();
		int height = resolution.getScaledHeight();
		final int mouseX = Mouse.getX() * width / this.client.getMinecraft().displayWidth;
		final int mouseY = height - Mouse.getY() * height / this.client.getMinecraft().displayHeight - 1;
		// Only draw in survival
		if (client.getMinecraft().playerController.shouldDrawHUD() && client.getPlayer() != null)
			this.renderPlayerStats(width, height, mouseX, mouseY);
	}

	private void renderPlayerStats(int width, int height, int mouseX, int mouseY) {
		int y = height / 2;
		int x = 4;
		FontRenderer fontRenderer = client.getMinecraft().fontRenderer;

		RenderHelper.drawImage(x, y - 20, DAYS_SURVIVED, 16, 16, 1.0F);
		fontRenderer.drawString(String.valueOf(client.getPlayer().getDaysSurvived()), x + 20, y - 16, 0xFFFFFF, true);

		RenderHelper.drawImage(x, y, ZOMBIE_KILLS, 16, 16, 1.0F);
		fontRenderer.drawString(String.valueOf(client.getPlayer().getZombieKills()), x + 20, y + 4, 0xFFFFFF, true);

		RenderHelper.drawImage(x, y + 20, PLAYER_KILLS, 16, 16, 1.0F);
		fontRenderer.drawString(String.valueOf(client.getPlayer().getPlayerKills()), x + 20, y + 24, 0xFFFFFF, true);

	}

}
