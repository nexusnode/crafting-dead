package com.craftingdead.mod.client.renderer.entity;

import com.craftingdead.mod.common.entity.monster.EntityCDZombie;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class EntityRenderers {

	public static void registerRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(EntityCDZombie.class, new IRenderFactory<EntityCDZombie>() {

			@Override
			public Render<? super EntityCDZombie> createRenderFor(RenderManager manager) {
				return new RenderCDZombie(manager, new ModelBiped(), 0.4F);
			}

		});
	}

}
