package com.craftingdead.immerse.world.level.extension;

import com.craftingdead.immerse.CraftingDeadImmerse;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class LandOwnerTypes {

  public static final DeferredRegister<LandOwnerType> landOwnerTypes =
      DeferredRegister.create(LandOwnerType.class, CraftingDeadImmerse.ID);

  public static final Lazy<IForgeRegistry<LandOwnerType>> registry =
      Lazy.of(landOwnerTypes.makeRegistry("land_owner_type", RegistryBuilder::new));

  public static final RegistryObject<LandOwnerType> LEGACY_BASE =
      landOwnerTypes.register("legacy_base",
          () -> new LandOwnerType(LegacyBase.CODEC, LegacyBase.CODEC));

}
