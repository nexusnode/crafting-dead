package com.craftingdead.mod.init;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.potion.PotionBrokenLeg;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(CraftingDead.MOD_ID)
@Mod.EventBusSubscriber
public class ModPotions {

	public static final Potion BROKEN_LEG = null;

	@SubscribeEvent
	public static void registerPotions(RegistryEvent.Register<Potion> event) {
		event.getRegistry()
				.registerAll(appendRegistryName("broken_leg", new PotionBrokenLeg(true, 0)).setIconIndex(0, 0)
						.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED,
								"7107DE5E-7CE8-4030-940E-514C1F160890", -0.15000000596046448D, 2));
	}

	@SuppressWarnings("unchecked")
	private static <P extends Potion> P appendRegistryName(String registryName, P potion) {
		return (P) potion.setRegistryName(new ResourceLocation(CraftingDead.MOD_ID, registryName))
				.setPotionName(String.format("%s%s%s", CraftingDead.MOD_ID, ".", registryName));
	}

}
