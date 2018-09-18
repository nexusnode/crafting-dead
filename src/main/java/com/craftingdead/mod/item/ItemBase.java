package com.craftingdead.mod.item;

import com.craftingdead.mod.common.core.CraftingDead;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ItemBase extends Item {
    protected String name;

    public ItemBase(final String name) {
        this.name = name;
        setRegistryName(name);
    }

    public void registerModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(CraftingDead.MOD_ID + ":" + name, "assets.craftingdead.textures.gun"));
    }

    @Override
    public ItemBase setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }
}
