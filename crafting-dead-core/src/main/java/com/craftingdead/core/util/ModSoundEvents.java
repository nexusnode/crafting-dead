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

package com.craftingdead.core.util;

import com.craftingdead.core.CraftingDead;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSoundEvents {

  public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
      DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CraftingDead.ID);

  public static final RegistryObject<SoundEvent> ACR_SHOOT = register("acr_shoot");
  public static final RegistryObject<SoundEvent> AK47_RELOAD = register("ak47_reload");
  public static final RegistryObject<SoundEvent> AK47_DISTANT_SHOOT =
      register("ak47_distant_shoot");
  public static final RegistryObject<SoundEvent> AK47_SHOOT = register("ak47_shoot");
  public static final RegistryObject<SoundEvent> AS50_RELOAD = register("as50_reload");
  public static final RegistryObject<SoundEvent> AS50_SHOOT = register("as50_shoot");
  public static final RegistryObject<SoundEvent> AWP_RELOAD = register("awp_reload");
  public static final RegistryObject<SoundEvent> AWP_DISTANT_SHOOT = register("awp_distant_shoot");
  public static final RegistryObject<SoundEvent> AWP_SHOOT = register("awp_shoot");
  public static final RegistryObject<SoundEvent> BULLET_IMPACT_DIRT =
      register("bullet_impact_dirt");
  public static final RegistryObject<SoundEvent> BULLET_IMPACT_FLESH =
      register("bullet_impact_flesh");
  public static final RegistryObject<SoundEvent> BULLET_IMPACT_GLASS =
      register("bullet_impact_glass");
  public static final RegistryObject<SoundEvent> BULLET_IMPACT_METAL =
      register("bullet_impact_metal");
  public static final RegistryObject<SoundEvent> BULLET_IMPACT_METAL2 =
      register("bullet_impact_metal2");
  public static final RegistryObject<SoundEvent> BULLET_IMPACT_STONE =
      register("bullet_impact_stone");
  public static final RegistryObject<SoundEvent> BULLET_IMPACT_WOOD =
      register("bullet_impact_wood");
  public static final RegistryObject<SoundEvent> CROSSBOW_SHOOT = register("crossbow_shoot");
  public static final RegistryObject<SoundEvent> DESERT_EAGLE_DISTANT_SHOOT =
      register("desert_eagle_distant_shoot");
  public static final RegistryObject<SoundEvent> DESERT_EAGLE_SHOOT =
      register("desert_eagle_shoot");
  public static final RegistryObject<SoundEvent> DMR_RELOAD = register("dmr_reload");
  public static final RegistryObject<SoundEvent> DMR_SHOOT = register("dmr_shoot");
  public static final RegistryObject<SoundEvent> DRAGUNOV_DISTANT_SHOOT =
      register("dragunov_distant_shoot");
  public static final RegistryObject<SoundEvent> DRAGUNOV_SHOOT = register("dragunov_shoot");
  public static final RegistryObject<SoundEvent> DRY_FIRE = register("dry_fire");
  public static final RegistryObject<SoundEvent> FN57_RELOAD = register("fn57_reload");
  public static final RegistryObject<SoundEvent> FN57_SHOOT = register("fn57_shoot");
  public static final RegistryObject<SoundEvent> FNFAL_DISTANT_SHOOT =
      register("fnfal_distant_shoot");
  public static final RegistryObject<SoundEvent> FNFAL_SHOOT = register("fnfal_shoot");
  public static final RegistryObject<SoundEvent> G18_DISTANT_SHOOT = register("g18_distant_shoot");
  public static final RegistryObject<SoundEvent> G18_SHOOT = register("g18_shoot");
  public static final RegistryObject<SoundEvent> G36C_SHOOT = register("g36c_shoot");
  public static final RegistryObject<SoundEvent> HK417_SHOOT = register("hk417_shoot");
  public static final RegistryObject<SoundEvent> M107_RELOAD = register("m107_reload");
  public static final RegistryObject<SoundEvent> M107_SHOOT = register("m107_shoot");
  public static final RegistryObject<SoundEvent> M1911_RELOAD = register("m1911_reload");
  public static final RegistryObject<SoundEvent> M1911_DISTANT_SHOOT =
      register("m1911_distant_shoot");
  public static final RegistryObject<SoundEvent> M1911_SHOOT = register("m1911_shoot");
  public static final RegistryObject<SoundEvent> M1GARAND_SHOOT = register("m1garand_shoot");
  public static final RegistryObject<SoundEvent> M240B_RELOAD = register("m240b_reload");
  public static final RegistryObject<SoundEvent> M240B_DISTANT_SHOOT =
      register("m240b_distant_shoot");
  public static final RegistryObject<SoundEvent> M240B_SHOOT = register("m240b_shoot");
  public static final RegistryObject<SoundEvent> M4A1_RELOAD = register("m4a1_reload");
  public static final RegistryObject<SoundEvent> M4A1_DISTANT_SHOOT =
      register("m4a1_distant_shoot");
  public static final RegistryObject<SoundEvent> M4A1_SHOOT = register("m4a1_shoot");
  public static final RegistryObject<SoundEvent> M9_RELOAD = register("m9_reload");
  public static final RegistryObject<SoundEvent> M9_SHOOT = register("m9_shoot");
  public static final RegistryObject<SoundEvent> MAC10_DISTANT_SHOOT =
      register("mac10_distant_shoot");
  public static final RegistryObject<SoundEvent> MAC10_SHOOT = register("mac10_shoot");
  public static final RegistryObject<SoundEvent> MAGNUM_RELOAD = register("magnum_reload");
  public static final RegistryObject<SoundEvent> MAGNUM_SHOOT = register("magnum_shoot");
  public static final RegistryObject<SoundEvent> MINIGUN_BARREL = register("minigun_barrel");
  public static final RegistryObject<SoundEvent> MINIGUN_SHOOT = register("minigun_shoot");
  public static final RegistryObject<SoundEvent> MK48MOD_SHOOT = register("mk48mod_shoot");
  public static final RegistryObject<SoundEvent> MOSSBERG_RELOAD = register("mossberg_reload");
  public static final RegistryObject<SoundEvent> MOSSBERG_SHOOT = register("mossberg_shoot");
  public static final RegistryObject<SoundEvent> MP5A5_RELOAD = register("mp5a5_reload");
  public static final RegistryObject<SoundEvent> MP5A5_SHOOT = register("mp5a5_shoot");
  public static final RegistryObject<SoundEvent> MPT_SHOOT = register("mpt_shoot");
  public static final RegistryObject<SoundEvent> P250_DISTANT_SHOOT =
      register("p250_distant_shoot");
  public static final RegistryObject<SoundEvent> P250_SHOOT = register("p250_shoot");
  public static final RegistryObject<SoundEvent> P90_RELOAD = register("p90_reload");
  public static final RegistryObject<SoundEvent> P90_DISTANT_SHOOT = register("p90_distant_shoot");
  public static final RegistryObject<SoundEvent> P90_SHOOT = register("p90_shoot");
  public static final RegistryObject<SoundEvent> RPK_RELOAD = register("rpk_reload");
  public static final RegistryObject<SoundEvent> RPK_SHOOT = register("rpk_shoot");
  public static final RegistryObject<SoundEvent> SCARH_SHOOT = register("scarh_shoot");
  public static final RegistryObject<SoundEvent> SHOTGUN_RELOAD = register("shotgun_reload");
  public static final RegistryObject<SoundEvent> SILENCED_AK47_SHOOT =
      register("silenced_ak47_shoot");
  public static final RegistryObject<SoundEvent> SILENCED_M240B_SHOOT =
      register("silenced_m240b_shoot");
  public static final RegistryObject<SoundEvent> SILENCED_M4A1_SHOOT =
      register("silenced_m4a1_shoot");
  public static final RegistryObject<SoundEvent> SILENCED_M9_SHOOT = register("silenced_m9_shoot");
  public static final RegistryObject<SoundEvent> SILENCED_MK48MOD_SHOOT =
      register("silenced_mk48mod_shoot");
  public static final RegistryObject<SoundEvent> SILENCED_MP5A5_SHOOT =
      register("silenced_mp5a5_shoot");
  public static final RegistryObject<SoundEvent> SILENCED_P90_SHOOT =
      register("silenced_p90_shoot");
  public static final RegistryObject<SoundEvent> SILENCED_RPK_SHOOT =
      register("silenced_rpk_shoot");
  public static final RegistryObject<SoundEvent> SPORTER22_SHOOT = register("sporter22_shoot");
  public static final RegistryObject<SoundEvent> TASER_SHOOT = register("taser_shoot");
  public static final RegistryObject<SoundEvent> TOGGLE_FIRE_MODE = register("toggle_fire_mode");
  public static final RegistryObject<SoundEvent> TRENCHGUN_SHOOT = register("trenchgun_shoot");
  public static final RegistryObject<SoundEvent> VECTOR_DISTANT_SHOOT =
      register("vector_distant_shoot");
  public static final RegistryObject<SoundEvent> VECTOR_SHOOT = register("vector_shoot");
  public static final RegistryObject<SoundEvent> SCOPE_ZOOM = register("scope_zoom");
  public static final RegistryObject<SoundEvent> GUN_EQUIP = register("gun_equip");

  private static RegistryObject<SoundEvent> register(String name) {
    ResourceLocation registryName = new ResourceLocation(CraftingDead.ID, name);
    return SOUND_EVENTS.register(name, () -> new SoundEvent(registryName));
  }
}
