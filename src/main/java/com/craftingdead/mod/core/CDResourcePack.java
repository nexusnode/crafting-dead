package com.craftingdead.mod.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ModContainer;

public class CDResourcePack implements IResourcePack {

	protected ModContainer ownerContainer;

	public CDResourcePack(ModContainer container) {
		this.ownerContainer = container;
	}

	@Override
	public InputStream getInputStream(ResourceLocation location) throws IOException {
		return getResourceStream(location);
	}

	@Override
	public boolean resourceExists(ResourceLocation location) {
		return getResourceStream(location) != null;
	}

	@Override
	public Set<String> getResourceDomains() {
		return ImmutableSet.of(ownerContainer.getModId());
	}

	@Override
	public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer,
			String metadataSectionName) throws IOException {
		return null;
	}

	@Override
	public BufferedImage getPackImage() throws IOException {
		return null;
	}

	@Override
	public String getPackName() {
		return ownerContainer.getName();
	}

	private static InputStream getResourceStream(ResourceLocation resourceLocation) {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		return classloader.getResourceAsStream("assets" + File.separator + resourceLocation.getNamespace()
				+ File.separator + resourceLocation.getPath());
	}

}