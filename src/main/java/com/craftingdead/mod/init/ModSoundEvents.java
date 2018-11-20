package com.craftingdead.mod.init;

import com.craftingdead.mod.CraftingDead;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(CraftingDead.MOD_ID)
@Mod.EventBusSubscriber
public class ModSoundEvents {

	public static final SoundEvent ACR_SHOOT = null;

	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
		event.getRegistry().registerAll(createSoundEvent("item.gun.acr_shoot", "acr_shoot"));
	}

	private static SoundEvent createSoundEvent(String soundName, String registryName) {
		return new SoundEvent(new ResourceLocation(CraftingDead.MOD_ID, soundName))
				.setRegistryName(new ResourceLocation(CraftingDead.MOD_ID, registryName));
	}

}
