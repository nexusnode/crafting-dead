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

import java.util.Optional;
import java.util.function.Supplier;
import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.world.gun.Gun;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

public class SyncGunEquipmentSlotMessage {

  private final int entityId;
  private final EquipmentSlotType slot;
  private final PacketBuffer data;

  public SyncGunEquipmentSlotMessage(int entityId, EquipmentSlotType slot, Gun gun,
      boolean writeAll) {
    this(entityId, slot, new PacketBuffer(Unpooled.buffer()));
    gun.encode(this.data, writeAll);
  }

  public SyncGunEquipmentSlotMessage(int entityId, EquipmentSlotType slot, PacketBuffer data) {
    this.entityId = entityId;
    this.slot = slot;
    this.data = data;
  }

  public static void encode(SyncGunEquipmentSlotMessage message, PacketBuffer out) {
    out.writeVarInt(message.entityId);
    out.writeEnum(message.slot);
    out.writeVarInt(message.data.readableBytes());
    out.writeBytes(message.data);
  }

  public static SyncGunEquipmentSlotMessage decode(PacketBuffer in) {
    int entityId = in.readVarInt();
    EquipmentSlotType slot = in.readEnum(EquipmentSlotType.class);
    byte[] data = new byte[in.readVarInt()];
    in.readBytes(data);
    return new SyncGunEquipmentSlotMessage(entityId, slot,
        new PacketBuffer(Unpooled.wrappedBuffer(data)));
  }

  public static boolean handle(SyncGunEquipmentSlotMessage message,
      Supplier<NetworkEvent.Context> ctx) {
    Optional<World> world =
        LogicalSidedProvider.CLIENTWORLD.get(ctx.get().getDirection().getReceptionSide());
    world.map(w -> w.getEntity(message.entityId))
        .filter(e -> e instanceof LivingEntity)
        .map(e -> ((LivingEntity) e).getItemBySlot(message.slot))
        .flatMap(itemStack -> itemStack.getCapability(Capabilities.GUN).resolve())
        .ifPresent(gun -> gun.decode(message.data));
    return true;
  }
}
