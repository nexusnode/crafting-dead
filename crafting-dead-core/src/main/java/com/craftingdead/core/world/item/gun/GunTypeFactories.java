package com.craftingdead.core.world.item.gun;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.world.item.gun.aimable.AimableGunType;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class GunTypeFactories {

  public static final DeferredRegister<GunTypeFactory> gunTypeFactories =
      DeferredRegister.create(GunTypeFactory.class, CraftingDead.ID);

  public static final Lazy<IForgeRegistry<GunTypeFactory>> registry =
      Lazy.of(gunTypeFactories.makeRegistry("gun_type_factory", RegistryBuilder::new));

  public static final RegistryObject<GunTypeFactory> SIMPLE = gunTypeFactories.register("simple",
      () -> new GunTypeFactory(GunType.DIRECT_CODEC));

  public static final RegistryObject<GunTypeFactory> AIMABLE = gunTypeFactories.register("aimable",
      () -> new GunTypeFactory(AimableGunType.DIRECT_CODEC));
}
