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
	public static final SoundEvent AK47_SHOOT = null;
	public static final SoundEvent DESERT_EAGLE_SHOOT = null;
	public static final SoundEvent M4A1_SHOOT = null;
	public static final SoundEvent M9_SHOOT = null;
	public static final SoundEvent TASER_SHOOT = null;

	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
		event.getRegistry().registerAll(createSoundEvent("acr_shoot"), createSoundEvent("ak47_shoot"),
				createSoundEvent("desert_eagle_shoot"), createSoundEvent("m4a1_shoot"), createSoundEvent("m9_shoot"),
				createSoundEvent("taser_shoot"));
	}

	private static SoundEvent createSoundEvent(String name) {
		ResourceLocation registryName = new ResourceLocation(CraftingDead.MOD_ID, name);
		return new SoundEvent(registryName).setRegistryName(registryName);
	}

}
