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

package com.craftingdead.core.network.message.play;

import java.util.function.Supplier;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.core.world.inventory.ModEquipmentSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class OpenStorageMessage {

  private final ModEquipmentSlot slot;

  public OpenStorageMessage(ModEquipmentSlot slot) {
    this.slot = slot;
  }

  public void encode(FriendlyByteBuf out) {
    out.writeEnum(this.slot);
  }

  public static OpenStorageMessage decode(FriendlyByteBuf in) {
    return new OpenStorageMessage(in.readEnum(ModEquipmentSlot.class));
  }

  public boolean handle(Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(
        () -> PlayerExtension.getOrThrow(ctx.get().getSender()).openStorage(this.slot));
    return true;
  }
}
