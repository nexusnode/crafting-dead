package com.craftingdead.mod.potion;

import com.craftingdead.mod.CraftingDead;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(CraftingDead.ID)
@Mod.EventBusSubscriber(modid = CraftingDead.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEffects {

  public static final Effect BROKEN_LEG = null;

  public static final Effect HYDRATE = null;

  @SubscribeEvent
  public static void handle(RegistryEvent.Register<Effect> event) {
    event.getRegistry().registerAll(new BrokenLegEffect() //
            .setRegistryName(new ResourceLocation(CraftingDead.ID, "broken_leg")), //
        new HydrateEffect() //
            .setRegistryName(new ResourceLocation(CraftingDead.ID, "hydrate")));
  }
}
