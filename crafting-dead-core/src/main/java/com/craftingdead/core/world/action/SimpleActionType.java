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

package com.craftingdead.core.world.action;

import java.util.function.Function;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class SimpleActionType<T extends Action> extends ForgeRegistryEntry<ActionType<?>>
    implements ActionType<T> {

  private final Function<LivingExtension<?, ?>, T> factory;
  private final boolean triggeredByClient;

  public SimpleActionType(Function<LivingExtension<?, ?>, T> factory, boolean triggeredByClient) {
    this.factory = factory;
    this.triggeredByClient = triggeredByClient;
  }

  @Override
  public void encode(T action, FriendlyByteBuf out) {}

  @Override
  public T decode(LivingExtension<?, ?> performer, FriendlyByteBuf in) {
    return this.factory.apply(performer);
  }

  @Override
  public boolean isTriggeredByClient() {
    return this.triggeredByClient;
  }

  @FunctionalInterface
  public interface Encoder<T extends Action> {

    void encode(T action, FriendlyByteBuf out);
  }

  @FunctionalInterface
  public interface Decoder<T extends Action> {

    T create(LivingExtension<?, ?> performer, FriendlyByteBuf in);
  }
}
