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

package com.craftingdead.core.world.item.gun;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.sounds.ModSoundEvents;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class GunConfigurations {

  public static final ResourceKey<Registry<GunConfiguration>> REGISTRY_KEY =
      ResourceKey.createRegistryKey(new ResourceLocation(CraftingDead.ID, "gun_configuration"));

  public static final DeferredRegister<GunConfiguration> deferredRegister =
      DeferredRegister.create(REGISTRY_KEY, CraftingDead.ID);

  public static final Supplier<IForgeRegistry<GunConfiguration>> registry =
      deferredRegister.makeRegistry(GunConfiguration.class,
          () -> new RegistryBuilder<GunConfiguration>()
              .disableSaving()
              .dataPackRegistry(GunConfiguration.DIRECT_CODEC, GunConfiguration.DIRECT_CODEC));

  // ================================================================================
  // Assault Rifles
  // ================================================================================

  public static final RegistryObject<GunConfiguration> M4A1 =
      deferredRegister.register("m4a1",
          () -> GunConfiguration.builder()
              .aimable(false)
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
              .build());

  public static final RegistryObject<GunConfiguration> SCARL =
      deferredRegister.register("scarl",
          () -> GunConfiguration.builder()
              .aimable(false)
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
                  .setDistantShootSound(ModSoundEvents.SCARL_DISTANT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.SCARL_RELOAD)
              .build());

  public static final RegistryObject<GunConfiguration> AK47 =
      deferredRegister.register("ak47",
          () -> GunConfiguration.builder()
              .aimable(false)
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
              .build());

  public static final RegistryObject<GunConfiguration> FNFAL =
      deferredRegister.register("fnfal",
          () -> GunConfiguration.builder()
              .aimable(false)
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
              .setReloadSound(ModSoundEvents.FNFAL_RELOAD)
              .build());

  public static final RegistryObject<GunConfiguration> ACR =
      deferredRegister.register("acr",
          () -> GunConfiguration.builder()
              .aimable(false)
              .setFireDelayMs(92)
              .setDamage(7)
              .setReloadDurationTicks(20 * 4)
              .setAccuracy(0.8F)
              .setRecoil(3.25F)
              .setRange(150.0D)
              .addFireMode(FireMode.AUTO)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.ACR_SHOOT)
                  .setDistantShootSound(ModSoundEvents.ACR_DISTANT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.ACR_RELOAD)
              .build());

  public static final RegistryObject<GunConfiguration> HK417 =
      deferredRegister.register("hk417",
          () -> GunConfiguration.builder()
              .aimable(false)
              .setFireDelayMs(100)
              .setDamage(8)
              .setReloadDurationTicks(20 * 4)
              .setAccuracy(0.85F)
              .setRecoil(3.5F)
              .setRange(130.0D)
              .addFireMode(FireMode.AUTO)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.HK417_SHOOT)
                  .setDistantShootSound(ModSoundEvents.HK417_DISTANT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_RPK_SHOOT)
              .setReloadSound(ModSoundEvents.HK417_RELOAD)
              .build());

  public static final RegistryObject<GunConfiguration> MPT55 =
      deferredRegister.register("mpt55",
          () -> GunConfiguration.builder()
              .aimable(false)
              .setFireDelayMs(70)
              .setDamage(6)
              .setReloadDurationTicks(20 * 4)
              .setAccuracy(0.8F)
              .setRecoil(2.0F)
              .setRange(180.0D)
              .addFireMode(FireMode.BURST)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.MPT_SHOOT)
                  .setDistantShootSound(ModSoundEvents.MPT_DISTANT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.MPT_RELOAD)
              .build());

  public static final RegistryObject<GunConfiguration> M1GARAND =
      deferredRegister.register("m1garand",
          () -> GunConfiguration.builder()
              .aimable(false)
              .setFireDelayMs(170)
              .setDamage(10)
              .setReloadDurationTicks(20 * 5)
              .setAccuracy(0.92F)
              .setRecoil(7.0F)
              .setRange(200.0D)
              .setCrosshairEnabled(false)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.M1GARAND_SHOOT)
                  .setDistantShootSound(ModSoundEvents.M1GARAND_DISTANT_SHOOT)
              .setReloadSound(ModSoundEvents.M1GARAND_RELOAD)
              .build());

  public static final RegistryObject<GunConfiguration> SPORTER22 =
      deferredRegister.register("sporter22",
          () -> GunConfiguration.builder()
              .aimable(false)
              .setFireDelayMs(200)
              .setDamage(7)
              .setReloadDurationTicks(20 * 3)
              .setAccuracy(0.7F)
              .setRecoil(2.5F)
              .setCrosshairEnabled(false)
              .setRange(60.0D)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.SPORTER22_SHOOT)
                  .setDistantShootSound(ModSoundEvents.SPORTER22_DISTANT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_RPK_SHOOT)
              .setReloadSound(ModSoundEvents.SPORTER22_RELOAD)
              .build());

  public static final RegistryObject<GunConfiguration> G36C =
      deferredRegister.register("g36c",
          () -> GunConfiguration.builder()
              .aimable(false)
              .setFireDelayMs(92)
              .setDamage(8)
              .setReloadDurationTicks(20 * 4)
              .setAccuracy(0.91F)
              .setRecoil(2.0F)
              .setRange(80.0D)
              .addFireMode(FireMode.AUTO)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.G36C_SHOOT)
                  .setDistantShootSound(ModSoundEvents.G36C_DISTANT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.G36C_RELOAD)
              .build());

  // ================================================================================
  // Machine Guns
  // ================================================================================

  public static final RegistryObject<GunConfiguration> M240B =
      deferredRegister.register("m240b",
          () -> GunConfiguration.builder()
              .aimable(false)
              .setFireDelayMs(85)
              .setDamage(8)
              .setReloadDurationTicks(20 * 10)
              .setAccuracy(0.79F)
              .setRecoil(6.0F)
              .setRange(260.0D)
              .addFireMode(FireMode.AUTO)
              .setShootSound(ModSoundEvents.M240B_SHOOT)
              .setDistantShootSound(ModSoundEvents.M240B_DISTANT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M240B_SHOOT)
              .setReloadSound(ModSoundEvents.M240B_RELOAD)
              .build());

  public static final RegistryObject<GunConfiguration> RPK =
      deferredRegister.register("rpk",
          () -> GunConfiguration.builder()
              .aimable(false)
              .setFireDelayMs(92)
              .setDamage(6)
              .setReloadDurationTicks(20 * 5)
              .setAccuracy(0.9F)
              .setRecoil(4.5F)
              .setRange(150.0D)
              .addFireMode(FireMode.AUTO)
              .setShootSound(ModSoundEvents.RPK_SHOOT)
                  .setDistantShootSound(ModSoundEvents.RPK_DISTANT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M240B_SHOOT)
              .setReloadSound(ModSoundEvents.RPK_RELOAD)
              .build());

  public static final RegistryObject<GunConfiguration> MINIGUN =
      deferredRegister.register("minigun",
          () -> GunConfiguration.builder()
              .setFireDelayMs(60)
              .setDamage(4)
              .setReloadDurationTicks(20 * 10)
              .setAccuracy(0.6F)
              .setRecoil(2.5F)
              .setRange(350.0D)
              .addFireMode(FireMode.AUTO)
              .setShootSound(ModSoundEvents.MINIGUN_SHOOT)
                  .setDistantShootSound(ModSoundEvents.MINIGUN_DISTANT_SHOOT)
              .setReloadSound(ModSoundEvents.M240B_RELOAD)
              .setRightMouseActionTriggerType(Gun.SecondaryActionTrigger.HOLD)
              .setSecondaryActionSound(ModSoundEvents.MINIGUN_BARREL)
              .setSecondaryActionSoundRepeatDelayMs(177L)
              .build());

  public static final RegistryObject<GunConfiguration> MK48MOD =
      deferredRegister.register("mk48mod",
          () -> GunConfiguration.builder()
              .aimable(false)
              .setFireDelayMs(92)
              .setDamage(7)
              .setReloadDurationTicks(20 * 4)
              .setAccuracy(0.79F)
              .setRecoil(5.0F)
              .setRange(260.0D)
              .addFireMode(FireMode.AUTO)
              .setShootSound(ModSoundEvents.MK48MOD_SHOOT)
                  .setDistantShootSound(ModSoundEvents.MK48MOD_DISTANT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_MK48MOD_SHOOT)
              .setReloadSound(ModSoundEvents.MK48MOD_RELOAD)
              .build());

  // ================================================================================
  // Pistols
  // ================================================================================

  public static final RegistryObject<GunConfiguration> TASER =
      deferredRegister.register("taser",
          () -> GunConfiguration.builder()
              .aimable(false)
              .setFireDelayMs(2000)
              .setDamage(7)
              .setReloadDurationTicks(20 * 3)
              .setAccuracy(0.9F)
              .setRecoil(1.5F)
              .setRange(7.0D)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.TASER_SHOOT)
              .build());

  public static final RegistryObject<GunConfiguration> M1911 =
      deferredRegister.register("m1911",
          () -> GunConfiguration.builder()
              .aimable(false)
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
              .build());

  public static final RegistryObject<GunConfiguration> G18 =
      deferredRegister.register("g18",
          () -> GunConfiguration.builder()
              .aimable(false)
              .setFireDelayMs(160)
              .setDamage(7)
              .setReloadDurationTicks(20 * 2)
              .setAccuracy(0.7F)
              .setRecoil(2.5F)
              .setRange(50.0D)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.G18_SHOOT)
              .setDistantShootSound(ModSoundEvents.G18_DISTANT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_RPK_SHOOT)
              .setReloadSound(ModSoundEvents.G18_RELOAD)
              .build());

  public static final RegistryObject<GunConfiguration> M9 =
      deferredRegister.register("m9",
          () -> GunConfiguration.builder()
              .aimable(false)
              .setFireDelayMs(160)
              .setDamage(7)
              .setReloadDurationTicks(20 * 2)
              .setAccuracy(0.7F)
              .setRecoil(2.0F)
              .setRange(50.0D)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.M9_SHOOT)
                  .setDistantShootSound(ModSoundEvents.M9_DISTANT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M9_SHOOT)
              .setReloadSound(ModSoundEvents.M9_RELOAD)
              .build());

  public static final RegistryObject<GunConfiguration> DESERT_EAGLE =
      deferredRegister.register("desert_eagle",
          () -> GunConfiguration.builder()
              .aimable(false)
              .setFireDelayMs(160)
              .setDamage(8)
              .setReloadDurationTicks(20 * 2)
              .setAccuracy(0.9F)
              .setRecoil(8.0F)
              .setRange(80.0D)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.DESERT_EAGLE_SHOOT)
              .setDistantShootSound(ModSoundEvents.DESERT_EAGLE_DISTANT_SHOOT)
              .setReloadSound(ModSoundEvents.DESERT_EAGLE_RELOAD)
              .build());

  public static final RegistryObject<GunConfiguration> P250 =
      deferredRegister.register("p250",
          () -> GunConfiguration.builder()
              .aimable(false)
              .setFireDelayMs(160)
              .setDamage(6)
              .setReloadDurationTicks(20 * 2)
              .setAccuracy(0.7F)
              .setRecoil(3.5F)
              .setRange(60.0D)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.P250_SHOOT)
              .setDistantShootSound(ModSoundEvents.P250_DISTANT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M9_SHOOT)
              .setReloadSound(ModSoundEvents.P250_RELOAD)
              .build());

  public static final RegistryObject<GunConfiguration> MAGNUM =
      deferredRegister.register("magnum",
          () -> GunConfiguration.builder()
              .aimable(false)
              .setFireDelayMs(160)
              .setDamage(7)
              .setReloadDurationTicks(20 * 3)
              .setAccuracy(0.8F)
              .setRecoil(8.0F)
              .setRange(80.0D)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.MAGNUM_SHOOT)
                  .setDistantShootSound(ModSoundEvents.MAGNUM_DISTANT_SHOOT)
              .setReloadSound(ModSoundEvents.MAGNUM_RELOAD)
              .build());

  public static final RegistryObject<GunConfiguration> FN57 =
      deferredRegister.register("fn57",
          () -> GunConfiguration.builder()
              .aimable(false)
              .setFireDelayMs(160)
              .setDamage(8)
              .setReloadDurationTicks(20 * 2)
              .setAccuracy(0.8F)
              .setRecoil(3.5F)
              .setRange(50.0D)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.FN57_SHOOT)
                  .setDistantShootSound(ModSoundEvents.FN57_DISTANT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.FN57_RELOAD)
              .build());

  // ================================================================================
  // Submachine Guns
  // ================================================================================

  public static final RegistryObject<GunConfiguration> MAC10 =
      deferredRegister.register("mac10",
          () -> GunConfiguration.builder()
              .aimable(false)
              .setFireDelayMs(80)
              .setDamage(6)
              .setReloadDurationTicks(20 * 2)
              .setAccuracy(0.7F)
              .setRecoil(3.0F)
              .setRange(70.0D)
              .addFireMode(FireMode.AUTO)
              .setShootSound(ModSoundEvents.MAC10_SHOOT)
              .setDistantShootSound(ModSoundEvents.MAC10_DISTANT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.MAC10_RELOAD)
              .build());

  public static final RegistryObject<GunConfiguration> P90 =
      deferredRegister.register("p90",
          () -> GunConfiguration.builder()
              .aimable(false)
              .setFireDelayMs(80)
              .setDamage(5)
              .setReloadDurationTicks(20 * 4)
              .setAccuracy(0.75F)
              .setRecoil(2.0F)
              .setRange(100.0D)
              .addFireMode(FireMode.AUTO)
              .setShootSound(ModSoundEvents.P90_SHOOT)
              .setDistantShootSound(ModSoundEvents.P90_DISTANT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_P90_SHOOT)
              .setReloadSound(ModSoundEvents.P90_RELOAD)
              .build());

  public static final RegistryObject<GunConfiguration> VECTOR =
      deferredRegister.register("vector",
          () -> GunConfiguration.builder()
              .aimable(false)
              .setFireDelayMs(90)
              .setDamage(5)
              .setReloadDurationTicks(20 * 3)
              .setAccuracy(0.7F)
              .setRecoil(2.5F)
              .setRange(80.0D)
              .addFireMode(FireMode.AUTO)
              .setShootSound(ModSoundEvents.VECTOR_SHOOT)
              .setDistantShootSound(ModSoundEvents.VECTOR_DISTANT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.VECTOR_RELOAD)
              .build());

  public static final RegistryObject<GunConfiguration> MP5A5 =
      deferredRegister.register("mp5a5",
          () -> GunConfiguration.builder()
              .aimable(false)
              .setFireDelayMs(85)
              .setDamage(7)
              .setReloadDurationTicks(20 * 3)
              .setAccuracy(0.7F)
              .setRecoil(4.5F)
              .setRange(100.0D)
              .addFireMode(FireMode.AUTO)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.MP5A5_SHOOT)
                  .setDistantShootSound(ModSoundEvents.MP5A5_DISTANT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_MP5A5_SHOOT)
              .setReloadSound(ModSoundEvents.MP5A5_RELOAD)
              .build());

  // ================================================================================
  // Sniper Rifles
  // ================================================================================

  public static final RegistryObject<GunConfiguration> M107 =
      deferredRegister.register("m107",
          () -> GunConfiguration.builder()
              .aimable(true)
              .setFireDelayMs(750)
              .setDamage(20)
              .setReloadDurationTicks(20 * 5)
              .setAccuracy(0.9F)
              .setRecoil(12.0F)
              .setRange(400.0D)
              .setCrosshairEnabled(false)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.M107_SHOOT)
                  .setDistantShootSound(ModSoundEvents.M107_DISTANT_SHOOT)
              .setReloadSound(ModSoundEvents.M107_RELOAD)
              .build());

  public static final RegistryObject<GunConfiguration> AS50 =
      deferredRegister.register("as50",
          () -> GunConfiguration.builder()
              .aimable(false)
              .setFireDelayMs(750)
              .setDamage(14)
              .setReloadDurationTicks(20 * 5)
              .setAccuracy(0.9F)
              .setRecoil(8.5F)
              .setRange(400.0D)
              .setCrosshairEnabled(false)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.AS50_SHOOT)
                  .setDistantShootSound(ModSoundEvents.AS50_DISTANT_SHOOT)
              .setReloadSound(ModSoundEvents.AS50_RELOAD)
              .build());

  public static final RegistryObject<GunConfiguration> AWP =
      deferredRegister.register("awp",
          () -> GunConfiguration.builder()
              .aimable(true)
              .setFireDelayMs(1500)
              .setDamage(20)
              .setReloadDurationTicks(20 * 3)
              .setAccuracy(0.97F)
              .setRecoil(9.0F)
              .setRange(400.0D)
              .setCrosshairEnabled(false)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.AWP_SHOOT)
              .setDistantShootSound(ModSoundEvents.AWP_DISTANT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
              .setReloadSound(ModSoundEvents.AWP_RELOAD)
              .build());

  public static final RegistryObject<GunConfiguration> DMR =
      deferredRegister.register("dmr",
          () -> GunConfiguration.builder()
              .aimable(false)
              .setFireDelayMs(170)
              .setDamage(15)
              .setReloadDurationTicks(20 * 4)
              .setAccuracy(0.9F)
              .setRecoil(7.0F)
              .setRange(300.0D)
              .setCrosshairEnabled(false)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.DMR_SHOOT)
                  .setDistantShootSound(ModSoundEvents.DMR_DISTANT_SHOOT)
              .setSilencedShootSound(ModSoundEvents.SILENCED_RPK_SHOOT)
              .setReloadSound(ModSoundEvents.DMR_RELOAD)
              .build());

  // ================================================================================
  // Shotguns
  // ================================================================================

  public static final RegistryObject<GunConfiguration> TRENCH_GUN =
      deferredRegister.register("trench_gun",
          () -> GunConfiguration.builder()
              .aimable(true)
              .setFireDelayMs(1200)
              .setDamage(2)
              .setRoundsPerShot(8)
              .setReloadDurationTicks(20 * 2)
              .setAccuracy(0.7F)
              .setRecoil(9.0F)
              .setRange(25.0D)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.TRENCH_GUN_SHOOT)
                  .setDistantShootSound(ModSoundEvents.TRENCH_GUN_DISTANT_SHOOT)
              .setReloadSound(ModSoundEvents.SHOTGUN_RELOAD)
              .build());

  public static final RegistryObject<GunConfiguration> MOSSBERG =
      deferredRegister.register("mossberg",
          () -> GunConfiguration.builder()
              .aimable(true)
              .setRoundsPerShot(8)
              .setFireDelayMs(1200)
              .setDamage(3)
              .setReloadDurationTicks(20 * 3)
              .setAccuracy(0.7F)
              .setRecoil(9.0F)
              .setRange(20.0D)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.MOSSBERG_SHOOT)
                  .setDistantShootSound(ModSoundEvents.MOSSBERG_DISTANT_SHOOT)
              .setReloadSound(ModSoundEvents.MOSSBERG_RELOAD)
              .build());
}
