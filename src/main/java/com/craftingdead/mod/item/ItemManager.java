package com.craftingdead.mod.item;

import com.craftingdead.mod.common.core.CraftingDead;
import com.craftingdead.mod.item.gun.ARCModel;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ItemManager {
    public static ItemBase ARCModel = new ARCModel();

    public static void register(IForgeRegistry<Item> registry) {
        CraftingDead.LOGGER.info("Registering items");
        registry.register(ARCModel);
    }

    public static void registerModels() {
        CraftingDead.LOGGER.info("Registering models");
        ARCModel.registerModel();
    }


}
