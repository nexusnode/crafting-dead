package com.craftingdead.immerse.world.action;

import java.util.Optional;
import com.craftingdead.core.world.action.Action;
import com.craftingdead.core.world.action.item.ItemActionType;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.immerse.world.level.extension.LevelExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;

public abstract class BlueprintActionType extends ItemActionType<BlueprintAction> {

  private final PlacementPredicate predicate;

  protected BlueprintActionType(Builder<?> builder) {
    super(builder);
    this.predicate = builder.predicate;
  }

  public PlacementPredicate getPlacementPredicate() {
    return this.predicate;
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

  public interface PlacementPredicate {

    PlacementPredicate ALWAYS = (living, blockPos) -> true;

    PlacementPredicate WITHIN_BASE =
        (living, blockPos) -> LevelExtension.getOrThrow(living.getLevel()).getLandManager()
            .getLandOwnerAt(blockPos)
            .map(owner -> owner.isAllowedToBuild(living.getEntity(), blockPos))
            .orElse(false);

    PlacementPredicate NOT_CLAIMED =
        (living, blockPos) -> LevelExtension.getOrThrow(living.getLevel()).getLandManager()
            .isLandClaimed(blockPos);

    boolean test(LivingExtension<?, ?> living, BlockPos blockPos);
  }

  public static abstract class Builder<SELF extends Builder<SELF>>
      extends ItemActionType.Builder<SELF> {

    private PlacementPredicate predicate = PlacementPredicate.ALWAYS;

    public SELF withinBase() {
      this.predicate = PlacementPredicate.WITHIN_BASE;
      return this.self();
    }

    public SELF notClaimed() {
      this.predicate = PlacementPredicate.NOT_CLAIMED;
      return this.self();
    }
  }
}
