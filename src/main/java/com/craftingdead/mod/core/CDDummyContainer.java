package com.craftingdead.mod.core;

import com.google.common.eventbus.EventBus;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

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

}
