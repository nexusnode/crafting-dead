package com.craftingdead.mod.common.core;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

import java.io.File;
import java.util.List;

/**
 * Injects Crafting Dead into the mod list. All normal mod behaviours are
 * available through this class - this container will receive and handle normal
 * loading events
 * 
 * @author Sm0keySa1m0n
 *
 */
public class CDDummyContainer extends DummyModContainer {

	public CDDummyContainer() {
		super(new ModMetadata());
		this.getMetadata().modId = CraftingDead.MOD_ID;
		this.getMetadata().name = CraftingDead.MOD_NAME;
		this.getMetadata().version = CraftingDead.MOD_VERSION;
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		return CraftingDead.instance().registerBus(bus, controller);
	}

	@Override
	public Class<?> getCustomResourcePackClass() {
		return CDResourcePack.class;
	}

	@Override
	public List<String> getOwnedPackages() {
		return ImmutableList.of("com.craftingdead.mod.client", "com.craftingdead.mod.server",
				"com.craftingdead.mod.common.server");
	}

	@Override
	public File getSource() {
		return CraftingDead.instance().getModLocation();
	}

}
