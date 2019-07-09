package com.craftingdead.mod.client.model;

public class ModelRegistry {
  //
  // public static void registerModels(ClientDist client) {
  // ModelLoaderRegistry.registerLoader(BuiltinModelLoader.INSTANCE);
  //
  // registerItemModel(ModItems.ACR, new GunRenderer(client, new
  // ResourceLocation(CraftingDead.MOD_ID, "item/acr")));
  // registerItemModel(ModItems.AK47,
  // new GunRenderer(client, new ResourceLocation(CraftingDead.MOD_ID, "item/ak47")));
  // registerItemModel(ModItems.DESERT_EAGLE,
  // new GunRenderer(client, new ResourceLocation(CraftingDead.MOD_ID, "item/desert_eagle")));
  // registerItemModel(ModItems.M4A1,
  // new GunRenderer(client, new ResourceLocation(CraftingDead.MOD_ID, "item/m4a1")));
  // registerItemModel(ModItems.M9, new GunRenderer(client, new
  // ResourceLocation(CraftingDead.MOD_ID, "item/m9")));
  // registerItemModel(ModItems.TASER,
  // new GunRenderer(client, new ResourceLocation(CraftingDead.MOD_ID, "item/taser")));
  // registerItemModel(ModItems.MAGNUM,
  // new GunRenderer(client, new ResourceLocation(CraftingDead.MOD_ID, "item/44_magnum")));
  // registerItemModel(ModItems.FN57,
  // new GunRenderer(client, new ResourceLocation(CraftingDead.MOD_ID, "item/fn57")));
  //
  // registerItemModel(ModItems.RESIDENTIAL_LOOT, "normal");
  // }
  //
  // private static void registerItemModel(Item item, IUnbakedModel builtinModel) {
  // BuiltinModelLoader.INSTANCE.registerModel(registerItemModel(item, "inventory"), builtinModel);
  // }
  //
  // private static ModelResourceLocation registerItemModel(Item item, String modelVariant) {
  // ModelResourceLocation modelLocation = new ModelResourceLocation(item.getRegistryName(),
  // modelVariant);
  // // TODO ModelLoader.setCustomModelResourceLocation(item, 0, modelLocation);
  // return modelLocation;
  // }
  //
  // public static enum BuiltinModelLoader implements ICustomModelLoader {
  //
  // INSTANCE;
  //
  // private final Map<ResourceLocation, IUnbakedModel> locationToModel = new
  // HashMap<ResourceLocation, IUnbakedModel>();
  //
  // @Override
  // public void onResourceManagerReload(IResourceManager resourceManager) {
  // ;
  // }
  //
  // @Override
  // public boolean accepts(ResourceLocation modelLocation) {
  // return this.locationToModel.containsKey(modelLocation);
  // }
  //
  // @Override
  // public IUnbakedModel loadModel(ResourceLocation modelLocation) throws Exception {
  // return this.locationToModel.get(modelLocation);
  // }
  //
  // public void registerModel(ResourceLocation modelLocation, IUnbakedModel model) {
  // this.locationToModel.put(modelLocation, model);
  // }
  // }
}
