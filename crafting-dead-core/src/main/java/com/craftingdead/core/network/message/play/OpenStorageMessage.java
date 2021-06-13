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
import com.craftingdead.core.world.inventory.ModEquipmentSlotType;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class OpenStorageMessage {

  private final ModEquipmentSlotType slotType;

  public OpenStorageMessage(ModEquipmentSlotType slotType) {
    this.slotType = slotType;
  }

  public static void encode(OpenStorageMessage msg, PacketBuffer out) {
    out.writeEnum(msg.slotType);
  }

  public static OpenStorageMessage decode(PacketBuffer in) {
    return new OpenStorageMessage(in.readEnum(ModEquipmentSlotType.class));
  }

  public static boolean handle(OpenStorageMessage msg, Supplier<NetworkEvent.Context> ctx) {
    PlayerExtension.getExpected(ctx.get().getSender()).openStorage(msg.slotType);
    return true;
  }
}
