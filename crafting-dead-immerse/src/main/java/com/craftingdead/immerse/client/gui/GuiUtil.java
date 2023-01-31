package com.craftingdead.immerse.client.gui;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvent;
import sm0keysa1m0n.bliss.StyledText;
import sm0keysa1m0n.bliss.minecraft.AdapterUtil;
import sm0keysa1m0n.bliss.view.View;
import sm0keysa1m0n.bliss.view.event.ViewEvent;

public class GuiUtil {

  public static StyledText translatable(String key, Object... args) {
    return AdapterUtil.createStyledText(new TranslatableComponent(key, args));
  }

  public static StyledText translatable(String key) {
    return AdapterUtil.createStyledText(new TranslatableComponent(key));
  }

  public static <T extends ViewEvent> void addEventSound(
      View view, Class<T> eventType, Supplier<SoundEvent> soundEvent) {
    view.eventBus().subscribe(eventType, __ -> playSound(soundEvent));
  }

  public static <T extends ViewEvent> void addEventSound(
      View view, Class<T> eventType, SoundEvent soundEvent) {
    view.eventBus().subscribe(eventType, __ -> playSound(soundEvent));
  }

  public static void playSound(Supplier<SoundEvent> soundEvent) {
    playSound(soundEvent.get());
  }

  public static void playSound(SoundEvent soundEvent) {
    Minecraft.getInstance().getSoundManager().play(
        SimpleSoundInstance.forUI(soundEvent, 1.0F));
  }
}
