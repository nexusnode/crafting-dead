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

package com.craftingdead.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.craftingdead.core.world.item.gun.Gun;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

// TODO - temp until https://github.com/MinecraftForge/MinecraftForge/pull/7630 gets merged
@Mixin(FriendlyByteBuf.class)
public class PacketBufferMixin {

  @Inject(at = @At(value = "RETURN"),
      method = "Lnet/minecraft/network/FriendlyByteBuf;writeItemStack(Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/network/FriendlyByteBuf;",
      remap = false)
  private void writeItemStack(ItemStack itemStack, boolean limitedTag,
      CallbackInfoReturnable<FriendlyByteBuf> callbackInfo) {
    FriendlyByteBuf packetBuffer = (FriendlyByteBuf) (Object) this;
    Gun gun = itemStack.getCapability(Gun.CAPABILITY).orElse(null);

    if (gun == null) {
      packetBuffer.writeVarInt(0);
    } else {
      FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
      gun.encode(data, true);
      packetBuffer.writeVarInt(data.readableBytes());
      packetBuffer.writeBytes(data);
    }
  }

  @Inject(at = @At(value = "RETURN"), method = "readItem")
  private void readItem(CallbackInfoReturnable<ItemStack> callbackInfo) {
    FriendlyByteBuf packetBuffer = (FriendlyByteBuf) (Object) this;
    ItemStack itemStack = callbackInfo.getReturnValue();
    byte[] data = new byte[packetBuffer.readVarInt()];
    if (data.length > 0) {
      packetBuffer.readBytes(data);
      Gun gun = itemStack.getCapability(Gun.CAPABILITY).orElse(null);
      if (gun != null) {
        gun.decode(new FriendlyByteBuf(Unpooled.wrappedBuffer(data)));
      }
    }
  }
}
