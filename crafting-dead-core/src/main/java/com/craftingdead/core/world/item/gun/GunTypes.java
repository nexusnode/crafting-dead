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
import com.craftingdead.core.client.animation.gun.GunAnimations;
import com.craftingdead.core.sounds.ModSoundEvents;
import com.craftingdead.core.world.item.ModItems;
import com.craftingdead.core.world.item.combatslot.CombatSlot;
import com.craftingdead.core.world.item.gun.aimable.AimableGunType;
import com.craftingdead.core.world.item.gun.attachment.Attachments;
import com.craftingdead.core.world.item.gun.minigun.MinigunType;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class GunTypes {

  public static final DeferredRegister<GunType> gunTypes =
      DeferredRegister.create(GunType.class, CraftingDead.ID);

  public static final Lazy<IForgeRegistry<GunType>> registry =
      Lazy.of(gunTypes.makeRegistry("gun_type", RegistryBuilder::new));

  // ================================================================================
  // Assault Rifles
  // ================================================================================

  public static final RegistryObject<GunType> M4A1 =
      gunTypes.register("m4a1",
          () -> AimableGunType.builder()
              .setItem(ModItems.M4A1)
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
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.RIFLE_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
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

  public static final RegistryObject<GunType> SCARL =
      gunTypes.register("scarl",
          () -> AimableGunType.builder()
              .setItem(ModItems.SCARL)
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
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.RIFLE_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
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

  public static final RegistryObject<GunType> AK47 =
      gunTypes.register("ak47",
          () -> AimableGunType.builder()
              .setItem(ModItems.AK47)
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
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.RIFLE_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
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

  public static final RegistryObject<GunType> FNFAL =
      gunTypes.register("fnfal",
          () -> AimableGunType.builder()
              .setItem(ModItems.FNFAL)
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
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.RIFLE_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
              .setDefaultMagazine(ModItems.FNFAL_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.ACOG_SIGHT)
              .addAcceptedAttachment(Attachments.LP_SCOPE)
              .addAcceptedAttachment(Attachments.HP_SCOPE)
              .addAcceptedAttachment(Attachments.TACTICAL_GRIP)
              .addAcceptedAttachment(Attachments.BIPOD)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .build());

  public static final RegistryObject<GunType> ACR =
      gunTypes.register("acr",
          () -> AimableGunType.builder()
              .setItem(ModItems.ACR)
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
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.RIFLE_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
              .setDefaultMagazine(ModItems.ACR_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .addAcceptedAttachment(Attachments.LP_SCOPE)
              .addAcceptedAttachment(Attachments.HP_SCOPE)
              .addAcceptedAttachment(Attachments.TACTICAL_GRIP)
              .addAcceptedAttachment(Attachments.EOTECH_SIGHT)
              .build());

  public static final RegistryObject<GunType> HK417 =
      gunTypes.register("hk417",
          () -> AimableGunType.builder()
              .setItem(ModItems.HK417)
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
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.RIFLE_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
              .setDefaultMagazine(ModItems.HK417_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.TACTICAL_GRIP)
              .addAcceptedAttachment(Attachments.ACOG_SIGHT)
              .addAcceptedAttachment(Attachments.LP_SCOPE)
              .addAcceptedAttachment(Attachments.HP_SCOPE)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .addAcceptedAttachment(Attachments.EOTECH_SIGHT)
              .build());

  public static final RegistryObject<GunType> MPT55 =
      gunTypes.register("mpt55",
          () -> AimableGunType.builder()
              .setItem(ModItems.MPT55)
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
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.RIFLE_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
              .setDefaultMagazine(ModItems.MPT55_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.ACOG_SIGHT)
              .addAcceptedAttachment(Attachments.LP_SCOPE)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .build());

  public static final RegistryObject<GunType> M1GARAND =
      gunTypes.register("m1garand",
          () -> AimableGunType.builder()
              .setItem(ModItems.M1GARAND)
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
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.RIFLE_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
              .setDefaultMagazine(ModItems.M1GARAND_MAGAZINE)
              .addAcceptedAttachment(Attachments.LP_SCOPE)
              .addAcceptedAttachment(Attachments.HP_SCOPE)
              .addAcceptedAttachment(Attachments.BIPOD)
              .build());

  public static final RegistryObject<GunType> SPORTER22 =
      gunTypes.register("sporter22",
          () -> AimableGunType.builder()
              .setItem(ModItems.SPORTER22)
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
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.RIFLE_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
              .setDefaultMagazine(ModItems.SPORTER22_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.ACOG_SIGHT)
              .addAcceptedAttachment(Attachments.LP_SCOPE)
              .addAcceptedAttachment(Attachments.HP_SCOPE)
              .addAcceptedAttachment(Attachments.TACTICAL_GRIP)
              .addAcceptedAttachment(Attachments.BIPOD)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .build());

  public static final RegistryObject<GunType> G36C =
      gunTypes.register("g36c",
          () -> AimableGunType.builder()
              .setItem(ModItems.G36C)
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
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.RIFLE_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
              .setDefaultMagazine(ModItems.G36C_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.TACTICAL_GRIP)
              .addAcceptedAttachment(Attachments.ACOG_SIGHT)
              .addAcceptedAttachment(Attachments.EOTECH_SIGHT)
              .build());

  // ================================================================================
  // Machine Guns
  // ================================================================================

  public static final RegistryObject<GunType> M240B =
      gunTypes.register("m240b",
          () -> AimableGunType.builder()
              .setItem(ModItems.M240B)
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
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.SUBMACHINE_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
              .setDefaultMagazine(ModItems.M240B_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.TACTICAL_GRIP)
              .addAcceptedAttachment(Attachments.BIPOD)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .addAcceptedAttachment(Attachments.EOTECH_SIGHT)
              .build());

  public static final RegistryObject<GunType> RPK =
      gunTypes.register("rpk",
          () -> AimableGunType.builder()
              .setItem(ModItems.RPK)
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
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.RIFLE_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
              .setDefaultMagazine(ModItems.RPK_MAGAZINE)
              .addAcceptedMagazine(ModItems.RPK_DRUM_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .addAcceptedAttachment(Attachments.ACOG_SIGHT)
              .build());

  public static final RegistryObject<GunType> MINIGUN =
      gunTypes.register("minigun",
          () -> MinigunType.builder()
              .setItem(ModItems.MINIGUN)
              .setFireDelayMs(75)
              .setDamage(4)
              .setReloadDurationTicks(20 * 5)
              .setAccuracy(0.6F)
              .setRecoil(2.5F)
              .setRange(350.0D)
              .addFireMode(FireMode.AUTO)
              .setShootSound(ModSoundEvents.MINIGUN_SHOOT)
              .setReloadSound(ModSoundEvents.M240B_RELOAD)
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.SUBMACHINE_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
              .setDefaultMagazine(ModItems.MINIGUN_MAGAZINE)
              .setRightMouseActionTriggerType(Gun.SecondaryActionTrigger.HOLD)
              .setTriggerPredicate(GunTriggerPredicates.PERFORMING_SECONDARY_ACTION)
              .setSecondaryActionSound(ModSoundEvents.MINIGUN_BARREL)
              .setSecondaryActionSoundRepeatDelayMs(177L)
              .build());

  public static final RegistryObject<GunType> MK48MOD =
      gunTypes.register("mk48mod",
          () -> AimableGunType.builder()
              .setItem(ModItems.MK48MOD)
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
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.SUBMACHINE_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
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

  public static final RegistryObject<GunType> TASER =
      gunTypes.register("taser",
          () -> AimableGunType.builder()
              .setItem(ModItems.TASER)
              .setCombatSlot(CombatSlot.SECONDARY)
              .setFireDelayMs(2000)
              .setDamage(7)
              .setReloadDurationTicks(20 * 3)
              .setAccuracy(0.9F)
              .setRecoil(1.5F)
              .setRange(7.0D)
              .addFireMode(FireMode.SEMI)
              .setShootSound(ModSoundEvents.TASER_SHOOT)
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.PISTOL_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
              .setDefaultMagazine(ModItems.TASER_CARTRIDGE)
              .build());

  public static final RegistryObject<GunType> M1911 =
      gunTypes.register("m1911",
          () -> AimableGunType.builder()
              .setItem(ModItems.M1911)
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
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.PISTOL_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
              .setDefaultMagazine(ModItems.M1911_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .build());

  public static final RegistryObject<GunType> G18 =
      gunTypes.register("g18",
          () -> AimableGunType.builder()
              .setItem(ModItems.G18)
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
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.PISTOL_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
              .setDefaultMagazine(ModItems.G18_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .build());

  public static final RegistryObject<GunType> M9 =
      gunTypes.register("m9",
          () -> AimableGunType.builder()
              .setItem(ModItems.M9)
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
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.PISTOL_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
              .setDefaultMagazine(ModItems.M9_MAGAZINE)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .build());

  public static final RegistryObject<GunType> DESERT_EAGLE =
      gunTypes.register("desert_eagle",
          () -> AimableGunType.builder()
              .setItem(ModItems.DESERT_EAGLE)
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
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.PISTOL_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
              .setDefaultMagazine(ModItems.DESERT_EAGLE_MAGAZINE)
              .build());

  public static final RegistryObject<GunType> P250 =
      gunTypes.register("p250",
          () -> AimableGunType.builder()
              .setItem(ModItems.P250)
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
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.PISTOL_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
              .setDefaultMagazine(ModItems.P250_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .build());

  public static final RegistryObject<GunType> MAGNUM =
      gunTypes.register("magnum",
          () -> AimableGunType.builder()
              .setItem(ModItems.MAGNUM)
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
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.PISTOL_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
              .setDefaultMagazine(ModItems.MAGNUM_MAGAZINE)
              .build());

  public static final RegistryObject<GunType> FN57 =
      gunTypes.register("fn57",
          () -> AimableGunType.builder()
              .setItem(ModItems.FN57)
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
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.PISTOL_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
              .setDefaultMagazine(ModItems.FN57_MAGAZINE)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .build());

  // ================================================================================
  // Submachine Guns
  // ================================================================================

  public static final RegistryObject<GunType> MAC10 =
      gunTypes.register("mac10",
          () -> AimableGunType.builder()
              .setItem(ModItems.MAC10)
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
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.SUBMACHINE_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
              .setDefaultMagazine(ModItems.MAC10_MAGAZINE)
              .addAcceptedMagazine(ModItems.MAC10_EXTENDED_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.ACOG_SIGHT)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .build());

  public static final RegistryObject<GunType> P90 =
      gunTypes.register("p90",
          () -> AimableGunType.builder()
              .setItem(ModItems.P90)
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
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.SUBMACHINE_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
              .setDefaultMagazine(ModItems.P90_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.ACOG_SIGHT)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .build());

  public static final RegistryObject<GunType> VECTOR =
      gunTypes.register("vector",
          () -> AimableGunType.builder()
              .setItem(ModItems.VECTOR)
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
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.SUBMACHINE_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
              .setDefaultMagazine(ModItems.VECTOR_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.ACOG_SIGHT)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .build());

  public static final RegistryObject<GunType> MP5A5 =
      gunTypes.register("mp5a5",
          () -> AimableGunType.builder()
              .setItem(ModItems.MP5A5)
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
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.SUBMACHINE_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
              .setDefaultMagazine(ModItems.MP5A5_21_ROUND_MAGAZINE)
              .addAcceptedMagazine(ModItems.MP5A5_35_ROUND_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.ACOG_SIGHT)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .build());

  // ================================================================================
  // Sniper Rifles
  // ================================================================================

  public static final RegistryObject<GunType> M107 =
      gunTypes.register("m107",
          () -> AimableGunType.builder()
              .setItem(ModItems.M107)
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
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.RIFLE_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
              .setDefaultMagazine(ModItems.M107_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.ACOG_SIGHT)
              .addAcceptedAttachment(Attachments.LP_SCOPE)
              .addAcceptedAttachment(Attachments.HP_SCOPE)
              .addAcceptedAttachment(Attachments.TACTICAL_GRIP)
              .addAcceptedAttachment(Attachments.BIPOD)
              .build());

  public static final RegistryObject<GunType> AS50 =
      gunTypes.register("as50",
          () -> AimableGunType.builder()
              .setItem(ModItems.AS50)
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
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.RIFLE_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
              .setDefaultMagazine(ModItems.AS50_MAGAZINE)
              .addAcceptedAttachment(Attachments.RED_DOT_SIGHT)
              .addAcceptedAttachment(Attachments.ACOG_SIGHT)
              .addAcceptedAttachment(Attachments.LP_SCOPE)
              .addAcceptedAttachment(Attachments.HP_SCOPE)
              .addAcceptedAttachment(Attachments.TACTICAL_GRIP)
              .addAcceptedAttachment(Attachments.BIPOD)
              .build());

  public static final RegistryObject<GunType> AWP =
      gunTypes.register("awp",
          () -> AimableGunType.builder()
              .setItem(ModItems.AWP)
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
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.RIFLE_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
              .setDefaultMagazine(ModItems.AWP_MAGAZINE)
              .addAcceptedAttachment(Attachments.LP_SCOPE)
              .addAcceptedAttachment(Attachments.HP_SCOPE)
              .addAcceptedAttachment(Attachments.BIPOD)
              .addAcceptedAttachment(Attachments.SUPPRESSOR)
              .build());

  public static final RegistryObject<GunType> DMR =
      gunTypes.register("dmr",
          () -> AimableGunType.builder()
              .setItem(ModItems.DMR)
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
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.RIFLE_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
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

  public static final RegistryObject<GunType> TRENCH_GUN =
      gunTypes.register("trench_gun",
          () -> AimableGunType.builder()
              .setItem(ModItems.TRENCH_GUN)
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
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.PISTOL_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
              .setDefaultMagazine(ModItems.TRENCH_GUN_SHELLS)
              .addAcceptedAttachment(Attachments.TACTICAL_GRIP)
              .build());

  public static final RegistryObject<GunType> MOSSBERG =
      gunTypes.register("mossberg",
          () -> AimableGunType.builder()
              .setItem(ModItems.MOSSBERG)
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
              .putAnimation(GunAnimationEvent.SHOOT, GunAnimations.PISTOL_SHOOT)
              .putAnimation(GunAnimationEvent.RELOAD, GunAnimations.RELOAD)
              .putAnimation(GunAnimationEvent.INSPECT, GunAnimations.INSPECT)
              .setDefaultMagazine(ModItems.MOSSBERG_SHELLS)
              .addAcceptedAttachment(Attachments.TACTICAL_GRIP)
              .build());
}
