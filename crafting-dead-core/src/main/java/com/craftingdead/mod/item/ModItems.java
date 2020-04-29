package com.craftingdead.mod.item;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.capability.animation.fire.PistolShootAnimation;
import com.craftingdead.mod.capability.animation.fire.RifleShootAnimation;
import com.craftingdead.mod.capability.animation.fire.SubmachineShootAnimation;
import com.craftingdead.mod.entity.ModEntityTypes;
import com.craftingdead.mod.inventory.CraftingInventorySlotType;
import com.craftingdead.mod.item.AttachmentItem.MultiplierType;
import com.craftingdead.mod.potion.ModEffects;
import com.craftingdead.mod.type.Backpack;
import com.craftingdead.mod.type.Vest;
import com.craftingdead.mod.util.ModDamageSource;
import com.craftingdead.mod.util.ModSoundEvents;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

  public static final DeferredRegister<Item> ITEMS =
      new DeferredRegister<>(ForgeRegistries.ITEMS, CraftingDead.ID);

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

  // ================================================================================
  // Clips
  // ================================================================================

  public static final RegistryObject<MagazineItem> STANAG_20_ROUND_MAGAZINE = ITEMS
      .register("stanag_20_round_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(20)
              .setArmorPenetration(40)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> STANAG_30_ROUND_MAGAZINE = ITEMS
      .register("stanag_30_round_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .setArmorPenetration(40)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> STANAG_DRUM_MAGAZINE = ITEMS
      .register("stanag_drum_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(45)
              .setArmorPenetration(40)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> STANAG_BOX_MAGAZINE = ITEMS
      .register("stanag_box_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(85)
              .setArmorPenetration(40)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> MPT55_MAGAZINE = ITEMS
      .register("mpt55_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .setArmorPenetration(40)
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
              .setArmorPenetration(55)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> ACR_MAGAZINE = ITEMS
      .register("acr_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(20)
              .setArmorPenetration(50)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> G36C_MAGAZINE = ITEMS
      .register("g36c_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .setArmorPenetration(45)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> HK417_MAGAZINE = ITEMS
      .register("hk417_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .setArmorPenetration(47)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> M1911_MAGAZINE = ITEMS
      .register("m1911_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(7)
              .setArmorPenetration(8)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> G18_MAGAZINE = ITEMS
      .register("g18_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(20)
              .setArmorPenetration(8)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> M9_MAGAZINE = ITEMS
      .register("m9_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(15)
              .setArmorPenetration(8)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> DESERT_EAGLE_MAGAZINE = ITEMS
      .register("desert_eagle_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(13)
              .setArmorPenetration(35)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> P250_MAGAZINE = ITEMS
      .register("p250_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(12)
              .setArmorPenetration(8)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> MAGNUM_AMMO = ITEMS
      .register("magnum_ammo",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(6)
              .setArmorPenetration(65)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> FN57_MAGAZINE = ITEMS
      .register("fn57_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(20)
              .setArmorPenetration(9)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> MAC10_MAGAZINE = ITEMS
      .register("mac10_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .setArmorPenetration(15)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> P90_MAGAZINE = ITEMS
      .register("p90_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(50)
              .setArmorPenetration(15)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> VECTOR_MAGAZINE = ITEMS
      .register("vector_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(30)
              .setArmorPenetration(15)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> MP5A5_21_ROUND_MAGAZINE = ITEMS
      .register("mp5a5_21_round_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(21)
              .setArmorPenetration(15)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> MP5A5_35_ROUND_MAGAZINE = ITEMS
      .register("mp5a5_35_round_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(35)
              .setArmorPenetration(15)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> MAC10_EXTENDED_MAGAZINE = ITEMS
      .register("mac10_extended_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(45)
              .setArmorPenetration(15)
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
              .setArmorPenetration(65)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> M107_AP_MAGAZINE = ITEMS
      .register("m107_ap_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(6)
              .setArmorPenetration(95)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> AS50_MAGAZINE = ITEMS
      .register("as50_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(10)
              .setArmorPenetration(65)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> AS50_AP_MAGAZINE = ITEMS
      .register("as50_ap_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(5)
              .setArmorPenetration(95)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> M1GARAND_MAGAZINE = ITEMS
      .register("m1garand_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(8)
              .setArmorPenetration(95)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> AWP_MAGAZINE = ITEMS
      .register("awp_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(10)
              .setArmorPenetration(95)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> TRENCHGUN_SHELLS = ITEMS
      .register("trenchgun_shells",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(6)
              .setArmorPenetration(35)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> MOSSBERG_SLUGS = ITEMS
      .register("mossberg_slugs",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(8)
              .setArmorPenetration(30)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> DMR_MAGAZINE = ITEMS
      .register("dmr_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(5)
              .setArmorPenetration(65)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> CROSSBOW_BOLT = ITEMS
      .register("crossbow_bolt",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(1)
              .setArmorPenetration(12)
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
              .setArmorPenetration(50)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> RPK_MAGAZINE = ITEMS
      .register("rpk_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(60)
              .setArmorPenetration(50)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> RPK_DRUM_MAGAZINE = ITEMS
      .register("rpk_drum_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(100)
              .setArmorPenetration(50)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> MINIGUN_MAGAZINE = ITEMS
      .register("minigun_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(350)
              .setArmorPenetration(30)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> MK48MOD_MAGAZINE = ITEMS
      .register("mk48mod_magazine",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(150)
              .setArmorPenetration(52)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> COMBAT_ARROW = ITEMS
      .register("combat_arrow",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(1)
              .setArmorPenetration(25)
              .setGroundHitDropChance(95)
              .setEntityHitDropChance(90)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> EXPLOSIVE_ARROW = ITEMS
      .register("explosive_arrow",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(1)
              .setArmorPenetration(12)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> FIRE_ARROW = ITEMS
      .register("fire_arrow",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(1)
              .setArmorPenetration(12)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> POISON_ARROW = ITEMS
      .register("poison_arrow",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(1)
              .setArmorPenetration(12)
              .setGroundHitDropChance(95)
              .setEntityHitDropChance(70)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> WOODEN_ARROW = ITEMS
      .register("wooden_arrow",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(1)
              .setArmorPenetration(12)
              .setGroundHitDropChance(75)
              .setEntityHitDropChance(75)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> BOLT = ITEMS
      .register("bolt",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(1)
              .setArmorPenetration(12)
              .setGroundHitDropChance(95)
              .setEntityHitDropChance(85)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> POISON_BOLT = ITEMS
      .register("poison_bolt",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(1)
              .setArmorPenetration(12)
              .setGroundHitDropChance(95)
              .setEntityHitDropChance(70)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<MagazineItem> EXPLOSIVE_BOLT = ITEMS
      .register("explosive_bolt",
          () -> new MagazineItem((MagazineItem.Properties) new MagazineItem.Properties()
              .setSize(1)
              .setArmorPenetration(12)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  // ================================================================================
  // Attachments
  // ================================================================================

  // TODO Add the correct multipliers and their correct values

  public static final RegistryObject<AttachmentItem> M4A1_IRON_SIGHT = ITEMS
      .register("m4a1_iron_sight",
          () -> new AttachmentItem((AttachmentItem.Properties) new AttachmentItem.Properties()
              .addMultiplier(MultiplierType.FOV, 0.5F)
              .setInventorySlot(CraftingInventorySlotType.OVERBARREL_ATTACHMENT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<AttachmentItem> PISTOL_IRON_SIGHT = ITEMS
      .register("pistol_iron_sight",
          () -> new AttachmentItem((AttachmentItem.Properties) new AttachmentItem.Properties()
              .addMultiplier(MultiplierType.FOV, 0.5F)
              .setInventorySlot(CraftingInventorySlotType.OVERBARREL_ATTACHMENT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<AttachmentItem> SCARH_IRON_SIGHT = ITEMS
      .register("scarh_iron_sight",
          () -> new AttachmentItem((AttachmentItem.Properties) new AttachmentItem.Properties()
              .addMultiplier(MultiplierType.FOV, 0.5F)
              .setInventorySlot(CraftingInventorySlotType.OVERBARREL_ATTACHMENT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<AttachmentItem> AKM_IRON_SIGHT = ITEMS
      .register("akm_iron_sight",
          () -> new AttachmentItem((AttachmentItem.Properties) new AttachmentItem.Properties()
              .addMultiplier(MultiplierType.FOV, 0.5F)
              .setInventorySlot(CraftingInventorySlotType.OVERBARREL_ATTACHMENT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<AttachmentItem> RED_DOT_SIGHT = ITEMS
      .register("red_dot_sight",
          () -> new AttachmentItem((AttachmentItem.Properties) new AttachmentItem.Properties()
              .addMultiplier(MultiplierType.FOV, 2.5F)
              .setInventorySlot(CraftingInventorySlotType.OVERBARREL_ATTACHMENT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<AttachmentItem> ACOG_SIGHT = ITEMS
      .register("acog_sight",
          () -> new AttachmentItem((AttachmentItem.Properties) new AttachmentItem.Properties()
              .addMultiplier(MultiplierType.FOV, 3.25F)
              .setInventorySlot(CraftingInventorySlotType.OVERBARREL_ATTACHMENT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<AttachmentItem> LP_SCOPE = ITEMS
      .register("lp_scope",
          () -> new AttachmentItem((AttachmentItem.Properties) new AttachmentItem.Properties()
              .addMultiplier(MultiplierType.FOV, 5F)
              .setInventorySlot(CraftingInventorySlotType.OVERBARREL_ATTACHMENT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<AttachmentItem> HP_SCOPE = ITEMS
      .register("hp_scope",
          () -> new AttachmentItem((AttachmentItem.Properties) new AttachmentItem.Properties()
              .addMultiplier(MultiplierType.FOV, 10F)
              .setInventorySlot(CraftingInventorySlotType.OVERBARREL_ATTACHMENT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<AttachmentItem> SUPPRESSOR = ITEMS
      .register("suppressor",
          () -> new AttachmentItem((AttachmentItem.Properties) new AttachmentItem.Properties()
              .setInventorySlot(CraftingInventorySlotType.MUZZLE_ATTACHMENT)
              .setSuppressesSounds(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<AttachmentItem> TACTICAL_GRIP = ITEMS
      .register("tactical_grip",
          () -> new AttachmentItem((AttachmentItem.Properties) new AttachmentItem.Properties()
              // The following multiplier value differs from 1.6.4
              .addMultiplier(MultiplierType.ACCURACY, 1.25F)
              .setInventorySlot(CraftingInventorySlotType.UNDERBARREL_ATTACHMENT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<AttachmentItem> BIPOD = ITEMS
      .register("bipod",
          () -> new AttachmentItem((AttachmentItem.Properties) new AttachmentItem.Properties()
              // The following multiplier value differs from 1.6.4
              .addMultiplier(MultiplierType.ACCURACY, 1.25F)
              .setInventorySlot(CraftingInventorySlotType.UNDERBARREL_ATTACHMENT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<AttachmentItem> EOTECH_SIGHT = ITEMS
      .register("eotech_sight",
          () -> new AttachmentItem((AttachmentItem.Properties) new AttachmentItem.Properties()
              // The following multiplier value differs from 1.6.4
              .addMultiplier(MultiplierType.FOV, 2.5F)
              .setInventorySlot(CraftingInventorySlotType.OVERBARREL_ATTACHMENT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<AttachmentItem> RETICLE_SIGHT = ITEMS
      .register("reticle_sight",
          () -> new AttachmentItem((AttachmentItem.Properties) new AttachmentItem.Properties()
              // The following multiplier value differs from 1.6.4
              .addMultiplier(MultiplierType.FOV, 2.5F)
              .setInventorySlot(CraftingInventorySlotType.OVERBARREL_ATTACHMENT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  // TODO Show a green light when it is equipped
  public static final RegistryObject<AttachmentItem> LASER_SIGHT = ITEMS
      .register("laser_sight",
          () -> new AttachmentItem((AttachmentItem.Properties) new AttachmentItem.Properties()
              .setHasLaser(true)
              .setInventorySlot(CraftingInventorySlotType.UNDERBARREL_ATTACHMENT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  // ================================================================================
  // Guns
  // ================================================================================

  // TODO Set accuracy amount for every gun
  public static final RegistryObject<Item> M4A1 = ITEMS
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
              .addAnimation(GunItem.AnimationType.SHOOT, RifleShootAnimation::new)
              // Magazines
              .addAcceptedMagazine(STANAG_20_ROUND_MAGAZINE)
              .addAcceptedMagazine(STANAG_30_ROUND_MAGAZINE)
              .addAcceptedMagazine(STANAG_DRUM_MAGAZINE)
              .addAcceptedMagazine(STANAG_BOX_MAGAZINE)
              // Attachments
              .addDefaultAttachment(M4A1_IRON_SIGHT)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedAttachment(LP_SCOPE)
              .addAcceptedAttachment(HP_SCOPE)
              .addAcceptedAttachment(TACTICAL_GRIP)
              .addAcceptedAttachment(BIPOD)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedAttachment(EOTECH_SIGHT)
              // Paints
              .addAcceptedPaint(CYREX_PAINT)
              .addAcceptedPaint(ASMO_PAINT)
              .addAcceptedPaint(DIAMOND_PAINT)
              .addAcceptedPaint(INFERNO_PAINT)
              .addAcceptedPaint(SCORCHED_PAINT)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<Item> SCARH = ITEMS
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
              .addAnimation(GunItem.AnimationType.SHOOT, RifleShootAnimation::new)
              // Magazines
              .addAcceptedMagazine(STANAG_20_ROUND_MAGAZINE)
              .addAcceptedMagazine(STANAG_30_ROUND_MAGAZINE)
              .addAcceptedMagazine(STANAG_DRUM_MAGAZINE)
              .addAcceptedMagazine(STANAG_BOX_MAGAZINE)
              // Attachments
              .addDefaultAttachment(SCARH_IRON_SIGHT)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedAttachment(LP_SCOPE)
              .addAcceptedAttachment(HP_SCOPE)
              .addAcceptedAttachment(TACTICAL_GRIP)
              .addAcceptedAttachment(BIPOD)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedAttachment(EOTECH_SIGHT)
              // Paints
              .addAcceptedPaint(ASMO_PAINT)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<Item> AK47 = ITEMS
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
              .addAnimation(GunItem.AnimationType.SHOOT, RifleShootAnimation::new)
              .addAcceptedMagazine(AK47_30_ROUND_MAGAZINE)
              .addDefaultAttachment(AKM_IRON_SIGHT)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedAttachment(LP_SCOPE)
              .addAcceptedAttachment(HP_SCOPE)
              .addAcceptedAttachment(TACTICAL_GRIP)
              .addAcceptedAttachment(BIPOD)
              .addAcceptedAttachment(EOTECH_SIGHT)
              .addAcceptedAttachment(SUPPRESSOR)
              // Paints
              .addAcceptedPaint(VULCAN_PAINT)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<Item> FNFAL = ITEMS
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
              .addAnimation(GunItem.AnimationType.SHOOT, RifleShootAnimation::new)
              .addAcceptedMagazine(FNFAL_MAGAZINE)
              .addDefaultAttachment(M4A1_IRON_SIGHT)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedAttachment(LP_SCOPE)
              .addAcceptedAttachment(HP_SCOPE)
              .addAcceptedAttachment(TACTICAL_GRIP)
              .addAcceptedAttachment(BIPOD)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<Item> ACR = ITEMS
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
              .addAnimation(GunItem.AnimationType.SHOOT, RifleShootAnimation::new)
              .addAcceptedMagazine(ACR_MAGAZINE)
              .addDefaultAttachment(M4A1_IRON_SIGHT)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedAttachment(LP_SCOPE)
              .addAcceptedAttachment(HP_SCOPE)
              .addAcceptedAttachment(TACTICAL_GRIP)
              .addAcceptedAttachment(EOTECH_SIGHT)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<Item> G36C = ITEMS
      .register("g36c",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(92)
              .setDamage(8)
              .setReloadDurationTicks(20 * 4)
              .setAccuracy(0.8F)
              .addFireMode(FireMode.AUTO)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.G36C_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.M4A1_RELOAD)
              .addAnimation(GunItem.AnimationType.SHOOT, RifleShootAnimation::new)
              .addAcceptedMagazine(G36C_MAGAZINE)
              .addDefaultAttachment(M4A1_IRON_SIGHT)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(TACTICAL_GRIP)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedAttachment(EOTECH_SIGHT)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<Item> HK417 = ITEMS
      .register("hk417",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(100)
              .setDamage(8)
              .setReloadDurationTicks(20 * 4)
              .setAccuracy(0.8F)
              .addFireMode(FireMode.AUTO)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.HK417_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_RPK_SHOOT)
              .setReloadSound(ModSoundEvents.M4A1_RELOAD)
              .addAnimation(GunItem.AnimationType.SHOOT, RifleShootAnimation::new)
              .addAcceptedMagazine(HK417_MAGAZINE)
              .addDefaultAttachment(M4A1_IRON_SIGHT)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(TACTICAL_GRIP)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedAttachment(LP_SCOPE)
              .addAcceptedAttachment(HP_SCOPE)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedAttachment(EOTECH_SIGHT)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<Item> MPT55 = ITEMS
      .register("mpt55",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(70)
              .setDamage(6)
              .setReloadDurationTicks(20 * 4)
              .setAccuracy(0.8F)
              .addFireMode(FireMode.BURST)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.MPT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.M4A1_RELOAD)
              .addAnimation(GunItem.AnimationType.SHOOT, RifleShootAnimation::new)
              .addAcceptedMagazine(MPT55_MAGAZINE)
              .addDefaultAttachment(M4A1_IRON_SIGHT)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedAttachment(LP_SCOPE)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<Item> TASER = ITEMS
      .register("taser",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(2000)
              .setDamage(7)
              .setReloadDurationTicks(20 * 3)
              .setAccuracy(0.9F)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.TASER_SHOOT)
              .addAnimation(GunItem.AnimationType.SHOOT, PistolShootAnimation::new)
              .addAcceptedMagazine(TASER_CARTRIDGE)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<Item> M1911 = ITEMS
      .register("m1911",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(120)
              .setDamage(7)
              .setReloadDurationTicks((int) (20 * 2.25F))
              .setAccuracy(0.9F)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.M1911_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.M1911_RELOAD)
              .addAnimation(GunItem.AnimationType.SHOOT, PistolShootAnimation::new)
              .addAcceptedMagazine(M1911_MAGAZINE)
              .addDefaultAttachment(PISTOL_IRON_SIGHT)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<Item> G18 = ITEMS
      .register("g18",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(120)
              .setDamage(7)
              .setReloadDurationTicks((int) (20 * 2.25F))
              .setAccuracy(0.9F)
              .addFireMode(FireMode.AUTO)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.G18_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_RPK_SHOOT)
              .setReloadSound(ModSoundEvents.M1911_RELOAD)
              .addAnimation(GunItem.AnimationType.SHOOT, PistolShootAnimation::new)
              .addAcceptedMagazine(G18_MAGAZINE)
              .addDefaultAttachment(PISTOL_IRON_SIGHT)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedPaint(FADE_PAINT)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<Item> M9 = ITEMS
      .register("m9",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(120)
              .setDamage(7)
              .setReloadDurationTicks((int) (20 * 2.25F))
              .setAccuracy(0.9F)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.M9_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M9_SHOOT)
              .setReloadSound(ModSoundEvents.M9_RELOAD)
              .addAnimation(GunItem.AnimationType.SHOOT, PistolShootAnimation::new)
              .addAcceptedMagazine(M9_MAGAZINE)
              .addDefaultAttachment(PISTOL_IRON_SIGHT)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<Item> DESERT_EAGLE = ITEMS
      .register("desert_eagle",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(120)
              .setDamage(8)
              .setReloadDurationTicks((int) (20 * 2.25F))
              .setAccuracy(0.7F)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.DESERT_EAGLE_SHOOT)
              .setReloadSound(ModSoundEvents.M9_RELOAD)
              .addAnimation(GunItem.AnimationType.SHOOT, PistolShootAnimation::new)
              .addAcceptedMagazine(DESERT_EAGLE_MAGAZINE)
              .addAcceptedPaint(INFERNO_PAINT)
              .addAcceptedPaint(SCORCHED_PAINT)
              .addDefaultAttachment(PISTOL_IRON_SIGHT)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<Item> P250 = ITEMS
      .register("p250",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(120)
              .setDamage(6)
              .setReloadDurationTicks((int) (20 * 2.25F))
              .setAccuracy(0.7F)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.P250_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M9_SHOOT)
              .setReloadSound(ModSoundEvents.M9_RELOAD)
              .addAnimation(GunItem.AnimationType.SHOOT, PistolShootAnimation::new)
              .addAcceptedMagazine(P250_MAGAZINE)
              .addDefaultAttachment(PISTOL_IRON_SIGHT)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<Item> MAGNUM = ITEMS
      .register("magnum",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(80)
              .setDamage(7)
              .setReloadDurationTicks((int) (20 * 2.75F))
              .setAccuracy(0.9F)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.MAGNUM_SHOOT)
              .setReloadSound(ModSoundEvents.MAGNUM_RELOAD)
              .addAnimation(GunItem.AnimationType.SHOOT, PistolShootAnimation::new)
              .addAcceptedMagazine(MAGNUM_AMMO)
              .addDefaultAttachment(PISTOL_IRON_SIGHT)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<Item> FN57 = ITEMS
      .register("fn57",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(140)
              .setDamage(8)
              .setReloadDurationTicks((int) (20 * 2.75F))
              .setAccuracy(0.9F)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.FN57_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.FN57_RELOAD)
              .addAnimation(GunItem.AnimationType.SHOOT, PistolShootAnimation::new)
              .addAcceptedMagazine(FN57_MAGAZINE)
              .addDefaultAttachment(PISTOL_IRON_SIGHT)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<Item> MAC10 = ITEMS
      .register("mac10",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(100)
              .setDamage(6)
              .setReloadDurationTicks(20 * 4)
              .setAccuracy(0.9F)
              .addFireMode(FireMode.AUTO)
              .setShootSound(ModSoundEvents.MAC10_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.M4A1_RELOAD)
              .addAnimation(GunItem.AnimationType.SHOOT, SubmachineShootAnimation::new)
              .addAcceptedMagazine(MAC10_MAGAZINE)
              .addAcceptedMagazine(MAC10_EXTENDED_MAGAZINE)
              .addDefaultAttachment(PISTOL_IRON_SIGHT)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedPaint(FADE_PAINT)
              .addAcceptedPaint(UV_PAINT)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  // TODO Reminder: P90 uses two different iron sights at the same time.
  // See RenderP90.java (1.6.4)
  public static final RegistryObject<Item> P90 = ITEMS
      .register("p90",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(100)
              .setDamage(5)
              .setReloadDurationTicks((int) (20 * 1.5F))
              .setAccuracy(0.9F)
              .addFireMode(FireMode.AUTO)
              .setShootSound(ModSoundEvents.P90_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_P90_SHOOT)
              .setReloadSound(ModSoundEvents.P90_RELOAD)
              .addAnimation(GunItem.AnimationType.SHOOT, SubmachineShootAnimation::new)
              .addAcceptedMagazine(P90_MAGAZINE)
              .addDefaultAttachment(PISTOL_IRON_SIGHT)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedPaint(RUBY_PAINT)
              .addAcceptedPaint(GEM_PAINT)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<Item> VECTOR = ITEMS
      .register("vector",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(85)
              .setDamage(5)
              .setReloadDurationTicks(20 * 4)
              .setAccuracy(0.9F)
              .addFireMode(FireMode.AUTO)
              .setShootSound(ModSoundEvents.VECTOR_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.M4A1_RELOAD)
              .addAnimation(GunItem.AnimationType.SHOOT, SubmachineShootAnimation::new)
              .addAcceptedMagazine(VECTOR_MAGAZINE)
              .addDefaultAttachment(PISTOL_IRON_SIGHT)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedPaint(SLAUGHTER_PAINT)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<Item> MP5A5 = ITEMS
      .register("mp5a5",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(85)
              .setDamage(7)
              .setReloadDurationTicks(20 * 1)
              .setAccuracy(0.9F)
              .addFireMode(FireMode.AUTO)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.MP5A5_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_MP5A5_SHOOT)
              .setReloadSound(ModSoundEvents.MP5A5_RELOAD)
              .addAnimation(GunItem.AnimationType.SHOOT, SubmachineShootAnimation::new)
              .addAcceptedMagazine(MP5A5_21_ROUND_MAGAZINE)
              .addAcceptedMagazine(MP5A5_35_ROUND_MAGAZINE)
              .addDefaultAttachment(PISTOL_IRON_SIGHT)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  // TODO Reminder: Sporter22 uses two different iron sights at the same time.
  // See RenderSporter.java (1.6.4)
  public static final RegistryObject<Item> SPORTER22 = ITEMS
      .register("sporter22",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(200)
              .setDamage(7)
              .setReloadDurationTicks((int) (20 * 2.75F))
              .setAccuracy(0.9F)
              .setCrosshair(false)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.SPORTER22_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_RPK_SHOOT)
              .setReloadSound(ModSoundEvents.M107_RELOAD)
              .addAnimation(GunItem.AnimationType.SHOOT, RifleShootAnimation::new)
              .addAcceptedMagazine(SPORTER22_MAGAZINE)
              .addDefaultAttachment(PISTOL_IRON_SIGHT)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedAttachment(LP_SCOPE)
              .addAcceptedAttachment(HP_SCOPE)
              .addAcceptedAttachment(TACTICAL_GRIP)
              .addAcceptedAttachment(BIPOD)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  // TODO Reminder: M107 uses two different iron sights at the same time.
  // See RenderM107.java (1.6.4)
  public static final RegistryObject<Item> M107 = ITEMS
      .register("m107",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(750)
              .setDamage(20)
              .setReloadDurationTicks((int) (20 * 2.75F))
              .setAccuracy(0.9F)
              .setCrosshair(false)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.M107_SHOOT)
              .setReloadSound(ModSoundEvents.M107_RELOAD)
              .addAnimation(GunItem.AnimationType.SHOOT, RifleShootAnimation::new)
              .addAcceptedMagazine(M107_MAGAZINE)
              .addAcceptedMagazine(M107_AP_MAGAZINE)
              .addDefaultAttachment(PISTOL_IRON_SIGHT)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedAttachment(LP_SCOPE)
              .addAcceptedAttachment(HP_SCOPE)
              .addAcceptedAttachment(TACTICAL_GRIP)
              .addAcceptedAttachment(BIPOD)
              .addAcceptedPaint(CANDY_APPLE_PAINT)
              .addAcceptedPaint(DIAMOND_PAINT)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  // TODO Reminder: AS50 uses two different iron sights at the same time.
  // See RenderAS50.java (1.6.4)
  public static final RegistryObject<Item> AS50 = ITEMS
      .register("as50",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(170)
              .setDamage(14)
              .setReloadDurationTicks((int) (20 * 1.5F))
              .setAccuracy(0.9F)
              .setCrosshair(false)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.AS50_SHOOT)
              .setReloadSound(ModSoundEvents.AS50_RELOAD)
              .addAnimation(GunItem.AnimationType.SHOOT, RifleShootAnimation::new)
              .addAcceptedMagazine(AS50_MAGAZINE)
              .addAcceptedMagazine(AS50_AP_MAGAZINE)
              .addDefaultAttachment(PISTOL_IRON_SIGHT)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedAttachment(LP_SCOPE)
              .addAcceptedAttachment(HP_SCOPE)
              .addAcceptedAttachment(TACTICAL_GRIP)
              .addAcceptedAttachment(BIPOD)
              .addAcceptedPaint(CANDY_APPLE_PAINT)
              .addAcceptedPaint(DIAMOND_PAINT)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<Item> M1GARAND = ITEMS
      .register("m1garand",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(170)
              .setDamage(10)
              .setReloadDurationTicks((int) (20 * 1.5F))
              .setAccuracy(0.9F)
              .setCrosshair(false)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.M1GARAND_SHOOT)
              .setReloadSound(ModSoundEvents.AS50_RELOAD)
              .addAnimation(GunItem.AnimationType.SHOOT, RifleShootAnimation::new)
              .addAcceptedMagazine(M1GARAND_MAGAZINE)
              .addDefaultAttachment(SCARH_IRON_SIGHT)
              .addAcceptedAttachment(LP_SCOPE)
              .addAcceptedAttachment(HP_SCOPE)
              .addAcceptedAttachment(BIPOD)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  // TODO Reminder: AWP uses two different iron sights at the same time.
  // See RenderAWP.java (1.6.4)
  public static final RegistryObject<Item> AWP = ITEMS
      .register("awp",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(1200)
              .setDamage(20)
              .setReloadDurationTicks((int) (20 * 2.75F))
              .setAccuracy(0.9F)
              .setCrosshair(false)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.AWP_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.AWP_RELOAD)
              .addAnimation(GunItem.AnimationType.SHOOT, RifleShootAnimation::new)
              .addAcceptedMagazine(AWP_MAGAZINE)
              .addDefaultAttachment(SCARH_IRON_SIGHT)
              .addAcceptedAttachment(LP_SCOPE)
              .addAcceptedAttachment(HP_SCOPE)
              .addAcceptedAttachment(BIPOD)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedPaint(DRAGON_PAINT)
              .addAcceptedPaint(SCORCHED_PAINT)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<Item> DMR = ITEMS
      .register("dmr",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(170)
              .setDamage(15)
              .setReloadDurationTicks((int) (20 * 2.75F))
              .setAccuracy(0.9F)
              .setCrosshair(false)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.DMR_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_RPK_SHOOT)
              .setReloadSound(ModSoundEvents.DMR_RELOAD)
              .addAnimation(GunItem.AnimationType.SHOOT, RifleShootAnimation::new)
              .addAcceptedMagazine(DMR_MAGAZINE)
              .addDefaultAttachment(M4A1_IRON_SIGHT)
              .addAcceptedAttachment(LP_SCOPE)
              .addAcceptedAttachment(HP_SCOPE)
              .addAcceptedAttachment(BIPOD)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedPaint(DIAMOND_PAINT)
              .addAcceptedPaint(SCORCHED_PAINT)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  // TODO Reminder: Trenchgun uses only the front part of the iron sight model.
  // See RenderTrenchGun.java (1.6.4)
  public static final RegistryObject<Item> TRENCHGUN = ITEMS
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
              .addAnimation(GunItem.AnimationType.SHOOT, PistolShootAnimation::new)
              .addAcceptedMagazine(TRENCHGUN_SHELLS)
              .addDefaultAttachment(SCARH_IRON_SIGHT)
              .addAcceptedAttachment(TACTICAL_GRIP)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  // TODO Reminder: M240B uses two different iron sights at the same time.
  // See RenderM240B.java (1.6.4)
  public static final RegistryObject<Item> M240B = ITEMS
      .register("m240b",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(85)
              .setDamage(8)
              .setReloadDurationTicks((int) (20 * 1.5F))
              .setAccuracy(0.9F)
              .addFireMode(FireMode.AUTO)
              .setShootSound(ModSoundEvents.M240B_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M240B_SHOOT)
              .setReloadSound(ModSoundEvents.M240B_RELOAD)
              .addAnimation(GunItem.AnimationType.SHOOT, SubmachineShootAnimation::new)
              .addAcceptedMagazine(M240B_MAGAZINE)
              .addDefaultAttachment(SCARH_IRON_SIGHT)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(TACTICAL_GRIP)
              .addAcceptedAttachment(BIPOD)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedAttachment(EOTECH_SIGHT)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<Item> RPK = ITEMS
      .register("rpk",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(92)
              .setDamage(6)
              .setReloadDurationTicks((int) (20 * 3.75F))
              .setAccuracy(0.9F)
              .addFireMode(FireMode.AUTO)
              .setShootSound(ModSoundEvents.RPK_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M240B_SHOOT)
              .setReloadSound(ModSoundEvents.RPK_RELOAD)
              .addAnimation(GunItem.AnimationType.SHOOT, RifleShootAnimation::new)
              .addAcceptedMagazine(RPK_MAGAZINE)
              .addAcceptedMagazine(RPK_DRUM_MAGAZINE)
              .addDefaultAttachment(AKM_IRON_SIGHT)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<Item> MINIGUN = ITEMS
      .register("minigun",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setAimable(false)
              .setFireRate(25)
              .setDamage(4)
              .setReloadDurationTicks((int) (20 * 1.5F))
              .setAccuracy(0.9F)
              .addFireMode(FireMode.AUTO)
              .setShootSound(ModSoundEvents.MINIGUN_SHOOT)
              .setReloadSound(ModSoundEvents.M240B_RELOAD)

              // TODO Create the following animation from legacy
              // .addAnimation(GunItem.AnimationType.SHOOT, MinigunShootAnimation::new)
              .addAnimation(GunItem.AnimationType.SHOOT, RifleShootAnimation::new)

              .addAcceptedMagazine(MINIGUN_MAGAZINE)
              .addAcceptedPaint(FURY_PAINT)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  // TODO Reminder: Crossbow uses only the back part of the iron sight model.
  // See RenderCrossbow.java (1.6.4)
  public static final RegistryObject<Item> CROSSBOW = ITEMS
      .register("crossbow",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(1000)
              .setDamage(17)
              .setReloadDurationTicks((int) (20 * 2F))
              .setAccuracy(0.9F)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.CROSSBOW_SHOOT)
              .addAnimation(GunItem.AnimationType.SHOOT, RifleShootAnimation::new)
              .addAcceptedMagazine(CROSSBOW_BOLT)
              .addDefaultAttachment(SCARH_IRON_SIGHT)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  // TODO Reminder: Mossberg uses only the front part of the iron sight model.
  // See RenderMossberg.java (1.6.4)
  public static final RegistryObject<Item> MOSSBERG = ITEMS
      .register("mossberg",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setBulletAmountToFire(8)
              .setFireRate(300)
              .setDamage(3)
              .setReloadDurationTicks(20 * 1)
              .setAccuracy(0.9F)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.MOSSBERG_SHOOT)
              .setReloadSound(ModSoundEvents.MOSSBERG_RELOAD)
              .addAnimation(GunItem.AnimationType.SHOOT, PistolShootAnimation::new)
              .addAcceptedMagazine(MOSSBERG_SLUGS)
              .addDefaultAttachment(SCARH_IRON_SIGHT)
              .addAcceptedAttachment(TACTICAL_GRIP)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  // TODO Reminder: MK48MOD uses two different iron sights at the same time.
  // See RenderMK48Mod1.java (1.6.4)
  public static final RegistryObject<Item> MK48MOD = ITEMS
      .register("mk48mod",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(92)
              .setDamage(7)
              .setReloadDurationTicks((int) (20 * 1.5F))
              .setAccuracy(0.9F)
              .addFireMode(FireMode.AUTO)
              .setShootSound(ModSoundEvents.MK48MOD_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_MK48MOD_SHOOT)
              .setReloadSound(ModSoundEvents.M240B_RELOAD)
              .addAnimation(GunItem.AnimationType.SHOOT, SubmachineShootAnimation::new)
              .addAcceptedMagazine(MK48MOD_MAGAZINE)
              .addDefaultAttachment(SCARH_IRON_SIGHT)
              .addAcceptedAttachment(RED_DOT_SIGHT)
              .addAcceptedAttachment(TACTICAL_GRIP)
              .addAcceptedAttachment(BIPOD)
              .addAcceptedAttachment(SUPPRESSOR)
              .addAcceptedAttachment(EOTECH_SIGHT)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  // TODO Reminder: Basic Crossbow uses only the back part of the iron sight model.
  // See RenderBasicCrossbow.java (1.6.4)
  public static final RegistryObject<Item> BASIC_CROSSBOW = ITEMS
      .register("basic_crossbow",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(1000)
              .setDamage(4)
              .setReloadDurationTicks((int) (20 * 2F))
              .setAccuracy(0.9F)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.CROSSBOW_SHOOT)
              .addAnimation(GunItem.AnimationType.SHOOT, RifleShootAnimation::new)
              .addAcceptedMagazine(BOLT)
              .addAcceptedMagazine(POISON_BOLT)
              .addAcceptedMagazine(EXPLOSIVE_BOLT)
              .addDefaultAttachment(SCARH_IRON_SIGHT)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  // TODO Reminder: Military Crossbow uses only the back part of the iron sight model.
  // See RenderMilitaryCrossbow.java (1.6.4)
  public static final RegistryObject<Item> MILITARY_CROSSBOW = ITEMS
      .register("military_crossbow",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(1000)
              .setDamage(5)
              .setReloadDurationTicks((int) (20 * 2F))
              .setAccuracy(0.9F)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.CROSSBOW_SHOOT)
              .addAnimation(GunItem.AnimationType.SHOOT, RifleShootAnimation::new)
              .addAcceptedMagazine(BOLT)
              .addAcceptedMagazine(POISON_BOLT)
              .addAcceptedMagazine(EXPLOSIVE_BOLT)
              .addDefaultAttachment(SCARH_IRON_SIGHT)
              .addAcceptedAttachment(ACOG_SIGHT)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<Item> LIGHT_BOW = ITEMS
      .register("light_bow",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(1000)
              .setDamage(4)
              .setReloadDurationTicks((int) (20 * 2F))
              .setAccuracy(0.9F)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.CROSSBOW_SHOOT)
              .addAnimation(GunItem.AnimationType.SHOOT, RifleShootAnimation::new)
              .addAcceptedMagazine(WOODEN_ARROW)
              .addAcceptedMagazine(POISON_ARROW)
              .addAcceptedMagazine(COMBAT_ARROW)
              .addAcceptedMagazine(FIRE_ARROW)
              .addAcceptedMagazine(EXPLOSIVE_ARROW)
              .addAcceptedAttachment(RETICLE_SIGHT)
              .addAcceptedAttachment(LASER_SIGHT)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<Item> COMBAT_BOW = ITEMS
      .register("combat_bow",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(1000)
              .setDamage(5)
              .setReloadDurationTicks((int) (20 * 2F))
              .setAccuracy(0.9F)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.CROSSBOW_SHOOT)
              .addAnimation(GunItem.AnimationType.SHOOT, RifleShootAnimation::new)
              .addAcceptedMagazine(WOODEN_ARROW)
              .addAcceptedMagazine(POISON_ARROW)
              .addAcceptedMagazine(COMBAT_ARROW)
              .addAcceptedMagazine(FIRE_ARROW)
              .addAcceptedMagazine(EXPLOSIVE_ARROW)
              .addAcceptedAttachment(RETICLE_SIGHT)
              .addAcceptedAttachment(LASER_SIGHT)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<Item> HEAVY_BOW = ITEMS
      .register("heavy_bow",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(1000)
              .setDamage(6)
              .setReloadDurationTicks((int) (20 * 2F))
              .setAccuracy(0.9F)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.CROSSBOW_SHOOT)
              .addAnimation(GunItem.AnimationType.SHOOT, RifleShootAnimation::new)
              .addAcceptedMagazine(WOODEN_ARROW)
              .addAcceptedMagazine(POISON_ARROW)
              .addAcceptedMagazine(COMBAT_ARROW)
              .addAcceptedMagazine(FIRE_ARROW)
              .addAcceptedMagazine(EXPLOSIVE_ARROW)
              .addAcceptedAttachment(RETICLE_SIGHT)
              .addAcceptedAttachment(LASER_SIGHT)
              .addAcceptedPaint(MULTI_PAINT)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  // ================================================================================
  // Grenades
  // ================================================================================

  public static final RegistryObject<Item> FIRE_GRENADE = ITEMS
      .register("fire_grenade",
          () -> new GrenadeItem((GrenadeItem.Properties) new GrenadeItem.Properties()
              .setTyoe(GrenadeType.FIRE)
              .setExplosionRadius(1f)
              .setTimeUntilExplosion(10)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<Item> GAS_GRENADE = ITEMS
      .register("gas_grenade",
          () -> new GrenadeItem((GrenadeItem.Properties) new GrenadeItem.Properties()
              .setTyoe(GrenadeType.GAS)
              .setExplosionRadius(1f)
              .setTimeUntilExplosion(10)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  public static final RegistryObject<Item> SMOKE_GRENADE = ITEMS
      .register("smoke_grenade",
          () -> new GrenadeItem((GrenadeItem.Properties) new GrenadeItem.Properties()
              .setTyoe(GrenadeType.SMOKE)
              .setExplosionRadius(1f)
              .setTimeUntilExplosion(10)
              .group(ModItemGroups.CRAFTING_DEAD_COMBAT)));

  // ================================================================================
  // Tools
  // ================================================================================

  public static final RegistryObject<Item> CAN_OPENER = ITEMS
      .register("can_opener", () -> new ToolItem(
          new Item.Properties().maxDamage(8).group(ModItemGroups.CRAFTING_DEAD_MISC)));

  public static final RegistryObject<Item> SCREWDRIVER = ITEMS
      .register("screwdriver", () -> new ToolItem(
          new Item.Properties().maxDamage(4).group(ModItemGroups.CRAFTING_DEAD_MISC)));

  public static final RegistryObject<Item> MULTI_TOOL = ITEMS
      .register("multi_tool", () -> new MeleeWeaponItem(8, -2.4F,
          new Item.Properties().maxDamage(20).group(ModItemGroups.CRAFTING_DEAD_MISC)));

  // ================================================================================
  // Consumable
  // ================================================================================

  public static final RegistryObject<Item> EMPTY_WATER_BOTTLE = ITEMS
      .register("empty_water_bottle",
          () -> new FillableItem((FillableItem.Properties) new FillableItem.Properties()
              .setFullItem(new ResourceLocation(CraftingDead.ID, "bottled_water"))
              .setBlockPredicate(
                  (blockPos, blockState) -> blockState.getFluidState().getFluid() == Fluids.WATER)
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> BOTTLED_WATER = ITEMS
      .register("bottled_water",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 7), 1.0F)
              .containerItem(EMPTY_WATER_BOTTLE.get())
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> POWER_BAR = ITEMS
      .register("power_bar", () -> new Item(
          new Item.Properties().food(ModFoods.POWER_BAR).group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> CANDY_BAR = ITEMS
      .register("candy_bar", () -> new Item(
          new Item.Properties().food(ModFoods.CANDY_BAR).group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> CEREAL = ITEMS
      .register("cereal", () -> new Item(
          new Item.Properties().food(ModFoods.CEREAL).group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> CANNED_CORN = ITEMS
      .register("canned_corn",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> OPEN_CANNED_CORN = ITEMS
      .register("open_canned_corn",
          () -> new Item(new Item.Properties()
              .food(ModFoods.CANNED_CORN)
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> CANNED_BEANS = ITEMS
      .register("canned_beans",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> OPEN_CANNED_BEANS = ITEMS
      .register("open_canned_beans",
          () -> new Item(new Item.Properties()
              .food(ModFoods.CANNED_BEANS)
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> CANNED_TUNA = ITEMS
      .register("canned_tuna",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> OPEN_CANNED_TUNA = ITEMS
      .register("open_canned_tuna",
          () -> new Item(new Item.Properties()
              .food(ModFoods.CANNED_TUNA)
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> CANNED_PEACH = ITEMS
      .register("canned_peach",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> OPEN_CANNED_PEACH = ITEMS
      .register("open_canned_peach",
          () -> new Item(new Item.Properties()
              .food(ModFoods.CANNED_PEACH)
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> CANNED_PASTA = ITEMS
      .register("canned_pasta",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> OPEN_CANNED_PASTA = ITEMS
      .register("open_canned_pasta",
          () -> new Item(new Item.Properties()
              .food(ModFoods.CANNED_PASTA)
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> CANNED_BACON = ITEMS
      .register("canned_bacon",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> OPEN_CANNED_BACON = ITEMS
      .register("open_canned_bacon",
          () -> new Item(new Item.Properties()
              .food(ModFoods.CANNED_BACON)
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> CANNED_CUSTARD = ITEMS
      .register("canned_custard",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> OPEN_CANNED_CUSTARD = ITEMS
      .register("open_canned_custard",
          () -> new Item(new Item.Properties()
              .food(ModFoods.CANNED_CUSTARD)
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> CANNED_PICKLES = ITEMS
      .register("canned_pickles",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> OPEN_CANNED_PICKLES = ITEMS
      .register("open_canned_pickles",
          () -> new Item(new Item.Properties()
              .food(ModFoods.CANNED_PICKLES)
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> CANNED_DOG_FOOD = ITEMS
      .register("canned_dog_food",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> OPEN_CANNED_DOG_FOOD = ITEMS
      .register("open_canned_dog_food",
          () -> new Item(new Item.Properties()
              .food(ModFoods.CANNED_DOG_FOOD)
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> CANNED_TOMATO_SOUP = ITEMS
      .register("canned_tomato_soup",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> OPEN_CANNED_TOMATO_SOUP = ITEMS
      .register("open_canned_tomato_soup",
          () -> new Item(new Item.Properties()
              .food(ModFoods.CANNED_TOMATO_SOUP)
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> EMPTY_ORANGE_SODA = ITEMS
      .register("empty_orange_soda",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> ORANGE_SODA = ITEMS
      .register("orange_soda",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 3), 1.0F)
              .containerItem(EMPTY_ORANGE_SODA.get())
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> EMPTY_ICED_TEA = ITEMS
      .register("empty_iced_tea",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> ICED_TEA = ITEMS
      .register("iced_tea",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 3), 1.0F)
              .containerItem(EMPTY_ICED_TEA.get())
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> EMPTY_JUICE_POUCH = ITEMS
      .register("empty_juice_pouch",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> JUICE_POUCH = ITEMS
      .register("juice_pouch",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 1), 1.0F)
              .containerItem(EMPTY_JUICE_POUCH.get())
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> EMPTY_PEPE_SODA = ITEMS
      .register("empty_pepe_soda",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> PEPE_SODA = ITEMS
      .register("pepe_soda",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 3), 1.0F)
              .containerItem(EMPTY_PEPE_SODA.get())
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> EMPTY_LEMON_SODA = ITEMS
      .register("empty_lemon_soda",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> LEMON_SODA = ITEMS
      .register("lemon_soda",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 3), 1.0F)
              .containerItem(EMPTY_LEMON_SODA.get())
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> EMPTY_COLA_SODA = ITEMS
      .register("empty_cola_soda",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> COLA_SODA = ITEMS
      .register("cola_soda",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 3), 1.0F)
              .containerItem(EMPTY_COLA_SODA.get())
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> EMPTY_MILK_CARTON = ITEMS
      .register("empty_milk_carton",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> MILK_CARTON = ITEMS
      .register("milk_carton",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 7), 1.0F)
              .containerItem(EMPTY_MILK_CARTON.get())
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> ROTTON_MILK = ITEMS
      .register("rotten_milk",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 3), 1.0F)
              .effect(() -> new EffectInstance(Effects.HUNGER, 600, 1), 0.2F)
              .containerItem(EMPTY_MILK_CARTON.get())
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> EMPTY_COLA_POP = ITEMS
      .register("empty_cola_pop",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> COLA_POP = ITEMS
      .register("cola_pop",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 5), 1.0F)
              .containerItem(EMPTY_COLA_POP.get())
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> EMPTY_IRON_BREW = ITEMS
      .register("empty_iron_brew",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> IRON_BREW = ITEMS
      .register("iron_brew",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 8), 1.0F)
              .containerItem(EMPTY_IRON_BREW.get())
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> EMPTY_SPRITE = ITEMS
      .register("empty_sprite",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> SPRITE = ITEMS
      .register("sprite",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 8), 1.0F)
              .containerItem(EMPTY_SPRITE.get())
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> EMPTY_ZOMBIE_ENERGY = ITEMS
      .register("empty_zombie_energy",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> ZOMBIE_ENERGY = ITEMS
      .register("zombie_energy",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 8), 1.0F)
              .containerItem(EMPTY_ZOMBIE_ENERGY.get())
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> MRE = ITEMS
      .register("mre", () -> new Item(
          new Item.Properties().food(ModFoods.MRE).group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> ORANGE = ITEMS
      .register("orange", () -> new Item(
          new Item.Properties().food(ModFoods.ORANGE).group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> ROTTEN_ORANGE = ITEMS
      .register("rotten_orange",
          () -> new Item(new Item.Properties()
              .food(ModFoods.ROTTEN_ORANGE)
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> PEAR = ITEMS
      .register("pear", () -> new Item(
          new Item.Properties().food(ModFoods.PEAR).group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> ROTTEN_PEAR = ITEMS
      .register("rotten_pear",
          () -> new Item(new Item.Properties()
              .food(ModFoods.ROTTEN_PEAR)
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> RICE_BAG = ITEMS
      .register("rice_bag", () -> new Item(
          new Item.Properties().food(ModFoods.RICE_BAG).group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> APPLE = ITEMS
      .register("apple", () -> new Item(
          new Item.Properties().food(ModFoods.APPLE).group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> ROTTEN_APPLE = ITEMS
      .register("rotten_apple",
          () -> new Item(new Item.Properties()
              .food(ModFoods.ROTTEN_APPLE)
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> NOODLES = ITEMS
      .register("noodles", () -> new Item(
          new Item.Properties().food(ModFoods.NOODLE_CUP).group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> WATERMELON = ITEMS
      .register("watermelon", () -> new Item(
          new Item.Properties().food(ModFoods.WATERMELON).group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> ROTTEN_WATERMELON = ITEMS
      .register("rotten_watermelon",
          () -> new Item(new Item.Properties()
              .food(ModFoods.ROTTEN_WATERMELON)
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> BLUEBERRY = ITEMS
      .register("blueberry", () -> new Item(
          new Item.Properties().food(ModFoods.BLUEBERRY).group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> ROTTEN_BLUEBERRY = ITEMS
      .register("rotten_blueberry",
          () -> new Item(new Item.Properties()
              .food(ModFoods.ROTTEN_BLUEBERRY)
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> RASPBERRY = ITEMS
      .register("raspberry", () -> new Item(
          new Item.Properties().food(ModFoods.RASPBERRY).group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> ROTTEN_RASPBERRY = ITEMS
      .register("rotten_raspberry",
          () -> new Item(new Item.Properties()
              .food(ModFoods.ROTTEN_RASPBERRY)
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> CHIPS = ITEMS
      .register("chips", () -> new Item(
          new Item.Properties().food(ModFoods.CHIPS).group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> RANCH_CHIPS = ITEMS
      .register("ranch_chips",
          () -> new Item(new Item.Properties()
              .food(ModFoods.RANCH_CHIPS)
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> CHEESE_CHIPS = ITEMS
      .register("cheese_chips",
          () -> new Item(new Item.Properties()
              .food(ModFoods.CHEESE_CHIPS)
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> SALT_CHIPS = ITEMS
      .register("salt_chips", () -> new Item(
          new Item.Properties().food(ModFoods.SALT_CHIPS).group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> POPCORN = ITEMS
      .register("popcorn", () -> new Item(
          new Item.Properties().food(ModFoods.POPCORN).group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> NUTTY_CEREAL = ITEMS
      .register("nutty_cereal",
          () -> new Item(new Item.Properties()
              .food(ModFoods.NUTTY_CEREAL)
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> EMERALD_CEREAL = ITEMS
      .register("emerald_cereal",
          () -> new Item(new Item.Properties()
              .food(ModFoods.EMERALD_CEREAL)
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> FLAKE_CEREAL = ITEMS
      .register("flake_cereal",
          () -> new Item(new Item.Properties()
              .food(ModFoods.FLAKE_CEREAL)
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> EMPTY_WATER_CANTEEN = ITEMS
      .register("empty_water_canteen",
          () -> new FillableItem((FillableItem.Properties) new FillableItem.Properties()
              .setFullItem(new ResourceLocation(CraftingDead.ID, "water_canteen"))
              .setBlockPredicate(
                  (blockPos, blockState) -> blockState.getFluidState().getFluid() == Fluids.WATER)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> WATER_CANTEEN = ITEMS
      .register("water_canteen",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 8), 1.0F)
              .containerItem(EMPTY_WATER_CANTEEN.get())
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> EMPTY_FLASK = ITEMS
      .register("empty_flask",
          () -> new FillableItem((FillableItem.Properties) new FillableItem.Properties()
              .setFullItem(new ResourceLocation(CraftingDead.ID, "flask"))
              .setBlockPredicate(
                  (blockPos, blockState) -> blockState.getFluidState().getFluid() == Fluids.WATER)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> FLASK = ITEMS
      .register("flask",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 5), 1.0F)
              .containerItem(EMPTY_FLASK.get())
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> TEA_FLASK = ITEMS
      .register("tea_flask",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 5), 1.0F)
              .containerItem(EMPTY_FLASK.get())
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  public static final RegistryObject<Item> COFFEE_FLASK = ITEMS
      .register("coffee_flask",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 5), 1.0F)
              .containerItem(EMPTY_FLASK.get())
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_FOOD)));

  // ================================================================================
  // Medical
  // ================================================================================

  public static final RegistryObject<Item> EMPTY_BLOOD_BAG = ITEMS
      .register("empty_blood_bag",
          () -> new FillableItem((FillableItem.Properties) new FillableItem.Properties()
              .setFullItem(new ResourceLocation(CraftingDead.ID, "blood_bag"))
              .setEntityPredicate((entity) -> {
                if (entity instanceof PlayerEntity && ((PlayerEntity) entity).getHealth() > 4) {
                  entity.attackEntityFrom(ModDamageSource.BLEEDING, 2.0F);
                  return true;
                }
                return false;
              })
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MED)));

  public static final RegistryObject<Item> BLOOD_BAG = ITEMS
      .register("blood_bag",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .effect(() -> new EffectInstance(Effects.INSTANT_HEALTH, 1, 0), 1.0F)
              .setShowProgress(true)
              .maxStackSize(1)
              .containerItem(EMPTY_BLOOD_BAG.get())
              .group(ModItemGroups.CRAFTING_DEAD_MED)));

  public static final RegistryObject<Item> FIRST_AID_KIT = ITEMS
      .register("first_aid_kit",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .effect(() -> new EffectInstance(Effects.INSTANT_HEALTH, 1, 1), 1.0F)
              .setShowProgress(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MED)));

  public static final RegistryObject<Item> ADRENALINE_SYRINGE = ITEMS
      .register("adrenaline_syringe",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .effect(() -> new EffectInstance(Effects.SPEED, (20 * 20), 1), 1.0F)
              .setShowProgress(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MED)));

  public static final RegistryObject<Item> SYRINGE = ITEMS
      .register("syringe",
          () -> new FillableItem((FillableItem.Properties) new FillableItem.Properties()
              .setFullItem(new ResourceLocation(CraftingDead.ID, "rbi_syringe"))
              .setEntityPredicate((entity) -> entity instanceof ZombieEntity)
              .setProbability(0.25F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MED)));

  public static final RegistryObject<Item> MORPHINE_SYRINGE = ITEMS
      .register("morphine_syringe",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setShowProgress(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MED)));

  public static final RegistryObject<Item> BANDAGE = ITEMS
      .register("bandage",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .effect(() -> new EffectInstance(Effects.INSTANT_HEALTH, 1, 0), 1.0F)
              .setShowProgress(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MED)));

  public static final RegistryObject<Item> RBI_SYRINGE = ITEMS
      .register("rbi_syringe", () -> new Item(
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_MED)));

  public static final RegistryObject<Item> BOTTLED_RBI = ITEMS
      .register("bottled_rbi", () -> new Item(
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_MED)));

  public static final RegistryObject<Item> DIRTY_RAG = ITEMS
      .register("dirty_rag",
          () -> new FillableItem((FillableItem.Properties) new FillableItem.Properties()
              .setFullItem(new ResourceLocation(CraftingDead.ID, "clean_rag"))
              .setBlockPredicate(
                  (blockPos, blockState) -> blockState.getFluidState().getFluid() == Fluids.WATER)
              .group(ModItemGroups.CRAFTING_DEAD_MED)));

  public static final RegistryObject<Item> BLOODY_RAG = ITEMS
      .register("bloody_rag",
          () -> new FillableItem((FillableItem.Properties) new FillableItem.Properties()
              .setFullItem(new ResourceLocation(CraftingDead.ID, "clean_rag"))
              .setBlockPredicate(
                  (blockPos, blockState) -> blockState.getFluidState().getFluid() == Fluids.WATER)
              .group(ModItemGroups.CRAFTING_DEAD_MED)));

  public static final RegistryObject<Item> CLEAN_RAG = ITEMS
      .register("clean_rag",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_MED)));

  public static final RegistryObject<Item> SPLINT = ITEMS
      .register("splint",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setShowProgress(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MED)));

  public static final RegistryObject<Item> CURE_SYRINGE = ITEMS
      .register("cure_syringe",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setShowProgress(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MED)));

  public static final RegistryObject<Item> BOTTLED_CURE = ITEMS
      .register("bottled_cure", () -> new Item(
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_MED)));

  public static final RegistryObject<Item> ANTIBIOTICS = ITEMS
      .register("antibiotics",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .effect(() -> new EffectInstance(Effects.INSTANT_HEALTH, 1, 0), 1.0F)
              .setShowProgress(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MED)));

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

  public static final RegistryObject<Item> BOWIE = ITEMS
      .register("bowie", () -> new MeleeWeaponItem(15, -2.4F,
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
  // Backpacks
  // ================================================================================

  public static final RegistryObject<Item> SMALL_RED_BACKPACK = ITEMS
      .register("small_red_backpack", () -> new BackpackItem(Backpack.SMALL,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> SMALL_ORANGE_BACKPACK = ITEMS
      .register("small_orange_backpack", () -> new BackpackItem(Backpack.SMALL,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> SMALL_YELLOW_BACKPACK = ITEMS
      .register("small_yellow_backpack", () -> new BackpackItem(Backpack.SMALL,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> SMALL_GREEN_BACKPACK = ITEMS
      .register("small_green_backpack", () -> new BackpackItem(Backpack.SMALL,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> SMALL_BLUE_BACKPACK = ITEMS
      .register("small_blue_backpack", () -> new BackpackItem(Backpack.SMALL,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> SMALL_PURPLE_BACKPACK = ITEMS
      .register("small_purple_backpack", () -> new BackpackItem(Backpack.SMALL,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> MEDIUM_RED_BACKPACK = ITEMS
      .register("medium_red_backpack", () -> new BackpackItem(Backpack.MEDIUM,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> MEDIUM_ORANGE_BACKPACK = ITEMS
      .register("medium_orange_backpack", () -> new BackpackItem(Backpack.MEDIUM,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> MEDIUM_YELLOW_BACKPACK = ITEMS
      .register("medium_yellow_backpack", () -> new BackpackItem(Backpack.MEDIUM,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> MEDIUM_GREEN_BACKPACK = ITEMS
      .register("medium_green_backpack", () -> new BackpackItem(Backpack.MEDIUM,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> MEDIUM_BLUE_BACKPACK = ITEMS
      .register("medium_blue_backpack", () -> new BackpackItem(Backpack.MEDIUM,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> MEDIUM_PURPLE_BACKPACK = ITEMS
      .register("medium_purple_backpack", () -> new BackpackItem(Backpack.MEDIUM,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> MEDIUM_GREY_BACKPACK = ITEMS
      .register("medium_grey_backpack", () -> new BackpackItem(Backpack.MEDIUM,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> MEDIUM_BLACK_BACKPACK = ITEMS
      .register("medium_black_backpack", () -> new BackpackItem(Backpack.MEDIUM,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> MEDIUM_GHILLIE_BACKPACK = ITEMS
      .register("medium_ghillie_backpack", () -> new BackpackItem(Backpack.MEDIUM,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> MEDIUM_WHITE_BACKPACK = ITEMS
      .register("medium_white_backpack", () -> new BackpackItem(Backpack.MEDIUM,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> LARGE_GREY_BACKPACK = ITEMS
      .register("large_grey_backpack", () -> new BackpackItem(Backpack.LARGE,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> LARGE_GREEN_BACKPACK = ITEMS
      .register("large_green_backpack", () -> new BackpackItem(Backpack.LARGE,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> LARGE_TAN_BACKPACK = ITEMS
      .register("large_tan_backpack", () -> new BackpackItem(Backpack.LARGE,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> LARGE_BLACK_BACKPACK = ITEMS
      .register("large_black_backpack", () -> new BackpackItem(Backpack.LARGE,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> LARGE_GHILLIE_BACKPACK = ITEMS
      .register("large_ghillie_backpack", () -> new BackpackItem(Backpack.LARGE,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> TAN_GUN_BACKPACK = ITEMS
      .register("tan_gun_backpack", () -> new BackpackItem(Backpack.LARGE,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> GREY_GUN_BACKPACK = ITEMS
      .register("grey_gun_backpack", () -> new BackpackItem(Backpack.LARGE,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  // TODO Make ammo backpack provide magazines as a primary inventory
  public static final RegistryObject<Item> AMMO_BACKPACK = ITEMS
      .register("ammo_backpack", () -> new BackpackItem(Backpack.LARGE,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  // TODO Make it only carry fuel
  public static final RegistryObject<Item> FUEL_TANK = ITEMS
      .register("fuel_tank", () -> new BackpackItem(Backpack.SMALL,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  /* TODO Make quivers only carry arrows and bolts */

  public static final RegistryObject<Item> TAN_QUIVER = ITEMS
      .register("tan_quiver", () -> new BackpackItem(Backpack.SMALL,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> GREEN_QUIVER = ITEMS
      .register("green_quiver", () -> new BackpackItem(Backpack.SMALL,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> BLACK_QUIVER = ITEMS
      .register("black_quiver", () -> new BackpackItem(Backpack.SMALL,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  // ================================================================================
  // Vests
  // ================================================================================

  public static final RegistryObject<Item> AMMO_TACTICAL_VEST = ITEMS
      .register("ammo_tactical_vest", () -> new VestItem(Vest.AMMO,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> BLACK_TACTICAL_VEST = ITEMS
      .register("black_tactical_vest", () -> new VestItem(Vest.NORM,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> GHILLIE_TACTICAL_VEST = ITEMS
      .register("ghillie_tactical_vest", () -> new VestItem(Vest.NORM,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> GREEN_TACTICAL_VEST = ITEMS
      .register("green_tactical_vest", () -> new VestItem(Vest.NORM,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> GREY_TACTICAL_VEST = ITEMS
      .register("grey_tactical_vest", () -> new VestItem(Vest.NORM,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> RIOT_VEST = ITEMS
      .register("riot_vest", () -> new VestItem(Vest.NORM,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> TAN_TACTICAL_VEST = ITEMS
      .register("tan_tactical_vest", () -> new VestItem(Vest.NORM,
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  // ================================================================================
  // Hats, Helmets and Masks
  // ================================================================================

  public static final RegistryObject<Item> ARMY_HELMET = ITEMS
      .register("army_helmet",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(20.0)
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
              .setHeadshotReductionPercentage(20.0)
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
              .setHeadshotReductionPercentage(20.0)
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
              .setHeadshotReductionPercentage(20.0)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> CLONE_HAT = ITEMS
      .register("clone_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(20.0)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> COMBAT_BDU_HELMET = ITEMS
      .register("combat_bdu_helmet",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(20.0)
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
              .setHeadshotReductionPercentage(20.0)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> GREEN_BALLISTIC_HELMET = ITEMS
      .register("green_ballistic_helmet",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(20.0)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> GREEN_HARD_HAT = ITEMS
      .register("green_hard_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(20.0)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> GREY_ARMY_HELMET = ITEMS
      .register("grey_army_helmet",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(20.0)
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
              .setHeadshotReductionPercentage(20.0)
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
              .setAllowsNightVision(true)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> ORANGE_HARD_HAT = ITEMS
      .register("orange_hard_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(20.0)
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
              .setHeadshotReductionPercentage(20.0)
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
              .setHeadshotReductionPercentage(20.0)
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
              .setHeadshotReductionPercentage(20.0)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> YELLOW_HARD_HAT = ITEMS
      .register("yellow_hard_hat",
          () -> new HatItem((HatItem.Properties) new HatItem.Properties()
              .setHeadshotReductionPercentage(20.0)
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
              .setArmorLevel(3)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> SAS_CLOTHING = ITEMS
      .register("sas_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(3)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> SPETSNAZ_CLOTHING = ITEMS
      .register("spetsnaz_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(3)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> POLICE_CLOTHING = ITEMS
      .register("police_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(2)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> CAMO_CLOTHING = ITEMS
      .register("camo_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(3)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> COMBAT_BDU_CLOTHING = ITEMS
      .register("combat_bdu_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(3)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> WINTER_ARMY_CLOTHING = ITEMS
      .register("winter_army_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(3)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> ARMY_DESERT_CLOTHING = ITEMS
      .register("army_desert_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(3)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> PILOT_CLOTHING = ITEMS
      .register("pilot_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(1)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> HAZMAT_CLOTHING = ITEMS
      .register("hazmat_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(2)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> TAC_GHILLIE_CLOTHING = ITEMS
      .register("tac_ghillie_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(2)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> SWAT_CLOTHING = ITEMS
      .register("swat_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(3)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> SPACE_SUIT_CLOTHING = ITEMS
      .register("space_suit_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(3)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> SHERIFF_CLOTHING = ITEMS
      .register("sheriff_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(1)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> JUGGERNAUT_CLOTHING = ITEMS
      .register("juggernaut_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(3)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> FIREMAN_CLOTHING = ITEMS
      .register("fireman_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(3)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> DOCTOR_CLOTHING = ITEMS
      .register("doctor_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(0)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> SMART_CLOTHING = ITEMS
      .register("smart_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(0)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> CASUAL_GREEN_CLOTHING = ITEMS
      .register("casual_green_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(0)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> BUILDER_CLOTHING = ITEMS
      .register("builder_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(0)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> BUSINESS_CLOTHING = ITEMS
      .register("business_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(0)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> SEC_GUARD_CLOTHING = ITEMS
      .register("sec_guard_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(1)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> MIL_HAZMAT_CLOTHING = ITEMS
      .register("mil_hazmat_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(2)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> FULL_GHILLIE_CLOTHING = ITEMS
      .register("full_ghillie_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(2)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> RED_DUSK_CLOTHING = ITEMS
      .register("red_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(2)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> CLONE_CLOTHING = ITEMS
      .register("clone_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(2)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> COOKIE_CLOTHING = ITEMS
      .register("cookie_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(0)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> DEADPOOL_CLOTHING = ITEMS
      .register("deadpool_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(2)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> NINJA_CLOTHING = ITEMS
      .register("ninja_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(1)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> ARMY_MEDIC_CLOTHING = ITEMS
      .register("army_medic_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(3)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> BLUE_DUSK_CLOTHING = ITEMS
      .register("blue_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(2)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> PRESIDENT_CLOTHING = ITEMS
      .register("president_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(0)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> YELLOW_DUSK_CLOTHING = ITEMS
      .register("yellow_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(2)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> ORANGE_DUSK_CLOTHING = ITEMS
      .register("orange_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(2)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> GREEN_DUSK_CLOTHING = ITEMS
      .register("green_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(2)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> WHITE_DUSK_CLOTHING = ITEMS
      .register("white_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(2)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> PURPLE_DUSK_CLOTHING = ITEMS
      .register("purple_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(2)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> SCUBA_CLOTHING = ITEMS
      .register("scuba_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(1)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> DDPAT_CLOTHING = ITEMS
      .register("ddpat_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(2)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  public static final RegistryObject<Item> CONTRACTOR_CLOTHING = ITEMS
      .register("contractor_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(3)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CLOTHING)));

  // ================================================================================
  // Air Drop Radio
  // ================================================================================

  public static final RegistryObject<Item> MEDICAL_DROP_RADIO = ITEMS
      .register("medical_drop_radio",
          () -> new AirDropRadioItem((AirDropRadioItem.Properties) new AirDropRadioItem.Properties()
              .setLootTable(new ResourceLocation(CraftingDead.ID, "supply_drops/medical"))
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MISC)));

  public static final RegistryObject<Item> MILITARY_DROP_RADIO = ITEMS
      .register("military_drop_radio",
          () -> new AirDropRadioItem((AirDropRadioItem.Properties) new AirDropRadioItem.Properties()
              .setLootTable(new ResourceLocation(CraftingDead.ID, "supply_drops/military"))
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

  // ================================================================================
  // Miscellaneous
  // ================================================================================

  public static final RegistryObject<Item> SCRAP_METAL = ITEMS
      .register("scrap_metal",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_MISC)));
}
