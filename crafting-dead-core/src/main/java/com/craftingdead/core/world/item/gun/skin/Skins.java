/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.item.gun.skin;

import java.util.Arrays;
import java.util.stream.Collectors;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.world.item.ModItems;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

public class Skins {

  public static final ResourceKey<Registry<Skin>> SKINS = createRegistryKey("skins");

  public static final WritableRegistry<Skin> REGISTRY =
      new MappedRegistry<>(SKINS, Lifecycle.stable(), null);

  public static final ResourceKey<Skin> VULCAN =
      ResourceKey.create(SKINS, new ResourceLocation(CraftingDead.ID, "vulcan"));

  public static final ResourceKey<Skin> ASMO =
      ResourceKey.create(SKINS, new ResourceLocation(CraftingDead.ID, "asmo"));

  public static final ResourceKey<Skin> CANDY_APPLE =
      ResourceKey.create(SKINS, new ResourceLocation(CraftingDead.ID, "candy_apple"));

  public static final ResourceKey<Skin> CYREX =
      ResourceKey.create(SKINS, new ResourceLocation(CraftingDead.ID, "cyrex"));

  public static final ResourceKey<Skin> DIAMOND =
      ResourceKey.create(SKINS, new ResourceLocation(CraftingDead.ID, "diamond"));

  public static final ResourceKey<Skin> DRAGON =
      ResourceKey.create(SKINS, new ResourceLocation(CraftingDead.ID, "dragon"));

  public static final ResourceKey<Skin> FADE =
      ResourceKey.create(SKINS, new ResourceLocation(CraftingDead.ID, "fade"));

  public static final ResourceKey<Skin> FURY =
      ResourceKey.create(SKINS, new ResourceLocation(CraftingDead.ID, "fury"));

  public static final ResourceKey<Skin> GEM =
      ResourceKey.create(SKINS, new ResourceLocation(CraftingDead.ID, "gem"));

  public static final ResourceKey<Skin> INFERNO =
      ResourceKey.create(SKINS, new ResourceLocation(CraftingDead.ID, "inferno"));

  public static final ResourceKey<Skin> RUBY =
      ResourceKey.create(SKINS, new ResourceLocation(CraftingDead.ID, "ruby"));

  public static final ResourceKey<Skin> SCORCHED =
      ResourceKey.create(SKINS, new ResourceLocation(CraftingDead.ID, "scorched"));

  public static final ResourceKey<Skin> SLAUGHTER =
      ResourceKey.create(SKINS, new ResourceLocation(CraftingDead.ID, "slaughter"));

  public static final ResourceKey<Skin> UV =
      ResourceKey.create(SKINS, new ResourceLocation(CraftingDead.ID, "uv"));

  public static final ResourceKey<Skin> HYPER_BEAST =
      ResourceKey.create(SKINS, new ResourceLocation(CraftingDead.ID, "hyper_beast"));

  public static final ResourceKey<Skin> EMPEROR_DRAGON =
      ResourceKey.create(SKINS, new ResourceLocation(CraftingDead.ID, "emperor_dragon"));

  public static final ResourceKey<Skin> NUCLEAR_WINTER =
      ResourceKey.create(SKINS, new ResourceLocation(CraftingDead.ID, "nuclear_winter"));

  public static final ResourceKey<Skin> MONARCH =
      ResourceKey.create(SKINS, new ResourceLocation(CraftingDead.ID, "monarch"));

  public static final ResourceKey<Skin> LOVELACE =
      ResourceKey.create(SKINS, new ResourceLocation(CraftingDead.ID, "lovelace"));

  static {
    register(VULCAN, ModItems.AK47);
    register(ASMO, ModItems.AK47, ModItems.AWP, ModItems.M4A1, ModItems.P250, ModItems.P90,
        ModItems.SCARL);
    register(CANDY_APPLE, ModItems.AS50, ModItems.M107);
    register(CYREX, ModItems.M4A1, ModItems.M9);
    register(DIAMOND, ModItems.AS50, ModItems.DMR, ModItems.M107, ModItems.M4A1);
    register(DRAGON, ModItems.AWP);
    register(FADE, ModItems.G18, ModItems.MAC10);
    register(FURY, ModItems.MINIGUN);
    register(GEM, ModItems.P90);
    register(INFERNO, ModItems.DESERT_EAGLE, ModItems.M4A1);
    register(RUBY, ModItems.P90);
    register(SCORCHED, ModItems.AWP, ModItems.DESERT_EAGLE, ModItems.DMR, ModItems.M4A1);
    register(SLAUGHTER, ModItems.VECTOR);
    register(UV, ModItems.MAC10);
    register(HYPER_BEAST, ModItems.M4A1);
    register(EMPEROR_DRAGON, ModItems.M4A1);
    register(NUCLEAR_WINTER, ModItems.DESERT_EAGLE);
    register(MONARCH, ModItems.AWP);
    register(LOVELACE, ModItems.DESERT_EAGLE, ModItems.M4A1);
  }

  private static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
    return ResourceKey.createRegistryKey(new ResourceLocation(CraftingDead.ID, name));
  }

  private static void register(ResourceKey<Skin> location, RegistryObject<?>... guns) {
    REGISTRY.register(location, new Skin(location.location(),
        Arrays.stream(guns)
            .map(RegistryObject::getId)
            .collect(Collectors.toList())),
        Lifecycle.stable());
  }
}
