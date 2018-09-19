package com.craftingdead.mod.client.model;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

public class ModelLoader implements ICustomModelLoader {

	private Map<ResourceLocation, IModel> locationToModel = new HashMap<ResourceLocation, IModel>();

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		;
	}

	@Override
	public boolean accepts(ResourceLocation modelLocation) {
		return locationToModel.containsKey(modelLocation);
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception {
		return locationToModel.get(modelLocation);
	}

	public void registerModel(ResourceLocation modelLocation, IModel model) {
		this.locationToModel.put(modelLocation, model);
	}

}
