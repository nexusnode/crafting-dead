package com.craftingdead.mod.client.gui.ingame;

import java.util.ArrayList;

import com.craftingdead.mod.client.renderer.Graphics;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class CrosshairManager {

	private static final float DEFAULT_SPREAD = 0.1F;
	private static final float MAX_SPREAD = 12.0F;

	private float spread = DEFAULT_SPREAD;
	private ArrayList<Float> spreads = new ArrayList<>();

	private float renderSpread = 0.0F;
	private float lastRenderSpread = 0.0F;

	private Crosshair crosshair;

	public void addSpread(float spread) {
		this.spread += spread;
		this.setSpreadDirty();
	}

	public void removeSpread(float spread) {
		this.spread -= spread;
		this.setSpreadDirty();
	}

	public void setSpread(float spread) {
		this.spread = spread;
		this.setSpreadDirty();
	}

	private void setSpreadDirty() {
		if (this.spread < DEFAULT_SPREAD) {
			this.spread = DEFAULT_SPREAD;
		}
		if (this.spread > MAX_SPREAD) {
			this.spread = MAX_SPREAD;
		}
		this.addAverageSpread(this.spread);
	}

	private void addAverageSpread(float spread) {
		this.spreads.add(spread);
		if (this.spreads.size() > 2) {
			this.spreads.remove(0);
		}
	}

	public float getAverageSpread() {
		float totalSpread = DEFAULT_SPREAD;
		for (Float spread : this.spreads) {
			totalSpread += spread;
		}
		return this.spreads.size() > 0 ? totalSpread / this.spreads.size() : DEFAULT_SPREAD;
	}

	public Crosshair getCrosshair() {
		return crosshair;
	}

	public void setCrosshair(Crosshair crosshair) {
		this.crosshair = crosshair;
	}

	public void renderCrossHairs(ScaledResolution resolution, float partialTicks) {
		final double x = resolution.getScaledWidth_double() / 2.0D;
		final double y = resolution.getScaledHeight_double() / 2.0D;

		final double width = 16.0D, height = 16.0D;

		this.lastRenderSpread = this.renderSpread;
		this.renderSpread = this.getAverageSpread() * 5.0F;

		final float spread = this.crosshair.isStatic() ? 0.0F
				: this.lastRenderSpread + (this.renderSpread - this.lastRenderSpread) * partialTicks;

		GlStateManager.pushMatrix();
		{
			GlStateManager.translate(-3.5D, -3.5D, 0.0D);
			GlStateManager.translate(4.0D, 4.0D, 0.0D);

			Graphics.bind(this.crosshair.getMiddle());
			Graphics.drawTexturedRectangle(x - (width / 2.0D), y - (width / 2.0D), width, height);

			Graphics.bind(this.crosshair.getTop());
			Graphics.drawTexturedRectangle(x - (width / 2.0D), y - (14.0D + spread), width, height);

			Graphics.bind(this.crosshair.getBottom());
			Graphics.drawTexturedRectangle(x - (width / 2.0D), y - (2.0D - spread), width, height);

			Graphics.bind(this.crosshair.getLeft());
			Graphics.drawTexturedRectangle(x - (14.0D + spread), y - (width / 2.0D), width, height);

			Graphics.bind(this.crosshair.getRight());
			Graphics.drawTexturedRectangle(x - (2.0D - spread), y - (width / 2.0D), width, height);
		}
		GlStateManager.popMatrix();
	}

}
