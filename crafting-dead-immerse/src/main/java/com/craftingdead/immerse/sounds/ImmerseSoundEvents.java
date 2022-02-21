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

package com.craftingdead.immerse.sounds;

import com.craftingdead.immerse.CraftingDeadImmerse;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ImmerseSoundEvents {

  public static final DeferredRegister<SoundEvent> soundEvents =
      DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CraftingDeadImmerse.ID);

  public static final RegistryObject<SoundEvent> BUTTON_CLICK = register("button_click");
  public static final RegistryObject<SoundEvent> MAIN_MENU_HOVER = register("main_menu_hover");
  public static final RegistryObject<SoundEvent> MAIN_MENU_PRESS_PLAY =
      register("main_menu_press_play");
  public static final RegistryObject<SoundEvent> TAB_SELECT = register("tab_select");
  public static final RegistryObject<SoundEvent> TAB_HOVER = register("tab_hover");
  public static final RegistryObject<SoundEvent> DROP_DOWN_EXPAND = register("drop_down_expand");
  public static final RegistryObject<SoundEvent> SUBMENU_SELECT = register("submenu_select");

  public static final RegistryObject<SoundEvent> COUNTDOWN = register("countdown");
  public static final RegistryObject<SoundEvent> VICTORY_MUSIC = register("victory_music");
  public static final RegistryObject<SoundEvent> DEFEAT_MUSIC = register("defeat_music");

  public static final RegistryObject<SoundEvent> START_MUSIC = register("start_music");

  public static final RegistryObject<SoundEvent> RED_VICTORY = register("team.red.victory");
  public static final RegistryObject<SoundEvent> RED_DEFEAT = register("team.red.defeat");

  public static final RegistryObject<SoundEvent> BLUE_VICTORY = register("team.blue.victory");
  public static final RegistryObject<SoundEvent> BLUE_DEFEAT = register("team.blue.defeat");

  private static RegistryObject<SoundEvent> register(String name) {
    ResourceLocation registryName = new ResourceLocation(CraftingDeadImmerse.ID, name);
    return soundEvents.register(name, () -> new SoundEvent(registryName));
  }
}
