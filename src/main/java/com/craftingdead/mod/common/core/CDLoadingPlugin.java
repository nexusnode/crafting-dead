package com.craftingdead.mod.common.core;

import com.craftingdead.mod.client.transformer.MinecraftTransformer;
import com.craftingdead.mod.client.transformer.SplashProgressTransformer;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

/**
 * Used by FML to load our transformers and mod
 * 
 * @author Sm0keySa1m0n
 *
 */
public class CDLoadingPlugin implements IFMLLoadingPlugin {

	private static final String[] ASM_TRANSFORMER_CLASSES = new String[] {
			MinecraftTransformer.class.getCanonicalName(), SplashProgressTransformer.class.getCanonicalName() };

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