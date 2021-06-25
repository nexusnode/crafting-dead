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
import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.network.NetworkUtil;
import com.craftingdead.core.world.gun.Gun;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.network.PacketBuffer;
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

  public void encode(PacketBuffer out) {
    out.writeVarInt(this.entityId);
    out.writeEnum(this.slot);
    out.writeVarInt(this.data.readableBytes());
    out.writeBytes(this.data);
  }

  public static SyncGunEquipmentSlotMessage decode(PacketBuffer in) {
    int entityId = in.readVarInt();
    EquipmentSlotType slot = in.readEnum(EquipmentSlotType.class);
    byte[] data = new byte[in.readVarInt()];
    in.readBytes(data);
    return new SyncGunEquipmentSlotMessage(entityId, slot,
        new PacketBuffer(Unpooled.wrappedBuffer(data)));
  }

  public boolean handle(Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> NetworkUtil.getEntity(ctx.get(), this.entityId, LivingEntity.class)
        .getItemBySlot(this.slot)
        .getCapability(Capabilities.GUN)
        .ifPresent(gun -> gun.decode(this.data)));
    return true;
  }
}
