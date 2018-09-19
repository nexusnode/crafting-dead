package com.craftingdead.mod.item;

import com.craftingdead.mod.common.core.CraftingDead;
import com.craftingdead.mod.item.gun.model.ARCModel;
import com.craftingdead.mod.item.gun.model.L85A3Model;
import com.craftingdead.mod.item.gun.model.M4A1Model;
import com.craftingdead.mod.item.gun.model.ScarHModel;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ItemManager {
    public static ItemBase arModel = new ARCModel();
    public static ItemBase l85a3Model = new L85A3Model();
    public static ItemBase m4a1Model = new M4A1Model();
    public static ItemBase scarhModel = new ScarHModel();

    public static void register(IForgeRegistry<Item> registry) {
        CraftingDead.LOGGER.info("Registering items");
        registry.register(arModel);
        registry.register(l85a3Model);
        registry.register(m4a1Model);
        registry.register(scarhModel);
    }

    public static void registerModels() {
        CraftingDead.LOGGER.info("Registering models");
        arModel.registerModel();
        l85a3Model.registerModel();
        m4a1Model.registerModel();
        scarhModel.registerModel();
    }
}
