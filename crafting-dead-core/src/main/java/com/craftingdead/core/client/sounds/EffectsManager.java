/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.client.sounds;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.EXTEfx;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;

public class EffectsManager {

  private static final Logger logger = LogUtils.getLogger();

  private boolean initialisied = false;

  private final SoundEngine soundEngine;

  private int highpassSend;

  public EffectsManager(SoundEngine soundEngine) {
    this.soundEngine = soundEngine;
  }

  private void checkInit() {
    if (this.initialisied) {
      return;
    }

    final long currentContext = ALC10.alcGetCurrentContext();
    final long currentDevice = ALC10.alcGetContextsDevice(currentContext);

    if (ALC10.alcIsExtensionPresent(currentDevice, "ALC_EXT_EFX")) {
      logger.info("EFX extension recognised");
    } else {
      logger.warn("EFX extension not found on current device");
      return;
    }

    this.highpassSend = EXTEfx.alGenFilters();
    EXTEfx.alFilteri(this.highpassSend, EXTEfx.AL_FILTER_TYPE, EXTEfx.AL_FILTER_HIGHPASS);
    checkLogError("Failed to generate high-pass send");

    this.initialisied = true;
  }

  public void setHighpassLevels(float highpassGain, float highpassCutoff) {
    checkInit();
    EXTEfx.alFilterf(this.highpassSend, EXTEfx.AL_HIGHPASS_GAIN, highpassGain);
    EXTEfx.alFilterf(this.highpassSend, EXTEfx.AL_HIGHPASS_GAINLF, highpassCutoff);
  }

  public void setDirectHighpassForAll() {
    for (ChannelAccess.ChannelHandle entry : this.soundEngine.instanceToChannel.values()) {
      entry.execute(source -> this.setDirectHighpass(source.source));
    }
  }

  public void setDirectHighpass(SoundInstance sound) {
    ChannelAccess.ChannelHandle entry = this.soundEngine.instanceToChannel.get(sound);
    if (entry != null) {
      entry.execute(source -> this.setDirectHighpass(source.source));
    }
  }

  public void setDirectHighpass(int source) {
    AL10.alSourcei(source, EXTEfx.AL_DIRECT_FILTER, this.highpassSend);
  }

  public void removeFilterForAll() {
    for (ChannelAccess.ChannelHandle entry : this.soundEngine.instanceToChannel.values()) {
      entry.execute(source -> this.removeFilter(source.source));
    }
  }

  public void removeFilter(int source) {
    checkInit();
    AL10.alSourcei(source, EXTEfx.AL_DIRECT_FILTER, 0);
  }

  private static boolean checkLogError(final String errorMessage) {
    final int error = AL10.alGetError();
    if (error == AL10.AL_NO_ERROR) {
      return false;
    }

    String errorName;

    switch (error) {
      case AL10.AL_INVALID_NAME:
        errorName = "AL_INVALID_NAME";
        break;
      case AL10.AL_INVALID_ENUM:
        errorName = "AL_INVALID_ENUM";
        break;
      case AL10.AL_INVALID_VALUE:
        errorName = "AL_INVALID_VALUE";
        break;
      case AL10.AL_INVALID_OPERATION:
        errorName = "AL_INVALID_OPERATION";
        break;
      case AL10.AL_OUT_OF_MEMORY:
        errorName = "AL_OUT_OF_MEMORY";
        break;
      default:
        errorName = Integer.toString(error);
        break;
    }

    logger.warn(errorMessage + " OpenAL error " + errorName);
    return true;
  }
}
