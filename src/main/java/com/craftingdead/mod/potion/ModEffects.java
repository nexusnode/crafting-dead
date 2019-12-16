package com.craftingdead.mod.potion;

import com.craftingdead.mod.CraftingDead;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEffects {

  public static final DeferredRegister<Effect> EFFECTS =
      new DeferredRegister<>(ForgeRegistries.POTIONS, CraftingDead.ID);
  
  public static final RegistryObject<Effect> SCUBA =
      EFFECTS.register("scuba", HydrateEffect::new);
  
  public static final RegistryObject<Effect> INFECTION =
      EFFECTS.register("infection", HydrateEffect::new);

  public static final RegistryObject<Effect> BLEEDING =
      EFFECTS.register("bleeding", BleedingEffect::new);

  public static final RegistryObject<Effect> BROKEN_LEG =
      EFFECTS.register("broken_leg", BrokenLegEffect::new);

  public static final RegistryObject<Effect> HYDRATE =
      EFFECTS.register("hydrate", HydrateEffect::new);
}
