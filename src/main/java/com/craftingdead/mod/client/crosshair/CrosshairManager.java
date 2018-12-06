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
	private static final float MINIMUM_SPREAD = 0.5F, MAXIMUM_SPREAD = 60.0F, MOVEMENT_SPREAD = 12.5F;

	private final ClientMod client;

	private final Map<ResourceLocation, Crosshair> loadedCrosshairs = Maps.newHashMap();

	private float spread = 0.0F, lastSpread = spread;
	private float averageSpread = 0.0F, lastAverageSpread = averageSpread;

	private Crosshair crosshair;

	public CrosshairManager(ClientMod client) {
		this.client = client;
	}

	public Crosshair getCrosshair() {
		return this.crosshair;
	}

	public void setCrosshair(ResourceLocation crosshairName) {
		this.crosshair = this.loadedCrosshairs.get(crosshairName);
	}

	public void updateAndDrawCrosshairs(ScaledResolution resolution, float partialTicks,
			CrosshairProvider crosshairProvider) {
		this.updateSpread(crosshairProvider.getDefaultSpread());

		this.lastAverageSpread = this.averageSpread;
		this.averageSpread = (this.spread + this.lastSpread) / 2.0F;
		this.lastSpread = this.spread;

		final float spread = this.crosshair.isStatic() ? 0.0F
				: this.lastAverageSpread + (this.averageSpread - this.lastAverageSpread) * partialTicks;

		final double x = resolution.getScaledWidth_double() / 2.0D;
		final double y = resolution.getScaledHeight_double() / 2.0D;

		final double width = 16.0D, height = 16.0D;

		GlStateManager.pushMatrix();
		{
			GlStateManager.enableBlend();
			GlStateManager.translate(0.5D, 0.5D, 0.0D);

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
			GlStateManager.disableBlend();
		}
		GlStateManager.popMatrix();
	}

	private void updateSpread(float defaultSpread) {
		EntityPlayerSP player = this.client.getPlayer().getEntity();
		final float originalSpread = this.spread;

		if (player.posX != player.lastTickPosX || player.posY != player.lastTickPosY
				|| player.posZ != player.lastTickPosZ) {
			float movementSpread = MOVEMENT_SPREAD;

			if (player.isSprinting())
				movementSpread *= 2F;

			if (player.isSneaking() && player.onGround)
				movementSpread /= 2F;

			if (this.spread < movementSpread)
				this.spread += 2.5F;
		}

		// If the spread hasn't changed, decrease it
		if (this.spread == originalSpread) {
			this.spread -= 2.5F;
		}

		if (this.spread < defaultSpread) {
			this.spread = defaultSpread;
		}

		if (this.spread < MINIMUM_SPREAD) {
			this.spread = MINIMUM_SPREAD;
		}

		if (this.spread > MAXIMUM_SPREAD) {
			this.spread = MAXIMUM_SPREAD;
		}
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
		if (resourcePredicate.test(ModResourceType.CROSSHAIRS)) {
			this.loadedCrosshairs.clear();
			for (String domain : resourceManager.getResourceDomains()) {
				try {
					resourceManager.getAllResources(new ResourceLocation(domain, "crosshairs.json"))
							.forEach((resource) -> {
								try (InputStream input = resource.getInputStream()) {
									Crosshair[] crosshairs = JsonUtils.fromJson(GSON,
											new InputStreamReader(input, StandardCharsets.UTF_8), Crosshair[].class);
									for (Crosshair crosshair : crosshairs) {
										this.loadedCrosshairs.put(crosshair.getName(), crosshair);
									}
								} catch (IOException e) {
									LOGGER.catching(e);
								}
							});
				} catch (IOException e) {
					;
				}
			}
			if (this.crosshair == null || !this.loadedCrosshairs.containsKey(this.crosshair.getName()))
				this.crosshair = this.loadedCrosshairs.get(DEFAULT_CROSSHAIR);
		}
	}

}
