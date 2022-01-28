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
