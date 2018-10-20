package com.craftingdead.mod.client.renderer.item;

import com.craftingdead.mod.client.ClientProxy;
import com.craftingdead.mod.client.model.ModelHandle;
import com.craftingdead.mod.common.CraftingDead;

import net.minecraft.util.ResourceLocation;

public class ArcRenderer extends GunRenderer {

	public ArcRenderer(ClientProxy client) {
		super(client, ModelHandle.of(new ResourceLocation(CraftingDead.MOD_ID, "item/arc_base")));
	}

}
