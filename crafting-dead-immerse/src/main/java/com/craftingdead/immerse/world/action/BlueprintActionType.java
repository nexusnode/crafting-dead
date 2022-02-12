package com.craftingdead.immerse.world.action;

import java.util.Optional;
import com.craftingdead.core.world.action.Action;
import com.craftingdead.core.world.action.item.ItemActionType;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;

public abstract class BlueprintActionType extends ItemActionType<BlueprintAction> {

  private final boolean baseRequired;

  protected BlueprintActionType(Builder<?> builder) {
    super(builder);
    this.baseRequired = builder.baseRequired;
  }

  public boolean isBaseRequired() {
    return this.baseRequired;
  }

  protected abstract BlueprintAction create(LivingExtension<?, ?> performer,
      BlockPlaceContext context);

  @Override
  public void encode(BlueprintAction action, FriendlyByteBuf out) {
    out.writeEnum(action.getHand());
    out.writeBlockHitResult(action.getContext().getHitResult());
  }

  @Override
  public BlueprintAction decode(LivingExtension<?, ?> performer, FriendlyByteBuf in) {
    var hand = in.readEnum(InteractionHand.class);
    var hitResult = in.readBlockHitResult();
    var context = new BlockPlaceContext(performer.getLevel(),
        performer.getEntity() instanceof Player player ? player : null, hand,
        performer.getEntity().getItemInHand(hand), hitResult);
    return this.create(performer, context);
  }

  @Override
  public Optional<Action> createBlockAction(LivingExtension<?, ?> performer, UseOnContext context) {
    return Optional.of(this.create(performer, new BlockPlaceContext(context)));
  }

  public static abstract class Builder<SELF extends Builder<SELF>>
      extends ItemActionType.Builder<SELF> {

    private boolean baseRequired = true;

    public SELF baseRequired(boolean baseRequired) {
      this.baseRequired = baseRequired;
      return this.self();
    }
  }
}
