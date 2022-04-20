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

package net.rocketpowered.connector.client.gui;

import java.util.List;
import javax.annotation.Nullable;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.FormattedCharSequence;

public class RocketToast implements Toast {

  private static final long DISPLAY_TIME = 5000L;
  private static final int MAX_LINE_SIZE = 200;
  private final Token token;
  private Component title;
  private List<FormattedCharSequence> messageLines;
  private long lastChanged;
  private boolean changed;
  private final int width;

  public RocketToast(Token token, Component title, @Nullable Component message) {
    this(token, title, nullToEmpty(message), 160);
  }

  public static RocketToast multiline(Font font, Token token, Component title, Component message) {
    var messageLines = font.split(message, MAX_LINE_SIZE);
    var width = Math.max(MAX_LINE_SIZE, messageLines.stream()
        .mapToInt(font::width)
        .max()
        .orElse(MAX_LINE_SIZE));
    return new RocketToast(token, title, messageLines, width + 30);
  }

  private RocketToast(Token token, Component title,
      List<FormattedCharSequence> messageLines, int width) {
    this.token = token;
    this.title = title;
    this.messageLines = messageLines;
    this.width = width;
  }

  private static List<FormattedCharSequence> nullToEmpty(@Nullable Component component) {
    return component == null ? List.of() : List.of(component.getVisualOrderText());
  }

  @Override
  public int width() {
    return this.width;
  }

  @SuppressWarnings("resource")
  @Override
  public Visibility render(PoseStack poseStack, ToastComponent toasts, long time) {
    if (this.changed) {
      this.lastChanged = time;
      this.changed = false;
    }

    RenderSystem.setShaderTexture(0, TEXTURE);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    final var width = this.width();
    int lineHeight = 12;
    if (width == 160 && this.messageLines.size() <= 1) {
      toasts.blit(poseStack, 0, 0, 0, 64, width, this.height());
    } else {
      int height = this.height() + Math.max(0, this.messageLines.size() - 1) * lineHeight;
      int l = 28;
      int i1 = Math.min(4, height - l);
      this.renderBackgroundRow(poseStack, toasts, width, 0, 0, l);

      for (int i = l; i < height - i1; i += 10) {
        this.renderBackgroundRow(poseStack, toasts, width, 16, i, Math.min(16, height - i - i1));
      }

      this.renderBackgroundRow(poseStack, toasts, width, 32 - i1, height - i1, i1);
    }

    if (this.messageLines == null) {
      toasts.getMinecraft().font.draw(poseStack, this.title, 18.0F, lineHeight, -256);
    } else {
      toasts.getMinecraft().font.draw(poseStack, this.title, 18.0F, 7.0F, -256);

      for (int i = 0; i < this.messageLines.size(); ++i) {
        toasts.getMinecraft().font.draw(poseStack, this.messageLines.get(i), 18.0F,
            (float) (18 + i * lineHeight), -1);
      }
    }

    return time - this.lastChanged < DISPLAY_TIME ? Visibility.SHOW : Visibility.HIDE;
  }

  private void renderBackgroundRow(PoseStack poseStack, ToastComponent toasts, int p_94839_,
      int p_94840_, int p_94841_, int p_94842_) {
    int i = p_94840_ == 0 ? 20 : 5;
    int j = Math.min(60, p_94839_ - i);
    toasts.blit(poseStack, 0, p_94841_, 0, 64 + p_94840_, i, p_94842_);

    for (int k = i; k < p_94839_ - j; k += 64) {
      toasts.blit(poseStack, k, p_94841_, 32, 64 + p_94840_, Math.min(64, p_94839_ - k - j),
          p_94842_);
    }

    toasts.blit(poseStack, p_94839_ - j, p_94841_, 160 - j, 64 + p_94840_, j, p_94842_);
  }

  public void reset(Component title, @Nullable Component message) {
    this.title = title;
    this.messageLines = nullToEmpty(message);
    this.changed = true;
  }

  @Override
  public Token getToken() {
    return this.token;
  }

  public static void add(ToastComponent toasts, Token token,
      Component title, @Nullable Component message) {
    toasts.addToast(new RocketToast(token, title, message));
  }

  public static void addOrUpdate(ToastComponent toasts, Token token,
      Component title, @Nullable Component message) {
    var toast = toasts.getToast(RocketToast.class, token);
    if (toast == null) {
      add(toasts, token, title, message);
    } else {
      toast.reset(title, message);
    }
  }

  public static void info(Minecraft minecraft, String message) {
    addOrUpdate(minecraft.getToasts(), Token.INFO,
        new TranslatableComponent("toasts.info"), new TextComponent(message));
  }

  public static void error(Minecraft minecraft, String message) {
    add(minecraft.getToasts(), Token.ERROR,
        new TranslatableComponent("toasts.error"), new TextComponent(message));
  }

  public enum Token {
    INFO,
    ERROR;
  }
}
