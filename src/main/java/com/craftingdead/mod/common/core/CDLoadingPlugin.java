package com.craftingdead.mod.common.core;

import java.util.Map;

import com.craftingdead.mod.client.transformer.MinecraftTransformer;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class CDLoadingPlugin implements IFMLLoadingPlugin {

    private static final String[] ASM_TRANSFORMER_CLASSES = new String[]{
            MinecraftTransformer.class.getCanonicalName()};

    private static final String ACCESS_TRANSFORMER_CLASS = CDAccessTransformer.class.getCanonicalName();

    private static final String MOD_CONTAINER_CLASS = CDDummyContainer.class.getCanonicalName();

    private static final String SETUP_CLASS = CraftingDead.class.getCanonicalName();

    @Override
    public String[] getASMTransformerClass() {
        return ASM_TRANSFORMER_CLASSES;
    }

    @Override
    public String getAccessTransformerClass() {
        return ACCESS_TRANSFORMER_CLASS;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        ;
    }

    @Override
    public String getModContainerClass() {
        return MOD_CONTAINER_CLASS;
    }

    @Override
    public String getSetupClass() {
        return SETUP_CLASS;
    }

}
