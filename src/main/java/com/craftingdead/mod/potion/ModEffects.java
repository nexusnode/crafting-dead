package com.craftingdead.mod.potion;

import com.craftingdead.mod.CraftingDead;

import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(CraftingDead.MOD_ID)
@Mod.EventBusSubscriber(modid = CraftingDead.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEffects {

	public static final Effect BROKEN_LEG = null;

	@SubscribeEvent
	public static void handle(RegistryEvent.Register<Effect> event) {
		event.getRegistry().registerAll( //
				new BrokenLegEffect() //
						.setRegistryName(new ResourceLocation(CraftingDead.MOD_ID, "broken_leg")) //
		);
	}
}
