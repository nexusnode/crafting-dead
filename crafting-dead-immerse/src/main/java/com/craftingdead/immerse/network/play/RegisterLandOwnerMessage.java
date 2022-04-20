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

package com.craftingdead.immerse.network.play;

import java.util.function.Supplier;
import com.craftingdead.immerse.world.level.extension.LandOwner;
import com.craftingdead.immerse.world.level.extension.LevelExtension;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.network.NetworkEvent;

public record RegisterLandOwnerMessage(LandOwner landOwner) {

  public void encode(FriendlyByteBuf out) {
    out.writeWithCodec(LandOwner.NETWORK_CODEC, this.landOwner);
  }

  public static RegisterLandOwnerMessage decode(FriendlyByteBuf in) {
    return new RegisterLandOwnerMessage(in.readWithCodec(LandOwner.NETWORK_CODEC));
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    context.get().enqueueWork(() -> LogicalSidedProvider.CLIENTWORLD
        .get(context.get().getDirection().getReceptionSide())
        .map(LevelExtension::getOrThrow)
        .map(LevelExtension::getLandManager)
        .ifPresent(landManager -> landManager.registerLandOwner(this.landOwner)));
    return true;
  }
}
