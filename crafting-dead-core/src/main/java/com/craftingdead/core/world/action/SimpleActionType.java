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
