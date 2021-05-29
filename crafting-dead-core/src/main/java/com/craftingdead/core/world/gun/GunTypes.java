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

package com.craftingdead.core.world.gun;

import com.craftingdead.core.client.animation.gun.AnimationType;
import com.craftingdead.core.client.animation.gun.fire.PistolShootAnimation;
import com.craftingdead.core.client.animation.gun.fire.RifleShootAnimation;
import com.craftingdead.core.client.animation.gun.fire.SubmachineShootAnimation;
import com.craftingdead.core.client.animation.gun.inspect.GunAnimationInspectPistol;
import com.craftingdead.core.client.animation.gun.inspect.GunAnimationInspectRifle;
import com.craftingdead.core.client.animation.gun.inspect.GunAnimationInspectSMG;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadACR;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadAK47;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadAS50;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadAWP;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadDMR;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadDeagle;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadFNFAL;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadFiveSeven;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadG18;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadG36C;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadHK417;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadM107;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadM1911;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadM1Garand;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadM240B;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadM4A1;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadM9;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadMAC10;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadMK48;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadMP5A5;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadMPT55;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadMagnum;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadMinigun;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadMossberg;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadP250;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadP90;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadRPK;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadSCARH;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadSporter22;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadTrench;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReloadVector;
import com.craftingdead.core.sounds.ModSoundEvents;
import com.craftingdead.core.world.gun.aimable.AimableGunType;
import com.craftingdead.core.world.gun.minigun.MinigunClient;
import com.craftingdead.core.world.gun.simple.SimpleGunType;
import com.craftingdead.core.world.item.ModItems;
import com.craftingdead.core.world.item.combatslot.CombatSlotType;

public class GunTypes {

  // ================================================================================
  // Assault Rifles
  // ================================================================================

  public static final AbstractGunType<?> M4A1 = AimableGunType.builder()
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
      .addAnimation(AnimationType.SHOOT, RifleShootAnimation::new)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadM4A1::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
      .setDefaultMagazine(ModItems.STANAG_20_ROUND_MAGAZINE)
      .addAcceptedMagazine(ModItems.STANAG_30_ROUND_MAGAZINE)
      .addAcceptedMagazine(ModItems.STANAG_DRUM_MAGAZINE)
      .addAcceptedMagazine(ModItems.STANAG_BOX_MAGAZINE)
      .addAcceptedAttachment(ModItems.RED_DOT_SIGHT)
      .addAcceptedAttachment(ModItems.ACOG_SIGHT)
      .addAcceptedAttachment(ModItems.LP_SCOPE)
      .addAcceptedAttachment(ModItems.HP_SCOPE)
      .addAcceptedAttachment(ModItems.TACTICAL_GRIP)
      .addAcceptedAttachment(ModItems.BIPOD)
      .addAcceptedAttachment(ModItems.SUPPRESSOR)
      .addAcceptedAttachment(ModItems.EOTECH_SIGHT)
      .addAcceptedPaint(ModItems.CYREX_PAINT)
      .addAcceptedPaint(ModItems.ASMO_PAINT)
      .addAcceptedPaint(ModItems.DIAMOND_PAINT)
      .addAcceptedPaint(ModItems.INFERNO_PAINT)
      .addAcceptedPaint(ModItems.SCORCHED_PAINT)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .addAcceptedPaint(ModItems.EMPEROR_DRAGON_PAINT)
      .addAcceptedPaint(ModItems.HYPER_BEAST_PAINT)
      .addAcceptedPaint(ModItems.LOVELACE_PAINT)
      .build();

  public static final AbstractGunType<?> SCARH = AimableGunType.builder()
      .setFireDelayMs(110)
      .setDamage(6)
      .setReloadDurationTicks(20 * 4)
      .setAccuracy(0.87F)
      .setRecoil(4.25F)
      .setRange(300.0D)
      .addFireMode(FireMode.AUTO)
      .addFireMode(FireMode.BURST)
      .addFireMode(FireMode.SEMI)
      .setShootSound(ModSoundEvents.SCARH_SHOOT)
      .setSilencedShootSound(ModSoundEvents.SILENCED_M4A1_SHOOT)
      .setReloadSound(ModSoundEvents.M4A1_RELOAD)
      .addAnimation(AnimationType.SHOOT, RifleShootAnimation::new)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadSCARH::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
      .setDefaultMagazine(ModItems.STANAG_20_ROUND_MAGAZINE)
      .addAcceptedMagazine(ModItems.STANAG_30_ROUND_MAGAZINE)
      .addAcceptedMagazine(ModItems.STANAG_DRUM_MAGAZINE)
      .addAcceptedMagazine(ModItems.STANAG_BOX_MAGAZINE)
      .addAcceptedAttachment(ModItems.RED_DOT_SIGHT)
      .addAcceptedAttachment(ModItems.ACOG_SIGHT)
      .addAcceptedAttachment(ModItems.LP_SCOPE)
      .addAcceptedAttachment(ModItems.HP_SCOPE)
      .addAcceptedAttachment(ModItems.TACTICAL_GRIP)
      .addAcceptedAttachment(ModItems.BIPOD)
      .addAcceptedAttachment(ModItems.SUPPRESSOR)
      .addAcceptedAttachment(ModItems.EOTECH_SIGHT)
      .addAcceptedPaint(ModItems.ASMO_PAINT)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .build();

  public static final AbstractGunType<?> AK47 = AimableGunType.builder()
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
      .addAnimation(AnimationType.SHOOT, RifleShootAnimation::new)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadAK47::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
      .setDefaultMagazine(ModItems.AK47_30_ROUND_MAGAZINE)
      .addAcceptedAttachment(ModItems.RED_DOT_SIGHT)
      .addAcceptedAttachment(ModItems.ACOG_SIGHT)
      .addAcceptedAttachment(ModItems.LP_SCOPE)
      .addAcceptedAttachment(ModItems.HP_SCOPE)
      .addAcceptedAttachment(ModItems.TACTICAL_GRIP)
      .addAcceptedAttachment(ModItems.BIPOD)
      .addAcceptedAttachment(ModItems.EOTECH_SIGHT)
      .addAcceptedAttachment(ModItems.SUPPRESSOR)
      .addAcceptedPaint(ModItems.VULCAN_PAINT)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .addAcceptedPaint(ModItems.ASMO_PAINT)
      .build();

  public static final AbstractGunType<?> FNFAL = AimableGunType.builder()
      .setFireDelayMs(80)
      .setDamage(9)
      .setReloadDurationTicks(20 * 4)
      .setAccuracy(0.24F)
      .setRecoil(4.0F)
      .setRange(300.0D)
      .addFireMode(FireMode.SEMI)
      .setShootSound(ModSoundEvents.FNFAL_SHOOT)
      .setDistantShootSound(ModSoundEvents.FNFAL_DISTANT_SHOOT)
      .setSilencedShootSound(ModSoundEvents.SILENCED_RPK_SHOOT)
      .setReloadSound(ModSoundEvents.M4A1_RELOAD)
      .addAnimation(AnimationType.SHOOT, RifleShootAnimation::new)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadFNFAL::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
      .setDefaultMagazine(ModItems.FNFAL_MAGAZINE)
      .addAcceptedAttachment(ModItems.RED_DOT_SIGHT)
      .addAcceptedAttachment(ModItems.ACOG_SIGHT)
      .addAcceptedAttachment(ModItems.LP_SCOPE)
      .addAcceptedAttachment(ModItems.HP_SCOPE)
      .addAcceptedAttachment(ModItems.TACTICAL_GRIP)
      .addAcceptedAttachment(ModItems.BIPOD)
      .addAcceptedAttachment(ModItems.SUPPRESSOR)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .build();

  public static final AbstractGunType<?> ACR = AimableGunType.builder()
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
      .addAnimation(AnimationType.SHOOT, RifleShootAnimation::new)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadACR::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
      .setDefaultMagazine(ModItems.ACR_MAGAZINE)
      .addAcceptedAttachment(ModItems.RED_DOT_SIGHT)
      .addAcceptedAttachment(ModItems.SUPPRESSOR)
      .addAcceptedAttachment(ModItems.LP_SCOPE)
      .addAcceptedAttachment(ModItems.HP_SCOPE)
      .addAcceptedAttachment(ModItems.TACTICAL_GRIP)
      .addAcceptedAttachment(ModItems.EOTECH_SIGHT)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .build();

  public static final AbstractGunType<?> HK417 = AimableGunType.builder()
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
      .addAnimation(AnimationType.SHOOT, RifleShootAnimation::new)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadHK417::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
      .setDefaultMagazine(ModItems.HK417_MAGAZINE)
      .addAcceptedAttachment(ModItems.RED_DOT_SIGHT)
      .addAcceptedAttachment(ModItems.TACTICAL_GRIP)
      .addAcceptedAttachment(ModItems.ACOG_SIGHT)
      .addAcceptedAttachment(ModItems.LP_SCOPE)
      .addAcceptedAttachment(ModItems.HP_SCOPE)
      .addAcceptedAttachment(ModItems.SUPPRESSOR)
      .addAcceptedAttachment(ModItems.EOTECH_SIGHT)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .build();

  public static final AbstractGunType<?> MPT55 = AimableGunType.builder()
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
      .addAnimation(AnimationType.SHOOT, RifleShootAnimation::new)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadMPT55::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
      .setDefaultMagazine(ModItems.MPT55_MAGAZINE)
      .addAcceptedAttachment(ModItems.RED_DOT_SIGHT)
      .addAcceptedAttachment(ModItems.ACOG_SIGHT)
      .addAcceptedAttachment(ModItems.LP_SCOPE)
      .addAcceptedAttachment(ModItems.SUPPRESSOR)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .build();

  public static final AbstractGunType<?> M1GARAND = AimableGunType.builder()
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
      .addAnimation(AnimationType.SHOOT, RifleShootAnimation::new)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadM1Garand::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
      .setDefaultMagazine(ModItems.M1GARAND_MAGAZINE)
      .addAcceptedAttachment(ModItems.LP_SCOPE)
      .addAcceptedAttachment(ModItems.HP_SCOPE)
      .addAcceptedAttachment(ModItems.BIPOD)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .build();

  public static final AbstractGunType<?> SPORTER22 = AimableGunType.builder()
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
      .addAnimation(AnimationType.SHOOT, RifleShootAnimation::new)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadSporter22::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
      .setDefaultMagazine(ModItems.SPORTER22_MAGAZINE)
      .addAcceptedAttachment(ModItems.RED_DOT_SIGHT)
      .addAcceptedAttachment(ModItems.ACOG_SIGHT)
      .addAcceptedAttachment(ModItems.LP_SCOPE)
      .addAcceptedAttachment(ModItems.HP_SCOPE)
      .addAcceptedAttachment(ModItems.TACTICAL_GRIP)
      .addAcceptedAttachment(ModItems.BIPOD)
      .addAcceptedAttachment(ModItems.SUPPRESSOR)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .build();

  public static final AbstractGunType<?> G36C = AimableGunType.builder()
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
      .addAnimation(AnimationType.SHOOT, RifleShootAnimation::new)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadG36C::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
      .setDefaultMagazine(ModItems.G36C_MAGAZINE)
      .addAcceptedAttachment(ModItems.RED_DOT_SIGHT)
      .addAcceptedAttachment(ModItems.TACTICAL_GRIP)
      .addAcceptedAttachment(ModItems.ACOG_SIGHT)
      .addAcceptedAttachment(ModItems.EOTECH_SIGHT)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .build();


  // ================================================================================
  // Machine Guns
  // ================================================================================

  public static final AbstractGunType<?> M240B = AimableGunType.builder()
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
      .addAnimation(AnimationType.SHOOT, SubmachineShootAnimation::new)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadM240B::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
      .setDefaultMagazine(ModItems.M240B_MAGAZINE)
      .addAcceptedAttachment(ModItems.RED_DOT_SIGHT)
      .addAcceptedAttachment(ModItems.TACTICAL_GRIP)
      .addAcceptedAttachment(ModItems.BIPOD)
      .addAcceptedAttachment(ModItems.SUPPRESSOR)
      .addAcceptedAttachment(ModItems.EOTECH_SIGHT)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .build();

  public static final AbstractGunType<?> RPK = AimableGunType.builder()
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
      .addAnimation(AnimationType.SHOOT, RifleShootAnimation::new)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadRPK::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
      .setDefaultMagazine(ModItems.RPK_MAGAZINE)
      .addAcceptedMagazine(ModItems.RPK_DRUM_MAGAZINE)
      .addAcceptedAttachment(ModItems.RED_DOT_SIGHT)
      .addAcceptedAttachment(ModItems.SUPPRESSOR)
      .addAcceptedAttachment(ModItems.ACOG_SIGHT)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .build();

  public static final AbstractGunType<?> MINIGUN = SimpleGunType.builder()
      .setFireDelayMs(75)
      .setDamage(4)
      .setReloadDurationTicks(20 * 5)
      .setAccuracy(0.6F)
      .setRecoil(2.5F)
      .setRange(350.0D)
      .addFireMode(FireMode.AUTO)
      .setShootSound(ModSoundEvents.MINIGUN_SHOOT)
      .setReloadSound(ModSoundEvents.M240B_RELOAD)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadMinigun::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
      .setDefaultMagazine(ModItems.MINIGUN_MAGAZINE)
      .addAcceptedPaint(ModItems.FURY_PAINT)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .setRightMouseActionTriggerType(Gun.RightMouseActionTriggerType.HOLD)
      .setTriggerPredicate(Gun::isPerformingRightMouseAction)
      .setRightMouseActionSound(ModSoundEvents.MINIGUN_BARREL)
      .setRightMouseActionSoundRepeatDelayMs(177L)
      .setClientFactory(MinigunClient::new)
      .build();

  public static final AbstractGunType<?> MK48MOD = AimableGunType.builder()
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
      .addAnimation(AnimationType.SHOOT, SubmachineShootAnimation::new)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadMK48::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
      .setDefaultMagazine(ModItems.MK48MOD_MAGAZINE)
      .addAcceptedAttachment(ModItems.RED_DOT_SIGHT)
      .addAcceptedAttachment(ModItems.TACTICAL_GRIP)
      .addAcceptedAttachment(ModItems.BIPOD)
      .addAcceptedAttachment(ModItems.SUPPRESSOR)
      .addAcceptedAttachment(ModItems.EOTECH_SIGHT)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .build();

  // ================================================================================
  // Pistols
  // ================================================================================

  public static final AbstractGunType<?> TASER = AimableGunType.builder()
      .setCombatSlotType(CombatSlotType.SECONDARY)
      .setFireDelayMs(2000)
      .setDamage(7)
      .setReloadDurationTicks(20 * 3)
      .setAccuracy(0.9F)
      .setRecoil(1.5F)
      .setRange(7.0D)
      .addFireMode(FireMode.SEMI)
      .setShootSound(ModSoundEvents.TASER_SHOOT)
      .addAnimation(AnimationType.SHOOT, PistolShootAnimation::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectPistol::new)
      .setDefaultMagazine(ModItems.TASER_CARTRIDGE)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .build();

  public static final AbstractGunType<?> M1911 = AimableGunType.builder()
      .setCombatSlotType(CombatSlotType.SECONDARY)
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
      .addAnimation(AnimationType.SHOOT, PistolShootAnimation::new)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadM1911::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectPistol::new)
      .setDefaultMagazine(ModItems.M1911_MAGAZINE)
      .addAcceptedAttachment(ModItems.RED_DOT_SIGHT)
      .addAcceptedAttachment(ModItems.SUPPRESSOR)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .build();

  public static final AbstractGunType<?> G18 = AimableGunType.builder()
      .setCombatSlotType(CombatSlotType.SECONDARY)
      .setFireDelayMs(160)
      .setDamage(7)
      .setReloadDurationTicks((int) (20 * 2.2F))
      .setAccuracy(0.7F)
      .setRecoil(2.5F)
      .setRange(50.0D)
      .addFireMode(FireMode.AUTO)
      .addFireMode(FireMode.SEMI)
      .setShootSound(ModSoundEvents.G18_SHOOT)
      .setDistantShootSound(ModSoundEvents.G18_DISTANT_SHOOT)
      .setSilencedShootSound(ModSoundEvents.SILENCED_RPK_SHOOT)
      .setReloadSound(ModSoundEvents.M1911_RELOAD)
      .addAnimation(AnimationType.SHOOT, PistolShootAnimation::new)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadG18::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectPistol::new)
      .setDefaultMagazine(ModItems.G18_MAGAZINE)
      .addAcceptedAttachment(ModItems.RED_DOT_SIGHT)
      .addAcceptedAttachment(ModItems.SUPPRESSOR)
      .addAcceptedPaint(ModItems.FADE_PAINT)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .build();

  public static final AbstractGunType<?> M9 = AimableGunType.builder()
      .setCombatSlotType(CombatSlotType.SECONDARY)
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
      .addAnimation(AnimationType.SHOOT, PistolShootAnimation::new)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadM9::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectPistol::new)
      .setDefaultMagazine(ModItems.M9_MAGAZINE)
      .addAcceptedAttachment(ModItems.SUPPRESSOR)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .addAcceptedPaint(ModItems.CYREX_PAINT)
      .build();

  public static final AbstractGunType<?> DESERT_EAGLE = AimableGunType.builder()
      .setCombatSlotType(CombatSlotType.SECONDARY)
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
      .addAnimation(AnimationType.SHOOT, PistolShootAnimation::new)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadDeagle::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectPistol::new)
      .setDefaultMagazine(ModItems.DESERT_EAGLE_MAGAZINE)
      .addAcceptedPaint(ModItems.INFERNO_PAINT)
      .addAcceptedPaint(ModItems.SCORCHED_PAINT)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .addAcceptedPaint(ModItems.NUCLEAR_WINTER_PAINT)
      .addAcceptedPaint(ModItems.LOVELACE_PAINT)
      .build();

  public static final AbstractGunType<?> P250 = AimableGunType.builder()
      .setCombatSlotType(CombatSlotType.SECONDARY)
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
      .addAnimation(AnimationType.SHOOT, PistolShootAnimation::new)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadP250::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectPistol::new)
      .setDefaultMagazine(ModItems.P250_MAGAZINE)
      .addAcceptedAttachment(ModItems.RED_DOT_SIGHT)
      .addAcceptedAttachment(ModItems.SUPPRESSOR)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .addAcceptedPaint(ModItems.ASMO_PAINT)
      .build();

  public static final AbstractGunType<?> MAGNUM = AimableGunType.builder()
      .setCombatSlotType(CombatSlotType.SECONDARY)
      .setFireDelayMs(160)
      .setDamage(7)
      .setReloadDurationTicks(20 * 2)
      .setAccuracy(0.8F)
      .setRecoil(8.0F)
      .setRange(80.0D)
      .addFireMode(FireMode.SEMI)
      .setShootSound(ModSoundEvents.MAGNUM_SHOOT)
      .setReloadSound(ModSoundEvents.MAGNUM_RELOAD)
      .addAnimation(AnimationType.SHOOT, PistolShootAnimation::new)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadMagnum::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectPistol::new)
      .setDefaultMagazine(ModItems.MAGNUM_MAGAZINE)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .build();

  public static final AbstractGunType<?> FN57 = AimableGunType.builder()
      .setCombatSlotType(CombatSlotType.SECONDARY)
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
      .addAnimation(AnimationType.SHOOT, PistolShootAnimation::new)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadFiveSeven::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectPistol::new)
      .setDefaultMagazine(ModItems.FN57_MAGAZINE)
      .addAcceptedAttachment(ModItems.SUPPRESSOR)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .build();

  // ================================================================================
  // Submachine Guns
  // ================================================================================

  public static final AbstractGunType<?> MAC10 = AimableGunType.builder()
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
      .addAnimation(AnimationType.SHOOT, SubmachineShootAnimation::new)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadMAC10::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectSMG::new)
      .setDefaultMagazine(ModItems.MAC10_MAGAZINE)
      .addAcceptedMagazine(ModItems.MAC10_EXTENDED_MAGAZINE)
      .addAcceptedAttachment(ModItems.RED_DOT_SIGHT)
      .addAcceptedAttachment(ModItems.ACOG_SIGHT)
      .addAcceptedAttachment(ModItems.SUPPRESSOR)
      .addAcceptedPaint(ModItems.FADE_PAINT)
      .addAcceptedPaint(ModItems.UV_PAINT)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .build();

  public static final AbstractGunType<?> P90 = AimableGunType.builder()
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
      .addAnimation(AnimationType.SHOOT, SubmachineShootAnimation::new)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadP90::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectSMG::new)
      .setDefaultMagazine(ModItems.P90_MAGAZINE)
      .addAcceptedAttachment(ModItems.RED_DOT_SIGHT)
      .addAcceptedAttachment(ModItems.ACOG_SIGHT)
      .addAcceptedAttachment(ModItems.SUPPRESSOR)
      .addAcceptedPaint(ModItems.RUBY_PAINT)
      .addAcceptedPaint(ModItems.GEM_PAINT)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .addAcceptedPaint(ModItems.ASMO_PAINT)
      .build();

  public static final AbstractGunType<?> VECTOR = AimableGunType.builder()
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
      .addAnimation(AnimationType.SHOOT, SubmachineShootAnimation::new)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadVector::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectSMG::new)
      .setDefaultMagazine(ModItems.VECTOR_MAGAZINE)
      .addAcceptedAttachment(ModItems.RED_DOT_SIGHT)
      .addAcceptedAttachment(ModItems.ACOG_SIGHT)
      .addAcceptedAttachment(ModItems.SUPPRESSOR)
      .addAcceptedPaint(ModItems.SLAUGHTER_PAINT)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .build();

  public static final AbstractGunType<?> MP5A5 = AimableGunType.builder()
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
      .addAnimation(AnimationType.SHOOT, SubmachineShootAnimation::new)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadMP5A5::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectSMG::new)
      .setDefaultMagazine(ModItems.MP5A5_21_ROUND_MAGAZINE)
      .addAcceptedMagazine(ModItems.MP5A5_35_ROUND_MAGAZINE)
      .addAcceptedAttachment(ModItems.RED_DOT_SIGHT)
      .addAcceptedAttachment(ModItems.ACOG_SIGHT)
      .addAcceptedAttachment(ModItems.SUPPRESSOR)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .build();

  // ================================================================================
  // Sniper Rifles
  // ================================================================================

  public static final AbstractGunType<?> M107 = AimableGunType.builder()
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
      .addAnimation(AnimationType.SHOOT, RifleShootAnimation::new)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadM107::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
      .setDefaultMagazine(ModItems.M107_MAGAZINE)
      .addAcceptedAttachment(ModItems.RED_DOT_SIGHT)
      .addAcceptedAttachment(ModItems.ACOG_SIGHT)
      .addAcceptedAttachment(ModItems.LP_SCOPE)
      .addAcceptedAttachment(ModItems.HP_SCOPE)
      .addAcceptedAttachment(ModItems.TACTICAL_GRIP)
      .addAcceptedAttachment(ModItems.BIPOD)
      .addAcceptedPaint(ModItems.CANDY_APPLE_PAINT)
      .addAcceptedPaint(ModItems.DIAMOND_PAINT)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .build();

  public static final AbstractGunType<?> AS50 = AimableGunType.builder()
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
      .addAnimation(AnimationType.SHOOT, RifleShootAnimation::new)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadAS50::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
      .setDefaultMagazine(ModItems.AS50_MAGAZINE)
      .addAcceptedAttachment(ModItems.RED_DOT_SIGHT)
      .addAcceptedAttachment(ModItems.ACOG_SIGHT)
      .addAcceptedAttachment(ModItems.LP_SCOPE)
      .addAcceptedAttachment(ModItems.HP_SCOPE)
      .addAcceptedAttachment(ModItems.TACTICAL_GRIP)
      .addAcceptedAttachment(ModItems.BIPOD)
      .addAcceptedPaint(ModItems.CANDY_APPLE_PAINT)
      .addAcceptedPaint(ModItems.DIAMOND_PAINT)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .build();

  public static final AbstractGunType<?> AWP = AimableGunType.builder()
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
      .addAnimation(AnimationType.SHOOT, RifleShootAnimation::new)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadAWP::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
      .setDefaultMagazine(ModItems.AWP_MAGAZINE)
      .addAcceptedAttachment(ModItems.LP_SCOPE)
      .addAcceptedAttachment(ModItems.HP_SCOPE)
      .addAcceptedAttachment(ModItems.BIPOD)
      .addAcceptedAttachment(ModItems.SUPPRESSOR)
      .addAcceptedPaint(ModItems.DRAGON_PAINT)
      .addAcceptedPaint(ModItems.SCORCHED_PAINT)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .addAcceptedPaint(ModItems.ASMO_PAINT)
      .addAcceptedPaint(ModItems.MONARCH_PAINT)
      .build();

  public static final AbstractGunType<?> DMR = AimableGunType.builder()
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
      .addAnimation(AnimationType.SHOOT, RifleShootAnimation::new)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadDMR::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
      .setDefaultMagazine(ModItems.DMR_MAGAZINE)
      .addAcceptedAttachment(ModItems.LP_SCOPE)
      .addAcceptedAttachment(ModItems.HP_SCOPE)
      .addAcceptedAttachment(ModItems.BIPOD)
      .addAcceptedAttachment(ModItems.ACOG_SIGHT)
      .addAcceptedAttachment(ModItems.SUPPRESSOR)
      .addAcceptedPaint(ModItems.DIAMOND_PAINT)
      .addAcceptedPaint(ModItems.SCORCHED_PAINT)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .build();

  // ================================================================================
  // Shotguns
  // ================================================================================

  public static final AbstractGunType<?> TRENCHGUN = AimableGunType.builder()
      .setFireDelayMs(1200)
      .setDamage(2)
      .setBulletAmountToFire(8)
      .setReloadDurationTicks(20 * 1)
      .setAccuracy(0.7F)
      .setRecoil(9.0F)
      .setRange(25.0D)
      .setBoltAction(true)
      .addFireMode(FireMode.SEMI)
      .setShootSound(ModSoundEvents.TRENCHGUN_SHOOT)
      .setReloadSound(ModSoundEvents.SHOTGUN_RELOAD)
      .addAnimation(AnimationType.SHOOT, PistolShootAnimation::new)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadTrench::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
      .setDefaultMagazine(ModItems.TRENCHGUN_SHELLS)
      .addAcceptedAttachment(ModItems.TACTICAL_GRIP)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .build();

  public static final AbstractGunType<?> MOSSBERG = AimableGunType.builder()
      .setBulletAmountToFire(8)
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
      .addAnimation(AnimationType.SHOOT, PistolShootAnimation::new)
      .addAnimation(AnimationType.RELOAD, GunAnimationReloadMossberg::new)
      .addAnimation(AnimationType.INSPECT, GunAnimationInspectRifle::new)
      .setDefaultMagazine(ModItems.MOSSBERG_SHELLS)
      .addAcceptedAttachment(ModItems.TACTICAL_GRIP)
      .addAcceptedPaint(ModItems.MULTI_PAINT)
      .build();
}
