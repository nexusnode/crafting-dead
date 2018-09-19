package com.craftingdead.mod.item;

import com.craftingdead.mod.common.core.CraftingDead;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public class ItemBase extends Item {
    protected String name;
    protected IBakedModel mainModel;

    public ItemBase(final String name) {
        this.name = name;
        setRegistryName(name);
    }

    public void registerModel() {
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(CraftingDead.MOD_ID + ":" + name, "inventory");
        ModelLoader.setCustomModelResourceLocation(this, 0, modelResourceLocation);
        try {
            this.mainModel = (IBakedModel) ModelLoaderRegistry.getModel(modelResourceLocation);
        } catch (Exception e) {
            CraftingDead.LOGGER.error(e.getMessage());
        }
    }

    @Override
    public ItemBase setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }
}
