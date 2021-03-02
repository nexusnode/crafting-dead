package com.craftingdead.core.ammoprovider;

import com.craftingdead.core.CraftingDead;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class AmmoProviderTypes {
  @SuppressWarnings("unchecked")
  public static final DeferredRegister<AmmoProviderType> AMMO_PROVIDER_TYPES =
      DeferredRegister.create((Class<AmmoProviderType>) (Class<?>) AmmoProviderType.class,
          CraftingDead.ID);

  public static final RegistryObject<AmmoProviderType> MAGAZINE =
      AMMO_PROVIDER_TYPES.register("magazine",
          () -> new AmmoProviderType(MagazineAmmoProvider::new));

  public static final RegistryObject<AmmoProviderType> REFILLABLE =
      AMMO_PROVIDER_TYPES.register("refillable",
          () -> new AmmoProviderType(RefillableAmmoProvider::new));

}
