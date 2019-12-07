package com.craftingdead.mod.client.model.builtin;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;

public enum BuiltinModelLoader implements ICustomModelLoader {

  INSTANCE;

  private final Map<ResourceLocation, IUnbakedModel> models = new HashMap<>();

  public void registerModel(ResourceLocation modelLocation, IUnbakedModel model) {
    this.models.put(modelLocation, model);
  }

  @Override
  public void onResourceManagerReload(IResourceManager resourceManager) {}

  @Override
  public boolean accepts(ResourceLocation modelLocation) {
    return this.models.containsKey(modelLocation);
  }

  @Override
  public IUnbakedModel loadModel(ResourceLocation modelLocation) throws Exception {
    return this.models.get(modelLocation);
  }
}
