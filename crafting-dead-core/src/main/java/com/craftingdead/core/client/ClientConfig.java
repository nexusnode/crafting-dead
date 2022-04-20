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

package com.craftingdead.core.client;

import com.craftingdead.core.client.crosshair.CrosshairManager;
import com.craftingdead.core.client.gui.HitMarker;
import com.craftingdead.core.client.tutorial.ModTutorialSteps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

  public final ForgeConfigSpec.BooleanValue displayBlood;

  public final ForgeConfigSpec.EnumValue<HitMarker.Mode> hitMarkerMode;

  public final ForgeConfigSpec.BooleanValue killSoundEnabled;

  public final ForgeConfigSpec.ConfigValue<String> killSound;

  public final ForgeConfigSpec.ConfigValue<String> crosshair;

  public final ForgeConfigSpec.EnumValue<ModTutorialSteps> tutorialStep;

  public ClientConfig(ForgeConfigSpec.Builder builder) {
    builder.push("client");
    {
      this.displayBlood = builder
          .translation("options.craftingdead.client.display_blood")
          .define("displayBlood", true);
      this.hitMarkerMode = builder
          .translation("options.craftingdead.client.hit_marker_mode")
          .defineEnum("hitMarkerMode", HitMarker.Mode.HIT_AND_KILL);
      this.killSoundEnabled = builder
          .translation("options.craftingdead.client.kill_sound_enabled")
          .define("killSoundEnabled", true);
      this.killSound = builder
          .translation("options.craftingdead.client.kill_sound")
          .define("killSound", SoundEvents.TRIDENT_RETURN.getRegistryName().toString(),
              v -> v instanceof String && ResourceLocation.isValidResourceLocation((String) v));
      this.tutorialStep = builder
          .comment("Internal")
          .defineEnum("tutorialStep", ModTutorialSteps.OPEN_EQUIPMENT_MENU);
      this.crosshair = builder
          .translation("options.craftingdead.client.crosshair")
          .define("crosshair", CrosshairManager.DEFAULT_CROSSHAIR.toString(),
              o -> o instanceof String s && ResourceLocation.isValidResourceLocation(s));
    }
    builder.pop();
  }
}
