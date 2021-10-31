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

package com.craftingdead.core.data;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.tags.ModItemTags;
import com.craftingdead.core.world.item.ModItems;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemTagsProvider extends ItemTagsProvider {

  public ModItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider,
      ExistingFileHelper existingFileHelper) {
    super(dataGenerator, blockTagProvider, CraftingDead.ID, existingFileHelper);
  }

  @Override
  public void addTags() {
    this.tag(ModItemTags.MAGAZINES)
        .add(ModItems.STANAG_BOX_MAGAZINE.get())
        .add(ModItems.STANAG_DRUM_MAGAZINE.get())
        .add(ModItems.STANAG_30_ROUND_MAGAZINE.get())
        .add(ModItems.STANAG_20_ROUND_MAGAZINE.get())
        .add(ModItems.MPT55_MAGAZINE.get())
        .add(ModItems.AK47_30_ROUND_MAGAZINE.get())
        .add(ModItems.FNFAL_MAGAZINE.get())
        .add(ModItems.ACR_MAGAZINE.get())
        .add(ModItems.G36C_MAGAZINE.get())
        .add(ModItems.HK417_MAGAZINE.get())
        .add(ModItems.M1911_MAGAZINE.get())
        .add(ModItems.G18_MAGAZINE.get())
        .add(ModItems.M9_MAGAZINE.get())
        .add(ModItems.DESERT_EAGLE_MAGAZINE.get())
        .add(ModItems.P250_MAGAZINE.get())
        .add(ModItems.MAGNUM_MAGAZINE.get())
        .add(ModItems.FN57_MAGAZINE.get())
        .add(ModItems.P90_MAGAZINE.get())
        .add(ModItems.VECTOR_MAGAZINE.get())
        .add(ModItems.MP5A5_21_ROUND_MAGAZINE.get())
        .add(ModItems.MP5A5_35_ROUND_MAGAZINE.get())
        .add(ModItems.MAC10_EXTENDED_MAGAZINE.get())
        .add(ModItems.MAC10_MAGAZINE.get())
        .add(ModItems.SPORTER22_MAGAZINE.get())
        .add(ModItems.M107_MAGAZINE.get())
        .add(ModItems.AS50_MAGAZINE.get())
        .add(ModItems.M1GARAND_MAGAZINE.get())
        .add(ModItems.AWP_MAGAZINE.get())
        .add(ModItems.TRENCH_GUN_SHELLS.get())
        .add(ModItems.MOSSBERG_SHELLS.get())
        .add(ModItems.DMR_MAGAZINE.get())
        .add(ModItems.TASER_CARTRIDGE.get())
        .add(ModItems.M240B_MAGAZINE.get())
        .add(ModItems.RPK_DRUM_MAGAZINE.get())
        .add(ModItems.RPK_MAGAZINE.get())
        .add(ModItems.MINIGUN_MAGAZINE.get())
        .add(ModItems.MK48MOD_MAGAZINE.get());

    this.tag(ModItemTags.SYRINGES).add(ModItems.ADRENALINE_SYRINGE.get(),
        ModItems.SYRINGE.get(), ModItems.BLOOD_SYRINGE.get());
    this.tag(ModItemTags.CLOTHING).add(ModItems.ARMY_CLOTHING.get(),
        ModItems.ARMY_DESERT_CLOTHING.get(), ModItems.ARMY_MEDIC_CLOTHING.get(),
        ModItems.BLUE_DUSK_CLOTHING.get(), ModItems.BUILDER_CLOTHING.get(),
        ModItems.BUSINESS_CLOTHING.get(), ModItems.CAMO_CLOTHING.get(),
        ModItems.CASUAL_GREEN_CLOTHING.get(), ModItems.CLONE_CLOTHING.get(),
        ModItems.COMBAT_BDU_CLOTHING.get(), ModItems.CONTRACTOR_CLOTHING.get(),
        ModItems.COOKIE_CLOTHING.get(), ModItems.DDPAT_CLOTHING.get(),
        ModItems.DEADPOOL_CLOTHING.get(), ModItems.DOCTOR_CLOTHING.get(),
        ModItems.FIREMAN_CLOTHING.get(), ModItems.FULL_GHILLIE_CLOTHING.get(),
        ModItems.GREEN_DUSK_CLOTHING.get(), ModItems.HAZMAT_CLOTHING.get(),
        ModItems.JUGGERNAUT_CLOTHING.get(), ModItems.MIL_HAZMAT_CLOTHING.get(),
        ModItems.NINJA_CLOTHING.get(), ModItems.ORANGE_DUSK_CLOTHING.get(),
        ModItems.PILOT_CLOTHING.get(), ModItems.POLICE_CLOTHING.get(),
        ModItems.PRESIDENT_CLOTHING.get(), ModItems.PURPLE_DUSK_CLOTHING.get(),
        ModItems.RED_DUSK_CLOTHING.get(), ModItems.SAS_CLOTHING.get(),
        ModItems.SCUBA_CLOTHING.get(), ModItems.SEC_GUARD_CLOTHING.get(),
        ModItems.SHERIFF_CLOTHING.get(), ModItems.SMART_CLOTHING.get(),
        ModItems.SPACE_SUIT_CLOTHING.get(), ModItems.SPETSNAZ_CLOTHING.get(),
        ModItems.SWAT_CLOTHING.get(), ModItems.TAC_GHILLIE_CLOTHING.get(),
        ModItems.WHITE_DUSK_CLOTHING.get(), ModItems.WINTER_ARMY_CLOTHING.get(),
        ModItems.YELLOW_DUSK_CLOTHING.get());
    this.tag(ModItemTags.MELEES).add(ModItems.BAT.get(), ModItems.BO_STAFF.get(),
        ModItems.BOLT_CUTTERS.get(), ModItems.BOWIE_KNIFE.get(), ModItems.BROADSWORD.get(),
        ModItems.BROKEN_BOTTLE.get(), ModItems.CHAINSAW.get(), ModItems.CLEAVER.get(),
        ModItems.COMBAT_KNIFE.get(), ModItems.CROWBAR.get(), ModItems.FIRE_AXE.get(),
        ModItems.FRYING_PAN.get(), ModItems.GOLF_CLUB.get(), ModItems.HATCHET.get(),
        ModItems.KATANA.get(), ModItems.MACHETE.get(), ModItems.NAIL_BAT.get(),
        ModItems.NIGHT_STICK.get(), ModItems.PICKAXE.get(), ModItems.PIPE.get(),
        ModItems.RUSTY_PIPE.get(), ModItems.SCYTHE.get(), ModItems.SHOVEL.get(),
        ModItems.SLEDGEHAMMER.get(), ModItems.STEEL_BAT.get(), ModItems.WEAPONIZED_SCYTHE.get(),
        ModItems.WRENCH.get());
    this.tag(ModItemTags.HATS).add(ModItems.BEANIE_HAT.get(),
        ModItems.BLACK_BALLISTIC_HAT.get(), ModItems.BLUE_HARD_HAT.get(), ModItems.BUNNY_HAT.get(),
        ModItems.CLONE_HAT.get(), ModItems.FIREMAN_CHIEF_HAT.get(), ModItems.FIREMAN_HAT.get(),
        ModItems.GHILLIE_HAT.get(), ModItems.GREEN_HARD_HAT.get(), ModItems.HAZMAT_HAT.get(),
        ModItems.KNIGHT_HAT.get(), ModItems.MILITARY_HAZMAT_HAT.get(), ModItems.NINJA_HAT.get(),
        ModItems.NV_GOGGLES_HAT.get(), ModItems.ORANGE_HARD_HAT.get(), ModItems.RIOT_HAT.get(),
        ModItems.SANTA_HAT.get(), ModItems.TOP_HAT.get(), ModItems.TRAPPER_HAT.get(),
        ModItems.USHANKA_HAT.get(), ModItems.YELLOW_HARD_HAT.get());
  }

  @Override
  public String getName() {
    return "Crafting Dead Item Tags";
  }
}
