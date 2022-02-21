/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
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
