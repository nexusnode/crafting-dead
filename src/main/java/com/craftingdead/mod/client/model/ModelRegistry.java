package com.craftingdead.mod.client.model;

import java.util.HashMap;
import java.util.Map;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.client.ClientDist;
import com.craftingdead.mod.client.renderer.item.GunRenderer;
import com.craftingdead.mod.init.ModItems;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public class ModelRegistry {

	public static void registerModels(ClientDist client) {
		ModelLoaderRegistry.registerLoader(BuiltinModelLoader.INSTANCE);

		registerItemModel(ModItems.ACR, new GunRenderer(client, new ResourceLocation(CraftingDead.MOD_ID, "item/acr")));
		registerItemModel(ModItems.AK47,
				new GunRenderer(client, new ResourceLocation(CraftingDead.MOD_ID, "item/ak47")));
		registerItemModel(ModItems.DESERT_EAGLE,
				new GunRenderer(client, new ResourceLocation(CraftingDead.MOD_ID, "item/desert_eagle")));
		registerItemModel(ModItems.M4A1,
				new GunRenderer(client, new ResourceLocation(CraftingDead.MOD_ID, "item/m4a1")));
		registerItemModel(ModItems.M9, new GunRenderer(client, new ResourceLocation(CraftingDead.MOD_ID, "item/m9")));
		registerItemModel(ModItems.TASER,
				new GunRenderer(client, new ResourceLocation(CraftingDead.MOD_ID, "item/taser")));
		registerItemModel(ModItems.MAGNUM,
				new GunRenderer(client, new ResourceLocation(CraftingDead.MOD_ID, "item/44_magnum")));
		registerItemModel(ModItems.FN57,
				new GunRenderer(client, new ResourceLocation(CraftingDead.MOD_ID, "item/fn57")));

		registerItemModel(ModItems.RESIDENTIAL_LOOT, "normal");
	}

	private static void registerItemModel(Item item, IModel builtinModel) {
		BuiltinModelLoader.INSTANCE.registerModel(registerItemModel(item, "normal"), builtinModel);
	}

	private static ModelResourceLocation registerItemModel(Item item, String modelVariant) {
		ModelResourceLocation modelLocation = new ModelResourceLocation(item.getRegistryName(), modelVariant);
		ModelLoader.setCustomModelResourceLocation(item, 0, modelLocation);
		return modelLocation;
	}

	public static enum BuiltinModelLoader implements ICustomModelLoader {

		INSTANCE;

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
			locationToModel.put(modelLocation, model);
		}

	}

}
