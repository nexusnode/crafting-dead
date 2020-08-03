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

  private SoundEngine soundEngine;

  private int highPassSend;

  public void reload(SoundEngine soundEngine) {
    this.soundEngine = soundEngine;

    final long currentContext = ALC10.alcGetCurrentContext();
    final long currentDevice = ALC10.alcGetContextsDevice(currentContext);

    if (ALC10.alcIsExtensionPresent(currentDevice, "ALC_EXT_EFX")) {
      logger.info("EFX extension recognised");
    } else {
      logger.warn("EFX extension not found on current device");
      return;
    }

    this.highPassSend = EXTEfx.alGenFilters();
    EXTEfx.alFilteri(this.highPassSend, EXTEfx.AL_FILTER_TYPE, EXTEfx.AL_FILTER_HIGHPASS);
    checkLogError("Failed to generate high pass send");
  }

  public void setAllLevels(float highPassGain, float highPassCutoff) {
    for (ChannelManager.Entry entry : this.soundEngine.playingSoundsChannel.values()) {
      entry
          .runOnSoundExecutor(
              source -> this.setLevels(source.id, highPassGain, highPassCutoff));
    }
  }

  public void setLevels(ISound sound, float highPassGain, float highPassCutoff) {
    ChannelManager.Entry entry = this.soundEngine.playingSoundsChannel.get(sound);
    if (entry != null) {
      entry
          .runOnSoundExecutor(
              source -> this.setLevels(source.id, highPassGain, highPassCutoff));
    }
  }

  public void setLevels(int source, float highPassGain, float highPassCutoff) {
    EXTEfx.alFilterf(this.highPassSend, EXTEfx.AL_HIGHPASS_GAIN, highPassGain);
    EXTEfx.alFilterf(this.highPassSend, EXTEfx.AL_HIGHPASS_GAINLF, highPassCutoff);
    AL10.alSourcei(source, EXTEfx.AL_DIRECT_FILTER, this.highPassSend);
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
