/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.client.animation.gun;

import java.util.function.Supplier;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.animation.Animation;
import com.craftingdead.core.util.FunctionRegistryEntry;
import com.craftingdead.core.world.item.gun.GunConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class GunAnimationTypes {

  public static final ResourceKey<Registry<FunctionRegistryEntry<GunConfiguration, Animation>>> REGISTRY_KEY =
      ResourceKey.createRegistryKey(new ResourceLocation(CraftingDead.ID, "gun_animation_type"));

  public static final DeferredRegister<FunctionRegistryEntry<GunConfiguration, Animation>> deferredRegister =
      DeferredRegister.create(REGISTRY_KEY, CraftingDead.ID);

  @SuppressWarnings("unchecked")
  public static final Supplier<IForgeRegistry<FunctionRegistryEntry<GunConfiguration, Animation>>> registry =
      deferredRegister.makeRegistry(
          (Class<FunctionRegistryEntry<GunConfiguration, Animation>>) (Class<?>) FunctionRegistryEntry.class,
          RegistryBuilder::new);

  public static final Codec<FunctionRegistryEntry<GunConfiguration, Animation>> CODEC =
      ExtraCodecs.lazyInitializedCodec(() -> registry.get().getCodec());

  public static final RegistryObject<FunctionRegistryEntry<GunConfiguration, Animation>> INSPECT =
      deferredRegister.register("inspect",
          () -> FunctionRegistryEntry.of(gun -> new InspectAnimation()));

  public static final RegistryObject<FunctionRegistryEntry<GunConfiguration, Animation>> RELOAD =
      deferredRegister.register("reload",
          () -> FunctionRegistryEntry.of(gun -> new ReloadAnimation(gun.getReloadDurationTicks())));

  public static final RegistryObject<FunctionRegistryEntry<GunConfiguration, Animation>> PISTOL_SHOOT =
      deferredRegister.register("pistol_shoot",
          () -> FunctionRegistryEntry.of(gun -> new PistolShootAnimation()));

  public static final RegistryObject<FunctionRegistryEntry<GunConfiguration, Animation>> RIFLE_SHOOT =
      deferredRegister.register("rifle_shoot",
          () -> FunctionRegistryEntry.of(gun -> new RifleShootAnimation()));

  public static final RegistryObject<FunctionRegistryEntry<GunConfiguration, Animation>> SUBMACHINE_SHOOT =
      deferredRegister.register("submachine_shoot",
          () -> FunctionRegistryEntry.of(gun -> new SubmachineShootAnimation()));
}
