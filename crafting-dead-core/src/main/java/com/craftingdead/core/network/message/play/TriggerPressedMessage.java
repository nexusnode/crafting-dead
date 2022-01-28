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
import com.craftingdead.core.network.NetworkUtil;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class TriggerPressedMessage {

  private final int entityId;
  private final boolean triggerPressed;

  public TriggerPressedMessage(int entityId, boolean triggerPressed) {
    this.entityId = entityId;
    this.triggerPressed = triggerPressed;
  }

  public void encode(FriendlyByteBuf out) {
    out.writeVarInt(this.entityId);
    out.writeBoolean(this.triggerPressed);
  }

  public static TriggerPressedMessage decode(FriendlyByteBuf in) {
    return new TriggerPressedMessage(in.readVarInt(), in.readBoolean());
  }

  public boolean handle(Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> NetworkUtil.getEntityOrSender(ctx.get(), this.entityId)
        .getCapability(LivingExtension.CAPABILITY)
        .ifPresent(extension -> extension.getMainHandGun()
            .ifPresent(gun -> gun.setTriggerPressed(extension, this.triggerPressed,
                ctx.get().getDirection().getReceptionSide().isServer()))));
    return true;
  }
}
