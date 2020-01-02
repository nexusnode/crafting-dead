package com.craftingdead.mod.potion;

import java.util.ArrayList;
import java.util.List;
import com.craftingdead.mod.CraftingDead;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

public class ModEffects {

  private static final List<Effect> toRegister = new ArrayList<>();

  public static Effect scuba;

  public static Effect infection;

  public static Effect bleeding;

  public static Effect brokenLeg;

  public static Effect hydrate;

  public static void initialize() {
    scuba = register("scuba", new HydrateEffect());
    bleeding = register("bleeding", new BleedingEffect());
    infection = register("infection", new HydrateEffect());
    brokenLeg = register("broken_leg", new BrokenLegEffect());
    hydrate = register("hydrate", new HydrateEffect());
  }

  private static Effect register(String name, Effect effect) {
    effect.setRegistryName(new ResourceLocation(CraftingDead.ID, name));
    toRegister.add(effect);
    return effect;
  }

  public static void register(RegistryEvent.Register<Effect> event) {
    toRegister.forEach(event.getRegistry()::register);
  }
}
