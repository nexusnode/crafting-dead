package com.craftingdead.immerse.world.effect;

import com.craftingdead.immerse.CraftingDeadImmerse;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ImmerseMobEffects {

  public static final DeferredRegister<MobEffect> deferredRegister =
      DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CraftingDeadImmerse.ID);

  public static final RegistryObject<MobEffect> HYDRATE =
      deferredRegister.register("hydrate", HydrateMobEffect::new);
}
