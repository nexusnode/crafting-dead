package com.craftingdead.mod.client.crosshair;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.client.ClientMod;
import com.craftingdead.mod.client.renderer.Graphics;
import com.craftingdead.mod.init.ModResourceType;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;

public class CrosshairManager implements ISelectiveResourceReloadListener {

	private static final Gson GSON = new GsonBuilder()
			.registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer()).create();

	private static final Logger LOGGER = LogManager.getLogger();

	private static final ResourceLocation DEFAULT_CROSSHAIR = new ResourceLocation(CraftingDead.MOD_ID, "default");
	private static final float MINIMUM_SPREAD = 0.1F, MAX_SPREAD = 12.0F;

	private final ClientMod client;

	private final Map<ResourceLocation, Crosshair> crosshairs = Maps.newHashMap();

	private float spread = MINIMUM_SPREAD;
	private float lastSpread = spread;

	private float renderSpread = 0.0F;
	private float lastRenderSpread = 0.0F;

	private Crosshair selectedCrosshair;

	public CrosshairManager(ClientMod client) {
		this.client = client;
	}

	public void setSpread(float spread) {
		this.lastSpread = this.spread;
		this.spread = spread;
		if (this.spread < MINIMUM_SPREAD) {
			this.spread = MINIMUM_SPREAD;
		}
		if (this.spread > MAX_SPREAD) {
			this.spread = MAX_SPREAD;
		}
	}

	public float getAverageSpread() {
		return (this.spread + this.lastSpread) / 2;
	}

	public Crosshair getCrosshair() {
		return this.selectedCrosshair;
	}

	public void setCrosshair(ResourceLocation crosshairName) {
		this.selectedCrosshair = this.crosshairs.get(crosshairName);
	}

	public void renderCrossHairs(ScaledResolution resolution, float partialTicks, CrosshairProvider crosshairProvider) {
		this.updateCrosshairSpread(crosshairProvider);

		final double x = resolution.getScaledWidth_double() / 2.0D;
		final double y = resolution.getScaledHeight_double() / 2.0D;

		final double width = 16.0D, height = 16.0D;

		this.lastRenderSpread = this.renderSpread;
		this.renderSpread = this.getAverageSpread() * 5.0F;

		final float spread = this.selectedCrosshair.isStatic() ? 0.0F
				: this.lastRenderSpread + (this.renderSpread - this.lastRenderSpread) * partialTicks;

		GlStateManager.pushMatrix();
		{
			GlStateManager.translate(-3.5D, -3.5D, 0.0D);
			GlStateManager.translate(4.0D, 4.0D, 0.0D);

			Graphics.bind(this.selectedCrosshair.getMiddle());
			Graphics.drawTexturedRectangle(x - (width / 2.0D), y - (width / 2.0D), width, height);

			Graphics.bind(this.selectedCrosshair.getTop());
			Graphics.drawTexturedRectangle(x - (width / 2.0D), y - (14.0D + spread), width, height);

			Graphics.bind(this.selectedCrosshair.getBottom());
			Graphics.drawTexturedRectangle(x - (width / 2.0D), y - (2.0D - spread), width, height);

			Graphics.bind(this.selectedCrosshair.getLeft());
			Graphics.drawTexturedRectangle(x - (14.0D + spread), y - (width / 2.0D), width, height);

			Graphics.bind(this.selectedCrosshair.getRight());
			Graphics.drawTexturedRectangle(x - (2.0D - spread), y - (width / 2.0D), width, height);
		}
		GlStateManager.popMatrix();
	}

	private void updateCrosshairSpread(CrosshairProvider crosshairProvider) {
		EntityPlayerSP player = this.client.getPlayer().getEntity();
		float originalSpread = this.spread;

		if (player.posX != player.lastTickPosX || player.posY != player.lastTickPosY
				|| player.posZ != player.lastTickPosZ) {
			float movementSpread = crosshairProvider.getMovementSpread();

			if (player.isSprinting())
				movementSpread *= 2F;

			if (player.isSneaking())
				movementSpread /= 2F;

			if (this.spread < movementSpread) {
				this.setSpread(this.spread + 0.5F);
			}
		}

		// If the spread hasn't changed, decrease it
		if (this.spread == originalSpread) {
			this.setSpread(this.spread - 0.5F);
		}
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
		if (resourcePredicate.test(ModResourceType.CROSSHAIRS)) {
			this.crosshairs.clear();
			for (String domain : resourceManager.getResourceDomains()) {
				try {
					resourceManager.getAllResources(new ResourceLocation(domain, "crosshairs.json"))
							.forEach((resource) -> {
								try (InputStream input = resource.getInputStream()) {
									Crosshair[] crosshairs = JsonUtils.fromJson(GSON,
											new InputStreamReader(input, StandardCharsets.UTF_8), Crosshair[].class);
									for (Crosshair crosshair : crosshairs) {
										this.crosshairs.put(crosshair.getName(), crosshair);
									}
								} catch (IOException e) {
									LOGGER.catching(e);
								}
							});
				} catch (IOException e) {
					;
				}
			}
			if (this.selectedCrosshair == null || !this.crosshairs.containsKey(this.selectedCrosshair.getName()))
				this.selectedCrosshair = this.crosshairs.get(DEFAULT_CROSSHAIR);
		}
	}

}
