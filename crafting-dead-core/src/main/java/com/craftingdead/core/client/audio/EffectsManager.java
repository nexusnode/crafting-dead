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

package com.craftingdead.core.client.audio;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.EXTEfx;
import net.minecraft.client.audio.ChannelManager;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundEngine;

public class EffectsManager {

  private static final Logger logger = LogManager.getLogger();

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
    for (ChannelManager.Entry entry : this.soundEngine.playingSoundsChannel.values()) {
      entry.runOnSoundExecutor(source -> this.setDirectHighpass(source.id));
    }
  }

  public void setDirectHighpass(ISound sound) {
    ChannelManager.Entry entry = this.soundEngine.playingSoundsChannel.get(sound);
    if (entry != null) {
      entry.runOnSoundExecutor(source -> this.setDirectHighpass(source.id));
    }
  }

  public void setDirectHighpass(int source) {
    AL10.alSourcei(source, EXTEfx.AL_DIRECT_FILTER, this.highpassSend);
  }

  public void removeFilterForAll() {
    for (ChannelManager.Entry entry : this.soundEngine.playingSoundsChannel.values()) {
      entry.runOnSoundExecutor(source -> this.removeFilter(source.id));
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
