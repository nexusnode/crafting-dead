package com.craftingdead.mod.client.model;

import com.craftingdead.mod.client.ClientProxy;
import com.craftingdead.mod.client.renderer.item.gun.ArcRenderer;
import com.craftingdead.mod.common.registry.forge.ItemRegistry;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModelManager {

	private final ClientProxy client;
	private final BuiltinModelLoader builtinModelLoader;

	public ModelManager(ClientProxy client) {
		this.client = client;
		this.builtinModelLoader = new BuiltinModelLoader();
	}

	@SubscribeEvent
	public void registerModels(ModelRegistryEvent event) {
		ModelLoaderRegistry.registerLoader(builtinModelLoader);

		this.registerItemModel(ItemRegistry.ARC, new ArcRenderer(client));

		this.registerItemModel(ItemRegistry.ROAD, "normal");
		this.registerItemModel(ItemRegistry.LINED_ROAD, "normal");
		this.registerItemModel(ItemRegistry.BROKEN_LINED_ROAD, "normal");
		this.registerItemModel(ItemRegistry.BARBED_WIRE, "normal");
		this.registerItemModel(ItemRegistry.RESIDENTIAL_LOOT, "normal");
	}

	private void registerItemModel(Item item, IModel builtinModel) {
		this.builtinModelLoader.registerModel(registerItemModel(item, "normal"), builtinModel);
	}

	private ModelResourceLocation registerItemModel(Item item, String modelVariant) {
		ModelResourceLocation modelLocation = new ModelResourceLocation(item.getRegistryName(), modelVariant);
		ModelLoader.setCustomModelResourceLocation(item, 0, modelLocation);
		return modelLocation;
	}

}
