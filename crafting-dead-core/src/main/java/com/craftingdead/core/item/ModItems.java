/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
package com.craftingdead.core.item;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.action.ActionTypes;
import com.craftingdead.core.capability.actionprovider.DefaultActionProvider;
import com.craftingdead.core.capability.animationprovider.gun.AnimationType;
import com.craftingdead.core.capability.animationprovider.gun.fire.PistolShootAnimation;
import com.craftingdead.core.capability.animationprovider.gun.fire.RifleShootAnimation;
import com.craftingdead.core.capability.animationprovider.gun.fire.SubmachineShootAnimation;
import com.craftingdead.core.capability.animationprovider.gun.inspect.GunAnimationInspectPistol;
import com.craftingdead.core.capability.animationprovider.gun.inspect.GunAnimationInspectRifle;
import com.craftingdead.core.capability.animationprovider.gun.inspect.GunAnimationInspectSMG;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadACR;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadAK47;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadAS50;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadAWP;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadDMR;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadDeagle;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadFNFAL;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadFiveSeven;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadG18;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadG36C;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadHK417;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadM107;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadM1911;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadM1Garand;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadM240B;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadM4A1;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadM9;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadMAC10;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadMK48;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadMP5A5;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadMPT55;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadMagnum;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadMinigun;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadMossberg;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadP250;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadP90;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadRPK;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadSCARH;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadSporter22;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadTrench;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReloadVector;
import com.craftingdead.core.capability.gun.IGun;
import com.craftingdead.core.client.renderer.item.ACRRenderer;
import com.craftingdead.core.client.renderer.item.AK47Renderer;
import com.craftingdead.core.client.renderer.item.AS50Renderer;
import com.craftingdead.core.client.renderer.item.AWPRenderer;
import com.craftingdead.core.client.renderer.item.DMRRenderer;
import com.craftingdead.core.client.renderer.item.DesertEagleRenderer;
import com.craftingdead.core.client.renderer.item.FN57Renderer;
import com.craftingdead.core.client.renderer.item.FNFALRenderer;
import com.craftingdead.core.client.renderer.item.G18Renderer;
import com.craftingdead.core.client.renderer.item.G36CRenderer;
import com.craftingdead.core.client.renderer.item.HK417Renderer;
import com.craftingdead.core.client.renderer.item.M107Renderer;
import com.craftingdead.core.client.renderer.item.M1911Renderer;
import com.craftingdead.core.client.renderer.item.M1GarandRenderer;
import com.craftingdead.core.client.renderer.item.M240BRenderer;
import com.craftingdead.core.client.renderer.item.M4A1Renderer;
import com.craftingdead.core.client.renderer.item.M9Renderer;
import com.craftingdead.core.client.renderer.item.MAC10Renderer;
import com.craftingdead.core.client.renderer.item.MK48ModRenderer;
import com.craftingdead.core.client.renderer.item.MP5A5Renderer;
import com.craftingdead.core.client.renderer.item.MPT55Renderer;
import com.craftingdead.core.client.renderer.item.MagnumRenderer;
import com.craftingdead.core.client.renderer.item.MinigunRenderer;
import com.craftingdead.core.client.renderer.item.MossbergRenderer;
import com.craftingdead.core.client.renderer.item.P250Renderer;
import com.craftingdead.core.client.renderer.item.P90Renderer;
import com.craftingdead.core.client.renderer.item.RPKRenderer;
import com.craftingdead.core.client.renderer.item.ScarhRenderer;
import com.craftingdead.core.client.renderer.item.Sporter22Renderer;
import com.craftingdead.core.client.renderer.item.TaserRenderer;
import com.craftingdead.core.client.renderer.item.TrenchgunRenderer;
import com.craftingdead.core.client.renderer.item.VectorRenderer;
import com.craftingdead.core.entity.ModEntityTypes;
import com.craftingdead.core.entity.grenade.C4ExplosiveEntity;
import com.craftingdead.core.entity.grenade.DecoyGrenadeEntity;
import com.craftingdead.core.entity.grenade.FireGrenadeEntity;
import com.craftingdead.core.entity.grenade.FlashGrenadeEntity;
import com.craftingdead.core.entity.grenade.FragGrenadeEntity;
import com.craftingdead.core.entity.grenade.PipeGrenadeEntity;
import com.craftingdead.core.entity.grenade.SmokeGrenadeEntity;
import com.craftingdead.core.inventory.CraftingInventorySlotType;
import com.craftingdead.core.item.AttachmentItem.MultiplierType;
import com.craftingdead.core.util.ModSoundEvents;
import com.craftingdead.core.world.storage.loot.ModLootTables;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

  public static final DeferredRegister<Item> ITEMS =
      DeferredRegister.create(ForgeRegistries.ITEMS, CraftingDead.ID);

  // ================================================================================
  // Paints
  // ================================================================================

  public static final RegistryObject<PaintItem> VULCAN_PAINT = ITEMS
      .register("vulcan_paint",
          () -> new PaintItem((PaintItem.Properties) new PaintItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<PaintItem> ASMO_PAINT = ITEMS
      .register("asmo_paint",
          () -> new PaintItem((PaintItem.Properties) new PaintItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<PaintItem> CANDY_APPLE_PAINT = ITEMS
      .register("candy_apple_paint",
          () -> new PaintItem((PaintItem.Properties) new PaintItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<PaintItem> CYREX_PAINT = ITEMS
      .register("cyrex_paint",
          () -> new PaintItem((PaintItem.Properties) new PaintItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<PaintItem> DIAMOND_PAINT = ITEMS
      .register("diamond_paint",
          () -> new PaintItem((PaintItem.Properties) new PaintItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<PaintItem> DRAGON_PAINT = ITEMS
      .register("dragon_paint",
          () -> new PaintItem((PaintItem.Properties) new PaintItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<PaintItem> FADE_PAINT = ITEMS
      .register("fade_paint",
          () -> new PaintItem((PaintItem.Properties) new PaintItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<PaintItem> FURY_PAINT = ITEMS
      .register("fury_paint",
          () -> new PaintItem((PaintItem.Properties) new PaintItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<PaintItem> GEM_PAINT = ITEMS
      .register("gem_paint",
          () -> new PaintItem((PaintItem.Properties) new PaintItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<PaintItem> INFERNO_PAINT = ITEMS
      .register("inferno_paint",
          () -> new PaintItem((PaintItem.Properties) new PaintItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<PaintItem> MULTI_PAINT = ITEMS
      .register("multi_paint",
          () -> new PaintItem((PaintItem.Properties) new PaintItem.Properties()
              .setColour()
              .setHasSkin(false)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<PaintItem> RUBY_PAINT = ITEMS
      .register("ruby_paint",
          () -> new PaintItem((PaintItem.Properties) new PaintItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<PaintItem> SCORCHED_PAINT = ITEMS
      .register("scorched_paint",
          () -> new PaintItem((PaintItem.Properties) new PaintItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<PaintItem> SLAUGHTER_PAINT = ITEMS
      .register("slaughter_paint",
          () -> new PaintItem((PaintItem.Properties) new PaintItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<PaintItem> UV_PAINT = ITEMS
      .register("uv_paint",
          () -> new PaintItem((PaintItem.Properties) new PaintItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<PaintItem> ULTRA_BEAST_PAINT = ITEMS
      .register("ultra_beast_paint",
          () -> new PaintItem((PaintItem.Properties) new PaintItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<PaintItem> EMPEROR_DRAGON_PAINT = ITEMS
      .register("emperor_dragon_paint",
          () -> new PaintItem((PaintItem.Properties) new PaintItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<PaintItem> NUCLEAR_WINTER_PAINT = ITEMS
      .register("nuclear_winter_paint",
          () -> new PaintItem((PaintItem.Properties) new PaintItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));
  // ================================================================================
  // Magazines
  // ================================================================================

  public static final RegistryObject<MagazineItem> STANAG_BOX_MAGAZINE = ITEMS
      .register("stanag_box_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(85)
              .setArmorPenetration(0.4F)
              .setCustomTexture(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> STANAG_DRUM_MAGAZINE = ITEMS
      .register("stanag_drum_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(45)
              .setArmorPenetration(0.4F)
              .setNextTier(STANAG_BOX_MAGAZINE)
              .setCustomTexture(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> STANAG_30_ROUND_MAGAZINE = ITEMS
      .register("stanag_30_round_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .setArmorPenetration(0.4F)
              .setNextTier(STANAG_DRUM_MAGAZINE)
              .setCustomTexture(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> STANAG_20_ROUND_MAGAZINE = ITEMS
      .register("stanag_20_round_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(20)
              .setArmorPenetration(0.4F)
              .setNextTier(STANAG_30_ROUND_MAGAZINE)
              .setCustomTexture(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> MPT55_MAGAZINE = ITEMS
      .register("mpt55_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .setArmorPenetration(0.4F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> AK47_30_ROUND_MAGAZINE = ITEMS
      .register("ak47_30_round_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> FNFAL_MAGAZINE = ITEMS
      .register("fnfal_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(20)
              .setArmorPenetration(0.55F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> ACR_MAGAZINE = ITEMS
      .register("acr_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(20)
              .setArmorPenetration(0.5F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> G36C_MAGAZINE = ITEMS
      .register("g36c_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .setArmorPenetration(0.45F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> HK417_MAGAZINE = ITEMS
      .register("hk417_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .setArmorPenetration(0.47F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> M1911_MAGAZINE = ITEMS
      .register("m1911_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(7)
              .setArmorPenetration(0.08F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> G18_MAGAZINE = ITEMS
      .register("g18_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(20)
              .setArmorPenetration(0.08F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> M9_MAGAZINE = ITEMS
      .register("m9_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(15)
              .setArmorPenetration(0.08F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> DESERT_EAGLE_MAGAZINE = ITEMS
      .register("desert_eagle_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(13)
              .setArmorPenetration(0.35F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> P250_MAGAZINE = ITEMS
      .register("p250_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(12)
              .setArmorPenetration(0.08F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> MAGNUM_MAGAZINE = ITEMS
      .register("magnum_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(6)
              .setArmorPenetration(0.65F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> FN57_MAGAZINE = ITEMS
      .register("fn57_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(20)
              .setArmorPenetration(0.09F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> P90_MAGAZINE = ITEMS
      .register("p90_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(50)
              .setArmorPenetration(0.15F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> VECTOR_MAGAZINE = ITEMS
      .register("vector_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .setArmorPenetration(0.15F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> MP5A5_35_ROUND_MAGAZINE = ITEMS
      .register("mp5a5_35_round_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(35)
              .setArmorPenetration(0.15F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> MP5A5_21_ROUND_MAGAZINE = ITEMS
      .register("mp5a5_21_round_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(21)
              .setArmorPenetration(0.15F)
              .setNextTier(MP5A5_35_ROUND_MAGAZINE)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> MAC10_EXTENDED_MAGAZINE = ITEMS
      .register("mac10_extended_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(45)
              .setArmorPenetration(0.15F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> MAC10_MAGAZINE = ITEMS
      .register("mac10_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .setArmorPenetration(0.15F)
              .setNextTier(MAC10_EXTENDED_MAGAZINE)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> SPORTER22_MAGAZINE = ITEMS
      .register("sporter22_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> M107_MAGAZINE = ITEMS
      .register("m107_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(10)
              .setArmorPenetration(0.65F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> AS50_MAGAZINE = ITEMS
      .register("as50_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(10)
              .setArmorPenetration(0.65F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> M1GARAND_MAGAZINE = ITEMS
      .register("m1garand_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(8)
              .setArmorPenetration(0.95F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> AWP_MAGAZINE = ITEMS
      .register("awp_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(10)
              .setArmorPenetration(0.95F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> TRENCHGUN_SHELLS = ITEMS
      .register("trenchgun_shells",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(6)
              .setArmorPenetration(0.35F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> MOSSBERG_SLUGS = ITEMS
      .register("mossberg_slugs",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(8)
              .setArmorPenetration(0.3F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> DMR_MAGAZINE = ITEMS
      .register("dmr_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(5)
              .setArmorPenetration(0.65F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> TASER_CARTRIDGE = ITEMS
      .register("taser_cartridge",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(3)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> M240B_MAGAZINE = ITEMS
      .register("m240b_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(150)
              .setArmorPenetration(0.5F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));


  public static final RegistryObject<MagazineItem> RPK_DRUM_MAGAZINE = ITEMS
      .register("rpk_drum_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(100)
              .setArmorPenetration(0.5F)
              .setCustomTexture(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> RPK_MAGAZINE = ITEMS
      .register("rpk_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(60)
              .setArmorPenetration(0.5F)
              .setNextTier(RPK_DRUM_MAGAZINE)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> MINIGUN_MAGAZINE = ITEMS
      .register("minigun_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(350)
              .setArmorPenetration(0.3F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> MK48MOD_MAGAZINE = ITEMS
      .register("mk48mod_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(150)
              .setArmorPenetration(0.52F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  // ================================================================================
  // Attachments
  // ================================================================================

  public static final RegistryObject<AttachmentItem> RED_DOT_SIGHT = ITEMS
      .register("red_dot_sight",
          () -> new AttachmentItem((AttachmentItem.Properties) new AttachmentItem.Properties()
              .addMultiplier(MultiplierType.ZOOM, 2.5F)
              .setInventorySlot(CraftingInventorySlotType.OVERBARREL_ATTACHMENT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<AttachmentItem> ACOG_SIGHT = ITEMS
      .register("acog_sight",
          () -> new AttachmentItem((AttachmentItem.Properties) new AttachmentItem.Properties()
              .addMultiplier(MultiplierType.ZOOM, 3.25F)
              .setInventorySlot(CraftingInventorySlotType.OVERBARREL_ATTACHMENT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<AttachmentItem> LP_SCOPE = ITEMS
      .register("lp_scope",
          () -> new AttachmentItem((AttachmentItem.Properties) new AttachmentItem.Properties()
              .addMultiplier(MultiplierType.ZOOM, 5.0F)
              .setInventorySlot(CraftingInventorySlotType.OVERBARREL_ATTACHMENT)
              .setScope(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<AttachmentItem> HP_SCOPE = ITEMS
      .register("hp_scope",
          () -> new AttachmentItem((AttachmentItem.Properties) new AttachmentItem.Properties()
              .addMultiplier(MultiplierType.ZOOM, 8.0F)
              .setInventorySlot(CraftingInventorySlotType.OVERBARREL_ATTACHMENT)
              .setScope(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<AttachmentItem> SUPPRESSOR = ITEMS
      .register("suppressor",
          () -> new AttachmentItem((AttachmentItem.Properties) new AttachmentItem.Properties()
              .setInventorySlot(CraftingInventorySlotType.MUZZLE_ATTACHMENT)
              .setSoundSuppressor(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<AttachmentItem> TACTICAL_GRIP = ITEMS
      .register("tactical_grip",
          () -> new AttachmentItem((AttachmentItem.Properties) new AttachmentItem.Properties()
              .addMultiplier(MultiplierType.ACCURACY, 1.15F)
              .setInventorySlot(CraftingInventorySlotType.UNDERBARREL_ATTACHMENT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<AttachmentItem> BIPOD = ITEMS
      .register("bipod",
          () -> new AttachmentItem((AttachmentItem.Properties) new AttachmentItem.Properties()
              .addMultiplier(MultiplierType.ACCURACY, 1.05F)
              .setInventorySlot(CraftingInventorySlotType.UNDERBARREL_ATTACHMENT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<AttachmentItem> EOTECH_SIGHT = ITEMS
      .register("eotech_sight",
          () -> new AttachmentItem((AttachmentItem.Properties) new AttachmentItem.Properties()
              .addMultiplier(MultiplierType.ZOOM, 2.5F)
              .setInventorySlot(CraftingInventorySlotType.OVERBARREL_ATTACHMENT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  // ================================================================================
  // Assault Rifles
  // ================================================================================

  public static final RegistryObject<GunItem> M4A1 = ITEMS
      .register("m4a1",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(80)
              .setDamage(7)
              .setReloadDurationTicks(20 * 4)
              .setAccuracy(0.9F)
              .addFireMode(FireMode.AUTO)
              .addFireMode(FireMode.BURST)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.M4A1_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.M4A1_RELOAD)
              .addAnimation(AnimationType.SHOOT, RifleShootAnimation::new)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadM4A1::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
              .setDefaultMagazine(STANAG_20_ROUND_MAGAZINE)
              .addAcceptedMagazine(STANAG_30_ROUND_MAGAZINE)
              .addAcceptedMagazine(STANAG_DRUM_MAGAZINE)
              .addAcceptedMagazine(STANAG_BOX_MAGAZINE)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedAttachment(LP_SCOPE)
              .addAcceptedAttachment(HP_SCOPE)
              .addAcceptedAttachment(TACTICAL_GRIP)
              .addAcceptedAttachment(BIPOD)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedAttachment(EOTECH_SIGHT)
              .addAcceptedPaint(CYREX_PAINT)
              .addAcceptedPaint(ASMO_PAINT)
              .addAcceptedPaint(DIAMOND_PAINT)
              .addAcceptedPaint(INFERNO_PAINT)
              .addAcceptedPaint(SCORCHED_PAINT)
              .addAcceptedPaint(MULTI_PAINT)
              .addAcceptedPaint(EMPEROR_DRAGON_PAINT)
              .addAcceptedPaint(ULTRA_BEAST_PAINT)
              .setRendererFactory(() -> M4A1Renderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GunItem> SCARH = ITEMS
      .register("scarh",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(110)
              .setDamage(6)
              .setReloadDurationTicks(20 * 4)
              .setAccuracy(0.9F)
              .addFireMode(FireMode.AUTO)
              .addFireMode(FireMode.BURST)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.SCARH_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.M4A1_RELOAD)
              .addAnimation(AnimationType.SHOOT, RifleShootAnimation::new)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadSCARH::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
              .setDefaultMagazine(STANAG_20_ROUND_MAGAZINE)
              .addAcceptedMagazine(STANAG_30_ROUND_MAGAZINE)
              .addAcceptedMagazine(STANAG_DRUM_MAGAZINE)
              .addAcceptedMagazine(STANAG_BOX_MAGAZINE)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedAttachment(LP_SCOPE)
              .addAcceptedAttachment(HP_SCOPE)
              .addAcceptedAttachment(TACTICAL_GRIP)
              .addAcceptedAttachment(BIPOD)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedAttachment(EOTECH_SIGHT)
              .addAcceptedPaint(ASMO_PAINT)
              .addAcceptedPaint(MULTI_PAINT)
              .setRendererFactory(() -> ScarhRenderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GunItem> AK47 = ITEMS
      .register("ak47",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(80)
              .setDamage(7)
              .setReloadDurationTicks(20 * 4)
              .setAccuracy(0.8F)
              .addFireMode(FireMode.AUTO)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.AK47_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_AK47_SHOOT)
              .setReloadSound(ModSoundEvents.AK47_RELOAD)
              .addAnimation(AnimationType.SHOOT, RifleShootAnimation::new)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadAK47::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
              .setDefaultMagazine(AK47_30_ROUND_MAGAZINE)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedAttachment(LP_SCOPE)
              .addAcceptedAttachment(HP_SCOPE)
              .addAcceptedAttachment(TACTICAL_GRIP)
              .addAcceptedAttachment(BIPOD)
              .addAcceptedAttachment(EOTECH_SIGHT)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedPaint(VULCAN_PAINT)
              .addAcceptedPaint(MULTI_PAINT)
              .setRendererFactory(() -> AK47Renderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GunItem> FNFAL = ITEMS
      .register("fnfal",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(100)
              .setDamage(9)
              .setReloadDurationTicks(20 * 4)
              .setAccuracy(0.8F)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.FNFAL_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_RPK_SHOOT)
              .setReloadSound(ModSoundEvents.M4A1_RELOAD)
              .addAnimation(AnimationType.SHOOT, RifleShootAnimation::new)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadFNFAL::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
              .setDefaultMagazine(FNFAL_MAGAZINE)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedAttachment(LP_SCOPE)
              .addAcceptedAttachment(HP_SCOPE)
              .addAcceptedAttachment(TACTICAL_GRIP)
              .addAcceptedAttachment(BIPOD)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedPaint(MULTI_PAINT)
              .setRendererFactory(() -> FNFALRenderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GunItem> ACR = ITEMS
      .register("acr",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(92)
              .setDamage(7)
              .setReloadDurationTicks(20 * 4)
              .setAccuracy(0.8F)
              .addFireMode(FireMode.AUTO)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.ACR_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.M4A1_RELOAD)
              .addAnimation(AnimationType.SHOOT, RifleShootAnimation::new)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadACR::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
              .setDefaultMagazine(ACR_MAGAZINE)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedAttachment(LP_SCOPE)
              .addAcceptedAttachment(HP_SCOPE)
              .addAcceptedAttachment(TACTICAL_GRIP)
              .addAcceptedAttachment(EOTECH_SIGHT)
              .addAcceptedPaint(MULTI_PAINT)
              .setRendererFactory(() -> ACRRenderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GunItem> HK417 = ITEMS
      .register("hk417",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(100)
              .setDamage(8)
              .setReloadDurationTicks((int) (20 * 2.1F))
              .setAccuracy(0.8F)
              .addFireMode(FireMode.AUTO)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.HK417_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_RPK_SHOOT)
              .setReloadSound(ModSoundEvents.M4A1_RELOAD)
              .addAnimation(AnimationType.SHOOT, RifleShootAnimation::new)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadHK417::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
              .setDefaultMagazine(HK417_MAGAZINE)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(TACTICAL_GRIP)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedAttachment(LP_SCOPE)
              .addAcceptedAttachment(HP_SCOPE)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedAttachment(EOTECH_SIGHT)
              .addAcceptedPaint(MULTI_PAINT)
              .setRendererFactory(() -> HK417Renderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GunItem> MPT55 = ITEMS
      .register("mpt55",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(70)
              .setDamage(6)
              .setReloadDurationTicks(20 * 2)
              .setAccuracy(0.8F)
              .addFireMode(FireMode.BURST)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.MPT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.M4A1_RELOAD)
              .addAnimation(AnimationType.SHOOT, RifleShootAnimation::new)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadMPT55::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
              .setDefaultMagazine(MPT55_MAGAZINE)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedAttachment(LP_SCOPE)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedPaint(MULTI_PAINT)
              .setRendererFactory(() -> MPT55Renderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GunItem> M1GARAND = ITEMS
      .register("m1garand",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(170)
              .setDamage(10)
              .setReloadDurationTicks(20 * 2)
              .setAccuracy(0.9F)
              .setCrosshair(false)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.M1GARAND_SHOOT)
              .setReloadSound(ModSoundEvents.AS50_RELOAD)
              .addAnimation(AnimationType.SHOOT, RifleShootAnimation::new)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadM1Garand::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
              .setDefaultMagazine(M1GARAND_MAGAZINE)
              .addAcceptedAttachment(LP_SCOPE)
              .addAcceptedAttachment(HP_SCOPE)
              .addAcceptedAttachment(BIPOD)
              .addAcceptedPaint(MULTI_PAINT)
              .setRendererFactory(() -> M1GarandRenderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GunItem> SPORTER22 = ITEMS
      .register("sporter22",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(200)
              .setDamage(7)
              .setReloadDurationTicks((int) (20 * 1.5F))
              .setAccuracy(0.9F)
              .setCrosshair(false)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.SPORTER22_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_RPK_SHOOT)
              .setReloadSound(ModSoundEvents.M107_RELOAD)
              .addAnimation(AnimationType.SHOOT, RifleShootAnimation::new)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadSporter22::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
              .setDefaultMagazine(SPORTER22_MAGAZINE)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedAttachment(LP_SCOPE)
              .addAcceptedAttachment(HP_SCOPE)
              .addAcceptedAttachment(TACTICAL_GRIP)
              .addAcceptedAttachment(BIPOD)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedPaint(MULTI_PAINT)
              .setRendererFactory(() -> Sporter22Renderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GunItem> G36C = ITEMS
      .register("g36c",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(92)
              .setDamage(8)
              .setReloadDurationTicks((int) (20 * 2.2F))
              .setAccuracy(0.8F)
              .addFireMode(FireMode.AUTO)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.G36C_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.M4A1_RELOAD)
              .addAnimation(AnimationType.SHOOT, RifleShootAnimation::new)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadG36C::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
              .setDefaultMagazine(G36C_MAGAZINE)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(TACTICAL_GRIP)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedAttachment(EOTECH_SIGHT)
              .addAcceptedPaint(MULTI_PAINT)
              .setRendererFactory(() -> G36CRenderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));


  // ================================================================================
  // Machine Guns
  // ================================================================================

  public static final RegistryObject<GunItem> M240B = ITEMS
      .register("m240b",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(85)
              .setDamage(8)
              .setReloadDurationTicks(20 * 4)
              .setAccuracy(0.9F)
              .addFireMode(FireMode.AUTO)
              .setShootSound(ModSoundEvents.M240B_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M240B_SHOOT)
              .setReloadSound(ModSoundEvents.M240B_RELOAD)
              .addAnimation(AnimationType.SHOOT, SubmachineShootAnimation::new)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadM240B::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
              .setDefaultMagazine(M240B_MAGAZINE)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(TACTICAL_GRIP)
              .addAcceptedAttachment(BIPOD)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedAttachment(EOTECH_SIGHT)
              .addAcceptedPaint(MULTI_PAINT)
              .setRendererFactory(() -> M240BRenderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GunItem> RPK = ITEMS
      .register("rpk",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(92)
              .setDamage(6)
              .setReloadDurationTicks(20 * 3)
              .setAccuracy(0.9F)
              .addFireMode(FireMode.AUTO)
              .setShootSound(ModSoundEvents.RPK_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M240B_SHOOT)
              .setReloadSound(ModSoundEvents.RPK_RELOAD)
              .addAnimation(AnimationType.SHOOT, RifleShootAnimation::new)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadRPK::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
              .setDefaultMagazine(RPK_MAGAZINE)
              .addAcceptedMagazine(RPK_DRUM_MAGAZINE)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedPaint(MULTI_PAINT)
              .setRendererFactory(() -> RPKRenderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GunItem> MINIGUN = ITEMS
      .register("minigun",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setAimable(false)
              .setFireRate(25)
              .setDamage(4)
              .setReloadDurationTicks(20 * 5)
              .setAccuracy(0.9F)
              .addFireMode(FireMode.AUTO)
              .setShootSound(ModSoundEvents.MINIGUN_SHOOT)
              .setReloadSound(ModSoundEvents.M240B_RELOAD)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadMinigun::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
              .setDefaultMagazine(MINIGUN_MAGAZINE)
              .addAcceptedPaint(FURY_PAINT)
              .addAcceptedPaint(MULTI_PAINT)
              .setRendererFactory(() -> MinigunRenderer::new)
              .setRightMouseActionTriggerType(IGun.RightMouseActionTriggerType.HOLD)
              .setTriggerPredicate(IGun::isPerformingRightMouseAction)
              .setRightMouseActionSound(ModSoundEvents.MINIGUN_BARREL)
              .setRightMouseActionSoundRepeatDelayMs(177L)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GunItem> MK48MOD = ITEMS
      .register("mk48mod",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(92)
              .setDamage(7)
              .setReloadDurationTicks(20 * 4)
              .setAccuracy(0.9F)
              .addFireMode(FireMode.AUTO)
              .setShootSound(ModSoundEvents.MK48MOD_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_MK48MOD_SHOOT)
              .setReloadSound(ModSoundEvents.M240B_RELOAD)
              .addAnimation(AnimationType.SHOOT, SubmachineShootAnimation::new)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadMK48::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
              .setDefaultMagazine(MK48MOD_MAGAZINE)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(TACTICAL_GRIP)
              .addAcceptedAttachment(BIPOD)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedAttachment(EOTECH_SIGHT)
              .addAcceptedPaint(MULTI_PAINT)
              .setRendererFactory(() -> MK48ModRenderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  // ================================================================================
  // Pistols
  // ================================================================================

  public static final RegistryObject<GunItem> TASER = ITEMS
      .register("taser",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(2000)
              .setDamage(7)
              .setReloadDurationTicks(20 * 3)
              .setAccuracy(0.9F)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.TASER_SHOOT)
              .addAnimation(AnimationType.SHOOT, PistolShootAnimation::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectPistol::new)
              .setDefaultMagazine(TASER_CARTRIDGE)
              .addAcceptedPaint(MULTI_PAINT)
              .setRendererFactory(() -> TaserRenderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GunItem> M1911 = ITEMS
      .register("m1911",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(120)
              .setDamage(7)
              .setReloadDurationTicks(20 * 2)
              .setAccuracy(0.9F)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.M1911_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.M1911_RELOAD)
              .addAnimation(AnimationType.SHOOT, PistolShootAnimation::new)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadM1911::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectPistol::new)
              .setDefaultMagazine(M1911_MAGAZINE)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedPaint(MULTI_PAINT)
              .setRendererFactory(() -> M1911Renderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GunItem> G18 = ITEMS
      .register("g18",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(120)
              .setDamage(7)
              .setReloadDurationTicks((int) (20 * 2.2F))
              .setAccuracy(0.9F)
              .addFireMode(FireMode.AUTO)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.G18_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_RPK_SHOOT)
              .setReloadSound(ModSoundEvents.M1911_RELOAD)
              .addAnimation(AnimationType.SHOOT, PistolShootAnimation::new)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadG18::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectPistol::new)
              .setDefaultMagazine(G18_MAGAZINE)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedPaint(FADE_PAINT)
              .addAcceptedPaint(MULTI_PAINT)
              .setRendererFactory(() -> G18Renderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GunItem> M9 = ITEMS
      .register("m9",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(120)
              .setDamage(7)
              .setReloadDurationTicks((int) (20 * 1.5F))
              .setAccuracy(0.9F)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.M9_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M9_SHOOT)
              .setReloadSound(ModSoundEvents.M9_RELOAD)
              .addAnimation(AnimationType.SHOOT, PistolShootAnimation::new)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadM9::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectPistol::new)
              .setDefaultMagazine(M9_MAGAZINE)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedPaint(MULTI_PAINT)
              .addAcceptedPaint(CYREX_PAINT)
              .setRendererFactory(() -> M9Renderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GunItem> DESERT_EAGLE = ITEMS
      .register("desert_eagle",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(120)
              .setDamage(8)
              .setReloadDurationTicks(20 * 2)
              .setAccuracy(0.7F)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.DESERT_EAGLE_SHOOT)
              .setReloadSound(ModSoundEvents.M9_RELOAD)
              .addAnimation(AnimationType.SHOOT, PistolShootAnimation::new)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadDeagle::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectPistol::new)
              .setDefaultMagazine(DESERT_EAGLE_MAGAZINE)
              .addAcceptedPaint(INFERNO_PAINT)
              .addAcceptedPaint(SCORCHED_PAINT)
              .addAcceptedPaint(MULTI_PAINT)
              .addAcceptedPaint(NUCLEAR_WINTER_PAINT)
              .setRendererFactory(() -> DesertEagleRenderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GunItem> P250 = ITEMS
      .register("p250",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(120)
              .setDamage(6)
              .setReloadDurationTicks((int) (20 * 1.5F))
              .setAccuracy(0.7F)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.P250_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M9_SHOOT)
              .setReloadSound(ModSoundEvents.M9_RELOAD)
              .addAnimation(AnimationType.SHOOT, PistolShootAnimation::new)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadP250::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectPistol::new)
              .setDefaultMagazine(P250_MAGAZINE)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedPaint(MULTI_PAINT)
              .addAcceptedPaint(ASMO_PAINT)
              .setRendererFactory(() -> P250Renderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GunItem> MAGNUM = ITEMS
      .register("magnum",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(80)
              .setDamage(7)
              .setReloadDurationTicks(20 * 2)
              .setAccuracy(0.9F)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.MAGNUM_SHOOT)
              .setReloadSound(ModSoundEvents.MAGNUM_RELOAD)
              .addAnimation(AnimationType.SHOOT, PistolShootAnimation::new)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadMagnum::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectPistol::new)
              .setDefaultMagazine(MAGNUM_MAGAZINE)
              .addAcceptedPaint(MULTI_PAINT)
              .setRendererFactory(() -> MagnumRenderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GunItem> FN57 = ITEMS
      .register("fn57",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(140)
              .setDamage(8)
              .setReloadDurationTicks((int) (20 * 1.5F))
              .setAccuracy(0.9F)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.FN57_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.FN57_RELOAD)
              .addAnimation(AnimationType.SHOOT, PistolShootAnimation::new)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadFiveSeven::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectPistol::new)
              .setDefaultMagazine(FN57_MAGAZINE)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedPaint(MULTI_PAINT)
              .setRendererFactory(() -> FN57Renderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  // ================================================================================
  // Submachine Guns
  // ================================================================================

  public static final RegistryObject<GunItem> MAC10 = ITEMS
      .register("mac10",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(100)
              .setDamage(6)
              .setReloadDurationTicks((int) (20 * 1.8F))
              .setAccuracy(0.9F)
              .addFireMode(FireMode.AUTO)
              .setShootSound(ModSoundEvents.MAC10_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.M4A1_RELOAD)
              .addAnimation(AnimationType.SHOOT, SubmachineShootAnimation::new)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadMAC10::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectSMG::new)
              .setDefaultMagazine(MAC10_MAGAZINE)
              .addAcceptedMagazine(MAC10_EXTENDED_MAGAZINE)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedPaint(FADE_PAINT)
              .addAcceptedPaint(UV_PAINT)
              .addAcceptedPaint(MULTI_PAINT)
              .setRendererFactory(() -> MAC10Renderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GunItem> P90 = ITEMS
      .register("p90",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(100)
              .setDamage(5)
              .setReloadDurationTicks((int) (20 * 2.2F))
              .setAccuracy(0.9F)
              .addFireMode(FireMode.AUTO)
              .setShootSound(ModSoundEvents.P90_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_P90_SHOOT)
              .setReloadSound(ModSoundEvents.P90_RELOAD)
              .addAnimation(AnimationType.SHOOT, SubmachineShootAnimation::new)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadP90::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectSMG::new)
              .setDefaultMagazine(P90_MAGAZINE)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedPaint(RUBY_PAINT)
              .addAcceptedPaint(GEM_PAINT)
              .addAcceptedPaint(MULTI_PAINT)
              .addAcceptedPaint(ASMO_PAINT)
              .setRendererFactory(() -> P90Renderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GunItem> VECTOR = ITEMS
      .register("vector",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(85)
              .setDamage(5)
              .setReloadDurationTicks((int) (20 * 1.9F))
              .setAccuracy(0.9F)
              .addFireMode(FireMode.AUTO)
              .setShootSound(ModSoundEvents.VECTOR_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.M4A1_RELOAD)
              .addAnimation(AnimationType.SHOOT, SubmachineShootAnimation::new)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadVector::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectSMG::new)
              .setDefaultMagazine(VECTOR_MAGAZINE)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedPaint(SLAUGHTER_PAINT)
              .addAcceptedPaint(MULTI_PAINT)
              .setRendererFactory(() -> VectorRenderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GunItem> MP5A5 = ITEMS
      .register("mp5a5",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(85)
              .setDamage(7)
              .setReloadDurationTicks((int) (20 * 2.2F))
              .setAccuracy(0.9F)
              .addFireMode(FireMode.AUTO)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.MP5A5_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_MP5A5_SHOOT)
              .setReloadSound(ModSoundEvents.MP5A5_RELOAD)
              .addAnimation(AnimationType.SHOOT, SubmachineShootAnimation::new)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadMP5A5::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectSMG::new)
              .setDefaultMagazine(MP5A5_21_ROUND_MAGAZINE)
              .addAcceptedMagazine(MP5A5_35_ROUND_MAGAZINE)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedPaint(MULTI_PAINT)
              .setRendererFactory(() -> MP5A5Renderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  // ================================================================================
  // Sniper Rifles
  // ================================================================================

  public static final RegistryObject<GunItem> M107 = ITEMS
      .register("m107",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(750)
              .setDamage(20)
              .setReloadDurationTicks(20 * 3)
              .setAccuracy(0.9F)
              .setCrosshair(false)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.M107_SHOOT)
              .setReloadSound(ModSoundEvents.M107_RELOAD)
              .addAnimation(AnimationType.SHOOT, RifleShootAnimation::new)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadM107::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
              .setDefaultMagazine(M107_MAGAZINE)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedAttachment(LP_SCOPE)
              .addAcceptedAttachment(HP_SCOPE)
              .addAcceptedAttachment(TACTICAL_GRIP)
              .addAcceptedAttachment(BIPOD)
              .addAcceptedPaint(CANDY_APPLE_PAINT)
              .addAcceptedPaint(DIAMOND_PAINT)
              .addAcceptedPaint(MULTI_PAINT)
              .setRendererFactory(() -> M107Renderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GunItem> AS50 = ITEMS
      .register("as50",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(170)
              .setDamage(14)
              .setReloadDurationTicks((int) (20 * 3.5F))
              .setAccuracy(0.9F)
              .setCrosshair(false)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.AS50_SHOOT)
              .setReloadSound(ModSoundEvents.AS50_RELOAD)
              .addAnimation(AnimationType.SHOOT, RifleShootAnimation::new)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadAS50::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
              .setDefaultMagazine(AS50_MAGAZINE)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedAttachment(LP_SCOPE)
              .addAcceptedAttachment(HP_SCOPE)
              .addAcceptedAttachment(TACTICAL_GRIP)
              .addAcceptedAttachment(BIPOD)
              .addAcceptedPaint(CANDY_APPLE_PAINT)
              .addAcceptedPaint(DIAMOND_PAINT)
              .addAcceptedPaint(MULTI_PAINT)
              .setRendererFactory(() -> AS50Renderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GunItem> AWP = ITEMS
      .register("awp",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(1200)
              .setDamage(20)
              .setReloadDurationTicks(20 * 3)
              .setAccuracy(0.95F)
              .setCrosshair(false)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.AWP_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.AWP_RELOAD)
              .addAnimation(AnimationType.SHOOT, RifleShootAnimation::new)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadAWP::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
              .setDefaultMagazine(AWP_MAGAZINE)
              .addAcceptedAttachment(LP_SCOPE)
              .addAcceptedAttachment(HP_SCOPE)
              .addAcceptedAttachment(BIPOD)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedPaint(DRAGON_PAINT)
              .addAcceptedPaint(SCORCHED_PAINT)
              .addAcceptedPaint(MULTI_PAINT)
              .addAcceptedPaint(ASMO_PAINT)
              .setRendererFactory(() -> AWPRenderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GunItem> DMR = ITEMS
      .register("dmr",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(170)
              .setDamage(15)
              .setReloadDurationTicks(20 * 2)
              .setAccuracy(0.9F)
              .setCrosshair(false)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.DMR_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_RPK_SHOOT)
              .setReloadSound(ModSoundEvents.DMR_RELOAD)
              .addAnimation(AnimationType.SHOOT, RifleShootAnimation::new)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadDMR::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
              .setDefaultMagazine(DMR_MAGAZINE)
              .addAcceptedAttachment(LP_SCOPE)
              .addAcceptedAttachment(HP_SCOPE)
              .addAcceptedAttachment(BIPOD)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedPaint(DIAMOND_PAINT)
              .addAcceptedPaint(SCORCHED_PAINT)
              .addAcceptedPaint(MULTI_PAINT)
              .setRendererFactory(() -> DMRRenderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  // ================================================================================
  // Shotguns
  // ================================================================================

  public static final RegistryObject<GunItem> TRENCHGUN = ITEMS
      .register("trenchgun",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(300)
              .setDamage(2)
              .setBulletAmountToFire(8)
              .setReloadDurationTicks(20 * 1)
              .setAccuracy(0.9F)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.TRENCHGUN_SHOOT)
              .setReloadSound(ModSoundEvents.SHOTGUN_RELOAD)
              .addAnimation(AnimationType.SHOOT, PistolShootAnimation::new)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadTrench::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
              .setDefaultMagazine(TRENCHGUN_SHELLS)
              .addAcceptedAttachment(TACTICAL_GRIP)
              .addAcceptedPaint(MULTI_PAINT)
              .setRendererFactory(() -> TrenchgunRenderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GunItem> MOSSBERG = ITEMS
      .register("mossberg",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setBulletAmountToFire(8)
              .setFireRate(300)
              .setDamage(3)
              .setReloadDurationTicks(20 * 3)
              .setAccuracy(0.9F)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.MOSSBERG_SHOOT)
              .setReloadSound(ModSoundEvents.MOSSBERG_RELOAD)
              .addAnimation(AnimationType.SHOOT, PistolShootAnimation::new)
              .addAnimation(AnimationType.RELOAD, GunAnimationReloadMossberg::new)
              .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
              .setDefaultMagazine(MOSSBERG_SLUGS)
              .addAcceptedAttachment(TACTICAL_GRIP)
              .addAcceptedPaint(MULTI_PAINT)
              .setRendererFactory(() -> MossbergRenderer::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  // ================================================================================
  // Grenades
  // ================================================================================

  public static final RegistryObject<GrenadeItem> FIRE_GRENADE = ITEMS
      .register("fire_grenade",
          () -> new GrenadeItem((GrenadeItem.Properties) new GrenadeItem.Properties()
              .setGrenadeEntitySupplier(FireGrenadeEntity::new)
              .maxStackSize(3)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GrenadeItem> SMOKE_GRENADE = ITEMS
      .register("smoke_grenade",
          () -> new GrenadeItem((GrenadeItem.Properties) new GrenadeItem.Properties()
              .setGrenadeEntitySupplier(SmokeGrenadeEntity::new)
              .maxStackSize(3)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GrenadeItem> FLASH_GRENADE = ITEMS
      .register("flash_grenade",
          () -> new GrenadeItem((GrenadeItem.Properties) new GrenadeItem.Properties()
              .setGrenadeEntitySupplier(FlashGrenadeEntity::new)
              .maxStackSize(3)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GrenadeItem> DECOY_GRENADE = ITEMS
      .register("decoy_grenade",
          () -> new GrenadeItem((GrenadeItem.Properties) new GrenadeItem.Properties()
              .setGrenadeEntitySupplier(DecoyGrenadeEntity::new)
              .maxStackSize(3)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GrenadeItem> FRAG_GRENADE = ITEMS
      .register("frag_grenade",
          () -> new GrenadeItem((GrenadeItem.Properties) new GrenadeItem.Properties()
              .setGrenadeEntitySupplier(FragGrenadeEntity::new)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GrenadeItem> PIPE_GRENADE = ITEMS
      .register("pipe_grenade",
          () -> new GrenadeItem((GrenadeItem.Properties) new GrenadeItem.Properties()
              .setGrenadeEntitySupplier(PipeGrenadeEntity::new)
              .maxStackSize(3)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<GrenadeItem> C4 = ITEMS
      .register("c4_explosive",
          () -> new GrenadeItem((GrenadeItem.Properties) new GrenadeItem.Properties()
              .setGrenadeEntitySupplier(C4ExplosiveEntity::new)
              .setThrowSpeed(0.75F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<Item> REMOTE_DETONATOR = ITEMS
      .register("remote_detonator", () -> new RemoteDetonatorItem(
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  // ================================================================================
  // Medical
  // ================================================================================

  public static final RegistryObject<Item> FIRST_AID_KIT = ITEMS
      .register("first_aid_kit",
          () -> new ActionItem(new Item.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MED),
              new DefaultActionProvider(ActionTypes.USE_FIRST_AID_KIT)));

  public static final RegistryObject<Item> ADRENALINE_SYRINGE = ITEMS
      .register("adrenaline_syringe",
          () -> new ActionItem(new Item.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MED),
              new DefaultActionProvider(ActionTypes.USE_ADRENALINE_SYRINGE)));

  public static final RegistryObject<Item> SYRINGE = ITEMS
      .register("syringe",
          () -> new ActionItem(new Item.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MED),
              new DefaultActionProvider(ActionTypes.USE_SYRINGE)));

  public static final RegistryObject<Item> BLOOD_SYRINGE = ITEMS
      .register("blood_syringe",
          () -> new ActionItem(new Item.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MED),
              new DefaultActionProvider(ActionTypes.USE_BLOOD_SYRINGE)));

  public static final RegistryObject<Item> BANDAGE = ITEMS
      .register("bandage",
          () -> new ActionItem(new Item.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MED),
              new DefaultActionProvider(ActionTypes.USE_BANDAGE)));

  public static final RegistryObject<Item> RBI_SYRINGE = ITEMS
      .register("rbi_syringe",
          () -> new ActionItem(new Item.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MED),
              new DefaultActionProvider(ActionTypes.USE_RBI_SYRINGE)));

  public static final RegistryObject<Item> DIRTY_RAG = ITEMS
      .register("dirty_rag",
          () -> new ActionItem(new Item.Properties()
              .group(ModItemGroups.CRAFTING_DEAD_MED),
              new DefaultActionProvider(ActionTypes.WASH_RAG)));

  public static final RegistryObject<Item> BLOODY_RAG = ITEMS
      .register("bloody_rag",
          () -> new ActionItem(new Item.Properties()
              .group(ModItemGroups.CRAFTING_DEAD_MED),
              new DefaultActionProvider(ActionTypes.WASH_RAG)));

  public static final RegistryObject<Item> CLEAN_RAG = ITEMS
      .register("clean_rag",
          () -> new ActionItem(new Item.Properties()
              .group(ModItemGroups.CRAFTING_DEAD_MED),
              new DefaultActionProvider(ActionTypes.USE_CLEAN_RAG)));

  public static final RegistryObject<Item> SPLINT = ITEMS
      .register("splint",
          () -> new ActionItem(new Item.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MED),
              new DefaultActionProvider(ActionTypes.USE_SPLINT)));

  public static final RegistryObject<Item> CURE_SYRINGE = ITEMS
      .register("cure_syringe",
          () -> new ActionItem(
              new Item.Properties().maxStackSize(1)
                  .group(ModItemGroups.CRAFTING_DEAD_MED),
              new DefaultActionProvider(ActionTypes.USE_CURE_SYRINGE)));

  // ================================================================================
  // Weapon
  // ================================================================================

  public static final RegistryObject<Item> CROWBAR = ITEMS
      .register("crowbar", () -> new MeleeWeaponItem(3, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_COMBAT))));

  public static final RegistryObject<Item> BAT = ITEMS
      .register("bat", () -> new MeleeWeaponItem(5, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_COMBAT))));

  public static final RegistryObject<Item> KATANA = ITEMS
      .register("katana", () -> new MeleeWeaponItem(18, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_COMBAT))));

  public static final RegistryObject<Item> PIPE = ITEMS
      .register("pipe", () -> new MeleeWeaponItem(9, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_COMBAT))));

  public static final RegistryObject<Item> RUSTY_PIPE = ITEMS
      .register("rusty_pipe", () -> new MeleeWeaponItem(9, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_COMBAT))));

  public static final RegistryObject<Item> FIRE_AXE = ITEMS
      .register("fire_axe", () -> new MeleeWeaponItem(14, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_COMBAT))));

  public static final RegistryObject<Item> CHAINSAW = ITEMS
      .register("chainsaw", () -> new MeleeWeaponItem(8, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_COMBAT))));

  public static final RegistryObject<Item> BOWIE_KNIFE = ITEMS
      .register("bowie_knife", () -> new MeleeWeaponItem(15, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_COMBAT))));

  public static final RegistryObject<Item> GOLF_CLUB = ITEMS
      .register("golf_club", () -> new MeleeWeaponItem(6, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_COMBAT))));

  public static final RegistryObject<Item> NIGHT_STICK = ITEMS
      .register("night_stick", () -> new MeleeWeaponItem(4, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_COMBAT))));

  public static final RegistryObject<Item> SLEDGEHAMMER = ITEMS
      .register("sledge_hammer", () -> new MeleeWeaponItem(10, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_COMBAT))));

  public static final RegistryObject<Item> NAIL_BAT = ITEMS
      .register("nail_bat", () -> new MeleeWeaponItem(8, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_COMBAT))));

  public static final RegistryObject<Item> SHOVEL = ITEMS
      .register("shovel", () -> new MeleeWeaponItem(8, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_COMBAT))));

  public static final RegistryObject<Item> HATCHET = ITEMS
      .register("hatchet", () -> new MeleeWeaponItem(16, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_COMBAT))));

  public static final RegistryObject<Item> BROADSWORD = ITEMS
      .register("broad_sword", () -> new MeleeWeaponItem(14, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_COMBAT))));

  public static final RegistryObject<Item> MACHETE = ITEMS
      .register("machete", () -> new MeleeWeaponItem(14, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_COMBAT))));

  public static final RegistryObject<Item> WEAPONIZED_SCYTHE = ITEMS
      .register("weaponized_scythe", () -> new MeleeWeaponItem(15, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_COMBAT))));

  public static final RegistryObject<Item> SCYTHE = ITEMS
      .register("scythe", () -> new MeleeWeaponItem(20, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_COMBAT))));

  public static final RegistryObject<Item> PICKAXE = ITEMS
      .register("pickaxe", () -> new MeleeWeaponItem(10, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_COMBAT))));

  public static final RegistryObject<Item> BO_STAFF = ITEMS
      .register("bo_staff", () -> new MeleeWeaponItem(4, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_COMBAT))));

  public static final RegistryObject<Item> WRENCH = ITEMS
      .register("wrench", () -> new MeleeWeaponItem(4, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_COMBAT))));

  public static final RegistryObject<Item> FRYING_PAN = ITEMS
      .register("frying_pan", () -> new MeleeWeaponItem(8, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_COMBAT))));

  public static final RegistryObject<Item> BOLT_CUTTERS = ITEMS
      .register("bolt_cutters", () -> new MeleeWeaponItem(9, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_COMBAT))));

  public static final RegistryObject<Item> COMBAT_KNIFE = ITEMS
      .register("combat_knife", () -> new MeleeWeaponItem(14, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_COMBAT))));

  public static final RegistryObject<Item> STEEL_BAT = ITEMS
      .register("steel_bat", () -> new MeleeWeaponItem(7, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_COMBAT))));

  public static final RegistryObject<Item> CLEAVER = ITEMS
      .register("cleaver", () -> new MeleeWeaponItem(10, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_COMBAT))));

  public static final RegistryObject<Item> BROKEN_BOTTLE = ITEMS
      .register("broken_bottle", () -> new MeleeWeaponItem(15, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_COMBAT))));

  // ================================================================================
  // Vests
  // ================================================================================

  public static final RegistryObject<Item> BLACK_TACTICAL_VEST = ITEMS
      .register("black_tactical_vest", () -> new StorageItem(StorageItem.VEST,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> GHILLIE_TACTICAL_VEST = ITEMS
      .register("ghillie_tactical_vest", () -> new StorageItem(StorageItem.VEST,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> GREEN_TACTICAL_VEST = ITEMS
      .register("green_tactical_vest", () -> new StorageItem(StorageItem.VEST,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> GREY_TACTICAL_VEST = ITEMS
      .register("grey_tactical_vest", () -> new StorageItem(StorageItem.VEST,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> RIOT_VEST = ITEMS
      .register("riot_vest", () -> new StorageItem(StorageItem.VEST,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> TAN_TACTICAL_VEST = ITEMS
      .register("tan_tactical_vest", () -> new StorageItem(StorageItem.VEST,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  // ================================================================================
  // Hats, Helmets and Masks
  // ================================================================================

  public static final RegistryObject<Item> ARMY_HELMET = ITEMS
      .register("army_helmet",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> BEANIE_HAT = ITEMS
      .register("beanie_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> BLACK_BALLISTIC_HAT = ITEMS
      .register("black_ballistic_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> FIREMAN_CHIEF_HAT = ITEMS
      .register("chief_fireman_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> BLUE_HARD_HAT = ITEMS
      .register("blue_hard_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> BUNNY_HAT = ITEMS
      .register("bunny_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> CAMO_HELMET = ITEMS
      .register("camo_helmet",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> CLONE_HAT = ITEMS
      .register("clone_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> COMBAT_BDU_HELMET = ITEMS
      .register("combat_bdu_helmet",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> COOKIE_MASK = ITEMS
      .register("cookie_mask",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> COW_MASK = ITEMS
      .register("cow_mask",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> CREEPER_MASK = ITEMS
      .register("creeper_mask",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> DEADPOOL_MASK = ITEMS
      .register("deadpool_mask",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> DOCTOR_MASK = ITEMS
      .register("doctor_mask",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> FIREMAN_HAT = ITEMS
      .register("fireman_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> GAS_MASK = ITEMS
      .register("gas_mask",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setImmuneToFlashes(true)
              .setImmuneToGas(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> GHILLIE_HAT = ITEMS
      .register("ghillie_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> GREEN_ARMY_HELMET = ITEMS
      .register("green_army_helmet",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> GREEN_BALLISTIC_HELMET = ITEMS
      .register("green_ballistic_helmet",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> GREEN_HARD_HAT = ITEMS
      .register("green_hard_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> GREY_ARMY_HELMET = ITEMS
      .register("grey_army_helmet",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> HACKER_MASK = ITEMS
      .register("hacker_mask",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> HAZMAT_HAT = ITEMS
      .register("hazmat_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setImmuneToFlashes(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> JUGGERNAUT_HELMET = ITEMS
      .register("juggernaut_helmet",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> KNIGHT_HAT = ITEMS
      .register("knight_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> MILITARY_HAZMAT_HAT = ITEMS
      .register("military_hazmat_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setImmuneToFlashes(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> NINJA_HAT = ITEMS
      .register("ninja_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> NV_GOGGLES_HAT = ITEMS
      .register("nv_goggles_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setNightVision(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> ORANGE_HARD_HAT = ITEMS
      .register("orange_hard_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> PAYDAY_MASK = ITEMS
      .register("payday_mask",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> PAYDAY2_MASK = ITEMS
      .register("payday2_mask",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> PILOT_HELMET = ITEMS
      .register("pilot_helmet",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> PUMPKIN_MASK = ITEMS
      .register("pumpkin_mask",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> RADAR_CAP = ITEMS
      .register("radar_cap",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> RIOT_HAT = ITEMS
      .register("riot_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> SANTA_HAT = ITEMS
      .register("santa_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  // TODO Add respiration ability to the CLOTHING+MASK item combo
  public static final RegistryObject<Item> SCUBA_MASK = ITEMS
      .register("scuba_mask",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> SHEEP_MASK = ITEMS
      .register("sheep_mask",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> SKI_MASK = ITEMS
      .register("ski_mask",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setImmuneToFlashes(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> SPETSNAZ_HELMET = ITEMS
      .register("spetsnaz_helmet",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> TOP_HAT = ITEMS
      .register("top_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> TRAPPER_HAT = ITEMS
      .register("trapper_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> USHANKA_HAT = ITEMS
      .register("ushanka_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> WINTER_MILITARY_HELMET = ITEMS
      .register("winter_military_helmet",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> YELLOW_HARD_HAT = ITEMS
      .register("yellow_hard_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> ZOMBIE_MASK = ITEMS
      .register("zombie_mask",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  // ================================================================================
  // Clothing
  // ================================================================================

  public static final RegistryObject<Item> ARMY_CLOTHING = ITEMS
      .register("army_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setEnhancedProtection(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> SAS_CLOTHING = ITEMS
      .register("sas_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setEnhancedProtection(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> SPETSNAZ_CLOTHING = ITEMS
      .register("spetsnaz_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setEnhancedProtection(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> POLICE_CLOTHING = ITEMS
      .register("police_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setEnhancedProtection(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> CAMO_CLOTHING = ITEMS
      .register("camo_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setEnhancedProtection(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> COMBAT_BDU_CLOTHING = ITEMS
      .register("combat_bdu_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setEnhancedProtection(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> WINTER_ARMY_CLOTHING = ITEMS
      .register("winter_army_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setEnhancedProtection(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> ARMY_DESERT_CLOTHING = ITEMS
      .register("army_desert_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setEnhancedProtection(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> PILOT_CLOTHING = ITEMS
      .register("pilot_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> HAZMAT_CLOTHING = ITEMS
      .register("hazmat_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setEnhancedProtection(true)
              .setFireImmunity(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> TAC_GHILLIE_CLOTHING = ITEMS
      .register("tac_ghillie_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setEnhancedProtection(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> SWAT_CLOTHING = ITEMS
      .register("swat_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setEnhancedProtection(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> SPACE_SUIT_CLOTHING = ITEMS
      .register("space_suit_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setEnhancedProtection(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> SHERIFF_CLOTHING = ITEMS
      .register("sheriff_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setEnhancedProtection(false)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> JUGGERNAUT_CLOTHING = ITEMS
      .register("juggernaut_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setEnhancedProtection(true)
              .setFireImmunity(true)
              .setSlownessAmplifier(0)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> FIREMAN_CLOTHING = ITEMS
      .register("fireman_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setEnhancedProtection(true)
              .setFireImmunity(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> DOCTOR_CLOTHING = ITEMS
      .register("doctor_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> SMART_CLOTHING = ITEMS
      .register("smart_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> CASUAL_GREEN_CLOTHING = ITEMS
      .register("casual_green_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> BUILDER_CLOTHING = ITEMS
      .register("builder_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> BUSINESS_CLOTHING = ITEMS
      .register("business_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> SEC_GUARD_CLOTHING = ITEMS
      .register("sec_guard_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> MIL_HAZMAT_CLOTHING = ITEMS
      .register("mil_hazmat_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setEnhancedProtection(true)
              .setFireImmunity(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> FULL_GHILLIE_CLOTHING = ITEMS
      .register("full_ghillie_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setEnhancedProtection(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> RED_DUSK_CLOTHING = ITEMS
      .register("red_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setEnhancedProtection(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> CLONE_CLOTHING = ITEMS
      .register("clone_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setEnhancedProtection(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> COOKIE_CLOTHING = ITEMS
      .register("cookie_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> DEADPOOL_CLOTHING = ITEMS
      .register("deadpool_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setEnhancedProtection(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> NINJA_CLOTHING = ITEMS
      .register("ninja_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> ARMY_MEDIC_CLOTHING = ITEMS
      .register("army_medic_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setEnhancedProtection(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> BLUE_DUSK_CLOTHING = ITEMS
      .register("blue_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setEnhancedProtection(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> PRESIDENT_CLOTHING = ITEMS
      .register("president_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> YELLOW_DUSK_CLOTHING = ITEMS
      .register("yellow_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setEnhancedProtection(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> ORANGE_DUSK_CLOTHING = ITEMS
      .register("orange_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setEnhancedProtection(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> GREEN_DUSK_CLOTHING = ITEMS
      .register("green_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setEnhancedProtection(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> WHITE_DUSK_CLOTHING = ITEMS
      .register("white_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setEnhancedProtection(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> PURPLE_DUSK_CLOTHING = ITEMS
      .register("purple_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setEnhancedProtection(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> SCUBA_CLOTHING = ITEMS
      .register("scuba_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> DDPAT_CLOTHING = ITEMS
      .register("ddpat_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setEnhancedProtection(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> CONTRACTOR_CLOTHING = ITEMS
      .register("contractor_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setEnhancedProtection(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  // ================================================================================
  // Air Drop Radio
  // ================================================================================

  public static final RegistryObject<Item> MEDICAL_DROP_RADIO = ITEMS
      .register("medical_drop_radio",
          () -> new AirDropRadioItem((AirDropRadioItem.Properties) new AirDropRadioItem.Properties()
              .setLootTable(ModLootTables.MEDICAL_SUPPLY_DROP)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MISC)));

  public static final RegistryObject<Item> MILITARY_DROP_RADIO = ITEMS
      .register("military_drop_radio",
          () -> new AirDropRadioItem((AirDropRadioItem.Properties) new AirDropRadioItem.Properties()
              .setLootTable(ModLootTables.MILITARY_SUPPLY_DROP)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MISC)));

  // ================================================================================
  // Spawn Eggs
  // ================================================================================

  public static final RegistryObject<Item> ADVANCED_ZOMBIE_SPAWN_EGG = ITEMS
      .register("advanced_zombie_spawn_egg", () -> new SpawnEggItem(ModEntityTypes.advancedZombie,
          0x000000, 0xFFFFFF, new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_MISC)));

  public static final RegistryObject<Item> FAST_ZOMBIE_SPAWN_EGG = ITEMS
      .register("fast_zombie_spawn_egg", () -> new SpawnEggItem(ModEntityTypes.fastZombie, 0x000000,
          0xFFFFFF, new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_MISC)));

  public static final RegistryObject<Item> TANK_ZOMBIE_SPAWN_EGG = ITEMS
      .register("tank_zombie_spawn_egg", () -> new SpawnEggItem(ModEntityTypes.tankZombie, 0x000000,
          0xFFFFFF, new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_MISC)));

  public static final RegistryObject<Item> WEAK_ZOMBIE_SPAWN_EGG = ITEMS
      .register("weak_zombie_spawn_egg", () -> new SpawnEggItem(ModEntityTypes.weakZombie, 0x000000,
          0xFFFFFF, new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_MISC)));

  public static final RegistryObject<Item> POLICE_ZOMBIE_SPAWN_EGG = ITEMS
      .register("police_zombie_spawn_egg", () -> new SpawnEggItem(ModEntityTypes.policeZombie,
          0x000000, 0xFFFFFF, new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_MISC)));

  public static final RegistryObject<Item> DOCTOR_ZOMBIE_SPAWN_EGG = ITEMS
      .register("doctor_zombie_spawn_egg", () -> new SpawnEggItem(ModEntityTypes.doctorZombie,
          0x000000, 0xFFFFFF, new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_MISC)));

  // ================================================================================
  // Miscellaneous
  // ================================================================================

  public static final RegistryObject<Item> BINOCULARS = ITEMS
      .register("binoculars",
          () -> new BinocularsItem(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_MISC)));

  public static final RegistryObject<Item> SMALL_BARREL = ITEMS
      .register("small_barrel",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_MISC)));

  public static final RegistryObject<Item> MEDIUM_BARREL = ITEMS
      .register("medium_barrel",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_MISC)));

  public static final RegistryObject<Item> HEAVY_BARREL = ITEMS
      .register("heavy_barrel",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_MISC)));

  public static final RegistryObject<Item> SMALL_BODY = ITEMS
      .register("small_body",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_MISC)));

  public static final RegistryObject<Item> MEDIUM_BODY = ITEMS
      .register("medium_body",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_MISC)));

  public static final RegistryObject<Item> HEAVY_BODY = ITEMS
      .register("heavy_body",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_MISC)));

  public static final RegistryObject<Item> SMALL_HANDLE = ITEMS
      .register("small_handle",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_MISC)));

  public static final RegistryObject<Item> MEDIUM_HANDLE = ITEMS
      .register("medium_handle",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_MISC)));

  public static final RegistryObject<Item> HEAVY_HANDLE = ITEMS
      .register("heavy_handle",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_MISC)));

  public static final RegistryObject<Item> SMALL_STOCK = ITEMS
      .register("small_stock",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_MISC)));

  public static final RegistryObject<Item> MEDIUM_STOCK = ITEMS
      .register("medium_stock",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_MISC)));

  public static final RegistryObject<Item> MEDIUM_BOLT = ITEMS
      .register("medium_bolt",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_MISC)));

  public static final RegistryObject<Item> HEAVY_BOLT = ITEMS
      .register("heavy_bolt",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_MISC)));
}
