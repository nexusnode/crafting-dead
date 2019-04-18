package com.craftingdead.mod.client.gui;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.ModConfig;
import com.craftingdead.mod.capability.player.UserPlayer;
import com.craftingdead.mod.client.ClientDist;
import com.craftingdead.mod.client.crosshair.Crosshair;
import com.craftingdead.mod.client.renderer.Graphics;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiIngame {

	private static final ResourceLocation DAYS_SURVIVED = new ResourceLocation(CraftingDead.MOD_ID,
			"textures/gui/hud/days_survived.png"),
			ZOMBIES_KILLED = new ResourceLocation(CraftingDead.MOD_ID, "textures/gui/hud/zombies_killed.png"),
			PLAYERS_KILLED = new ResourceLocation(CraftingDead.MOD_ID, "textures/gui/hud/players_killed.png");

	private static final ResourceLocation BLOOD = new ResourceLocation(CraftingDead.MOD_ID, "textures/gui/blood.png"),
			BLOOD_2 = new ResourceLocation(CraftingDead.MOD_ID, "textures/gui/blood_2.png");

	private final ClientDist client;

	private Crosshair crosshair;

	private float lastSpread;

	public GuiIngame(ClientDist client, Crosshair crosshair) {
		this.client = client;
		this.crosshair = crosshair;
	}

	public void renderGameOverlay(ScaledResolution resolution, float partialTicks) {
		final int width = resolution.getScaledWidth();
		final int height = resolution.getScaledHeight();
//		final int mouseX = Mouse.getX() * width / this.client.getMinecraft().displayWidth;
//		final int mouseY = height - Mouse.getY() * height / this.client.getMinecraft().displayHeight - 1;

		UserPlayer player = client.getPlayer();
		if (player != null) {
			// Only draw in survival
			if (client.getMinecraft().playerController.shouldDrawHUD())
				this.renderPlayerStats(client.getMinecraft().fontRenderer, width, height, player.getDaysSurvived(),
						player.getZombiesKilled(), player.getPlayersKilled());

			float health = player.getEntity().getHealth();
			if (ModConfig.client.displayBlood && health <= 19) {
				float opc = 1 - health / 20;
				ResourceLocation res = health <= 6 ? BLOOD_2 : BLOOD;

				GlStateManager.enableBlend();
				{
					Graphics.bind(res);
					GlStateManager.color(1.0F, 1.0F, 1.0F, opc);
					Graphics.drawTexturedRectangle(0, 0, width, height);
				}
				GlStateManager.disableBlend();

			}
		}

	}

	private void renderPlayerStats(FontRenderer fontRenderer, int width, int height, int daysSurvived,
			int zombiesKilled, int playersKilled) {
		int y = height / 2;
		int x = 4;
		GlStateManager.enableBlend();
		{
			Graphics.bind(DAYS_SURVIVED);
			Graphics.drawTexturedRectangle(x, y - 20, 16, 16);
			fontRenderer.drawString(String.valueOf(daysSurvived), x + 20, y - 16, 0xFFFFFF, true);

			Graphics.bind(ZOMBIES_KILLED);
			Graphics.drawTexturedRectangle(x, y, 16, 16);
			fontRenderer.drawString(String.valueOf(zombiesKilled), x + 20, y + 4, 0xFFFFFF, true);

			Graphics.bind(PLAYERS_KILLED);
			Graphics.drawTexturedRectangle(x, y + 20, 16, 16);
			fontRenderer.drawString(String.valueOf(playersKilled), x + 20, y + 24, 0xFFFFFF, true);
		}
		GlStateManager.disableBlend();
	}

	public void renderCrosshairs(ScaledResolution resolution, float partialTicks) {
		final double x = resolution.getScaledWidth_double() / 2.0D;
		final double y = resolution.getScaledHeight_double() / 2.0D;

		final double imageWidth = 16.0D, imageHeight = 16.0D;

		final float spread = this.lastSpread + (this.client.getPlayer().getSpread() - this.lastSpread) * partialTicks;

		GlStateManager.pushMatrix();
		{
			GlStateManager.enableBlend();
			GlStateManager.translate(0.5D, 0.5D, 0.0D);

			Graphics.bind(this.crosshair.getMiddle());
			Graphics.drawTexturedRectangle(x - (imageWidth / 2.0D), y - (imageWidth / 2.0D), imageWidth, imageHeight);

			Graphics.bind(this.crosshair.getTop());
			Graphics.drawTexturedRectangle(x - (imageWidth / 2.0D), y - (14.0D + spread), imageWidth, imageHeight);

			Graphics.bind(this.crosshair.getBottom());
			Graphics.drawTexturedRectangle(x - (imageWidth / 2.0D), y - (2.0D - spread), imageWidth, imageHeight);

			Graphics.bind(this.crosshair.getLeft());
			Graphics.drawTexturedRectangle(x - (14.0D + spread), y - (imageWidth / 2.0D), imageWidth, imageHeight);

			Graphics.bind(this.crosshair.getRight());
			Graphics.drawTexturedRectangle(x - (2.0D - spread), y - (imageWidth / 2.0D), imageWidth, imageHeight);
			GlStateManager.disableBlend();
		}
		GlStateManager.popMatrix();

		this.lastSpread = spread;
	}

	public Crosshair getCrosshair() {
		return this.crosshair;
	}

	public void setCrosshair(Crosshair crosshair) {
		this.crosshair = crosshair;
	}

}
