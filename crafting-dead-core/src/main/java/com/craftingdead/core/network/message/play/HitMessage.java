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

package com.craftingdead.core.network.message.play;

import java.util.function.Supplier;
import com.craftingdead.core.CraftingDead;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

public record HitMessage(Vec3 hitPos, boolean dead) {

  public void encode(FriendlyByteBuf out) {
    out.writeDouble(this.hitPos.x());
    out.writeDouble(this.hitPos.y());
    out.writeDouble(this.hitPos.z());
    out.writeBoolean(this.dead);
  }

  public static HitMessage decode(FriendlyByteBuf in) {
    return new HitMessage(new Vec3(in.readDouble(), in.readDouble(), in.readDouble()),
        in.readBoolean());
  }

  public boolean handle(Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(
        () -> CraftingDead.getInstance().getClientDist().handleHit(this.hitPos, this.dead));
    return true;
  }
}
