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

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.animation.Animation;
import com.craftingdead.core.util.FunctionRegistryEntry;
import com.craftingdead.core.world.item.gun.GunType;
import com.mojang.serialization.Codec;
import net.minecraft.util.ExtraCodecs;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class GunAnimations {

  public static final DeferredRegister<FunctionRegistryEntry<GunType, Animation>> gunAnimations =
      DeferredRegister.create(FunctionRegistryEntry.asClass(), CraftingDead.ID);

  public static final Lazy<IForgeRegistry<FunctionRegistryEntry<GunType, Animation>>> registry =
      Lazy.of(gunAnimations.makeRegistry("gun_animation", RegistryBuilder::new));

  public static final Codec<FunctionRegistryEntry<GunType, Animation>> CODEC =
      ExtraCodecs.lazyInitializedCodec(() -> registry.get().getCodec());

  public static final RegistryObject<FunctionRegistryEntry<GunType, Animation>> INSPECT = gunAnimations.register(
      "inspect",
      () -> FunctionRegistryEntry.of((gun) -> new InspectAnimation()));

  public static final RegistryObject<FunctionRegistryEntry<GunType, Animation>> RELOAD = gunAnimations.register(
      "reload",
      () -> FunctionRegistryEntry.of((gun) -> new ReloadAnimation(gun.getReloadDurationTicks())));

  public static final RegistryObject<FunctionRegistryEntry<GunType, Animation>> PISTOL_SHOOT = gunAnimations.register(
      "pistol_shoot",
      () -> FunctionRegistryEntry.of((gun) -> new PistolShootAnimation()));

  public static final RegistryObject<FunctionRegistryEntry<GunType, Animation>> RIFLE_SHOOT = gunAnimations.register(
      "rifle_shoot",
      () -> FunctionRegistryEntry.of((gun) -> new RifleShootAnimation()));

  public static final RegistryObject<FunctionRegistryEntry<GunType, Animation>> SUBMACHINE_SHOOT = gunAnimations.register(
      "submachine_shoot",
      () -> FunctionRegistryEntry.of((gun) -> new SubmachineShootAnimation()));
}
