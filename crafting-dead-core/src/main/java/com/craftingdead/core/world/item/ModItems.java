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
import com.craftingdead.core.world.action.ActionTypes;
import com.craftingdead.core.world.clothing.Clothing;
import com.craftingdead.core.world.entity.grenade.C4ExplosiveEntity;
import com.craftingdead.core.world.entity.grenade.DecoyGrenadeEntity;
import com.craftingdead.core.world.entity.grenade.FireGrenadeEntity;
import com.craftingdead.core.world.entity.grenade.FlashGrenadeEntity;
import com.craftingdead.core.world.entity.grenade.FragGrenadeEntity;
import com.craftingdead.core.world.entity.grenade.SmokeGrenadeEntity;
import com.craftingdead.core.world.gun.attachment.Attachments;
import com.craftingdead.core.world.gun.type.GunTypes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

  public static final DeferredRegister<Item> ITEMS =
      DeferredRegister.create(ForgeRegistries.ITEMS, CraftingDead.ID);

  public static final ItemGroup COSMETICS_TAB = new ItemGroup("craftingdead.cosmetics") {

    @Override
    public ItemStack makeIcon() {
      return new ItemStack(ModItems.BUILDER_CLOTHING::get);
    }
  };

  public static final ItemGroup COMBAT_TAB = new ItemGroup("craftingdead.combat") {

    @Override
    public ItemStack makeIcon() {
      return new ItemStack(ModItems.AK47::get);
    }
  };

  public static final ItemGroup MEDICAL_TAB = new ItemGroup("craftingdead.medical") {

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
          () -> new Item(new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> ASMO_PAINT =
      ITEMS.register("asmo_paint",
          () -> new Item(new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> CANDY_APPLE_PAINT =
      ITEMS.register("candy_apple_paint",
          () -> new Item(new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> CYREX_PAINT =
      ITEMS.register("cyrex_paint",
          () -> new Item(new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> DIAMOND_PAINT =
      ITEMS.register("diamond_paint",
          () -> new Item(new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> DRAGON_PAINT =
      ITEMS.register("dragon_paint",
          () -> new Item(new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> FADE_PAINT =
      ITEMS.register("fade_paint",
          () -> new Item(new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> FURY_PAINT =
      ITEMS.register("fury_paint",
          () -> new Item(new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> GEM_PAINT =
      ITEMS.register("gem_paint",
          () -> new Item(new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> INFERNO_PAINT =
      ITEMS.register("inferno_paint",
          () -> new Item(new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> RUBY_PAINT =
      ITEMS.register("ruby_paint",
          () -> new Item(new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> SCORCHED_PAINT =
      ITEMS.register("scorched_paint",
          () -> new Item(new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> SLAUGHTER_PAINT =
      ITEMS.register("slaughter_paint",
          () -> new Item(new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> UV_PAINT =
      ITEMS.register("uv_paint",
          () -> new Item(new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> HYPER_BEAST_PAINT =
      ITEMS.register("hyper_beast_paint",
          () -> new Item(new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> EMPEROR_DRAGON_PAINT =
      ITEMS.register("emperor_dragon_paint",
          () -> new Item(new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> NUCLEAR_WINTER_PAINT =
      ITEMS.register("nuclear_winter_paint",
          () -> new Item(new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> MONARCH_PAINT =
      ITEMS.register("monarch_paint",
          () -> new Item(new Item.Properties()
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> LOVELACE_PAINT =
      ITEMS.register("lovelace_paint",
          () -> new Item(new Item.Properties()
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
              .setCustomTexture(true)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> STANAG_DRUM_MAGAZINE =
      ITEMS.register("stanag_drum_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(45)
              .setArmorPenetration(0.4F)
              .setCustomTexture(true)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> STANAG_30_ROUND_MAGAZINE =
      ITEMS.register("stanag_30_round_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .setArmorPenetration(0.4F)
              .setCustomTexture(true)
              .stacksTo(1)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<MagazineItem> STANAG_20_ROUND_MAGAZINE =
      ITEMS.register("stanag_20_round_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(20)
              .setArmorPenetration(0.4F)
              .setCustomTexture(true)
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

  public static final RegistryObject<MagazineItem> TRENCHGUN_SHELLS =
      ITEMS.register("trenchgun_shells",
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
              .setCustomTexture(true)
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

  public static final RegistryObject<GunItem> M4A1 = ITEMS.register("m4a1",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.M4A1)
          .setRendererFactory(() -> M4A1Renderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GunItem> SCARH = ITEMS.register("scarh",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.SCARH)
          .setRendererFactory(() -> ScarhRenderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GunItem> AK47 = ITEMS.register("ak47",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.AK47)
          .setRendererFactory(() -> AK47Renderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GunItem> FNFAL = ITEMS.register("fnfal",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.FNFAL)
          .setRendererFactory(() -> FNFALRenderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GunItem> ACR = ITEMS.register("acr",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.ACR)
          .setRendererFactory(() -> ACRRenderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GunItem> HK417 = ITEMS.register("hk417",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.HK417)
          .setRendererFactory(() -> HK417Renderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GunItem> MPT55 = ITEMS.register("mpt55",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.MPT55)
          .setRendererFactory(() -> MPT55Renderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GunItem> M1GARAND = ITEMS.register("m1garand",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.M1GARAND)
          .setRendererFactory(() -> M1GarandRenderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GunItem> SPORTER22 = ITEMS.register("sporter22",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.SPORTER22)
          .setRendererFactory(() -> Sporter22Renderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GunItem> G36C = ITEMS.register("g36c",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.G36C)
          .setRendererFactory(() -> G36CRenderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));


  // ================================================================================
  // Machine Guns
  // ================================================================================

  public static final RegistryObject<GunItem> M240B = ITEMS.register("m240b",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.M240B)
          .setRendererFactory(() -> M240BRenderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GunItem> RPK = ITEMS.register("rpk",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.RPK)
          .setRendererFactory(() -> RPKRenderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GunItem> MINIGUN = ITEMS.register("minigun",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.MINIGUN)
          .setRendererFactory(() -> MinigunRenderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GunItem> MK48MOD = ITEMS.register("mk48mod",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.MK48MOD)
          .setRendererFactory(() -> MK48ModRenderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  // ================================================================================
  // Pistols
  // ================================================================================

  public static final RegistryObject<GunItem> TASER = ITEMS.register("taser",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.TASER)
          .setRendererFactory(() -> TaserRenderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GunItem> M1911 = ITEMS.register("m1911",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.M1911)
          .setRendererFactory(() -> M1911Renderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GunItem> G18 = ITEMS.register("g18",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.G18)
          .setRendererFactory(() -> G18Renderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GunItem> M9 = ITEMS.register("m9",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.M9)
          .setRendererFactory(() -> M9Renderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GunItem> DESERT_EAGLE = ITEMS.register("desert_eagle",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.DESERT_EAGLE)
          .setRendererFactory(() -> DesertEagleRenderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GunItem> P250 = ITEMS.register("p250",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.P250)
          .setRendererFactory(() -> P250Renderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GunItem> MAGNUM = ITEMS.register("magnum",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.MAGNUM)
          .setRendererFactory(() -> MagnumRenderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GunItem> FN57 = ITEMS.register("fn57",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.FN57)
          .setRendererFactory(() -> FN57Renderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  // ================================================================================
  // Submachine Guns
  // ================================================================================

  public static final RegistryObject<GunItem> MAC10 = ITEMS.register("mac10",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.MAC10)
          .setRendererFactory(() -> MAC10Renderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GunItem> P90 = ITEMS.register("p90",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.P90)
          .setRendererFactory(() -> P90Renderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GunItem> VECTOR = ITEMS.register("vector",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.VECTOR)
          .setRendererFactory(() -> VectorRenderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GunItem> MP5A5 = ITEMS.register("mp5a5",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.MP5A5)
          .setRendererFactory(() -> MP5A5Renderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  // ================================================================================
  // Sniper Rifles
  // ================================================================================

  public static final RegistryObject<GunItem> M107 = ITEMS.register("m107",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.M107)
          .setRendererFactory(() -> M107Renderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GunItem> AS50 = ITEMS.register("as50",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.AS50)
          .setRendererFactory(() -> AS50Renderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GunItem> AWP = ITEMS.register("awp",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.AWP)
          .setRendererFactory(() -> AWPRenderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GunItem> DMR = ITEMS.register("dmr",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.DMR)
          .setRendererFactory(() -> DMRRenderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  // ================================================================================
  // Shotguns
  // ================================================================================

  public static final RegistryObject<GunItem> TRENCHGUN = ITEMS.register("trenchgun",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.TRENCHGUN)
          .setRendererFactory(() -> TrenchgunRenderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GunItem> MOSSBERG = ITEMS.register("mossberg",
      () -> new GunItem((GunItem.Properties) new GunItem.Properties()
          .setGunType(GunTypes.MOSSBERG)
          .setRendererFactory(() -> MossbergRenderer::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

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
          .setGrenadeEntitySupplier(FragGrenadeEntity::new)
          .stacksTo(1)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<GrenadeItem> C4 = ITEMS.register("c4_explosive",
      () -> new GrenadeItem((GrenadeItem.Properties) new GrenadeItem.Properties()
          .setGrenadeEntitySupplier(C4ExplosiveEntity::new)
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
      () -> new AxeItem(ItemTier.IRON, 14, -2.4F, new Item.Properties()
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
      () -> new PickaxeItem(ItemTier.IRON, 10, -2.4F,
          new Item.Properties()
              .durability(110)
              .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> NAIL_BAT = ITEMS.register("nail_bat",
      () -> new MeleeWeaponItem(8, -2.4F, new Item.Properties()
          .durability(55)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> SHOVEL = ITEMS.register("shovel",
      () -> new ShovelItem(ItemTier.IRON, 8, -2.4F, new Item.Properties()
          .durability(70)
          .tab(COMBAT_TAB)));

  public static final RegistryObject<Item> HATCHET = ITEMS.register("hatchet",
      () -> new AxeItem(ItemTier.IRON, 16, -2.4F, new Item.Properties()
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
      () -> new PickaxeItem(ItemTier.IRON, 10, -2.4F, new Item.Properties()
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
      ITEMS.register("black_tactical_vest", () -> new StorageItem(StorageItem.VEST::get,
          new Item.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> GHILLIE_TACTICAL_VEST =
      ITEMS.register("ghillie_tactical_vest", () -> new StorageItem(StorageItem.VEST::get,
          new Item.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> GREEN_TACTICAL_VEST =
      ITEMS.register("green_tactical_vest", () -> new StorageItem(StorageItem.VEST::get,
          new Item.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> GREY_TACTICAL_VEST =
      ITEMS.register("grey_tactical_vest", () -> new StorageItem(StorageItem.VEST::get,
          new Item.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> RIOT_VEST =
      ITEMS.register("riot_vest", () -> new StorageItem(StorageItem.VEST::get,
          new Item.Properties()
              .stacksTo(1)
              .tab(COSMETICS_TAB)));

  public static final RegistryObject<Item> TAN_TACTICAL_VEST =
      ITEMS.register("tan_tactical_vest", () -> new StorageItem(StorageItem.VEST::get,
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
}
