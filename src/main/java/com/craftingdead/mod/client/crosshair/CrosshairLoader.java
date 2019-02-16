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
import com.craftingdead.mod.init.ModResourceType;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;

public class CrosshairLoader implements ISelectiveResourceReloadListener {

	private static final Gson GSON = new GsonBuilder()
			.registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer()).create();

	private static final Logger LOGGER = LogManager.getLogger();

	private static final ResourceLocation DEFAULT_CROSSHAIR = new ResourceLocation(CraftingDead.MOD_ID, "default");

	private final Map<ResourceLocation, Crosshair> loadedCrosshairs = Maps.newHashMap();

	public Crosshair getCrosshair(ResourceLocation crosshairLocation) {
		Crosshair crosshair = this.loadedCrosshairs.get(crosshairLocation);
		return crosshair != null ? crosshair : this.loadedCrosshairs.get(DEFAULT_CROSSHAIR);
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
		}
	}

}
