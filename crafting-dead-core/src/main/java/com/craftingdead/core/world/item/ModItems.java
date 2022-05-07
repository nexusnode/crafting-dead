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

package com.craftingdead.core.world.item;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.world.action.ActionTypes;
import com.craftingdead.core.world.entity.grenade.C4Explosive;
import com.craftingdead.core.world.entity.grenade.DecoyGrenadeEntity;
import com.craftingdead.core.world.entity.grenade.FireGrenadeEntity;
import com.craftingdead.core.world.entity.grenade.FlashGrenadeEntity;
import com.craftingdead.core.world.entity.grenade.FragGrenade;
import com.craftingdead.core.world.entity.grenade.SmokeGrenadeEntity;
import com.craftingdead.core.world.item.clothing.Clothing;
import com.craftingdead.core.world.item.gun.GunTypes;
import com.craftingdead.core.world.item.gun.attachment.Attachments;
import com.craftingdead.core.world.item.gun.skin.Skins;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

  public static final DeferredRegister<Item> deferredRegister =
      DeferredRegister.create(ForgeRegistries.ITEMS, CraftingDead.ID);

  public static final CreativeModeTab COSMETICS_TAB =
      new CreativeModeTab("craftingdead.cosmetics") {

        @Override
        public ItemStack makeIcon() {
          return new ItemStack(BUILDER_CLOTHING.get());
        }
      };

  public static final CreativeModeTab COMBAT_TAB = new CreativeModeTab("craftingdead.combat") {

    @Override
    public ItemStack makeIcon() {
      return new ItemStack(AK47.get());
    }
  };

  public static final CreativeModeTab MEDICAL_TAB = new CreativeModeTab("craftingdead.medical") {

    @Override
    public ItemStack makeIcon() {
      return new ItemStack(FIRST_AID_KIT.get());
    }
  };

  // ================================================================================
  // Paints
  // ================================================================================

  public static final RegistryObject<Item> VULCAN_PAINT =
      deferredRegister.register("vulcan_paint",
          () -> new PaintItem(Skins.VULCAN, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> ASMO_PAINT =
      deferredRegister.register("asmo_paint",
          () -> new PaintItem(Skins.ASMO, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> CANDY_APPLE_PAINT =
      deferredRegister.register("candy_apple_paint",
          () -> new PaintItem(Skins.CANDY_APPLE, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> CYREX_PAINT =
      deferredRegister.register("cyrex_paint",
          () -> new PaintItem(Skins.CYREX, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> DIAMOND_PAINT =
      deferredRegister.register("diamond_paint",
          () -> new PaintItem(Skins.DIAMOND, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> DRAGON_PAINT =
      deferredRegister.register("dragon_paint",
          () -> new PaintItem(Skins.DRAGON, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> FADE_PAINT =
      deferredRegister.register("fade_paint",
          () -> new PaintItem(Skins.FADE, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> FURY_PAINT =
      deferredRegister.register("fury_paint",
          () -> new PaintItem(Skins.FURY, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> GEM_PAINT =
      deferredRegister.register("gem_paint",
          () -> new PaintItem(Skins.GEM, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> INFERNO_PAINT =
      deferredRegister.register("inferno_paint",
          () -> new PaintItem(Skins.INFERNO, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> RUBY_PAINT =
      deferredRegister.register("ruby_paint",
          () -> new PaintItem(Skins.RUBY, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> SCORCHED_PAINT =
      deferredRegister.register("scorched_paint",
          () -> new PaintItem(Skins.SCORCHED, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> SLAUGHTER_PAINT =
      deferredRegister.register("slaughter_paint",
          () -> new PaintItem(Skins.SLAUGHTER, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> UV_PAINT =
      deferredRegister.register("uv_paint",
          () -> new PaintItem(Skins.UV, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> HYPER_BEAST_PAINT =
      deferredRegister.register("hyper_beast_paint",
          () -> new PaintItem(Skins.HYPER_BEAST, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> EMPEROR_DRAGON_PAINT =
      deferredRegister.register("emperor_dragon_paint",
          () -> new PaintItem(Skins.EMPEROR_DRAGON, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> NUCLEAR_WINTER_PAINT =
      deferredRegister.register("nuclear_winter_paint",
          () -> new PaintItem(Skins.NUCLEAR_WINTER, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> MONARCH_PAINT =
      deferredRegister.register("monarch_paint",
          () -> new PaintItem(Skins.MONARCH, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> LOVELACE_PAINT =
      deferredRegister.register("lovelace_paint",
          () -> new PaintItem(Skins.LOVELACE, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));
  // ================================================================================
  // Magazines
  // ================================================================================

  public static final RegistryObject<MagazineItem> STANAG_BOX_MAGAZINE =
      deferredRegister.register("stanag_box_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(85)
              .setArmorPenetration(0.4F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> STANAG_DRUM_MAGAZINE =
      deferredRegister.register("stanag_drum_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(45)
              .setArmorPenetration(0.4F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> STANAG_30_ROUND_MAGAZINE =
      deferredRegister.register("stanag_30_round_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .setArmorPenetration(0.4F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> STANAG_20_ROUND_MAGAZINE =
      deferredRegister.register("stanag_20_round_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(20)
              .setArmorPenetration(0.4F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> MPT55_MAGAZINE =
      deferredRegister.register("mpt55_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .setArmorPenetration(0.4F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> AK47_30_ROUND_MAGAZINE =
      deferredRegister.register("ak47_30_round_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> FNFAL_MAGAZINE =
      deferredRegister.register("fnfal_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(20)
              .setArmorPenetration(0.55F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> ACR_MAGAZINE =
      deferredRegister.register("acr_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(20)
              .setArmorPenetration(0.5F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> G36C_MAGAZINE =
      deferredRegister.register("g36c_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .setArmorPenetration(0.45F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> HK417_MAGAZINE =
      deferredRegister.register("hk417_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .setArmorPenetration(0.47F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> M1911_MAGAZINE =
      deferredRegister.register("m1911_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(7)
              .setArmorPenetration(0.08F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> G18_MAGAZINE =
      deferredRegister.register("g18_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(20)
              .setArmorPenetration(0.08F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> M9_MAGAZINE =
      deferredRegister.register("m9_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(15)
              .setArmorPenetration(0.08F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> DESERT_EAGLE_MAGAZINE =
      deferredRegister.register("desert_eagle_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(13)
              .setArmorPenetration(0.35F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> P250_MAGAZINE =
      deferredRegister.register("p250_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(12)
              .setArmorPenetration(0.08F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> MAGNUM_MAGAZINE =
      deferredRegister.register("magnum_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(6)
              .setArmorPenetration(0.65F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> FN57_MAGAZINE =
      deferredRegister.register("fn57_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(20)
              .setArmorPenetration(0.09F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> P90_MAGAZINE =
      deferredRegister.register("p90_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(50)
              .setArmorPenetration(0.15F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> VECTOR_MAGAZINE =
      deferredRegister.register("vector_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .setArmorPenetration(0.15F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> MP5A5_35_ROUND_MAGAZINE =
      deferredRegister.register("mp5a5_35_round_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(35)
              .setArmorPenetration(0.15F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> MP5A5_21_ROUND_MAGAZINE =
      deferredRegister.register("mp5a5_21_round_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(21)
              .setArmorPenetration(0.15F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> MAC10_EXTENDED_MAGAZINE =
      deferredRegister.register("mac10_extended_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(45)
              .setArmorPenetration(0.15F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> MAC10_MAGAZINE =
      deferredRegister.register("mac10_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .setArmorPenetration(0.15F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> SPORTER22_MAGAZINE =
      deferredRegister.register("sporter22_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> M107_MAGAZINE =
      deferredRegister.register("m107_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(10)
              .setArmorPenetration(0.65F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> AS50_MAGAZINE =
      deferredRegister.register("as50_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(10)
              .setArmorPenetration(0.65F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> M1GARAND_MAGAZINE =
      deferredRegister.register("m1garand_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(8)
              .setArmorPenetration(0.95F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> AWP_MAGAZINE =
      deferredRegister.register("awp_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(10)
              .setArmorPenetration(0.95F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> TRENCH_GUN_SHELLS =
      deferredRegister.register("trench_gun_shells",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(6)
              .setArmorPenetration(0.35F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> MOSSBERG_SHELLS =
      deferredRegister.register("mossberg_shells",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(8)
              .setArmorPenetration(0.3F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> DMR_MAGAZINE =
      deferredRegister.register("dmr_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(5)
              .setArmorPenetration(0.65F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> TASER_CARTRIDGE =
      deferredRegister.register("taser_cartridge",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(3)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> M240B_MAGAZINE =
      deferredRegister.register("m240b_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(150)
              .setArmorPenetration(0.5F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));


  public static final RegistryObject<MagazineItem> RPK_DRUM_MAGAZINE =
      deferredRegister.register("rpk_drum_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(100)
              .setArmorPenetration(0.5F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> RPK_MAGAZINE =
      deferredRegister.register("rpk_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(60)
              .setArmorPenetration(0.5F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> MINIGUN_MAGAZINE =
      deferredRegister.register("minigun_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(350)
              .setArmorPenetration(0.3F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> MK48MOD_MAGAZINE =
      deferredRegister.register("mk48mod_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(150)
              .setArmorPenetration(0.52F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  // ================================================================================
  // Attachments
  // ================================================================================

  public static final RegistryObject<AttachmentItem> RED_DOT_SIGHT =
      deferredRegister.register("red_dot_sight",
          () -> new AttachmentItem(Attachments.RED_DOT_SIGHT, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<AttachmentItem> ACOG_SIGHT =
      deferredRegister.register("acog_sight",
          () -> new AttachmentItem(Attachments.ACOG_SIGHT, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<AttachmentItem> LP_SCOPE =
      deferredRegister.register("lp_scope",
          () -> new AttachmentItem(Attachments.LP_SCOPE, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<AttachmentItem> HP_SCOPE =
      deferredRegister.register("hp_scope",
          () -> new AttachmentItem(Attachments.HP_SCOPE, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<AttachmentItem> SUPPRESSOR =
      deferredRegister.register("suppressor",
          () -> new AttachmentItem(Attachments.SUPPRESSOR, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<AttachmentItem> TACTICAL_GRIP =
      deferredRegister.register("tactical_grip",
          () -> new AttachmentItem(Attachments.TACTICAL_GRIP, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<AttachmentItem> BIPOD =
      deferredRegister.register("bipod",
          () -> new AttachmentItem(Attachments.BIPOD, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<AttachmentItem> EOTECH_SIGHT =
      deferredRegister.register("eotech_sight",
          () -> new AttachmentItem(Attachments.EOTECH_SIGHT, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  // ================================================================================
  // Assault Rifles
  // ================================================================================

  public static final RegistryObject<GunItem> M4A1 =
      deferredRegister.register("m4a1", () -> new GunItem(GunTypes.M4A1));

  public static final RegistryObject<GunItem> SCARL =
      deferredRegister.register("scarl", () -> new GunItem(GunTypes.SCARL));

  public static final RegistryObject<GunItem> AK47 =
      deferredRegister.register("ak47", () -> new GunItem(GunTypes.AK47));

  public static final RegistryObject<GunItem> FNFAL =
      deferredRegister.register("fnfal", () -> new GunItem(GunTypes.FNFAL));

  public static final RegistryObject<GunItem> ACR =
      deferredRegister.register("acr", () -> new GunItem(GunTypes.ACR));

  public static final RegistryObject<GunItem> HK417 =
      deferredRegister.register("hk417", () -> new GunItem(GunTypes.HK417));

  public static final RegistryObject<GunItem> MPT55 =
      deferredRegister.register("mpt55", () -> new GunItem(GunTypes.MPT55));

  public static final RegistryObject<GunItem> M1GARAND =
      deferredRegister.register("m1garand", () -> new GunItem(GunTypes.M1GARAND));

  public static final RegistryObject<GunItem> SPORTER22 =
      deferredRegister.register("sporter22", () -> new GunItem(GunTypes.SPORTER22));

  public static final RegistryObject<GunItem> G36C =
      deferredRegister.register("g36c", () -> new GunItem(GunTypes.G36C));

  // ================================================================================
  // Machine Guns
  // ================================================================================

  public static final RegistryObject<GunItem> M240B =
      deferredRegister.register("m240b", () -> new GunItem(GunTypes.M240B));

  public static final RegistryObject<GunItem> RPK =
      deferredRegister.register("rpk", () -> new GunItem(GunTypes.RPK));

  public static final RegistryObject<GunItem> MINIGUN =
      deferredRegister.register("minigun", () -> new GunItem(GunTypes.MINIGUN));

  public static final RegistryObject<GunItem> MK48MOD =
      deferredRegister.register("mk48mod", () -> new GunItem(GunTypes.MK48MOD));

  // ================================================================================
  // Pistols
  // ================================================================================

  public static final RegistryObject<GunItem> TASER =
      deferredRegister.register("taser", () -> new GunItem(GunTypes.TASER));

  public static final RegistryObject<GunItem> M1911 =
      deferredRegister.register("m1911", () -> new GunItem(GunTypes.M1911));

  public static final RegistryObject<GunItem> G18 =
      deferredRegister.register("g18", () -> new GunItem(GunTypes.G18));

  public static final RegistryObject<GunItem> M9 =
      deferredRegister.register("m9", () -> new GunItem(GunTypes.M9));

  public static final RegistryObject<GunItem> DESERT_EAGLE =
      deferredRegister.register("desert_eagle", () -> new GunItem(GunTypes.DESERT_EAGLE));

  public static final RegistryObject<GunItem> P250 =
      deferredRegister.register("p250", () -> new GunItem(GunTypes.P250));

  public static final RegistryObject<GunItem> MAGNUM =
      deferredRegister.register("magnum", () -> new GunItem(GunTypes.MAGNUM));

  public static final RegistryObject<GunItem> FN57 =
      deferredRegister.register("fn57", () -> new GunItem(GunTypes.FN57));

  // ================================================================================
  // Submachine Guns
  // ================================================================================

  public static final RegistryObject<GunItem> MAC10 =
      deferredRegister.register("mac10", () -> new GunItem(GunTypes.MAC10));

  public static final RegistryObject<GunItem> P90 =
      deferredRegister.register("p90", () -> new GunItem(GunTypes.P90));

  public static final RegistryObject<GunItem> VECTOR =
      deferredRegister.register("vector", () -> new GunItem(GunTypes.VECTOR));

  public static final RegistryObject<GunItem> MP5A5 =
      deferredRegister.register("mp5a5", () -> new GunItem(GunTypes.MP5A5));

  // ================================================================================
  // Sniper Rifles
  // ================================================================================

  public static final RegistryObject<GunItem> M107 =
      deferredRegister.register("m107", () -> new GunItem(GunTypes.M107));

  public static final RegistryObject<GunItem> AS50 =
      deferredRegister.register("as50", () -> new GunItem(GunTypes.AS50));

  public static final RegistryObject<GunItem> AWP =
      deferredRegister.register("awp", () -> new GunItem(GunTypes.AWP));

  public static final RegistryObject<GunItem> DMR =
      deferredRegister.register("dmr", () -> new GunItem(GunTypes.DMR));

  // ================================================================================
  // Shotguns
  // ================================================================================

  public static final RegistryObject<GunItem> TRENCH_GUN =
      deferredRegister.register("trench_gun", () -> new GunItem(GunTypes.TRENCH_GUN));

  public static final RegistryObject<GunItem> MOSSBERG =
      deferredRegister.register("mossberg", () -> new GunItem(GunTypes.MOSSBERG));

  // ================================================================================
  // Grenades
  // ================================================================================

  public static final RegistryObject<GrenadeItem> FIRE_GRENADE =
      deferredRegister.register("fire_grenade",
          () -> new GrenadeItem((GrenadeItem.Properties) new GrenadeItem.Properties()
              .setGrenadeEntitySupplier(FireGrenadeEntity::new)
              .setEnabledSupplier(CraftingDead.serverConfig.explosivesFireGrenadeEnabled::get)
              .stacksTo(3)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<GrenadeItem> SMOKE_GRENADE =
      deferredRegister.register("smoke_grenade",
          () -> new GrenadeItem((GrenadeItem.Properties) new GrenadeItem.Properties()
              .setGrenadeEntitySupplier(SmokeGrenadeEntity::new)
              .setEnabledSupplier(CraftingDead.serverConfig.explosivesSmokeGrenadeEnabled::get)
              .stacksTo(3)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<GrenadeItem> FLASH_GRENADE =
      deferredRegister.register("flash_grenade",
          () -> new GrenadeItem((GrenadeItem.Properties) new GrenadeItem.Properties()
              .setGrenadeEntitySupplier(FlashGrenadeEntity::new)
              .setEnabledSupplier(CraftingDead.serverConfig.explosivesFlashGrenadeEnabled::get)
              .stacksTo(3)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<GrenadeItem> DECOY_GRENADE =
      deferredRegister.register("decoy_grenade",
          () -> new GrenadeItem((GrenadeItem.Properties) new GrenadeItem.Properties()
              .setGrenadeEntitySupplier(DecoyGrenadeEntity::new)
              .setEnabledSupplier(CraftingDead.serverConfig.explosivesDecoyGrenadeEnabled::get)
              .stacksTo(3)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<GrenadeItem> FRAG_GRENADE =
      deferredRegister.register("frag_grenade",
          () -> new GrenadeItem((GrenadeItem.Properties) new GrenadeItem.Properties()
              .setGrenadeEntitySupplier(FragGrenade::new)
              .setEnabledSupplier(CraftingDead.serverConfig.explosivesFragGrenadeEnabled::get)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<GrenadeItem> C4 = deferredRegister.register("c4_explosive",
      () -> new GrenadeItem((GrenadeItem.Properties) new GrenadeItem.Properties()
          .setGrenadeEntitySupplier(C4Explosive::new)
          .setEnabledSupplier(CraftingDead.serverConfig.explosivesC4Enabled::get)
          .setThrowSpeed(0.75F)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> REMOTE_DETONATOR =
      deferredRegister.register("remote_detonator",
          () -> new RemoteDetonatorItem(new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  // ================================================================================
  // Weapon
  // ================================================================================

  public static final RegistryObject<Item> CROWBAR = deferredRegister.register("crowbar",
      () -> new MeleeWeaponItem(7, -2.4F, new Item.Properties()
          .durability(100)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> BAT = deferredRegister.register("bat",
      () -> new MeleeWeaponItem(5, -2.4F, new Item.Properties()
          .durability(55)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> KATANA = deferredRegister.register("katana",
      () -> new MeleeWeaponItem(18, -2.4F, new Item.Properties()
          .durability(40)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> PIPE = deferredRegister.register("pipe",
      () -> new MeleeWeaponItem(9, -2.4F, new Item.Properties()
          .durability(60)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> RUSTY_PIPE = deferredRegister.register("rusty_pipe",
      () -> new MeleeWeaponItem(9, -2.4F, new Item.Properties()
          .durability(20)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> FIRE_AXE = deferredRegister.register("fire_axe",
      () -> new AxeItem(Tiers.IRON, 14, -2.4F, new Item.Properties()
          .durability(100)
          .tab((COMBAT_TAB))));

  public static final RegistryObject<Item> CHAINSAW = deferredRegister.register("chainsaw",
      () -> new MeleeWeaponItem(8, -2.4F, new Item.Properties()
          .durability(75)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> BOWIE_KNIFE = deferredRegister.register("bowie_knife",
      () -> new MeleeWeaponItem(15, -2.4F, new Item.Properties()
          .durability(20)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> GOLF_CLUB = deferredRegister.register("golf_club",
      () -> new MeleeWeaponItem(6, -2.4F, new Item.Properties()
          .durability(40)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> NIGHT_STICK = deferredRegister.register("night_stick",
      () -> new MeleeWeaponItem(4, -2.4F, new Item.Properties()
          .durability(70)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> SLEDGEHAMMER = deferredRegister.register("sledgehammer",
      () -> new PickaxeItem(Tiers.IRON, 10, -2.4F,
          new Item.Properties()
              .durability(110)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> NAIL_BAT = deferredRegister.register("nail_bat",
      () -> new MeleeWeaponItem(8, -2.4F, new Item.Properties()
          .durability(55)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> SHOVEL = deferredRegister.register("shovel",
      () -> new ShovelItem(Tiers.IRON, 8, -2.4F, new Item.Properties()
          .durability(70)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> HATCHET = deferredRegister.register("hatchet",
      () -> new AxeItem(Tiers.IRON, 16, -2.4F, new Item.Properties()
          .durability(40)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> BROADSWORD = deferredRegister.register("broadsword",
      () -> new MeleeWeaponItem(14, -2.4F, new Item.Properties()
          .durability(55)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> MACHETE = deferredRegister.register("machete",
      () -> new MeleeWeaponItem(12, -2.4F, new Item.Properties()
          .durability(70)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> WEAPONIZED_SCYTHE =
      deferredRegister.register("weaponized_scythe",
          () -> new MeleeWeaponItem(15, -2.4F, new Item.Properties()
              .durability(40)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> SCYTHE = deferredRegister.register("scythe",
      () -> new MeleeWeaponItem(20, -2.4F, new Item.Properties()
          .durability(20)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> PICKAXE = deferredRegister.register("pickaxe",
      () -> new PickaxeItem(Tiers.IRON, 10, -2.4F, new Item.Properties()
          .durability(210)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> BO_STAFF = deferredRegister.register("bo_staff",
      () -> new MeleeWeaponItem(4, -2.4F, new Item.Properties()
          .durability(70)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> WRENCH = deferredRegister.register("wrench",
      () -> new MeleeWeaponItem(8, -2.4F, new Item.Properties()
          .durability(120)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> FRYING_PAN = deferredRegister.register("frying_pan",
      () -> new MeleeWeaponItem(6, -2.4F, new Item.Properties()
          .durability(80)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> BOLT_CUTTERS = deferredRegister.register("bolt_cutters",
      () -> new BoltCuttersItem(40, 9, -2.4F, new Item.Properties()
          .durability(50)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> COMBAT_KNIFE = deferredRegister.register("combat_knife",
      () -> new MeleeWeaponItem(14, -2.4F, new Item.Properties()
          .durability(100)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> STEEL_BAT = deferredRegister.register("steel_bat",
      () -> new MeleeWeaponItem(7, -2.4F, new Item.Properties()
          .durability(180)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> CLEAVER = deferredRegister.register("cleaver",
      () -> new MeleeWeaponItem(10, -2.4F, new Item.Properties()
          .durability(80)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> BROKEN_BOTTLE =
      deferredRegister.register("broken_bottle",
          () -> new MeleeWeaponItem(15, -2.4F, new Item.Properties()
              .durability(10)
              .tab(COMBAT_TAB)));

  // ================================================================================
  // Vests
  // ================================================================================

  public static final RegistryObject<Item> BLACK_TACTICAL_VEST =
      deferredRegister.register("black_tactical_vest", () -> new StorageItem(StorageItem.VEST,
          new Item.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> GHILLIE_TACTICAL_VEST =
      deferredRegister.register("ghillie_tactical_vest", () -> new StorageItem(StorageItem.VEST,
          new Item.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> GREEN_TACTICAL_VEST =
      deferredRegister.register("green_tactical_vest", () -> new StorageItem(StorageItem.VEST,
          new Item.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> GREY_TACTICAL_VEST =
      deferredRegister.register("grey_tactical_vest", () -> new StorageItem(StorageItem.VEST,
          new Item.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> RIOT_VEST =
      deferredRegister.register("riot_vest", () -> new StorageItem(StorageItem.VEST,
          new Item.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> TAN_TACTICAL_VEST =
      deferredRegister.register("tan_tactical_vest", () -> new StorageItem(StorageItem.VEST,
          new Item.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  // ================================================================================
  // Hats, Helmets and Masks
  // ================================================================================

  public static final RegistryObject<Item> ARMY_HELMET = deferredRegister.register("army_helmet",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .setHeadshotReductionPercentage(0.2F)
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> BEANIE_HAT = deferredRegister.register("beanie_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> BLACK_BALLISTIC_HAT =
      deferredRegister.register("black_ballistic_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> FIREMAN_CHIEF_HAT =
      deferredRegister.register("chief_fireman_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> BLUE_HARD_HAT =
      deferredRegister.register("blue_hard_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> BUNNY_HAT = deferredRegister.register("bunny_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> CAMO_HELMET = deferredRegister.register("camo_helmet",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .setHeadshotReductionPercentage(0.2F)
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> CLONE_HAT = deferredRegister.register("clone_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .setHeadshotReductionPercentage(0.2F)
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> COMBAT_BDU_HELMET =
      deferredRegister.register("combat_bdu_helmet",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> COOKIE_MASK = deferredRegister.register("cookie_mask",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> COW_MASK = deferredRegister.register("cow_mask",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> CREEPER_MASK = deferredRegister.register("creeper_mask",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> DEADPOOL_MASK =
      deferredRegister.register("deadpool_mask",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> DOCTOR_MASK = deferredRegister.register("doctor_mask",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> FIREMAN_HAT = deferredRegister.register("fireman_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> GAS_MASK = deferredRegister.register("gas_mask",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .setImmuneToFlashes(true)
          .setImmuneToGas(true)
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> GHILLIE_HAT = deferredRegister.register("ghillie_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> GREEN_ARMY_HELMET =
      deferredRegister.register("green_army_helmet",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> GREEN_BALLISTIC_HELMET =
      deferredRegister.register("green_ballistic_helmet",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> GREEN_HARD_HAT =
      deferredRegister.register("green_hard_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> GREY_ARMY_HELMET =
      deferredRegister.register("grey_army_helmet",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> HACKER_MASK = deferredRegister.register("hacker_mask",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> HAZMAT_HAT = deferredRegister.register("hazmat_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .setImmuneToFlashes(true)
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> JUGGERNAUT_HELMET =
      deferredRegister.register("juggernaut_helmet",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> KNIGHT_HAT = deferredRegister.register("knight_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> MILITARY_HAZMAT_HAT =
      deferredRegister.register("military_hazmat_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setImmuneToFlashes(true)
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> NINJA_HAT = deferredRegister.register("ninja_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> NV_GOGGLES_HAT =
      deferredRegister.register("nv_goggles_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setNightVision(true)
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> ORANGE_HARD_HAT =
      deferredRegister.register("orange_hard_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> PAYDAY_MASK = deferredRegister.register("payday_mask",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> PAYDAY2_MASK = deferredRegister.register("payday2_mask",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> PILOT_HELMET = deferredRegister.register("pilot_helmet",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> PUMPKIN_MASK = deferredRegister.register("pumpkin_mask",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> RADAR_CAP = deferredRegister.register("radar_cap",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> RIOT_HAT = deferredRegister.register("riot_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .setHeadshotReductionPercentage(0.2F)
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SANTA_HAT = deferredRegister.register("santa_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SCUBA_MASK = deferredRegister.register("scuba_mask",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SHEEP_MASK = deferredRegister.register("sheep_mask",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SKI_MASK = deferredRegister.register("ski_mask",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .setImmuneToFlashes(true)
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SPETSNAZ_HELMET =
      deferredRegister.register("spetsnaz_helmet",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> TOP_HAT = deferredRegister.register("top_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> TRAPPER_HAT = deferredRegister.register("trapper_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> USHANKA_HAT = deferredRegister.register("ushanka_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> WINTER_MILITARY_HELMET =
      deferredRegister.register("winter_military_helmet",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> YELLOW_HARD_HAT =
      deferredRegister.register("yellow_hard_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> ZOMBIE_MASK = deferredRegister.register("zombie_mask",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  // ================================================================================
  // Clothing
  // ================================================================================

  public static final RegistryObject<Item> ARMY_CLOTHING =
      deferredRegister.register("army_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  3,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SAS_CLOTHING =
      deferredRegister.register("sas_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  3,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SPETSNAZ_CLOTHING =
      deferredRegister.register("spetsnaz_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  3,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> POLICE_CLOTHING =
      deferredRegister.register("police_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  2,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> CAMO_CLOTHING =
      deferredRegister.register("camo_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  3,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> COMBAT_BDU_CLOTHING =
      deferredRegister.register("combat_bdu_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  3,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> WINTER_ARMY_CLOTHING =
      deferredRegister.register("winter_army_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  3,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> ARMY_DESERT_CLOTHING =
      deferredRegister.register("army_desert_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  3,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> PILOT_CLOTHING =
      deferredRegister.register("pilot_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  1,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> HAZMAT_CLOTHING =
      deferredRegister.register("hazmat_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  2,
                  AttributeModifier.Operation.ADDITION))
              .setFireImmunity(true)
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> TAC_GHILLIE_CLOTHING =
      deferredRegister.register("tac_ghillie_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  2,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SWAT_CLOTHING =
      deferredRegister.register("swat_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  3,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SPACE_SUIT_CLOTHING =
      deferredRegister.register("space_suit_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  3,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SHERIFF_CLOTHING =
      deferredRegister.register("sheriff_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  1,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> JUGGERNAUT_CLOTHING =
      deferredRegister.register("juggernaut_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  3,
                  AttributeModifier.Operation.ADDITION))
              .addAttributeModifier(Attributes.MOVEMENT_SPEED, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Slowness modifier",
                  -0.15D,
                  AttributeModifier.Operation.MULTIPLY_TOTAL))
              .setFireImmunity(true)
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> FIREMAN_CLOTHING =
      deferredRegister.register("fireman_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  3,
                  AttributeModifier.Operation.ADDITION))
              .setFireImmunity(true)
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> DOCTOR_CLOTHING =
      deferredRegister.register("doctor_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SMART_CLOTHING =
      deferredRegister.register("smart_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> CASUAL_GREEN_CLOTHING =
      deferredRegister.register("casual_green_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> BUILDER_CLOTHING =
      deferredRegister.register("builder_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> BUSINESS_CLOTHING =
      deferredRegister.register("business_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SEC_GUARD_CLOTHING =
      deferredRegister.register("sec_guard_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  1,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> MIL_HAZMAT_CLOTHING =
      deferredRegister.register("mil_hazmat_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  2,
                  AttributeModifier.Operation.ADDITION))
              .setFireImmunity(true)
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> FULL_GHILLIE_CLOTHING =
      deferredRegister.register("full_ghillie_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  2,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> RED_DUSK_CLOTHING =
      deferredRegister.register("red_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  2,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> CLONE_CLOTHING =
      deferredRegister.register("clone_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  2,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> COOKIE_CLOTHING =
      deferredRegister.register("cookie_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> DEADPOOL_CLOTHING =
      deferredRegister.register("deadpool_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  2,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> NINJA_CLOTHING =
      deferredRegister.register("ninja_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  1,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> ARMY_MEDIC_CLOTHING =
      deferredRegister.register("army_medic_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  3,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> BLUE_DUSK_CLOTHING =
      deferredRegister.register("blue_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  2,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> PRESIDENT_CLOTHING =
      deferredRegister.register("president_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> YELLOW_DUSK_CLOTHING =
      deferredRegister.register("yellow_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  2,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> ORANGE_DUSK_CLOTHING =
      deferredRegister.register("orange_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  2,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> GREEN_DUSK_CLOTHING =
      deferredRegister.register("green_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  2,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> WHITE_DUSK_CLOTHING =
      deferredRegister.register("white_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  2,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> PURPLE_DUSK_CLOTHING =
      deferredRegister.register("purple_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  2,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SCUBA_CLOTHING =
      deferredRegister.register("scuba_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  1,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> DDPAT_CLOTHING =
      deferredRegister.register("ddpat_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  2,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> CONTRACTOR_CLOTHING =
      deferredRegister.register("contractor_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  3,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  // ================================================================================
  // Gun Parts
  // ================================================================================

  public static final RegistryObject<Item> SMALL_BARREL = deferredRegister.register("small_barrel",
      () -> new Item(new Item.Properties().tab(COMBAT_TAB)));

  public static final RegistryObject<Item> MEDIUM_BARREL =
      deferredRegister.register("medium_barrel",
          () -> new Item(new Item.Properties().tab(COMBAT_TAB)));

  public static final RegistryObject<Item> HEAVY_BARREL = deferredRegister.register("heavy_barrel",
      () -> new Item(new Item.Properties().tab(COMBAT_TAB)));

  public static final RegistryObject<Item> SMALL_BODY = deferredRegister.register("small_body",
      () -> new Item(new Item.Properties().tab(COMBAT_TAB)));

  public static final RegistryObject<Item> MEDIUM_BODY = deferredRegister.register("medium_body",
      () -> new Item(new Item.Properties().tab(COMBAT_TAB)));

  public static final RegistryObject<Item> HEAVY_BODY = deferredRegister.register("heavy_body",
      () -> new Item(new Item.Properties().tab(COMBAT_TAB)));

  public static final RegistryObject<Item> SMALL_HANDLE = deferredRegister.register("small_handle",
      () -> new Item(new Item.Properties().tab(COMBAT_TAB)));

  public static final RegistryObject<Item> MEDIUM_HANDLE =
      deferredRegister.register("medium_handle",
          () -> new Item(new Item.Properties().tab(COMBAT_TAB)));

  public static final RegistryObject<Item> HEAVY_HANDLE = deferredRegister.register("heavy_handle",
      () -> new Item(new Item.Properties().tab(COMBAT_TAB)));

  public static final RegistryObject<Item> SMALL_STOCK = deferredRegister.register("small_stock",
      () -> new Item(new Item.Properties().tab(COMBAT_TAB)));

  public static final RegistryObject<Item> MEDIUM_STOCK = deferredRegister.register("medium_stock",
      () -> new Item(new Item.Properties().tab(COMBAT_TAB)));

  public static final RegistryObject<Item> MEDIUM_BOLT = deferredRegister.register("medium_bolt",
      () -> new Item(new Item.Properties().tab(COMBAT_TAB)));

  public static final RegistryObject<Item> HEAVY_BOLT = deferredRegister.register("heavy_bolt",
      () -> new Item(new Item.Properties().tab(COMBAT_TAB)));

  // ================================================================================
  // Miscellaneous
  // ================================================================================

  public static final RegistryObject<Item> BINOCULARS =
      deferredRegister.register("binoculars",
          () -> new BinocularsItem(new Item.Properties()
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> PARACHUTE =
      deferredRegister.register("parachute",
          () -> new ParachuteItem(new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<ActionItem> HANDCUFFS =
      deferredRegister.register("handcuffs",
          () -> new ActionItem(ActionTypes.APPLY_HANDCUFFS, new Item.Properties()
              .durability(200)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> HANDCUFFS_KEY =
      deferredRegister.register("handcuffs_key",
          () -> new HandcuffsKeyItem(new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  // ================================================================================
  // Medical
  // ================================================================================

  public static final RegistryObject<ActionItem> FIRST_AID_KIT =
      deferredRegister.register("first_aid_kit",
          () -> new ActionItem(ActionTypes.USE_FIRST_AID_KIT, new Item.Properties()
              .stacksTo(1)
              .tab(MEDICAL_TAB)));

  public static final RegistryObject<ActionItem> ADRENALINE_SYRINGE =
      deferredRegister.register("adrenaline_syringe",
          () -> new ActionItem(ActionTypes.USE_ADRENALINE_SYRINGE, new Item.Properties()
              .stacksTo(1)
              .tab(MEDICAL_TAB)));

  public static final RegistryObject<ActionItem> SYRINGE =
      deferredRegister.register("syringe",
          () -> new ActionItem(ActionTypes.USE_SYRINGE, new Item.Properties()
              .stacksTo(1)
              .tab(MEDICAL_TAB)));

  public static final RegistryObject<ActionItem> BLOOD_SYRINGE =
      deferredRegister.register("blood_syringe",
          () -> new ActionItem(ActionTypes.USE_BLOOD_SYRINGE, new Item.Properties()
              .stacksTo(1)
              .tab(MEDICAL_TAB)));

  public static final RegistryObject<ActionItem> BANDAGE =
      deferredRegister.register("bandage",
          () -> new ActionItem(ActionTypes.USE_BANDAGE, new Item.Properties()
              .stacksTo(1)
              .tab(MEDICAL_TAB)));

  // ================================================================================
  // Backpacks
  // ================================================================================

  public static final RegistryObject<Item> SMALL_RED_BACKPACK = deferredRegister
      .register("small_red_backpack", () -> new StorageItem(StorageItem.SMALL_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SMALL_ORANGE_BACKPACK = deferredRegister
      .register("small_orange_backpack", () -> new StorageItem(StorageItem.SMALL_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SMALL_YELLOW_BACKPACK = deferredRegister
      .register("small_yellow_backpack", () -> new StorageItem(StorageItem.SMALL_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SMALL_GREEN_BACKPACK = deferredRegister
      .register("small_green_backpack", () -> new StorageItem(StorageItem.SMALL_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SMALL_BLUE_BACKPACK = deferredRegister
      .register("small_blue_backpack", () -> new StorageItem(StorageItem.SMALL_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SMALL_PURPLE_BACKPACK = deferredRegister
      .register("small_purple_backpack", () -> new StorageItem(StorageItem.SMALL_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> MEDIUM_RED_BACKPACK = deferredRegister
      .register("medium_red_backpack", () -> new StorageItem(StorageItem.MEDIUM_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> MEDIUM_ORANGE_BACKPACK = deferredRegister
      .register("medium_orange_backpack", () -> new StorageItem(StorageItem.MEDIUM_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> MEDIUM_YELLOW_BACKPACK = deferredRegister
      .register("medium_yellow_backpack", () -> new StorageItem(StorageItem.MEDIUM_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> MEDIUM_GREEN_BACKPACK = deferredRegister
      .register("medium_green_backpack", () -> new StorageItem(StorageItem.MEDIUM_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> MEDIUM_BLUE_BACKPACK = deferredRegister
      .register("medium_blue_backpack", () -> new StorageItem(StorageItem.MEDIUM_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> MEDIUM_PURPLE_BACKPACK = deferredRegister
      .register("medium_purple_backpack", () -> new StorageItem(StorageItem.MEDIUM_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> MEDIUM_GREY_BACKPACK = deferredRegister
      .register("medium_grey_backpack", () -> new StorageItem(StorageItem.MEDIUM_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> MEDIUM_BLACK_BACKPACK = deferredRegister
      .register("medium_black_backpack", () -> new StorageItem(StorageItem.MEDIUM_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> MEDIUM_GHILLIE_BACKPACK = deferredRegister
      .register("medium_ghillie_backpack", () -> new StorageItem(StorageItem.MEDIUM_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> MEDIUM_WHITE_BACKPACK = deferredRegister
      .register("medium_white_backpack", () -> new StorageItem(StorageItem.MEDIUM_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> LARGE_GREY_BACKPACK = deferredRegister
      .register("large_grey_backpack", () -> new StorageItem(StorageItem.LARGE_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> LARGE_GREEN_BACKPACK = deferredRegister
      .register("large_green_backpack", () -> new StorageItem(StorageItem.LARGE_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> LARGE_TAN_BACKPACK = deferredRegister
      .register("large_tan_backpack", () -> new StorageItem(StorageItem.LARGE_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> LARGE_BLACK_BACKPACK = deferredRegister
      .register("large_black_backpack", () -> new StorageItem(StorageItem.LARGE_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> LARGE_GHILLIE_BACKPACK = deferredRegister
      .register("large_ghillie_backpack", () -> new StorageItem(StorageItem.LARGE_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> TAN_GUN_BAG = deferredRegister
      .register("tan_gun_bag", () -> new StorageItem(StorageItem.GUN_BAG,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> GREY_GUN_BAG = deferredRegister
      .register("grey_gun_bag", () -> new StorageItem(StorageItem.GUN_BAG,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  static {
    ArbitraryTooltips.registerTooltip(SCUBA_MASK,
        new TranslatableComponent("clothing_item.water_breathing")
            .withStyle(ChatFormatting.GRAY));
    ArbitraryTooltips.registerTooltip(SCUBA_CLOTHING,
        new TranslatableComponent("clothing_item.water_speed")
            .withStyle(ChatFormatting.GRAY));
  }
}
