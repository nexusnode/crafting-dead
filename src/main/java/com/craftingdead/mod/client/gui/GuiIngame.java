package com.craftingdead.mod.client.gui;

import org.lwjgl.input.Mouse;

import com.craftingdead.mod.client.ModClient;
import com.craftingdead.mod.client.renderer.RenderHelper;
import com.craftingdead.mod.common.core.CraftingDead;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class GuiIngame {

	private static final ResourceLocation DAYS_SURVIVED = new ResourceLocation(CraftingDead.MOD_ID,
			"textures/gui/hud/daysSurvived.png");
	private static final ResourceLocation KARMA = new ResourceLocation(CraftingDead.MOD_ID,
			"textures/gui/hud/karma.png");
	private static final ResourceLocation ZOMBIE_KILLS = new ResourceLocation(CraftingDead.MOD_ID,
			"textures/gui/hud/zombieKills.png");
	private static final ResourceLocation PLAYER_KILLS = new ResourceLocation(CraftingDead.MOD_ID,
			"textures/gui/hud/playerKills.png");

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
		// Bottom of screen
		int y = height - 20;
		// Left align
		int x = 4;
		RenderHelper.drawImage(x, y, DAYS_SURVIVED, 16, 16, 1.0F);
		RenderHelper.drawTextScaled(String.valueOf(client.getPlayer().getDaysSurvived()), x + 20, y + 4, 0xFFFFFF, true,
				1F);

		RenderHelper.drawImage(x + 40, y, KARMA, 16, 16, 1.0F);
		RenderHelper.drawTextScaled("0", x + 60, y + 4, 0xFFFFFF, true, 1F);

		// Right align
		int x2 = width - 20;
		RenderHelper.drawImage(x2, y, ZOMBIE_KILLS, 16, 16, 1.0F);
		RenderHelper.drawTextScaled(String.valueOf(client.getPlayer().getZombieKills()), x2 - 10, y + 4, 0xFFFFFF, true,
				1F);

		RenderHelper.drawImage(x2 - 40, y, PLAYER_KILLS, 16, 16, 1.0F);
		RenderHelper.drawTextScaled(String.valueOf(client.getPlayer().getPlayerKills()), x2 - 50, y + 4, 0xFFFFFF, true,
				1.0D);
	}

}
