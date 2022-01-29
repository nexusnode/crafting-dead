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

package com.craftingdead.core.world.item;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.animation.gun.InspectAnimation;
import com.craftingdead.core.client.animation.gun.PistolShootAnimation;
import com.craftingdead.core.client.animation.gun.ReloadAnimation;
import com.craftingdead.core.client.animation.gun.RifleShootAnimation;
import com.craftingdead.core.client.animation.gun.SubmachineShootAnimation;
import com.craftingdead.core.sounds.ModSoundEvents;
import com.craftingdead.core.world.action.ActionTypes;
import com.craftingdead.core.world.entity.grenade.C4Explosive;
import com.craftingdead.core.world.entity.grenade.DecoyGrenadeEntity;
import com.craftingdead.core.world.entity.grenade.FireGrenadeEntity;
import com.craftingdead.core.world.entity.grenade.FlashGrenadeEntity;
import com.craftingdead.core.world.entity.grenade.FragGrenade;
import com.craftingdead.core.world.entity.grenade.SmokeGrenadeEntity;
import com.craftingdead.core.world.item.clothing.Clothing;
import com.craftingdead.core.world.item.combatslot.CombatSlot;
import com.craftingdead.core.world.item.gun.FireMode;
import com.craftingdead.core.world.item.gun.Gun;
import com.craftingdead.core.world.item.gun.GunAnimationEvent;
import com.craftingdead.core.world.item.gun.GunItem;
import com.craftingdead.core.world.item.gun.aimable.AimableGunItem;
import com.craftingdead.core.world.item.gun.attachment.Attachments;
import com.craftingdead.core.world.item.gun.minigun.MinigunItem;
import com.craftingdead.core.world.item.gun.skin.Skins;
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

  public static final DeferredRegister<Item> ITEMS =
      DeferredRegister.create(ForgeRegistries.ITEMS, CraftingDead.ID);

  public static final CreativeModeTab COSMETICS_TAB =
      new CreativeModeTab("craftingdead.cosmetics") {

        @Override
        public ItemStack makeIcon() {
          return new ItemStack(ModItems.BUILDER_CLOTHING::get);
        }
      };

  public static final CreativeModeTab COMBAT_TAB = new CreativeModeTab("craftingdead.combat") {

    @Override
    public ItemStack makeIcon() {
      return new ItemStack(ModItems.AK47::get);
    }
  };

  public static final CreativeModeTab MEDICAL_TAB = new CreativeModeTab("craftingdead.medical") {

    @Override
    public ItemStack makeIcon() {
      return new ItemStack(ModItems.FIRST_AID_KIT::get);
    }
  };

  // ================================================================================
  // Paints
  // ================================================================================

  public static final RegistryObject<Item> VULCAN_PAINT =
      ITEMS.register("vulcan_paint",
          () -> new PaintItem(Skins.VULCAN, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> ASMO_PAINT =
      ITEMS.register("asmo_paint",
          () -> new PaintItem(Skins.ASMO, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> CANDY_APPLE_PAINT =
      ITEMS.register("candy_apple_paint",
          () -> new PaintItem(Skins.CANDY_APPLE, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> CYREX_PAINT =
      ITEMS.register("cyrex_paint",
          () -> new PaintItem(Skins.CYREX, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> DIAMOND_PAINT =
      ITEMS.register("diamond_paint",
          () -> new PaintItem(Skins.DIAMOND, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> DRAGON_PAINT =
      ITEMS.register("dragon_paint",
          () -> new PaintItem(Skins.DRAGON, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> FADE_PAINT =
      ITEMS.register("fade_paint",
          () -> new PaintItem(Skins.FADE, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> FURY_PAINT =
      ITEMS.register("fury_paint",
          () -> new PaintItem(Skins.FURY, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> GEM_PAINT =
      ITEMS.register("gem_paint",
          () -> new PaintItem(Skins.GEM, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> INFERNO_PAINT =
      ITEMS.register("inferno_paint",
          () -> new PaintItem(Skins.INFERNO, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> RUBY_PAINT =
      ITEMS.register("ruby_paint",
          () -> new PaintItem(Skins.RUBY, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> SCORCHED_PAINT =
      ITEMS.register("scorched_paint",
          () -> new PaintItem(Skins.SCORCHED, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> SLAUGHTER_PAINT =
      ITEMS.register("slaughter_paint",
          () -> new PaintItem(Skins.SLAUGHTER, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> UV_PAINT =
      ITEMS.register("uv_paint",
          () -> new PaintItem(Skins.UV, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> HYPER_BEAST_PAINT =
      ITEMS.register("hyper_beast_paint",
          () -> new PaintItem(Skins.HYPER_BEAST, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> EMPEROR_DRAGON_PAINT =
      ITEMS.register("emperor_dragon_paint",
          () -> new PaintItem(Skins.EMPEROR_DRAGON, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> NUCLEAR_WINTER_PAINT =
      ITEMS.register("nuclear_winter_paint",
          () -> new PaintItem(Skins.NUCLEAR_WINTER, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> MONARCH_PAINT =
      ITEMS.register("monarch_paint",
          () -> new PaintItem(Skins.MONARCH, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> LOVELACE_PAINT =
      ITEMS.register("lovelace_paint",
          () -> new PaintItem(Skins.LOVELACE, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));
  // ================================================================================
  // Magazines
  // ================================================================================

  public static final RegistryObject<MagazineItem> STANAG_BOX_MAGAZINE =
      ITEMS.register("stanag_box_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(85)
              .setArmorPenetration(0.4F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> STANAG_DRUM_MAGAZINE =
      ITEMS.register("stanag_drum_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(45)
              .setArmorPenetration(0.4F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> STANAG_30_ROUND_MAGAZINE =
      ITEMS.register("stanag_30_round_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .setArmorPenetration(0.4F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> STANAG_20_ROUND_MAGAZINE =
      ITEMS.register("stanag_20_round_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(20)
              .setArmorPenetration(0.4F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> MPT55_MAGAZINE =
      ITEMS.register("mpt55_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .setArmorPenetration(0.4F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> AK47_30_ROUND_MAGAZINE =
      ITEMS.register("ak47_30_round_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> FNFAL_MAGAZINE =
      ITEMS.register("fnfal_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(20)
              .setArmorPenetration(0.55F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> ACR_MAGAZINE =
      ITEMS.register("acr_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(20)
              .setArmorPenetration(0.5F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> G36C_MAGAZINE =
      ITEMS.register("g36c_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .setArmorPenetration(0.45F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> HK417_MAGAZINE =
      ITEMS.register("hk417_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .setArmorPenetration(0.47F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> M1911_MAGAZINE =
      ITEMS.register("m1911_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(7)
              .setArmorPenetration(0.08F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> G18_MAGAZINE =
      ITEMS.register("g18_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(20)
              .setArmorPenetration(0.08F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> M9_MAGAZINE =
      ITEMS.register("m9_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(15)
              .setArmorPenetration(0.08F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> DESERT_EAGLE_MAGAZINE =
      ITEMS.register("desert_eagle_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(13)
              .setArmorPenetration(0.35F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> P250_MAGAZINE =
      ITEMS.register("p250_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(12)
              .setArmorPenetration(0.08F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> MAGNUM_MAGAZINE =
      ITEMS.register("magnum_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(6)
              .setArmorPenetration(0.65F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> FN57_MAGAZINE =
      ITEMS.register("fn57_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(20)
              .setArmorPenetration(0.09F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> P90_MAGAZINE =
      ITEMS.register("p90_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(50)
              .setArmorPenetration(0.15F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> VECTOR_MAGAZINE =
      ITEMS.register("vector_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .setArmorPenetration(0.15F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> MP5A5_35_ROUND_MAGAZINE =
      ITEMS.register("mp5a5_35_round_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(35)
              .setArmorPenetration(0.15F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> MP5A5_21_ROUND_MAGAZINE =
      ITEMS.register("mp5a5_21_round_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(21)
              .setArmorPenetration(0.15F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> MAC10_EXTENDED_MAGAZINE =
      ITEMS.register("mac10_extended_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(45)
              .setArmorPenetration(0.15F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> MAC10_MAGAZINE =
      ITEMS.register("mac10_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .setArmorPenetration(0.15F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> SPORTER22_MAGAZINE =
      ITEMS.register("sporter22_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> M107_MAGAZINE =
      ITEMS.register("m107_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(10)
              .setArmorPenetration(0.65F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> AS50_MAGAZINE =
      ITEMS.register("as50_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(10)
              .setArmorPenetration(0.65F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> M1GARAND_MAGAZINE =
      ITEMS.register("m1garand_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(8)
              .setArmorPenetration(0.95F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> AWP_MAGAZINE =
      ITEMS.register("awp_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(10)
              .setArmorPenetration(0.95F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> TRENCH_GUN_SHELLS =
      ITEMS.register("trench_gun_shells",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(6)
              .setArmorPenetration(0.35F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> MOSSBERG_SHELLS =
      ITEMS.register("mossberg_shells",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(8)
              .setArmorPenetration(0.3F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> DMR_MAGAZINE =
      ITEMS.register("dmr_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(5)
              .setArmorPenetration(0.65F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> TASER_CARTRIDGE =
      ITEMS.register("taser_cartridge",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(3)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> M240B_MAGAZINE =
      ITEMS.register("m240b_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(150)
              .setArmorPenetration(0.5F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));


  public static final RegistryObject<MagazineItem> RPK_DRUM_MAGAZINE =
      ITEMS.register("rpk_drum_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(100)
              .setArmorPenetration(0.5F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> RPK_MAGAZINE =
      ITEMS.register("rpk_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(60)
              .setArmorPenetration(0.5F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> MINIGUN_MAGAZINE =
      ITEMS.register("minigun_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(350)
              .setArmorPenetration(0.3F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> MK48MOD_MAGAZINE =
      ITEMS.register("mk48mod_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(150)
              .setArmorPenetration(0.52F)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  // ================================================================================
  // Attachments
  // ================================================================================

  public static final RegistryObject<AttachmentItem> RED_DOT_SIGHT =
      ITEMS.register("red_dot_sight",
          () -> new AttachmentItem(Attachments.RED_DOT_SIGHT, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<AttachmentItem> ACOG_SIGHT =
      ITEMS.register("acog_sight",
          () -> new AttachmentItem(Attachments.ACOG_SIGHT, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<AttachmentItem> LP_SCOPE =
      ITEMS.register("lp_scope",
          () -> new AttachmentItem(Attachments.LP_SCOPE, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<AttachmentItem> HP_SCOPE =
      ITEMS.register("hp_scope",
          () -> new AttachmentItem(Attachments.HP_SCOPE, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<AttachmentItem> SUPPRESSOR =
      ITEMS.register("suppressor",
          () -> new AttachmentItem(Attachments.SUPPRESSOR, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<AttachmentItem> TACTICAL_GRIP =
      ITEMS.register("tactical_grip",
          () -> new AttachmentItem(Attachments.TACTICAL_GRIP, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<AttachmentItem> BIPOD =
      ITEMS.register("bipod",
          () -> new AttachmentItem(Attachments.BIPOD, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<AttachmentItem> EOTECH_SIGHT =
      ITEMS.register("eotech_sight",
          () -> new AttachmentItem(Attachments.EOTECH_SIGHT, new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  // ================================================================================
  // Assault Rifles
  // ================================================================================

  public static final RegistryObject<GunItem> M4A1 =
      ITEMS.register("m4a1",
          () -> AimableGunItem.builder()
              .setFireDelayMs(100)
              .setDamage(7)
              .setReloadDurationTicks(20 * 4)
              .setAccuracy(0.9F)
              .setRecoil(3.5F)
              .setRange(160.0D)
              .addFireMode(FireMode.AUTO)
              .addFireMode(FireMode.BURST)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.M4A1_SHOOT)
              .setDistantShootSound(ModSoundEvents.M4A1_DISTANT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.M4A1_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, RifleShootAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.STANAG_20_ROUND_MAGAZINE)
              .addAcceptedMagazine(ModItems.STANAG_30_ROUND_MAGAZINE)
              .addAcceptedMagazine(ModItems.STANAG_DRUM_MAGAZINE)
              .addAcceptedMagazine(ModItems.STANAG_BOX_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.ACOG_SIGHT)
              .addAcceptedAttachment(Attachments.LP_SCOPE)
              .addAcceptedAttachment(Attachments.HP_SCOPE)
              .addAcceptedAttachment(Attachments.TACTICAL_GRIP)
              .addAcceptedAttachment(Attachments.BIPOD)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .addAcceptedAttachment(Attachments.EOTECH_SIGHT)
              .build());

  public static final RegistryObject<GunItem> SCARL =
      ITEMS.register("scarl",
          () -> AimableGunItem.builder()
              .setFireDelayMs(110)
              .setDamage(6)
              .setReloadDurationTicks(20 * 4)
              .setAccuracy(0.87F)
              .setRecoil(4.25F)
              .setRange(300.0D)
              .addFireMode(FireMode.AUTO)
              .addFireMode(FireMode.BURST)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.SCARL_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.M4A1_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, RifleShootAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.STANAG_20_ROUND_MAGAZINE)
              .addAcceptedMagazine(ModItems.STANAG_30_ROUND_MAGAZINE)
              .addAcceptedMagazine(ModItems.STANAG_DRUM_MAGAZINE)
              .addAcceptedMagazine(ModItems.STANAG_BOX_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.ACOG_SIGHT)
              .addAcceptedAttachment(Attachments.LP_SCOPE)
              .addAcceptedAttachment(Attachments.HP_SCOPE)
              .addAcceptedAttachment(Attachments.TACTICAL_GRIP)
              .addAcceptedAttachment(Attachments.BIPOD)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .addAcceptedAttachment(Attachments.EOTECH_SIGHT)
              .build());

  public static final RegistryObject<GunItem> AK47 =
      ITEMS.register("ak47",
          () -> AimableGunItem.builder()
              .setFireDelayMs(100)
              .setDamage(7)
              .setReloadDurationTicks(20 * 4)
              .setAccuracy(0.83F)
              .setRecoil(4.5F)
              .setRange(125.0D)
              .addFireMode(FireMode.AUTO)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.AK47_SHOOT)
              .setDistantShootSound(ModSoundEvents.AK47_DISTANT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_AK47_SHOOT)
              .setReloadSound(ModSoundEvents.AK47_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, RifleShootAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.AK47_30_ROUND_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.ACOG_SIGHT)
              .addAcceptedAttachment(Attachments.LP_SCOPE)
              .addAcceptedAttachment(Attachments.HP_SCOPE)
              .addAcceptedAttachment(Attachments.TACTICAL_GRIP)
              .addAcceptedAttachment(Attachments.BIPOD)
              .addAcceptedAttachment(Attachments.EOTECH_SIGHT)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .build());

  public static final RegistryObject<GunItem> FNFAL =
      ITEMS.register("fnfal",
          () -> AimableGunItem.builder()
              .setFireDelayMs(80)
              .setDamage(9)
              .setReloadDurationTicks(20 * 4)
              .setAccuracy(0.80F)
              .setRecoil(4.0F)
              .setRange(100.0D)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.FNFAL_SHOOT)
              .setDistantShootSound(ModSoundEvents.FNFAL_DISTANT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_RPK_SHOOT)
              .setReloadSound(ModSoundEvents.M4A1_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, RifleShootAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.FNFAL_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.ACOG_SIGHT)
              .addAcceptedAttachment(Attachments.LP_SCOPE)
              .addAcceptedAttachment(Attachments.HP_SCOPE)
              .addAcceptedAttachment(Attachments.TACTICAL_GRIP)
              .addAcceptedAttachment(Attachments.BIPOD)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .build());

  public static final RegistryObject<GunItem> ACR =
      ITEMS.register("acr",
          () -> AimableGunItem.builder()
              .setFireDelayMs(92)
              .setDamage(7)
              .setReloadDurationTicks(20 * 4)
              .setAccuracy(0.8F)
              .setRecoil(3.25F)
              .setRange(150.0D)
              .addFireMode(FireMode.AUTO)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.ACR_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.M4A1_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, RifleShootAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.ACR_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .addAcceptedAttachment(Attachments.LP_SCOPE)
              .addAcceptedAttachment(Attachments.HP_SCOPE)
              .addAcceptedAttachment(Attachments.TACTICAL_GRIP)
              .addAcceptedAttachment(Attachments.EOTECH_SIGHT)
              .build());

  public static final RegistryObject<GunItem> HK417 =
      ITEMS.register("hk417",
          () -> AimableGunItem.builder()
              .setFireDelayMs(100)
              .setDamage(8)
              .setReloadDurationTicks((int) (20 * 2.1F))
              .setAccuracy(0.85F)
              .setRecoil(3.5F)
              .setRange(130.0D)
              .addFireMode(FireMode.AUTO)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.HK417_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_RPK_SHOOT)
              .setReloadSound(ModSoundEvents.M4A1_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, RifleShootAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.HK417_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.TACTICAL_GRIP)
              .addAcceptedAttachment(Attachments.ACOG_SIGHT)
              .addAcceptedAttachment(Attachments.LP_SCOPE)
              .addAcceptedAttachment(Attachments.HP_SCOPE)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .addAcceptedAttachment(Attachments.EOTECH_SIGHT)
              .build());

  public static final RegistryObject<GunItem> MPT55 =
      ITEMS.register("mpt55",
          () -> AimableGunItem.builder()
              .setFireDelayMs(70)
              .setDamage(6)
              .setReloadDurationTicks(20 * 2)
              .setAccuracy(0.8F)
              .setRecoil(2.0F)
              .setRange(180.0D)
              .addFireMode(FireMode.BURST)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.MPT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.M4A1_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, RifleShootAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.MPT55_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.ACOG_SIGHT)
              .addAcceptedAttachment(Attachments.LP_SCOPE)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .build());

  public static final RegistryObject<GunItem> M1GARAND =
      ITEMS.register("m1garand",
          () -> AimableGunItem.builder()
              .setFireDelayMs(170)
              .setDamage(10)
              .setReloadDurationTicks(20 * 2)
              .setAccuracy(0.92F)
              .setRecoil(7.0F)
              .setRange(200.0D)
              .setCrosshair(false)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.M1GARAND_SHOOT)
              .setReloadSound(ModSoundEvents.AS50_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, RifleShootAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.M1GARAND_MAGAZINE)
              .addAcceptedAttachment(Attachments.LP_SCOPE)
              .addAcceptedAttachment(Attachments.HP_SCOPE)
              .addAcceptedAttachment(Attachments.BIPOD)
              .build());

  public static final RegistryObject<GunItem> SPORTER22 =
      ITEMS.register("sporter22",
          () -> AimableGunItem.builder()
              .setFireDelayMs(200)
              .setDamage(7)
              .setReloadDurationTicks((int) (20 * 1.5F))
              .setAccuracy(0.7F)
              .setRecoil(2.5F)
              .setCrosshair(false)
              .setRange(60.0D)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.SPORTER22_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_RPK_SHOOT)
              .setReloadSound(ModSoundEvents.M107_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, RifleShootAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.SPORTER22_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.ACOG_SIGHT)
              .addAcceptedAttachment(Attachments.LP_SCOPE)
              .addAcceptedAttachment(Attachments.HP_SCOPE)
              .addAcceptedAttachment(Attachments.TACTICAL_GRIP)
              .addAcceptedAttachment(Attachments.BIPOD)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .build());

  public static final RegistryObject<GunItem> G36C =
      ITEMS.register("g36c",
          () -> AimableGunItem.builder()
              .setFireDelayMs(92)
              .setDamage(8)
              .setReloadDurationTicks((int) (20 * 2.2F))
              .setAccuracy(0.91F)
              .setRecoil(2.0F)
              .setRange(80.0D)
              .addFireMode(FireMode.AUTO)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.G36C_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.M4A1_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, RifleShootAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.G36C_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.TACTICAL_GRIP)
              .addAcceptedAttachment(Attachments.ACOG_SIGHT)
              .addAcceptedAttachment(Attachments.EOTECH_SIGHT)
              .build());

  // ================================================================================
  // Machine Guns
  // ================================================================================

  public static final RegistryObject<GunItem> M240B =
      ITEMS.register("m240b",
          () -> AimableGunItem.builder()
              .setFireDelayMs(85)
              .setDamage(8)
              .setReloadDurationTicks(20 * 4)
              .setAccuracy(0.79F)
              .setRecoil(6.0F)
              .setRange(260.0D)
              .addFireMode(FireMode.AUTO)
              .setShootSound(ModSoundEvents.M240B_SHOOT)
              .setDistantShootSound(ModSoundEvents.M240B_DISTANT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M240B_SHOOT)
              .setReloadSound(ModSoundEvents.M240B_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, SubmachineShootAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.M240B_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.TACTICAL_GRIP)
              .addAcceptedAttachment(Attachments.BIPOD)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .addAcceptedAttachment(Attachments.EOTECH_SIGHT)
              .build());

  public static final RegistryObject<GunItem> RPK =
      ITEMS.register("rpk",
          () -> AimableGunItem.builder()
              .setFireDelayMs(92)
              .setDamage(6)
              .setReloadDurationTicks(20 * 3)
              .setAccuracy(0.9F)
              .setRecoil(4.5F)
              .setRange(150.0D)
              .addFireMode(FireMode.AUTO)
              .setShootSound(ModSoundEvents.RPK_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M240B_SHOOT)
              .setReloadSound(ModSoundEvents.RPK_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, RifleShootAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.RPK_MAGAZINE)
              .addAcceptedMagazine(ModItems.RPK_DRUM_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .addAcceptedAttachment(Attachments.ACOG_SIGHT)
              .build());

  public static final RegistryObject<GunItem> MINIGUN =
      ITEMS.register("minigun",
          () -> MinigunItem.builder()
              .setFireDelayMs(75)
              .setDamage(4)
              .setReloadDurationTicks(20 * 5)
              .setAccuracy(0.6F)
              .setRecoil(2.5F)
              .setRange(350.0D)
              .addFireMode(FireMode.AUTO)
              .setShootSound(ModSoundEvents.MINIGUN_SHOOT)
              .setReloadSound(ModSoundEvents.M240B_RELOAD)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.MINIGUN_MAGAZINE)
              .setRightMouseActionTriggerType(Gun.SecondaryActionTrigger.HOLD)
              .setTriggerPredicate(Gun::isPerformingSecondaryAction)
              .setSecondaryActionSound(ModSoundEvents.MINIGUN_BARREL)
              .setSecondaryActionSoundRepeatDelayMs(177L)
              .build());

  public static final RegistryObject<GunItem> MK48MOD =
      ITEMS.register("mk48mod",
          () -> AimableGunItem.builder()
              .setFireDelayMs(92)
              .setDamage(7)
              .setReloadDurationTicks(20 * 4)
              .setAccuracy(0.79F)
              .setRecoil(5.0F)
              .setRange(260.0D)
              .addFireMode(FireMode.AUTO)
              .setShootSound(ModSoundEvents.MK48MOD_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_MK48MOD_SHOOT)
              .setReloadSound(ModSoundEvents.M240B_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, SubmachineShootAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.MK48MOD_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.TACTICAL_GRIP)
              .addAcceptedAttachment(Attachments.BIPOD)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .addAcceptedAttachment(Attachments.EOTECH_SIGHT)
              .build());

  // ================================================================================
  // Pistols
  // ================================================================================

  public static final RegistryObject<GunItem> TASER =
      ITEMS.register("taser",
          () -> AimableGunItem.builder()
              .setCombatSlot(CombatSlot.SECONDARY)
              .setFireDelayMs(2000)
              .setDamage(7)
              .setReloadDurationTicks(20 * 3)
              .setAccuracy(0.9F)
              .setRecoil(1.5F)
              .setRange(7.0D)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.TASER_SHOOT)
              .putAnimation(GunAnimationEvent.SHOOT, PistolShootAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .setDefaultMagazine(ModItems.TASER_CARTRIDGE)
              .build());

  public static final RegistryObject<GunItem> M1911 =
      ITEMS.register("m1911",
          () -> AimableGunItem.builder()
              .setCombatSlot(CombatSlot.SECONDARY)
              .setFireDelayMs(160)
              .setDamage(7)
              .setReloadDurationTicks(20 * 2)
              .setAccuracy(0.8F)
              .setRecoil(2.0F)
              .setRange(50.0D)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.M1911_SHOOT)
              .setDistantShootSound(ModSoundEvents.M1911_DISTANT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.M1911_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, PistolShootAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.M1911_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .build());

  public static final RegistryObject<GunItem> G18 =
      ITEMS.register("g18",
          () -> AimableGunItem.builder()
              .setCombatSlot(CombatSlot.SECONDARY)
              .setFireDelayMs(160)
              .setDamage(7)
              .setReloadDurationTicks((int) (20 * 2.2F))
              .setAccuracy(0.7F)
              .setRecoil(2.5F)
              .setRange(50.0D)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.G18_SHOOT)
              .setDistantShootSound(ModSoundEvents.G18_DISTANT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_RPK_SHOOT)
              .setReloadSound(ModSoundEvents.M1911_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, PistolShootAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.G18_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .build());

  public static final RegistryObject<GunItem> M9 =
      ITEMS.register("m9",
          () -> AimableGunItem.builder()
              .setCombatSlot(CombatSlot.SECONDARY)
              .setFireDelayMs(160)
              .setDamage(7)
              .setReloadDurationTicks((int) (20 * 1.5F))
              .setAccuracy(0.7F)
              .setRecoil(2.0F)
              .setRange(50.0D)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.M9_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M9_SHOOT)
              .setReloadSound(ModSoundEvents.M9_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, PistolShootAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.M9_MAGAZINE)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .build());

  public static final RegistryObject<GunItem> DESERT_EAGLE =
      ITEMS.register("desert_eagle",
          () -> AimableGunItem.builder()
              .setCombatSlot(CombatSlot.SECONDARY)
              .setFireDelayMs(160)
              .setDamage(8)
              .setReloadDurationTicks(20 * 2)
              .setAccuracy(0.9F)
              .setRecoil(8.0F)
              .setRange(80.0D)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.DESERT_EAGLE_SHOOT)
              .setDistantShootSound(ModSoundEvents.DESERT_EAGLE_DISTANT_SHOOT)
              .setReloadSound(ModSoundEvents.M9_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, PistolShootAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.DESERT_EAGLE_MAGAZINE)
              .build());

  public static final RegistryObject<GunItem> P250 =
      ITEMS.register("p250",
          () -> AimableGunItem.builder()
              .setCombatSlot(CombatSlot.SECONDARY)
              .setFireDelayMs(160)
              .setDamage(6)
              .setReloadDurationTicks((int) (20 * 1.5F))
              .setAccuracy(0.7F)
              .setRecoil(3.5F)
              .setRange(60.0D)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.P250_SHOOT)
              .setDistantShootSound(ModSoundEvents.P250_DISTANT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M9_SHOOT)
              .setReloadSound(ModSoundEvents.M9_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, PistolShootAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.P250_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .build());

  public static final RegistryObject<GunItem> MAGNUM =
      ITEMS.register("magnum",
          () -> AimableGunItem.builder()
              .setCombatSlot(CombatSlot.SECONDARY)
              .setFireDelayMs(160)
              .setDamage(7)
              .setReloadDurationTicks(20 * 2)
              .setAccuracy(0.8F)
              .setRecoil(8.0F)
              .setRange(80.0D)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.MAGNUM_SHOOT)
              .setReloadSound(ModSoundEvents.MAGNUM_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, PistolShootAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.MAGNUM_MAGAZINE)
              .build());

  public static final RegistryObject<GunItem> FN57 =
      ITEMS.register("fn57",
          () -> AimableGunItem.builder()
              .setCombatSlot(CombatSlot.SECONDARY)
              .setFireDelayMs(160)
              .setDamage(8)
              .setReloadDurationTicks((int) (20 * 1.5F))
              .setAccuracy(0.8F)
              .setRecoil(3.5F)
              .setRange(50.0D)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.FN57_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.FN57_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, PistolShootAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.FN57_MAGAZINE)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .build());

  // ================================================================================
  // Submachine Guns
  // ================================================================================

  public static final RegistryObject<GunItem> MAC10 =
      ITEMS.register("mac10",
          () -> AimableGunItem.builder()
              .setFireDelayMs(80)
              .setDamage(6)
              .setReloadDurationTicks((int) (20 * 1.8F))
              .setAccuracy(0.7F)
              .setRecoil(3.0F)
              .setRange(70.0D)
              .addFireMode(FireMode.AUTO)
              .setShootSound(ModSoundEvents.MAC10_SHOOT)
              .setDistantShootSound(ModSoundEvents.MAC10_DISTANT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.M4A1_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, SubmachineShootAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.MAC10_MAGAZINE)
              .addAcceptedMagazine(ModItems.MAC10_EXTENDED_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.ACOG_SIGHT)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .build());

  public static final RegistryObject<GunItem> P90 =
      ITEMS.register("p90",
          () -> AimableGunItem.builder()
              .setFireDelayMs(80)
              .setDamage(5)
              .setReloadDurationTicks((int) (20 * 2.2F))
              .setAccuracy(0.75F)
              .setRecoil(2.0F)
              .setRange(100.0D)
              .addFireMode(FireMode.AUTO)
              .setShootSound(ModSoundEvents.P90_SHOOT)
              .setDistantShootSound(ModSoundEvents.P90_DISTANT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_P90_SHOOT)
              .setReloadSound(ModSoundEvents.P90_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, SubmachineShootAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.P90_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.ACOG_SIGHT)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .build());

  public static final RegistryObject<GunItem> VECTOR =
      ITEMS.register("vector",
          () -> AimableGunItem.builder()
              .setFireDelayMs(90)
              .setDamage(5)
              .setReloadDurationTicks((int) (20 * 1.9F))
              .setAccuracy(0.7F)
              .setRecoil(2.5F)
              .setRange(80.0D)
              .addFireMode(FireMode.AUTO)
              .setShootSound(ModSoundEvents.VECTOR_SHOOT)
              .setDistantShootSound(ModSoundEvents.VECTOR_DISTANT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.M4A1_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, SubmachineShootAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.VECTOR_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.ACOG_SIGHT)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .build());

  public static final RegistryObject<GunItem> MP5A5 =
      ITEMS.register("mp5a5",
          () -> AimableGunItem.builder()
              .setFireDelayMs(85)
              .setDamage(7)
              .setReloadDurationTicks((int) (20 * 2.2F))
              .setAccuracy(0.7F)
              .setRecoil(4.5F)
              .setRange(100.0D)
              .addFireMode(FireMode.AUTO)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.MP5A5_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_MP5A5_SHOOT)
              .setReloadSound(ModSoundEvents.MP5A5_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, SubmachineShootAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.MP5A5_21_ROUND_MAGAZINE)
              .addAcceptedMagazine(ModItems.MP5A5_35_ROUND_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.ACOG_SIGHT)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .build());

  // ================================================================================
  // Sniper Rifles
  // ================================================================================

  public static final RegistryObject<GunItem> M107 =
      ITEMS.register("m107",
          () -> AimableGunItem.builder()
              .setFireDelayMs(750)
              .setDamage(20)
              .setReloadDurationTicks(20 * 3)
              .setAccuracy(0.9F)
              .setRecoil(12.0F)
              .setRange(400.0D)
              .setCrosshair(false)
              .setBoltAction(true)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.M107_SHOOT)
              .setReloadSound(ModSoundEvents.M107_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, RifleShootAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.M107_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.ACOG_SIGHT)
              .addAcceptedAttachment(Attachments.LP_SCOPE)
              .addAcceptedAttachment(Attachments.HP_SCOPE)
              .addAcceptedAttachment(Attachments.TACTICAL_GRIP)
              .addAcceptedAttachment(Attachments.BIPOD)
              .build());

  public static final RegistryObject<GunItem> AS50 =
      ITEMS.register("as50",
          () -> AimableGunItem.builder()
              .setFireDelayMs(170)
              .setDamage(14)
              .setReloadDurationTicks((int) (20 * 3.5F))
              .setAccuracy(0.9F)
              .setRecoil(8.5F)
              .setRange(400.0D)
              .setCrosshair(false)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.AS50_SHOOT)
              .setReloadSound(ModSoundEvents.AS50_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, RifleShootAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.AS50_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.ACOG_SIGHT)
              .addAcceptedAttachment(Attachments.LP_SCOPE)
              .addAcceptedAttachment(Attachments.HP_SCOPE)
              .addAcceptedAttachment(Attachments.TACTICAL_GRIP)
              .addAcceptedAttachment(Attachments.BIPOD)
              .build());

  public static final RegistryObject<GunItem> AWP =
      ITEMS.register("awp",
          () -> AimableGunItem.builder()
              .setFireDelayMs(1200)
              .setDamage(20)
              .setReloadDurationTicks(20 * 3)
              .setAccuracy(0.97F)
              .setRecoil(9.0F)
              .setRange(400.0D)
              .setCrosshair(false)
              .setBoltAction(true)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.AWP_SHOOT)
              .setDistantShootSound(ModSoundEvents.AWP_DISTANT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.AWP_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, RifleShootAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.AWP_MAGAZINE)
              .addAcceptedAttachment(Attachments.LP_SCOPE)
              .addAcceptedAttachment(Attachments.HP_SCOPE)
              .addAcceptedAttachment(Attachments.BIPOD)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .build());

  public static final RegistryObject<GunItem> DMR =
      ITEMS.register("dmr",
          () -> AimableGunItem.builder()
              .setFireDelayMs(170)
              .setDamage(15)
              .setReloadDurationTicks(20 * 2)
              .setAccuracy(0.9F)
              .setRecoil(7.0F)
              .setRange(300.0D)
              .setCrosshair(false)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.DMR_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_RPK_SHOOT)
              .setReloadSound(ModSoundEvents.DMR_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, RifleShootAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.DMR_MAGAZINE)
              .addAcceptedAttachment(Attachments.LP_SCOPE)
              .addAcceptedAttachment(Attachments.HP_SCOPE)
              .addAcceptedAttachment(Attachments.BIPOD)
              .addAcceptedAttachment(Attachments.ACOG_SIGHT)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .build());

  // ================================================================================
  // Shotguns
  // ================================================================================

  public static final RegistryObject<GunItem> TRENCH_GUN =
      ITEMS.register("trench_gun",
          () -> AimableGunItem.builder()
              .setFireDelayMs(1200)
              .setDamage(2)
              .setRoundsPerShot(8)
              .setReloadDurationTicks(20 * 1)
              .setAccuracy(0.7F)
              .setRecoil(9.0F)
              .setRange(25.0D)
              .setBoltAction(true)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.TRENCH_GUN_SHOOT)
              .setReloadSound(ModSoundEvents.SHOTGUN_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, PistolShootAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.TRENCH_GUN_SHELLS)
              .addAcceptedAttachment(Attachments.TACTICAL_GRIP)
              .build());

  public static final RegistryObject<GunItem> MOSSBERG =
      ITEMS.register("mossberg",
          () -> AimableGunItem.builder()
              .setRoundsPerShot(8)
              .setFireDelayMs(1200)
              .setDamage(3)
              .setReloadDurationTicks(20 * 3)
              .setAccuracy(0.7F)
              .setRecoil(9.0F)
              .setRange(20.0D)
              .addFireMode(FireMode.SEMI)
              .setBoltAction(true)
              .setShootSound(ModSoundEvents.MOSSBERG_SHOOT)
              .setReloadSound(ModSoundEvents.MOSSBERG_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, PistolShootAnimation::new)
              .putReloadAnimation(ReloadAnimation::new)
              .putAnimation(GunAnimationEvent.INSPECT, InspectAnimation::new)
              .setDefaultMagazine(ModItems.MOSSBERG_SHELLS)
              .addAcceptedAttachment(Attachments.TACTICAL_GRIP)
              .build());

  // ================================================================================
  // Grenades
  // ================================================================================

  public static final RegistryObject<GrenadeItem> FIRE_GRENADE = ITEMS.register("fire_grenade",
      () -> new GrenadeItem((GrenadeItem.Properties) new GrenadeItem.Properties()
          .setGrenadeEntitySupplier(FireGrenadeEntity::new)
          .stacksTo(3)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GrenadeItem> SMOKE_GRENADE = ITEMS.register("smoke_grenade",
      () -> new GrenadeItem((GrenadeItem.Properties) new GrenadeItem.Properties()
          .setGrenadeEntitySupplier(SmokeGrenadeEntity::new)
          .stacksTo(3)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GrenadeItem> FLASH_GRENADE = ITEMS.register("flash_grenade",
      () -> new GrenadeItem((GrenadeItem.Properties) new GrenadeItem.Properties()
          .setGrenadeEntitySupplier(FlashGrenadeEntity::new)
          .stacksTo(3)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GrenadeItem> DECOY_GRENADE = ITEMS.register("decoy_grenade",
      () -> new GrenadeItem((GrenadeItem.Properties) new GrenadeItem.Properties()
          .setGrenadeEntitySupplier(DecoyGrenadeEntity::new)
          .stacksTo(3)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GrenadeItem> FRAG_GRENADE = ITEMS.register("frag_grenade",
      () -> new GrenadeItem((GrenadeItem.Properties) new GrenadeItem.Properties()
          .setGrenadeEntitySupplier(FragGrenade::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GrenadeItem> C4 = ITEMS.register("c4_explosive",
      () -> new GrenadeItem((GrenadeItem.Properties) new GrenadeItem.Properties()
          .setGrenadeEntitySupplier(C4Explosive::new)
          .setThrowSpeed(0.75F)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> REMOTE_DETONATOR = ITEMS.register("remote_detonator",
      () -> new RemoteDetonatorItem(new Item.Properties()
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  // ================================================================================
  // Weapon
  // ================================================================================

  public static final RegistryObject<Item> CROWBAR = ITEMS.register("crowbar",
      () -> new MeleeWeaponItem(7, -2.4F, new Item.Properties()
          .durability(100)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> BAT = ITEMS.register("bat",
      () -> new MeleeWeaponItem(5, -2.4F, new Item.Properties()
          .durability(55)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> KATANA = ITEMS.register("katana",
      () -> new MeleeWeaponItem(18, -2.4F, new Item.Properties()
          .durability(40)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> PIPE = ITEMS.register("pipe",
      () -> new MeleeWeaponItem(9, -2.4F, new Item.Properties()
          .durability(60)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> RUSTY_PIPE = ITEMS.register("rusty_pipe",
      () -> new MeleeWeaponItem(9, -2.4F, new Item.Properties()
          .durability(20)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> FIRE_AXE = ITEMS.register("fire_axe",
      () -> new AxeItem(Tiers.IRON, 14, -2.4F, new Item.Properties()
          .durability(100)
          .tab((COMBAT_TAB))));

  public static final RegistryObject<Item> CHAINSAW = ITEMS.register("chainsaw",
      () -> new MeleeWeaponItem(8, -2.4F, new Item.Properties()
          .durability(75)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> BOWIE_KNIFE = ITEMS.register("bowie_knife",
      () -> new MeleeWeaponItem(15, -2.4F, new Item.Properties()
          .durability(20)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> GOLF_CLUB = ITEMS.register("golf_club",
      () -> new MeleeWeaponItem(6, -2.4F, new Item.Properties()
          .durability(40)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> NIGHT_STICK = ITEMS.register("night_stick",
      () -> new MeleeWeaponItem(4, -2.4F, new Item.Properties()
          .durability(70)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> SLEDGEHAMMER = ITEMS.register("sledgehammer",
      () -> new PickaxeItem(Tiers.IRON, 10, -2.4F,
          new Item.Properties()
              .durability(110)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> NAIL_BAT = ITEMS.register("nail_bat",
      () -> new MeleeWeaponItem(8, -2.4F, new Item.Properties()
          .durability(55)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> SHOVEL = ITEMS.register("shovel",
      () -> new ShovelItem(Tiers.IRON, 8, -2.4F, new Item.Properties()
          .durability(70)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> HATCHET = ITEMS.register("hatchet",
      () -> new AxeItem(Tiers.IRON, 16, -2.4F, new Item.Properties()
          .durability(40)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> BROADSWORD = ITEMS.register("broadsword",
      () -> new MeleeWeaponItem(14, -2.4F, new Item.Properties()
          .durability(55)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> MACHETE = ITEMS.register("machete",
      () -> new MeleeWeaponItem(12, -2.4F, new Item.Properties()
          .durability(70)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> WEAPONIZED_SCYTHE = ITEMS.register("weaponized_scythe",
      () -> new MeleeWeaponItem(15, -2.4F, new Item.Properties()
          .durability(40)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> SCYTHE = ITEMS.register("scythe",
      () -> new MeleeWeaponItem(20, -2.4F, new Item.Properties()
          .durability(20)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> PICKAXE = ITEMS.register("pickaxe",
      () -> new PickaxeItem(Tiers.IRON, 10, -2.4F, new Item.Properties()
          .durability(210)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> BO_STAFF = ITEMS.register("bo_staff",
      () -> new MeleeWeaponItem(4, -2.4F, new Item.Properties()
          .durability(70)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> WRENCH = ITEMS.register("wrench",
      () -> new MeleeWeaponItem(8, -2.4F, new Item.Properties()
          .durability(120)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> FRYING_PAN = ITEMS.register("frying_pan",
      () -> new MeleeWeaponItem(6, -2.4F, new Item.Properties()
          .durability(80)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> BOLT_CUTTERS = ITEMS.register("bolt_cutters",
      () -> new MeleeWeaponItem(9, -2.4F, new Item.Properties()
          .durability(50)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> COMBAT_KNIFE = ITEMS.register("combat_knife",
      () -> new MeleeWeaponItem(14, -2.4F, new Item.Properties()
          .durability(100)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> STEEL_BAT = ITEMS.register("steel_bat",
      () -> new MeleeWeaponItem(7, -2.4F, new Item.Properties()
          .durability(180)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> CLEAVER = ITEMS.register("cleaver",
      () -> new MeleeWeaponItem(10, -2.4F, new Item.Properties()
          .durability(80)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> BROKEN_BOTTLE = ITEMS.register("broken_bottle",
      () -> new MeleeWeaponItem(15, -2.4F, new Item.Properties()
          .durability(10)
          .tab(COMBAT_TAB)));

  // ================================================================================
  // Vests
  // ================================================================================

  public static final RegistryObject<Item> BLACK_TACTICAL_VEST =
      ITEMS.register("black_tactical_vest", () -> new StorageItem(StorageItem.VEST,
          new Item.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> GHILLIE_TACTICAL_VEST =
      ITEMS.register("ghillie_tactical_vest", () -> new StorageItem(StorageItem.VEST,
          new Item.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> GREEN_TACTICAL_VEST =
      ITEMS.register("green_tactical_vest", () -> new StorageItem(StorageItem.VEST,
          new Item.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> GREY_TACTICAL_VEST =
      ITEMS.register("grey_tactical_vest", () -> new StorageItem(StorageItem.VEST,
          new Item.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> RIOT_VEST =
      ITEMS.register("riot_vest", () -> new StorageItem(StorageItem.VEST,
          new Item.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> TAN_TACTICAL_VEST =
      ITEMS.register("tan_tactical_vest", () -> new StorageItem(StorageItem.VEST,
          new Item.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  // ================================================================================
  // Hats, Helmets and Masks
  // ================================================================================

  public static final RegistryObject<Item> ARMY_HELMET = ITEMS.register("army_helmet",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .setHeadshotReductionPercentage(0.2F)
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> BEANIE_HAT = ITEMS.register("beanie_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> BLACK_BALLISTIC_HAT =
      ITEMS.register("black_ballistic_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> FIREMAN_CHIEF_HAT = ITEMS.register("chief_fireman_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> BLUE_HARD_HAT = ITEMS.register("blue_hard_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .setHeadshotReductionPercentage(0.2F)
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> BUNNY_HAT = ITEMS.register("bunny_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> CAMO_HELMET = ITEMS.register("camo_helmet",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .setHeadshotReductionPercentage(0.2F)
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> CLONE_HAT = ITEMS.register("clone_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .setHeadshotReductionPercentage(0.2F)
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> COMBAT_BDU_HELMET = ITEMS.register("combat_bdu_helmet",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .setHeadshotReductionPercentage(0.2F)
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> COOKIE_MASK = ITEMS.register("cookie_mask",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> COW_MASK = ITEMS.register("cow_mask",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> CREEPER_MASK = ITEMS.register("creeper_mask",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> DEADPOOL_MASK = ITEMS.register("deadpool_mask",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> DOCTOR_MASK = ITEMS.register("doctor_mask",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> FIREMAN_HAT = ITEMS.register("fireman_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> GAS_MASK = ITEMS.register("gas_mask",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .setImmuneToFlashes(true)
          .setImmuneToGas(true)
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> GHILLIE_HAT = ITEMS.register("ghillie_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> GREEN_ARMY_HELMET = ITEMS.register("green_army_helmet",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .setHeadshotReductionPercentage(0.2F)
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> GREEN_BALLISTIC_HELMET =
      ITEMS.register("green_ballistic_helmet",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> GREEN_HARD_HAT = ITEMS.register("green_hard_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .setHeadshotReductionPercentage(0.2F)
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> GREY_ARMY_HELMET = ITEMS.register("grey_army_helmet",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .setHeadshotReductionPercentage(0.2F)
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> HACKER_MASK = ITEMS.register("hacker_mask",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> HAZMAT_HAT = ITEMS.register("hazmat_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .setImmuneToFlashes(true)
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> JUGGERNAUT_HELMET = ITEMS.register("juggernaut_helmet",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .setHeadshotReductionPercentage(0.2F)
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> KNIGHT_HAT = ITEMS.register("knight_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> MILITARY_HAZMAT_HAT =
      ITEMS.register("military_hazmat_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setImmuneToFlashes(true)
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> NINJA_HAT = ITEMS.register("ninja_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> NV_GOGGLES_HAT = ITEMS.register("nv_goggles_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .setNightVision(true)
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> ORANGE_HARD_HAT = ITEMS.register("orange_hard_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .setHeadshotReductionPercentage(0.2F)
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> PAYDAY_MASK = ITEMS.register("payday_mask",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> PAYDAY2_MASK = ITEMS.register("payday2_mask",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> PILOT_HELMET = ITEMS.register("pilot_helmet",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> PUMPKIN_MASK = ITEMS.register("pumpkin_mask",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> RADAR_CAP = ITEMS.register("radar_cap",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> RIOT_HAT = ITEMS.register("riot_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .setHeadshotReductionPercentage(0.2F)
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SANTA_HAT = ITEMS.register("santa_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SCUBA_MASK = ITEMS.register("scuba_mask",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SHEEP_MASK = ITEMS.register("sheep_mask",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SKI_MASK = ITEMS.register("ski_mask",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .setImmuneToFlashes(true)
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SPETSNAZ_HELMET = ITEMS.register("spetsnaz_helmet",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .setHeadshotReductionPercentage(0.2F)
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> TOP_HAT = ITEMS.register("top_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> TRAPPER_HAT = ITEMS.register("trapper_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> USHANKA_HAT = ITEMS.register("ushanka_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> WINTER_MILITARY_HELMET =
      ITEMS.register("winter_military_helmet",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(0.2F)
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> YELLOW_HARD_HAT = ITEMS.register("yellow_hard_hat",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .setHeadshotReductionPercentage(0.2F)
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> ZOMBIE_MASK = ITEMS.register("zombie_mask",
      () -> new HatItem((HatItem.Properties) new HatItem.Properties()
          .stacksTo(1)
          .tab(COSMETICS_TAB)));

  // ================================================================================
  // Clothing
  // ================================================================================

  public static final RegistryObject<Item> ARMY_CLOTHING =
      ITEMS.register("army_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  3,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SAS_CLOTHING =
      ITEMS.register("sas_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  3,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SPETSNAZ_CLOTHING =
      ITEMS.register("spetsnaz_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  3,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> POLICE_CLOTHING =
      ITEMS.register("police_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  2,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> CAMO_CLOTHING =
      ITEMS.register("camo_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  3,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> COMBAT_BDU_CLOTHING =
      ITEMS.register("combat_bdu_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  3,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> WINTER_ARMY_CLOTHING =
      ITEMS.register("winter_army_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  3,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> ARMY_DESERT_CLOTHING =
      ITEMS.register("army_desert_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  3,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> PILOT_CLOTHING =
      ITEMS.register("pilot_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  1,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> HAZMAT_CLOTHING =
      ITEMS.register("hazmat_clothing",
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
      ITEMS.register("tac_ghillie_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  2,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SWAT_CLOTHING =
      ITEMS.register("swat_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  3,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SPACE_SUIT_CLOTHING =
      ITEMS.register("space_suit_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  3,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SHERIFF_CLOTHING =
      ITEMS.register("sheriff_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  1,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> JUGGERNAUT_CLOTHING =
      ITEMS.register("juggernaut_clothing",
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
      ITEMS.register("fireman_clothing",
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
      ITEMS.register("doctor_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SMART_CLOTHING =
      ITEMS.register("smart_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> CASUAL_GREEN_CLOTHING =
      ITEMS.register("casual_green_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> BUILDER_CLOTHING =
      ITEMS.register("builder_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> BUSINESS_CLOTHING =
      ITEMS.register("business_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SEC_GUARD_CLOTHING =
      ITEMS.register("sec_guard_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  1,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> MIL_HAZMAT_CLOTHING =
      ITEMS.register("mil_hazmat_clothing",
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
      ITEMS.register("full_ghillie_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  2,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> RED_DUSK_CLOTHING =
      ITEMS.register("red_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  2,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> CLONE_CLOTHING =
      ITEMS.register("clone_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  2,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> COOKIE_CLOTHING =
      ITEMS.register("cookie_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> DEADPOOL_CLOTHING =
      ITEMS.register("deadpool_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  2,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> NINJA_CLOTHING =
      ITEMS.register("ninja_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  1,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> ARMY_MEDIC_CLOTHING =
      ITEMS.register("army_medic_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  3,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> BLUE_DUSK_CLOTHING =
      ITEMS.register("blue_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  2,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> PRESIDENT_CLOTHING =
      ITEMS.register("president_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> YELLOW_DUSK_CLOTHING =
      ITEMS.register("yellow_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  2,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> ORANGE_DUSK_CLOTHING =
      ITEMS.register("orange_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  2,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> GREEN_DUSK_CLOTHING =
      ITEMS.register("green_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  2,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> WHITE_DUSK_CLOTHING =
      ITEMS.register("white_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  2,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> PURPLE_DUSK_CLOTHING =
      ITEMS.register("purple_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  2,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SCUBA_CLOTHING =
      ITEMS.register("scuba_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  1,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> DDPAT_CLOTHING =
      ITEMS.register("ddpat_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .addAttributeModifier(Attributes.ARMOR, new AttributeModifier(
                  Clothing.MODIFIER_ID,
                  "Armor modifier",
                  2,
                  AttributeModifier.Operation.ADDITION))
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> CONTRACTOR_CLOTHING =
      ITEMS.register("contractor_clothing",
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

  public static final RegistryObject<Item> SMALL_BARREL = ITEMS.register("small_barrel",
      () -> new Item(new Item.Properties().tab(COMBAT_TAB)));

  public static final RegistryObject<Item> MEDIUM_BARREL = ITEMS.register("medium_barrel",
      () -> new Item(new Item.Properties().tab(COMBAT_TAB)));

  public static final RegistryObject<Item> HEAVY_BARREL = ITEMS.register("heavy_barrel",
      () -> new Item(new Item.Properties().tab(COMBAT_TAB)));

  public static final RegistryObject<Item> SMALL_BODY = ITEMS.register("small_body",
      () -> new Item(new Item.Properties().tab(COMBAT_TAB)));

  public static final RegistryObject<Item> MEDIUM_BODY = ITEMS.register("medium_body",
      () -> new Item(new Item.Properties().tab(COMBAT_TAB)));

  public static final RegistryObject<Item> HEAVY_BODY = ITEMS.register("heavy_body",
      () -> new Item(new Item.Properties().tab(COMBAT_TAB)));

  public static final RegistryObject<Item> SMALL_HANDLE = ITEMS.register("small_handle",
      () -> new Item(new Item.Properties().tab(COMBAT_TAB)));

  public static final RegistryObject<Item> MEDIUM_HANDLE = ITEMS.register("medium_handle",
      () -> new Item(new Item.Properties().tab(COMBAT_TAB)));

  public static final RegistryObject<Item> HEAVY_HANDLE = ITEMS.register("heavy_handle",
      () -> new Item(new Item.Properties().tab(COMBAT_TAB)));

  public static final RegistryObject<Item> SMALL_STOCK = ITEMS.register("small_stock",
      () -> new Item(new Item.Properties().tab(COMBAT_TAB)));

  public static final RegistryObject<Item> MEDIUM_STOCK = ITEMS.register("medium_stock",
      () -> new Item(new Item.Properties().tab(COMBAT_TAB)));

  public static final RegistryObject<Item> MEDIUM_BOLT = ITEMS.register("medium_bolt",
      () -> new Item(new Item.Properties().tab(COMBAT_TAB)));

  public static final RegistryObject<Item> HEAVY_BOLT = ITEMS.register("heavy_bolt",
      () -> new Item(new Item.Properties().tab(COMBAT_TAB)));

  // ================================================================================
  // Miscellaneous
  // ================================================================================

  public static final RegistryObject<Item> BINOCULARS =
      ITEMS.register("binoculars",
          () -> new BinocularsItem(new Item.Properties()
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> PARACHUTE =
      ITEMS.register("parachute",
          () -> new ParachuteItem(new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  // ================================================================================
  // Medical
  // ================================================================================

  public static final RegistryObject<Item> FIRST_AID_KIT =
      ITEMS.register("first_aid_kit",
          () -> new ActionItem((ActionItem.Properties) new ActionItem.Properties()
              .setAction(ActionTypes.USE_FIRST_AID_KIT)
              .stacksTo(1)
              .tab(MEDICAL_TAB)));

  public static final RegistryObject<Item> ADRENALINE_SYRINGE =
      ITEMS.register("adrenaline_syringe",
          () -> new ActionItem((ActionItem.Properties) new ActionItem.Properties()
              .setAction(ActionTypes.USE_ADRENALINE_SYRINGE)
              .stacksTo(1)
              .tab(MEDICAL_TAB)));

  public static final RegistryObject<Item> SYRINGE =
      ITEMS.register("syringe",
          () -> new ActionItem((ActionItem.Properties) new ActionItem.Properties()
              .setAction(ActionTypes.USE_SYRINGE)
              .stacksTo(1)
              .tab(MEDICAL_TAB)));

  public static final RegistryObject<Item> BLOOD_SYRINGE =
      ITEMS.register("blood_syringe",
          () -> new ActionItem((ActionItem.Properties) new ActionItem.Properties()
              .setAction(ActionTypes.USE_BLOOD_SYRINGE)
              .stacksTo(1)
              .tab(MEDICAL_TAB)));

  public static final RegistryObject<Item> BANDAGE =
      ITEMS.register("bandage",
          () -> new ActionItem((ActionItem.Properties) new ActionItem.Properties()
              .setAction(ActionTypes.USE_BANDAGE)
              .stacksTo(1)
              .tab(MEDICAL_TAB)));

  // ================================================================================
  // Backpacks
  // ================================================================================

  public static final RegistryObject<Item> SMALL_RED_BACKPACK = ITEMS
      .register("small_red_backpack", () -> new StorageItem(StorageItem.SMALL_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SMALL_ORANGE_BACKPACK = ITEMS
      .register("small_orange_backpack", () -> new StorageItem(StorageItem.SMALL_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SMALL_YELLOW_BACKPACK = ITEMS
      .register("small_yellow_backpack", () -> new StorageItem(StorageItem.SMALL_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SMALL_GREEN_BACKPACK = ITEMS
      .register("small_green_backpack", () -> new StorageItem(StorageItem.SMALL_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SMALL_BLUE_BACKPACK = ITEMS
      .register("small_blue_backpack", () -> new StorageItem(StorageItem.SMALL_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> SMALL_PURPLE_BACKPACK = ITEMS
      .register("small_purple_backpack", () -> new StorageItem(StorageItem.SMALL_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> MEDIUM_RED_BACKPACK = ITEMS
      .register("medium_red_backpack", () -> new StorageItem(StorageItem.MEDIUM_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> MEDIUM_ORANGE_BACKPACK = ITEMS
      .register("medium_orange_backpack", () -> new StorageItem(StorageItem.MEDIUM_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> MEDIUM_YELLOW_BACKPACK = ITEMS
      .register("medium_yellow_backpack", () -> new StorageItem(StorageItem.MEDIUM_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> MEDIUM_GREEN_BACKPACK = ITEMS
      .register("medium_green_backpack", () -> new StorageItem(StorageItem.MEDIUM_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> MEDIUM_BLUE_BACKPACK = ITEMS
      .register("medium_blue_backpack", () -> new StorageItem(StorageItem.MEDIUM_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> MEDIUM_PURPLE_BACKPACK = ITEMS
      .register("medium_purple_backpack", () -> new StorageItem(StorageItem.MEDIUM_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> MEDIUM_GREY_BACKPACK = ITEMS
      .register("medium_grey_backpack", () -> new StorageItem(StorageItem.MEDIUM_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> MEDIUM_BLACK_BACKPACK = ITEMS
      .register("medium_black_backpack", () -> new StorageItem(StorageItem.MEDIUM_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> MEDIUM_GHILLIE_BACKPACK = ITEMS
      .register("medium_ghillie_backpack", () -> new StorageItem(StorageItem.MEDIUM_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> MEDIUM_WHITE_BACKPACK = ITEMS
      .register("medium_white_backpack", () -> new StorageItem(StorageItem.MEDIUM_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> LARGE_GREY_BACKPACK = ITEMS
      .register("large_grey_backpack", () -> new StorageItem(StorageItem.LARGE_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> LARGE_GREEN_BACKPACK = ITEMS
      .register("large_green_backpack", () -> new StorageItem(StorageItem.LARGE_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> LARGE_TAN_BACKPACK = ITEMS
      .register("large_tan_backpack", () -> new StorageItem(StorageItem.LARGE_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> LARGE_BLACK_BACKPACK = ITEMS
      .register("large_black_backpack", () -> new StorageItem(StorageItem.LARGE_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> LARGE_GHILLIE_BACKPACK = ITEMS
      .register("large_ghillie_backpack", () -> new StorageItem(StorageItem.LARGE_BACKPACK,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> TAN_GUN_BAG = ITEMS
      .register("tan_gun_bag", () -> new StorageItem(StorageItem.GUN_BAG,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> GREY_GUN_BAG = ITEMS
      .register("grey_gun_bag", () -> new StorageItem(StorageItem.GUN_BAG,
          new Item.Properties().stacksTo(1).tab(COSMETICS_TAB)));
}
