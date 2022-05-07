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

package com.craftingdead.core.world.inventory;

import com.craftingdead.core.CraftingDead;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {

  public static final DeferredRegister<MenuType<?>> deferredRegister =
      DeferredRegister.create(ForgeRegistries.CONTAINERS, CraftingDead.ID);

  public static final RegistryObject<MenuType<EquipmentMenu>> EQUIPMENT =
      deferredRegister.register("equipment", () -> new MenuType<>(EquipmentMenu::new));

  public static final RegistryObject<MenuType<GenericMenu>> VEST =
      deferredRegister.register("vest", () -> new MenuType<>(GenericMenu::createVest));

  public static final RegistryObject<MenuType<GenericMenu>> SMALL_BACKPACK =
      deferredRegister.register("small_backpack", () -> new MenuType<>(GenericMenu::createSmallBackpack));

  public static final RegistryObject<MenuType<GenericMenu>> MEDIUM_BACKPACK =
      deferredRegister.register("medium_backpack", () -> new MenuType<>(GenericMenu::createMediumBackpack));

  public static final RegistryObject<MenuType<GenericMenu>> LARGE_BACKPACK =
      deferredRegister.register("large_backpack", () -> new MenuType<>(GenericMenu::createLargeBackpack));

  public static final RegistryObject<MenuType<GenericMenu>> GUN_BAG =
      deferredRegister.register("gun_bag", () -> new MenuType<>(GenericMenu::createGunBag));
}
