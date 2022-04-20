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

package com.craftingdead.core.world.damagesource;

import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

public class KillFeedEntry {

  private final int killerEntityId;
  private final Component killerName;
  private final Component deadName;
  private final ItemStack weaponStack;
  private final Type type;

  public KillFeedEntry(int killerEntityId, Component killerName, Component deadName,
      ItemStack weaponStack, Type type) {
    this.killerEntityId = killerEntityId;
    this.killerName = killerName;
    this.deadName = deadName;
    this.weaponStack = weaponStack;
    this.type = type;
  }

  public int getKillerEntityId() {
    return this.killerEntityId;
  }

  public Component getKillerName() {
    return this.killerName;
  }

  public Component getDeadName() {
    return this.deadName;
  }

  public ItemStack getWeaponStack() {
    return this.weaponStack;
  }

  public Type getType() {
    return this.type;
  }

  public static enum Type {
    NONE, HEADSHOT, WALLBANG, WALLBANG_HEADSHOT;
  }

  public void encode(FriendlyByteBuf out) {
    out.writeVarInt(this.killerEntityId);
    out.writeComponent(this.killerName);
    out.writeComponent(this.deadName);
    out.writeItem(this.weaponStack);
    out.writeEnum(this.type);
  }

  public static KillFeedEntry decode(FriendlyByteBuf in) {
    return new KillFeedEntry(in.readVarInt(), in.readComponent(), in.readComponent(), in.readItem(),
        in.readEnum(KillFeedEntry.Type.class));
  }
}
