/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.immerse.sounds;

import com.craftingdead.immerse.CraftingDeadImmerse;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ImmerseSoundEvents {

  public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
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
    return SOUND_EVENTS.register(name, () -> new SoundEvent(registryName));
  }
}
