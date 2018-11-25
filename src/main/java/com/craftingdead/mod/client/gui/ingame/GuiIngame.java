package com.craftingdead.mod.client.gui.ingame;

import org.lwjgl.input.Mouse;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.client.ClientMod;
import com.craftingdead.mod.client.renderer.Graphics;
import com.craftingdead.mod.item.ExtendedItem;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiIngame {

	private static final ResourceLocation DAYS_SURVIVED = new ResourceLocation(CraftingDead.MOD_ID,
			"textures/gui/hud/days_survived.png");
	private static final ResourceLocation ZOMBIE_KILLS = new ResourceLocation(CraftingDead.MOD_ID,
			"textures/gui/hud/zombie_kills.png");
	private static final ResourceLocation PLAYER_KILLS = new ResourceLocation(CraftingDead.MOD_ID,
			"textures/gui/hud/player_kills.png");

	private final ClientMod client;

	private final CrosshairManager crosshairManager = new CrosshairManager();

	public GuiIngame(ClientMod client) {
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

		Graphics.bind(DAYS_SURVIVED);
		Graphics.drawTexturedRectangle(x, y - 20, 16, 16);
		fontRenderer.drawString(String.valueOf(client.getPlayer().getDaysSurvived()), x + 20, y - 16, 0xFFFFFF, true);

		Graphics.bind(ZOMBIE_KILLS);
		Graphics.drawTexturedRectangle(x, y, 16, 16);
		fontRenderer.drawString(String.valueOf(client.getPlayer().getZombieKills()), x + 20, y + 4, 0xFFFFFF, true);

		Graphics.bind(PLAYER_KILLS);
		Graphics.drawTexturedRectangle(x, y + 20, 16, 16);
		fontRenderer.drawString(String.valueOf(client.getPlayer().getPlayerKills()), x + 20, y + 24, 0xFFFFFF, true);
	}

	public CrosshairManager getCrosshairManager() {
		return this.crosshairManager;
	}

	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
		switch (event.getType()) {
		case ALL:
			this.renderGameOverlay(event.getResolution(), event.getPartialTicks());
			break;
		case CROSSHAIRS:
			if (this.useCustomCrosshair()) {
				event.setCanceled(true);
				this.crosshairManager.renderCrossHairs(event.getResolution(), event.getPartialTicks());
			}
			break;
		default:
			break;
		}
	}

	private boolean useCustomCrosshair() {
		ItemStack heldStack = this.client.getPlayer().getEntity().getHeldItemMainhand();
		if (heldStack.getItem() instanceof ExtendedItem) {
			ExtendedItem item = (ExtendedItem) heldStack.getItem();
			return item.hasCustomCrosshair();
		}
		return false;
	}

}
